// Ramanujan.java: Prints the integers <= N (command-line argument) that can be
// expressed as the sum of two distinct cubes.

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Ramanujan {
    // A data type that encapsulates a pair of numbers (i, j) 
    // and the sum of their cubes, ie, i^3 + j^3.
    private static class Pair implements Comparable<Pair> {
        private int i;          // first element of the pair
        private int j;          // second element of the pair
        private int sumOfCubes; // i^3 + j^3

        // Construct a pair (i, j).
        Pair(int i, int j) {
            this.i = i;
            this.j = j;
            sumOfCubes = i * i * i + j * j * j;
        }

        // Compare this pair to the other by sumOfCubes.
        public int compareTo(Pair other) {
            return sumOfCubes - other.sumOfCubes;
        }
    }

    public static void main(String[] args) {
    	int N = Integer.parseInt(args[0]);
    	// initialize PQ with Pairs
        MinPQ<Pair> ramanujan = new MinPQ<Pair>();
        for (int i = 1; i*i*i < N; i++) {
        	Pair temp = new Pair(i, i+1);
        	ramanujan.insert(temp);
        }
        Pair current = ramanujan.min();
        Pair prev;
        while (!ramanujan.isEmpty()) {
        	prev = current;
	        current = ramanujan.delMin();
	        if (prev != current && prev.sumOfCubes == current.sumOfCubes
	        		&& current.sumOfCubes <= N) {
	        	StdOut.println(prev.sumOfCubes+" = "+prev.i+"^3 + "+prev.j+"^3 = "+current.i+"^3 + "+ current.j+"^3");
	        }
	        if (current.j * current.j * current.j < N) {
	        	ramanujan.insert(new Pair(current.i, current.j+1));
	        }
        }
         
    }

}
