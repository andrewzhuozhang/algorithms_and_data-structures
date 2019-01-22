import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedQueue;
import edu.princeton.cs.algs4.StdOut;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    int[][] tiles; // tiles in board
    int N; // board size
    int hamming; // hamming distance
    int manhattan; // manhattan distance

    // Construct a board from an N-by-N array of tiles, where
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank
    // square.
    public Board(int[][] tiles) {
        this.N = tiles.length; // size of board
        // copy tiles to the new board
        this.tiles = new int[this.N][this.N];
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                this.tiles[i][j] = tiles[i][j];
            }
        }
    
        // calculate hamming distance
        this.hamming = 0;
        // add 1 each time tile's not the same as goal
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.tiles[i][j] != 0 && this.tiles[i][j] != i * this.N + j + 1) {
                    this.hamming++;
                }
            }
        }
        // calculate manhattan distance
        this.manhattan = 0;
        int row = 0;
        int col = 0;
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                // get row and col of tile from goal board
            	if (this.tiles[i][j] != 0) {
            		row = (this.tiles[i][j]-1) / this.N;
                    col = (this.tiles[i][j]-1) % this.N;
                    // calculate distance then add to manhattan
                    this.manhattan += Math.abs(row - i);
                    this.manhattan += Math.abs(col - j);
                }
            }
        }
    }

    // helper method to flatten boards into 1d lists
    private int[] flatten() {
        int[] flat = new int[this.N * this.N];
        int i = 0;
        for (int[] list : this.tiles) {
            for (int tile : list) {
                flat[i++] = tile;
            }
        }
        return flat;
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        return this.tiles[i][j];
    }

    // Size of this board.
    public int size() {
        return this.N;
    }

    // Number of tiles out of place.
    public int hamming() {
        return this.hamming;
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        return this.manhattan;
    }

    // Is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j ++) {
                if (this.tiles[i][j] != 0 && this.tiles[i][j] != i*this.N+j+1) {
                    return false;
                }
            }
        }
        return true;
    }

    // Is this board solvable?
    public boolean isSolvable() {
        boolean solv = true;
        // for even number boards
        if (this.N % 2 == 0) {
            // unsolvable is blank row num plus num of inversions is even
            if ((this.blankRow()+this.inversions()) % 2 == 0) {
                solv = false;
            }
        } else {
            // odd boards, unsolvable is num of inversions is odd
            if (this.inversions() % 2 != 0) {
                solv = false;
            }
        }
        return solv;
    }

    // Does this board equal that?
    public boolean equals(Board that) {
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        LinkedQueue<Board> boards = new LinkedQueue<Board>();
        int bRow = (this.blankPos()-1) / this.N;
        int bCol = (this.blankPos()-1) % this.N;
        // doesn't have n if top row
        if (bRow > 0) {
            // neighbor n is created by moving the tile from north
            Board n = new Board(this.cloneTiles());
            n.tiles[bRow][bCol] =  n.tiles[bRow-1][bCol]; // assign n to tile 0
            n.tiles[bRow-1][bCol] = 0; // swap 0
            boards.enqueue(n);
        }
        if (bCol < this.N-1) {
            // neighbor e is created by moving the tile from east
            Board e = new Board(this.cloneTiles());
            e.tiles[bRow][bCol] =  e.tiles[bRow][bCol+1]; // assign s to tile 0
            e.tiles[bRow][bCol+1] = 0; // swap 0
            boards.enqueue(e);
        }
        if (bRow < this.N-1) {
            // neighbor s is created by moving the tile from south
            Board s = new Board(this.cloneTiles());
            s.tiles[bRow][bCol] =  s.tiles[bRow+1][bCol]; // assign s to tile 0
            s.tiles[bRow+1][bCol] = 0; // swap 0
            boards.enqueue(s);
        }
        if (bCol > 0) {
            // neighbor w is created by moving the tile from west
            Board w = new Board(this.cloneTiles());
            w.tiles[bRow][bCol] =  w.tiles[bRow][bCol-1]; // assign w to tile 0
            w.tiles[bRow][bCol-1] = 0; // swap 0
            boards.enqueue(w);
        }
        
        
        return boards;
    }

    // String representation of this board.
    public String toString() {
        String s = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s += String.format("%2d", tiles[i][j]);
                if (j < N - 1) {
                    s += " ";
                }
            }
            if (i < N - 1) {
                s += "\n";
            }
        }
        return s;
    }

    // Helper method that returns the position (in row-major order) of the
    // blank (zero) tile.
    private int blankPos() {
        int[] flat = this.flatten();
        int pos = 0;
        for (int i = 0; i < this.N*this.N; i++) {
            if (flat[i] == 0) { pos = i+1; }
        }
        return pos;
    }

    // blankrow number
    private int blankRow() {
        int pos = 0;
        for (int i = 0; i < this.N; i ++) {
            for (int j = 0; j < this.N; j++) {
                if (this.tiles[i][j] == 0) {
                    pos = i;
                }
            }
        }
        return pos;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        int[] flat = this.flatten(); // flatten board
        int invers = 0;
        // if any number after i is smaller, increment invers count
        for (int i = 0; i < this.N*this.N; i++) {
            for (int j = i+1; j < this.N*this.N; j++) {
                if (flat[i] != 0 && flat[j] != 0 && flat[i] > flat[j]) { 
                    invers++; 
                }
            }
        }
        return invers;
    }

    // Helper method that clones the tiles[][] array in this board and
    // returns it.
    private int[][] cloneTiles() {
        int[][] clone = new int[this.N][this.N];
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                clone[i][j] = this.tiles[i][j];
            }
        }
        return clone;
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
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
