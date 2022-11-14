/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control.patterndiscovery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.patterndiscovery.Pattern;
import model.patterndiscovery.StringDensityEstimate;
import model.patterndiscovery.TimeSeries;
import util.distances.Distance;
import util.distances.DistanceFactory;
import util.exceptions.WLargerThanTimeSeriesSizeException;
import util.exceptions.WNegativeException;
import util.kernels.Kernel;
import util.kernels.KernelFunctionFactory;

/**
 * Discovers frequent patterns for a given time series.
 *
 * @author jcsilva
 */
public class DPDTS {

    private List<Entry<Pattern<String>, Double>> topEntries;

    /**
     * Retrieves the k most frequent patterns using DPDTS algorithm. BK indexing
     * is using to find neighbors, because we aren't yusing putAdding in all
     * abstract neighbors.
     *
     * @param ts is the time series to pattern discovery
     * @param k is the number of clusters to find
     * @param n is the size of a subsequence of the time series, used in the
     * reduction process
     * @param w is the size of the subsequences. The string is split in slices
     * of size w in the density estimation step. The patterns are elements of a
     * vector espace formed by the set $alphabet^w$;
     * @param alphabet is the alphabet used to discretize the time series
     * @param kfunc is the kernel function used to estimate the density for each
     * subsequence
     * @param radius defines a ball from which other subsequences are to be
     * considered as part of the neighborhood around a given pattern
     * @param h is a bandwidth parameter and controls the smoothness of the
     * density estimates
     * @param partialDensity is the partial clusters found
     * @param d is the distance function / * @param isToStandardize is the flag
     * that indicates if it's necessary to standardize the time series
     * @return a list of clusters
     * @throws WNegativeException WNegativeException if w is negative
     * @throws WLargerThanTimeSeriesSizeException
     * WLargerThanTimeSeriesSizeException if w is larger than time series size
     * @throws Exception Exception if occurs other exception
     */
    public List<Entry<Pattern<String>, Double>> findPatterns(TimeSeries ts,
            int k,
            int n,
            int w,
            String alphabet,
            //            int s,
            Kernel kfunc,
            double radius,
            double h,
            Distance d,
            boolean isToStandardize
    ) throws WNegativeException, WLargerThanTimeSeriesSizeException, Exception {
        try {
            StringBuffer discret = ts.reduceAndDiscretize(n, w, alphabet, isToStandardize);
            String sDiscret = discret.toString();

            TimeSeriesEstimator tse = new TimeSeriesEstimator();
            //==================================================================
            StringDensityEstimate<Pattern<String>> localDensity = tse.estimateWithConcreteSequences(sDiscret, alphabet, w, d, radius, kfunc, h, new ArrayList<>());
            //==================================================================
            // get top k most dense words of size s from density hotspotestimate
            StringDensityEstimate globalDensity = localDensity;
            // SEND LOCAL DENSITY TO OTHER PEERS
            // RECEIVE GLOBAL DENSITY FROM OTHER PEERS
            //System.out.println("Discret = "+sDiscret);
            return globalDensity.getTopEntries(Integer.MAX_VALUE);

        } catch (Exception ex) {
            Logger.getLogger(DPDTS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<Entry<Pattern<String>, Double>> findPatterns(List<TimeSeries> tsList,
            int k,
            int n,
            int w,
            String alphabet,
            String kfunc,
            double radius,
            double h,
            String d,
            boolean isToStandardize
    ) throws WNegativeException, WLargerThanTimeSeriesSizeException, Exception {
        try {

            StringBuffer discret = new StringBuffer();
            for (TimeSeries ts : tsList) {
                StringBuffer discretTs = ts.reduceAndDiscretize(n, w, alphabet, isToStandardize);
                discret.append(discretTs);
            }

            String sDiscret = discret.toString();
            TimeSeriesEstimator tse = new TimeSeriesEstimator();
            //==================================================================
            StringDensityEstimate<Pattern<String>> localDensity = tse.estimateWithConcreteSequences(sDiscret,
                    alphabet,
                    w,
                    DistanceFactory.getDistance(d),
                    radius,
                    KernelFunctionFactory.getKernel(kfunc),
                    h,
                    topEntries);
            //==================================================================
            // get top k most dense words of size s from density hotspotestimate
            StringDensityEstimate globalDensity = localDensity;
            // SEND LOCAL DENSITY TO OTHER PEERS
            // RECEIVE GLOBAL DENSITY FROM OTHER PEERS
            //System.out.println("Discret = "+sDiscret);
            return globalDensity.getTopEntries(Integer.MAX_VALUE);

        } catch (Exception ex) {
            Logger.getLogger(DPDTS.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     *
     * @param patterns is the list of patterns
     * @param k is the number of top patterns to find
     * @return the k-top patterns
     */
    public List<Entry<Pattern<String>, Double>> getKPattern(List<Entry<Pattern<String>, Double>> patterns, int k) {
        int size = k;
        if (size >= patterns.size()) {
            size = patterns.size();
        }
        List<Entry<Pattern<String>, Double>> topEntries = patterns.subList(0, size);
        return topEntries;

    }

}
