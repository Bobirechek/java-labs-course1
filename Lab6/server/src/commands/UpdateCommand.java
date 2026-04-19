package commands;

import managers.CollectionManager;
import managers.JsonManager;
import models.HumanBeing;

public class UpdateCommand extends AbstractCommand {

    private final CollectionManager manager;

    public UpdateCommand(CollectionManager manager) {

        super("update", "update id {element} : update the value of a collection item by id");

        this.manager = manager;
    }

    @Override
    public String execute(Object arg) {

        if (arg == null) return "No data";

        HumanBeing newHuman = JsonManager.parseHuman((String) arg);

        manager.update(newHuman);

        return "Updated";
    }
    
    // @Override
    // public void execute(String arg) {

    //     if (arg == null) {

    //         System.out.println("Error: you need to specify the id.");

    //         return;
    //     }

    //     String[] parts = arg.split(" ", 2);

    //     long id;

    //     try {

    //         id = Long.parseLong(parts[0]);

    //     } catch (NumberFormatException e) {

    //         System.out.println("Error: the id must be a number.");

    //         return;
    //     }

    //     HumanBeing newHuman;

    //     if (parts.length > 1 && parts[1].startsWith("{")) {

    //         newHuman = JsonManager.parseHuman(parts[1]);

    //     } else {

    //         newHuman = (HumanBeing) arg;
    //     }

    //     HumanBeing oldHuman = manager.getCollection().stream()
    //             .filter(h -> h.getId() == id)
    //             .findFirst()
    //             .orElse(null);

    //     if (oldHuman == null) {

    //         System.out.println("Element with this id not found.");

    //         return;
    //     }

    //     if (newHuman == null) {
    //         System.out.println("Element was not updated.");
    //         return;
    //     }

    //     manager.removeById(id);

    //     newHuman.setId(id);
    //     newHuman.setCreationDate(oldHuman.getCreationDate());

    //     manager.add(newHuman);

    //     System.out.println("Element successfully update");
    // }
}