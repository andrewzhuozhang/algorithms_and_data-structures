import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// An immutable data type for outcast detection.
public class Outcast {
    WordNet wordnet; 

    // Construct an Outcast object given a WordNet object.
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet; // initialize wordnet instance var
    }

    // The outcast noun from nouns.
    public String outcast(String[] nouns) {
        // initialize vars for keeping track
        double max = 0; // keep track of max distance
        String outcast = null;
        double dist; // keep track of distance
        // use nested for loop to calculate distance between
        // each two vertices
        for (int i = 0; i < nouns.length; i++) {
            dist = 0;
            for (int j = 0; j < nouns.length; j++) {
                dist = dist + wordnet.distance(nouns[i], nouns[j]);
            }
            // keep updating to the vertex with largest dist till 
            // the whole array is exhausted
            if (dist > max) {
                max = dist;
                outcast = nouns[i];
            }
        }
        return outcast;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println("outcast(" + args[t] + ") = "
                           + outcast.outcast(nouns));
        }
    }
}
