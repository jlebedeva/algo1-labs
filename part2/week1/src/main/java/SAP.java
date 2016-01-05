
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Shortest ancestral path. An ancestral path between two vertices v and w in a
 * digraph is a directed path from v to a common ancestor x, together with a
 * directed path from w to the same ancestor x. A shortest ancestral path is an
 * ancestral path of minimum total length.
 */
public class SAP {

    private final Digraph G;

    /**
     *
     * @param G a digraph (not necessarily a DAG)
     */
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    /**
     * length of shortest ancestral path between v and w; -1 if no such path
     *
     * @param v
     * @param w
     * @return
     */
    public int length(int v, int w) {
        return length(Arrays.asList(v), Arrays.asList(w));
    }

    /**
     * a common ancestor of v and w that participates in a shortest ancestral
     * path; -1 if no such path
     *
     * @param v
     * @param w
     * @return
     */
    public int ancestor(int v, int w) {
        return ancestor(Arrays.asList(v), Arrays.asList(w));
    }

    /**
     * length of shortest ancestral path between any vertex in v and any vertex
     * in w; -1 if no such path
     *
     * @param v
     * @param w
     * @return
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        DistanceNode n = calculate(v, w);
        if (n == null) {
            return -1;
        } else {
            return n.distance;
        }
    }

    /**
     * a common ancestor that participates in shortest ancestral path; -1 if no
     * such path
     *
     * @param v
     * @param w
     * @return
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        DistanceNode n = calculate(v, w);
        if (n == null) {
            return -1;
        } else {
            return n.index;
        }
    }

    /**
     * The following test client takes the name of a digraph input file as as a
     * command-line argument, constructs the digraph, reads in vertex pairs from
     * standard input, and prints out the length of the shortest ancestral path
     * between the two vertices and a common ancestor that participates in that
     * path
     *
     * @param args
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    /**
     *
     * @param v
     * @param w
     * @return
     */
    private DistanceNode calculate(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, w);

        Queue<DistanceNode> q = new Queue<>();
        Map<Integer, Boolean> marked = new HashMap<>();
        for (int sourceNodeIndex : v) {
            marked.put(sourceNodeIndex, true);
            q.enqueue(new DistanceNode(sourceNodeIndex, 0));
        }

        List<DistanceNode> candidates = new ArrayList<>();
        DistanceNode n;
        while (!q.isEmpty()) {
            n = q.dequeue();
            if (bfs.hasPathTo(n.index)) {
                candidates.add(new DistanceNode(n.index, n.distance + bfs.distTo(n.index)));
            }
            for (int childIndex : G.adj(n.index)) {
                if (marked.get(childIndex) == null) {
                    marked.put(childIndex, true);
                    q.enqueue(new DistanceNode(childIndex, n.distance + 1));
                }
            }
        }
        DistanceNode min = null;
        for (DistanceNode candidate : candidates) {
            if (min == null || min.distance > candidate.distance) {
                min = candidate;
            }
        }
        return min;
    }

    private static class DistanceNode {

        private int index;
        private int distance;

        public DistanceNode(int index, int distance) {
            this.index = index;
            this.distance = distance;
        }

    }

}
