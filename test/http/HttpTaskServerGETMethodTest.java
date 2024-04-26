package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controllers.Managers;
import controllers.TaskManager;
import httpServer.HttpTaskServer;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerGETMethodTest {
    private HttpTaskServer httpTaskServer;
    private final Gson gson = Managers.getGson();
    private TaskManager taskManager;
    private final String host = "http://localhost:8080/";
    private Task task;
    private Epic epic;
    private SubTask subTask;

    @BeforeEach
    void init() throws IOException {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        task = new Task("Task test", "Task test description",
                Status.NEW, Duration.ofMinutes(17), "11.04.24 - 17:40");
        taskManager.createTask(task);
        epic = new Epic("Epic test", "Epic test description");
        taskManager.createEpic(epic);
        subTask = new SubTask("Subtask Test", "SubTask Test description",
                Status.DONE, epic.getID().getAsInt(), Duration.ofMinutes(20), "11.04.24 - 17:20");
        taskManager.createSubTask(subTask);
        httpTaskServer.start();
    }

    @AfterEach
    void stop() {
        httpTaskServer.stop();
    }

    @Test
    void shouldReturnTaskByID() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "tasks/" + task.getID().getAsInt());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(task, gson.fromJson(response.body(), Task.class));
        System.out.println(response.body());
    }

    @Test
    void shouldReturnAllTasks() throws IOException, InterruptedException {
        Task task2 = new Task("Task2 test", "Desription task2 test",
                Status.IN_PROGRESS, Duration.ofMinutes(17), "11.04.24 - 18:00");
        taskManager.createTask(task2);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type taskType = new TypeToken<List<Task>>() {}.getType();
        assertEquals(taskManager.getAllTask(), gson.fromJson(response.body(), taskType));
    }

    @Test
    void shouldReturnEpicByID() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "epics/" + epic.getID().getAsInt());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(epic, gson.fromJson(response.body(), Epic.class));
    }

    @Test
    void shouldReturnAllEpics() throws IOException, InterruptedException {
        Epic epic2 = new Epic("Epic2 test", "Epic2 test description");
        taskManager.createEpic(epic2);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type epicType = new TypeToken<List<Epic>>() {}.getType();
        assertEquals(taskManager.getAllEpic(), gson.fromJson(response.body(), epicType));
    }

    @Test
    void shouldReturnSubTasksFromEpic() throws IOException, InterruptedException {
        SubTask subTask2 = new SubTask("SubTask2 test", "Desription Subtask2 test",
                Status.IN_PROGRESS, epic.getID().getAsInt(), Duration.ofMinutes(17), "11.04.24 - 12:00");
        taskManager.createSubTask(subTask2);
        URI uri = URI.create(host + "epics/" + epic.getID().getAsInt() + "/subtasks");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type subTaskType = new TypeToken<List<SubTask>>() {}.getType();
        assertEquals(epic.getListSubTasks(), gson.fromJson(response.body(), subTaskType));
    }

    @Test
    void shouldReturnSubTaskByID() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "subtasks/" + subTask.getID().getAsInt());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(subTask, gson.fromJson(response.body(), SubTask.class));
    }

    @Test
    void shouldReturnAllSubTask() throws IOException, InterruptedException {
        SubTask subTask2 = new SubTask("SubTask2 test", "Desription Subtask2 test",
                Status.IN_PROGRESS, epic.getID().getAsInt(), Duration.ofMinutes(17), "11.04.24 - 18:00");
        taskManager.createSubTask(subTask2);
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type subTaskType = new TypeToken<List<SubTask>>() {}.getType();
        assertEquals(taskManager.getAllSubTask(), gson.fromJson(response.body(), subTaskType));
    }

    @Test
    void shouldReturn404WhenSubTaskCreateWithoutEpic() throws IOException, InterruptedException {
        SubTask subTask2 = new SubTask("SubTask2 test", "Desription Subtask2 test",
                Status.IN_PROGRESS, 20, Duration.ofMinutes(17), "11.04.24 - 18:00");
        taskManager.createSubTask(subTask2);
        URI uri = URI.create(host + "subtasks/" + subTask2.getID().getAsInt());
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    void shouldReturnHistoryListInRightOrder() throws IOException, InterruptedException {
        taskManager.getTaskFromID(task.getID().getAsInt());
        taskManager.getTaskFromID(subTask.getID().getAsInt());
        taskManager.getTaskFromID(epic.getID().getAsInt());
        taskManager.getTaskFromID(task.getID().getAsInt());
        URI uri = URI.create(host + "history");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        //Не придумал как десериализовать разные типы задач в один список,
        //пришлось использовать вот такой костыль :(
        assertEquals(gson.toJson(taskManager.getHistory()), response.body());
    }

    @Test
    void shouldReturnPrioritizedListInRightOrder() throws IOException, InterruptedException {
        URI uri = URI.create(host + "prioritized");
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        //Не придумал как десериализовать разные типы задач в один список,
        //пришлось использовать вот такой костыль :(
        assertEquals(gson.toJson(taskManager.getPrioritizedTasks()), response.body());
    }
}
