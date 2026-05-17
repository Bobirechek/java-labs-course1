package managers;

import commands.*;
import common.Command;
import common.Response;
import models.CommandType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private static final Logger logger =
        LogManager.getLogger(CommandManager.class);

    private final Map<CommandType, AbstractCommand> commands = new HashMap<>();

    public CommandManager(CollectionManager collectionManager, FileManager fileManager) {

        commands.put(CommandType.ADD, new AddCommand(collectionManager));
        commands.put(CommandType.CLEAR, new ClearCommand(collectionManager));
        commands.put(CommandType.INFO, new InfoCommand(collectionManager));
        commands.put(CommandType.SHOW, new ShowCommand(collectionManager));
        commands.put(CommandType.SORT, new SortCommand(collectionManager));
        commands.put(CommandType.REORDER, new ReorderCommand(collectionManager));

        commands.put(CommandType.HELP, new HelpCommand(this));
        commands.put(CommandType.EXECUTE_SCRIPT, new ExecuteScriptCommand());
        commands.put(CommandType.EXIT, new ExitCommand());

        commands.put(CommandType.REMOVE_BY_ID,
                new RemoveByIdCommand(collectionManager));

        commands.put(CommandType.REMOVE_GREATER,
                new RemoveGreaterCommand(collectionManager));

        commands.put(CommandType.COUNT_BY_SOUNDTRACK_NAME,
                new CountBySoundtrackNameCommand(collectionManager));

        commands.put(CommandType.FILTER_CONTAINS_NAME,
                new FilterContainsNameCommand(collectionManager));

        commands.put(CommandType.PRINT_FIELD_DESCENDING_IMPACT_SPEED,
                new PrintFieldDescendingImpactSpeedCommand(collectionManager));

        commands.put(CommandType.SAVE,
                new SaveCommand(collectionManager, fileManager));

        commands.put(CommandType.UPDATE,
                new UpdateCommand(collectionManager));
    }

    public Response execute(Command command) {
        AbstractCommand cmd = commands.get(command.getType());

        if (cmd == null) {
            logger.warn("Unknown command received: {}", command.getType());

            return new Response("Unknown command", null);
        }

        Object finalArg;

        Object arg = command.getArgument();
        Object objectArg = command.getObject();

        if (arg != null && objectArg != null) {
            finalArg = new Object[] {arg, objectArg};
        } else if (arg != null) {
            finalArg = arg;
        } else {
            finalArg = objectArg;
        }
        logger.info("Executing the command {}", command.getType());

        String result = cmd.execute(finalArg);
        if (result.equals("EXIT_SIGNAL")) {
            return new Response("Client stopped by server", null, true);    
        }

        logger.info("The {} command is executed", command.getType());

        return new Response(result, null);
    }

    public String getHelp() {
        StringBuilder sb = new StringBuilder();
        for (AbstractCommand cmd : commands.values()) {
            sb.append(cmd.getName())
              .append(" : ")
              .append(cmd.getDescription())
              .append("\n");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }
}