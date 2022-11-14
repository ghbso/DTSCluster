package util.trees;

import java.util.List;

public interface Tree<T> {

    int size();

    void insert(T s, int posSeqquence);

    
    void remove(T s);

    T search(T s);

    List<T> neighborsSingleOccurrences(T s, double radious);

    List<Key<T>> neighborsKeysSingleOccurrences(T s, double radious);

    List<T> neighbors(T s, double radious);

    List<Integer> getOccurencesPositions(T d);

    int getQntOcurrencesNodo(T d);

}
