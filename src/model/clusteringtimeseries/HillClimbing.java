package model.clusteringtimeseries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import util.distances.Distance;
import util.distances.EuclideanDistance;
import util.distances.ManhattanDistance;
import util.trees.BkTree;
import util.trees.Tree;

public class HillClimbing<T> {

    private Map<T, HillClimbingPoint<T>> hillClimbingSequences;
    private Tree<T> tree;
    private Tree<T> tree_alternative;
    private Distance distanceCalculator;

    
    private List<HillClimbingPoint<T>> navigatedPoints;

    public HillClimbing(List<Map.Entry<T, Double>> topEntries, Distance dist) {
        this.distanceCalculator = dist;
        hillClimbingSequences = new HashMap<>();
        navigatedPoints = new ArrayList<>();
        tree = new BkTree<>(distanceCalculator);
        tree_alternative = new BkTree<>(new EuclideanDistance());
        for (Map.Entry<T, Double> entry : topEntries) {
            T point = entry.getKey();
            double density = entry.getValue();
            HillClimbingPoint<T> sequencue = new HillClimbingPoint<>();
            sequencue.setVisited(Boolean.FALSE);
            sequencue.setDensity(density);
            tree.insert(point, -1);
            tree_alternative.insert(point, -1);
            hillClimbingSequences.put(point, sequencue);
        }
    }

    public Double getSd(T sequence) {
        HillClimbingPoint<T> sequenceHillClimbing = hillClimbingSequences.get(sequence);
        Double sequenceDensity = sequenceHillClimbing.getDensity();
        return sequenceDensity;
    }

    public T upHill(T sequence, double r) throws Exception {

        T maximaLocal = null;

        HillClimbingPoint<T> sequenceHillClimbing = hillClimbingSequences.get(sequence);
        Double sequenceDensity = sequenceHillClimbing.getDensity();
//        System.out.println(sequence + " " + sequenceHillClimbing.isLabeled());
        if (!sequenceHillClimbing.isLabeled()) {
            List<T> neighbors = tree.neighbors(sequence, r);
//            System.out.println("--->" + sequence + " " + neighbors.size());
            T denserNeighbor = sequence;
            double denserNeighborDensity = Double.MIN_VALUE;
            double minDistance = Double.MAX_VALUE;
            for (T neigh : neighbors) {
//                System.out.println("--->" + sequence + ":" + denserNeighbor);
                if (!neigh.equals(sequence)) {
                    HillClimbingPoint<T> neighHillClimbing = hillClimbingSequences.get(neigh);
                    double densityNeigh = neighHillClimbing.getDensity();
                    double distance = distanceCalculator.calcDistance(sequence, neigh);
//                    System.out.println(sequence + " " + neigh + " " + densityNeigh + " "  +  distance );
                    if (distance <= minDistance && densityNeigh >= sequenceDensity) {

                        if ((densityNeigh > denserNeighborDensity) || (distance < minDistance)) {
                            denserNeighborDensity = densityNeigh;
                            minDistance = distance;
                            denserNeighbor = neigh;
                        }
                    }
                }
            }
//            System.out.println("*" + sequence + " " + denserNeighbor);
            HillClimbingPoint<T> denserNeighborPoint = this.hillClimbingSequences.get(denserNeighbor);

            if (denserNeighbor.equals(sequence)) {
                maximaLocal = sequence;
                denserNeighborPoint.clearNavigationPath();

            } else if (denserNeighborPoint.isVisited() && !denserNeighborPoint.isLabeled()) {
                maximaLocal = sequence;
                sequenceHillClimbing.clearNavigationPath();

            } else {
                sequenceHillClimbing.setNavagated(true);
                denserNeighborPoint.setLastPointVisited(sequenceHillClimbing);
                maximaLocal = upHill(denserNeighbor, r);
            }

            sequenceHillClimbing.setVisited(Boolean.TRUE);
            sequenceHillClimbing.setMaximaLocal(maximaLocal);
            return maximaLocal;
        } else {
            return sequenceHillClimbing.getMaximaLocal();
        }
    }

