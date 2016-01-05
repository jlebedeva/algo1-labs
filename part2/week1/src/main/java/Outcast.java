
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordnet;

    /**
     *
     * @param wordnet
     */
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    /**
     * given an array of WordNet nouns, return an outcast
     *
     * @param nouns
     * @return
     */
    public String outcast(String[] nouns) {
        String outcast = null;
        int distanceSum, maxDistanceSum = -1;

        for (String noun : nouns) {
            distanceSum = 0;
            for (String otherNoun : nouns) {
                if (!noun.equals(otherNoun)) {
                    distanceSum += wordnet.distance(noun, otherNoun);
                }
            }
            if (distanceSum > maxDistanceSum) {
                outcast = noun;
                maxDistanceSum = distanceSum;
            }
        }
        return outcast;
    }

    /**
     * The following test client takes from the command line the name of a
     * synset file, the name of a hypernym file, followed by the names of
     * outcast files, and prints out an outcast in each file.
     *
     * @param args
     */
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
