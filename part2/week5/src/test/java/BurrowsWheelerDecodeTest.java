
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class BurrowsWheelerDecodeTest extends BaseTest {

    /**
     * Test of decode method, of class BurrowsWheeler.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testDecode() throws IOException {
        System.out.println("decode");
        File expectedFile = new File(RESOURCES + "abra.txt");
        FileInputStream expectedFileIn = new FileInputStream(expectedFile);
        byte[] expected = new byte[expectedFileIn.available()];
        expectedFileIn.read(expected);

        File f = new File(RESOURCES + "abra.txt.bwt");
        FileInputStream testIn = new FileInputStream(f);
        System.setIn(testIn);

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        BurrowsWheeler.decode();

        byte[] bytes = testOut.toByteArray();
        assertArrayEquals(expected, bytes);
    }

}
