package commands;

import managers.CollectionManager;
import managers.DatabaseManager;

public class ClearCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final DatabaseManager dbManager;

    public ClearCommand(CollectionManager collectionManager, DatabaseManager dbManager) {
        super("clear", "remove all your elements from the collection");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        try {
            int removed = dbManager.clearByOwner(userLogin);

            if (removed == 0) {
                return "You have no elements to remove.";
            }

            collectionManager.clearByOwner(userLogin);

            return "Removed " + removed + " of your element(s).";

        } catch (Exception e) {
            return "Database error while clearing: " + e.getMessage();
        }
    }
}