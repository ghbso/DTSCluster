package util.distances;

public class DistanceFactory {

    public static Distance getDistance(Distances distance) {
        switch (distance) {
            case MANHATTAN:
                return new ManhattanDistance();
            case EUCLIDEAN:
                return new EuclideanDistance();
            case HAMMING:
                return new HammingDistance();
            case PEARSON:
                return new PearsonCoefficientDistance();
            default:
                return null;
        }
    }

    public static Distance getDistance(String distance) {
        Distance d = null;

        if (distance.equalsIgnoreCase("Manhattan")) {
            d = new ManhattanDistance();
        } else if (distance.equalsIgnoreCase("Hamming")) {
            d = new HammingDistance();
        } else if (distance.equalsIgnoreCase("Euclidean")) {
            d = new EuclideanDistance();
        } else if (distance.equalsIgnoreCase("Pearson")) {
            d = new PearsonCoefficientDistance();
        }
        return d;
    }

    public static enum Distances {

        EUCLIDEAN,
        MANHATTAN,
        HAMMING,
        PEARSON;

        public static Distances getDistance(String distance) {
            if (distance.equalsIgnoreCase("Manhattan")) {
                return MANHATTAN;
            } else if (distance.equalsIgnoreCase("Hamming")) {
                return HAMMING;
            } else if (distance.equalsIgnoreCase("Euclidean")) {
                return EUCLIDEAN;
            } else if (distance.equalsIgnoreCase("Pearson")) {
                return PEARSON;
            }
            return null;
        }
    };

    /*public static DistanceMetric<double[]> getNumericDistanceMetric(Distances selector) {
     DistanceMetric<double[]> d= null;
     /*switch (selector) {
     case EUCLIDEAN:
     d = new EuclideanDistance();
     break;
     }*/
    /*     return d;
     }*/

    
    
    
    @Deprecated
    public static Distance getNumericDistanceMetric(Distances selector) {
        Distance d = null;
        switch (selector) {
            case EUCLIDEAN:
                d = new EuclideanDistance();
                break;
            case MANHATTAN:
                d = new ManhattanDistance();
                break;
            case HAMMING:
                d = new HammingDistance();
                break;
            case PEARSON:
                d = new PearsonCoefficientDistance();
                break;

        }
        return d;
    }

    @Deprecated
    public static Distance getStringDistanceMetric(Distances selector) {
        Distance d = null;
        switch (selector) {
            case EUCLIDEAN:
                d = new EuclideanDistance();
                break;
            case MANHATTAN:
                d = new ManhattanDistance();
                break;
            case HAMMING:
                d = new HammingDistance();
                break;
            case PEARSON:
                d = new PearsonCoefficientDistance();
                break;

        }

        return d;
    }
}
