
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class WordNet {

    private final Digraph graph;
    private final Map<Integer, String> synset;
    private final Map<String, List<Integer>> wordIndex = new TreeMap<>();
    private final SAP sap;

    /**
     * the name of the two input files
     *
     * @param synsets
     * @param hypernyms
     */
    public WordNet(String synsets, String hypernyms) {
        In dictIn = new In(synsets);
        synset = new HashMap<>();
        while (dictIn.hasNextLine()) {
            String[] line = dictIn.readLine().split(",");
            int idx = Integer.parseInt(line[0]);
            synset.put(idx, line[1]);
            for (String word : line[1].split(" ")) {
                List<Integer> list = wordIndex.get(word);
                if (list == null) {
                    list = new ArrayList<>();                    
                }
                list.add(idx);
                wordIndex.put(word, list);
            }
        }

        In graphIn = new In(hypernyms);
        graph = new Digraph(synset.size());
        while (graphIn.hasNextLine()) {
            String[] line = graphIn.readLine().split(",");
            int source = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                graph.addEdge(source, Integer.parseInt(line[i]));
            }
        }
        
        // check if there's more then 1 sink
        boolean rootFound = false;
        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0) {
                if (rootFound) {
                    throw new IllegalArgumentException();
                } else {
                    rootFound = true;
                }
            }
        }
        
        // check if there's a cycle
        DirectedCycle c = new DirectedCycle(graph);
        if (c.hasCycle()) {
            throw new IllegalArgumentException();
        }
        
        sap = new SAP(graph);
    }

    /**
     *
     * @return all WordNet nouns
     */
    public Iterable<String> nouns() {
        return wordIndex.keySet();
    }

    /**
     * is the word a WordNet noun?
     *
     * @param word
     * @return
     */
    public boolean isNoun(String word) {
        return wordIndex.containsKey(word);
    }

    /**
     * distance between nounA and nounB (defined below)
     *
     * @param nounA
     * @param nounB
     * @return
     */
    public int distance(String nounA, String nounB) {
        List<Integer> nounAIndeces = wordIndex.get(nounA);
        List<Integer> nounBIndeces = wordIndex.get(nounB);
        if (nounAIndeces == null || nounBIndeces == null) {
            throw new IllegalArgumentException();
        }
        return sap.length(nounAIndeces, nounBIndeces);
    }

    /**
     * a synset (second field of synsets.txt) that is the common ancestor of
     * nounA and nounB in a shortest ancestral path (defined below)
     *
     * @param nounA
     * @param nounB
     * @return
     */
    public String sap(String nounA, String nounB) {
        List<Integer> nounAIndeces = wordIndex.get(nounA);
        List<Integer> nounBIndeces = wordIndex.get(nounB);
        if (nounAIndeces == null || nounBIndeces == null) {
            throw new IllegalArgumentException();
        }
        return synset.get(sap.ancestor(nounAIndeces, nounBIndeces));
    }

}
