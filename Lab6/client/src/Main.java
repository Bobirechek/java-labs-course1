import models.CommandType;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import common.Command;
import common.Response;
import managers.JsonManager;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            try {
                Socket socket = new Socket("localhost", 5555);

                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                // 🔥 пока простой парсинг
                String[] parts = input.split(" ", 2);
                CommandType type = CommandType.valueOf(parts[0].toUpperCase());
                String arg = parts.length > 1 ? parts[1] : null;

                Command command = new Command(type, arg);

                String json = JsonManager.toJson(command);

                out.write(json);
                out.newLine();
                out.flush();

                String responseJson = in.readLine();
                Response response = JsonManager.parseResponse(responseJson);

                System.out.println(response.getMessage());

                socket.close();

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}