/*
 * DataSetIndex.java
 *
 * Created on September 9, 2004, 7:08 PM
 */
package model.patterndiscovery;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSetIndexUpdate {

    private DataSet ds = null;
    private GridUpdate grid = null;
    private HashMap<Long, List<DataPoint>> hashMapIndex = new HashMap<>(89);

    public DataSetIndexUpdate(DataSet ds, GridUpdate g) throws Exception {
        this.ds = ds;
        this.grid = g;

        if ((ds != null) && (g != null)) {
            for (int i = 0; i < ds.size(); i++) {
                Long code = new Long(g.toCode(g.toIndex(ds.get(i))));
                List<DataPoint> points;
                if (hashMapIndex.containsKey(code)) {
                    points = hashMapIndex.get(code);
                    points.add(ds.get(i));
                } else {
                    points = new ArrayList<>(1);
                    points.add(ds.get(i));
                }
                hashMapIndex.put(code, points);

            }
        } else {
            throw new Exception("Object Initialization Exception",
                    new Throwable("Atempt to initialize DataSetIndex with dataset or grid null!"));
        }
    }

    public int getOccurenceNumber(DataPoint dp) {
        Long code = null;
        try {
            code = new Long(this.grid.toCode(this.grid.toIndex(dp)));
        } catch (Exception ex) {
            Logger.getLogger(DataSetIndexUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }

        return this.hashMapIndex.get(code).size();
    }

    public DataSet neighbors(DataPoint dp, double radius) throws Exception {
        List<DataPoint> v = null;
        Set<DataPoint> s = null;
        if (radius > 0 && dp != null) {

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

            List<long[]> neighbors = this.grid.getConnectedNeighbors(index, radius);

            v = new ArrayList<>();
            s = new HashSet<>();
            for (int n = 0; n < neighbors.size(); n++) {   // for all gridneighbors(index,steps) neighInd check if it contains datapoints 
                Long code = new Long(this.grid.toCode((long[]) neighbors.get(n)));//  get code
                if (this.hashMapIndex.containsKey(code)) {                          //  check in the hashtable
                    List<DataPoint> points = this.getHashMapIndex().get(code);
                    s.addAll(points);

                }//endif
            }// end for 

        }//endif
        if (s == null || s.isEmpty()) {
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

    public GridUpdate getGrid() {
        return grid;
    }

    public void setGrid(GridUpdate grid) {
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