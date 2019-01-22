import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
 
// An immutable data type for computing shortest common ancestors.
public class ShortestCommonAncestor {
    Digraph G;
   
    // Construct a ShortestCommonAncestor object given a rooted DAG.
    public ShortestCommonAncestor(Digraph G) {
        this.G = new Digraph(G); //initlize Digraph from graph given
    }

    // Length of the shortest ancestral path between v and w.
    public int length(int v, int w) {
        int ancestor = ancestor(v, w); 
        return distFrom(v).get(ancestor) + distFrom(w).get(ancestor); 
    }

    // Shortest common ancestor of vertices v and w.
    public int ancestor(int v, int w) { 
        SeparateChainingHashST<Integer, Integer> dist_v  = distFrom(v); 
        SeparateChainingHashST<Integer, Integer> dist_w  = distFrom(w); 
        int shortest_distance = Integer.MAX_VALUE; // initlize a Distance and Ancestor Value that is unubtainable. 
        // Make sure the Distance will always be larger than any initial distance it can get.
        int shortest_ancestor = -1;
        for (int i : dist_w.keys()) { //for every vertice(Ancestor) in wDistFrom
            if (dist_v.contains(i)) { // if vDistFrom contains the Ancestor
                int distance = dist_v.get(i) + dist_w.get(i); //compute distance from the Ancestor from both Nodes)
                if (distance < shortest_distance) { //if this distance is shorter than old distance
                    shortest_distance = distance; // replace shortestdistance and Ancestor with the new values
                    shortest_ancestor = i;
                }
            }
        }
        return shortest_ancestor;
    }
 
    // Length of the shortest ancestral path of vertex subsets A and B.
    public int length(Iterable<Integer> A, Iterable<Integer> B) {
        // get an array of a sca, subset A, subset B
        int[] AB = triad(A, B);
        int v = AB[1]; //store the v and w as ints
        int w = AB[2];
        SeparateChainingHashST<Integer, Integer> dists_v = distFrom(v); //create a map of verticies 
        SeparateChainingHashST<Integer, Integer> dists_w = distFrom(w);
        int total = (dists_v.get(AB[0]) + dists_w.get(AB[0])); // calculate the total distance of the ancestor to v and w
        return total; //return the Length of sca to subset A and B
    }
 
    // A shortest common ancestor of vertex subsets A and B.
    public int ancestor(Iterable<Integer> A, Iterable<Integer> B) {
        // return the ancestor of the Sca
        return triad(A, B)[0];
    }
 
        // Helper: Return a map of vertices reachable from v and their
        // respective shortest distances from v.    
    private SeparateChainingHashST<Integer, Integer> distFrom(int v) {
        SeparateChainingHashST<Integer, Integer> st = new SeparateChainingHashST<Integer, Integer>();
        LinkedQueue<Integer> q = new LinkedQueue<Integer>();
        st.put(v, 0); 
        q.enqueue(v);
        while (!q.isEmpty()) {
            int tmp = q.dequeue(); // dequeue from q to process
            for (int i : G.adj(tmp)) { 
                if (!st.contains(i)) { // if vertex in tmp, then add it to i but distance plus 1
                    st.put(i, st.get(tmp) + 1); 
                    q.enqueue(i); 
                }
            }
        }
        return st;
    }
 
  
    private int[] triad(Iterable<Integer> A, Iterable<Integer> B) {
        // initialize distance and ancestor
        int shortest_distance = Integer.MAX_VALUE;
        int shortest_ancestor = -1;
        // initialize v and w to avoid null pointer issues
        int v = -1;
        int w = -1;
        // loop through both sets
        for (int a : A) { 
            for (int b : B) {
                int distance = length(a, b); 
                // if shorter distance and ancestor found then update
                if (distance < shortest_distance) {
                    shortest_distance = distance;
                    shortest_ancestor = ancestor(a, b);
                    v = a;
                    w = b;
                }
            }
        }
        // triad is [ancestor, v, w]
        int[] triad = {shortest_ancestor, v, w};
        return triad; 
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
