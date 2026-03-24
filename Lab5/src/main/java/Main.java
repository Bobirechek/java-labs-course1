import builders.InteractiveHumanBeingBuilder;
import managers.*;
import commands.*;
import models.HumanBeing;
import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) {

        String file = System.getenv("LAB_FILE");

        if (file == null) {

            System.out.println("The LAB_FILE environment variable is not found.");

            return;
        }

        FileManager fileManager = new FileManager(file);

        CollectionManager collectionManager =
                new CollectionManager(fileManager.load());

        long curId = 1;
        for (HumanBeing h : collectionManager.getCollection()) {
            if (h.getId() > curId) {
                curId = h.getId();
            }
        }
        IdGenerator.setCurrencyId(curId);

        InteractiveHumanBeingBuilder builder =
                new InteractiveHumanBeingBuilder();

        CommandManager commandManager = new CommandManager();

        commandManager.register(new HelpCommand(commandManager));
        commandManager.register(new ShowCommand(collectionManager));
        commandManager.register(new AddCommand(collectionManager, builder));
        commandManager.register(new UpdateCommand(collectionManager, builder));
        commandManager.register(new SaveCommand(collectionManager, fileManager));
        commandManager.register(new ExecuteScriptCommand(commandManager));
        commandManager.register(new ClearCommand(collectionManager));
        commandManager.register(new CountBySoundtrackNameCommand(collectionManager));
        commandManager.register(new ExecuteScriptCommand(commandManager));
        commandManager.register(new ExitCommand());
        commandManager.register(new FilterContainsNameCommand(collectionManager));
        commandManager.register(new InfoCommand(collectionManager));
        commandManager.register(new PrintFieldDescendingImpactSpeedCommand(collectionManager));
        commandManager.register(new RemoveByIdCommand(collectionManager, fileManager));
        commandManager.register(new RemoveGreaterCommand(collectionManager, fileManager));
        commandManager.register(new ReorderCommand(collectionManager));
        commandManager.register(new SaveCommand(collectionManager, fileManager));
        commandManager.register(new ShowCommand(collectionManager));
        commandManager.register(new SortCommand(collectionManager));
        commandManager.register(new UpdateCommand(collectionManager, builder));

        try {
            while (true) {
                
                    System.out.print("> ");

                    String line = ScriptManager.getScanner().nextLine();

                    String[] parts = line.split(" ", 2);

                    String name = parts[0];

                    String arg = parts.length > 1 ? parts[1] : null;

                    commandManager.execute(name, arg);
            }
        } catch (NoSuchElementException e) {
            System.out.println("Bye bye");
        }
    }
}