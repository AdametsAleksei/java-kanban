package controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    public void shouldReturnInMemoryFileBackedTaskManager() {
        assertEquals(Managers.getDefaultFile().getClass(), FileBackedTaskManager.class);
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