/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.distances;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.patterndiscovery.DataPoint;
import util.exceptions.ManhatanDistanceException;

/**
 *
 * @author jcsilva
 */
public class ManhattanDistance implements Distance, Serializable {

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
    public double calcDistance(DataPoint obj1, DataPoint obj2) throws Exception {
        DataPoint dp1 = (DataPoint) obj1;
        DataPoint dp2 = (DataPoint) obj2;
        double[] values = dp1.getValues();
        double[] values2 = dp2.getValues();
        return this.calcDistance(values, values2);
    }

    @Override
    public double calcDistance(double[] val1, double[] val2) throws Exception {
        if (val1.length != val2.length) {
            try {
                throw new Exception("Arrays must have the same length in Manhattan distance.");
            } catch (Exception ex) {
                Logger.getLogger(ManhattanDistance.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // sums the absolute differences of each coordinate
        double sum = 0;
        for (int i = 0; i < val1.length; i++) {
            sum += Math.abs(val1[i] - val2[i]);
        }
        return sum;
    }

    @Override
    public double calcDistance(String s1, String s2) throws Exception {
        if (s1.length() != s2.length()) {
            throw new ManhatanDistanceException();
        }
        // sums the absolute differences of each coordinate
        double sum = 0;
        for (int i = 0; i < s1.length(); i++) {
            sum += Math.abs(s1.charAt(i) - s2.charAt(i));
        }
        return sum;
    }
    
    
//    @Override
//    public double calc(String s1, String s2) throws ManhatanDistanceException {
//        if (s1.length() != s2.length()) {
//            throw new ManhatanDistanceException();
//        }
//        // sums the absolute differences of each coordinate
//        double sum = 0;
//        for (int i = 0; i < s1.length(); i++) {
//            sum += Math.abs(s1.charAt(i) - s2.charAt(i));
//        }
//        return sum;
//    }
//
//    @Override
//    public double distance(double[] val1, double[] val2) {
//        if (val1.length != val2.length) {
//            try {
//                throw new Exception("Arrays must have the same length in Manhattan distance.");
//            } catch (Exception ex) {
//                Logger.getLogger(ManhattanDistance.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        // sums the absolute differences of each coordinate
//        double sum = 0;
//        for (int i = 0; i < val1.length; i++) {
//            sum += Math.abs(val1[i] - val2[i]);
//
//        }
//
//        return sum;
//
//    }
//
//    @Override
//    public double distance(Double[] val1, Double[] val2) {
//        if (val1.length != val2.length) {
//            try {
//                throw new Exception("Arrays must have the same length in Manhattan distance.");
//            } catch (Exception ex) {
//                Logger.getLogger(ManhattanDistance.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        // sums the absolute differences of each coordinate
//        double sum = 0;
//        for (int i = 0; i < val1.length; i++) {
//            sum += Math.abs(val1[i] - val2[i]);
//        }
//        return sum;
//    }
//
//    @Override
//    public double calcAccordingVariation(String s1, String s2, double radius) throws Exception {
//        if (s1.length() != s2.length()) {
//            throw new Exception("Strings must have the same length in Manhattan distance.");
//        }
//        // sums the absolute differences of each coordinate
//        double sum = 0;
//        // boolean flagVariation = false;
//        for (int i = 0; i < s1.length(); i++) {
//            double distance = Math.abs(s1.charAt(i) - s2.charAt(i));
//            if (distance <= radius) {
//                distance = 0;
//            }
//            sum += distance;
//        }
//        return sum;
//    }
//
//    @Override
//    public double distanceAccordingVariation(Double[] val1, Double[] val2, double variation) {
//
//        double sum = 0;
//        for (int i = 0; i < val1.length; i++) {
//            double distance = Math.abs(Math.abs(val1[i] - val2[i]));
//            if (distance <= variation) {
//                distance = 0;
//            }
//            sum += distance;
//        }
//        return sum;
//    }
//
//    @Override
//    public double distanceAccordingVariation(double[] val1, double[] val2, double radius) {
//
//        double sum = 0;
//        for (int i = 0; i < val1.length; i++) {
//            double distance = Math.abs(Math.abs(val1[i] - val2[i]));
//            if (distance <= radius) {
//                distance = 0;
//            }
//            sum += distance;
//        }
//        return sum;
//    }
//
//    @Override
//    public double calcDistance(Object obj1, Object obj2) throws Exception {
//        if (obj1.getClass().equals(String.class)) {
//            String str1 = (String) obj1;
//            String str2 = (String) obj2;
//            return this.calc(str1, str2);
//        } else if (obj1.getClass().equals(DataPoint.class)) {
//            DataPoint dp1 = (DataPoint) obj1;
//            DataPoint dp2 = (DataPoint) obj2;
//            double[] values = dp1.getValues();
//            double[] values2 = dp2.getValues();
//            return this.distance(values, values2);
//        } else {
//            double[] v1 = (double[]) obj1;
//            double[] v2 = (double[]) obj2;
//            return this.distance(v1, v2);
//        }
//    }
//
//    @Override
//    public double calcDistance(String obj1, String obj2) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

}
