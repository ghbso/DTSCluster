/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.distances;

public interface NumericDistanceMetric<T> {

    public double calc(T obj1,T obj2)throws Exception;
}
