import managers.*;
import common.*;
import models.HumanBeing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ServerMain {
    private static final Logger logger =
            LogManager.getLogger(ServerMain.class);

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            String file = System.getenv("LAB_FILE");
            if (file == null) file = "data.json";

            logger.info("Starting the server");

            UdpServerModule network = new UdpServerModule(8888);
            logger.info("The server is running on port 8888");

            FileManager fileManager = new FileManager(file);
            CollectionManager collectionManager = new CollectionManager(fileManager.load());
            logger.info("Collection is uploaded");

            CommandManager commandManager = new CommandManager(collectionManager, fileManager);

            long curId = 0;
            for (HumanBeing h : collectionManager.getCollection()) {
                if (h.getId() > curId) {
                    curId = h.getId();
                }
            }
            IdGenerator.setCurrencyId(curId);
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Server is shutting down");
                fileManager.save(collectionManager.getCollection());
                logger.info("Collection saved");
            }));
            
            logger.info("Server is running");
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                Command command = network.readCommand();
                if (command != null) {
                    logger.info("Command {} has been recieved", command.getType());

                    Response response = commandManager.execute(command);

                    if (response.getData() instanceof Collection<?>) {
                        List<HumanBeing> sorted = ((Collection<HumanBeing>) response.getData()).stream()
                                .sorted().collect(Collectors.toList());
                        response.setData(sorted);
                    }
                    network.sendResponse(response);

                    logger.info("The response has been sent to the client");
                }

                if (console.ready()) {
                    String line = console.readLine().trim();

                    if (line.equalsIgnoreCase("save")) {
                        fileManager.save(collectionManager.getCollection());
                        logger.info("Collection saved");

                    } else if (line.equalsIgnoreCase("exit")) {
                        fileManager.save(collectionManager.getCollection());
                        logger.info("Collection saved");
                        logger.info("Exit by command");
                        System.exit(0);
                    }
                }
            }
        } catch (Exception e) { 
            logger.error("Server error: \n", e);
        }
    }
}