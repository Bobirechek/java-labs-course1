package commands;

import managers.CollectionManager;

public class CountBySoundtrackNameCommand extends AbstractCommand {

    private final CollectionManager manager;

    public CountBySoundtrackNameCommand(CollectionManager manager) {

        super("count_by_soundtrack_name",
                "count_by_soundtrack_name soundtrackName : print the number of elements with the value of the soundtrackName field equal to the specified one");

        this.manager = manager;
    }

    public void execute(String arg) {

        if (arg == null || arg.isEmpty()) {

            System.out.println("Error: It is necessary to specify the soundtrackName.");

            return;
        }

        long count = manager.getCollection()
                .stream()
                .filter(h -> arg.equals(h.getSoundtrackName()))
                .count();

        System.out.println("Number of elements: " + count);
    }
}