
import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {

    private static final int EDGE_ENERGY = 1000;

    private Picture picture;
    private Color[][] colors;
    private boolean transposed;
    private int width;
    private int height;

    /**
     * Create a seam carver object based on the given picture.
     *
     * @param picture
     */
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
    }

    /**
     * Current picture.
     *
     * @return
     */
    public Picture picture() {
        if (picture == null) {
            if (transposed) {
                picture = new Picture(height, width);
            } else {
                picture = new Picture(width, height);
            }
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (transposed) {
                        picture.set(y, x, colors[x][y]);
                    } else {
                        picture.set(x, y, colors[x][y]);
                    }
                }
            }
        }

        if (transposed) {
            height = picture.height();
            width = picture.width();
            transposed = false;
        }
        colors = null; //FIXME reuse
        return new Picture(picture);
    }

    /**
     * Width of current picture.
     *
     * @return
     */
    public int width() {
        if (transposed) {
            return height;
        } else {
            return width;
        }
    }

    /**
     * Height of current picture.
     *
     * @return
     */
    public int height() {
        if (transposed) {
            return width;
        } else {
            return height;
        }
    }

    /**
     * Energy of pixel at column x and row y.
     *
     * @param x
     * @param y
     * @return
     */
    public double energy(int x, int y) {
        if (transposed) {
            return doEnergy(y, x);
        } else {
            return doEnergy(x, y);
        }
    }

    /**
     * Sequence of indices for horizontal seam.
     *
     * @return
     */
    public int[] findHorizontalSeam() {
        if (transposed) {
            transpose();
        }
        return findSeam();
    }

    /**
     * Sequence of indices for vertical seam.
     *
     * @return
     */
    public int[] findVerticalSeam() {
        if (!transposed) {
            transpose();
        }
        return findSeam();
    }

    /**
     * Remove horizontal seam from current picture.
     *
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        if (transposed) {
            transpose();
        }
        doRemoveHorizontalSeam(seam);
    }

    /**
     * Remove vertical seam from current picture.
     *
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        if (!transposed) {
            transpose();
        }
        doRemoveHorizontalSeam(seam);
    }

    private void transpose() {
        Color[][] newColors = new Color[height][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                newColors[y][x] = getColor(x, y);
            }
        }
        colors = newColors;
        picture = null;
        height = width;
        width = colors.length;
        transposed = !transposed;
    }

    private Color getColor(int x, int y) {
        if (picture == null) {
            return colors[x][y];
        } else {
            return picture.get(x, y);
        }
    }

    private double doEnergy(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IndexOutOfBoundsException();
        } else if (x == 0 || x == (width - 1)
                || y == 0 || y == (height - 1)) {
            return EDGE_ENERGY;
        } else {
            return Math.sqrt(sumColorDifferenceSquares(x - 1, y, x + 1, y)
                    + sumColorDifferenceSquares(x, y - 1, x, y + 1));
        }
    }

    private double sumColorDifferenceSquares(int x1, int y1, int x2, int y2) {
        Color c1, c2;
        if (picture == null) {
            c1 = colors[x1][y1];
            c2 = colors[x2][y2];
        } else {
            c1 = picture.get(x1, y1);
            c2 = picture.get(x2, y2);
        }
        return Math.pow(c1.getBlue() - c2.getBlue(), 2)
                + Math.pow(c1.getGreen() - c2.getGreen(), 2)
                + Math.pow(c1.getRed() - c2.getRed(), 2);
    }

    private int[] findSeam() {
        int source = width * height;
        int sink = source + 1;
        int lastColumn = width - 1;
        int lastRow = height - 1;
        int firstNodeInRow;
        int node;
        double energy;

        // total number of nodes: source + nodes + sink
        EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(width * height + 2);

        //iterate by rows
        for (int y = 0; y < height; y++) {
            firstNodeInRow = width * y;

            // add source and sink links
            ewd.addEdge(new DirectedEdge(source, firstNodeInRow, 0.0));
            ewd.addEdge(new DirectedEdge(lastColumn + firstNodeInRow, sink, EDGE_ENERGY));

            // iterate by columns (last one excluding, since already covered)
            for (int x = 0; x < lastColumn; x++) {
                node = x + firstNodeInRow;
                energy = doEnergy(x, y);
                // check that there's an edge below
                if (y < lastRow) {
                    ewd.addEdge(new DirectedEdge(node, node + width + 1, energy));
                }
                ewd.addEdge(new DirectedEdge(node, node + 1, energy));
                // check that there's an edge above
                if (y > 0) {
                    ewd.addEdge(new DirectedEdge(node, node - width + 1, energy));
                }
            }
        }

        AcyclicSP sp = new AcyclicSP(ewd, source);
        Iterable<DirectedEdge> it = sp.pathTo(sink);

        // seam has exact length of the cut dimension
        int[] seam = new int[width];
        for (DirectedEdge de : it) {
            int x = de.to() % width;
            int y = de.to() / width;
            //column is assumed, row is sought after
            if (de.to() != sink) {
                seam[x] = y;
            }
        }
        return seam;
    }

    private void validateSeam(int[] seam) {
        if (seam == null) {
            throw new NullPointerException();
        } else if (seam.length != width) {
            throw new IllegalArgumentException();
        } else {
            int last = seam[0];
            if (last < 0 || last >= height) {
                throw new IllegalArgumentException();
            }
            for (int i = 1; i < seam.length; i++) {
                if (seam[i] < 0 || seam[i] >= height) {
                    throw new IllegalArgumentException();
                }
                int difference = last - seam[i];
                if (difference > 1 || difference < -1) {
                    throw new IllegalArgumentException();
                } else {
                    last = seam[i];
                }
            }
        }
    }

    private void doRemoveHorizontalSeam(int[] seam) {
        validateSeam(seam);
        if (picture == null) {
            int lastRow = height - 1;
            for (int x = 0; x < width; x++) {
                if (lastRow > seam[x]) {
                    System.arraycopy(colors[x], seam[x] + 1, colors[x], seam[x], lastRow - seam[x]);                   
                }
                colors[x][lastRow] = null;
            }
        } else {
            colors = new Color[width][height];
            for (int x = 0; x < width; x++) {
                int newIndex = 0;
                for (int y = 0; y < height; y++) {
                    if (seam[x] != y) {
                        colors[x][newIndex++] = getColor(x, y);
                    }
                }
            }
            picture = null;
        }
        height = height - 1;
    }
}
