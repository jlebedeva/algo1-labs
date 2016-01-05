
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.Arrays;

public class BurrowsWheeler {

    private static final int R = 256;

    /**
     * Apply Burrows-Wheeler encoding, reading from standard input and writing
     * to standard output.
     */
    public static void encode() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int indexOfOriginal = -1;
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                indexOfOriginal = i;
                break;
            }
        }
        BinaryStdOut.write(indexOfOriginal);
        for (int i = 0; i < csa.length(); i++) {
            BinaryStdOut.write(s.charAt((csa.index(i) - 1 + csa.length()) % csa.length()));
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    /**
     * Apply Burrows-Wheeler decoding, reading from standard input and writing
     * to standard output.
     */
    public static void decode() {
        int first = BinaryStdIn.readInt();
        char[] lastColumn = BinaryStdIn.readString().toCharArray();
        char[] firstColumn = Arrays.copyOf(lastColumn, lastColumn.length);
        Arrays.sort(firstColumn);

        int[] next = new int[lastColumn.length];
        // index first column
        int[] starts = new int[R];
        for (int i = 0; i < starts.length; i++) {
            starts[i] = -1;
        }
        for (int i = 0; i < firstColumn.length; i++) {
            char c = firstColumn[i];
            if (starts[c] == -1) {
                starts[c] = i;
            }
        }
        // fill next
        for (int i = 0; i < lastColumn.length; i++) {
            char c = lastColumn[i];
            next[starts[c]++] = i;
        }

        // iterate next
        int nextCursor = first;
        for (int i = 0; i < lastColumn.length; i++) {
            BinaryStdOut.write(firstColumn[nextCursor]);
            nextCursor = next[nextCursor];
            if (nextCursor == first) {
                break;
            }
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    /**
     * If args[0] is '-', apply Burrows-Wheeler encoding, if args[0] is '+',
     * apply Burrows-Wheeler decoding.
     *
     * @param args
     */
    public static void main(String[] args) {
        String s = args[0];
        switch (s) {
            case "+":
                decode();
                break;
            case "-":
                encode();
                break;
        }
    }
}
