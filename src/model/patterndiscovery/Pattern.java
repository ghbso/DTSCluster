package model.patterndiscovery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import util.trees.Key;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Gustavo Henrique B.S. Oliveira
 */
public class Pattern<T> implements Serializable {

    private T point;
    private List<Key<T>> sequences;

    public Pattern(T point) {
        this.point = point;
        sequences = new ArrayList<>();
    }

    public T getPoint() {
        return point;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pattern<?> other = (Pattern<?>) obj;
        if (!Objects.equals(this.point, other.point)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return point.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    public List<Key<T>> getSequences() {
        return sequences;
    }

    public void addKey(Key<T> key) {
        if (!sequences.contains(key)) {
            sequences.add(key);
        }
    }

    public List<Integer> getNeighborsOcurrencesPositions() {
        List<Integer> positions = new ArrayList<>();
        for (Key<T> sequence : sequences) {
            if (!sequence.getPoint().equals(point)) {

//            System.out.println(sequence.getPoint() + " " + sequence.getOccurences());
                positions.addAll(sequence.getOccurences());
//            System.out.println(positions);
            }
        }
        return positions;

    }

    public List<Integer> getOcurrencePositions() {
        List<Integer> positions = new ArrayList<>();
        for (Key<T> sequence : sequences) {
            if (sequence.getPoint().equals(point)) {
//                System.out.println(sequence.getPoint().toString());
                positions.addAll(sequence.getOccurences());
            }
        }
        return positions;

    }

}
