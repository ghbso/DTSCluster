package util.distances;

import model.patterndiscovery.DataPoint;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class HammingDistance implements Distance {

    @Override
    public double calcDistance(Object obj1, Object obj2) throws Exception {
        if(obj1.getClass().equals(String.class)){
            return this.calcDistance((String) obj1, (String) obj2);
        }else if(obj1.getClass().equals(DataPoint.class)){
            return this.calcDistance((DataPoint) obj1, (DataPoint) obj2);
        }else if(obj1.getClass().equals(DataPoint.class)){
            return this.calcDistance((double[]) obj1, (double[]) obj2);
            
        }
        return Double.NaN;
    }
    
    @Override
    public double calcDistance(String obj1, String obj2) throws Exception {
        int distance = 0;
        for (int i = 0; i < obj1.length(); i++) {
            if (obj1.charAt(i) != obj2.charAt(i)) {
                distance++;
            }
        }
        return distance;
    }

    @Override
    public double calcDistance(DataPoint obj1, DataPoint obj2) throws Exception {
        DataPoint dp1 = (DataPoint) obj1;
        DataPoint dp2 = (DataPoint) obj2;

        double[] values1 = dp1.getValues();
        double[] values2 = dp2.getValues();
        return this.calcDistance(values1, values2);

    }

    @Override
    public double calcDistance(double[] val1, double[] val2) throws Exception {

        int distance = 0;
        for (int i = 0; i < val1.length; i++) {
            if (val1[i] != val2[i]) {
                distance++;
            }
        }
        return distance;
    }

//    @Override
//    public double distance(double[] val1, double[] val2) {
//        int distance = 0;
//        for (int i = 0; i < val1.length; i++) {
//            if (val1[i] != val2[i]) {
//                distance++;
//            }
//        }
//        return distance;
//    }
//    @Override
//    public double distance(Double[] val1, Double[] val2) {
//        int distance = 0;
//        for (int i = 0; i < val1.length; i++) {
//            if (val1[i] != val2[i]) {
//                distance++;
//            }
//        }
//        return distance;
//    }
//    @Override
//    public double distanceAccordingVariation(Double[] val1, Double[] val2, double radius) {
//        return this.distance(val1, val2);
//    }
//
//    @Override
//    public double distanceAccordingVariation(double[] val1, double[] val2, double radius) {
//        return this.distance(val1, val2);
//    }
//    @Override
//    public double calc(String obj1, String obj2) throws Exception {
//        int distance = 0;
//        for (int i = 0; i < obj1.length(); i++) {
//            if (obj1.charAt(i) != obj2.charAt(i)) {
//                distance++;
//            }
//        }
//        return distance;
//    }
//    @Override
//    public double calcAccordingVariation(String obj1, String obj2, double variation) throws Exception {
//        return calc(obj1, obj2);
//    }

//    @Override
//    public double calcDistance(Object obj1, Object obj2) throws Exception {
//        if (obj1.getClass().equals(String.class)) {
//            String str1 = (String) obj1;
//            String str2 = (String) obj2;
//            return this.calc(str1, str2);
//        } else {
//            double[] v1 = (double[]) obj1;
//            double[] v2 = (double[]) obj2;
//            return this.distance(v1, v2);
//        }
//    }

}
