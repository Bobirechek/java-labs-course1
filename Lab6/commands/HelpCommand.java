package commands;

import managers.CommandManager;

public class HelpCommand extends AbstractCommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {

        super("help", "help : output help for available commands");

        this.manager = manager;
    }

    @Override
    public void execute(String arg) {

        manager.printHelp();

    }
}