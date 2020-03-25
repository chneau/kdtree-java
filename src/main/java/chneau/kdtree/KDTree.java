package chneau.kdtree;

import java.util.ArrayList;
import java.util.List;

public class KDTree {
    Node root;

    public KDTree(List<Point> points) {
        root = new Node(points, 0);
    }

    public void insert(Point p) {
        if (root == null) {
            root = new Node(p, 0);
        } else {
            root.insert(p, 0);
        }
    }

    public Point remove(Point p) {
        if (root.self == null || p == null) {
            return null;
        }
        var rn = root.remove(p, 0);
        if (rn.returned == root) {
            root = rn.subtitute;
        }
        if (rn.returned == null) {
            return null;
        }
        return rn.returned;
    }

    public void balance() {
        root = new Node(points(), 0);
    }

    public List<Point> points() {
        if (root == null) {
            return new ArrayList<Point>();
        }
        return root.points();
    }
}
