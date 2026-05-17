package commands;

import managers.CollectionManager;

public class SortCommand extends AbstractCommand {

    private CollectionManager manager;

    public SortCommand(CollectionManager manager) {
        super("sort", "sort the collection in a natural order");
        this.manager = manager;
    }

    public String getName() {
        return "sort";
    }

    @Override
    public String execute(Object arg) {
        manager.sort();

        return "Collection is sorted";
    }

    @Override
    public String getDescription() {
        return "sort the collection in a natural order";
    }
}