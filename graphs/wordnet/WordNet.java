import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

// An immutable WordNet data type.
public class WordNet {
    RedBlackBST<String, SET<Integer>> st; // maps synset nouns to a set of IDs
    RedBlackBST<Integer, String> rst; // maps synset id to the synset string
    ShortestCommonAncestor sca;
    Digraph G;
    
    // Construct a WordNet object given the names of the input (synset and
    // hypernym) files.
    public WordNet(String synsets, String hypernyms) {
       if (synsets == null || hypernyms == null) {
           throw new NullPointerException(); // take care of exceptions
       }
       // set up BSTs for storing nouns and ids
       st = new RedBlackBST<String, SET<Integer>>();
       rst = new RedBlackBST<Integer, String>();
       int id = -1; // use id here to keep track of the temp ids
       In in = new In(synsets);
       while (in.hasNextLine()) {
           // read line and split
           String line = in.readLine();
           String[] split = line.split(",");
           id = Integer.parseInt(split[0]); // ids are first elements of line
           String[] nouns = split[1].split(" "); // nouns are second elements
           SET<String> set = new SET<String>(); // initlize a set of nouns
           for (String noun : nouns) {
               set.add(noun); // add each noun to the set
           }
           rst.put(id, split[1]); // add id to rst
           for (String noun : nouns) { 
               if (st.contains(noun)) { 
                   st.get(noun).add(id); 
               } else {
               	   // if word doesn't exist yet
                   SET<Integer> s = new SET<Integer>();
                   // populate content for new words
                   s.add(id); 
                   st.put(noun, s); 
               }
           }
       }
       this.G = new Digraph(id + 1); 
       in = new In(hypernyms); // read hypernyms
       while (in.hasNextLine()) {
          String line = in.readLine();
          String[] split = line.split(","); // read all ids from line
          int tmp = Integer.parseInt(split[0]); 
          for (int i = 1; i < split.length; i++) {
              G.addEdge(tmp, Integer.parseInt(split[i])); 
          }
        }

       sca = new ShortestCommonAncestor(G); 

    }

    // All WordNet nouns.
    public Iterable<String> nouns() {
        // return nouns;
        return st.keys();
    }

    // Is the word a WordNet noun?
    public boolean isNoun(String word) {
        // see if word is a noun in synset
	    return st.contains(word);
    }

    // A synset that is a shortest common ancestor of noun1 and noun2.
    public String sca(String noun1, String noun2) {
        if(!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException();
        }
        // calculate shortest ancestor using sca
        SET<Integer> ids1 = st.get(noun1);
        SET<Integer> ids2 = st.get(noun2);
        int ancestor = sca.ancestor(ids1, ids2);
        return rst.get(ancestor);
    }

    // Distance between noun1 and noun2.
    public int distance(String noun1, String noun2) {
        if(!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException();
        }
        // calculate shortest ancestral path using sca
        SET<Integer> ids1 = st.get(noun1);
        SET<Integer> ids2 = st.get(noun2);
        return sca.length(ids1, ids2);
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        String word1 = args[2];
        String word2 = args[3];        
        int nouns = 0;
        for (String noun : wordnet.nouns()) {
            nouns++;
        }
        StdOut.println("# of nouns = " + nouns);
        StdOut.println("isNoun(" + word1 + ") = " + wordnet.isNoun(word1));
        StdOut.println("isNoun(" + word2 + ") = " + wordnet.isNoun(word2));
        StdOut.println("isNoun(" + (word1 + " " + word2) + ") = "
                       + wordnet.isNoun(word1 + " " + word2));
        StdOut.println("sca(" + word1 + ", " + word2 + ") = "
                       + wordnet.sca(word1, word2));
        StdOut.println("distance(" + word1 + ", " + word2 + ") = "
                       + wordnet.distance(word1, word2));
    }
}
