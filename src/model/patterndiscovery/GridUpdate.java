package model.patterndiscovery;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.distances.EuclideanDistance;

public class GridUpdate {

    int dim;
    double[] tau;
    private long[][] corners;
    private DataPoint origin;
    private double factors[] = null;

    public GridUpdate(DataPoint origin, long[][] corners, double[] tau) throws Exception {
        //WISH Create especialized Exception

        if (origin.getDimension() != corners[0].length) {
            throw new Exception("Object Initialization Exception",
                    new Throwable("Start corner has wrong dimensions"));
        }
        if (origin.getDimension() != corners[1].length) {
            throw new Exception("Object Initialization Exception",
                    new Throwable("End corner has wrong dimensions"));
        }
        if (origin.getDimension() != tau.length) {
            throw new Exception("Object Initialization Exception",
                    new Throwable("Tau (sample rate vector) has wrong dimensions"));
        }

        this.origin = origin;
        this.dim = corners[1].length;
        this.tau = tau;
        this.corners = corners;

        for (int d = 0; d < corners[0].length; d++) {
            if (corners[0][d] > corners[1][d]) {
                throw new Exception("Object Initialization Exception",
                        new Throwable("Start Corner " + corners[0][d] + " >  exceeds End Corner " + corners[1][d] + "in at least one dimension"));
            }
        }
        factors = new double[this.dim];
        for (int d = 0; d < this.dim; d++) {
            factors[d] = 1;

            for (int j = 0; j < d; j++) {
                factors[d] *= (Math.abs(this.getEndCorner()[j] - this.getStartCorner()[j]) + 1);
            }
        }
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
            dp = new DataPoint(values, new EuclideanDistance());
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

    public long toCode(DataPoint dp) throws Exception {
        if (dp == null) {
            throw new Exception("toCode needs a data point!");
        }
        return this.toCode(this.toIndex(dp));

    }

    public long toCode(long[] index) throws Exception {

        long code = -1; // exception condition
        if (this.isInside(index)) {
            code = 0;
            for (int d = 0; d < this.dim; d++) {
                code += (long) (Math.abs(index[d] - this.getStartCorner()[d]) * this.factors[d]);
            }
        }

        return code;

    }

    public long[] toIndex(long code) {
        long[] index = null;
        if ((code < 0) || (code > this.getNumberOfPoints())) { // validity test
            return index; // return null
        }
        index = new long[this.dim];
        double prod = 1;
        for (int i = 0; i < this.dim - 1; i++) {
            prod *= Math.abs(this.getNumberOfPoints(i));
        }

        for (int d = this.dim - 1; d > 0; d--) {
            index[d] = (long) Math.floor(code / prod);//index(dimension)= floor(code/prod);
            code = (long) (code % prod); // remainder or MOD of code by prod
            prod = prod / Math.abs(this.getNumberOfPoints(d));
        }

        // firt dimension receives simply the remaining code
        index[0] = code;
        long[] offSet = this.getStartCorner();
        for (int d = 0; d < this.dim; d++) {
            index[d] += offSet[d];
        }
        return index;


    }

    /*
     * function index = _code2index(diminf,dimsup,code) % Input % diminf:
     * inferior corner % dinfsup: superior corner % code: number representing
     * the point % Returns % index
     *
     * index(1)=code; index=index' + diminf; end
     *
     */
    public boolean isInside(long index[]) {
        long[] startCorner = this.getStartCorner();
        long[] endCorner = this.getEndCorner();


        //index must be greater or equal to the initial corner
        for (int d = 0; d < this.dim; d++) {//number of dimensions
            if (index[d] < startCorner[d]) {
                return false;
            }
        }

        //moreover index must be less or equal to the end corner
        for (int d = 0; d < this.dim; d++) {//number of dimensions
            if (index[d] > endCorner[d]) {
                return false;
            }
        }
        return true;
    }

    public GridIteratorUpdate createIterator() {
        return new GridIteratorUpdate(this);
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

    public long[][] getCorners() {
        long[][] c = new long[2][this.dim];

        //copy corners to avoid passing ref to corners
        for (int i = 0; i < this.dim; i++) {
            c[0][i] = corners[0][i];
            c[1][i] = corners[1][i];
        }

        return c;
    }

    public long getNumberOfPoints() {
        long number = 1;
        for (int d = 0; d < this.dim; d++) {
            number *= this.getNumberOfPoints(d);
        }
        return number;
    }

    public long getNumberOfPoints(int d) {
        long numPoints = 0;
        if (d < this.dim) {
            numPoints = 1 + Math.abs(this.getStartCorner()[d] - this.getEndCorner()[d]); // Do not use tau, because grid is represents a transformation to Z.
        }
        return numPoints;
    }

    public long[] getEndCorner() {
        long[] c = new long[this.dim];

        System.arraycopy(corners[1], 0, c, 0, this.dim);

        return c;
    }

    public long[] getStartCorner() {
        long[] c = new long[this.dim];

        System.arraycopy(corners[0], 0, c, 0, this.dim);

        return c;
    }

    public List<long[]> getNeighbors(long[] index, int radius) {
        // BUG: this method just consider the neighbors aligned with each dimension 
        // and forget about the neighbors in diagonals and all other points in 
        // between. This is parcularly bad when we need do consider all neighbors
        // of a given point.

        List<long[]> neighbors = new ArrayList<long[]>(1);
        neighbors.add(index);


        if ((this.isInside(index)) && (radius > 0)) {
            for (int d = 0; d < this.dim; d++) {
                int size = neighbors.size();
                for (int i = 0; i < size; i++) {

                    for (int s = 0; s < radius; s++) {
                        long[][] tempPlus = new long[radius][this.dim];
                        long[][] tempMinus = new long[radius][this.dim];

                        System.arraycopy((long[]) neighbors.get(i), 0, tempPlus[s], 0, this.dim);
                        System.arraycopy((long[]) neighbors.get(i), 0, tempMinus[s], 0, this.dim);
                        tempPlus[s][d] += (s + 1);
                        tempMinus[s][d] -= (s + 1);
                        if ((tempPlus[s][d] <= this.getEndCorner()[d]) && (tempPlus[s][d] >= this.getStartCorner()[d])) {
                            neighbors.add(tempPlus[s]);
                        }
                        if ((tempMinus[s][d] <= this.getEndCorner()[d]) && (tempMinus[s][d] >= this.getStartCorner()[d])) {
                            neighbors.add(tempMinus[s]);
                        }
                    }
                }
            }

        }
        return neighbors;
    }

    public List<long[]> generateSequences(int pos, long[] index) {
        List<long[]> neighbors = new LinkedList<>();

        if (pos == (dim - 1)) {
            for (int i = -1; i < 2; i++) {
                long combination[] = new long[this.dim];
                System.arraycopy(index, 0, combination, 0, this.dim);
                combination[pos] += i;
                neighbors.add(combination);
            }
        } else {
            int startPosition = neighbors.size();
          
            List<long[]> createdCombinations = this.generateSequences((pos + 1), index);
            neighbors.addAll(createdCombinations);
            createdCombinations = null;
            
            int lastPosition = neighbors.size();
            
            for (int i = -1; i <= 1; i += 2) {

                for(int p= startPosition; p < lastPosition; p++){
                    long[] combination = neighbors.get(p);
                    long newCombination[] = new long[this.dim];
                    System.arraycopy(combination, 0, newCombination, 0, this.dim);
                    newCombination[pos] += i;
                    if (this.isInside(newCombination)) {
                        neighbors.add(newCombination);

                    }
                }
            }


        }
        return neighbors;
    }

    public List<long[]> getEssentialNeighbors(long[] index) {
        List<long[]> neighbors = this.generateSequences(0, index);
        return neighbors;


    }

    public List<long[]> getConnectedNeighbors(long[] index, double radius) {
        List<long[]> essentialNeighbors = new ArrayList<long[]>(1);
        Set<Long> codeSet = new HashSet<>();
        List<long[]> connectedNeighbors = new ArrayList<long[]>(1);
        essentialNeighbors.add(index);
        try {
            codeSet.add(this.toCode(index));
        } catch (Exception ex) {
            Logger.getLogger(GridUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }

        // find essential neighbors, but it is not enougth since it ignores the 
        // corners
        essentialNeighbors = this.getEssentialNeighbors(index);
        codeSet = this.cleanUpRedundancies(essentialNeighbors, codeSet);

        
        // connectedNeighbors = essentialNeighbors;
        // add 2*dim essential neighbors of the essential neighbors, to be sure that
        // all connected neighbors are found. Notice that 2*dim extra points are 
        // added, although we dont care.  


        /*for (int i = 1; i < radius; i++) {
            List<long[]> neighEssNeigh = null;

            for (Iterator<long[]> it = essentialNeighbors.iterator(); it.hasNext();) {
                long[] essNeigh = it.next();

                neighEssNeigh = this.getEssentialNeighbors(essNeigh);
                codeSet = this.cleanUpRedundancies(neighEssNeigh, codeSet);
            }
            essentialNeighbors = this.getIndexList(codeSet);
        }*/

        for (Iterator<Long> it = codeSet.iterator(); it.hasNext();) {
            long c = it.next();
            try {
                //System.out.println(">>>>> puting "+ c +" "+ this.toPoint(this.toIndex(c)));
                connectedNeighbors.add(this.toIndex(c));
            } catch (Exception ex) {
                Logger.getLogger(GridUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        return connectedNeighbors;

    }

    private List<long[]> getIndexList(Set<Long> codeSet) {
        List<long[]> subSequences = new ArrayList<>();
        for (Long code : codeSet) {
            subSequences.add(this.toIndex(code));
        }
        return subSequences;
    }

    private Set<Long> cleanUpRedundancies(List<long[]> neighbors, Set<Long> codeSet) {
        for (Iterator<long[]> it = neighbors.iterator(); it.hasNext();) {
            long[] n = it.next();
            try {
                codeSet.add(this.toCode(n));
            } catch (Exception ex) {
                Logger.getLogger(GridUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return codeSet;
    }
}
