package util.exceptions;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class WNegativeException extends Exception{

    private String message = "W must not be negative";

    @Override
    public String getMessage() {
        return this.message;
    }
}
