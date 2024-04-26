package http;

import com.google.gson.Gson;
import controllers.Managers;
import controllers.TaskManager;
import httpserver.HttpTaskServer;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
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

public class HttpTaskServerPOSTMethodTest {
    private HttpTaskServer httpTaskServer;
    private final Gson gson = Managers.getGson();
    private TaskManager taskManager;
    private final String host = "http://localhost:8080/";

    @BeforeEach
    void init() throws IOException {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    void stop() {
        httpTaskServer.stop();
    }

    @Test
    void shouldBeAddTask() throws IOException, InterruptedException {
        Task task = new Task("Task test", "Task test description",
                Status.NEW, Duration.ofMinutes(17), "11.04.24 - 17:40");
        task.setID(4);
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(host + "tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(task, taskManager.getTaskFromID(task.getID().getAsInt()));
    }

    @Test
    void shouldBeUpdatedTaskWhenIdIsUsing() throws IOException, InterruptedException {
        Task task = new Task("Task test", "Task test description",
                Status.NEW, Duration.ofMinutes(17), "11.04.24 - 17:40");
        task.setID(4);
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(host + "tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskNew = new Task("Task test", "Task test description",
                Status.NEW, Duration.ofMinutes(17), "11.04.24 - 17:40");
        taskNew.setID(4);
        taskJson = gson.toJson(taskNew);
        request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(taskNew, taskManager.getTaskFromID(task.getID().getAsInt()));
    }

    @Test
    void shouldBeReturn406WhenTimeReservedForTask() throws IOException, InterruptedException {
        Task task1 = new Task("Task test", "Task test description",
                Status.NEW, Duration.ofMinutes(17), "11.04.24 - 17:40");
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(host + "tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task2 = new Task("Task test", "Task test description",
                Status.NEW, Duration.ofMinutes(17), "11.04.24 - 17:40");
        taskJson = gson.toJson(task2);
        request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
        assertEquals(1, taskManager.getAllTask().size());
    }


    @Test
    void shouldBeAddEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic test", "Epic test description");
        epic.setID(1);
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(host + "epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(epic, taskManager.getTaskFromID(epic.getID().getAsInt()));
    }

    @Test
    void shouldBeUpdatedEpicWhenIdIsUsing() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Description");
        epic.setID(2);
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(host + "epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicNew = new Epic("EpicNew", "Description");
        epicNew.setID(2);
        epicJson = gson.toJson(epicNew);
        request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(epicNew, taskManager.getTaskFromID(epic.getID().getAsInt()));
    }

    @Test
    void shouldBeAddSubTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic test", "Epic test description");
        epic.setID(1);
        SubTask subTask = new SubTask("Subtask Test", "SubTask Test description",
                Status.DONE, epic.getID().getAsInt(), Duration.ofMinutes(20), "11.04.24 - 17:20");
        subTask.setID(4);
        String epicJson = gson.toJson(epic);
        String subTaskJson = gson.toJson(subTask);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(host + "epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        uri = URI.create(host + "subtasks");
        request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(subTask, taskManager.getTaskFromID(subTask.getID().getAsInt()));
    }

    @Test
    void shouldBeUpdatedSubTaskWhenIdIsUsing() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic test", "Epic test description");
        epic.setID(1);
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(host + "epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask = new SubTask("Subtask Test", "SubTask Test description",
                Status.DONE, epic.getID().getAsInt(), Duration.ofMinutes(20), "11.04.24 - 17:20");
        subTask.setID(4);
        String subTaskJson = gson.toJson(subTask);
        uri = URI.create(host + "subtasks");
        request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTaskNew = new SubTask("SubtaskNew", "SubTask NEW description",
                Status.DONE, epic.getID().getAsInt(), Duration.ofMinutes(20), "11.04.24 - 17:20");
        subTaskNew.setID(4);
        subTaskJson = gson.toJson(subTaskNew);
        request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(subTaskNew, taskManager.getTaskFromID(subTask.getID().getAsInt()));
    }

    @Test
    void shouldBeReturn406WhenTimeReservedForSubTask() throws IOException, InterruptedException {
        Task task1 = new Task("Task test", "Task test description",
                Status.NEW, Duration.ofMinutes(17), "11.04.24 - 17:40");
        String taskJson = gson.toJson(task1);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(host + "tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic = new Epic("Epic test", "Epic test description");
        epic.setID(3);
        String epicJson = gson.toJson(epic);
        uri = URI.create(host + "epics");
        request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask1 = new SubTask("Subtask Test", "SubTask Test description",
                Status.DONE, epic.getID().getAsInt(), Duration.ofMinutes(20), "11.04.24 - 16:20");
        String subTaskJson = gson.toJson(subTask1);
        uri = URI.create(host + "subtasks");
        request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask2 = new SubTask("SubtaskNew", "SubTask NEW description",
                Status.DONE, epic.getID().getAsInt(), Duration.ofMinutes(20), "11.04.24 - 17:40");
        subTaskJson = gson.toJson(subTask2);
        request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
        assertEquals(1, taskManager.getAllTask().size());
        assertEquals(1, taskManager.getAllEpic().size());
        assertEquals(1, taskManager.getAllSubTask().size());
    }
}