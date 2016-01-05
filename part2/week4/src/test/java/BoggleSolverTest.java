
import edu.princeton.cs.algs4.In;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoggleSolverTest {

    private static final String RESOURCES = "src/test/resources/";

    /**
     * Test of main method, of class BoggleSolver.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        BoggleSolver.main(new String[]{
            RESOURCES + "dictionary-algs4.txt",
            RESOURCES + "board4x4.txt"});
    }

    /**
     * Test of main method, of class BoggleSolver.
     */
    @Test
    public void testQ() {
        System.out.println("main");
        BoggleSolver.main(new String[]{
            RESOURCES + "dictionary-algs4.txt",
            RESOURCES + "board-q.txt"});
    }

    @Test
    public void testPerformance() {
        System.out.println("performance");

        In in = new In(RESOURCES + "dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver bs = new BoggleSolver(dictionary);

        int runs = 1000;
        BoggleBoard[] testSet = new BoggleBoard[runs];
        for (int i = 0; i < runs; i++) {
            testSet[i] = new BoggleBoard(10, 10);
        }

        long start = System.currentTimeMillis();
        for (BoggleBoard bb : testSet) {
            bs.getAllValidWords(bb);
        }

        double average = ((double) System.currentTimeMillis() - start) / runs;
        System.out.println("average 10x10: " + average);
        assertTrue("average 10x10 is less than 2 ms", average < 2);
    }

}
