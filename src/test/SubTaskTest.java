package test;

import model.Status;
import model.SubTask;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    public void shouldBeEqualsWhenSubTaskIDEquals(){
        SubTask subTask1 = new SubTask("subTask1", "subTask1", Status.NEW, 1);
        SubTask subTask2 = new SubTask("subTask2", "subTask2", Status.NEW, 1);
        subTask2.setID(subTask1.getID());
        assertEquals(subTask1,subTask2,"Задачи не совпадают");
    }

    @Test
    public void shouldReturnedEpicID(){
        int epicID = 1;
        SubTask subTask1 = new SubTask("subTask1", "subTask1", Status.NEW, epicID);
        assertEquals(epicID, subTask1.getEpicID());
    }
}