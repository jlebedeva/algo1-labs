
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Picture;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class SeamCarverTest {

    private static final String RESOURCE_DIR = "src/test/resources/";       
    
    /**
     * Print Energy.
     */
    @Test
    public void printEnergy() {
        System.out.println("printEnergy");
        PrintEnergy.main(new String[]{"src/test/resources/10x10.png"});
    }
    
    /**
     * Print Seams.
     */
    @Test
    public void printSeams() {
        System.out.println("printSeams");

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream defaultOut = System.out;
        System.setOut(new PrintStream(testOut));

        PrintSeams.main(new String[]{RESOURCE_DIR + "10x10.png"});
        
        System.setOut(defaultOut);
        String result = testOut.toString();
        System.out.print(result);
        
        assertEquals(RESOURCE_DIR + new In(RESOURCE_DIR + "10x10.printseams.txt").readAll(), result);        
    }
    
    @Test
    public void testRemoveSeam() {
        Picture p = new Picture(RESOURCE_DIR + "7x10.png");
        SeamCarver sc = new SeamCarver(p);
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        sc.picture();
        sc.removeVerticalSeam(sc.findVerticalSeam());
        sc.removeVerticalSeam(sc.findVerticalSeam());
        sc.removeVerticalSeam(sc.findVerticalSeam());
        sc.findHorizontalSeam();
    }
}
