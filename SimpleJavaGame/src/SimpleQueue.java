import java.util.LinkedList;

/**
 * Created by mohamedeid on 5/6/14.
 */
public class SimpleQueue {
    private LinkedList list;

    // Constructors
    public SimpleQueue() {
        this.list = new LinkedList();
    }

    public SimpleQueue(Object object) {
        this.list = new LinkedList();
        this.enqueue(object);
    }

    public boolean isEmpty() {
        return this.list.size() == 0;
    }

    public void enqueue(Object object) {
        this.list.add(object);
    }

    public Object dequeue() {
        Object object = this.list.getFirst();
        this.list.remove(0);
        return object;
    }

    public Object peek() {
        return this.list.getLast();
    }

}
