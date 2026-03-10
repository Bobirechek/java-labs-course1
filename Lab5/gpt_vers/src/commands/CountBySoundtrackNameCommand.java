package commands;

import managers.CollectionManager;

public class CountBySoundtrackNameCommand extends AbstractCommand {

    private final CollectionManager manager;

    public CountBySoundtrackNameCommand(CollectionManager manager) {

        super("count_by_soundtrack_name",
                "count_by_soundtrack_name soundtrackName : вывести количество элементов, значение поля soundtrackName которых равно заданному");

        this.manager = manager;
    }

    public void execute(String arg) {

        if (arg == null || arg.isEmpty()) {

            System.out.println("Ошибка: необходимо указать soundtrackName.");

            return;
        }

        long count = manager.getCollection()
                .stream()
                .filter(h -> arg.equals(h.getSoundtrackName()))
                .count();

        System.out.println("Количество элементов: " + count);
    }
}