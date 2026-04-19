package commands;

import managers.CollectionManager;

public class InfoCommand extends AbstractCommand {

    private CollectionManager manager;

    public InfoCommand(CollectionManager manager) {
        super("info", "output information about the collection to the standard output stream");
        this.manager = manager;
    }

    public String getName() {
        return "info";
    }

    @Override
    public String execute(Object arg) {
        manager.info();
        return "info";
    }

    @Override
    public String getDescription() {
        return "output information about the collection to the standard output stream";
    }
}