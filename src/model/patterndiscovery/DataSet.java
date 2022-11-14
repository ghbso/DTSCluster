package model.patterndiscovery;

import java.util.*;

public class DataSet {

    private List<DataPoint> dataPoints;
    private int dim;
    private String headers[];

    public DataSet(int dim) {
        this.dim = dim;
        this.dataPoints = new ArrayList<>();
    }

    public DataSet(List<DataPoint> dataPoints) throws Exception {
        if (dataPoints == null) {
            throw new Exception("Object Initialization Exception",
                    new Throwable("Atempt to initialize DataSet without data points!"));
        }
        this.dataPoints = dataPoints;
        this.dim = 0;
        if ((dataPoints != null) && (dataPoints.size() > 0)) {
            this.dim = ((DataPoint) dataPoints.get(0)).getDimension();
        }
        this.headers = null;
    }

    public DataSet(List<DataPoint> dataPoints, String[] headers) {
        this.dataPoints = dataPoints;
        if (dataPoints != null) {
            this.dim = ((DataPoint) dataPoints.get(0)).getDimension();
        }
        this.headers = headers;
    }

    public int getDim() {
        return dim;
    }

    public String[] getHeaders() {
        return this.headers;
    }

    public void setHeaders(String[] headers) {
        this.headers = headers;
    }

    public boolean equals(DataSet other) {
        return this.dataPoints.equals(other.dataPoints);
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public DataPoint get(int i) {
        return (DataPoint) this.dataPoints.get(i);
    }

    public int size() {
        return this.dataPoints.size();
    }

    public DataSetIterator createIterator() {
        if (this.dim > 0) {
            return new DataSetIterator(this);
        } else {
            return null;
        }

    }

    public DataSetIndex createIndex(Grid g) throws Exception {
        if (g != null) {
            return new DataSetIndex(this, g);
        }
        return null;
    }

    public DataSetIndexLight createIndexLight(DataPointGrid g) throws Exception {
        if (g != null) {
            return new DataSetIndexLight(this, g);
        }
        return null;
    }

    public void union(DataSet anotherDataSet) {

        //[removed because it does not fulfill the post condition]
        //      this.dataPoints.addAll(anotherDataSet.getDataPoints());

        for (DataSetIterator it = anotherDataSet.createIterator(); it._hasMoreElements();) {
            DataPoint next = it.next();
            this.dataPoints.add(next);

            /*if (!this.contains(next))// it's a new point
            
             {
             this.dataPoints.add(next);
             }*/

        }
        //post condition this.dataPoints has no duplicates

    }

    public void unionNoDuplicate(DataSet anotherDataSet) {

        //[removed because it does not fulfill the post condition]
        //      this.dataPoints.addAll(anotherDataSet.getDataPoints());

        for (DataSetIterator it = anotherDataSet.createIterator(); it._hasMoreElements();) {
            DataPoint next = it.next();
            
            if (!this.contains(next) && next!=null)// it's a new point
            {
                
                this.dataPoints.add(next);
            }

        }
        //post condition this.dataPoints has no duplicates

    }

    @Override
    public String toString() {
        String s = "";
        for (Iterator<DataPoint> it = this.dataPoints.iterator(); it.hasNext();) {
            DataPoint p = it.next();
            s += p.toString() + "\n";
        }

        return s;
    }

    public void addPoint(DataPoint p) {
        if (!this.dataPoints.contains(p)) {
            this.dataPoints.add(p);
            //System.out.println("[DataSet] added "+p);
        } else {
            //System.out.println("[DataSet] Duplicate "+p+" not included");
        }
    }

    public boolean contains(DataPoint p) {
        for (DataPoint d : this.dataPoints) {
            if (d.equals(p)) {
                return true;
            }
        }
        return false;
    }
}
