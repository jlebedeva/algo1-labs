
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jlebedeva
 */
public class PointSETTest {

    public PointSETTest() {
    }

    /**
     * Test of isEmpty method, of class PointSET.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        PointSET instance = new PointSET();
        boolean expResult = true;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);

        addRandomTwenty(instance);
        expResult = false;
        result = instance.isEmpty();
        assertEquals(expResult, result);
    }

    /**
     * Test of size method, of class PointSET.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        PointSET instance = new PointSET();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);

        addRandomTwenty(instance);
        expResult = 20;
        result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of insert & contains methods, of class PointSET.
     */
    @Test
    public void testInsertContains() {
        System.out.println("insert & contains");
        Point2D p = new Point2D(0.3, 0.2);
        PointSET instance = new PointSET();

        boolean expResult = false;
        boolean result = instance.contains(p);
        assertEquals(expResult, result);

        instance.insert(p);
        expResult = true;
        result = instance.contains(p);
        assertEquals(expResult, result);
    }

    /**
     * Test of draw method, of class PointSET.
     */
    @Test
    public void testDraw() {
        System.out.println("draw");
        StdDraw.show(0);
        PointSET instance = new PointSET();
        addRandomTwenty(instance);
        instance.draw();
        StdDraw.show(0);
        StdDraw.show(40);
    }

    /**
     * Test of range method, of class PointSET.
     */
    @Test
    public void testRange() {
        System.out.println("range");
        PointSET instance = new PointSET();
        addFour(instance);
        
        Iterable<Point2D> result = instance.range(new RectHV(0, 0, 1, 1));
        int expResultCounter = 4;
        int resultCounter = 0;
        for (Point2D p : result) {
            resultCounter++;
        }
        assertEquals(expResultCounter, resultCounter);
        
        result = instance.range(new RectHV(0, 0, 0.5, 0.5));
        expResultCounter = 1;
        resultCounter = 0;
        for (Point2D p : result) {
            resultCounter++;
        }
        assertEquals(expResultCounter, resultCounter);
    }

    /**
     * Test of nearest method, of class PointSET.
     */
    @Test
    public void testNearest() {
        System.out.println("nearest");
        PointSET instance = new PointSET();
        addFour(instance);
        
        Point2D target = new Point2D(0.9, 0.9);
        Point2D expResult = new Point2D(0.75, 0.75);
        Point2D result = instance.nearest(target);
        assertEquals(expResult, result);
    }

    private void addRandomTwenty(PointSET instance) {
        for (int i = 0; i < 20; i++) {
            instance.insert(new Point2D(StdRandom.uniform(), StdRandom.uniform()));
        }
    }

    private void addFour(PointSET instance) {
        instance.insert(new Point2D(0.25, 0.25));
        instance.insert(new Point2D(0.25, 0.75));
        instance.insert(new Point2D(0.75, 0.25));
        instance.insert(new Point2D(0.75, 0.75));
    }

}
