package commands;

import managers.CollectionManager;

public class FilterContainsNameCommand implements Command{

    private CollectionManager manager;

    public FilterContainsNameCommand(CollectionManager m){
        manager=m;
    }

    public String getName(){return "filter_contains_name";}

    public void execute(String arg){

        manager.getCollection()
                .stream()
                .filter(h->h.getName().contains(arg))
                .forEach(System.out::println);
    }

    @Override
    public String getDescription() {
        return "filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку";
    }
}