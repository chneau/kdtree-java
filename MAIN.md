## testing the code

```java
package chneau.kdtree;

public class Main {
    static class Location implements Point {
        double lat;
        double lng;

        Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        public String toString() {
            return "[" + lat + ", " + lng + "]";
        }

        @Override
        public int dimensions() {
            return 2;
        }

        @Override
        public double dimension(int i) {
            if (i == 0) {
                return lat;
            }
            return lng;
        }
    }

    public static void main(String[] args) {
        var kdtree = new KDTree();
        for (int i = 0; i < 10; i++) {
            var x = new Location(i, i);
            kdtree.insert(x);
        }
        System.out.println(kdtree.knn(new Location(1, 1), 5));
    }
}
```
