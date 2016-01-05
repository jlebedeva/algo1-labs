
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseballElimination {

    private final List<String> teams = new ArrayList<>();
    private final int[] wins;
    private final int[] loses;
    private final int[] remaining;
    private final int[][] games;
    private final Map<String, Iterable<String>> certificates = new HashMap<>();

    /**
     * Create a baseball division from given filename in format specified below.
     *
     * @param filename
     */
    public BaseballElimination(String filename) {
        In in = new In(filename);
        int l = in.readInt();
        wins = new int[l];
        loses = new int[l];
        remaining = new int[l];
        games = new int[l][l];
        for (int i = 0; i < l; i++) {
            teams.add(in.readString());
            wins[i] = in.readInt();
            loses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < l; j++) {
                games[i][j] = in.readInt();
            }
        }
    }

    /**
     * Number of teams.
     *
     * @return
     */
    public int numberOfTeams() {
        return teams.size();
    }

    /**
     * All teams.
     *
     * @return
     */
    public Iterable<String> teams() {
        return Collections.unmodifiableList(teams);
    }

    /**
     * Number of wins for given team.
     *
     * @param team
     * @return
     */
    public int wins(String team) {
        validateTeam(team);
        return wins[teams.indexOf(team)];
    }

    /**
     * Number of losses for given team.
     *
     * @param team
     * @return
     */
    public int losses(String team) {
        validateTeam(team);
        return loses[teams.indexOf(team)];
    }

    /**
     * Number of remaining games for given team.
     *
     * @param team
     * @return
     */
    public int remaining(String team) {
        validateTeam(team);
        return remaining[teams.indexOf(team)];
    }

    /**
     * Number of remaining games between team1 and team2.
     *
     * @param team1
     * @param team2
     * @return
     */
    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return games[teams.indexOf(team1)][teams.indexOf(team2)];
    }

    /**
     * Is given team eliminated?
     *
     * @param team
     * @return
     */
    public boolean isEliminated(String team) {
        validateTeam(team);

        Iterable<String> cert = certificates.get(team);
        // not calculated yet
        if (cert == null) {
            cert = calculate(team);
            certificates.put(team, cert);
        }
        
        return cert.iterator().hasNext();
    }

    private int gameIndex(int teamIndex1, int teamIndex2) {
        if (teamIndex1 == teamIndex2) {
            return -1;
        } else if (teamIndex2 < teamIndex1) {
            return gameIndex(teamIndex2, teamIndex1);
        } else {
            return (teamIndex1 + 1) * numberOfTeams()
                    - (teamIndex1 + 1) * teamIndex1 / 2
                    + teamIndex2
                    - teamIndex1
                    - 1;
        }
    }

    /**
     * Subset R of teams that eliminates given team; null if not eliminated.
     *
     * @param team
     * @return
     */
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);

        Iterable<String> cert = certificates.get(team);
        // not calculated yet
        if (cert == null) {
            cert = calculate(team);
            certificates.put(team, cert);
        }
        
        if (cert.iterator().hasNext()) {
            return cert;
        } else {
            return null;
        }
    }

    private Iterable<String> calculate(String team) {
        int teamIndex = teams.indexOf(team);
        int teamReq = wins[teamIndex] + remaining[teamIndex];
        
        //easy case
        for (int i = 0; i < wins.length; i++) {
            if (wins[i] > teamReq) {
                return Arrays.asList(teams.get(i));
            }
        }

        // 0 to t = all teams (including eliminated one)
        // t + 1 + gameIndex(t, t1, t2)
        // teamIndex = source
        // V - 1 = sink
        int t = teams.size();
        int g = t * (t - 1) / 2;
        int V = 2 + t + g;
        int source = V - 2;
        int sink = V - 1;
        FlowNetwork fn = new FlowNetwork(V);
        for (int i = 0; i < teams.size(); i++) {
            if (i != teamIndex) {
                if (teamReq - wins[i] > 0) {
                    fn.addEdge(new FlowEdge(i, sink, teamReq - wins[i]));
                }
                for (int j = 0; j < teams.size(); j++) {
                    if (i != j && j != teamIndex) {
                        fn.addEdge(new FlowEdge(gameIndex(i, j), i, Double.POSITIVE_INFINITY));
                        if (i < j) {
                            fn.addEdge(new FlowEdge(source, gameIndex(i, j), games[i][j]));
                        }
                    }
                }
            }
        }
        FordFulkerson ff = new FordFulkerson(fn, source, sink);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < t; i++) {
            if (i != teamIndex && ff.inCut(i)) {
                result.add(teams.get(i));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

    private void validateTeam(String team) {
        if (!teams.contains(team)) {
            throw new IllegalArgumentException();
        }
    }
}
