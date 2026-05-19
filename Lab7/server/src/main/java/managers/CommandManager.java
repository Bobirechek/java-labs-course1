package managers;

import commands.*;
import common.Command;
import common.Response;
import models.CommandType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    private static final Logger logger = LogManager.getLogger(CommandManager.class);

    private final Map<CommandType, AbstractCommand> commands = new HashMap<>();
    private final DatabaseManager dbManager;

    public CommandManager(CollectionManager collectionManager, DatabaseManager dbManager) {
        this.dbManager = dbManager;

        commands.put(CommandType.SHOW,   new ShowCommand(collectionManager));
        commands.put(CommandType.INFO,   new InfoCommand(collectionManager));
        commands.put(CommandType.SORT,   new SortCommand(collectionManager));
        commands.put(CommandType.REORDER, new ReorderCommand(collectionManager));
        commands.put(CommandType.COUNT_BY_SOUNDTRACK_NAME,
                new CountBySoundtrackNameCommand(collectionManager));
        commands.put(CommandType.FILTER_CONTAINS_NAME,
                new FilterContainsNameCommand(collectionManager));
        commands.put(CommandType.PRINT_FIELD_DESCENDING_IMPACT_SPEED,
                new PrintFieldDescendingImpactSpeedCommand(collectionManager));

        commands.put(CommandType.ADD,
                new AddCommand(collectionManager, dbManager));
        commands.put(CommandType.UPDATE,
                new UpdateCommand(collectionManager, dbManager));
        commands.put(CommandType.REMOVE_BY_ID,
                new RemoveByIdCommand(collectionManager, dbManager));
        commands.put(CommandType.REMOVE_GREATER,
                new RemoveGreaterCommand(collectionManager, dbManager));
        commands.put(CommandType.CLEAR,
                new ClearCommand(collectionManager, dbManager));

        commands.put(CommandType.HELP,           new HelpCommand(this));
        commands.put(CommandType.EXECUTE_SCRIPT,  new ExecuteScriptCommand());
        commands.put(CommandType.EXIT,            new ExitCommand());
    }

    public Response execute(Command command) {
        CommandType type = command.getType();
        String login       = command.getLogin();
        String passwordHash = command.getPasswordHash();

        if (type == CommandType.REGISTER) {
            return handleRegister(login, passwordHash);
        }

        if (type == CommandType.LOGIN) {
            return handleLogin(login, passwordHash);
        }

        if (login == null || login.isBlank() || passwordHash == null || passwordHash.isBlank()) {
            return new Response("Not authenticated. Please login.", null);
        }

        try {
            if (!dbManager.authenticateUser(login, passwordHash)) {
                logger.warn("Auth failed for user '{}'", login);
                return new Response("Authentication failed. Wrong login or password.", null);
            }
        } catch (Exception e) {
            logger.error("DB error during auth", e);
            return new Response("Server error during authentication.", null);
        }

        AbstractCommand cmd = commands.get(type);
        if (cmd == null) {
            logger.warn("Unknown command: {}", type);
            return new Response("Unknown command: " + type, null);
        }

        Object arg = command.getArgument();
        Object obj = command.getObject();

        Object finalArg;
        if (arg != null && obj != null) {
            finalArg = new Object[]{arg, obj};
        } else if (obj != null) {
            finalArg = obj;
        } else {
            finalArg = arg;
        }

        logger.info("Executing command {} for user '{}'", type, login);
        String result = cmd.execute(finalArg, login);
        logger.info("Command {} done for '{}'", type, login);

        if ("EXIT_SIGNAL".equals(result)) {
            return new Response("Goodbye, " + login + "!", null, true);
        }

        return new Response(result, null);
    }


    private Response handleRegister(String login, String passwordHash) {
        if (login == null || login.isBlank()) {
            return new Response("Login cannot be empty.", null);
        }
        if (passwordHash == null || passwordHash.isBlank()) {
            return new Response("Password cannot be empty.", null);
        }
        try {
            boolean ok = dbManager.registerUser(login, passwordHash);
            if (ok) {
                logger.info("New user registered: '{}'", login);
                return new Response("Registration successful! Welcome, " + login + "!", null);
            } else {
                return new Response("Login '" + login + "' is already taken. Choose another.", null);
            }
        } catch (Exception e) {
            logger.error("Registration error", e);
            return new Response("Server error during registration: " + e.getMessage(), null);
        }
    }

    private Response handleLogin(String login, String passwordHash) {
        if (login == null || passwordHash == null) {
            return new Response("Login and password are required.", null);
        }
        try {
            boolean ok = dbManager.authenticateUser(login, passwordHash);
            if (ok) {
                logger.info("User '{}' logged in", login);
                return new Response("Login successful! Welcome back, " + login + "!", null);
            } else {
                return new Response("Wrong login or password.", null);
            }
        } catch (Exception e) {
            logger.error("Login error", e);
            return new Response("Server error during login: " + e.getMessage(), null);
        }
    }

    public String getHelp() {
        List<String> cmds = new ArrayList<>();

        commands.forEach((type, cmd) ->
                cmds.add(cmd.getName() + " : " + cmd.getDescription())
        );

        cmds.add("logout : log out of the system");

        Collections.sort(cmds);

        return String.join("\n", cmds);
    }
}