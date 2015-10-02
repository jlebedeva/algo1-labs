
import static org.junit.Assert.*;
import org.junit.Test;

public class PointTest {

    @Test
    public void compareToByY() {
        Point point = new Point(25, 31);
        Point equalPoint = new Point(25, 31);
        Point higherPoint = new Point(11, 57);
        Point zeroPoint = new Point(0, 0);

        int result = point.compareTo(equalPoint);
        assertEquals(0, result);

        result = point.compareTo(higherPoint);
        assertEquals(-1, result);

        result = higherPoint.compareTo(point);
        assertEquals(1, result);

        result = zeroPoint.compareTo(point);
        assertEquals(-1, result);

        result = point.compareTo(zeroPoint);
        assertEquals(1, result);
    }

    @Test
    public void compareToTieByX() {
        Point point = new Point(25, 31);
        Point leftTiePoint = new Point(5, 31);
        Point rightTiePoint = new Point(55, 31);

        int result = point.compareTo(rightTiePoint);
        assertEquals(-1, result);

        result = point.compareTo(leftTiePoint);
        assertEquals(1, result);
    }

    @Test
    public void slopeTo() {
        Point point = new Point(0, 3);
        Point another = new Point(6, 0);
        Point zeroPoint = new Point(0, 0);

        double slope = point.slopeTo(another);
        assertEquals(-0.5, slope, 0.0);

        slope = another.slopeTo(point);
        assertEquals(-0.5, slope, 0.0);

        slope = point.slopeTo(point);
        assertEquals(Double.NEGATIVE_INFINITY, slope, 0.0);

        slope = another.slopeTo(zeroPoint);
        assertEquals(0, slope, 0.0);

        slope = point.slopeTo(zeroPoint);
        assertEquals(Double.POSITIVE_INFINITY, slope, 0.0);
    }
}
