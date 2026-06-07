import managers.*;
import common.Command;
import common.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerMain {
    private static final Logger logger = LogManager.getLogger(ServerMain.class);

    public static void main(String[] args) {
        try {
            logger.info("DB: {}  User: {}", managers.DatabaseConfig.getUrl(), managers.DatabaseConfig.USER);

            // Инициализация БД
            DatabaseManager dbManager = new DatabaseManager();
            dbManager.initSchema();
            logger.info("Database schema initialized.");

            // Загрузка коллекции из БД
            List<models.HumanBeing> loaded = dbManager.loadAll();
            CollectionManager collectionManager = new CollectionManager(loaded);
            logger.info("Loaded {} elements from DB into memory.", loaded.size());

            
            CommandManager commandManager = new CommandManager(collectionManager, dbManager);

            // Сеть
            UdpServerModule network = new UdpServerModule(5555);
            logger.info("Server is running on port 5555.");

            ExecutorService processPool = Executors.newCachedThreadPool();

            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while (true) {

                final UdpServerModule.RawPacket rawPacket = network.receiveRaw();

                if (rawPacket != null) {

                    new Thread(() -> {
                        try {
                            final Command command = UdpServerModule.deserializeCommand(rawPacket.data);
                            logger.info("Deserialized command: {} from {}", command.getType(), rawPacket.clientAddress);

                            processPool.submit(() -> {
                                try {
                                    final Response response = commandManager.execute(command);

                                    new Thread(() -> {
                                        try {
                                            network.sendResponse(response, rawPacket.clientAddress);
                                        } catch (Exception e) {
                                            logger.error("Error sending response to {}: {}",
                                                    rawPacket.clientAddress, e.getMessage());
                                        }
                                    }).start();

                                } catch (Exception e) {
                                    logger.error("Error processing command: {}", e.getMessage());
                                }
                            });

                        } catch (Exception e) {
                            logger.error("Error deserializing packet from {}: {}",
                                    rawPacket.clientAddress, e.getMessage());
                        }
                    }).start();
                }

                if (console.ready()) {
                    String line = console.readLine();
                    if (line == null) continue;
                    line = line.trim();

                    if (line.equalsIgnoreCase("exit")) {
                        logger.info("Exit command received from console.");
                        processPool.shutdown();
                        System.exit(0);

                    } else if (line.equalsIgnoreCase("info")) {
                        logger.info(collectionManager.info());

                    } else if (!line.isEmpty()) {
                        logger.info("Unknown server console command: '{}'", line);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Server error", e);
        }
    }
}