package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Status;
import model.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static adapter.AdapterHelper.statusFromString;

public class TaskAdapter extends TypeAdapter<Task> {

    @Override
    public void write(JsonWriter jsonWriter, Task task) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(task.getID().getAsInt());
        jsonWriter.name("name").value(task.getName());
        jsonWriter.name("description").value(task.getDescription());
        jsonWriter.name("duration").value(task.getDuration().toMinutes());
        jsonWriter.name("status").value(task.getStatus().toString());
        new LocalDateTimeAdapter().write(jsonWriter, task.getStartTime());
        jsonWriter.endObject();
    }

    @Override
    public Task read(JsonReader jsonReader) throws IOException {
        String name = null;
        String description = null;
        long duration = 0;
        LocalDateTime time = null;
        Status status = null;
        int id = 0;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String fieldName = jsonReader.nextName();
            switch (fieldName) {
                case "name" -> name = jsonReader.nextString();
                case "description" -> description = jsonReader.nextString();
                case "status" -> status = statusFromString(jsonReader.nextString());
                case "startTime" -> time = new LocalDateTimeAdapter().read(jsonReader);
                case "duration" -> duration = jsonReader.nextLong();
                case "id" -> id = jsonReader.nextInt();
                default -> jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        Task task = new Task(name, description, status, Duration.ofMinutes(duration),
                time.format(AdapterHelper.formatter));
        if (id != 0) {
            task.setID(id);
        }
        return task;
    }

}
