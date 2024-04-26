package controllers;

import adapter.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getDefaultFile(File file) {
        return FileBackedTaskManager.loadFromFile(file);
    }

    public static Gson getGson() {
        return new GsonBuilder()
               // .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
               // .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(Task.class, new TaskAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(SubTask.class, new SubTaskAdapter())
                .create();
    }
}
