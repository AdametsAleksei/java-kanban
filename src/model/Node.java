package model;

public class Node<T> {
    private Node<T> prev;
    private Node<T> next;
    private final Task data;

    public Node(Task data) {
        this.data = data;
    }

    public void setPrev(Node<T> node) {
        this.prev = node;
    }

    public void setNext(Node<T> node) {
        this.next = node;
    }

    public Node<T> getPrev() {
        return this.prev;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public Task getData() {
        return this.data;
    }
}
