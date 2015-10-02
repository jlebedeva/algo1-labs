
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> segments;

    /**
     * Finds all line segments containing 4 or more points.
     *
     * @param points
     */
    public FastCollinearPoints(Point[] points) {
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

        Point[] sortedBySlope = new Point[points.size()];
        points.toArray(sortedBySlope);

        double currentSlope, lastSlope = 0;
        int seen;
        Point s;
        LineSegment segment;

        for (Point p : points) {
            seen = 0;
            Arrays.sort(sortedBySlope, p.slopeOrder());
            for (int j = 0; j < sortedBySlope.length; j++) {
                s = sortedBySlope[j];
                if (p.compareTo(s) == 0) {
                    continue;
                }
                currentSlope = p.slopeTo(s);

                if (j > 0) {
                    //there was a chance to see any lastSlope
                    if (currentSlope == lastSlope) {
                        seen++;

                        if (j + 1 == sortedBySlope.length && seen > 2) {
                            //there's no more, finalize
                            segment = createSegment(sortedBySlope, p, j + 1 - seen, j);
                            if (segment != null) {
                                segmentList.add(segment);
                            }
                        }
                        //collect more slopes (or finish with this p)
                        continue;

                    } else if (seen > 2) {
                        //another slope value already seen, finalize
                        segment = createSegment(sortedBySlope, p, j - seen, j - 1);
                        if (segment != null) {
                            segmentList.add(segment);
                        }
                    }
                }

                //start counting points for a new slope value
                lastSlope = currentSlope;
                seen = 1;
            }
        }
        return segmentList;
    }

    private LineSegment createSegment(Point[] source, Point base, int from, int to) {
        //to (exclusive)
        Arrays.sort(source, from, to + 1);
        //we are analyzing the edge point
        if (base.compareTo(source[from]) < 0) {
            return new LineSegment(base, source[to]);
        } else {
            return null;
        }
    }
}
