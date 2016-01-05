
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.Before;

public class BaseTest {
    public static final String RESOURCES = "src/test/resources/";
    
    private PrintStream defaultOut;
    private InputStream defaultIn;

    @Before
    public void setUpStreams() {
        defaultOut = System.out;
        defaultIn = System.in;
    }

    @After    
    public void cleanUpStreams() {
        System.setOut(defaultOut);
        System.setIn(defaultIn);
        defaultOut = null;
        defaultIn = null;
    }

}
