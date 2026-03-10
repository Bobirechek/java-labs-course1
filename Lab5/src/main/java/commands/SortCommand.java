package commands;

import managers.CollectionManager;

public class SortCommand implements Command{

    private CollectionManager manager;

    public SortCommand(CollectionManager m){
        manager=m;
    }

    public String getName() {
        return "sort";
    }

    public void execute(String arg){
        manager.sort();
    }

    @Override
    public String getDescription() {
        return "sort : sort the collection in a natural order";
    }
}