
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.junit.Test;
import static org.junit.Assert.*;

public class MoveToFrontEncodeTest extends BaseTest {

    /**
     * Test of encode method, of class MoveToFront.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testEncode() throws IOException {
        System.out.println("encode");

        File expectedFile = new File(RESOURCES + "abra.txt.mtf");
        FileInputStream expectedFileIn = new FileInputStream(expectedFile);
        byte[] expected = new byte[expectedFileIn.available()];
        expectedFileIn.read(expected);

        File f = new File(RESOURCES + "abra.txt");
        FileInputStream testIn = new FileInputStream(f);
        System.setIn(testIn);

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));

        MoveToFront.encode();

        byte[] bytes = testOut.toByteArray();
        assertArrayEquals(expected, bytes);
    }

}
