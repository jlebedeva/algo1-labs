
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.junit.Test;

public class RunSubset {

    @Test
    public void runSubset() throws UnsupportedEncodingException {
        //replace System.in with custom data stream
        String data = "A B C D E F G H I";
        InputStream testInput = new ByteArrayInputStream(data.getBytes("UTF-8"));
        System.setIn(testInput);
        
        //run console client
        String sampleSize = "5";
        Subset.main(new String[] {sampleSize});
    }
}
