package controllers;

import model.Task;

import java.util.ArrayList;

public interface HistoryManager {

    ArrayList getHistory();

    public void addToHistory(Task task);
}
