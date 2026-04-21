package common;

import java.io.Serializable;

public class Response implements Serializable {
    private String message;
    private Object data;
    private boolean shouldExit;

    public Response(String message, Object data) {
        this.message = message;
        this.data = data;
        this.shouldExit = false;
    }

    public Response(String message, Object data, boolean shouldExit) {
        this.message = message;
        this.data = data;
        this.shouldExit = shouldExit;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    // Добавляем сеттер, чтобы обновить данные (например, отсортированную коллекцию)
    public void setData(Object data) {
        this.data = data;
    }

    public boolean shouldExit() {
        return shouldExit;
    }
}