package util;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import model.patterndiscovery.Pattern;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class StringDensityComparator implements Comparator<Entry<Pattern<String>, Double>> {

    public int compare(Entry<Pattern<String>, Double> o1, Entry<Pattern<String>, Double> o2) {
        Entry e1 = (Entry) o1;
        Entry e2 = (Entry) o2;
        
        if((Double) e1.getValue() > (Double)e2.getValue()){
            return 1;
        }
        else{
            if((Double) e1.getValue() < (Double)e2.getValue()){
                return -1;
            }
        }
        return 0;
    }

}