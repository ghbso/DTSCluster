/*
 * EuclideanDistance.java
 *
 * Created on August 9, 2004, 5:33 PM
 */
package util.distances;

import model.patterndiscovery.DataPoint;

/**
 *
 * @author jcsilva
 */
public class EuclideanDistance implements Distance {

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
        double dist = 0;
        double sqrsum = 0;
        if (obj1.length() == obj2.length()) {
            for (int i = 0; i < obj1.length(); i++) {
                sqrsum = sqrsum + Math.pow((obj1.charAt(i) - obj2.charAt(i)), 2);
            }
            dist = Math.sqrt(sqrsum);
        }
        return dist;
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
    public double calcDistance(double[] p1, double[] p2) throws Exception {
        double dist = 0;
        double sqrsum = 0;
        if (p1.length == p2.length) {
            for (int i = 0; i < p1.length; i++) {
                sqrsum = sqrsum + Math.pow((p1[i] - p2[i]), 2);
            }
            dist = Math.sqrt(sqrsum);
        }
        return dist;
    }

//    @Override
//    public double distance(double[] p1, double[] p2) {
//        double dist = 0;
//        double sqrsum = 0;
//        if (p1.length == p2.length) {
//            for (int i = 0; i < p1.length; i++) {
//                sqrsum = sqrsum + Math.pow((p1[i] - p2[i]), 2);
//            }
//            dist = Math.sqrt(sqrsum);
//        }
//        return dist;
//    }
//    @Override
//    public double calc(String obj1, String obj2) throws Exception {
//        double dist = 0;
//        double sqrsum = 0;
//        if (obj1.length() == obj2.length()) {
//            for (int i = 0; i < obj1.length(); i++) {
//                sqrsum = sqrsum + Math.pow((obj1.charAt(i) - obj2.charAt(i)), 2);
//            }
//            dist = Math.sqrt(sqrsum);
//        }
//        return dist;
//    }
//    @Override
//    public double distance(Double[] val1, Double[] val2) {
//        double dist = 0;
//        double sqrsum = 0;
//        if (val1.length == val2.length) {
//            for (int i = 0; i < val1.length; i++) {
//                sqrsum = sqrsum + Math.pow((val1[i] - val2[i]), 2);
//            }
//            dist = Math.sqrt(sqrsum);
//        }
//        return dist;
//    }
//    @Override
//    public double distanceAccordingVariation(Double[] val1, Double[] val2, double radius) {
//
//        double dist = 0;
//        double sqrsum = 0;
//        if (val1.length == val2.length) {
//            for (int i = 0; i < val1.length; i++) {
//                double distAux = val1[i] - val2[i];
//                if (distAux <= radius) {
//                    distAux = 0;
//                }
//                sqrsum = sqrsum + Math.pow(distAux, 2);
//            }
//            dist = Math.sqrt(sqrsum);
//        }
//        return dist;
//    }
//    @Override
//    public double calcAccordingVariation(String obj1, String obj2, double radius) throws Exception {
//        double dist = Double.NaN;
//        double sqrsum = 0;
//        if (obj1.length() == obj2.length()) {
//
//            for (int i = 0; i < obj1.length(); i++) {
//                double distAux = Math.abs(obj1.charAt(i) - obj2.charAt(i));
//
//                if (distAux <= radius) {
//                    distAux = 0;
//                }
//                sqrsum = sqrsum + Math.pow(distAux, 2);
//            }
//            dist = Math.sqrt(sqrsum);
//        }
//        return dist;
//
//    }
//
//    @Override
//    public double distanceAccordingVariation(double[] val1, double[] val2, double radius) {
//        double dist = 0;
//        double sqrsum = 0;
//        if (val1.length == val2.length) {
//            for (int i = 0; i < val1.length; i++) {
//                double distAux = val1[i] - val2[i];
//                if (distAux <= radius) {
//                    distAux = 0;
//                }
//                sqrsum = sqrsum + Math.pow(distAux, 2);
//            }
//            dist = Math.sqrt(sqrsum);
//        }
//        return dist;
//    }
//    @Override
//    public double calcDistance(Object obj1, Object obj2) throws Exception {
//        if (obj1.getClass().equals(String.class)) {
//            String str1 = (String) obj1;
//            String str2 = (String) obj2;
//            return this.calc(str1, str2);
//        }
//        if (obj1.getClass().equals(DataPoint.class)) {
//            DataPoint dp1 = (DataPoint) obj1;
//            DataPoint dp2 = (DataPoint) obj2;
//
//            double[] values1 = dp1.getValues();
//            double[] values2 = dp2.getValues();
//            return this.distance(values1, values2);
//
//        } else {
//            double[] v1 = (double[]) obj1;
//            double[] v2 = (double[]) obj2;
//            return this.distance(v1, v2);
//        }
//    }


}
