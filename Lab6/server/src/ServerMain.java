import managers.*;
import common.*;
import models.HumanBeing;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ServerMain {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            String file = System.getenv("LAB_FILE");
            if (file == null) file = "data.json";

            UdpServerModule network = new UdpServerModule(5555);
            FileManager fileManager = new FileManager(file);
            CollectionManager collectionManager = new CollectionManager(fileManager.load());
            CommandManager commandManager = new CommandManager(collectionManager, fileManager);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                fileManager.save(collectionManager.getCollection());
            }));
            
            System.out.println("Сервер на связи...");
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                Command command = network.readCommand();
                if (command != null) {
                    // Обработка
                    Response response = commandManager.execute(command);

                    // STREAM API Сортировка (Требование)
                    if (response.getData() instanceof Collection<?>) {
                        List<HumanBeing> sorted = ((Collection<HumanBeing>) response.getData()).stream()
                                .sorted().collect(Collectors.toList());
                        response.setData(sorted);
                    }
                    network.sendResponse(response);
                }

                if (console.ready()) {
                    String line = console.readLine().trim();

                    if (line.equalsIgnoreCase("save")) {
                        fileManager.save(collectionManager.getCollection());
                        System.out.println("Collection saved");

                    } else if (line.equalsIgnoreCase("exit")) {
                        fileManager.save(collectionManager.getCollection());
                        System.out.println("Collection saved");
                        System.out.println("Exit by command");
                        System.exit(0);
                    }
                }
            }
        } catch (Exception e) { e.printStackTrace();}
    }
}