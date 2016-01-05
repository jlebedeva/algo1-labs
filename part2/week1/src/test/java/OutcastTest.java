
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.*;
import org.junit.Test;

public class OutcastTest {

    /**
     * Test of main method, of class Outcast.
     */
    @Test
    public void testMain() {
        System.out.println("main");

        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream defaultOut = System.out;
        System.setOut(new PrintStream(testOut));

        String[] args = {"synsets.txt", "hypernyms.txt",
            "outcast5.txt", "outcast8.txt", "outcast11.txt"};
        ClassLoader classLoader = getClass().getClassLoader();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            args[i] = classLoader.getResource(arg).getPath();
        }
        Outcast.main(args);

        System.setOut(defaultOut);
        String result = testOut.toString();
        System.out.print(result);

        assertEquals(toOutcastPrintFormat(args[2], "table")
                + toOutcastPrintFormat(args[3], "bed")
                + toOutcastPrintFormat(args[4], "potato"), result);
    }

    private static String toOutcastPrintFormat(String filename, String outcast) {
        return new StringBuilder(filename).append(": ").append(outcast)
                .append(System.lineSeparator()).toString();
    }

}
