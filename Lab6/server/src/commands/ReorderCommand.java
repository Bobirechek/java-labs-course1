package commands;

import managers.CollectionManager;

public class ReorderCommand extends AbstractCommand {

    private CollectionManager manager;

    public ReorderCommand(CollectionManager manager) {
        super("reorder", "отсортировать коллекцию в порядке, обратном нынешнему");
        this.manager = manager;
    }

    public String getName() {
        return "reorder";
    }

    @Override
    public String execute(Object arg) {
        manager.reorder();
        return "reorder";
    }

    @Override
    public String getDescription() {
        return "reorder : sort the collection in the reverse order of the current one";
    }
}