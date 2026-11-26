# kdtree-java

KD-tree spatial index implemented in Java.

The tree supports inserting and removing points, rebalancing, listing all points and k-nearest-neighbour queries (`knn` for one or k neighbours).

## Build and test

```bash
./gradlew build
./gradlew test
```

## Layout

- `src/main/java/chneau/kdtree` — `KDTree`, `Node`, `Point`
- `src/test/java` — tests
- `MAIN.md` — additional notes

## License

MIT
