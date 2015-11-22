
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Solver {

    private final Board board;

    private MinPQ<SearchNode> queue;
    private int moves;
    private List<Board> solution = new ArrayList<>();

    /**
     * Find a solution to the initial board (using the A* algorithm)
     *
     * @param initial
     */
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }
        board = initial;

        // board is solved
        if (board.isGoal()) {
            solution.add(board);
            moves = 0;
        } else {
            SearchNode goal = findSolution();
            moves = goal.moves;
            while (goal != null) {
                solution.add(goal.board);
                goal = goal.previous;
            }
            Collections.reverse(solution);

            // if the twin wins
            if (!solution.get(0).equals(board)) {
                solution = null;
                moves = -1;
            }
        }
    }

    /**
     * Is the initial board solvable?
     *
     * @return
     */
    public boolean isSolvable() {
        return moves >= 0;
    }

    /**
     * Min number of moves to solve initial board; -1 if unsolvable
     *
     * @return
     */
    public int moves() {
        return moves;
    }

    /**
     * Sequence of boards in a shortest solution; null if unsolvable
     *
     * @return
     */
    public Iterable<Board> solution() {
        return solution;
    }

    /**
     * Solve a slider puzzle from file given file name
     *
     * @param args
     */
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

    private SearchNode findSolution() {
        queue = new MinPQ<>(SearchNode.priorityComparator());
        queue.insert(new SearchNode(board, 0, null));
        queue.insert(new SearchNode(board.twin(), 0, null));

        while (true) {
            SearchNode candidate = queue.delMin();
            for (Board b : candidate.board.neighbors()) {
                if (b.isGoal()) {
                    return new SearchNode(b, candidate.moves + 1, candidate);
                } else if (candidate.previous == null
                        || !b.equals(candidate.previous.board)) {
                    queue.insert(new SearchNode(b, candidate.moves + 1, candidate));
                }
            }
        }
    }

    private static class SearchNode {

        private final Board board;
        private final int moves;
        private final int priority;
        private final SearchNode previous;

        public SearchNode(Board b, int moves, SearchNode previous) {
            this.board = b;
            this.moves = moves;
            this.priority = moves + board.manhattan();
            this.previous = previous;
        }

        public static Comparator<SearchNode> priorityComparator() {
            return new Comparator<SearchNode>() {

                @Override
                public int compare(SearchNode o1, SearchNode o2) {
                    return Integer.compare(o1.priority, o2.priority);
                }
            };
        }
    }
}
