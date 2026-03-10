package commands;

import builders.HumanBeingBuilderInteractive;
import managers.CollectionManager;
import models.HumanBeing;

import java.util.Scanner;

public class RemoveGreaterCommand extends AbstractCommand {

    private final CollectionManager manager;

    public RemoveGreaterCommand(CollectionManager manager) {

        super("remove_greater",
                "remove_greater {element} : удалить из коллекции все элементы, превышающие заданный");

        this.manager = manager;
    }

    public void execute(String arg) {

        Scanner scanner = new Scanner(System.in);

        HumanBeingBuilderInteractive builder =
                new HumanBeingBuilderInteractive(scanner);

        HumanBeing reference = builder.build();

        manager.getCollection()
                .removeIf(h -> h.compareTo(reference) > 0);

        System.out.println("Элементы удалены.");
    }
}