package teste;

import control.DTSClusterExecutor;
import java.util.List;
import model.clusteringtimeseries.Cluster;
import model.patterndiscovery.TimeSeries;
import persistence.TimeSeriesDAOPlainFile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Teste {

    public static void main(String[] args) throws Exception {

        DTSClusterExecutor facade = new DTSClusterExecutor();
        TimeSeriesDAOPlainFile timeSeriesDAOPlainFile = new TimeSeriesDAOPlainFile();

        String dataset = "yeast-galactose-205genes-20time-points-values.txt";
        dataset = "y5-dataset-values-std.txt";
//        dataset = "y5-dataset-std-values.txt";

        int k = 5;
        int n = 17;
        int w = 17;
        String alpha = "abcdefgh";
        String kernel = "Gauss";
        double radius = 0.05;
        double h = 0.05;
        String distance = "Pearson";
        boolean isToStd = true;
        
        List<TimeSeries> tsList = timeSeriesDAOPlainFile.load("C:\\Users\\ghbso\\Downloads\\" + dataset, n);

        List<Cluster<String>> clusters = facade.doClustering(tsList,
                k,
                n,
                w,
                alpha,
                kernel,
                radius,
                h,
                distance,
                isToStd);

        for (Cluster cluster : clusters) {
            System.out.println(cluster.getCentroid() + " " + cluster.getCentroidDensity() + " " + cluster.getAllOcurrencesPositions().size());
//            System.out.println(cluster.getAllOcurrencesPositions());
            System.out.println(cluster.getMeanProfile(tsList, n).size());
            
        }
        
        List<Integer> pos = clusters.get(4).getAllOcurrencesPositions();
//        System.out.println(clusters.get(4).getMeanProfile(tsList, n).getDataPoints());
        for (Integer po : pos) {
            System.out.println(tsList.get(po).getDataPoints());
        }
    }
}
