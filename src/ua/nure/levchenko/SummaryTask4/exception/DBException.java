package ua.nure.levchenko.SummaryTask4.exception;

/**
 * An exception that provides information on a database access error.
 *
 * @author K.Levchenko
 */
public class DBException extends ServiceException {

    private static final long serialVersionUID = 7724449906837780062L;

    public DBException() {
        super();
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

}
