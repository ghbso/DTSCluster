package util.distances;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gustavo Henrique B.S. Oliveira
 */
public class DistanceUtil {

    public static double frac = 0.1d;

    public double downToNearest(double value) {
        if (frac == 0d) {
            frac = 0.1;
        }
        frac = 0.1d;
        double newValue = Math.floor(value / frac) * frac;
        newValue= (double) Math.round(newValue * 100) / 100;
        return newValue;
    }

}
