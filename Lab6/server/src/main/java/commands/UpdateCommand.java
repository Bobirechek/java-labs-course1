package commands;

import managers.CollectionManager;
import models.HumanBeing;

public class UpdateCommand extends AbstractCommand {

    private final CollectionManager manager;

    public UpdateCommand(CollectionManager manager) {

        super("update id {element}", "update the value of a collection item by id");

        this.manager = manager;
    }

    @Override
    public String execute(Object arg) {

        if (!(arg instanceof Object[])) return "Invalid arguments";
        
        Object[] args = (Object[]) arg;
        Long id = (Long) args[0];
        HumanBeing newHuman = (HumanBeing) args[1];

        HumanBeing oldHuman = manager.getById(id);

        if (oldHuman == null) return "Element with this ID not found - " + id;

        newHuman.setId(id);
        newHuman.setCreationDate(oldHuman.getCreationDate());
        manager.update(newHuman);

        return "Successfully updated";
    }
}