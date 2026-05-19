import builders.InteractiveHumanBeingBuilder;
import common.Command;
import common.Response;
import managers.JsonManager;
import models.CommandType;
import models.HumanBeing;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    private static final Stack<String> scriptStack = new Stack<>();
    private static boolean isExitingNormally = false;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!isExitingNormally) {
                System.out.println("I predict that");
                System.out.println("Bye Bye");
            }
        }));

        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            socket.setSoTimeout(2500);
            InetAddress address = InetAddress.getByName("localhost");
            int port = 8888;

            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) break;
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                processInput(input, socket, address, port, scanner, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processInput(String input, DatagramSocket socket, InetAddress address, int port, Scanner scanner, boolean isFromScript) {
        String[] parts = input.split("\\s+", 2);
        String cmdName = parts[0].toUpperCase();
        String arg = (parts.length > 1) ? parts[1].trim() : null;

        if (isFromScript) {
            System.out.println("> " + parts[0] + " " + (arg != null ? " " + arg : ""));
            
            try {
                Thread.sleep(1200);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
            
        try {
            CommandType type = CommandType.valueOf(cmdName);

            if (type == CommandType.EXECUTE_SCRIPT) {
                handleExecuteScript(arg, socket, address, port);
                return;
            }

            Command command = prepareCommand(type, arg, scanner);
            if (command != null) {
                sendAndReceive(command, socket, address, port);
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Unknown command: " + cmdName);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static Command prepareCommand(CommandType type, String arg, Scanner scanner) {
        try {
            if (type == CommandType.ADD || type == CommandType.REMOVE_GREATER) {
                HumanBeing human = parseHumanArgument(arg, scanner);
                return new Command(type, null, human);
            } 
            
            if (type == CommandType.UPDATE) {
                if (arg == null || arg.isEmpty()) {
                    System.out.println("Error: ID is required");
                    return null;
                }
                
                String[] updateParts = arg.split("\\s+", 2);
                Long id = Long.parseLong(updateParts[0]);
                String jsonPart = (updateParts.length > 1) ? updateParts[1].trim() : null;
                
                HumanBeing human = parseHumanArgument(jsonPart, scanner);
                return new Command(type, id, human);
            }

            if (arg != null && !arg.isEmpty()) {
                try {
                    return new Command(type, Long.parseLong(arg), null);
                } catch (NumberFormatException e) {
                    return new Command(type, arg, null);
                }
            }

            return new Command(type, null, null);
        } catch (Exception e) {
            System.out.println("Data validation error: " + e.getMessage());
            return null;
        }
    }

    private static HumanBeing parseHumanArgument(String arg, Scanner scanner) {
        if (arg != null && arg.startsWith("{")) {
            String json = arg;
            if (json.startsWith("\"") && json.endsWith("\"")) {
                json = json.substring(1, json.length() - 1).replace("\\\"", "\"");
            }
            return JsonManager.convert(json, HumanBeing.class);
        }
        return new InteractiveHumanBeingBuilder(scanner).build();
    }

    private static void handleExecuteScript(String fileName, DatagramSocket socket, InetAddress address, int port) {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Specify file name.");
            return;
        }

        File file = new File(fileName);
        String absolutePath = file.getAbsolutePath();

        if (scriptStack.contains(absolutePath)) {
            System.out.println("Recursion detected! Script " + fileName + " is already running.");
            return;
        }

        scriptStack.push(absolutePath);
        try (Scanner fileScanner = new Scanner(new FileInputStream(file))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                processInput(line, socket, address, port, fileScanner, true);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Script file not found: " + fileName);
        } finally {
            scriptStack.pop();
        }
    }

    private static void sendAndReceive(Command command, DatagramSocket socket, InetAddress address, int port) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(command);
            byte[] data = baos.toByteArray();
            socket.send(new DatagramPacket(data, data.length, address, port));

            byte[] buf = new byte[65536];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
            Response response = (Response) ois.readObject();

            System.out.println(response.getMessage());

            if (response.shouldExit()) {
                isExitingNormally = true;
                System.exit(0);
            }
            
            if (response.getData() instanceof Collection<?>) {
                ((Collection<?>) response.getData()).forEach(System.out::println);
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Server is sleeping...");
        } catch (Exception e) {
            System.out.println("Communication error: " + e.getMessage());
        }
    }
}