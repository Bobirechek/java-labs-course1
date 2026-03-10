package commands;

import managers.CollectionManager;

public class ReorderCommand implements Command{

    private CollectionManager manager;

    public ReorderCommand(CollectionManager m){
        manager=m;
    }

    public String getName() {
        return "reorder";
    }

    public void execute(String arg){
        manager.reorder();
    }

    @Override
    public String getDescription() {
        return "reorder : отсортировать коллекцию в порядке, обратном нынешнему";
    }   
}