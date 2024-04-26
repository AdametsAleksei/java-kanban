package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Status;
import model.SubTask;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static adapter.AdapterHelper.statusFromString;

public class SubTaskAdapter extends TypeAdapter<SubTask> {

    @Override
    public void write(JsonWriter jsonWriter, SubTask subTask) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(subTask.getID().getAsInt());
        jsonWriter.name("name").value(subTask.getName());
        jsonWriter.name("description").value(subTask.getDescription());
        jsonWriter.name("duration").value(subTask.getDuration().toMinutes());
        jsonWriter.name("status").value(subTask.getStatus().toString());
        jsonWriter.name("epicID").value(subTask.getEpicID());
        new LocalDateTimeAdapter().write(jsonWriter, subTask.getStartTime());
        jsonWriter.endObject();
    }

    @Override
    public SubTask read(JsonReader jsonReader) throws IOException {
        String name = null;
        String description = null;
        long duration = 0;
        LocalDateTime time = null;
        Status status = null;
        int epicID = 0;
        int id = 0;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String fieldName = jsonReader.nextName();
            switch (fieldName) {
                case "name" -> name = jsonReader.nextString();
                case "description" -> description = jsonReader.nextString();
                case "status" -> status = statusFromString(jsonReader.nextString());
                case "epicID" -> epicID = jsonReader.nextInt();
                case "startTime" -> time = new LocalDateTimeAdapter().read(jsonReader);
                case "duration" -> duration = jsonReader.nextLong();
                case "id" -> id = jsonReader.nextInt();
                default -> jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        SubTask subTask = new SubTask(name, description, status, epicID, Duration.ofMinutes(duration),
                time.format(AdapterHelper.formatter));
        if (id != 0) {
            subTask.setID(id);
        }
        return subTask;
    }
}
