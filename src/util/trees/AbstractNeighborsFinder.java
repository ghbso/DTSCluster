package util.trees;

import util.distances.Distance;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractNeighborsFinder<T, E> {

    abstract E[] initVector(int w);

    abstract T buildPoint(E values[]);

    abstract List<E> neighborSymbols(E symbol, double radius);

    abstract E valueAt(T point, int pos);

    abstract int length(T point);

    abstract double calculateDistance(E symbolA, E symbolB);

    private List<T> generateSequences(T point,
            double radius,
            int pos,
            boolean isAllowVariation) {

        List<T> abstractNeighbors = new ArrayList<>();
        List<E> neighborSymbols = neighborSymbols(valueAt(point, pos), radius);

//        System.out.println(point + " " + pos + " " + neighborSymbols);
        int w = length(point);

        E points[] = initVector(w);
        for (E neighborSymbol : neighborSymbols) {
            for (int j = 0; j < w; j++) {
                if (j != pos) {
                    points[j] = valueAt(point, j);
                }
            }
            double rAllowed = radius;

            if (!isAllowVariation) {
                E c = valueAt(point, pos);
                if (!neighborSymbol.equals(c)) {
                    double distance = calculateDistance(c, neighborSymbol);
                    rAllowed -= distance;
                }
            }

            points[pos] = neighborSymbol;

            T abstractNeighbor = buildPoint(points);
            if (pos == (w - 1)) {
//                System.out.println("-------------------------> " + abstractNeighbor);
                abstractNeighbors.add(abstractNeighbor);
            } else {

                List<T> neighbors = generateSequences(
                        abstractNeighbor,
                        rAllowed,
                        (pos + 1),
                        isAllowVariation);
                abstractNeighbors.addAll(neighbors);
//                if (pos == 0) {
//                    System.out.println("---->" + pos + " " + neighborSymbols + " " + neighborSymbol + " " + abstractNeighbor + " " + neighbors.size());
//                }
            }

        }

        return abstractNeighbors;
    }

    public final List<T> getAbstractNeighbors(T point,
            Double radius,
            Distance distance) throws Exception {

        List<T> abstractNeighbors = generateSequences(
                point,
                Math.ceil(radius),
                0,
                false);
        List<T> correctNeighbors = new ArrayList<>();
//        System.out.println(abstractNeighbors.size());
        for (T abstractNeighbor : abstractNeighbors) {
//            System.out.println(abstractNeighbor + " " + point + " " + distance.calcDistance(abstractNeighbor, point));
            if (distance.calcDistance(abstractNeighbor, point) <= radius) {
            correctNeighbors.add(abstractNeighbor);
            }
        }

        return correctNeighbors;
    }

    public final List<T> getAllPossibleNeighbors(T point,
            Double radius) throws Exception {

        List<T> abstractNeighbors = generateSequences(
                point,
                radius,
                0,
                true);

        return abstractNeighbors;
    }
}
