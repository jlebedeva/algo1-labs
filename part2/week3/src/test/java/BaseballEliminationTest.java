
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class BaseballEliminationTest {

    private static final String RESOURCE_DIR = "src/test/resources/";
    private BaseballElimination instance;

    @Before
    public void before() {
        instance = new BaseballElimination(RESOURCE_DIR + "teams12.txt");
    }

    /**
     * Test of numberOfTeams method, of class BaseballElimination.
     */
    @Test
    public void testNumberOfTeams() {
        System.out.println("numberOfTeams");

        int expResult = 12;
        int result = instance.numberOfTeams();
        assertEquals(expResult, result);
    }

    /**
     * Test of teams method, of class BaseballElimination.
     */
    @Test
    public void testTeams() {
        System.out.println("teams");

        Iterable<String> result = instance.teams();
        Iterator<String> it = result.iterator();
        assertEquals("Poland", it.next());
        assertEquals("Russia", it.next());
        assertEquals("Brazil", it.next());
        assertEquals("Iran", it.next());
        assertEquals("Italy", it.next());
        assertEquals("Cuba", it.next());
        assertEquals("Argentina", it.next());
        assertEquals("USA", it.next());
        assertEquals("Japan", it.next());
        assertEquals("Serbia", it.next());
        assertEquals("Egypt", it.next());
        assertEquals("China", it.next());
    }

    /**
     * Test of wins method, of class BaseballElimination.
     */
    @Test
    public void testWins() {
        System.out.println("wins");

        int expResult = 5;
        int result = instance.wins("Russia");
        assertEquals(expResult, result);
    }

    /**
     * Test of losses method, of class BaseballElimination.
     */
    @Test
    public void testLosses() {
        System.out.println("losses");

        int expResult = 2;
        int result = instance.losses("Italy");
        assertEquals(expResult, result);
    }

    /**
     * Test of remaining method, of class BaseballElimination.
     */
    @Test
    public void testRemaining() {
        System.out.println("remaining");

        int expResult = 4;
        int result = instance.remaining("Japan");
        assertEquals(expResult, result);
    }

    /**
     * Test of against method, of class BaseballElimination.
     */
    @Test
    public void testAgainst() {
        System.out.println("against");

        int expResult = 1;
        int result = instance.against("Serbia", "China");
        assertEquals(expResult, result);
    }

    /**
     * Test of isEliminated method, of class BaseballElimination.
     */
    @Test
    public void testIsEliminated() {
        System.out.println("isEliminated");

        assertEquals(true, instance.isEliminated("China"));
        assertEquals(true, instance.isEliminated("Japan"));
        assertEquals(false, instance.isEliminated("Poland"));
    }

    /**
     * Test of certificateOfElimination method, of class BaseballElimination.
     */
    @Test
    public void testCertificateOfElimination() {
        System.out.println("certificateOfElimination");

        Iterable<String> result = instance.certificateOfElimination("China");
        Set set = new HashSet();
        for (String s : result) {
            set.add(s);
        }
        assertEquals(1, set.size());
        assertTrue("contains team", set.contains("Poland"));
    }

    /**
     * Test of gameIndex method, of class BaseballElimination.
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
    @Test
    public void testGameIndex() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        System.out.println("gameIndex");
        Method method = BaseballElimination.class.getDeclaredMethod("gameIndex",
                int.class, int.class);
        method.setAccessible(true);

        // 0 to 11 = teams
        // 12 = (0,1)
        assertEquals(12, method.invoke(instance, 0, 1));
        assertEquals(12, method.invoke(instance, 1, 0));
        // 12 to 22 = 11 games of team 0
        assertEquals(22, method.invoke(instance, 11, 0));
        // 23 to 32 = 10 games of team 1 minus (0,1)
        assertEquals(23, method.invoke(instance, 1, 2));
        assertEquals(32, method.invoke(instance, 11, 1));
        // 33 to 41 = 9 games of team 2 minus (0,2) (1,2)
        assertEquals(33, method.invoke(instance, 2, 3));
        assertEquals(41, method.invoke(instance, 2, 11));
    }
    /**
     * Test of main method, of class BaseballElimination.
     */
    @Test
    public void testMain() {
        System.out.println("main");

        BaseballElimination.main(new String[]{RESOURCE_DIR + "teams4.txt"});
        BaseballElimination.main(new String[]{RESOURCE_DIR + "teams5.txt"});
    }
    
    @Test
    public void testZeroGames() {
        BaseballElimination be = new BaseballElimination(RESOURCE_DIR + "teams12-allgames.txt");
        for (String team : be.teams()) {
            if (team.equals("Team4") || team.equals("Team11")) {
                assertNull(team + " doesn't have certificate", be.certificateOfElimination(team));
                assertFalse(team + " is not eliminated", be.isEliminated(team));
            } else {
                assertNotNull(team + " has certificate", be.certificateOfElimination(team));
                assertTrue(team + " is eliminated", be.isEliminated(team));
            }
        }
    }
}
