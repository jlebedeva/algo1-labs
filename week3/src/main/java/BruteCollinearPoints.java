
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> segments;

    /**
     * Finds all line segments containing 4 points.
     *
     * @param points
     */
    public BruteCollinearPoints(Point[] points) {
        ArrayList<Point> list = new ArrayList<>(points.length);
        for (Point p : points) {
            if (list.contains(p)) {
                throw new IllegalArgumentException();
            }
            list.add(p);
        }
        segments = calculate(list);
    }

    /**
     * Returns the number of line segments.
     *
     * @return
     */
    public int numberOfSegments() {
        return segments.size();
    }

    /**
     * Returns the line segments.
     *
     * @return
     */
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }

    private ArrayList<LineSegment> calculate(List<Point> points) {
        ArrayList<LineSegment> segmentList = new ArrayList<>();
        Collections.sort(points);

        Point base, p1, p2, p3;
        double slope1, slope2, slope3;
        for (int i = 0; i < points.size(); i++) {
            base = points.get(i);

            for (int j = i + 1; j < points.size(); j++) {
                p1 = points.get(j);
                slope1 = base.slopeTo(p1);

                for (int k = j + 1; k < points.size(); k++) {
                    p2 = points.get(k);
                    slope2 = base.slopeTo(p2);

                    if (slope1 != slope2) {
                        continue;
                    }
                    for (int l = k + 1; l < points.size(); l++) {
                        p3 = points.get(l);
                        slope3 = base.slopeTo(p3);

                        if (slope1 == slope3) {
                            segmentList.add(new LineSegment(base, p3));
                        }
                    }
                }
            }
        }
        return segmentList;
    }
}
