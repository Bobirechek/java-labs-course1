package managers;

import commands.*;
import common.Command;
import common.Response;
import models.CommandType;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final Map<CommandType, AbstractCommand> commands = new HashMap<>();

    public CommandManager(CollectionManager collectionManager, FileManager fileManager) {

        commands.put(CommandType.ADD, new AddCommand(collectionManager));
        commands.put(CommandType.CLEAR, new ClearCommand(collectionManager));
        commands.put(CommandType.INFO, new InfoCommand(collectionManager));
        commands.put(CommandType.SHOW, new ShowCommand(collectionManager));
        commands.put(CommandType.SORT, new SortCommand(collectionManager));
        commands.put(CommandType.REORDER, new ReorderCommand(collectionManager));

        commands.put(CommandType.HELP, new HelpCommand(this));

        commands.put(CommandType.REMOVE_BY_ID,
                new RemoveByIdCommand(collectionManager, fileManager));

        commands.put(CommandType.REMOVE_GREATER,
                new RemoveGreaterCommand(collectionManager, fileManager));

        commands.put(CommandType.COUNT_BY_SOUNDTRACK_NAME,
                new CountBySoundtrackNameCommand(collectionManager));

        commands.put(CommandType.FILTER_CONTAINS_NAME,
                new FilterContainsNameCommand(collectionManager));

        commands.put(CommandType.PRINT_FIELD_DESCENDING_IMPACT_SPEED,
                new PrintFieldDescendingImpactSpeedCommand(collectionManager));

        commands.put(CommandType.SAVE,
                new SaveCommand(collectionManager, fileManager));

        commands.put(CommandType.EXIT, new ExitCommand());
    }

    public Response execute(Command command) {

        AbstractCommand cmd = commands.get(command.getType());

        if (cmd == null) {
            return new Response("Unknown command", null);
        }

        String result = cmd.execute(command.getArgument());

        System.out.println("Executing: " + command.getType() + " arg=" + command.getArgument());
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
        return sb.toString();
    }
}