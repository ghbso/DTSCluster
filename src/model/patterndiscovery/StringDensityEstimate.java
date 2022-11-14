/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.patterndiscovery;

import java.util.*;
import util.StringDensityComparator;

public class StringDensityEstimate<T> {

    private Map<T, Double> densityEstimate = null;

    public StringDensityEstimate()
    {
        this.densityEstimate = new HashMap<T, Double>();
    }

    public StringDensityEstimate(Map<T, Double> density) {
        this.densityEstimate=density;
    }

    public Map<T, Double> getEstimates() {
        return densityEstimate;
    }

    public void putAdding(T skey, Double value) {
        double d = value;
        // if already exists, adds the new value
        if (this.densityEstimate.containsKey(skey))
         {
            d += this.densityEstimate.get(skey);
        }
        this.densityEstimate.put(skey, d);
    }
    public List<Map.Entry<T, Double>> getTopEntries(int k){
        // order density values
        List <Map.Entry<T, Double>> entryList = new ArrayList<>(this.densityEstimate.entrySet()); 
//        Collections.sort(entryList,new StringDensityComparator());  // put in ascending order
       
        Collections.sort(entryList,new Comparator<Map.Entry<T, Double>>(){
            @Override
            public int compare(Map.Entry<T, Double> t, Map.Entry<T, Double> t1) {
                return t.getValue().compareTo(t1.getValue());
            }
            
        });  // put in ascending order
       
        Collections.reverse(entryList); // put in descending order
        
        // get top k entries
        if(k > entryList.size()){
            k = entryList.size(); // ensure that k is not greater than list.size()
        }
        List <Map.Entry<T, Double>> patterns = entryList.subList(0, k);
        
        return patterns;
    }

    public Double get(T key){
        return this.densityEstimate.get(key);
    }
}
