package commands;

import managers.CollectionManager;

public class ReorderCommand extends AbstractCommand {

    private final CollectionManager manager;

    public ReorderCommand(CollectionManager manager) {
        super("reorder", "sort your elements in the collection in reverse (descending) order");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        if (!manager.hasElementsByOwner(userLogin)) {
            return "You have no elements in the collection to reorder.";
        }

        manager.reorderByOwner(userLogin);
        return "Your elements have been reordered.";
    }
}