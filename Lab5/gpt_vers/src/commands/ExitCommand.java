package commands;

public class ExitCommand implements Command{

    public String getName() {
        return "exit";
    }

    public void execute(String arg) {
        System.out.println("Выход");

        System.exit(0);
    }

    @Override
    public String getDescription() {
        return "exit : завершить программу без сохранения в файл";
    }
}