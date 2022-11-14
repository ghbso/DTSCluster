package control.clustering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.clusteringtimeseries.Cluster;
import model.patterndiscovery.Pattern;
import model.patterndiscovery.TimeSeries;
import util.distances.Distance;
import util.distances.DistanceFactory;
import util.exceptions.WLargerThanTimeSeriesSizeException;
import util.exceptions.WNegativeException;
import util.kernels.Kernel;
import util.trees.Key;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class DTSCluster {

    
    

    /**
     * Retrieves the the k bigger clusters
     *
     * @param ts the time series to cluster
     * @param k is the number of clusters to find
     * @param n is the size of a subsequence of the time series, used in the
     * reduction process
     * @param w is the size of the subsequences. The string is split in slices
     * of size w in the density estimation step. The patterns are elements of a
     * vector espace formed by the set $alphabet^w$;
     * @param alphabet the alphabet used to discretize the time series
     * @param kernel the kernel function used to estimate the density for each
     * subsequence
     * @param radius defines a ball from which other subsequences are to be
     * considered as part of the neighborhood around a given pattern
     * @param h a bandwidth parameter and controls the smoothness of the density
     * estimates
     * @param topEntries the partial clusters found
     * @param d the distance function
     * @param isToStandardize the flag that indicates if it's necessary to
     * standardize the time series
     * @return a list of clusters
     * @throws WNegativeException WNegativeException if w is negative
     * @throws WLargerThanTimeSeriesSizeException
     * WLargerThanTimeSeriesSizeException if w is larger than time series size
     * @throws Exception Exception if occurs other exception
     */
    public List<Cluster<String>> doClustering(TimeSeries ts,
            Integer k,
            Integer n,
            Integer w,
            String alphabet,
            Kernel kernel,
            Double radius,
            Double h,
            List<Map.Entry<String, Double>> topEntries,
            Distance d,
            Boolean isToStandardize
    ) throws WNegativeException, WLargerThanTimeSeriesSizeException, Exception {

        StringBuffer discret = ts.reduceAndDiscretize(n, w, alphabet, isToStandardize);

        //==================================================================
        Map<String, Key<String>> subSeqs = new HashMap<>();
        for (int i = 0; i <= discret.length() - w; i += w) {
            String subStr = discret.substring(i, i + w);
            Key<String> key;
            if (subSeqs.containsKey(subStr)) {
                key = subSeqs.get(subStr);
            } else {
                key = new Key<>(subStr);
            }
            key.addOccurence(i);
            subSeqs.put(subStr, key);
        }

        DTSClusterAssigner<String> clusterAssigner = new DTSClusterAssigner<>();
        List<Cluster<String>> clusters = clusterAssigner.assignSequences(subSeqs, topEntries, d, radius);
        List<Cluster<String>> kClusters = getKClusters(clusters, k);
        return kClusters;
    }

    
    public List<Cluster<String>> doClustering_tsList(List<TimeSeries> tsList,
            Integer k,
            Integer n,
            Integer w,
            String alphabet,
            String kernel,
            Double radius,
            Double h,
            String distance,
            List<Map.Entry<Pattern<String>, Double>> topEntries,
            Boolean isToStandardize
    ) throws WNegativeException, WLargerThanTimeSeriesSizeException, Exception {

        Map<String, Key<String>> subSeqs = new HashMap<>();
        for (int i = 0; i < tsList.size(); i++) {
            String subStr = tsList.get(i).
                    reduceAndDiscretize(n, w, alphabet, isToStandardize).toString();

            Key<String> key;
            if (subSeqs.containsKey(subStr)) {
                key = subSeqs.get(subStr);
            } else {
                key = new Key<>(subStr);
            }
            key.addOccurence(i);
            subSeqs.put(subStr, key);
        }

        DTSClusterAssigner<String> clusterAssigner = new DTSClusterAssigner<>();
        List<Cluster<String>> clusters = clusterAssigner.assignSequences2(subSeqs,
                topEntries,
                DistanceFactory.getDistance(distance),
                radius);
        List<Cluster<String>> kClusters = getKClusters(clusters, k);
        return kClusters;
    }

    public List<Cluster<String>> doClustering_alternative(TimeSeries ts,
            Integer k,
            Integer n,
            Integer w,
            String alphabet,
            Kernel kernel,
            Double radius,
            Double h,
            List<Map.Entry<String, Double>> topEntries,
            Distance d,
            Boolean isToStandardize
    ) throws WNegativeException, WLargerThanTimeSeriesSizeException, Exception {

        StringBuffer discret = ts.reduceAndDiscretize(n, w, alphabet, isToStandardize);

        //==================================================================
        Map<String, Key<String>> subSeqs = new HashMap<>();
        for (int i = 0; i <= discret.length() - w; i += w) {
            String subStr = discret.substring(i, i + w);
            Key<String> key;
            if (subSeqs.containsKey(subStr)) {
                key = subSeqs.get(subStr);
            } else {
                key = new Key<>(subStr);
            }
            key.addOccurence(i);
            subSeqs.put(subStr, key);
        }

        DTSClusterAssigner<String> clusterAssigner = new DTSClusterAssigner<>();
        List<Cluster<String>> clusters = clusterAssigner.assignSequences_alternative(subSeqs,
                topEntries,
                d,
                radius);
        List<Cluster<String>> kClusters = getKClusters(clusters, k);
        return kClusters;
    }

    /**
     *
     * @param clusters the set of clusters
     * @param k the number of desired top clusters
     * @return the k-top clusters
     */
    public List<Cluster<String>> getKClusters(List<Cluster<String>> clusters, int k) {
        int tam = k;
        if (tam > clusters.size()) {
            tam = clusters.size();
        }

        return clusters.subList(0, tam);
    }

}
