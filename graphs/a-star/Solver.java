import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;

// A solver based on the A* algorithm for the 8-puzzle and its generalizations.
public class Solver {
    LinkedStack<Board> solution = new LinkedStack<Board>();
    int moves;

    // Helper search node class.representing a node in the game tree
    private class SearchNode {
        Board board; // board represented by this node
        int moves; // num of moves
        SearchNode previous; // previous board/node
        // constructor
        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }
    }
     
    // Find a solution to the initial board (using the A* algorithm).
    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.NullPointerException();
        }
        if (!initial.isSolvable()) {
            throw new IllegalArgumentException();
        }
        if (initial.isGoal()) {
            solution.push(initial);
            return;
        }
        // minPQ is used to hold all the different possibilities of moves
        Comparator<SearchNode> mcomp = new ManhattanOrder();
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>(mcomp);
        // initialize first node 
        SearchNode ini = new SearchNode(initial, 0, null);
        pq.insert(ini); // ! insert searchnodes into pq, not boards
        while (!pq.isEmpty()) {
            SearchNode node = pq.delMin();
            // if board is goal board, insert then return   
            if (node.board.isGoal()) {
                solution.push(node.board); // push initial board to solution     
                moves++;                
                while (node.previous.previous != null) {
                    node = node.previous;
                    moves++;   
                    solution.push(node.board);
                }    
                // for (SearchNode a : pq) {
                //     StdOut.printf("what's left in pq\n");
                //     StdOut.println(a.board);
                // }                   
                return;
            } else {
                // iterate through neighboring boards
                for (Board b : node.board.neighbors()) {
                    // if not same as previous one then push in
                    if (node.moves == 0) {
                        // for the first node
                        SearchNode tmp = new SearchNode(b, node.moves+1, node);
                        pq.insert(tmp);
                    } else if (!b.equals(node.previous.board)) {
                        // for non-first node
                        SearchNode tmp = new SearchNode(b, node.moves+1, node);
                        pq.insert(tmp);
                    }
                }
            }
        }
    }   

    // The minimum number of moves to solve the initial board.
    public int moves() {
        return this.moves; // return moves from the solver, not search nodes
    }

    // Sequence of boards in a shortest solution.
    public Iterable<Board> solution() {
        return this.solution; // the linkedstack to hold the moves
    }

    // Helper hamming priority function comparator.
    private static class HammingOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int diff = (a.board.hamming()+a.moves)-(b.board.hamming()+b.moves);
            if (diff > 0) { return 1;}
            else if (diff < 0) { return -1; }
            return 0;
        }
    }
    
    // Helper manhattan priority function comparator.
    private static class ManhattanOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int diff = (a.board.manhattan()+a.moves)-(b.board.manhattan()+b.moves);
            if (diff > 0) { return 1;}
            else if (diff < 0) { return -1; }
            return 0;        }
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
        else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
