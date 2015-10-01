
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    /**
     * Resizable array
     */
    private Item[] items;
    /**
     * Pointer to the first empty slot in the array
     */
    private int next = 0;
    /**
     * Flag that allows iterator snapshots avoid downsizing the array
     */
    private final boolean shrinkable;

    public RandomizedQueue() {
        this(1, true);
    }

    private RandomizedQueue(int capacity, boolean shrinkable) {
        this.items = (Item[]) new Object[capacity];
        this.shrinkable = shrinkable;
    }

    /**
     * Check if the queue empty.
     *
     * @return true if there's no items, false otherwise
     */
    public boolean isEmpty() {
        return next == 0;
    }

    /**
     * Return the number of items on the queue.
     *
     * @return number of items in the queue
     */
    public int size() {
        return next;
    }

    /**
     * Add the item.
     *
     * @param item new item to add
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        //resize if no space left
        if (next >= items.length) {
            resize(items.length * 2);
        }
        items[next++] = item;
    }

    /**
     * Remove and return a random item.
     *
     * @return random item
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        //get random item
        int idx = StdRandom.uniform(next);
        Item result = items[idx];

        //rewrite the item in the array with the most recent one
        items[idx] = items[--next];
        items[next] = null;

        //resize if array is more then 3/4 empty
        if (shrinkable && (next < items.length / 4)) {
            resize(items.length / 2);
        }
        return result;
    }

    /**
     * Return (but do not remove) a random item.
     *
     * @return random item
     */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniform(next)];
    }

    /**
     * Return an independent iterator over items in random order.
     *
     * @return iterator with randomized access
     */
    @Override
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    /**
     * Sample client.
     *
     * @param args run arguments
     */
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("apple1");
        rq.enqueue("apple2");
        rq.enqueue("apple3");
        for (String s : rq) {
            System.out.println(s);
        }
    }

    /**
     * Resize items array.
     *
     * @param capacity new array length
     */
    private void resize(int capacity) {
        Item[] newItems = (Item[]) new Object[capacity];
        for (int i = 0; i < next; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    private class RandomizedIterator implements Iterator<Item> {

        private final RandomizedQueue<Item> snapshot;

        public RandomizedIterator() {
            snapshot = new RandomizedQueue<>(next, false);
            for (int i = 0; i < next; i++) {
                snapshot.enqueue(items[i]);
            }
        }

        @Override
        public boolean hasNext() {
            return !snapshot.isEmpty();
        }

        @Override
        public Item next() {
            return snapshot.dequeue();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
