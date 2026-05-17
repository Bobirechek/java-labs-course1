package commands;

public class ExitCommand extends AbstractCommand {

    public ExitCommand() {
        super("exit", "save collection and exit client");
    }

    @Override
    public String execute(Object arg) {
        return "EXIT_SIGNAL";
    }
}