import builders.InteractiveHumanBeingBuilder;
import common.Command;
import common.Response;
import managers.JsonManager;
import models.CommandType;
import models.HumanBeing;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class Main {
    private static final Stack<String> scriptStack = new Stack<>();
    private static boolean isExitingNormally = false;

    // Текущий авторизованный пользователь
    private static String currentLogin = null;
    private static String currentPasswordHash = null;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!isExitingNormally) {
                System.out.println("Bye Bye");
            }
        }));

        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            socket.setSoTimeout(3000);
            InetAddress address = InetAddress.getByName("localhost");
            int port = 5555;

            // Авторизация
            authLoop(socket, address, port, scanner);
            
            if (currentLogin != null) {
                System.out.println("Hello " +  currentLogin);
                System.out.println("Type 'help' to see available commands.");
            }

            while (true) {
                if (currentLogin != null) {System.out.print("[" + currentLogin + "]> ");};

                if (!scanner.hasNextLine()) break;
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                processInput(input, socket, address, port, scanner, false);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // Функция авторизации
    private static void authLoop(DatagramSocket socket, InetAddress address, int port, Scanner scanner) {
        if (currentLogin == null) {
            System.out.println("First you need to register or log in");
        } else {
            currentLogin = null;
        }
        while (currentLogin == null) {
            System.out.println("Use the >login or >register command");
            System.out.print(">");
            try {
                String cmdName = scanner.nextLine().trim();

                try {
                    CommandType type = CommandType.valueOf(cmdName.toUpperCase());
                    if (type != CommandType.LOGIN && type != CommandType.REGISTER) {
                        throw new IllegalArgumentException("You cannot use this command without authorization.");
                    }

                    System.out.print("Login: ");
                    String login = scanner.nextLine().trim();
                    if (login.isEmpty()) { System.out.println("Login cannot be empty."); continue; }

                    System.out.print("Password: ");
                    String password = scanner.nextLine().trim();
                    // if (password.isEmpty()) { System.out.println("Password cannot be empty."); continue; }

                    String hash = sha512(password);

                    Command cmd = new Command(type, null, null, login, hash);
                    
                    try {
                        Response response = sendAndReceiveRaw(cmd, socket, address, port);
                        if (response == null) {
                            System.out.println("Server is not responding. Try again.");
                            continue;
                        }
                        System.out.println(response.getMessage());
                        
                        if (response.getMessage() != null &&
                        response.getMessage().toLowerCase().contains("successful")) {
                            currentLogin = login;
                            currentPasswordHash = hash;
                        }
                    } catch (Exception e) {
                        System.out.println("Connection error: " + e.getMessage());
                    }
                } catch (IllegalArgumentException e) {
                    if (e.getMessage().equals("You cannot use this command without authorization.")) {
                        System.out.println("You cannot use this command without authorization.");
                    } else {
                        System.out.println("Unknown command: " + cmdName);
                    }
                }
            } catch (NoSuchElementException e) {
                return;
            } 
        }
    }

    // Ввод команд
    private static void processInput(String input, DatagramSocket socket, InetAddress address,
                                     int port, Scanner scanner, boolean isFromScript) {
        String[] parts = input.split("\\s+", 2);
        String cmdName = parts[0];
        String arg = (parts.length > 1) ? parts[1].trim() : null;

        if (isFromScript) {
            System.out.println("> " + parts[0] + (arg != null ? " " + arg : ""));
            try { Thread.sleep(1200); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }

        try {
            CommandType type = CommandType.valueOf(cmdName.toUpperCase());

            if (type == CommandType.LOGOUT) {
                System.out.println("You are logged out.");
                authLoop(socket, address, port, scanner);
                return;
            }

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
                return new Command(type, null, human, currentLogin, currentPasswordHash);
            }

            if (type == CommandType.UPDATE) {
                if (arg == null || arg.isEmpty()) {
                    System.out.println("Error: ID is required for update.");
                    return null;
                }
                String[] updateParts = arg.split("\\s+", 2);
                Long id = Long.parseLong(updateParts[0]);
                String jsonPart = (updateParts.length > 1) ? updateParts[1].trim() : null;
                HumanBeing human = parseHumanArgument(jsonPart, scanner);
                return new Command(type, id, human, currentLogin, currentPasswordHash);
            }

            if (arg != null && !arg.isEmpty()) {
                try {
                    return new Command(type, Long.parseLong(arg), null, currentLogin, currentPasswordHash);
                } catch (NumberFormatException e) {
                    return new Command(type, arg, null, currentLogin, currentPasswordHash);
                }
            }

            return new Command(type, null, null, currentLogin, currentPasswordHash);

        } catch (Exception e) {
            System.out.println("Validation error: " + e.getMessage());
            return null;
        }
    }

    private static HumanBeing parseHumanArgument(String arg, Scanner scanner) {
        if (arg != null && arg.startsWith("{")) {
            return JsonManager.convert(arg, HumanBeing.class);
        }
        return new InteractiveHumanBeingBuilder(scanner).build();
    }

    private static void handleExecuteScript(String fileName, DatagramSocket socket,
                                             InetAddress address, int port) {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Specify file name.");
            return;
        }

        File file = new File(fileName);
        String absolutePath = file.getAbsolutePath();

        if (scriptStack.contains(absolutePath)) {
            System.out.println("Recursion detected! Script already running: " + fileName);
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

    // Сеть
    private static void sendAndReceive(Command command, DatagramSocket socket,
                                       InetAddress address, int port) {
        Response response = sendAndReceiveRaw(command, socket, address, port);
        if (response == null) return;

        System.out.println(response.getMessage());

        if (response.shouldExit()) {
            isExitingNormally = true;
            System.exit(0);
        }

        if (response.getData() instanceof Collection<?>) {
            ((Collection<?>) response.getData()).forEach(System.out::println);
        }
    }

    private static Response sendAndReceiveRaw(Command command, DatagramSocket socket,
                                               InetAddress address, int port) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(command);
            byte[] data = baos.toByteArray();
            socket.send(new DatagramPacket(data, data.length, address, port));

            byte[] buf = new byte[65536];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            byte[] received = Arrays.copyOf(packet.getData(), packet.getLength());
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(received));
            return (Response) ois.readObject();

        } catch (SocketTimeoutException e) {
            System.out.println("Server is not responding (timeout).");
            return null;
        } catch (Exception e) {
            System.out.println("Communication error: " + e.getMessage());
            return null;
        }
    }

    // Хэширование
    private static String sha512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 not available", e);
        }
    }
}