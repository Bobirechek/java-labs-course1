package commands;

import managers.CollectionManager;

public class FilterContainsNameCommand extends AbstractCommand {

    private CollectionManager manager;

    public FilterContainsNameCommand(CollectionManager manager) {
        super("clear", "очистить коллекцию");
        this.manager = manager;
    }

    public String getName() {
        return "filter_contains_name";
    }

    @Override
    public String execute(Object arg) {

        if (!(arg instanceof String)) {
            return "Invalid argument";
        }

        String name = (String) arg;

        return manager.filterContainsName(name);
    }

    @Override
    public String getDescription() {
        return "filter_contains_name name : output the elements whose name field value contains the specified substring";
    }
}