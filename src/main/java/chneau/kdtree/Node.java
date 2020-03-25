package chneau.kdtree;

import java.util.ArrayList;
import java.util.List;

class Node implements Point {
    Point self;
    Node left;
    Node right;

    Node(List<Point> points, int axis) {
        if (points == null || points.size() == 0) {
            return;
        }
        if (points.size() == 1) {
            self = points.get(0);
            return;
        }
        points.sort((a, b) -> (int) (a.dimension(axis) - b.dimension(axis)));
        var mid = points.size() / 2;
        self = points.get(mid);
        var nextDim = (axis + 1) % dimensions();
        left = new Node(points.subList(0, mid), nextDim);
        right = new Node(points.subList(mid + 1, points.size()), nextDim);
    }

    Node(Point point, int axis) {
        this(List.of(point), axis);
    }

    public int dimensions() {
        return self.dimensions();
    }

    public double dimension(int i) {
        return self.dimension(i);
    }

    public void insert(Point p, int i) {
        if (p.dimension(i) < dimension(i)) {
            if (left == null) {
                left = new Node(p, i);
            } else {
                left.insert(p, (i + 1) % dimensions());
            }
        } else {
            if (right == null) {
                right = new Node(p, i);
            } else {
                right.insert(p, (i + 1) % dimensions());
            }
        }
    }

    public static class RemovedNodes {
        public Node returned;
        public Node subtitute;

        RemovedNodes(Node returned, Node subtitute) {
            this.returned = returned;
            this.subtitute = subtitute;
        }
    }

    public RemovedNodes remove(Point p, int axis) {
        for (int i = 0; i < dimensions(); i++) {
            if (dimension(i) == p.dimension(i)) {
                if (left != null) {
                    var rn = left.remove(p, (axis + 1) % dimensions());
                    if (rn.returned != null) {
                        if (rn.returned == left) {
                            left = rn.subtitute;
                        }
                        return new RemovedNodes(rn.returned, null);
                    }
                }
                if (right != null) {
                    var rn = right.remove(p, (axis + 1) % dimensions());
                    if (rn.returned != null) {
                        if (rn.returned == right) {
                            right = rn.subtitute;
                        }
                        return new RemovedNodes(rn.returned, null);
                    }
                }
            }
        }

        if (left != null) {
            var largest = left.findLargest(axis, null);
            var rn = left.remove(largest, (axis + 1) % dimensions());
            rn.returned.left = left;
            rn.returned.right = right;
            if (left == rn.returned) {
                rn.returned.left = rn.subtitute;
            }
            return new RemovedNodes(this, rn.returned);
        }
        if (right != null) {
            var smallest = left.findSmallest(axis, null);
            var rn = left.remove(smallest, (axis + 1) % dimensions());
            rn.returned.left = left;
            rn.returned.right = right;
            if (right == rn.returned) {
                rn.returned.right = rn.subtitute;
            }
            return new RemovedNodes(this, rn.returned);
        }
        return new RemovedNodes(this, null);
    }

     Node findSmallest(int axis, Node smallest) {
        if (smallest == null || dimension(axis) < smallest.dimension(axis)) {
            smallest = this;
        }
        if (left != null) {
            smallest = left.findSmallest(axis, smallest);
        }
        if (right != null) {
            smallest = right.findSmallest(axis, smallest);
        }
        return smallest;
    }

     Node findLargest(int axis, Node largest) {
        if (largest == null || dimension(axis) > largest.dimension(axis)) {
            largest = this;
        }
        if (left != null) {
            largest = left.findLargest(axis, largest);
        }
        if (right != null) {
            largest = right.findLargest(axis, largest);
        }
        return largest;
    }

    public List<Point> points() {
        List<Point> points = new ArrayList<Point>();
        if (left != null) {
            points = left.points();
        }
        points.add(self);
        if (right != null) {
            points.addAll(right.points());
        }
        return points;
    }

    @Override
    public String toString() {
        return self.toString();
    }
}
