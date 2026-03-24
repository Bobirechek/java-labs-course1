package commands;

import builders.HumanBeingBuilder;
import managers.CollectionManager;
import managers.JsonManager;
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

        HumanBeing human;

        if (arg != null && arg.startsWith("{")) {

            human = JsonManager.parseHuman(arg);

            if (human == null) {
                System.out.println("Element was not added.");
                return;
            }

        } else {

            human = builder.build();

            if (human == null) {
                System.out.println("Input error. Element not added.");
                return;
            }
        }

        manager.add(human);

        System.out.println("Element successfully added.");
    }
}