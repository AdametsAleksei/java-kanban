package http;

import controllers.Managers;
import controllers.TaskManager;
import httpserver.HttpTaskServer;
import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskServerDELETEMethodTest {
    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private final String host = "http://localhost:8080/";
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private SubTask subTask1;
    private SubTask subTask2;

    @BeforeEach
    void init() throws IOException {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        task1 = new Task("Task1 test", "Task test description",
                Status.NEW, Duration.ofMinutes(17), "11.04.24 - 17:40");
        taskManager.createTask(task1);
        task2 = new Task("Task2 test", "Task test description",
                Status.NEW, Duration.ofMinutes(17), "11.04.24 - 13:40");
        taskManager.createTask(task2);
        epic1 = new Epic("Epic test", "Epic test description");
        taskManager.createEpic(epic1);
        epic2 = new Epic("Epic test", "Epic test description");
        taskManager.createEpic(epic2);
        subTask1 = new SubTask("Subtask Test", "SubTask Test description",
                Status.DONE, epic1.getID().getAsInt(), Duration.ofMinutes(20), "11.04.24 - 12:20");
        taskManager.createSubTask(subTask1);
        subTask2 = new SubTask("Subtask Test", "SubTask Test description",
                Status.DONE, epic1.getID().getAsInt(), Duration.ofMinutes(20), "11.04.24 - 14:20");
        taskManager.createSubTask(subTask2);
        httpTaskServer.start();
    }

    @AfterEach
    void stop() {
        httpTaskServer.stop();
    }

    @Test
    void shouldBeDeletedTask() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "tasks/" + task1.getID().getAsInt());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, taskManager.getAllTask().size());
    }

    @Test
    void shouldBeDeletedAllTask() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getAllTask().size());
    }

    @Test
    void shouldBeDeletedEpicAndHisSubTasks() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "epics/" + epic1.getID().getAsInt());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, taskManager.getAllEpic().size());
        assertEquals(0, taskManager.getAllSubTask().size());
    }

    @Test
    void shouldBeDeletedAllEpics() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "epics");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getAllEpic().size());
        assertEquals(0, taskManager.getAllSubTask().size());
    }

    @Test
    void shouldBeDeletedSubTask() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "subtasks/" + subTask1.getID().getAsInt());
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, taskManager.getAllSubTask().size());
    }

    @Test
    void shouldBeDeletedAllSubTask() throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create(host + "subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, taskManager.getAllSubTask().size());
        assertEquals(0, epic1.getListSubTasks().size());
    }

}