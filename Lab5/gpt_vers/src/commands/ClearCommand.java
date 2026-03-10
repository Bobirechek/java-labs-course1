package commands;

import managers.CollectionManager;

public class ClearCommand implements Command{

    private CollectionManager manager;

    public ClearCommand(CollectionManager manager){
        this.manager = manager;
    }

    public String getName() {
        return "clear";
    }

    public void execute(String arg){
        manager.clear();
    }

    @Override
    public String getDescription() {
        return "clear : очистить коллекцию";
    }
}