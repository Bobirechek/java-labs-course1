package commands;

import managers.CollectionManager;

public class InfoCommand implements Command{

    private CollectionManager manager;

    public InfoCommand(CollectionManager m){
        manager=m;
    }

    public String getName() {
        return "info";
    }

    public void execute(String arg){
        manager.info();
    }

    @Override
    public String getDescription() {
        return "info : вывести в стандартный поток вывода информацию о коллекции";
    }
}