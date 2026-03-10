package commands;

import builders.HumanBeingBuilder;
import managers.CollectionManager;
import models.HumanBeing;

public class UpdateCommand extends AbstractCommand {

    private final CollectionManager manager;
    private final HumanBeingBuilder builder;

    public UpdateCommand(CollectionManager manager, HumanBeingBuilder builder) {

        super("update", "update id {element} : update the value of a collection item by id");

        this.manager = manager;
        this.builder = builder;
    }

    @Override
    public void execute(String arg) {

        if (arg == null) {

            System.out.println("Error: you need to specify the id.");

            return;
        }

        long id;

        try {

            id = Long.parseLong(arg);

        } catch (NumberFormatException e) {

            System.out.println("Error: the id must be a number.");

            return;
        }

        HumanBeing newHuman = builder.build();

        manager.removeById(id);

        manager.add(newHuman);

        System.out.println("Element update.");
    }
}