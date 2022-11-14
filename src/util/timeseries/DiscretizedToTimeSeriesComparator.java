package util.timeseries;

import java.util.Comparator;
import model.patterndiscovery.TimeSeries;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Gustavo Henrique B.S. Oliveira
 */
public class DiscretizedToTimeSeriesComparator implements Comparator<TimeSeries> {

    @Override
    public int compare(TimeSeries t, TimeSeries t1) {
    
        for(int i = 0; i < t.size(); i++){
            if (!t.get(i).equals(t1.get(i)) || i == (t.size()-1)){
                return t.get(i).compareTo(t1.get(i));
            }
        }
        return 0;
        
    }

}
