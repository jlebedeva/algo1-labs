
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

    private static final int BLANK = 0;
    private final int[][] tiles;
    private final int dim;

    /**
     * construct a board from an N-by-N array of blocks (where blocks[i][j] =
     * block in row i, column j)
     *
     * @param blocks
     */
    public Board(int[][] blocks) {
        this.dim = blocks.length;
        this.tiles = copy(blocks);
    }

    /**
     * board dimension N
     *
     * @return
     */
    public int dimension() {
        return dim;
    }

    /**
     * number of blocks out of place
     *
     * @return
     */
    public int hamming() {
        int counter = 0;
        for (int tile, i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                tile = tiles[i][j];
                if (tile == BLANK) {
                    continue;
                } else if (j + 1 + i * dim == tile) {
                    continue;
                }
                counter++;
            }
        }
        return counter;
    }

    /**
     * sum of Manhattan distances between blocks and goal
     *
     * @return
     */
    public int manhattan() {
        int counter = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                int value = tiles[i][j];
                counter += rowsToTravel(value, i);
                counter += columnsToTravel(value, j);
            }
        }
        return counter;
    }

    /**
     * is this board the goal board?
     *
     * @return
     */
    public boolean isGoal() {
        int tile;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                tile = tiles[i][j];
                if (tile == BLANK) {
                    if (i != j || i + 1 != dim) {
                        return false;
                    }
                } else {
                    if (j + 1 + i * dim != tile) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * a board that is obtained by exchanging any pair of blocks
     *
     * @return
     */
    public Board twin() {
        if (dim < 2) {
            return null;
        } else {
            int ax = 0, ay = 0, bx = 0, by = 1;
            if (tiles[ax][ay] == BLANK) {
                ax++;
            } else if (tiles[bx][by] == BLANK) {
                bx++;
            }
            return copyWithSwitch(ax, ay, bx, by);
        }
    }

    /**
     * does this board equal y?
     *
     * @param y
     * @return
     */
    @Override
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y.getClass() != this.getClass()) {
            return false;
        }
        Board that = (Board) y;
        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    /**
     * all neighboring boards
     *
     * @return
     */
    public Iterable<Board> neighbors() {
        List<Board> list = new ArrayList<>();
        //find blank
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (tiles[i][j] == BLANK) {
                    list.add(copyWithSwitch(i, j, i + 1, j));
                    list.add(copyWithSwitch(i, j, i - 1, j));
                    list.add(copyWithSwitch(i, j, i, j + 1));
                    list.add(copyWithSwitch(i, j, i, j - 1));
                    while (list.remove(null)) {
                    }
                    break;
                }
            }
        }
        return list;
    }

    /**
     * string representation of this board (in the output format specified
     * below)
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dim).append("\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    private int columnsToTravel(int value, int j) {
        if (value == BLANK) {
            return 0;
        } else {
            return Math.abs(j - (value - 1) % dim);
        }
    }

    private int rowsToTravel(int value, int i) {
        if (value == BLANK) {
            return 0;
        } else {
            return Math.abs(i - (value - 1) / dim);
        }
    }

    private int[][] copy(int[][] original) {
        int[][] copy = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, dim);
        }
        return copy;
    }

    private boolean isOffGrid(int index) {
        return index < 0 || index >= dim;
    }

    private Board copyWithSwitch(int rowIdx, int colIdx,
            int newRowIdx, int newColIdx) {

        if (isOffGrid(newRowIdx) || isOffGrid(newColIdx)) {
            return null;
        }
        int[][] twinTiles = copy(tiles);
        twinTiles[rowIdx][colIdx] = tiles[newRowIdx][newColIdx];
        twinTiles[newRowIdx][newColIdx] = tiles[rowIdx][colIdx];
        return new Board(twinTiles);
    }
}
