package util.exceptions;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class WLargerThanTimeSeriesSizeException extends Exception {

    private String message = "W must be heigher or equal than S";
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
    
    
}
