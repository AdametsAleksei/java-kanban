package http;

import com.google.gson.Gson;
import controllers.Managers;
import controllers.TaskManager;
import httpServer.HttpTaskServer;
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
    void shouldReturn403WhenIdIsUsing() throws IOException, InterruptedException {
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
        assertEquals(task, taskManager.getTaskFromID(task.getID().getAsInt()));
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
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        uri = URI.create(host + "subtasks");
        request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(subTaskJson))
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(subTask, taskManager.getTaskFromID(subTask.getID().getAsInt()));
    }
}