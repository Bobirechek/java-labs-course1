package commands;

import managers.CollectionManager;

public class SortCommand extends AbstractCommand {

    private final CollectionManager manager;

    public SortCommand(CollectionManager manager) {
        super("sort", "sort your elements in the collection in natural (ascending) order");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        if (!manager.hasElementsByOwner(userLogin)) {
            return "You have no elements in the collection to sort.";
        }

        manager.sortByOwner(userLogin);
        return "Your elements have been sorted.";
    }
}