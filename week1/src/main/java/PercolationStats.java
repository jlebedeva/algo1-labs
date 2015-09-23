
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * PercolationStats.
 *
 * @author jlebedeva
 */
public class PercolationStats {

    /**
     * Confidence coefficient.
     */
    private static final double CONFIDENCE_COEFFICIENT = 1.96;
    /**
     * Number of experiments performed.
     */
    private final int itNum;
    /**
     * Results of T experiments.
     */
    private final double[] results;

    /**
     * Perform T independent experiments on an N-by-N grid.
     *
     * @param n size of the grid
     * @param t number of experiments to run
     * @throws IllegalArgumentException
     */
    public PercolationStats(final int n, final int t) {
        if (n <= 0 || t <= 0) {
            throw new IllegalArgumentException();
        }
        this.itNum = t;
        this.results = new double[t];
        for (int i = 0; i < t; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                p.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            }
            int count = 0;
            for (int rowNum = 1; rowNum <= n; rowNum++) {
                for (int colNum = 1; colNum <= n; colNum++) {
                    if (p.isOpen(rowNum, colNum)) {
                        count++;
                    }
                }
            }
            this.results[i] = (double) count / (n * n);
        }
    }

    /**
     * Sample mean of percolation threshold.
     *
     * @return mean
     */
    public double mean() {
        return StdStats.mean(results);
    }

    /**
     * Sample standard deviation of percolation threshold.
     *
     * @return standard deviation
     */
    public double stddev() {
        return StdStats.stddev(results);
    }

    /**
     * Compute low endpoint of 95% confidence interval.
     *
     * @return low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - CONFIDENCE_COEFFICIENT * stddev() / Math.sqrt(itNum);
    }

    /**
     * Compute high endpoint of 95% confidence interval.
     *
     * @return high endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + CONFIDENCE_COEFFICIENT * stddev() / Math.sqrt(itNum);
    }

    /**
     * Test client.
     *
     * @param args command-line arguments: 1) N - grid size 2) T - number of
     * iterations
     */
    public static void main(final String[] args) {
        //read command-line parameters
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        //run the algorithm
        PercolationStats ps = new PercolationStats(n, t);

        //report
        PercolationStats.report(ps.mean(), ps.stddev(), ps.confidenceLo(),
                ps.confidenceHi());
    }

    /**
     * Report to System.out.
     *
     * @param mean mean percolation threshold
     * @param stddev standard deviation
     * @param lo low endpoint of 95% confidence interval
     * @param hi high endpoint of 95% confidence interval
     */
    private static void report(final double mean, final double stddev,
            final double lo, final double hi) {
        System.out.println("mean\t\t\t= " + mean);
        System.out.println("stddev\t\t\t= " + stddev);
        System.out.println("95% confidence interval\t= " + lo + ", " + hi);
    }
}
