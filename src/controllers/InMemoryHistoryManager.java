package controllers;

import model.Node;
import model.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager{
    private final HandMadeList listNode = new HandMadeList();

    @Override
    public void addToHistory(Task task){
        listNode.linkLast(task);
    }

    @Override
    public void remove(int id){
        listNode.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<Task>(listNode.getTasks());
    }

    public class HandMadeList<T> {
        public Node<T> head;
        public Node<T> tail;
        HashMap<Integer, Node<T>> nodeMap = new HashMap<>();

        private void linkLast(Task task){
           Node<T> node = new Node<>(task);
           if (nodeMap.isEmpty()){
               nodeMap.put(task.getID(),node);
               this.head = node;
           } else if (nodeMap.containsKey(task.getID())) { //что если ID уже есть в мапе?
               Node<T> oldNode = nodeMap.get(task.getID());
               if (this.head.equals(oldNode)){
                   if(nodeMap.size() == 1){
                       this.head = node;
                   } else if (nodeMap.size() == 2){
                       this.tail.setPrev(null);
                       this.head = this.tail;
                       this.head.setNext(node);
                       this.tail = node;
                       this.tail.setPrev(this.head);
                   } else {
                        this.head.getNext().setPrev(null);
                        this.head = this.head.getNext();
                        this.tail.setNext(node);
                        node.setPrev(this.tail);
                        this.tail = node;
                   }
               } else if (this.tail == oldNode){
                   this.tail.getPrev().setNext(node);
                   node.setPrev(this.tail.getPrev());
                   this.tail = node;
               } else {
                   oldNode.getPrev().setNext(oldNode.getNext());
                   oldNode.getNext().setPrev(oldNode.getPrev());
                   this.tail.setNext(node);
                   node.setPrev(this.tail);
                   this.tail = node;
               }
               nodeMap.put(task.getID(), node);
           }else if(nodeMap.size() == 1) {
               nodeMap.put(task.getID(), node);
               this.tail = node;
               this.head.setNext(node);
               node.setPrev(this.head);
           } else {
               nodeMap.put(task.getID(),node);
               this.tail.setNext(node);
               node.setPrev(this.tail);
               this.tail = node;
           }
        }

        private void remove(int id){
            if (nodeMap.containsKey(id)){
                if (nodeMap.size() == 1){
                    this.head = null;
                    nodeMap.remove(id);
                } else if (nodeMap.size() == 2) {
                    this.tail = null;
                    this.head.setNext(null);
                    nodeMap.remove(id);
                } else {
                    if (nodeMap.get(id).equals(this.head)){
                        this.head.getNext().setPrev(null);
                        this.head = this.head.getNext();
                        nodeMap.remove(id);
                    } else if (nodeMap.get(id).equals(this.tail)){
                        this.tail.getPrev().setNext(null);
                        this.tail = this.tail.getPrev();
                        nodeMap.remove(id);
                    } else {
                        Node<T> removeNode = nodeMap.get(id);
                        removeNode.getPrev().setNext(removeNode.getNext());
                        removeNode.getNext().setPrev(removeNode.getPrev());
                        nodeMap.remove(id);
                    }
                }
            }
        }

        private ArrayList<Task> getTasks(){
            ArrayList<Task> historyList = new ArrayList<>();
            if (!nodeMap.isEmpty()) {
                Node<T> node = head;
                Task task = node.getData();
                while(true){
                    historyList.add(task);
                    node = node.getNext();
                    if (node == null){
                        break;
                    }
                    task = node.getData();
                }
                return historyList;
            }
            return historyList;
        }
    }

}
