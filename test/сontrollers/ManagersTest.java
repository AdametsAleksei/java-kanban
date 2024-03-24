package сontrollers;

import controllers.InMemoryHistoryManager;
import controllers.InMemoryTaskManager;
import controllers.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void shouldReturnInMemoryTaskManager() {
        assertEquals(Managers.getDefault().getClass(), new InMemoryTaskManager().getClass());
    }

    @Test
    public void shouldReturnInMemoryHistoryManager() {
        assertEquals(Managers.getDefaultHistory().getClass(), new InMemoryHistoryManager().getClass());
    }

}