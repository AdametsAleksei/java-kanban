import model.Epic;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void shouldBeEqualsWhenEpicIDEquals() {
        Epic epic1 = new Epic("Epic1", "Epic1");
        Epic epic2 = new Epic("Epic2", "Epic2");
        epic2.setID(epic1.getID());
        assertEquals(epic1,epic2,"Задачи не совпадают");
    }

}