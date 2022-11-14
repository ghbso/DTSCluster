package util.exceptions;

/**
 *
 * @author GUSTAVOHENRIQUE2
 */
public class NullTimeSeriesException extends Exception {

    private String message = "Choose a TimeSeries";

    @Override
    public String getMessage() {
        return this.message;
    }
}
