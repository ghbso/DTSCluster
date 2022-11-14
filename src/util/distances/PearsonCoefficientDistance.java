/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.distances;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.patterndiscovery.DataPoint;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 *
 * @author jcsilva
 */
public class PearsonCoefficientDistance implements Distance, Serializable {

    @Override
    public double calcDistance(Object obj1, Object obj2) throws Exception {
        if (obj1.getClass().equals(String.class)) {
            return this.calcDistance((String) obj1, (String) obj2);
        } else if (obj1.getClass().equals(DataPoint.class)) {
            return this.calcDistance((DataPoint) obj1, (DataPoint) obj2);
        } else if (obj1.getClass().equals(DataPoint.class)) {
            return this.calcDistance((double[]) obj1, (double[]) obj2);

        }
        return Double.NaN;
    }

    @Override
    public double calcDistance(String obj1, String obj2) throws Exception {
        double v1[] = transformToNumbers(obj1);
        double v2[] = transformToNumbers(obj2);

        double calcDistancePerason = calcDistancePerason(v1, v2);
        if (obj1.length() > 1) {
//            System.out.println(obj1 + " " + obj2 + " " + calcDistancePerason);
//            for (double w : v1) {
//                System.out.println(w);
//            }
//            for (double w : v2) {
//                System.out.println(w);
//            }

//            System.out.println("-----");
        }
        return calcDistancePerason;
    }

    @Override
    public double calcDistance(DataPoint obj1, DataPoint obj2) throws Exception {
        double[] v1 = obj1.getValues();
        double[] v2 = obj2.getValues();
        return calcDistancePerason(v1, v2);
    }

    @Override
    public double calcDistance(double[] val1, double[] val2) throws Exception {
        if (val1.length != val2.length) {
            try {
                throw new Exception("Arrays must have the same length in Manhattan distance.");
            } catch (Exception ex) {
                Logger.getLogger(PearsonCoefficientDistance.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        double[] v1 = new double[val1.length];
        double[] v2 = new double[val2.length];

        for (int i = 0; i < val1.length; i++) {
            v1[i] = val1[i];
            v2[i] = val2[i];
        }
        return calcDistancePerason(v1, v2);
    }

    private double[] transformToNumbers(String str) {
        int aValue = 97;
        double values[] = new double[str.length()];
        for (int i = 0; i < str.length(); i++) {
            int number = str.charAt(i);
            double value = (number - aValue) + 1;
            values[i] = value;
        }
        return values;
    }

    private double calcMean(Double[] val) {
        Double mean = 0d;
        for (Double val11 : val) {
            mean += val11;
        }
        return mean /= val.length;
    }

    private double calcDistancePerason(double[] v1, double[] v2) {
        double correlation = new PearsonsCorrelation().correlation(v1, v2);
        double s = correlation;
        return 1 - s;
    }

//    @Override
//    public double distance(double[] val1, double[] val2) {
//        if (val1.length != val2.length) {
//            try {
//                throw new Exception("Arrays must have the same length in Manhattan distance.");
//            } catch (Exception ex) {
//                Logger.getLogger(PearsonCoefficientDistance.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        Double[] v1 = new Double[val1.length];
//        Double[] v2 = new Double[val2.length];
//
//        for (int i = 0; i < val1.length; i++) {
//            v1[i] = val1[i];
//            v2[i] = val2[i];
//        }
//        return distance(v1, v2);
//
//    }
//    @Override
//    public double distance(Double[] val1, Double[] val2) {
//
//        if (val1.length != val2.length) {
//            try {
//                throw new Exception("Arrays must have the same length in Manhattan distance.");
//            } catch (Exception ex) {
//                Logger.getLogger(PearsonCoefficientDistance.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
////        double meanVal1 = calcMean(val1);
////        double meanVal2 = calcMean(val2);
////        // sums the absolute differences of each coordinate
////        double num = 0d;
////        
//////        System.out.println(meanVal1 + " " + meanVal2);
////        for (int i = 0; i < val1.length; i++) {
////            double name = (val1[i] - meanVal1) * (val2[i] - meanVal2);
//////            System.out.println(name);
////            num += name;
////        }
////        
////        double sumVal1 = 0;
////        for (int i = 0; i < val1.length; i++) {
////            sumVal1 +=  Math.pow((val1[i] - meanVal1) , 2);
//////            System.out.println(sumVal1);
////        }
////        sumVal1 = Math.sqrt(sumVal1);
////        
////        double sumVal2 = 0;
////        for (int i = 0; i < val2.length; i++) {
////            sumVal2 +=  Math.pow((val2[i] - meanVal2) , 2);
//////            System.out.println(sumVal2);
////        }
////        sumVal2 = Math.sqrt(sumVal2);
////        
////        double dem = sumVal1 * sumVal2;
////        double pearsonCoefficient = num/dem;
////        
//        double[] v1 = new double[val1.length];
//        double[] v2 = new double[val2.length];
//
//        for (int i = 0; i < val1.length; i++) {
//            v1[i] = val1[i];
//            v2[i] = val2[i];
//        }
//        return calcDistancePerason(v1, v2);
//    }
//    @Override
//    public double calcDistance(Object obj1, Object obj2) throws Exception {
//        if (obj1.getClass().equals(DataPoint.class)) {
//            DataPoint dp1 = (DataPoint) obj1;
//            DataPoint dp2 = (DataPoint) obj2;
//            double[] values = dp1.getValues();
//            double[] values2 = dp2.getValues();
//            return this.distance(values, values2);
//        } else if (obj1.getClass().equals(String.class)) {
//            String v1 = (String) obj1;
//            String v2 = (String) obj2;
//            return this.calc(v1, v2);
//        } else {
//            double[] v1 = (double[]) obj1;
//            double[] v2 = (double[]) obj2;
//            return this.distance(v1, v2);
//        }
//    }
//    @Override
//    public double distanceAccordingVariation(Double[] val1, Double[] val2, double radius) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public double distanceAccordingVariation(double[] val1, double[] val2, double radius) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    @Override
//    public double calc(String obj1, String obj2) throws Exception {
//        double v1[] = transformToNumbers(obj1);
//        double v2[] = transformToNumbers(obj2);
//        return calcDistancePerason(v1, v2);
//
//    }
//    @Override
//    public double calcAccordingVariation(String obj1, String obj2, double variation) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
}
