package commands;

public class ExitCommand extends AbstractCommand {

    public ExitCommand() {
        super("exit", "exit the client");
    }

    @Override
    public String execute(Object arg, String userLogin) {
        return "EXIT_SIGNAL";
    }
}