package commands;

import managers.CollectionManager;

public class CountBySoundtrackNameCommand extends AbstractCommand {

    private final CollectionManager manager;

    public CountBySoundtrackNameCommand(CollectionManager manager) {

        super("count_by_soundtrack_name soundtrackName",
                "print the number of elements with the value of the soundtrackName field equal to the specified one");

        this.manager = manager;
    }

    @Override
    public String execute(Object arg) {

        if (!(arg instanceof String) || ((String) arg).isEmpty()) {
            return "Invalid argument";
        }

        String name = (String) arg;

        int count = manager.countBySoundtrackName(name);

        return String.valueOf(count);
    }
}