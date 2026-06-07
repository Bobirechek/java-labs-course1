package commands;

import managers.CommandManager;

public class HelpCommand extends AbstractCommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        super("help", "display help for available commands");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        return manager.getHelp();
    }
}