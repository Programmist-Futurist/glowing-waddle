package ua.nure.levchenko.SummaryTask4.exception;

/**
 * An exception that provides information on an validation error.
 *
 * @author K.Levchenko
 */
public class ValidationException extends AppException {

    private static final long serialVersionUID = 7724449906837780062L;

    public ValidationException() {
        super();
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}