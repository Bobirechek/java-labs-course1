package commands;

import managers.CollectionManager;

public class ClearCommand extends AbstractCommand {

    private CollectionManager manager;

    public ClearCommand(CollectionManager manager) {
        super("clear", "очистить коллекцию");
        this.manager = manager;
    }

    public String getName() {
        return "clear";
    }

    @Override
    public String execute(Object arg) {
        manager.clear();
        return "Collection cleared";
    }

    @Override
    public String getDescription() {
        return "clear : clear collection";
    }
}