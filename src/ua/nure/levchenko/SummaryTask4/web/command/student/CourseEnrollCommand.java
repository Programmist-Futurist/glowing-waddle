package ua.nure.levchenko.SummaryTask4.web.command.student;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.exception.ValidationException;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.web.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Command to enroll or leave course.
 *
 * @author K.Levchenko
 */
public class CourseEnrollCommand extends Command {
    private static final long serialVersionUID = -945966818886063096L;

    private static final Logger LOG = Logger.getLogger(EditStudentDataCommand.class);


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LOG.debug("Command starts");

        //getting session
        HttpSession session = request.getSession();
        Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
        int userId = Integer.parseInt(user.get(EntityFields.ID));

        // initializing parameter list
        Map<String, String> parameterList = new HashMap<>();

        // getting parameters from request
        int courseId = Integer.parseInt(request.getParameter(REQUEST_PATH.getCourseId()));
        parameterList.put("courseId", String.valueOf(courseId));
        LOG.debug("Parameter required: courseId --> " + courseId);

        String button = request.getParameter(REQUEST_PATH.getButton());
        parameterList.put("button", button);
        LOG.debug("Parameter required: button --> " + button);

        try {
            if ("enroll".equals(button)) {
                if (courseId != 0) {
                    // validate
                    String errorMessage = JOURNAL_VALIDATOR.validateOnEnroll(userId, parameterList);

                    if (errorMessage.isEmpty()) {
                        // enroll student on course
                        CREATE_SERVICE.createJournalEntity(userId, parameterList);
                        request.setAttribute(REQUEST_PATH.getInfoMessage()
                                , resourceBundle.getString("course_enroll_command.success_on_enroll"));

                    } else {
                        LOG.trace(errorMessage);
                        request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                    }
                }
            } else if ("leave".equals(button)) {
                if (courseId != 0) {

                    // validate
                    String errorMessage = JOURNAL_VALIDATOR.validateOnLeave(userId, parameterList);

                    if (errorMessage.isEmpty()) {
                        // delete journal entity
                        DELETE_SERVICE.deleteJournalEntityByCourse(userId, courseId);

                        request.setAttribute(REQUEST_PATH.getInfoMessage()
                                , resourceBundle.getString("course_enroll_command.success_on_leave"));
                        return new StudentCoursesCommand().execute(request, response);
                    } else {
                        request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                    }
                }
            }
        } catch (ServiceException | ValidationException e) {
            LOG.error("Problems with singing in new user. Sorry for problems. Try later..", e);
            throw new AppException(resourceBundle.getString("course_enroll_command.app_exception"), e);
        }
        return new StudentCoursesCommand().execute(request, response);
    }
}
