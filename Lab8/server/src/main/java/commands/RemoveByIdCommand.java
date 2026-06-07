package commands;

import managers.CollectionManager;
import managers.DatabaseManager;

public class RemoveByIdCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final DatabaseManager dbManager;

    public RemoveByIdCommand(CollectionManager collectionManager, DatabaseManager dbManager) {
        super("remove_by_id id", "remove element from the collection by its id (only your own)");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        if (!(arg instanceof Long)) {
            return "Invalid argument: id must be a number.";
        }

        long id = (Long) arg;

        try {
            if (!dbManager.existsById(id)) {
                return "Element with id=" + id + " not found.";
            }

            boolean removed = dbManager.removeById(id, userLogin);

            if (!removed) {
                return "Access denied: element id=" + id + " does not belong to you.";
            }

            collectionManager.removeById(id);

            return "Element id=" + id + " removed successfully.";

        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }
}