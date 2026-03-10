package commands;

import managers.CollectionManager;
import managers.FileManager;

public class SaveCommand extends AbstractCommand {

    private final CollectionManager manager;
    private final FileManager fileManager;

    public SaveCommand(CollectionManager manager, FileManager fileManager) {

        super("save", "save : сохранить коллекцию в файл");

        this.manager = manager;
        this.fileManager = fileManager;
    }

    @Override
    public void execute(String arg) {

        fileManager.save(manager.getCollection());

        System.out.println("Коллекция сохранена.");
    }
}