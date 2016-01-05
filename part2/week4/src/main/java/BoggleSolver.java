
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class BoggleSolver {

    private static final int R = 26;
    private Node root;

    private enum Status {

        WORD, PREFIX, MISS
    }

    private static class Node {

        private final Node[] next = new Node[R];
        private boolean isString;
    }

    /**
     * Initializes the data structure using the given array of strings as the
     * dictionary. (You can assume each word in the dictionary contains only the
     * uppercase letters A through Z.)
     *
     * @param dictionary
     */
    public BoggleSolver(String[] dictionary) {
        for (String key : dictionary) {
            root = dictionaryAdd(root, key, 0);
        }
    }

    private Node dictionaryAdd(Node x, String key, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == key.length()) {
            x.isString = true;
        } else {
            int c = key.charAt(d) - 65;
            x.next[c] = dictionaryAdd(x.next[c], key, d + 1);
        }
        return x;
    }

    private Node dictionaryGet(Node x, String key, int d) {
        if (x == null) {
            return null;
        }
        if (d == key.length()) {
            return x;
        }
        int c = key.charAt(d) - 65;
        return dictionaryGet(x.next[c], key, d + 1);
    }

    private Status getDictionaryStatus(String key) {
        Node x = dictionaryGet(root, key, 0);
        if (x == null) {
            return Status.MISS;
        } else if (x.isString) {
            return Status.WORD;
        } else {
            return Status.PREFIX;
        }
    }

    /**
     * Returns the set of all valid words in the given Boggle board, as an
     * Iterable.
     *
     * @param board
     * @return
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Stack<List<Integer>> prefixStack = new Stack();
        Set<String> result = new TreeSet<>();
        List<List<Integer>> adjacent = new ArrayList<>();
        for (int row = 0; row < board.rows(); row++) {
            for (int col = 0; col < board.cols(); col++) {
                List<Integer> l = new ArrayList<>();
                l.add(row * board.cols() + col);
                prefixStack.add(l);
                adjacent.add(adjacent(col, row, board.cols(), board.rows()));
            }
        }

        while (!prefixStack.isEmpty()) {
            List<Integer> path = prefixStack.pop();
            for (int candidate : adjacent.get(path.get(path.size() - 1))) {
                if (path.contains(candidate)) {
                    continue;
                }
                String s = pathToString(path, candidate, board);
                switch (getDictionaryStatus(s)) {
                    case MISS:
                        continue;
                    case WORD:
                        if (s.length() > 2) {
                            result.add(s);
                        }
                        break;
                    case PREFIX:
                        break;
                }
                List<Integer> l = new ArrayList<>(path);
                l.add(candidate);
                prefixStack.add(l);
            }
        }
        return result;
    }

    private List<Integer> adjacent(int col, int row, int cols, int rows) {
        int lastDice = row * cols + col;
        boolean hasTop = row > 0;
        boolean hasBottom = row + 1 < rows;
        boolean hasLeft = col > 0;
        boolean hasRight = col + 1 < cols;
        List<Integer> list = new ArrayList<>();
        if (hasTop) {
            list.add(lastDice - cols);
            if (hasLeft) {
                list.add(lastDice - cols - 1);
            }
            if (hasRight) {
                list.add(lastDice - cols + 1);
            }
        }
        if (hasBottom) {
            list.add(lastDice + cols);
            if (hasLeft) {
                list.add(lastDice + cols - 1);
            }
            if (hasRight) {
                list.add(lastDice + cols + 1);
            }
        }
        if (hasLeft) {
            list.add(lastDice - 1);
        }
        if (hasRight) {
            list.add(lastDice + 1);
        }
        return list;
    }

    private String pathToString(List<Integer> list, int candidate, BoggleBoard board) {
        StringBuilder sb = new StringBuilder();
        for (int i : list) {
            appendLetter(sb, i, board);
        }
        appendLetter(sb, candidate, board);
        return sb.toString();
    }

    private void appendLetter(StringBuilder sb, int index, BoggleBoard board) {
        char c = board.getLetter(index / board.cols(), index % board.cols());
        sb.append(c);
        if (c == 'Q') {
            sb.append('U');
        }
    }

    /**
     * Returns the score of the given word if it is in the dictionary, zero
     * otherwise. (You can assume the word contains only the uppercase letters A
     * through Z.)
     *
     * @param word
     * @return
     */
    public int scoreOf(String word) {
        if (getDictionaryStatus(word) != Status.WORD) {
            return 0;
        }
        switch (word.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
