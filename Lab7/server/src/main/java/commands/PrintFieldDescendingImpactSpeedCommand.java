package commands;

import managers.CollectionManager;

public class PrintFieldDescendingImpactSpeedCommand extends AbstractCommand {

    private final CollectionManager manager;

    public PrintFieldDescendingImpactSpeedCommand(CollectionManager manager) {
        super("print_field_descending_impact_speed",
                "print impactSpeed values of all elements in descending order");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        return manager.printImpactSpeedDescending();
    }
}