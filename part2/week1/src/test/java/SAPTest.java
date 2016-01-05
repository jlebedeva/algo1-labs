
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import org.junit.Test;

public class SAPTest {

    /**
     * Test of main method, of class SAP.
     *
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void testMain() throws UnsupportedEncodingException {
        System.out.println("main");
        //replace System.in with custom data stream
        String data = "3 11\n9 12\n7 2\n1 6";
        InputStream testInput = new ByteArrayInputStream(data.getBytes("UTF-8"));
        System.setIn(testInput);

        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource("digraph1.txt");
        String[] args = {url.getPath()};
        SAP.main(args);
    }
}
