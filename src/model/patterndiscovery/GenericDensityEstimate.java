package model.patterndiscovery;
import java.util.*;
import util.GenericDEComparator;

public class GenericDensityEstimate<T> {
    Map<T,Double> estimateTable;
        
    public <T> GenericDensityEstimate(){
        this.estimateTable=new HashMap<>(3189); // I've choosed 89 since its a prime.

    }
    
    
    public void put(T dataPoint, Double  estimate){
        estimateTable.put(dataPoint, estimate);
    }

    public void putAdding(T dataPoint, Double estimate){
        if (estimateTable.containsKey(dataPoint)){
            estimateTable.put(dataPoint, estimate+estimateTable.get(dataPoint));
        }
        else {
            estimateTable.put(dataPoint, estimate);
        }
    }
    public Double get(T dataPoint) {
        
        if ((estimateTable!=null) && (estimateTable.containsKey(dataPoint))){
         return estimateTable.get(dataPoint);
        }
        else
            return new Double(0);
    }

    public Set<T> getCodes()
    {
        return this.estimateTable.keySet();
        
    }
    public boolean isEmpty(){
        return this.estimateTable.isEmpty();
        
    }
    
    protected Double remove(T dataPoint){
        Double value = null;
        try {
            value = this.estimateTable.remove(dataPoint);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        return value;
    }
    
    public Map<T,Double> getEstimateTable() {
        //
        return new HashMap<>(this.estimateTable);
    }
    
    public Map<T,Double> getCopyOfEstimateTable() {
        //
        return new HashMap<>(this.estimateTable);
    }
    
    public List<Map.Entry<T,Double>> getTopEntries(int k){
       
        // order density values
        List <Map.Entry<T,Double>> entryList = new ArrayList<>(this.estimateTable.entrySet()); 
        Collections.sort(entryList,new GenericDEComparator<T>());
        Collections.reverse(entryList); // put in descending order
        
        // get top k entries
        if(k > entryList.size()){
            k = entryList.size(); // ensure that k is not greater than list.size()
        }
        List <Map.Entry<T,Double>> patterns = entryList.subList(0, k);
        
        return patterns;
    }
    
}
