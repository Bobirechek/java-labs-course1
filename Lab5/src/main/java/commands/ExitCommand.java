package commands;

public class ExitCommand implements Command{

    public String getName() {
        return "exit";
    }

    public void execute(String arg) {
        System.out.println("Exit");

        System.exit(0);
    }

    @Override
    public String getDescription() {
        return "exit : exiting the program without saving to a file";
    }
}