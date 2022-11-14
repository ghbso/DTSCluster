package control;

import control.clustering.DTSCluster;
import control.patterndiscovery.DPDTS;
import java.util.List;
import java.util.Map;
import model.clusteringtimeseries.Cluster;
import model.patterndiscovery.Pattern;
import model.patterndiscovery.TimeSeries;
import util.exceptions.WLargerThanTimeSeriesSizeException;
import util.exceptions.WNegativeException;


public class DTSClusterExecutor {

    public List<Cluster<String>> doClustering(List<TimeSeries> tsList,
            Integer k,
            Integer n,
            Integer w,
            String alphabet,
            String kernel,
            Double radius,
            Double h,
            String distance,
            Boolean isToStandardize
    ) throws WNegativeException, WLargerThanTimeSeriesSizeException, Exception {
        DPDTS dpdtsMining = new DPDTS();

        List<Map.Entry<Pattern<String>, Double>> topEntries = dpdtsMining.findPatterns(tsList,
                Integer.MAX_VALUE,
                n,
                w,
                alphabet,
                kernel,
                radius,
                h,
                distance,
                isToStandardize);
    
        DTSCluster dpdtsCluster_v2 = new DTSCluster();
        
        return dpdtsCluster_v2.doClustering_tsList(tsList,
                k,
                n,
                w, 
                alphabet,
                kernel, 
                radius, 
                h,
                distance, 
                topEntries,
                isToStandardize);
        
    }
}
