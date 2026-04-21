package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import common.Command;
import common.Response;
import models.HumanBeing;

import java.time.LocalDateTime;

public class JsonManager {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    // 🔹 универсальный метод
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public static String toJson(Command command) {
        return gson.toJson(command);
    }

    public static Command parseCommand(String json) {
        return gson.fromJson(json, Command.class);
    }

    public static String toJson(Response response) {
        return gson.toJson(response);
    }

    public static Response parseResponse(String json) {
        return gson.fromJson(json, Response.class);
    }

    // если нужно
    public static HumanBeing parseHuman(String json) {
        return gson.fromJson(json, HumanBeing.class);
    }
}