    public T upHill_alternative(T sequence, double r) throws Exception {

        T maximaLocal = null;

        HillClimbingPoint<T> sequenceHillClimbing = hillClimbingSequences.get(sequence);
        Double sequenceDensity = sequenceHillClimbing.getDensity();
//        System.out.println(tree_alternative.getSize());
//        System.out.println(sequence + " isLabeled? " + sequenceHillClimbing.isLabeled());
        if (!sequenceHillClimbing.isLabeled()) {
            List<T> neighbors = tree_alternative.neighbors(sequence, r);
//            System.out.println(neighbors.size());
            T denserNeighbor = sequence;
            double denserNeighborDensity = Double.MIN_VALUE;
            double minDistance = Double.MAX_VALUE;
            for (T neigh : neighbors) {
                if (!neigh.equals(sequence)) {
                    HillClimbingPoint<T> neighHillClimbing = hillClimbingSequences.get(neigh);
                    double densityNeigh = neighHillClimbing.getDensity();
                    double distance = distanceCalculator.calcDistance(sequence, neigh);

                    if (distance <= minDistance && densityNeigh >= sequenceDensity) {

                        if ((densityNeigh > denserNeighborDensity) || (distance < minDistance)) {
                            denserNeighborDensity = densityNeigh;
                            minDistance = distance;
                            denserNeighbor = neigh;
                        }
                    }
                }
            }

            HillClimbingPoint<T> denserNeighborPoint = this.hillClimbingSequences.get(denserNeighbor);
            if (denserNeighbor.equals(sequence)) {
                maximaLocal = sequence;
                denserNeighborPoint.clearNavigationPath();

            } else if (denserNeighborPoint.isVisited() && !denserNeighborPoint.isLabeled()) {
                maximaLocal = sequence;
                sequenceHillClimbing.clearNavigationPath();

            } else {
                sequenceHillClimbing.setNavagated(true);
                denserNeighborPoint.setLastPointVisited(sequenceHillClimbing);
//                System.out.println("up hill " + denserNeighbor + " " + denserNeighborDensity);
                maximaLocal = upHill_alternative(denserNeighbor, r);
            }

            sequenceHillClimbing.setVisited(Boolean.TRUE);
            sequenceHillClimbing.setMaximaLocal(maximaLocal);
            return maximaLocal;
        } else {
            return sequenceHillClimbing.getMaximaLocal();
        }
    }

    @Deprecated
    public T upHillOld(T sequence, double r) throws Exception {

        T maximaLocal = null;

        HillClimbingPoint<T> sequenceHillClimbing = hillClimbingSequences.get(sequence);
        Double sequenceDensity = sequenceHillClimbing.getDensity();

        if (!sequenceHillClimbing.isLabeled()) {

            List<T> neighbors = tree.neighbors(sequence, r);
            T denserNeighbor = sequence;

            double denserNeighborDensity = Double.MIN_VALUE;
            double minDistance = Double.MAX_VALUE;
            System.out.println(sequence);
            for (T neigh : neighbors) {
                HillClimbingPoint<T> neighHillClimbing = hillClimbingSequences.get(neigh);
                double densityNeigh = neighHillClimbing.getDensity();
                double distance = distanceCalculator.calcDistance(sequence, neigh);
                System.out.println(neigh + " " + densityNeigh + " " + distance);
                if (distance <= minDistance && densityNeigh > sequenceDensity) {

                    if ((densityNeigh > denserNeighborDensity) || (distance < minDistance)) {
                        denserNeighborDensity = densityNeigh;
                        minDistance = distance;
                        System.out.println("---> " + denserNeighbor);
                        denserNeighbor = neigh;
                    }
                }

            }
//            System.out.println(sequence + " " + denserNeighbor);
            if (denserNeighbor.equals(sequence)) {
                maximaLocal = sequence;
            } else {
                maximaLocal = upHill(denserNeighbor, r);
            }

            sequenceHillClimbing.setVisited(Boolean.TRUE);
            sequenceHillClimbing.setMaximaLocal(maximaLocal);
            hillClimbingSequences.put(sequence, sequenceHillClimbing);
            return maximaLocal;
        } else {
            return sequenceHillClimbing.getMaximaLocal();
        }
    }

    public void clearSequenceLabels() {
        Collection<HillClimbingPoint<T>> sequences = hillClimbingSequences.values();
        sequences.stream().forEach(sq -> {
            sq.setVisited(false);
            sq.setLastPointVisited(null);
            sq.setMaximaLocal(null);
            sq.setNavagated(false);
        });
    }

}
