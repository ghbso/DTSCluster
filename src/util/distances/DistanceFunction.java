package util.distances;

/**
 *
 * @author jcsilva
 *
 */
public interface DistanceFunction  extends Distance{

    public abstract double distance(double[] val1, double[] val2);

    public abstract double distance(Double[] val1, Double[] val2);

    public abstract double distanceAccordingVariation(Double[] val1, Double[] val2, double radius);

    public abstract double distanceAccordingVariation(double[] val1, double[] val2, double radius);

    
}
