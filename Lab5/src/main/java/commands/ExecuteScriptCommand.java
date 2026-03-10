package commands;

import managers.CommandManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class ExecuteScriptCommand extends AbstractCommand {

    private final CommandManager manager;

    private static final Set<String> executing = new HashSet<>();

    public ExecuteScriptCommand(CommandManager manager) {

        super("execute_script", "execute_script file_name : read and execute the script from the specified file");

        this.manager = manager;
    }

    @Override
    public void execute(String arg) {

        if (arg == null) {

            System.out.println("You need to specify the file name.");

            return;
        }

        if (executing.contains(arg)) {

            System.out.println("Script recursion error.");

            return;
        }

        executing.add(arg);

        try (Scanner scanner = new Scanner(new File(arg))) {

            while (scanner.hasNextLine()) {

                String line = scanner.nextLine();

                String[] parts = line.split(" ", 2);

                String name = parts[0];

                String argument = parts.length > 1 ? parts[1] : null;
                
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                
                if (!name.equals("execute_script") && argument != arg) {
                    System.out.println("> " + name + " " + (argument != null
                                                            ? argument
                                                            : ""));
                }
            
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                manager.execute(name, argument);
            }

        } catch (FileNotFoundException e) {

            System.out.println("File not found.");
        }

        executing.remove(arg);
    }
}