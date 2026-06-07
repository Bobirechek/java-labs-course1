// package commands;

// import managers.CollectionManager;

// public class ShowCommand extends AbstractCommand {

//     private final CollectionManager manager;

//     public ShowCommand(CollectionManager manager) {
//         super("show", "display all elements of the collection");
//         this.manager = manager;
//     }

//     @Override
//     public String execute(Object arg, String userLogin) {
//         return manager.showAll();
//     }
// }

package commands;

import managers.CollectionManager;
import models.HumanBeing;

import java.util.List;

public class ShowCommand extends AbstractCommand {

    private final CollectionManager manager;

    public ShowCommand(CollectionManager manager) {
        super("show", "display all elements of the collection");
        this.manager = manager;
    }

    @Override
    public String execute(Object arg, String userLogin) {
        return "SHOW_COLLECTION";
    }

    public List<HumanBeing> getCollection() {
        return manager.getCollectionSnapshot();
    }
}