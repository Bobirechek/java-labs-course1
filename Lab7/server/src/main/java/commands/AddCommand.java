package commands;

import managers.CollectionManager;
import managers.DatabaseManager;
import models.HumanBeing;

import java.time.LocalDateTime;

public class AddCommand extends AbstractCommand {

    private final CollectionManager collectionManager;
    private final DatabaseManager dbManager;

    public AddCommand(CollectionManager collectionManager, DatabaseManager dbManager) {
        super("add {element}", "add a new element to the collection");
        this.collectionManager = collectionManager;
        this.dbManager = dbManager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        if (!(arg instanceof HumanBeing)) {
            return "Invalid argument: expected HumanBeing object.";
        }

        HumanBeing human = (HumanBeing) arg;

        human.setCreationDate(LocalDateTime.now());

        try {
            long generatedId = dbManager.addHumanBeing(human, userLogin);

            human.setId(generatedId);
            human.setOwnerLogin(userLogin);
            collectionManager.add(human);

            return "Element added successfully (id=" + generatedId + ").";

        } catch (Exception e) {
            return "Database error while adding element: " + e.getMessage();
        }
    }
}