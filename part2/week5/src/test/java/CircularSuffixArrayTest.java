
import org.junit.Test;
import static org.junit.Assert.*;

public class CircularSuffixArrayTest {

    /**
     * Test of index method, of class CircularSuffixArray.
     */
    @Test
    public void testIndex() {
        System.out.println("index");
        CircularSuffixArray instance = new CircularSuffixArray("ABRACADABRA!");
        assertEquals(11, instance.index(0));
        assertEquals(2, instance.index(11));
    }

}
