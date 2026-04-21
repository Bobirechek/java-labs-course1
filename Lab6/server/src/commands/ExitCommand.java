package commands;

import managers.CollectionManager;
import managers.FileManager;
import common.Response;

public class ExitCommand extends AbstractCommand {

    private final CollectionManager manager;
    private final FileManager fileManager;

    public ExitCommand(CollectionManager manager, FileManager fileManager) {
        super("exit", "save collection and exit client");
        this.manager = manager;
        this.fileManager = fileManager;
    }

    @Override
    public String execute(Object arg) {
        return "EXIT_SIGNAL";
    }

    // // 🔥 добавим отдельный метод для Response
    // public Response executeWithResponse() {
    //     fileManager.save(manager.getCollection());
    //     return new Response("Client stopped by server", null, true);
    // }
}