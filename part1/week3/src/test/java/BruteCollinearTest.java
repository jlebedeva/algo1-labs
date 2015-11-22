
public class BruteCollinearTest extends CollinearTest {

    @Override
    protected LineSegment[] runTest(Point[] points) {
        BruteCollinearPoints testObject = new BruteCollinearPoints(points);
        return testObject.segments();
    }

}
