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

            System.out.println("Неизвестная команда.");

            return;
        }

        command.execute(arg);
    }

    public void printHelp() {

        for (Command command : commands.values()) {

            System.out.println(
                    command.getDescription()
            );
        }
    }
}