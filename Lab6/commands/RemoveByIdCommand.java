package commands;

import managers.CollectionManager;
import managers.FileManager;

public class RemoveByIdCommand extends AbstractCommand {

    private final CollectionManager manager;
    private final FileManager fileManager;

    public RemoveByIdCommand(CollectionManager manager, FileManager fileManager) {

        super("remove_by_id", "remove_by_id id : delete an item from the collection by its id");

        this.manager = manager;
        this.fileManager = fileManager;
    }

    public void execute(String arg) {

        if (arg == null) {

            System.out.println("Error: ID must be specified.");

            return;
        }

        try {

            long id = Long.parseLong(arg);

            boolean removed = manager.getCollection()
                    .removeIf(h -> h.getId().equals(id));
            fileManager.save(manager.getCollection());
            
            System.out.println("Element (Id = " + id + ") delete");

            if (!removed) {

                System.out.println("Error: the element with this id was not found..");

            }

        } catch (NumberFormatException e) {

            System.out.println("Error: The id must be a number.");

        }
    }
}