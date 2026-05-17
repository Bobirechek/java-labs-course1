package commands;

import managers.CollectionManager;

public class ShowCommand extends AbstractCommand {

    private final CollectionManager manager;

    public ShowCommand(CollectionManager manager) {

        super("show",
                "output all the elements of the collection in a string representation to the standard output stream");

        this.manager = manager;
    }

    @Override
    public String execute(Object arg) {
        if (manager.getCollection().isEmpty()) {
            return "Collection is empty";
        } else {
            return manager.getCollection()
                            .stream()
                            .map(Object::toString)
                            .collect(java.util.stream.Collectors.joining("\n" + "=".repeat(45) + "\n"));
        }
    }
}