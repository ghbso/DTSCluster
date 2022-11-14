/*
 *  Copyright 2012 by Josenildo Silva <jcsilva@ifma.edu.br>
 */
package util;

import java.util.Comparator;
import java.util.Map;

public class GenericDEComparator<T> implements Comparator<Map.Entry<T, Double>> {

    @Override
    public int compare(Map.Entry<T, Double> o1, Map.Entry<T, Double> o2) {
        Map.Entry<T, Double> e1 = o1;
        Map.Entry<T, Double> e2 = o2;
        
        //System.out.print("[GenericDEComparator] e1: " + e1 + " e2 " +e2);
        
        if (e1.getValue() > e2.getValue()) {
            //System.out.println(" 1");
            return 1;
        } else {
            if (e1.getValue() < e2.getValue()) {
                //System.out.println(" -1");
                return -1;
            }
        }
        //System.out.println(" 0");
        return 0;
    }
}
