
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    
    private Node<Item> first;
    private Node<Item> last;
    private int size = 0;

    /**
     * Check if the deque is empty.
     *
     * @return true if the deque has no items, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Return the number of items on the deque.
     *
     * @return number of items
     */
    public int size() {
        return size;
    }

    /**
     * Add the item to the front.
     *
     * @param item item to add
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        setFirst(new Node(item, first, null));
        size++;
    }

    /**
     * Add the item to the end.
     *
     * @param item item to add
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        setLast(new Node(item, null, last));
        size++;
    }

    /**
     * Remove and return the item from the front.
     *
     * @return
     */
    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        Item result = first.item;
        setFirst(first.next);       
        size--;
        return result;
    }

    /**
     * Remove and return the item from the end.
     *
     * @return
     */
    public Item removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        Item result = last.item;
        setLast(last.previous);
        size--;
        return result;
    }

    /**
     * Return an iterator over items in order from front to end.
     *
     * @return iterator
     */
    @Override
    public Iterator<Item> iterator() {
        return new LinkedIterator();
    }

    /**
     * Console client.
     *
     * @param args run arguments
     */
    public static void main(String[] args) {
        Deque<String> d = new Deque<>();
        d.addFirst("b");
        d.addFirst("a");
        d.addLast("c");
        System.out.println(d.removeLast());
        System.out.println(d.removeLast());
        System.out.println(d.removeFirst());
        d.addFirst("aa");
        d.addLast("bb");
        d.addLast("cc");
        for (String s : d) {
            System.out.println(s);
        }
    }

    private static class Node<Item> {

        private Item item;
        private Node next;
        private Node previous;

        public Node(Item item, Node next, Node previous) {
            this.item = item;
            this.setNext(next);
            this.setPrevious(previous);
        }

        public final void setNext(Node next) {
            this.next = next;
            if (this.next != null) {
                this.next.previous = this;
            }
        }

        public final void setPrevious(Node previous) {
            this.previous = previous;
            if (this.previous != null) {
                this.previous.next = this;
            }
        }
        
    }

    private class LinkedIterator implements Iterator<Item> {

        private Node<Item> nextNode = first;

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            //get the payload
            Item result = nextNode.item;
            //move forward 1 step
            nextNode = nextNode.next;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void setFirst(Node<Item> first) {
        this.first = first;
        //clear link to nowhere
        if (first != null && first.previous != null) {
            first.previous = null;            
        }
        //also set last, if there's < 2 items
        if (first == null || first.next == null) {
            last = first;
        }
    }

    private void setLast(Node<Item> last) {
        this.last = last;
        //clear link to nowhere
        if (last != null && last.next != null) {
            last.next = null;            
        }
        //also set first, if there's < 2 items
        if (last == null || last.previous == null) {
            first = last;
        }
    }    
}
