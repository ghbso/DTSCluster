package model.patterndiscovery;

import java.util.*;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class GridIteratorUpdate {

    private long[] index;
    private GridUpdate g;
    private boolean hasMoreElements = true;

    //Constructor
    public GridIteratorUpdate(GridUpdate g) {
        this.g = g;
        this.index = g.getStartCorner();
    }

    public long[] first() throws IndexOutOfBoundsException {
        if (g != null) {
            return g.getStartCorner();
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    @Deprecated
    public DataPoint next() throws Exception {

        if (!isDone()) // if the index didn't reached the end corner
        {
            this.advanceIndex();

        }
        return this.currentItem();
    }

    public DataPoint _next() throws Exception {

        if (hasMoreElements()) // if the index didn't reached the startcorner again
        {
            this._advanceIndex();

        }
        return this.currentItem();
    }

    public boolean hasMoreElements() {
        return this.hasMoreElements;
    }

    public boolean isDone() {
        long[] supCorner = this.g.getEndCorner();
        // TODO Correct the semantic 
        // to be: if after a advanceIndex we are in the first item then we are done.
        //
        for (int d = 0; d < supCorner.length; d++) {//number of dimensions
            if (this.index[d] != supCorner[d]) {
                return false;
            }
        }
        return true;
    }

    public DataPoint currentItem() throws Exception {
        return this.g.getPoint(this.index);
    }

    private boolean isInside(long index[]) {
        return g.isInside(index);
    }

    public void advanceIndex() {
        // TODO Just avance it, without questioning.      
        if (!isDone()) {
            for (int d = 0; d < this.index.length; d++) {
                this.index[d] = this.index[d] + 1;
                if (this.index[d] <= g.getEndCorner()[d]) { // index doesn't exceeds the corner in the given dimension
                    break;
                } else {
                    this.index[d] = g.getStartCorner()[d];

                }
            }
        }

    }
    public boolean _advanceIndex() {
        // TODO Just avance it, without questioning.      

        long[] before = this.index;
        for (int d = 0; d < this.index.length; d++) {
            this.index[d] = this.index[d] + 1;
            if (this.index[d] <= g.getEndCorner()[d]) { // index doesn't exceeds the corner in the given dimension
                break;
            } else {
                this.index[d] = g.getStartCorner()[d];

            }
        }
        //if after avancing it is pointing to the first corner we are done. No more elements exists.
        if (Arrays.equals(this.index, g.getStartCorner())) {
            this.hasMoreElements = false;
        }
        return this.hasMoreElements;
    }
}
