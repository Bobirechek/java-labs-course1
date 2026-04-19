package commands;

public class ExitCommand extends AbstractCommand {

    public ExitCommand() {
        super("exit", "exiting the program without saving to a file");
    }

    public String getName() {
        return "exit";
    }

    @Override
    public String execute(Object arg) {
        System.out.println("Exit");
        
        System.exit(0);
        return "exit";
    }

    @Override
    public String getDescription() {
        return "exiting the program without saving to a file";
    }
}