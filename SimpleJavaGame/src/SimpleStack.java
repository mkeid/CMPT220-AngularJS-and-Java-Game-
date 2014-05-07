import java.util.LinkedList;
import java.util.Observable;

/**
 * Created by mohamedeid on 5/6/14.
 */
public class SimpleStack {
    private LinkedList list;

    // Constructor
    public SimpleStack() {
        this.list = new LinkedList();
    }

    public SimpleStack(Object object) {
        this.list = new LinkedList();
        this.push(object);
    }

    public boolean isEmpty() {
        return this.list.size() == 0;
    }

    public void push(Object item) {
        this.list.add(item);
    }

    public Object pop() {
        Object object = this.list.getLast();
        this.list.remove(list.size() - 1);
        return object;
    }

    public Object peek() {
        return this.list.get(list.size());
    }
}
