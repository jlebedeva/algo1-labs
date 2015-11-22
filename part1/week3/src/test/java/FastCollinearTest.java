
public class FastCollinearTest extends CollinearTest {

    @Override
    protected LineSegment[] runTest(Point[] points) {
        FastCollinearPoints testObject = new FastCollinearPoints(points);
        return testObject.segments();
    }
}
