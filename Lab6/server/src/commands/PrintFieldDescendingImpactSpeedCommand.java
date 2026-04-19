package commands;

import managers.CollectionManager;

import java.util.Comparator;

public class PrintFieldDescendingImpactSpeedCommand extends AbstractCommand {

    private CollectionManager manager;

    public PrintFieldDescendingImpactSpeedCommand(CollectionManager manager) {
        super("print_field_descending_impact_speed ", "вывести значения поля impactSpeed всех элементов в порядке убывания");
        this.manager = manager;
    }

    public String getName() {
        return "print_field_descending_impact_speed";
    }

    @Override
    public String execute(Object arg) {

        manager.getCollection()
                .stream()
                .map(h -> h.getImpactSpeed())
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    
        return "PrintFieldDescendingImpactSpeedCommand";
    }

    @Override
    public String getDescription() {
        return "print_field_descending_impact_speed : print the values of the impactSpeed field of all elements in descending order";
    }
}