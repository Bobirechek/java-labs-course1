package commands;

import builders.InteractiveHumanBeingBuilder;
import managers.CollectionManager;
import managers.FileManager;
import managers.JsonManager;
import models.HumanBeing;

import java.util.Scanner;

public class RemoveGreaterCommand extends AbstractCommand {

    private final CollectionManager manager;
    private final FileManager fileManager;

    public RemoveGreaterCommand(CollectionManager manager, FileManager fileManager) {

        super("remove_greater",
                "remove_greater {element} : delete all items from the collection that exceed the specified size.");

        this.manager = manager;
        this.fileManager = fileManager;
    }

    public void execute(String arg) {

        HumanBeing reference;

        if(arg != null && arg.startsWith("{")){

            reference = JsonManager.parseHuman(arg);

        } else {

            Scanner scanner = new Scanner(System.in);
            InteractiveHumanBeingBuilder builder =
                    new InteractiveHumanBeingBuilder(scanner);

            reference = builder.build();
        }

        if (reference == null) {
            System.out.println("Elements was not deleted");
            return;
        }

        manager.getCollection()
                .removeIf(h -> h.compareTo(reference) > 0);
        fileManager.save(manager.getCollection());

        System.out.println("The elements are deleted.");
    }
}