package commands;

import managers.CollectionManager;
import managers.FileManager;

public class SaveCommand extends AbstractCommand {

    private final CollectionManager manager;
    private final FileManager fileManager;

    public SaveCommand(CollectionManager manager, FileManager fileManager) {

        super("save", "save the collection to a file");

        this.manager = manager;
        this.fileManager = fileManager;
    }

    @Override
    public String execute(Object arg) {

        fileManager.save(manager.getCollection());

        return "Collection save.";
    }
}