import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class KdTreePointST<Value> implements PointST<Value> {
    Node root; // root node of tree
    int N; // number of nodes

    // 2d-tree (generalization of a BST in 2d) representation.
    private class Node {
        private Point2D p;   // the point
        private Value val;   // the symbol table maps the point to this value
        private RectHV rect; // the axis-aligned rectangle corresponding to
                             // this node
        private Node lb;     // the left/bottom subtree
        private Node rt;     // the right/top subtree

        // Construct a node given the point, the associated value, and the
        // axis-aligned rectangle corresponding to the node.
        Node(Point2D p, Value val, RectHV rect) {
            this.p = p;
            this.val = val;
            this.rect = rect;
        }
    }

    // Construct an empty symbol table of points.
    public KdTreePointST() {
        // construct a new symbol table
        this.root = null;
        this.N = 0;
    }

    // Is the symbol table empty?
    public boolean isEmpty() {
        return this.N == 0;
    }

    // Number of points in the symbol table.
    public int size() {
        return this.N;
    }

    // Associate the value val with point p.
    public void put(Point2D p, Value val) {
        // new rect representing root
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        root = put(root, p, val, rect, true);
    }

    // Helper for put(Point2D p, Value val).
    private Node put(Node x, Point2D p, Value val, RectHV rect, boolean lr) {
        if (x == null) {
            this.N++;
            return new Node(p, val, rect); // if reached an empty location
        }
        else if (x.p.equals(p)) {
            x.val = val; // if key exists, update its val
            return x;
        }
        if (!x.p.equals(p)) {
            if (lr) {
                // compare horizontally
                if (p.x() < x.p.x()) {
                    // if new p falls to the left of node x (hori)
                    RectHV tmp = new RectHV(rect.xmin(), rect.ymin(),
                                            p.x(), rect.ymax());
                    x.lb = put(x.lb, p, val, tmp, !lr);
                } else {
                    // p falls to the right
                    RectHV tmp = new RectHV(p.x(), rect.ymin(),
                                            rect.xmax(), rect.ymax());
                    x.rt = put(x.rt, p, val, tmp, !lr);
                }
            } else {
                // compare vertically
                if (p.y() < x.p.y()) {
                    // if new p falls to the left of node x (vert)
                    RectHV tmp = new RectHV(rect.xmin(), rect.ymin(), 
                                            rect.xmax(), p.y());
                    x.lb = put(x.lb, p, val, tmp, !lr);
                } else {
                    // p falls to the right
                    RectHV tmp = new RectHV(rect.xmin(), p.y(), 
                                            rect.xmax(), rect.ymax());
                    x.rt = put(x.rt, p, val, tmp, !lr);
                }
            }
        }
        return x;
    }

    // Value associated with point p.
    public Value get(Point2D p) {
        if (p == null) { throw new java.lang.NullPointerException(); }
        return get(root, p, true);
    }

    // Helper for get(Point2D p).
    private Value get(Node x, Point2D p, boolean lr) {
        if (x == null) { return null; }
        if (x.p.equals(p)) {
            return x.val; 
        } else {
            if (lr) {
                if (p.x() < x.p.x()) { return get(x.lb, p, !lr); }
                else { return get(x.rt, p, !lr); }
            } else {
                if (p.y() < x.p.y()) { return get(x.lb, p, !lr); }
                else { return get(x.rt, p, !lr); }
            }
        }
    }

    // Does the symbol table contain the point p?
    public boolean contains(Point2D p) {
        return this.get(p) != null;
    }

    // All points in the symbol table, in level order.
    public Iterable<Point2D> points() {
        Queue<Point2D> pts = new Queue<Point2D>();
        Queue<Node> tmp = new Queue<Node>();
        pts.enqueue(root.p);
        tmp.enqueue(root);
        while (!tmp.isEmpty()) {
            Node x = tmp.dequeue();
            if (x.lb != null) {
                pts.enqueue(x.lb.p);
                tmp.enqueue(x.lb);
            }
            if (x.rt != null) {
                pts.enqueue(x.rt.p);
                tmp.enqueue(x.rt);
            }
        }
        return pts;
    }

    // All points in the symbol table that are inside the rectangle rect.
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> q = new Queue<Point2D>();
        range(root, rect, q);
        return q;
    }

    // Helper for public range(RectHV rect).
    private void range(Node x, RectHV rect, Queue<Point2D> q) {
        if (x == null) { return; } 
        // traverse thru all points, if rect contains then enqueue
        if (rect.contains(x.p)) { q.enqueue(x.p); } 
        // check if r and l subtrees intersect the current one
        // if yes then recursively do the same to the two sub trees
        range(x.lb, rect, q);
        range(x.rt, rect, q);
    }

    // A nearest neighbor to point p; null if the symbol table is empty.
    public Point2D nearest(Point2D p) {
    	MaxPQ<Point2D> pq = new MaxPQ<Point2D>(p.distanceToOrder());
        // Point2D nearest = null;
        // Double nearestDistance = Double.MAX_VALUE;
        if (root == null) { return null; }
        nearest(root,p,1,pq,true);
        Point2D near = pq.delMax();
        return near;
    }

    // Helper for public nearest(Point2D p).
    // private Point2D nearest(Node x, Point2D p, Point2D nearest,
    //                         double nearestDistance, boolean lr) {
    //     if (x == null) { return nearest; }
    //     // update nearest and distance
    //     if (!x.p.equals(p)) {
    //         if (x.p.distanceSquaredTo(p) < nearestDistance) {
    //             nearestDistance = x.p.distanceSquaredTo(p);
    //             nearest = x.p;
    //             //StdOut.println(nearestDistance); // TEST

    //         }
    //     }
    //     nearest = nearest(x.lb, p, nearest, nearestDistance, !lr);
    //     nearest(x.rt, p, nearest, nearestDistance, !lr);
    //     return nearest;
    // }

    // k points that are closest to point p.
    public Iterable<Point2D> nearest(Point2D p, int k) {
        MaxPQ<Point2D> pq = new MaxPQ<Point2D>(p.distanceToOrder());
        nearest(root, p, k, pq, true);
        return pq;
    }

    // Helper for public nearest(Point2D p, int k).
    private void nearest(Node x, Point2D p, int k, MaxPQ<Point2D> pq,
                         boolean lr) {
        if (x == null || pq.size() > k) { return; }
        if (!x.p.equals(p)) { pq.insert(x.p); }
        if (pq.size() > k) { pq.delMax(); }
        nearest(x.lb, p, k, pq, !lr);
        nearest(x.rt, p, k, pq, !lr);
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        KdTreePointST<Integer> st = new KdTreePointST<Integer>();
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
