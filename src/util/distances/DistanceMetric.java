/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.distances;


@Deprecated
public interface DistanceMetric<T> extends Distance{
   
    public double calc(T obj1,T obj2)throws Exception;
//    public abstract double calcAccordingVariation(T obj1,T obj2, double variation)throws Exception; 
    
}
