
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {

    /**
     * Test of hamming method, of class Board.
     */
    @Test
    public void testHamming() {
        System.out.println("hamming");
        
        Board instance = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        });
        int expResult = 0;
        int result = instance.hamming();
        assertEquals(expResult, result);
        
        instance = new Board(new int[][]{
            {7, 8, 0},
            {1, 2, 3},
            {4, 5, 6}
        });
        expResult = 8;
        result = instance.hamming();
        assertEquals(expResult, result);
        
        instance = new Board(new int[][]{
            {3, 2, 1},
            {5, 6, 4},
            {0, 8, 7}
        });
        expResult = 6;
        result = instance.hamming();
        assertEquals(expResult, result);
        
        instance = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 0, 8}
        });
        expResult = 1;
        result = instance.hamming();
        assertEquals(expResult, result);
    }

    /**
     * Test of manhattan method, of class Board.
     */
    @Test
    public void testManhattan() {
        System.out.println("manhattan");
        Board instance = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        });
        int expResult = 0;
        int result = instance.manhattan();
        assertEquals(expResult, result);
        
        instance = new Board(new int[][]{
            {7, 8, 0},
            {1, 2, 3},
            {4, 5, 6}
        });
        expResult = 10;
        result = instance.manhattan();
        assertEquals(expResult, result);
        
        instance = new Board(new int[][]{
            {3, 2, 1},
            {5, 6, 4},
            {0, 8, 7}
        });
        expResult = 10;
        result = instance.manhattan();
        assertEquals(expResult, result);
        
        instance = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 0, 8}
        });
        expResult = 1;
        result = instance.manhattan();
        assertEquals(expResult, result);
    }

    /**
     * Test of isGoal method, of class Board.
     */
    @Test
    public void testIsGoal() {
        System.out.println("isGoal");

        Board instance = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        });
        boolean expResult = true;
        boolean result = instance.isGoal();
        assertEquals(expResult, result);
        
        instance = new Board(new int[][]{
            {3, 2, 1},
            {5, 6, 4},
            {0, 8, 7}
        });
        expResult = false;
        result = instance.isGoal();
        assertEquals(expResult, result);
    }

    /**
     * Test of twin method, of class Board.
     */
    @Test
    public void testTwin() {
        System.out.println("twin");
        Board instance = new Board(new int[][]{
            {0, 2, 3},
            {4, 5, 6},
            {7, 8, 1}
        });
        Board expResult = new Board(new int[][]{
            {0, 4, 3},
            {2, 5, 6},
            {7, 8, 1}
        });
        Board result = instance.twin();
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Board.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Board instance = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        });
        Object other = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 0}
        });
        boolean expResult = true;
        boolean result = instance.equals(other);
        assertEquals(expResult, result);
        
        other = new Board(new int[][]{
            {1, 2, 3},
            {4, 5, 6},
            {7, 0, 8}
        });
        expResult = false;
        result = instance.equals(other);
        assertEquals(expResult, result);
    }

    /**
     * Test of neighbors method, of class Board.
     */
    @Test
    public void testNeighbors() {
        System.out.println("neighbors");
        Board instance = new Board(new int[][]{
            {3, 2, 1},
            {5, 6, 4},
            {0, 8, 7}
        });
        
        // create model collection
        List<Board> targetList = new ArrayList<>();
        targetList.add(new Board(new int[][]{
            {3, 2, 1},
            {5, 6, 4},
            {8, 0, 7}
        }));
        targetList.add(new Board(new int[][]{
            {3, 2, 1},
            {0, 6, 4},
            {5, 8, 7}
        }));
                
        Iterator<Board> it = instance.neighbors().iterator();
        while (it.hasNext()) {
            assertTrue("contains board", targetList.remove(it.next()));
        }
        
        assertTrue("contains board", targetList.isEmpty());
    }    
}
