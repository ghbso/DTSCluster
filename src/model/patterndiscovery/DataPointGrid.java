package model.patterndiscovery;

import util.distances.EuclideanDistance;

public class DataPointGrid {

    private int dim;
    private double[] tau;
    private DataPoint origin;

    public DataPointGrid(DataPoint origin, double[] tau) throws Exception {
        //WISH Create especialized Exception


        if (origin.getDimension() != tau.length) {
            throw new Exception("Object Initialization Exception",
                    new Throwable("Tau (sample rate vector) has wrong dimensions"));
        }

        this.origin = origin;
        this.dim = origin.getDimension();
        this.tau = tau;


    }

    public DataPoint toPoint(long[] index) throws Exception {
        return this.getPoint(index);
    }

    public DataPoint getPoint(long[] index) throws Exception {


        //WISH especialize exception
        if (this.dim != index.length) {
            throw new Exception("Object Initialization Exception",
                    new Throwable("Atempt to get point with wrong index dimensions"));
        }


        DataPoint dp = null;
        double[] values = new double[this.dim];

        if (index.length == this.dim) {
            for (int d = 0; d < this.dim; d++) {
                values[d] = this.tau[d] * index[d] + this.origin.getValue(d);
            }
            dp = new DataPoint(values);
        }

        return dp;
    }

    public long[] toIndex(DataPoint dp) throws Exception {
        if (dp == null) {
            return null;
        }
        long[] index = new long[this.dim];

        double[] v = dp.getValues();
        for (int i = 0; i < this.dim; i++) {
            index[i] = Math.round((v[i] / this.tau[i]));
        }
        return index;
    }

    public int getDimension() {
        return dim;
    }

    public double[] getTau() {
        return this.tau;
    }

    public void setTau(double[] tau) {
        this.tau = tau;
    }

    public DataSet getGridPointNeighbors(DataPoint gridPoint, int radius) {
        DataSet aSet = new DataSet(gridPoint.getDimension());
        aSet.addPoint(gridPoint);
        //System.out.println("[DataPointGrid] aSet.addPoint(gridPoint)" + gridPoint);
        double[] values = gridPoint.getValues();
        // for each dimension 
        for (int d = 0; d < values.length; d++) {
            // produce variations of a grid point +inc and -inc
            int dimension = this.getDimension();
            double inc = this.tau[d];
            while (inc <= radius) {
                double[] variationP = new double[values.length];
                double[] variationM = new double[values.length];
                System.arraycopy(values, 0, variationP, 0,dimension);
                System.arraycopy(values, 0, variationM, 0,dimension);
                variationP[d] = values[d] + inc;
                variationM[d] = values[d] - inc;
//
//                DataPoint pP = new DataPoint(variationP, origin.getDistFunc());
//                //System.out.print("[DataPointGrid]pP : " + pP);
//                //System.out.println("[DataPointGrid] aSet.addPoint(gridPoint)" + pP);
//                aSet.addPoint(pP);
//                //System.out.println("[DataPointGrid] aSet = {" + aSet + "}");
//
//                DataPoint pM = new DataPoint(variationM, origin.getDistFunc());
//                //System.out.println("[DataPointGrid] aSet.addPoint(gridPoint)" + pM);
//                //System.out.print("[DataPointGrid]pM : " + pM);
//                aSet.addPoint(pM);
                inc += tau[d];
                //System.out.println("[DataPointGrid] aSet = {" + aSet + "}");
            }
            //until inc > radius
        }
        // end for
        return aSet;
    }
    
}
