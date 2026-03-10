package commands;

import managers.CollectionManager;

public class ShowCommand extends AbstractCommand {

    private final CollectionManager manager;

    public ShowCommand(CollectionManager manager) {

        super("show", "show : output all the elements of the collection in a string representation to the standard output stream");

        this.manager = manager;
    }

    @Override
    public void execute(String arg) {

        if (manager.getCollection().isEmpty()) {
            System.out.println("Collection is empty");
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