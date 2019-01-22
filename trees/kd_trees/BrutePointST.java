import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BrutePointST<Value>  {
    RedBlackBST<Point2D, Value> bst;
    // Construct an empty symbol table of points.
    public BrutePointST() {
        this.bst = new RedBlackBST<Point2D, Value>(); // use bst to hold all points and vals
    }

    // Is the symbol table empty?
    public boolean isEmpty() {
        return this.bst.isEmpty(); // check bst
    }

    // Number of points in the symbol table.
    public int size() {
        return this.bst.size(); // check bst
    }

    // Associate the value val with point p.
    public void put(Point2D p, Value val) {
        this.bst.put(p, val); // use bst method
    }

    // Value associated with point p.
    public Value get(Point2D p) {
        return this.bst.get(p); // use bst method
    }

    // Does the symbol table contain the point p?
    public boolean contains(Point2D p) {
        return this.bst.contains(p); // use bst method
    }

    // All points in the symbol table.
    public Iterable<Point2D> points() {
        return this.bst.keys(); // return iterable from bst
    }

    // All points in the symbol table that are inside the rectangle rect.
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> ps = new Queue<Point2D>();
        for (Point2D p : this.points()) {
            if (rect.contains(p)) {
                ps.enqueue(p); // enqueue points contained by the rect
            }
        }
        return ps;
    }

    // A nearest neighbor to point p; null if the symbol table is empty.
    public Point2D nearest(Point2D p) {
        // Point2D min = new Point2D();
        Point2D min = null;
        for (Point2D q : this.points()) {
            if (min == null) { min = q; }
            if (q.distanceSquaredTo(p) != 0 && q.distanceSquaredTo(p) < min.distanceSquaredTo(p)) {
                min = q; // compare distance to get the min distance
            }
        }
        return min;
    }

    // k points that are closest to point p.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        RedBlackBST<Double, Point2D> mins = new RedBlackBST<Double, Point2D>();
        Queue<Point2D> nears = new Queue<Point2D>();
        for (Point2D q : this.points()) {
            if (q.distanceSquaredTo(p) != 0) {
                mins.put(q.distanceSquaredTo(p), q);
            }
        }
        //mins.deleteMin(); // get rid of the
        for (int i = 0; i < k; i++) {
            nears.enqueue(mins.get(mins.min()));
            mins.deleteMin();
        }
        return nears;
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        BrutePointST<Integer> st = new BrutePointST<Integer>();
        double qx = Double.parseDouble(args[0]);
        double qy = Double.parseDouble(args[1]);
        double rx1 = Double.parseDouble(args[2]);
        double rx2 = Double.parseDouble(args[3]);
        double ry1 = Double.parseDouble(args[4]);
        double ry2 = Double.parseDouble(args[5]);
        int k = Integer.parseInt(args[6]);
        Point2D query = new Point2D(qx, qy);
        RectHV rect = new RectHV(rx1, ry1, rx2, ry2);
        int i = 0;
        while (!StdIn.isEmpty()) {
            double x = StdIn.readDouble();
            double y = StdIn.readDouble();
            Point2D p = new Point2D(x, y);
            st.put(p, i++);
        }
        StdOut.println("st.empty()? " + st.isEmpty());
        StdOut.println("st.size() = " + st.size());
        StdOut.println("First " + k + " values:");
        i = 0;
        for (Point2D p : st.points()) {
            StdOut.println("  " + st.get(p));
            if (i++ == k) {
                break;
            }
        }
        StdOut.println("st.contains(" + query + ")? " + st.contains(query));
        StdOut.println("st.range(" + rect + "):");
        for (Point2D p : st.range(rect)) {
            StdOut.println("  " + p);
        }
        StdOut.println("st.nearest(" + query + ") = " + st.nearest(query));
        StdOut.println("st.nearest(" + query + ", " + k + "):");
        for (Point2D p : st.nearest(query, k)) {
            StdOut.println("  " + p);
        }
    }
}
