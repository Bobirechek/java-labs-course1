package managers;

import commands.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();

    public void register(Command command) {

        commands.put(command.getName(), command);

    }

    public void execute(String name, String arg) {

        Command command = commands.get(name);

        if (command == null) {

            System.out.println("Unknown command.");

            return;
        }

        command.execute(arg);
    }

    public void printHelp() {

        commands.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> 
                    System.out.println(entry.getValue().getDescription())
                );
    }
}