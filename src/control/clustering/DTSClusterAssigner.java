package control.clustering;

import java.util.AbstractMap;
import model.clusteringtimeseries.HillClimbing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.clusteringtimeseries.Cluster;
import model.clusteringtimeseries.HillClimbingPoint;
import model.patterndiscovery.Pattern;
import util.distances.Distance;
import util.trees.BkTree;
import util.trees.Key;
import util.trees.Tree;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gustavo Henrique B.S. Oliveira
 * @param <T> Tipo
 */
public class DTSClusterAssigner<T> {

    private Map<T, Cluster<T>> clustersLabels;
    private HillClimbing<T> hillClimbing;

    public void clearSequenceLabels() {
        hillClimbing.clearSequenceLabels();
    }

    public List<Cluster<T>> assignSequences(
            Map<T, Key<T>> subSeqs,
            List<Map.Entry<T, Double>> topEntries,
            Distance dist,
            double radius) throws Exception {

        clustersLabels = new HashMap<>();
        hillClimbing = new HillClimbing<>(topEntries, dist);
//        System.out.println("oi");
        int label = 0;

        for (Map.Entry<T, Double> entry : topEntries) {
            T sequence = entry.getKey();

            Double density = entry.getValue();
            Key<T> key = subSeqs.get(sequence);
//            key.setDensity(density);
            try {
                T maximaLocal = hillClimbing.upHill(sequence, radius);
//                System.out.println(sequence + " " + maximaLocal);
                Cluster<T> cluster = clustersLabels.get(maximaLocal);

//                System.out.println(cluster + " " + clustersLabels.entrySet());
                if (cluster != null) {
                    cluster.addElementToCluster(key);

//                    if (sequence.toString().equals("3.0,4.0,4.0,4.0,4.0,3.0")) {
//                        System.out.println("hey" + cluster.getCentroid() + " " + cluster.getLabel());
//                        System.out.println(key.getOccurences().contains(12009));
//                        System.out.println(cluster.getAllOcurrencesPositions().contains(12009));
////                        System.out.println(cluster.getAllOcurrencesPositions().contains(ke));
//                    }
                } else {
                    label++;
                    Cluster<T> newCluster = new Cluster<>(sequence, density, label);
                    newCluster.addElementToCluster(key);
//                    System.out.println(cluster.getAllSequences());
//                    clustersLabels.put(sequence, newCluster);
                    clustersLabels.put(maximaLocal, newCluster);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
//        for (Cluster<T> value : clustersLabels.values()) {
//            if(value.getCentroid().toString().equalsIgnoreCase("4.0,4.0,4.0,4.0,4.0,3.0")){
//                System.out.println(value.getAllOcurrencesPositions().contains(12009) + " " + value.size() + " " + value.getLabel());
//            }
//        }
        List<Cluster<T>> clusters = new ArrayList(clustersLabels.values());
        Collections.sort(clusters, (Cluster<T> t, Cluster<T> t1) -> {
            Integer qnt = t.size();
            Integer qnt2 = t1.size();

            return qnt.compareTo(qnt2) * -1;
        });
//        System.out.println(clusters.get(1).getAllOcurrencesPositions().contains(12009));
        Cluster<T> smallerCluster = clusters.get(clusters.size() - 1);
//        System.out.println(clustersLabels.size() + " " + smallerCluster.size());

//        System.out.println(smallerCluster.size());
        if (smallerCluster.size() < 2) { // has noise
            Cluster<T> noiseCluster = new Cluster(null, -1d, -1);

            List<Cluster<T>> unoisedClusters = new ArrayList<>();
            clusters.forEach(c -> {
//                System.out.println(c.size());
                if (c.size() < 2) {
                    List<Key<T>> keySequences = c.getKeySequences();
                    for (Key<T> keySequence : keySequences) {
//                        System.out.println(noiseCluster.getCentroid());
                        noiseCluster.addElementToCluster(keySequence);
                    }
                } else {
//                    System.out.println(c.getCentroid() + " " + c.size() );
//                    c.getKeySequences().forEach(j-> System.out.println(j.getPoint()));
                    unoisedClusters.add(c);
                }
            });
//            System.out.println(noiseCluster.size());
//            unoisedClusters.add(noiseCluster); /// PENSAR, PENSAR, PENSAR
            clusters = unoisedClusters;
        }

//        System.out.println();
        return clusters;
    }

    public List<Cluster<T>> assignSequencesNoHillClibing(
            Map<T, Key<T>> subSeqs,
            List<Map.Entry<T, Double>> topEntries,
            Distance dist,
            double radius) throws Exception {

        clustersLabels = new HashMap<>();

        BkTree<T> tree = new BkTree<>(dist);

        for (Map.Entry<T, Double> entry : topEntries) {
            T point = entry.getKey();
            double density = entry.getValue();
            HillClimbingPoint<T> sequencue = new HillClimbingPoint<>();
            sequencue.setVisited(Boolean.FALSE);
            sequencue.setDensity(density);
            tree.insert(point, -1);
        }

        int label = 0;

        List<Cluster<T>> clusters = new ArrayList();

        for (Map.Entry<T, Double> entry : topEntries) { 
         
            T sequence = entry.getKey();

            Cluster<T> cluster = clustersLabels.get(sequence);

            Double density = entry.getValue();

            if (cluster == null) {
                cluster = new Cluster(sequence, density, label);
                clusters.add(cluster);
                label++;
            }

            if (clustersLabels.get(sequence) == null) {
                List<T> neighbors = tree.neighborsSingleOccurrences(sequence, radius);
                for (T neighbor : neighbors) {

                    if (clustersLabels.get(neighbor) == null) {
                        cluster.addElementToCluster(subSeqs.get(neighbor));
                        clustersLabels.put(neighbor, cluster);
                    }
                }
            }
        }

        Collections.sort(clusters, (Cluster<T> t, Cluster<T> t1) -> {
            Integer qnt = t.size();
            Integer qnt2 = t1.size();

            return qnt.compareTo(qnt2) * -1;
        });
        Cluster<T> smallerCluster = clusters.get(clusters.size() - 1);
        if (smallerCluster.size() < 2) { // has noise
            Cluster<T> noiseCluster = new Cluster(null, -1d, -1);

            List<Cluster<T>> unoisedClusters = new ArrayList<>();
            clusters.forEach(c -> {
//                System.out.println(c.size());
                if (c.size() < 2) {
                    List<Key<T>> keySequences = c.getKeySequences();
                    for (Key<T> keySequence : keySequences) {
                        noiseCluster.addElementToCluster(keySequence);
                    }
                } else {
                    unoisedClusters.add(c);
                }
            });
//            System.out.println(unoisedClusters.size());
            clusters = unoisedClusters;
        }

        for (Cluster<T> cluster : clusters) {
//              System.out.println(cluster.getCentroid() + " " + " " + cluster.getLabel() + " " + cluster.getAllOcurrencesPositions().size());

        }

        return clusters;
    }

    public List<Cluster<T>> assignSequences2(
            Map<T, Key<T>> subSeqs,
            List<Map.Entry<Pattern<T>, Double>> candidatePatterns,
            Distance dist,
            double radius) throws Exception {

        List<Map.Entry<T, Double>> topEntries = new ArrayList<>();

        for (Map.Entry<Pattern<T>, Double> candidatePattern : candidatePatterns) {
            topEntries.add(new AbstractMap.SimpleEntry<T, Double>(candidatePattern.getKey().getPoint(), candidatePattern.getValue()));
        }
    
        clustersLabels = new HashMap<>();
        hillClimbing = new HillClimbing<>(topEntries, dist);

        int label = 0;
        for (Map.Entry<T, Double> entry : topEntries) {
            T sequence = entry.getKey();
            Double density = entry.getValue();
            Key<T> key = subSeqs.get(sequence);
//            key.setDensity(density);
            try {
                T maximaLocal = hillClimbing.upHill(sequence, radius);
//                System.out.println(sequence + " " + maximaLocal);
                Cluster<T> cluster = clustersLabels.get(maximaLocal);
                System.out.println(clustersLabels  + " " + clustersLabels.size());
                if (cluster != null) {
                    cluster.addElementToCluster(key);
                } else {
                    label++;
                    Cluster<T> newCluster = new Cluster<>(maximaLocal, density, label);
                    newCluster.addElementToCluster(key);
//                    System.out.println(cluster.getAllSequences());
//                    clustersLabels.put(sequence, newCluster);
                    clustersLabels.put(maximaLocal, newCluster);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        List<Cluster<T>> clusters = new ArrayList(clustersLabels.values());
        Collections.sort(clusters, (Cluster<T> t, Cluster<T> t1) -> {
            Integer qnt = t.size();
            Integer qnt2 = t1.size();

            return qnt.compareTo(qnt2) * -1;
        });
        for (Cluster<T> entry : clusters) {
            System.out.println(entry.getCentroid()); 
        }
//        System.out.println(clusters.size());
        Cluster<T> smallerCluster = clusters.get(clusters.size() - 1);
        System.out.println(smallerCluster.size() + " " + smallerCluster.getCentroid());
        if (smallerCluster.size() < 2) { // has noise
            Cluster<T> noiseCluster = new Cluster(null, -1d, -1);

            List<Cluster<T>> unoisedClusters = new ArrayList<>();
            clusters.forEach(c -> {

                if (c.size() < 2 /*1 manter*/) {
                    List<Key<T>> keySequences = c.getKeySequences();
                    for (Key<T> keySequence : keySequences) {
//                        System.out.println(noiseCluster.getCentroid());
                        noiseCluster.addElementToCluster(keySequence);
                    }
                } else {
//                    System.out.println(c.getCentroid() + " " + c.size() );
//                    c.getKeySequences().forEach(j-> System.out.println(j.getPoint()));
                    unoisedClusters.add(c);
                }
            });
//            System.out.println(noiseCluster.size());
//            unoisedClusters.add(noiseCluster); /// PENSAR, PENSAR, PENSAR
            clusters = unoisedClusters;
        }

//        System.out.println(clusters.size());
//        System.out.println();
        return clusters;
    }

    public List<Cluster<T>> assignSequences_alternative(
            Map<T, Key<T>> subSeqs,
            List<Map.Entry<T, Double>> topEntries,
            Distance dist,
            double radius) throws Exception {

        clustersLabels = new HashMap<>();
        hillClimbing = new HillClimbing<>(topEntries, dist);

        int label = 0;

        for (Map.Entry<T, Double> entry : topEntries) {
            T sequence = entry.getKey();
            Double density = entry.getValue();
            Key<T> key = subSeqs.get(sequence);
//            key.setDensity(density);
            try {
                T maximaLocal = hillClimbing.upHill_alternative(sequence, radius);
//                System.out.println(sequence + " " + maximaLocal);
                Cluster<T> cluster = clustersLabels.get(maximaLocal);
//                System.out.println(cluster + " " + clustersLabels.entrySet());
                if (cluster != null) {
//                    System.out.println(sequence + " " + cluster.getLabel());
                    cluster.addElementToCluster(key);
                } else {
                    label++;
                    Cluster<T> newCluster = new Cluster<>(sequence, density, label);
                    newCluster.addElementToCluster(key);
//                    System.out.println(cluster.getAllSequences());
//                    clustersLabels.put(sequence, newCluster);
                    clustersLabels.put(maximaLocal, newCluster);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        List<Cluster<T>> clusters = new ArrayList(clustersLabels.values());
        Collections.sort(clusters, (Cluster<T> t, Cluster<T> t1) -> {
            Integer qnt = t.size();
            Integer qnt2 = t1.size();

            return qnt.compareTo(qnt2) * -1;
        });

        return clusters;
    }

    public boolean isSuperCluster(
            Cluster<T> clusterA,
            Cluster<T> clusterB,
            Distance dist) throws Exception {

        for (Key<T> key : clusterB.getKeySequences()) {

            T sequence = key.getPoint();
//            System.out.println(sequence);
            try {
                T maximaLocal = hillClimbing.upHill_alternative(sequence, Double.MAX_VALUE);
//                System.out.println(clusterA.getCentroid());
                System.out.println(maximaLocal + " " + hillClimbing.getSd(maximaLocal));
//                System.out.println(maximaLocal + " " + clusterB.getCentroid() + " " + maximaLocal.equals(clusterB.getCentroid()));
                if (maximaLocal == clusterA.getCentroid()) {
//                    System.out.println("oi");
//                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
//            break;
        }
        return false;
    }

    public void saveClusters(List<Cluster<T>> kClusters, List<Cluster<T>> clusters, Distance d) throws Exception {
        clearSequenceLabels();

        System.out.println(kClusters.get(0));
        for (int i = kClusters.size(); i < clusters.size(); i++) {
            System.out.println("----- Cluster " + (i + 1));
            Cluster<T> cluster = clusters.get(i);
            Cluster<T> closerCluster = getCloserCluster(cluster, d, kClusters);
            System.out.println(closerCluster.getCentroid());
            boolean superCluster = isSuperCluster(closerCluster, cluster, d);
            if (superCluster) {
//                closerCluster.addAllElementsToCluster(cluster.getKeySequences());
            }
            break;
        }
    }

    private Cluster<T> getCloserCluster(Cluster<T> currentCluster,
            Distance d,
            List<Cluster<T>> clusters) throws Exception {
        Double minDist = Double.MAX_VALUE;
        Cluster<T> closer = null;
        for (Cluster<T> cluster : clusters) {
            double distance = currentCluster.dist(cluster, d);
            if (distance < minDist) {
                minDist = distance;
                closer = cluster;
            }
        }
        return closer;
    }
}
