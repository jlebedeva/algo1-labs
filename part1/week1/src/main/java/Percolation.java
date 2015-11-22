
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation.
 *
 * @author jlebedeva
 */
public class Percolation {

    /**
     * Bit mask for open sites, 1st bit.
     */
    private static final int IS_OPEN = 1;
    /**
     * Bit mask for sites, connected to top, 2nd bit.
     */
    private static final int CONNECTED_TO_TOP = 2;
    /**
     * Bit mask for sites, connected to bottom, 3rd bit.
     */
    private static final int CONNECTED_TO_BOTTOM = 4;
    /**
     * State all sites in the grid: true if it has been open.
     */
    private final int[] siteFlags;
    /**
     * Algorithm implementation.
     */
    private final WeightedQuickUnionUF delegate;
    /**
     * Size of the grid, number of columns/rows is has.
     */
    private final int gridSize;
    /**
     * Number of sites in the grid.
     */
    private final int siteNum;

    /**
     * Create N-by-N grid, with all sites blocked.
     *
     * @param n size of the grid
     * @throws IllegalArgumentException
     */
    public Percolation(final int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.gridSize = n;
        this.siteNum = n * n;

        // add top and bottom sites, create UF implementation
        this.siteFlags = new int[this.siteNum + 2];
        this.delegate = new WeightedQuickUnionUF(this.siteNum + 2);

        // init top site as open
        this.siteFlags[getStartIndex()] |= IS_OPEN;
        this.siteFlags[getStartIndex()] |= CONNECTED_TO_TOP;
        this.siteFlags[getEndIndex()] |= CONNECTED_TO_BOTTOM;
    }

    /**
     * Open site (row rowNum, column colNum) if it is not open already.
     *
     * @param rowNum row
     * @param colNum column
     * @throws IndexOutOfBoundsException
     */
    public void open(final int rowNum, final int colNum) {
        ensureCoordsExist(rowNum, colNum);
        int site = getIndex(rowNum, colNum);

        if (hasOpenFlag(site)) {
            return;
        }
        int flag = IS_OPEN;

        if (coordExists(rowNum + 1)) {
            if (isOpen(rowNum + 1, colNum)) {
                flag |= connectTo(getIndex(rowNum + 1, colNum), site);
            }
        } else {
            // never copy 'top' flag from bottom node
            flag |= CONNECTED_TO_BOTTOM;
        }

        if (coordExists(rowNum - 1)) {
            if (isOpen(rowNum - 1, colNum)) {
                flag |= connectTo(getIndex(rowNum - 1, colNum), site);
            }
        } else {
            flag |= connectTo(getStartIndex(), site);
        }

        if (coordExists(colNum + 1) && isOpen(rowNum, colNum + 1)) {
            flag |= connectTo(getIndex(rowNum, colNum + 1), site);
        }
        if (coordExists(colNum - 1) && isOpen(rowNum, colNum - 1)) {
            flag |= connectTo(getIndex(rowNum, colNum - 1), site);
        }

        if (isFlagSet(flag, CONNECTED_TO_TOP)
                && isFlagSet(flag, CONNECTED_TO_BOTTOM)) {
            doConnectTo(getEndIndex(), site);
        }

        setFlags(site, flag);
    }

    /**
     * Determines if site is open.
     *
     * @param rowNum row
     * @param colNum column
     * @return true if open, false otherwise
     * @throws IndexOutOfBoundsException
     */
    public boolean isOpen(final int rowNum, final int colNum) {
        ensureCoordsExist(rowNum, colNum);
        return hasOpenFlag(getIndex(rowNum, colNum));
    }

    /**
     * Determines if site is full.
     *
     * @param rowNum row
     * @param colNum column
     * @return true if full, false otherwise
     * @throws IndexOutOfBoundsException
     */
    public boolean isFull(final int rowNum, final int colNum) {
        ensureCoordsExist(rowNum, colNum);
        return delegate.connected(getStartIndex(), getIndex(rowNum, colNum));
    }

