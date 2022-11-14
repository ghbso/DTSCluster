package util.trees;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Key<T> implements Serializable{

    private T point;
//    private Double density; Comentario realizado em 2017.09.07
    private List<Integer> occurences;

    public Key(T point) {
        this.point = point;
        occurences = new ArrayList<>();
    }

//    public void setDensity(Double density) {
//        this.density = density;
//    }

//    public Double getDensity() {
//        return density;
//    }
    
    
    
    public void addOccurence(int pos) {
        occurences.add(pos);
    }

    public int getQntOccurence() {
        return occurences.size();
    }

    public T getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Key other = (Key) obj;
        return this.getPoint().equals(other.getPoint());
    }

    @Override
    public int hashCode() {
        int temp = 0;
        T dt = this.getPoint();
        temp = dt.hashCode();

        return temp;
    }

    public List<Integer> getOccurences() {
        return occurences;
    }

    @Override
    public String toString() {
        return point.toString();
    }

    public void addOccurences(List<Integer> newPositions) {
        this.occurences.addAll(newPositions);
    }

    
}
