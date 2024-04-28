package controllers;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void shouldReturnInMemoryFileBackedTaskManager() throws IOException {
        assertEquals(Managers.getDefault(File.createTempFile("test", "csv")).getClass(),
                FileBackedTaskManager.class);
    }

    @Test
    public void shouldReturnInMemoryTaskManager() {
        assertEquals(Managers.getDefault().getClass(), InMemoryTaskManager.class);
    }

    @Test
    public void shouldReturnInMemoryHistoryManager() {
        assertEquals(Managers.getDefaultHistory().getClass(), InMemoryHistoryManager.class);
    }

}