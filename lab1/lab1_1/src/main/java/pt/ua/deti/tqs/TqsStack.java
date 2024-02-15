package pt.ua.deti.tqs;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TqsStack<T> {
    private final LinkedList<T> collection;
    private final int LIMIT = 10;

    public TqsStack() {
        this.collection = new LinkedList<>();
    }

    public T pop() {
        return collection.pop();
    }

    public int size() {
        return collection.size();
    }

    public T peek() {
        if (collection.isEmpty()) {
            throw new NoSuchElementException();
        }
        return collection.peek();
    }

    public void push(T element) {
        if (this.size() == LIMIT) {
            throw new IllegalStateException("Limit exceeded");
        }
        collection.push(element);
    }

    public boolean isEmpty() {
        return collection.isEmpty();
    }
}
