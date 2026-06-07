package commands;

import managers.CollectionManager;

public class CountBySoundtrackNameCommand extends AbstractCommand {

    private final CollectionManager manager;

    public CountBySoundtrackNameCommand(CollectionManager manager) {
        super("count_by_soundtrack_name soundtrackName",
                "count elements with the given soundtrackName value");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        if (!(arg instanceof String) || ((String) arg).isEmpty()) {
            return "Invalid argument: soundtrackName must be a non-empty String.";
        }
        int count = manager.countBySoundtrackName((String) arg);
        return "Count: " + count;
    }
}