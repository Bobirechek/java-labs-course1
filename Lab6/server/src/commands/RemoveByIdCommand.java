package commands;

import managers.CollectionManager;
import managers.FileManager;

public class RemoveByIdCommand extends AbstractCommand {

    private final CollectionManager manager;
    private final FileManager fileManager;

    public RemoveByIdCommand(CollectionManager manager, FileManager fileManager) {

        super("remove_by_id id", "delete an item from the collection by its id");

        this.manager = manager;
        this.fileManager = fileManager;
    }

    @Override
    public String execute(Object arg) {

        if (!(arg instanceof Long)) {
            return "Invalid Id: The Id must be Long";
        }

        try {
            long id = (Long) arg;

            boolean removed = manager.removeById(id);

            return removed ? "HumanBeing (id = " + id + ") is removed" : "Id not found";

        } catch (Exception e) {
            return "Invalid id format";
        }
    }
}