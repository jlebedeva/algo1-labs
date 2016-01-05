
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.ArrayList;
import java.util.List;

public class MoveToFront {
    private static final int R = 256;

    /**
     * Apply move-to-front encoding, reading from standard input and writing to
     * standard output.
     */
    public static void encode() {
        List<Character> list = new ArrayList<>();
        for (int i = R - 1; i >= 0; i--) {
            list.add((char) i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = list.indexOf(c);
            BinaryStdOut.write(R - 1 - index, 8);
            list.remove(index);
            list.add(c);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    /**
     * Apply move-to-front decoding, reading from standard input and writing to
     * standard output.
     */
    public static void decode() {
        List<Character> list = new ArrayList<>();
        for (int i = R - 1; i >= 0; i--) {
            list.add((char) i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char indexChar = BinaryStdIn.readChar();
            int index = R - 1 - indexChar;
            char c = list.remove(index);
            BinaryStdOut.write(c);
            list.add(c);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    /**
     * If args[0] is '-', apply move-to-front encoding, if args[0] is '+', apply
     * move-to-front decoding.
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
