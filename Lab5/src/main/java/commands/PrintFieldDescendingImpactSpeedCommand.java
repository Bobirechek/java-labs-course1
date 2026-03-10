package commands;

import managers.CollectionManager;

import java.util.Comparator;

public class PrintFieldDescendingImpactSpeedCommand implements Command{

    private CollectionManager manager;

    public PrintFieldDescendingImpactSpeedCommand(CollectionManager m){
        manager=m;
    }

    public String getName() {
        return "print_field_descending_impact_speed";
    }

    public void execute(String arg){

        manager.getCollection()
                .stream()
                .map(h->h.getImpactSpeed())
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);
    }

    @Override
    public String getDescription() {
        return "print_field_descending_impact_speed : print the values of the impactSpeed field of all elements in descending order";
    }
}