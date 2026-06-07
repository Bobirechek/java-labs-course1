package commands;

import managers.CollectionManager;

public class InfoCommand extends AbstractCommand {

    private final CollectionManager manager;

    public InfoCommand(CollectionManager manager) {
        super("info", "display information about the collection");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        return "Collection information:\n" + manager.info();
    }
}