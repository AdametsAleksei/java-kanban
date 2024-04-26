package httpserver;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BaseHttpHandler {

    public static void sendText(HttpExchange httpExchange, String message, int rCode) throws IOException {
        byte[] text = message.getBytes(UTF_8);
        httpExchange.sendResponseHeaders(rCode, text.length);
        httpExchange.getResponseBody().write(text);
    }

    public static void sendNotFound(HttpExchange httpExchange, String message) throws IOException {
        byte[] text = message.getBytes(UTF_8);
        httpExchange.sendResponseHeaders(404, text.length);
        httpExchange.getResponseBody().write(text);
    }

    public static void sendHasInteractions(HttpExchange httpExchange) throws IOException {
        byte[] text = "Указанное время пересекается с другой задачей".getBytes(UTF_8);
        httpExchange.sendResponseHeaders(406, text.length);
        httpExchange.getResponseBody().write(text);
    }
}
