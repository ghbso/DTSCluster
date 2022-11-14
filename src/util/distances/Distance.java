package util.distances;

import model.patterndiscovery.DataPoint;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public interface Distance {

    public double calcDistance(Object obj1, Object obj2) throws Exception;
    
    public double calcDistance(String obj1, String obj2) throws Exception;

    public double calcDistance(DataPoint obj1, DataPoint obj2) throws Exception;

    public double calcDistance(double[] obj1, double[] obj2) throws Exception;

}
