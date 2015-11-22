
import edu.princeton.cs.algs4.StdIn;

public class Subset {

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        String s;
        
        while (!StdIn.isEmpty()) {
            s = StdIn.readString();
            rq.enqueue(s);
        }
        
        for (int i = 0; i < n; i++) {
            System.out.println(rq.dequeue());
        }
    }
}
