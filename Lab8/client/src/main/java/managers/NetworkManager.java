package managers;

import common.Command;
import common.Response;
import models.CommandType;
import models.HumanBeing;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class NetworkManager {

    private static final int PORT = 5555;
    private static final int TIMEOUT = 5000;
    private static final int MAX_PACKET = 65536;

    private final InetAddress address;
    private String currentLogin;
    private String currentPasswordHash;

    public NetworkManager(String host) throws UnknownHostException {
        this.address = InetAddress.getByName(host);
    }

    public Response send(Command command) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(command);
            }
            byte[] data = baos.toByteArray();

            if (data.length > MAX_PACKET) {
                return new Response("Ошибка: данные слишком большие для отправки (" + data.length + " байт)", null);
            }

            socket.send(new DatagramPacket(data, data.length, address, PORT));

            byte[] buf = new byte[MAX_PACKET];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            byte[] received = Arrays.copyOf(packet.getData(), packet.getLength());
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(received))) {
                return (Response) ois.readObject();
            }

        } catch (SocketTimeoutException e) {
            return new Response("Сервер не отвечает. Проверьте, что сервер запущен.", null);
        } catch (ConnectException | NoRouteToHostException e) {
            return new Response("Нет связи с сервером: " + e.getMessage(), null);
        } catch (IOException e) {
            return new Response("Ошибка соединения: " + e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            return new Response("Ошибка десериализации ответа сервера.", null);
        } catch (Exception e) {
            return new Response("Неожиданная ошибка: " + e.getMessage(), null);
        }
    }

    public Response login(String login, String password) {
        if (login == null || login.isBlank()) return new Response("Логин не может быть пустым.", null);
        if (password == null || password.isEmpty()) return new Response("Пароль не может быть пустым.", null);

        String hash = sha512(password);
        Command cmd = new Command(CommandType.LOGIN, null, null, login.trim(), hash);
        Response r = send(cmd);
        if (r != null && r.getMessage() != null &&
                r.getMessage().toLowerCase().contains("successful")) {
            currentLogin = login.trim();
            currentPasswordHash = hash;
        }
        return r;
    }

    public Response register(String login, String password) {
        if (login == null || login.isBlank()) return new Response("Логин не может быть пустым.", null);
        if (password == null || password.isEmpty()) return new Response("Пароль не может быть пустым.", null);

        String hash = sha512(password);
        Command cmd = new Command(CommandType.REGISTER, null, null, login.trim(), hash);
        Response r = send(cmd);
        if (r != null && r.getMessage() != null &&
                r.getMessage().toLowerCase().contains("successful")) {
            currentLogin = login.trim();
            currentPasswordHash = hash;
        }
        return r;
    }

    public Response sendCommand(CommandType type, java.io.Serializable arg, HumanBeing human) {
        if (!isLoggedIn()) {
            return new Response("Вы не авторизованы.", null);
        }
        Command cmd = new Command(type, arg, human, currentLogin, currentPasswordHash);
        return send(cmd);
    }

    public void logout() {
        currentLogin = null;
        currentPasswordHash = null;
    }

    public String getCurrentLogin() { return currentLogin; }
    public boolean isLoggedIn() { return currentLogin != null; }

    public static String sha512(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(128);
            for (byte b : hash) {
                String h = Integer.toHexString(0xff & b);
                if (h.length() == 1) hex.append('0');
                hex.append(h);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512 недоступен", e);
        }
    }
}
