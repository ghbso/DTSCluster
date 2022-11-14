/*
 * DataSetIterator.java
 *
 * Created on August 10, 2004, 5:55 PM
 */

package model.patterndiscovery;
import java.util.Enumeration;
/**
 *
 * @author  jcsilva
 */
public class DataSetIterator implements Enumeration{
    private int index;
    private DataSet ds;
    private boolean hasMore=true;
    
    public DataSetIterator(DataSet ds) {
        this.ds=ds;
        this.index=-1;
    }
    
    public DataPoint first() throws IndexOutOfBoundsException
    {
        DataPoint dp = null;
        if( ds!=null )
        {
                dp = ds.get(0);
        }
        else
        {
                throw new IndexOutOfBoundsException();
        }
        return dp;
    }
    
    public DataPoint next() throws IndexOutOfBoundsException
    {
        DataPoint dp = null;
        
        if (this.ds==null){
            this.hasMore=false;
            return null;
        }
        
        if(index< ds.size()-1){
            
            this.index++;
            if( index==ds.size()-1){this.hasMore=false;}
            return ds.get(index);
         } 
        this.hasMore=false;
        return dp;
    }
    
    public boolean isDone() {
        boolean result = false;
        if( ds.getDataPoints().isEmpty() || index == ds.size()-1) // size -1 to avoid OutofBoundException
        {
            result = true;
        }
        return result;
   }
   
   
    @Deprecated
    public DataPoint currentItem() {
        if (this.ds==null){ 
            return null;
        }
        if (index>=0){
            return ds.get(index);
        }
        return null;
    }

    @Deprecated
    public boolean hasMoreElements() {
        return (!isDone());
    }

    public boolean _hasMoreElements() {
        return (this.hasMore);
    }

    public Object nextElement() {
       return next();
    }  
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    private void incIndex(){
        this.index++;
    }
}
