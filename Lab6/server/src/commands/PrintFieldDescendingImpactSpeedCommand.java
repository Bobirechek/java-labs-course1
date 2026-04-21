package commands;

import managers.CollectionManager;

import java.util.Comparator;

public class PrintFieldDescendingImpactSpeedCommand extends AbstractCommand {

    private CollectionManager manager;

    public PrintFieldDescendingImpactSpeedCommand(CollectionManager manager) {
        super("print_field_descending_impact_speed ", "print the values of the impactSpeed field of all elements in descending order");
        this.manager = manager;
    }

    public String getName() {
        return "print_field_descending_impact_speed";
    }

    @Override
    public String execute(Object arg) {
        StringBuilder sb = new StringBuilder();

        manager.getCollection()
                .stream()
                .map(h -> h.getImpactSpeed())
                .sorted(Comparator.reverseOrder())
                .forEach(speed -> sb.append(speed).append("\n"));

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        
        return sb.toString();
    }

    @Override
    public String getDescription() {
        return "print the values of the impactSpeed field of all elements in descending order";
    }
}