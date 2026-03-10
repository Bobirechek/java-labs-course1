package commands;

import builders.HumanBeingBuilder;
import managers.CollectionManager;
import models.HumanBeing;

public class UpdateCommand extends AbstractCommand {

    private final CollectionManager manager;
    private final HumanBeingBuilder builder;

    public UpdateCommand(CollectionManager manager, HumanBeingBuilder builder) {

        super("update", "update id {element} : обновить значение элемента коллекции по id");

        this.manager = manager;
        this.builder = builder;
    }

    @Override
    public void execute(String arg) {

        if (arg == null) {

            System.out.println("Ошибка: нужно указать id.");

            return;
        }

        long id;

        try {

            id = Long.parseLong(arg);

        } catch (NumberFormatException e) {

            System.out.println("Ошибка: id должен быть числом.");

            return;
        }

        HumanBeing newHuman = builder.build();

        manager.removeById(id);

        manager.add(newHuman);

        System.out.println("Элемент обновлен.");
    }
}