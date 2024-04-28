package httpserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import controllers.Managers;
import controllers.TaskManager;
import exceptions.TimeReservedException;
import model.*;

import java.io.*;
import java.net.InetSocketAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = Managers.getGson();
        httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/", this::handle);
    }

    public static void main(String[] args) throws IOException {

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

    }

    public void start() {
        System.out.println("Сервер запущен на порту - " + PORT);
        this.httpServer.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен на порту - " + PORT);
        this.httpServer.stop(0);
    }

    private void handle(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath().split("/")[1];
        switch (path) {
            case "tasks" -> {
                handleTask(httpExchange);
            }
            case "epics" -> {
                handleEpic(httpExchange);
            }
            case "subtasks" -> {
                handleSubTask(httpExchange);
            }
            case "history" -> {
                handleHistory(httpExchange);
            }
            case "prioritized" -> {
                handlePrioritized(httpExchange);
            }
            default -> {
                System.out.println(path);
                byte[] resp = "Неправильный путь".getBytes(UTF_8);
                httpExchange.sendResponseHeaders(404, resp.length);
                httpExchange.getResponseBody().write(resp);
            }
        }
    }

    private void handleTask(HttpExchange httpExchange) throws IOException {
        try (httpExchange) {
            String method = httpExchange.getRequestMethod();
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            int id = -1;
            switch (method) {
                case "GET" -> {
                    if (path.length > 2) {
                        id = parseIntFromPath(path[2]);
                        if (id != -1 & taskManager.getTaskFromID(id) != null) {
                            String taskSerialized = gson.toJson(taskManager.getTaskFromID(id));
                            BaseHttpHandler.sendText(httpExchange, taskSerialized, 200);
                        } else {
                            BaseHttpHandler.sendNotFound(httpExchange,
                                    "Указан неправильный id - " + id + ", для Task");
                        }
                    } else {
                        String tasksSerialized = gson.toJson(taskManager.getAllTask());
                        BaseHttpHandler.sendText(httpExchange, tasksSerialized, 200);
                    }
                }
                case "POST" -> {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), UTF_8);
                    Task task = gson.fromJson(body, Task.class);
                    try {
                        if (task.getID().getAsInt() == 0
                                || taskManager.getTaskFromID(task.getID().getAsInt()) == null) {
                            taskManager.createTask(task);
                            BaseHttpHandler.sendText(httpExchange,
                                    "Task с id - " + task.getID().getAsInt() + ", создан", 200);
                        } else {
                            if (taskManager.getTaskFromID(task.getID().getAsInt()).getClass().equals(Task.class)) {
                                taskManager.updateTask(task.getID().getAsInt(), task);
                                BaseHttpHandler.sendText(httpExchange,
                                        "Task с id - " + task.getID().getAsInt() + ", обновлен", 200);
                            }  else {
                                BaseHttpHandler.sendText(httpExchange,
                                        "Отправленный id - " + task.getID().getAsInt()
                                                + ", не принадлежит Task", 403);
                            }
                        }
                    } catch (TimeReservedException e) {
                        BaseHttpHandler.sendHasInteractions(httpExchange);
                    }
                } case "DELETE" -> {
                    if (path.length > 2) {
                        id = parseIntFromPath(path[2]);
                        if (taskManager.getTaskFromID(id) == null) {
                            BaseHttpHandler.sendNotFound(httpExchange,
                                    "Task с таким id - " + id + " не обнаружен");
                        } else {
                            taskManager.removeTask(taskManager.getTaskFromID(id));
                            BaseHttpHandler.sendText(httpExchange,
                                    "Task с id - " + id + ", удален", 200);
                        }
                    } else {
                        taskManager.deleteAllTask();
                        BaseHttpHandler.sendText(httpExchange, "Удалены все Task", 200);
                    }
                } default -> BaseHttpHandler.sendText(httpExchange,
                        "Такой метод не поддерживается", 418);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEpic(HttpExchange httpExchange) {
        try (httpExchange) {
            String method = httpExchange.getRequestMethod();
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            int id = -1;
            switch (method) {
                case "GET" -> {
                    if (path.length == 4) {
                        id = parseIntFromPath(path[2]);
                        if (taskManager.getTaskFromID(id) == null) {
                            BaseHttpHandler.sendNotFound(httpExchange,
                                    "Эпик с таким ID - " + id + ", не найден");
                        } else {
                            Epic epic = (Epic) taskManager.getTaskFromID(id);
                            String subTaskFromEpicSerialized = gson.toJson(taskManager.getSubTasksFromEpic(epic));
                            BaseHttpHandler.sendText(httpExchange, subTaskFromEpicSerialized, 200);
                        }
                    } else if (path.length == 3) {
                        id = parseIntFromPath(path[2]);
                        if (id != -1 && taskManager.getTaskFromID(id) != null) {
                            String epicSerialized = gson.toJson(taskManager.getTaskFromID(id));
                            BaseHttpHandler.sendText(httpExchange, epicSerialized, 200);
                        } else {
                            BaseHttpHandler.sendNotFound(httpExchange,
                                    "Указан неправильный id - " + id + ", для Epic");
                        }
                    } else {
                        String epicsSerialized = gson.toJson(taskManager.getAllEpic());
                        BaseHttpHandler.sendText(httpExchange, epicsSerialized, 200);
                    }
                }
                case "POST" -> {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), UTF_8);
                    Epic epic = gson.fromJson(body, Epic.class);
                    if (epic.getID().getAsInt() == 0
                            || taskManager.getTaskFromID(epic.getID().getAsInt()) == null) {
                        taskManager.createEpic(epic);
                        BaseHttpHandler.sendText(httpExchange,
                                "Epic с id - " + epic.getID().getAsInt() + ", создан", 200);
                    } else {
                        if (taskManager.getTaskFromID(epic.getID().getAsInt()).getClass().equals(Epic.class)) {
                            taskManager.updateEpic(epic.getID().getAsInt(), epic);
                            BaseHttpHandler.sendText(httpExchange,
                                    "Epic с id - " + epic.getID().getAsInt() + ", обновлен", 200);
                        }  else {
                            BaseHttpHandler.sendText(httpExchange,
                                    "Отправленный id - " + epic.getID().getAsInt()
                                            + ", не принадлежит Epic", 403);
                        }
                    }
                } case "DELETE" -> {
                    if (path.length > 2) {
                        id = parseIntFromPath(path[2]);
                        if (taskManager.getTaskFromID(id) == null) {
                            BaseHttpHandler.sendNotFound(httpExchange,
                                    "Epic с таким id - " + id + " не обнаружен");
                        } else {
                            taskManager.removeTask(taskManager.getTaskFromID(id));
                            BaseHttpHandler.sendText(httpExchange,
                                    "Epic с ID - " + id + " удален", 200);
                        }
                    } else {
                        taskManager.deleteAllEpic();
                        BaseHttpHandler.sendText(httpExchange, "Удалены все Epic", 200);
                    }
                } default -> BaseHttpHandler.sendText(httpExchange,
                        "Такой метод не поддерживается", 418);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSubTask(HttpExchange httpExchange) {
        try (httpExchange) {
            String method = httpExchange.getRequestMethod();
            String[] path = httpExchange.getRequestURI().getPath().split("/");
            int id = -1;
            switch (method) {
                case "GET" -> {
                    if (path.length == 3) {
                        id = parseIntFromPath(path[2]);
                        if (id != -1 & taskManager.getTaskFromID(id) != null) {
                            String subTaskSerialized = gson.toJson(taskManager.getTaskFromID(id));
                            BaseHttpHandler.sendText(httpExchange, subTaskSerialized, 200);
                        } else {
                            BaseHttpHandler.sendNotFound(httpExchange,
                                    "Указан неправильный ID - " + id + ", для SubTask");
                        }
                    } else {
                        String tasksSerialized = gson.toJson(taskManager.getAllSubTask());
                        BaseHttpHandler.sendText(httpExchange, tasksSerialized, 200);
                    }
                }
                case "POST" -> {
                    InputStream inputStream = httpExchange.getRequestBody();
                    String body = new String(inputStream.readAllBytes(), UTF_8);
                    SubTask subTask = gson.fromJson(body, SubTask.class);
                    try {
                        if (subTask.getID().getAsInt() == 0
                                || taskManager.getTaskFromID(subTask.getID().getAsInt()) == null) {
                            taskManager.createSubTask(subTask);
                            BaseHttpHandler.sendText(httpExchange,
                                    "SubTask с id - " + subTask.getID().getAsInt() + ", создан", 200);
                        } else {
                            if (taskManager.getTaskFromID(subTask.getID().getAsInt()).getClass()
                                    .equals(SubTask.class)) {
                                taskManager.updateSubTask(subTask.getID().getAsInt(), subTask);
                                BaseHttpHandler.sendText(httpExchange,
                                        "SubTask с id - " + subTask.getID().getAsInt()
                                                + ", обновлен", 200);
                            }  else {
                                BaseHttpHandler.sendText(httpExchange,
                                        "Отправленный id - " + subTask.getID().getAsInt()
                                                + ", не принадлежит SubTask", 403);
                            }
                        }
                    } catch (TimeReservedException e) {
                        BaseHttpHandler.sendHasInteractions(httpExchange);
                    } catch (NullPointerException e) {
                        BaseHttpHandler.sendNotFound(httpExchange, "Отсутствует Epic для SubTask");
                    }
                } case "DELETE" -> {
                    if (path.length == 3) {
                        id = parseIntFromPath(path[2]);
                        if (taskManager.getTaskFromID(id) == null) {
                            BaseHttpHandler.sendNotFound(httpExchange,
                                    "Задача с таким ID - " + id + " не обнаружена");
                        } else {
                            taskManager.removeTask(taskManager.getTaskFromID(id));
                            BaseHttpHandler.sendText(httpExchange, "SubTask c id - " + id + " удалена", 200);
                        }
                    } else {
                        taskManager.deleteAllSubTask();
                        BaseHttpHandler.sendText(httpExchange, "Удалены все SubTask", 200);
                    }
                } default -> BaseHttpHandler.sendText(httpExchange,
                        "Такой метод не поддерживается", 418);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleHistory(HttpExchange httpExchange) {
        try (httpExchange) {
            String method = httpExchange.getRequestMethod();
            if ("GET".equals(method)) {
                String historySerialized = gson.toJson(taskManager.getHistory());
                BaseHttpHandler.sendText(httpExchange, historySerialized, 200);
            } else {
                BaseHttpHandler.sendText(httpExchange,
                        "Такой метод не поддерживается", 418);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handlePrioritized(HttpExchange httpExchange) {
        try (httpExchange) {
            String method = httpExchange.getRequestMethod();
            if ("GET".equals(method)) {
                String prioritizedSerialized = gson.toJson(taskManager.getPrioritizedTasks());
                BaseHttpHandler.sendText(httpExchange, prioritizedSerialized, 200);
            } else {
                BaseHttpHandler.sendText(httpExchange,
                        "Такой метод не поддерживается", 418);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int parseIntFromPath(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}