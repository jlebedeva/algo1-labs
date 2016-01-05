
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CircularSuffixArray {

    private final String str;
    private final List<Integer> indeces;
    private final int length;

    /**
     * Circular suffix array of s.
     *
     * @param s
     */
    public CircularSuffixArray(String s) {
        this.str = s;
        this.length = str.length();
        indeces = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            indeces.add(i);
        }
        Collections.sort(indeces, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                for (int i = 0; i < length; i++) {
                    int result = Character.compare(
                            str.charAt((o1 + i) % length),
                            str.charAt((o2 + i) % length));
                    if (result != 0) {
                        return result;
                    }
                }
                return 0;
            }
        });
    }

    /**
     * Length of s.
     *
     * @return
     */
    public int length() {
        return length;
    }

    /**
     * Returns index of ith sorted suffix.
     *
     * @param i
     * @return
     */
    public int index(int i) {
        return indeces.get(i);
    }
}
