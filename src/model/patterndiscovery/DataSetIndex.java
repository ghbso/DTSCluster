/*
 * DataSetIndex.java
 *
 * Created on September 9, 2004, 7:08 PM
 */
package model.patterndiscovery;

import java.util.*;

public class DataSetIndex {

    private DataSet ds = null;
    private Grid grid = null;
    private HashMap<Long, List<DataPoint>> hashMapIndex = new HashMap<>(89);

    public DataSetIndex(DataSet ds, Grid g) throws Exception {
        this.ds = ds;
        this.grid = g;

        if ((ds != null) && (g != null)) {
            for (int i = 0; i < ds.size(); i++) {

                Long code = new Long(g.toCode(g.toIndex(ds.get(i))));
                //System.out.println(ds.get(i));
                if (hashMapIndex.containsKey(code)) {
                    List<DataPoint> points = hashMapIndex.get(code);
                    points.add(ds.get(i));
                } else {
                    List<DataPoint> points = new ArrayList<>(1);
                    points.add(ds.get(i));
                    hashMapIndex.put(code, points);
                }

            }
        } else {
            throw new Exception("Object Initialization Exception",
                    new Throwable("Atempt to initialize DataSetIndex with dataset or grid null!"));
        }
    }

    public DataSet neighbors(DataPoint dp, double radius) throws Exception {

        List<DataPoint> v = null;
        Set<DataPoint> s = null;


        if (radius > 0) {
            long[] index = this.grid.toIndex(dp);
            //PATCH 2005.05.04-01
            // To compute the value of steps in terms of number of grid points given
            // a desired radius and a vector of sampling rate (tau).
            // Old line:
            //      int  steps = radius; 

            double tau[] = grid.getTau();
            long min_step = (int) Math.ceil(radius / tau[0]);
            int steps = (int) min_step;

            for (int d = 0; d < grid.getDimension(); d++) {
                long num = grid.getNumberOfPoints(d);
                min_step = (int) Math.ceil(radius / tau[d]);

                min_step = Math.min(num, min_step);//avoid falling out of the grid
                if (min_step < steps) {
                    steps = (int) min_step;
                }

            }

            //END PATCH 2005.05.04-01


            //PATCH 2013.02.11.1605
            // Reason: neigbors in the diagonals around a given point are not returned
            // by getEssentialNeighbors()
            //
            // Old line (below):
            // List<long[]> neighbors = this.grid.getEssentialNeighbors(index);


            List<long[]> neighbors = this.grid.getConnectedNeighbors(index);
            //END PATCH 2013.02.11.1605


            v = new ArrayList<>();
            // to ensure no duplicates are added to the list use Set Interface
            s = new HashSet<>();

            //System.out.println(dp.toString());
            for (int n = 0; n < neighbors.size(); n++) {   // for all gridneighbors(index,steps) neighInd check if it contains datapoints 
                Long code = new Long(this.grid.toCode((long[]) neighbors.get(n)));//  get code

                /*if (dp.toString().equalsIgnoreCase("12.0,12.0,12.0")) {
                 if (this.grid.toPoint(neighbors.get(n)).toString().equalsIgnoreCase("12.0,13.0,14.0")) {
                 System.out.println("----> " + this.grid.toPoint(neighbors.get(n)));
                 }
                 }*/

                if (this.hashMapIndex.containsKey(code)) {                          //  check in the hashtable

                    List<DataPoint> points = this.getHashMapIndex().get(code);

                    s.addAll(points);

                }//endif
            }// end for 
        }//endif

        if (s.isEmpty()) {
            return null;
        }
        v.clear();
        v.addAll(s);
        if (v.isEmpty()) {
            return null;
        } else {
            return new DataSet(v);
        }
    }

    public DataPoint nearestDataSetNeighbor(DataPoint dp, double radius) throws Exception {

        DataPoint nearest = null;

        if (dp != null) {
            DataSet neighbors = this.neighbors(dp, radius);
            if (neighbors != null) {
                DataSetIterator dsit = neighbors.createIterator();
                int m = 0; //default: first neighbor is the nearest.
                double minDist = dp.distance(neighbors.get(m));

                for (int n = 0; n < neighbors.size(); n++) { // searches the point with minimum distance with respecto to dp
                    double dist = dp.distance(neighbors.get(n));
                    if (dist < minDist) {
                        m = n;
                        minDist = dist;
                    }
                }
                nearest = neighbors.get(m);
            }
        }
        return nearest;
    }

    public DataSet getDs() {
        return ds;
    }

    public void setDs(DataSet ds) {
        this.ds = ds;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public HashMap<Long, List<DataPoint>> getHashMapIndex() {
        return hashMapIndex;
    }

    public void setHashMapIndex(HashMap<Long, List<DataPoint>> hashMapIndex) {
        this.hashMapIndex = hashMapIndex;
    }
}
//PATCHES: 2005.05.04-01