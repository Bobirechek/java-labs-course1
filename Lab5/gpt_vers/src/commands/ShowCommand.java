package commands;

import managers.CollectionManager;

public class ShowCommand extends AbstractCommand {

    private final CollectionManager manager;

    public ShowCommand(CollectionManager manager) {

        super("show", "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении");

        this.manager = manager;
    }

    @Override
    public void execute(String arg) {

        if (manager.getCollection().isEmpty()) {
            System.out.println("Коллекция пуста");
        } else {
            System.out.println(
                manager.getCollection()
                    .stream()
                    .map(Object::toString)
                    .collect(java.util.stream.Collectors.joining("\n"+ "=".repeat(50) + "\n"))
            );    
        }
    }
}