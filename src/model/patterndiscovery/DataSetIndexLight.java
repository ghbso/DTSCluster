/*
 * DataSetIndex.java
 *
 * Created on September 9, 2004, 7:08 PM
 */

package model.patterndiscovery;

import java.util.*;
/**
 *
 * @author  jcsilva
 */
public class DataSetIndexLight {
    private DataSet ds=null;
    private DataPointGrid    grid=null;
    private HashMap<DataPoint, List<DataPoint>> hashMapIndex = new HashMap<>(3189);
    
    public DataSetIndexLight(DataSet ds, DataPointGrid g) throws Exception{
        this.ds=ds;
        this.grid=g;

        if ((ds!=null)&& (g!=null)){
            for (int i=0; i< ds.size();i++){
                DataPoint code = ds.get(i);
                
                System.out.print("add hash: "+code.hashCode());

                if (hashMapIndex.containsKey(code)){
                    List<DataPoint> points = hashMapIndex.get(code);
                    points.add(ds.get(i));
                    System.out.println(" old");
                }
                else{
                    List<DataPoint> points = new ArrayList<>(1);
                    points.add(ds.get(i));
                    hashMapIndex.put(code, points);
                    System.out.println(" new ");     
                }
                
            }
        } else {
            throw new Exception ("Object Initialization Exception", 
                                  new Throwable("Atempt to initialize DataSetIndex with dataset or grid null!")
                                ) ;
        }
    }
    
    public DataSet neighbors(DataPoint dp, double radius) throws Exception {

        List<DataPoint> v = null;
        
        
        if (radius>0) {
                  
           double tau[]= grid.getTau();
           long  min_step = (int)Math.ceil(radius/tau[0]); 
           int steps= (int)min_step;
           DataSet neighbors = this.grid.getGridPointNeighbors(dp, steps);

           v = new ArrayList<DataPoint>();
           
           for (int n=0; n<neighbors.size();n++){   // for all gridneighbors(index,steps) neighInd check if it contains datapoints 
                DataPoint code=neighbors.get(n);//  get code
                System.out.println(" get "+ code.hashCode());
                if (this.hashMapIndex.containsKey(code)){                          //  check in the hashtable
                    List<DataPoint> points = this.getHashMapIndex().get(code);
                    v.addAll(points);
                    
                }//endif
           }// end for 
        }//endif
        
        if (v.isEmpty()){   return null;}
        else { return new DataSet(v);}
    }
    
    public DataPoint nearestNeighbor(DataPoint dp, double radius) throws Exception{
        
        DataPoint nearest= null;
        
        if (dp!=null){
            DataSet neighbors = this.neighbors(dp, radius);
            if (neighbors!=null){
                DataSetIterator dsit = neighbors.createIterator();
                int m = 0; //default: first neighbor is the nearest.
                double minDist = dp.distance(neighbors.get(m));

                for (int n = 0 ; n<neighbors.size();n++){ // searches the point with minimum distance with respecto to dp
                    double dist = dp.distance(neighbors.get(n));
                    if (dist<minDist){
                        m=n;
                        minDist=dist;
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
    
    public DataPointGrid getGrid() {
        return grid;
    }
    
    
    public HashMap<DataPoint, List<DataPoint>> getHashMapIndex() {
        return hashMapIndex;
    }
    
}