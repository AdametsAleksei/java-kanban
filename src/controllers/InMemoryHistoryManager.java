package controllers;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void addToHistory(Task task){
        if (history.size() >= 10){
            history.remove(0);
        }
        history.add(task);
    }
}
