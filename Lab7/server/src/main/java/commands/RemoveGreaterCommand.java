package commands;

import managers.CollectionManager;
import managers.DatabaseManager;
import models.HumanBeing;

public class RemoveGreaterCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final DatabaseManager dbManager;

    public RemoveGreaterCommand(CollectionManager collectionManager, DatabaseManager dbManager) {
        super("remove_greater {element}", "remove all your elements greater than the specified one");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        if (!(arg instanceof HumanBeing)) {
            return "Invalid argument: expected HumanBeing object.";
        }

        HumanBeing ref = (HumanBeing) arg;

        try {
            int removed = dbManager.removeGreaterByOwner(ref, userLogin);

            if (removed == 0) {
                return "No elements found that are greater than the specified one (among your elements).";
            }

            collectionManager.removeGreaterByOwner(ref, userLogin);

            return "Removed " + removed + " element(s) greater than the specified one.";

        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }
}