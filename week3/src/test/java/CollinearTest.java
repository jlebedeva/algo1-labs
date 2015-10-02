
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public abstract class CollinearTest {

    @Test
    public void findThreeLines() {
        ArrayList<Point> points = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if (i != 0) {
                points.add(point(i, 0));
                points.add(point(0, i));
            }
            points.add(point(i, i));
        }

        //outlier
        points.add(point(1, 100));

        LineSegment[] res = runTest(points.toArray(new Point[points.size()]));
        System.out.println(Arrays.toString(res));
        assertEquals(3, res.length);
    }

    public static Point point(int x, int y) {
        return new Point(x, y);
    }

    protected abstract LineSegment[] runTest(Point[] points);
}
