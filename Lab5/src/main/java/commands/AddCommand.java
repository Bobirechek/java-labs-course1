package commands;

import builders.HumanBeingBuilder;
import managers.CollectionManager;
import models.HumanBeing;

public class AddCommand extends AbstractCommand {

    private final CollectionManager manager;
    private final HumanBeingBuilder builder;

    public AddCommand(CollectionManager manager, HumanBeingBuilder builder) {

        super("add", "add {element} : add a new item to the collection");

        this.manager = manager;
        this.builder = builder;
    }

    @Override
    public void execute(String arg) {

        HumanBeing human = builder.build();

        manager.add(human);

        System.out.println("Element added successfully.");
    }
}