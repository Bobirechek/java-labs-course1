package commands;

import managers.CollectionManager;

public class RemoveByIdCommand extends AbstractCommand {

    private final CollectionManager manager;

    public RemoveByIdCommand(CollectionManager manager) {

        super("remove_by_id", "remove_by_id id : удалить элемент из коллекции по его id");

        this.manager = manager;
    }

    public void execute(String arg) {

        if (arg == null) {

            System.out.println("Ошибка: необходимо указать id.");

            return;
        }

        try {

            long id = Long.parseLong(arg);

            boolean removed = manager.getCollection()
                    .removeIf(h -> h.getId().equals(id));

            if (!removed) {

                System.out.println("Ошибка: элемент с таким id не найден.");

            }

        } catch (NumberFormatException e) {

            System.out.println("Ошибка: id должен быть числом.");

        }
    }
}