package commands;

import managers.CollectionManager;
import models.HumanBeing;

public class RemoveGreaterCommand extends AbstractCommand {

    private final CollectionManager manager;

    public RemoveGreaterCommand(CollectionManager manager) {

        super("remove_greater {element}",
                "delete all items from the collection that exceed the specified size.");

        this.manager = manager;
    }

    @Override
    public String execute(Object arg) {
        if (!(arg instanceof HumanBeing)) {
            return "Invalid argument";
        }

        HumanBeing reference = (HumanBeing) arg;

        manager.removeGreater(reference);
        return "Removed greater elements";
    }
}