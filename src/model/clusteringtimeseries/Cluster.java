package model.clusteringtimeseries;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.patterndiscovery.TimeSeries;
import util.distances.Distance;
import util.distances.EuclideanDistance;
import util.timeseries.DiscretizedToTimeSeriesComparator;
import util.timeseries.TimeSeriesOperation;
import util.trees.Key;

/**
 *
 * @author GUSTAVOHENRIQUE2
 * @param <T> Subsequence class
 */
public class Cluster<T> implements Serializable {

    private T centroid;
    private Double centroidDensity;

    /**
     *
     */
    private int label;
    private List<Key<T>> sequences;

    /**
     *
     * @param centroid is cluster centroid
     * @param centroidDensity is cluster centroid density
     */
    public Cluster(T centroid, Double centroidDensity) {
        this.centroid = centroid;
        this.centroidDensity = centroidDensity;
        this.sequences = new ArrayList<>();
    }

    public double dist(Cluster<T> other, Distance d) throws Exception {
        return d.calcDistance(this.centroid, other.getCentroid());
    }

    /**
     *
     * @param centroid is cluster centroid
     * @param centroidDensity is cluster centroid density
     * @param label is cluster centroid label
     */
    public Cluster(T centroid, Double centroidDensity, int label) {
        this.centroid = centroid;
        this.sequences = new ArrayList<>();
        this.centroidDensity = centroidDensity;

        this.label = label;
    }

    public T getCentroid() {
        return centroid;
    }

    
    public void addElementToCluster(Key<T> sequence) {
        if (sequence != null) {
            sequences.add(sequence);
        }
    }

    public void addAllElementsToCluster(List<Key<T>> sequences) {
        this.sequences.addAll(sequences);

    }

    public int size() {
        int size = 0;
        for (Key sequenceCluster : sequences) {
            for (int qnt = 0; qnt < sequenceCluster.getQntOccurence(); qnt++) {
                size++;
            }
        }
        return size;
    }

    public List<Key<T>> getKeySequences() {
        return this.sequences;
    }

    public int getQntKeySequences() {
        return this.sequences.size();
    }

    public Key<T> getKey(T point) {
        for (Key<T> key : sequences) {
            if (key.getPoint().equals(point)) {
                return key;
            }
        }
        return null;
    }

    public Double getCentroidDensity() {
        return centroidDensity;
    }

    public List<T> getAllSequences() {
        List<T> allSequences = new ArrayList<>();
        for (Key<T> sequenceCluster : sequences) {
            for (int qnt = 0; qnt < sequenceCluster.getQntOccurence(); qnt++) {
                allSequences.add(sequenceCluster.getPoint());
            }
        }

        return allSequences;
    }

    public List<Integer> getAllOcurrencesPositions() {
        List<Integer> positions = new ArrayList();
        for (Key k : getKeySequences()) {
            positions.addAll(k.getOccurences()/*c.getSequenceOccurrences(k)*/);
        }
        return positions;
    }

    public TimeSeries getMeanProfile(TimeSeries ts,
            int n,
            //            int s,
            int w) throws Exception {

        int window = TimeSeriesOperation.getEquivalentPositionOnOriginalTS(w, n, w);
//        System.out.println(window);
        double[] sumValues = new double[window];

        for (int m = 0; m < sumValues.length; m++) {
            sumValues[m] = 0;
        }

        List<Integer> positions = getAllOcurrencesPositions();
        for (int j = 0; j < positions.size(); j++) {
            //System.out.println(positions.get(j));
            int startPosition = TimeSeriesOperation.getEquivalentPositionOnOriginalTS(positions.get(j), n, w);
            int endPosition = TimeSeriesOperation.getEquivalentPositionOnOriginalTS((positions.get(j) + w), n, w);
            int indexSum = 0;
            for (int i = startPosition; i < endPosition; i++) {
                sumValues[indexSum] += ts.get(i);
                indexSum++;
            }

        }
        List<Double> meanValues = new ArrayList<>();
        for (Double sum : sumValues) {
            Double mean = sum / positions.size();
            meanValues.add(mean);
        }

        return new TimeSeries(meanValues);
    }
    
    public TimeSeries getMeanProfile(List<TimeSeries> tsList, int n) throws Exception {

        double[] sumValues = new double[n];

        for (int m = 0; m < sumValues.length; m++) {
            sumValues[m] = 0;
        }

        List<Integer> positions = getAllOcurrencesPositions();
//        System.out.println(positions);
        for (int j = 0; j < positions.size(); j++) {
            TimeSeries ts = tsList.get(positions.get(j));
            //System.out.println(positions.get(j));
            int startPosition = 0;
            int endPosition = ts.size();
            int indexSum = 0;
            for (int i = startPosition; i < endPosition; i++) {
                sumValues[indexSum] += ts.get(i);
                indexSum++;
            }

        }
        List<Double> meanValues = new ArrayList<>();
        for (Double sum : sumValues) {
            Double mean = sum / positions.size();
            meanValues.add(mean);
        }

        return new TimeSeries(meanValues);
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public int getLabel() {
        return label;
    }

    private List<TimeSeries> buildTimeSeriesList(List<Key<T>> keySequences,
            Object discVariable,
            boolean isStd) {

        List<TimeSeries> tsList = new ArrayList();
        for (Key<T> keySequence : keySequences) {
            try {
                T point = keySequence.getPoint();
                TimeSeries discretizeToTimeSeries = TimeSeriesOperation.discretize2TimeSeries(point, discVariable, isStd);
                tsList.add(discretizeToTimeSeries);
            } catch (Exception ex) {
                Logger.getLogger(Cluster.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(tsList, new DiscretizedToTimeSeriesComparator());
        return tsList;
    }

    public double u(Object discVariable,
            boolean isStd) throws Exception {

        TimeSeries supTimeSeries = supTimeSeries(discVariable, isStd);
        TimeSeries infTimeSeries = infTimeSeries(discVariable, isStd);

        if (getCentroid().equals("aaaaabccccbbabbcc")) {
//            System.out.println("akie");
//            System.out.println(supTimeSeries.getDataPoints());
//            System.out.println(infTimeSeries.getDataPoints());
//            System.out.println(getAllOcurrencesPositions());
        }

        Double d1[] = new Double[supTimeSeries.size()];
        Double d2[] = new Double[infTimeSeries.size()];

        for (int i = 0; i < supTimeSeries.size(); i++) {
            d1[i] = supTimeSeries.get(i);
            d2[i] = infTimeSeries.get(i);

        }
        EuclideanDistance euclideanDistance = new EuclideanDistance();
        double u = euclideanDistance.calcDistance(d1, d2);
//        System.out.println(u);
        return u;

    }

    public TimeSeries supTimeSeries(Object discVariable,
            boolean isStd) {
        List<Key<T>> keySequences = getKeySequences();
        List<TimeSeries> buildTimeSeriesList = buildTimeSeriesList(keySequences, discVariable, isStd);
        return buildTimeSeriesList.get(keySequences.size() - 1);

    }

    public TimeSeries infTimeSeries(Object discVariable,
            boolean isStd) {
        List<Key<T>> keySequences = getKeySequences();
        List<TimeSeries> buildTimeSeriesList = buildTimeSeriesList(keySequences, discVariable, isStd);
        return buildTimeSeriesList.get(0);
    }

    @Override
    public String toString() {
        return centroid.toString();
    }

  
}
