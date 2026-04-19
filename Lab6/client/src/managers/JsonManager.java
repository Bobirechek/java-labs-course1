package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Command;
import common.Response;

public class JsonManager {

    private static final Gson gson = new GsonBuilder().create();

    // Преобразование команды в JSON (для отправки на сервер)
    public static String toJson(Command command) {
        return gson.toJson(command);
    }

    // Преобразование JSON в Response (ответ от сервера)
    public static Response parseResponse(String json) {
        return gson.fromJson(json, Response.class);
    }
}