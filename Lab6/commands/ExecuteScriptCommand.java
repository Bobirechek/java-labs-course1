package commands;

import managers.CommandManager;
import managers.ScriptManager;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class ExecuteScriptCommand extends AbstractCommand {

    private final CommandManager manager;
    private static final Set<String> executing = new HashSet<>();

    public ExecuteScriptCommand(CommandManager manager) {
        super("execute_script", "execute_script file_name : execute script");
        this.manager = manager;
    }

    @Override
    public void execute(String arg) {

        if (arg == null) {
            System.out.println("Specify file name.");
            return;
        }

        if (executing.contains(arg)) {
            System.out.println("Recursion detected.");
            return;
        }

        try {

            executing.add(arg);

            ScriptManager.pushScript(arg);

            while (ScriptManager.getScanner().hasNextLine()) {

                String line = ScriptManager.getScanner().nextLine();

                if (line.isEmpty())
                    continue;

                String[] parts = line.split(" ", 2);

                String name = parts[0];
                String argument = parts.length > 1 ? parts[1] : null;

                System.out.println("> " + name + " " + argument);
                try {
                    Thread.sleep(1200);
                }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                manager.execute(name, argument);
            }

        } catch (FileNotFoundException e) {

            System.out.println("Script file not found.");

        } catch (Exception e) {

            System.out.println("Script error: " + e.getMessage());

        } finally {

            ScriptManager.popScript();
            executing.remove(arg);
        }
    }
}