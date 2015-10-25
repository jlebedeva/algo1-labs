
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private Node root;
    private int size = 0;

    /**
     * is the set empty?
     *
     * @return
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * number of points in the set
     *
     * @return
     */
    public int size() {
        return size;
    }

    /**
     * add the point to the set (if it is not already in the set)
     *
     * @param p
     */
    public void insert(final Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        } else {
            if (root == null) {
                size++;
                root = new Node(p, true, new RectHV(0, 0, 1, 1));
            } else {
                root.put(p);
            }
        }
    }

    /**
     * does the set contain point p?
     *
     * @param p
     * @return
     */
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new NullPointerException();
        } else if (root == null) {
            return false;
        } else {
            return root.contains(p);
        }
    }

    /**
     * draw all points to standard draw
     */
    public void draw() {
        root.draw();
    }

    /**
     * all points that are inside the rectangle
     *
     * @param rect
     * @return
     */
    public Iterable<Point2D> range(final RectHV rect) {
        if (root == null) {
            return new ArrayList<>();
        }
        RangeCommand c = new RangeCommand(rect);
        c.execute(root);
        return c.list;
    }

    /**
     * a nearest neighbor in the set to point p; null if the set is empty
     *
     * @param p
     * @return
     */
    public Point2D nearest(Point2D p) {
        if (root == null) {
            return null;
        }
        NearestCommand c = new NearestCommand(p);
        c.execute(root);
        return c.candidate;
    }

    private static class NearestCommand {

        private Point2D candidate;
        private double candidateDistance;
        private final Point2D target;

        public NearestCommand(Point2D target) {
            this.target = target;
        }

        public void execute(Node n) {
            //stop search if node area is too far
            double d = n.rect.distanceSquaredTo(target);
            if (d > candidateDistance && candidate != null) {
                return;
            }
            
            //reassign candidate if found better
            d = n.point.distanceSquaredTo(target);
            if (candidate == null || d < candidateDistance) {
                candidate = n.point;
                candidateDistance = d;
            }

            //choose a subtree to continue
            if (n.lb != null && n.rt != null) {
                d = n.lb.rect.distanceSquaredTo(target);
                double d2 = n.rt.rect.distanceSquaredTo(target);
                if (d < d2) {
                    execute(n.lb);
                    execute(n.rt);
                } else {
                    execute(n.rt);
                    execute(n.lb);
                }
            } else {
                if (n.lb != null) {
                    execute(n.lb);
                }
                if (n.rt != null) {
                    execute(n.rt);
                }
            }
        }
    }

    private static class RangeCommand {

        private final List<Point2D> list = new ArrayList<>();
        private final RectHV rect;

        public RangeCommand(RectHV rect) {
            this.rect = rect;
        }

        public void execute(Node n) {
            if (!n.rect.intersects(rect)) {
                return;
            } else if (rect.contains(n.point)) {
                list.add(n.point);
            }
            if (n.lb != null) {
                execute(n.lb);
            }
            if (n.rt != null) {
                execute(n.rt);
            }
        }
    }

    private class Node {

        private final boolean isVertical;
        private Point2D point;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p, boolean isVertical, RectHV rect) {
            this.point = p;
            this.isVertical = isVertical;
            this.rect = rect;
        }

        public void draw() {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            point.draw();
            if (isVertical) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius(.002);
                StdDraw.line(point.x(), rect.ymin(), point.x(), rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius(.002);
                StdDraw.line(rect.xmin(), point.y(), rect.xmax(), point.y());
            }
            if (lb != null) {
                lb.draw();
            }
            if (rt != null) {
                rt.draw();
            }
        }

        public void put(Point2D p) {
            int compareTo = compareTo(p);
            switch (compareTo) {
                case 0:
                    point = p;
                    break;
                case -1:
                    if (lb == null) {
                        size++;
                        lb = new Node(p, !isVertical, getSubArea(compareTo));
                    } else {
                        lb.put(p);
                    }
                    break;
                case 1:
                    if (rt == null) {
                        size++;
                        rt = new Node(p, !isVertical, getSubArea(compareTo));
                    } else {
                        rt.put(p);
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        public boolean contains(Point2D p) {
            switch (compareTo(p)) {
                case 0:
                    return true;
                case -1:
                    if (lb != null && lb.contains(p)) {
                        return true;
                    }
                    break;
                case 1:
                    if (rt != null && rt.contains(p)) {
                        return true;
                    }
                    break;
                default:
                    throw new IllegalArgumentException();
            }
            return false;
        }

        public int compareTo(Point2D that) {
            if (isVertical) {
                if (that.x() < this.point.x()) {
                    return -1;
                }
            } else {
                if (that.y() < this.point.y()) {
                    return -1;
                }
            }
            if (this.point.equals(that)) {
                return 0;
            } else {
                return +1;
            }
        }

        public RectHV getSubArea(int compareTo) {
            if (isVertical && compareTo == -1) {
                return new RectHV(rect.xmin(), rect.ymin(), point.x(), rect.ymax());
            }
            if (isVertical && compareTo == 1) {
                return new RectHV(point.x(), rect.ymin(), rect.xmax(), rect.ymax());
            }
            if (!isVertical && compareTo == -1) {
                return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), point.y());
            }
            if (!isVertical && compareTo == 1) {
                return new RectHV(rect.xmin(), point.y(), rect.xmax(), rect.ymax());
            }
            throw new IllegalArgumentException();
        }
    }
}