    /**
     * Determines if the system percolates.
     *
     * @return true if it persolates, false otherwise
     */
    public boolean percolates() {
        return delegate.connected(getStartIndex(), getEndIndex());
    }

    /**
     * Test client.
     *
     * @param args one command-line argument: N - grid size
     */
    public static void main(final String[] args) {
        //read command-line parameter
        int n = Integer.parseInt(args[0]);

        //construct the experiment
        Percolation p = new Percolation(n);

        //open sites till the system percolates
        while (!p.percolates()) {
            p.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
        }

        //calculate the portion of open sites
        int count = 0;
        for (int rowNum = 1; rowNum <= n; rowNum++) {
            for (int colNum = 1; colNum <= n; colNum++) {
                if (p.isOpen(rowNum, colNum)) {
                    count++;
                }
            }
        }

        //report
        System.out.println("The system percolates at "
                + (double) count / (n * n) + " sites open");
    }

    /**
     * Check if there is a column or row by this number in the grid.
     *
     * @param i colNum or rowNum
     * @return true if colNum or rowNum is valid for the grid
     */
    private boolean coordExists(final int i) {
        return i <= gridSize && i > 0;
    }

    /**
     * Check if there's a site in the grid for these coordinates. If there is,
     * does nothing. If there's no such site, throws IndexOutOfBoundsException.
     *
     * @param rowNum row
     * @param colNum column
     * @throws IndexOutOfBoundsException
     */
    private void ensureCoordsExist(final int rowNum, final int colNum) {
        if (!coordExists(rowNum) || !coordExists(colNum)) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Retrieve UF node array index of virtual "source" site at the top.
     *
     * @return index of virtual "source" node
     */
    private int getStartIndex() {
        return this.siteNum;
    }

    /**
     * Retrieve UF node array index of virtual "target" site at the bottom.
     *
     * @return index of virtual "target" node
     */
    private int getEndIndex() {
        return this.siteNum + 1;
    }

    /**
     * Retrieve UF node array index of the site.
     *
     * @param rowNum row
     * @param colNum column
     * @return index of node
     */
    private int getIndex(final int rowNum, final int colNum) {
        return gridSize * (rowNum - 1) + colNum - 1;
    }

    /**
     * Check if flag is set.
     *
     * @param value flag value
     * @param mask flag bit mask
     * @return true if flag is set, false otherwise
     */
    private boolean isFlagSet(final int value, final int mask) {
        return (value & mask) == mask;
    }

    /**
     * Check if 'open' flag is set.
     *
     * @param idx index of the node
     * @return true if flag is set, false otherwise
     */
    private boolean hasOpenFlag(final int idx) {
        return isFlagSet(this.siteFlags[idx], IS_OPEN);
    }

    /**
     * Link a node to a newly opened neighbour.
     *
     * @param from index of the neighbouring site node
     * @param to index of the opened site node * @return
     * @return value of the connection flags to inherit
     */
    private int connectTo(final int from, final int to) {
        int flagValue = collectRootFlags(from);
        doConnectTo(from, to);
        return flagValue;
    }

    /**
     * Retrieve flags set on root node of the fragment.
     *
     * @param idx index of the node
     * @return connection flags
     */
    private int collectRootFlags(final int idx) {
        int root = delegate.find(idx);
        return this.siteFlags[root] & (CONNECTED_TO_TOP | CONNECTED_TO_BOTTOM);
    }

    /**
     * Set flags on root node of the fragment and node itself.
     *
     * @param idx index of the node
     * @param flag flag
     */
    private void setFlags(final int idx, final int flag) {
        this.siteFlags[idx] = flag;

        int root = delegate.find(idx);
        this.siteFlags[root] = flag;
    }

    /**
     * Do union.
     *
     * @param from index of the neighbouring site node
     * @param to index of the opened site node
     */
    private void doConnectTo(final int from, final int to) {
        delegate.union(to, from);
    }
}
