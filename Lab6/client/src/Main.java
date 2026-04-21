import builders.InteractiveHumanBeingBuilder;
import common.Command;
import models.CommandType;
import common.Response;
import models.HumanBeing;

import java.io.*;
import java.net.*;
import java.util.Collection;
import java.util.Scanner;

public class Main {
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("I predict that)");
            System.out.println("Bye Bye");
        }));

        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            socket.setSoTimeout(2500);
            InetAddress address = InetAddress.getByName("localhost");
            int port = 5555;

            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) break;
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                String[] parts = input.split("\\s+", 2);
                String cmd = parts[0].toUpperCase();
                String arg = (parts.length > 1) ? parts[1] : null;

                Command command = null;
                try {
                    CommandType type = CommandType.valueOf(cmd);
                    
                    if (type == CommandType.ADD || type == CommandType.REMOVE_GREATER) {
                        HumanBeing human = new InteractiveHumanBeingBuilder(scanner).build();
                        command = new Command(type, null, human);
                    } else if (type == CommandType.UPDATE) {
                        if (arg == null) { System.out.println("You need to specify the ID"); continue; }
                        Long id = Long.parseLong(arg);
                        HumanBeing human = new InteractiveHumanBeingBuilder(scanner).build();
                        command = new Command(type, id, human);
                    } else if (arg != null) {
                        // Если есть аргумент (например, ID для remove), делаем его Long объектом
                        try {
                            command = new Command(type, Long.parseLong(arg), null);
                        } catch (NumberFormatException e) {
                            command = new Command(type, arg, null); // Или String объектом, если это имя
                        }
                    } else {
                        command = new Command(type, null, null);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Unknown command"); continue;
                }

                // Отправка ОБЪЕКТА
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(command);
                byte[] data = baos.toByteArray();
                socket.send(new DatagramPacket(data, data.length, address, port));

                // Получение ОБЪЕКТА
                try {
                    byte[] buf = new byte[65536];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.receive(packet);
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(packet.getData()));
                    Response response = (Response) ois.readObject();

                    System.out.println(response.getMessage());

                    if (response.shouldExit()) System.exit(0);
                    
                    if (response.getData() instanceof Collection<?>) {
                        ((Collection<HumanBeing>) response.getData()).forEach(System.out::println);
                    }
                    
                } catch (SocketTimeoutException e) {
                    System.out.println("Server is sleeping...");
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}