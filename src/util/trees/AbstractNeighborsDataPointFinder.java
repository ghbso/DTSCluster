package util.trees;

import model.patterndiscovery.DataPoint;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class AbstractNeighborsDataPointFinder extends AbstractNeighborsFinder<DataPoint, Double> {

    @Override
    List<Double> neighborSymbols(Double value, double radius) {
        List<Double> neighborSymbols = new ArrayList<>();

        if (radius > 0) {
            neighborSymbols.add(value + radius);
            neighborSymbols.add(value - radius);
        }

        neighborSymbols.add(value);
        return neighborSymbols;
    }

    @Override
    Double[] initVector(int w) {
        return new Double[w];
    }

    @Override
    DataPoint buildPoint(Double[] values) {
        return new DataPoint(values);
    }

    @Override
    Double valueAt(DataPoint point, int pos) {
        return point.getValue(pos);
    }

    @Override
    int length(DataPoint point) {
        return point.getDimension();
    }

    @Override
    double calculateDistance(Double symbolA, Double symbolB) {
        double distance = Math.abs(symbolA - symbolB);
        return distance;
    }

}
