package adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Epic;

import java.io.IOException;

public class EpicAdapter extends TypeAdapter<Epic> {

    @Override
    public void write(JsonWriter jsonWriter, Epic epic) throws IOException {
        jsonWriter.beginObject();
        jsonWriter.name("id").value(epic.getID().getAsInt());
        jsonWriter.name("name").value(epic.getName());
        jsonWriter.name("description").value(epic.getDescription());
        jsonWriter.name("status").value(epic.getStatus().toString());
        jsonWriter.endObject();
    }

    @Override
    public Epic read(JsonReader jsonReader) throws IOException {
        String name = null;
        String description = null;
        int id = 0;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String fieldName = jsonReader.nextName();
            switch (fieldName) {
                case "name" -> name = jsonReader.nextString();
                case "description" -> description = jsonReader.nextString();
                case "id" -> id = jsonReader.nextInt();
                default -> jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        Epic epic = new Epic(name, description);
        if (id != 0) {
            epic.setID(id);
        }
        return epic;
    }

}
