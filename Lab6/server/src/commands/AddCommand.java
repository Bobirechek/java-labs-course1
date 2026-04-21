package commands;

import managers.CollectionManager;
import managers.IdGenerator;
import models.HumanBeing;

public class AddCommand extends AbstractCommand {

    private final CollectionManager manager;

    public AddCommand(CollectionManager manager) {
        super("add {element} :", "add a new item to the collection");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg) {
        if (!(arg instanceof HumanBeing)) {
            return "Invalid argument";
        }

        HumanBeing human = (HumanBeing) arg;
        human.setId(IdGenerator.generateId());

        manager.add(human);

        return "Human added successfully";
    }
}