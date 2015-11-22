
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jlebedeva
 */
public class KdTreeTest {

    public KdTreeTest() {
    }

    /**
     * Test of isEmpty method, of class KdTree.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        KdTree instance = new KdTree();
        boolean expResult = true;
        boolean result = instance.isEmpty();
        assertEquals(expResult, result);

        addRandomTwenty(instance);
        expResult = false;
        result = instance.isEmpty();
        assertEquals(expResult, result);
    }

    /**
     * Test of size method, of class KdTree.
     */
    @Test
    public void testSize() {
        System.out.println("size");
        KdTree instance = new KdTree();
        int expResult = 0;
        int result = instance.size();
        assertEquals(expResult, result);

        addRandomTwenty(instance);
        expResult = 20;
        result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of insert & contains methods, of class KdTree.
     */
    @Test
    public void testInsertContains() {
        System.out.println("insert & contains");        
        KdTree instance = new KdTree();

        boolean expResult = false;
        boolean result = instance.contains(new Point2D(0.2, 0.2));
        assertEquals(expResult, result);

        List<Point2D> list = addRandomTwenty(instance);
        expResult = true;
        for (Point2D p : list) {
            result = instance.contains(p);
            assertEquals(expResult, result);
        }
    }
    
        /**
     * Test of insert for 2 equal points, of class KdTree.
     */
    @Test
    public void testInsertEquals() {
        System.out.println("insert equals");        
        KdTree instance = new KdTree();

        instance.insert(new Point2D(0.2, 0.2));
        instance.insert(new Point2D(0.2, 0.2));
        int expResult = 1;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of draw method, of class KdTree.
     */
    @Test
    public void testDraw() {
        System.out.println("draw");
        StdDraw.show(0);
        KdTree instance = new KdTree();
        addRandomTwenty(instance);
        instance.draw();
        StdDraw.show(0);
        StdDraw.show(40);
    }

    /**
     * Test of range method, of class KdTree.
     */
    @Test
    public void testRange() {
        System.out.println("range");
        KdTree instance = new KdTree();
        addFour(instance);

        Iterable<Point2D> result = instance.range(new RectHV(0, 0, 1, 1));
        int expResultCounter = 4;
        int resultCounter = 0;
        for (Point2D p : result) {
            resultCounter++;
        }
        assertEquals(expResultCounter, resultCounter);

        result = instance.range(new RectHV(0, 0, 0.4, 0.4));
        expResultCounter = 1;
        resultCounter = 0;
        for (Point2D p : result) {
            resultCounter++;
        }
        assertEquals(expResultCounter, resultCounter);
    }

    /**
     * Test of nearest method, of class KdTree.
     */
    @Test
    public void testNearest() {
        System.out.println("nearest");
        KdTree instance = new KdTree();
        addFour(instance);

        Point2D target = new Point2D(0.9, 0.9);
        Point2D expResult = new Point2D(0.75, 0.75);
        Point2D result = instance.nearest(target);
        assertEquals(expResult, result);
    }

    private List<Point2D> addRandomTwenty(KdTree instance) {
        List<Point2D> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Point2D p = new Point2D(StdRandom.uniform(), StdRandom.uniform());
            list.add(p);
            instance.insert(p);
        }
        return list;
    }

    private void addFour(KdTree instance) {
        instance.insert(new Point2D(0.7, 0.25));
        instance.insert(new Point2D(0.5, 0.5));
        instance.insert(new Point2D(0.75, 0.75));
        instance.insert(new Point2D(0.25, 0.25));
    }

}
