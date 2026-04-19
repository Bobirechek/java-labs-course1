package commands;

import managers.CollectionManager;
import managers.FileManager;
import managers.JsonManager;
import models.HumanBeing;

public class RemoveGreaterCommand extends AbstractCommand {

    private final CollectionManager manager;
    private final FileManager fileManager;

    public RemoveGreaterCommand(CollectionManager manager, FileManager fileManager) {

        super("remove_greater {element}",
                "delete all items from the collection that exceed the specified size.");

        this.manager = manager;
        this.fileManager = fileManager;
    }

    @Override
    public String execute(Object arg) {
        if (!(arg instanceof models.HumanBeing)) {
            return "Invalid argument";
        }

        models.HumanBeing reference = (models.HumanBeing) arg;

        manager.removeGreater(reference);
        return "Removed greater elements";
    }
}