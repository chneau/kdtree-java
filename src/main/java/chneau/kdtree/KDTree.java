package chneau.kdtree;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

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

    public List<Point> knn(Point p, int k) {
        if (root == null || p == null || k == 0) {
            return List.of();
        }
        var nearestPQ =
                new PriorityQueue<Point>(
                        (a, b) -> (int) (distance(p, a) * 1000 - distance(p, b) * 1000));
        knn(p, k, root, 0, nearestPQ);
        var result = new ArrayList<Point>();
        for (int i = 0; i < k; i++) {
            result.add(nearestPQ.poll());
        }
        return result;
    }

    private static void knn(
            Point p, int k, Node start, int currentAxis, PriorityQueue<Point> nearestPQ) {
        if (p == null || k == 0 || start == null) {
            return;
        }
        var path = new ArrayList<Node>();
        var currentNode = start;

        // 1. move down
        while (currentNode != null) {
            path.add(currentNode);
            if (p.dimension(currentAxis) < currentNode.dimension(currentAxis)) {
                currentNode = currentNode.left;
            } else {
                currentNode = currentNode.right;
            }
            currentAxis = (currentAxis + 1) % p.dimensions();
        }

        // 2. move up
        currentAxis = (currentAxis - 1 + p.dimensions()) % p.dimensions();
        while (true) {
            if (path.size() == 0) {
                break;
            }
            currentNode = path.remove(path.size() - 1);
            var currentDistance = distance(p, currentNode);
            var checkedDistance = distance(p, getNth(nearestPQ, k - 1));
            if (currentDistance < checkedDistance) {
                nearestPQ.add(currentNode);
                checkedDistance = distance(p, getNth(nearestPQ, k - 1));
            }

            // check other side of plane
            if (planeDistance(p, currentNode.dimension(currentAxis), currentAxis)
                    < checkedDistance) {
                Node next = null;
                if (p.dimension(currentAxis) < currentNode.dimension(currentAxis)) {
                    next = currentNode.right;
                } else {
                    next = currentNode.left;
                }
                knn(p, k, next, (currentAxis + 1) % p.dimensions(), nearestPQ);
            }
            currentAxis = (currentAxis - 1 + p.dimensions()) % p.dimensions();
        }
    }

    private static double planeDistance(Point p, double planePosition, int dim) {
        return Math.abs(planePosition - p.dimension(dim));
    }

    private static Point getNth(PriorityQueue<Point> nearestPQ, int i) {
        if (nearestPQ.size() <= i) {
            return null;
        }
        return (new ArrayList<>(nearestPQ)).get(i);
    }

    private static double distance(Point a, Point b) {
        var sum = 0.;
        for (int i = 0; i < a.dimensions(); i++) {
            sum += Math.pow(a.dimension(i), b.dimension(i));
        }
        return Math.sqrt(sum);
    }
}
