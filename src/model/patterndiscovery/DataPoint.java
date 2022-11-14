package model.patterndiscovery;

import java.io.Serializable;
import java.util.Arrays;
import util.distances.Distance;
import util.distances.DistanceFunction;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class DataPoint implements Serializable {

    private int dim;
    private double[] values;
    private Distance distFunc;

    public DataPoint(double[] values) {
        this.values = values;
        this.dim = values.length;
    }

    public DataPoint(Double[] values) {
        this.values =  new double[values.length];
        for(int i = 0; i < values.length; i++){
            this.values[i] = values[i];
        }
        this.dim = values.length;
    }

    public DataPoint(double[] values, Distance distFunc) {
        this.values = values;
        this.dim = values.length;
//        this.distFunc = distFunc;

    }

    public DataPoint(long[] values, DistanceFunction distFunc) {
        this.values = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = (double) values[i];
        }
        this.dim = values.length;
//        this.distFunc = distFunc;

    }

    public DataPoint(Double[] values, DistanceFunction distFunc) {

        this.values = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i].doubleValue();
        }
        this.dim = values.length;
//        this.distFunc = distFunc;

    }

    public DataPoint(Long[] values, DistanceFunction distFunc) {

        this.values = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            this.values[i] = values[i].doubleValue();
        }
        this.dim = values.length;
//        this.distFunc = distFunc;

    }

    public int getDimension() {
        return dim;
    }

    public double getValue(int d) {
        if (values != null) {
            return values[d];
        } else {
            return 0;
        }
    }

    public double distance(DataPoint dp) throws Exception {
        //TODO Especialize exception
        if (dp.getDimension() != this.dim) {
            throw new Exception("Number of parameters exception", new Throwable("Atempt to get distance with data points of different dimensions"));
        }
        if (this.distFunc == null) {
            throw new Exception("No distance function assigned", new Throwable("Atempt to get distance without distance function being assigned to points"));
        }

        return this.distFunc.calcDistance(this.values, dp.getValues());
    }

    public double[] getValues() {
        return this.values;
    }

    public boolean equals(DataPoint other) {
        for (int i = 0; i < this.values.length; i++) {
            if (this.values[i] != other.values[i]) {
                //System.out.println(this.values[i]+"!="+other.values[i]);
                return false;
            }
        }
        return true;
        //return (java.util.Arrays.equals(this.values, other.values));
    }

    public Distance getDistFunc() {
        return distFunc;
    }

    public void setDistFunc(DistanceFunction distFunc) {
        this.distFunc = distFunc;
    }

    @Override
    public String toString() {
        String s = "" + values[0];
        for (int d = 1; d < this.dim; d++) {
            s = s + "," + this.values[d];
        }
        //s += "\n";
        return s;
    }

    @Override
    public int hashCode() {
        int code = 0; // exception condition

        double temp = 0;
        for (int i = 0; i < this.dim; i++) {
            temp += Math.round(this.getValue(i)) * Math.pow(10, i);
        }
        code = (int) Math.round(temp);
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataPoint other = (DataPoint) obj;
        if (this.dim != other.dim) {
            return false;
        }
        if (!Arrays.equals(this.values, other.values)) {
            return false;
        }
        return true;
    }
}
