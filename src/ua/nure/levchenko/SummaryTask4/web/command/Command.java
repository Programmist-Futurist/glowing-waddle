package ua.nure.levchenko.SummaryTask4.web.command;

import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.internationalization.SessionContainer;
import ua.nure.levchenko.SummaryTask4.services.CreateService;
import ua.nure.levchenko.SummaryTask4.services.DeleteService;
import ua.nure.levchenko.SummaryTask4.services.ReadService;
import ua.nure.levchenko.SummaryTask4.services.UpdateService;
import ua.nure.levchenko.SummaryTask4.validators.*;
import ua.nure.levchenko.SummaryTask4.web.path.ContextPath;
import ua.nure.levchenko.SummaryTask4.web.path.RequestPath;
import ua.nure.levchenko.SummaryTask4.web.path.SessionPath;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Main interface for the Command pattern implementation.
 *
 * @author K.Levchenko
 */
public abstract class Command implements Serializable {

    protected static final SessionContainer SESSION_CONTAINER;
    protected static final ContextPath CONTEXT_PATH;
    protected static final SessionPath SESSION_PATH;
    protected static final RequestPath REQUEST_PATH;
    protected static final CourseValidator COURSE_VALIDATOR;
    protected static final TeacherValidator TEACHER_VALIDATOR;
    protected static final StudentValidator STUDENT_VALIDATOR;
    protected static final JournalValidator JOURNAL_VALIDATOR;
    protected static final EntryValidator ENTRY_VALIDATOR;
    protected static final CreateService CREATE_SERVICE;
    protected static final UpdateService UPDATE_SERVICE;
    protected static final DeleteService DELETE_SERVICE;
    protected static final ReadService READ_SERVICE;
    private static final long serialVersionUID = 8879403039606311780L;
    protected static ResourceBundle resourceBundle;

    static {
        SESSION_CONTAINER = SessionContainer.getInstance();
        Locale.setDefault(Locale.ENGLISH);
        resourceBundle = ResourceBundle.getBundle("resources", Locale.ENGLISH);

        CONTEXT_PATH = new ContextPath();
        SESSION_PATH = new SessionPath();
        REQUEST_PATH = new RequestPath();

        COURSE_VALIDATOR = CourseValidator.getInstance();
        TEACHER_VALIDATOR = TeacherValidator.getInstance();
        STUDENT_VALIDATOR = StudentValidator.getInstance();
        JOURNAL_VALIDATOR = JournalValidator.getInstance();
        ENTRY_VALIDATOR = EntryValidator.getInstance();

        CREATE_SERVICE = CreateService.getInstance();
        UPDATE_SERVICE = UpdateService.getInstance();
        DELETE_SERVICE = DeleteService.getInstance();
        READ_SERVICE = ReadService.getInstance();
    }


    /**
     * Execution method for command.
     *
     * @return Address to go once the command is executed.
     */
    public abstract String execute(HttpServletRequest request,
                                   HttpServletResponse response)
            throws IOException, ServletException, AppException;

    @Override
    public final String toString() {
        return getClass().getSimpleName();
    }
}