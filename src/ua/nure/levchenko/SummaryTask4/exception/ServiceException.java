package ua.nure.levchenko.SummaryTask4.exception;

/**
 * An exception that provides information on an service operations error.
 *
 * @author K.Levchenko
 */
public class ServiceException extends AppException {

    private static final long serialVersionUID = 7724449906837780062L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
