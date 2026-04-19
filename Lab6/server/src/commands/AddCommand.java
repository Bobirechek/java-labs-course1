package commands;

import managers.CollectionManager;
import managers.JsonManager;
import models.HumanBeing;

public class AddCommand extends AbstractCommand {

    private final CollectionManager manager;

    public AddCommand(CollectionManager manager) {
        super("add", "добавить элемент");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg) {

        if (arg == null)
            return "No data";

        HumanBeing human = JsonManager.parseHuman((String) arg);

        manager.add(human);

        return "Added";
    }
}