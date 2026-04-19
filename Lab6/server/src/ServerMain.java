import managers.CommandManager;
import managers.CollectionManager;
import managers.FileManager;
import managers.JsonManager;
import common.Command;
import common.Response;
import models.HumanBeing;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class ServerMain {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5555);
            System.out.println("Server started on port 5555...");

            CollectionManager collectionManager = new CollectionManager(new Vector<>());
            FileManager fileManager = new FileManager("data.json");

            CommandManager commandManager = new CommandManager(collectionManager, fileManager);

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client connected");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(client.getInputStream()));
                BufferedWriter out = new BufferedWriter(
                        new OutputStreamWriter(client.getOutputStream()));

                String input = in.readLine();
                System.out.println("Received: " + input);

                Command command = JsonManager.parseCommand(input);
                Response response = commandManager.execute(command);

                String jsonResponse = JsonManager.toJson(response);
                out.write(jsonResponse);
                out.newLine();
                out.flush();

                client.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}