package commands;

import managers.CommandManager;

public class HelpCommand extends AbstractCommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {

        super("help", "help : вывести справку по доступным командам");

        this.manager = manager;
    }

    @Override
    public void execute(String arg) {

        manager.printHelp();

    }
}