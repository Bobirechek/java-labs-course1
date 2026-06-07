package commands;

import managers.CollectionManager;

public class FilterContainsNameCommand extends AbstractCommand {

    private final CollectionManager manager;

    public FilterContainsNameCommand(CollectionManager manager) {
        super("filter_contains_name name",
                "display elements whose name contains the specified substring");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        if (!(arg instanceof String)) {
            return "Invalid argument: name must be a String.";
        }
        return manager.filterContainsName((String) arg);
    }
}