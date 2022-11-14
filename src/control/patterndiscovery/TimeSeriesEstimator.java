package control.patterndiscovery;

import model.patterndiscovery.Pattern;
import model.patterndiscovery.StringDensityEstimate;
import util.distances.Distance;
import util.kernels.Kernel;
import util.trees.BkTree;
import util.trees.Key;
import util.trees.Tree;

import java.util.List;
import java.util.Map;

public class TimeSeriesEstimator {

    public StringDensityEstimate<Pattern<String>> estimateWithConcreteSequences(
            String discret,
            String alphabet,
            int s,
            Distance dist,
            double radius,
            Kernel kernelFunc,
            double h,
            List<Map.Entry<Pattern<String>, Double>> partialDensity)
            throws Exception {
        StringDensityEstimate<Pattern<String>> sde = new StringDensityEstimate<>();
        Tree<String> tree = new BkTree<>(dist);
        if (partialDensity != null) {
            for (Map.Entry<Pattern<String>, Double> entry : partialDensity) {
                sde.putAdding(entry.getKey(), entry.getValue());
                tree.insert(entry.getKey().getPoint(), -1);
            }
        }
        int count = 0;

        for (int i = 0; i <= discret.length() - s; i += s) {
            String subSeq = discret.substring(i, i + s);

            tree.insert(subSeq, i);
        }
    
        for (int i = 0; i <= discret.length() - s; i += s) {

            String subSeq = discret.substring(i, i + s);

            Pattern<String> candidate = new Pattern<>(subSeq);
            List<Key<String>> neighbors = tree.neighborsKeysSingleOccurrences(subSeq, radius);
            for (Key<String> neighbor : neighbors) {

                double distance = dist.calcDistance(subSeq, neighbor.getPoint());
                
                if (distance != Double.NaN) {
                    double density = kernelFunc.calculate((distance / h));
                    candidate.addKey(neighbor);
                    sde.putAdding(new Pattern<>(neighbor.getPoint()), density);
                }
            }

        }
        return sde;

    }
}
