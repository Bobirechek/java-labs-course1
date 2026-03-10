package commands;

import builders.InteractiveHumanBeingBuilder;
import managers.CollectionManager;
import models.HumanBeing;

import java.util.Scanner;

public class RemoveGreaterCommand extends AbstractCommand {

    private final CollectionManager manager;

    public RemoveGreaterCommand(CollectionManager manager) {

        super("remove_greater",
                "remove_greater {element} : delete all items from the collection that exceed the specified size.");

        this.manager = manager;
    }

    public void execute(String arg) {

        Scanner scanner = new Scanner(System.in);

        InteractiveHumanBeingBuilder builder =
                new InteractiveHumanBeingBuilder(scanner);

        HumanBeing reference = builder.build();

        manager.getCollection()
                .removeIf(h -> h.compareTo(reference) > 0);

        System.out.println("The elements are deleted.");
    }
}