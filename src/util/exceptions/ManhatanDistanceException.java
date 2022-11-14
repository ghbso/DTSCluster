package util.exceptions;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class ManhatanDistanceException extends Exception{
    private String message = "SubSequences must have the same length in Manhattan distance.";
    
    @Override
    public String getMessage() {
        return this.message;
    }
    
}
