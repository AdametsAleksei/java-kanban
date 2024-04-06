package controllers;

import model.*;
import exceptions.ManagerSaveException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private Task createTaskFromFile(String name, String description, int id, Status status) {
        Task task = new Task(name, description, status);
        task.setID(id);
        return task;
    }

    public Epic createEpicFromFile(String name, String description, int id) {
        Epic epic = new Epic(name, description);
        epic.setID(id);
        return epic;
    }

    public SubTask createSubTaskFromFile(String name, String description, int id, Status status, int epicID) {
        SubTask subTask = new SubTask(name, description, status, epicID);
        subTask.setID(id);
        return subTask;
    }

    public Status getStatusFromFile(String status) {
        if (status.equals(Status.NEW.toString())) {
            return Status.NEW;
        } else if (status.equals(Status.IN_PROGRESS.toString())) {
            return Status.IN_PROGRESS;
        } else {
            return Status.DONE;
        }
    }

    public void restoreTaskFromFile(String[] line) {
        if (line[1].equals(Type.Task.toString())) {
            Task task = createTaskFromFile(line[2], line[4], Integer.parseInt(line[0]), getStatusFromFile(line[3]));
            super.createTask(task);
        } else if (line[1].equals(Type.Epic.toString())) {
            Epic epic = createEpicFromFile(line[2], line[4], Integer.parseInt(line[0]));
            super.createEpic(epic);
        } else if (line[1].equals(Type.SubTask.toString())) {
            SubTask subTask = createSubTaskFromFile(line[2], line[4], Integer.parseInt(line[0]),
                    getStatusFromFile(line[3]), Integer.parseInt(line[5]));
            super.createSubTask(subTask);
        }
    }

    public void restoreHistoryFromFile(List<Integer> historyList) {
        for (Integer num : historyList) {
            addToHistory(getTaskFromID(num));
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
            while (bufferedReader.ready()) {
                List<Integer> historyList = new ArrayList<>();
                String[] line = bufferedReader.readLine().split(",");
                if (line[0].equals("")) {
                    break;
                }
                if (!line[0].equals("History")) {
                    fileBackedTaskManager.restoreTaskFromFile(line);
                } else {
                    historyList.add(Integer.parseInt(line[1]));
                }
                fileBackedTaskManager.restoreHistoryFromFile(historyList);
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке", e);
        }
    }

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            ArrayList<Task> tasks = new ArrayList<>(super.getAllTask());
            ArrayList<Task> epics = new ArrayList<>(super.getAllEpic());
            ArrayList<Task> subTasks = new ArrayList<>(super.getAllSubTask());
            ArrayList<Task> history = new ArrayList<>(super.getHistory());
            for (Task task : tasks) {
                bufferedWriter.write(task.toFile() + "\n");
            }
            for (Task epic : epics) {
                bufferedWriter.write(epic.toFile() + "\n");
            }
            for (Task subTask : subTasks) {
                bufferedWriter.write(subTask.toFile() + "\n");
            }
            for (Task taskHistory : history) {
                bufferedWriter.write("History," + taskHistory.toFile() + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при сохранении", e);
        }
    }

    @Override
    public Task getTaskFromID(int taskID) {
        Task task = super.getTaskFromID(taskID);
        save();
        return task;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public void removeTask(Task task) {
        super.removeTask(task);
        save();
    }

    @Override
    public void updateTask(int taskID, Task newTask) {
        super.updateTask(taskID, newTask);
        save();
    }

    @Override
    public void updateEpic(int epicID, Epic newEpic) {
        super.updateEpic(epicID, newEpic);
        save();
    }

    @Override
    public void updateSubTask(int taskID, SubTask newSubTask) {
        super.updateSubTask(taskID, newSubTask);
        save();
    }

    @Override
    public void setStatusTask(Task task, Status newStatus) {
        super.setStatusTask(task, newStatus);
        save();
    }

    @Override
    public void setStatusSubTask(SubTask subTask, Status newStatus) {
        super.setStatusSubTask(subTask, newStatus);
        save();
    }

}
