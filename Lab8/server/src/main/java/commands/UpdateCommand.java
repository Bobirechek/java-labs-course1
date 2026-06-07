package commands;

import managers.CollectionManager;
import managers.DatabaseManager;
import models.HumanBeing;

public class UpdateCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final DatabaseManager dbManager;

    public UpdateCommand(CollectionManager collectionManager, DatabaseManager dbManager) {
        super("update id {element}", "update element by id (only your own)");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        if (!(arg instanceof Object[])) return "Invalid arguments.";

        Object[] args = (Object[]) arg;
        Long id = (Long) args[0];

        try {
            HumanBeing existing = collectionManager.getById(id);
            if (existing == null) {
                return "Element with id=" + id + " not found.";
            }

            HumanBeing newHuman = (HumanBeing) args[1];

            newHuman.setId(id);
            newHuman.setCreationDate(existing.getCreationDate());
            newHuman.setOwnerLogin(existing.getOwnerLogin());

            boolean updated = dbManager.updateHumanBeing(newHuman, userLogin);

            if (!updated) {
                return "Access denied: element id=" + id + " does not belong to you.";
            }

            collectionManager.update(newHuman);

            return "Element id=" + id + " updated successfully.";

        } catch (Exception e) {
            return "Database error: " + e.getMessage();
        }
    }
}