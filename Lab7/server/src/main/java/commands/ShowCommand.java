package commands;

import managers.CollectionManager;

public class ShowCommand extends AbstractCommand {

    private final CollectionManager manager;

    public ShowCommand(CollectionManager manager) {
        super("show", "display all elements of the collection");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        return manager.showAll();
    }
}