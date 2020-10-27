package ua.nure.levchenko.SummaryTask4.web.command.admin;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Setting course for teacher command
 *
 * @author K.Levchenko
 */
public class CourseForTeacherSetCommand extends Command {

    private static final long serialVersionUID = -2571541296953712073L;

    private static final Logger LOG = Logger.getLogger(CourseForTeacherSetCommand.class);
    private static final int pageNum = 2;
    private static String teacherId;
    private static String courseId;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LOG.debug("Command starts");

        //getting session
        HttpSession session = request.getSession();
        Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
        int userId = Integer.parseInt(user.get(EntityFields.ID));


        // setting request attributes
        request.setAttribute(REQUEST_PATH.getCourses(), READ_SERVICE.getAllCourses(userId));
        request.setAttribute(REQUEST_PATH.getTeachers(), READ_SERVICE.getAllTeachers(userId));
        LOG.trace("Attributes updated");


        // initializing parameter list
        Map<String, String> parameterList = new HashMap<>();

        // getting action parameter
        String action = request.getParameter(REQUEST_PATH.getAction());
        parameterList.put("action", action);
        LOG.trace("Parameter required: action --> " + action);
        try {
            if (action != null) {
                parameterList.put("teacherId", teacherId);
                parameterList.put("courseId", courseId);
                switch (action) {
                    case "show":
                        // getting needed parameters for further action
                        if (request.getParameter(REQUEST_PATH.getTeacherId()) != null) {
                            teacherId = request.getParameter(REQUEST_PATH.getTeacherId());
                            LOG.trace("Parameter required: teacherId --> " + teacherId);

                        }

                        if (request.getParameter(REQUEST_PATH.getCourseId()) != null) {
                            courseId = request.getParameter(REQUEST_PATH.getCourseId());
                            LOG.trace("Parameter required: courseId --> " + courseId);
                        }
                        break;
                    case "connect":
                        // validation
                        String errorMessage = COURSE_VALIDATOR.validateOnConnect(parameterList);

                        // setting new teacher for the course
                        if (errorMessage.isEmpty()) {
                            //updating
                            UPDATE_SERVICE.teacherCourseUpdate(parameterList);

                            request.setAttribute(REQUEST_PATH.getInfoMessage(), resourceBundle.getString("course_for_teacher_set_command.success"));
                        } else {
                            LOG.trace(errorMessage);
                            request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                            break;
                        }
                        break;
                }

                if (teacherId != null) {

                    // request attribute preparing "teacher"
                    Map<String, String> teacherToShow
                            = READ_SERVICE.teacherToCorrectView(Integer.parseInt(teacherId));

                    // request attribute setting "teacher"
                    request.setAttribute(REQUEST_PATH.getChosenTeacher(), teacherToShow);
                    LOG.trace("Attribute has set (request): teacher --> " + teacherToShow);

                    // setting request attribute "teacherId"
                    request.setAttribute(REQUEST_PATH.getTeacherId(), teacherId);
                    LOG.trace("Attribute has set (request): teacherId --> " + teacherId);

                }
                if (courseId != null) {
                    // request attribute preparing "teacher"
                    Map<String, String> courseToShow
                            = READ_SERVICE.courseToCorrectView(userId, Integer.parseInt(courseId));

                    // request attribute setting "course"
                    request.setAttribute(REQUEST_PATH.getChosenCourse(), courseToShow);
                    LOG.trace("Attribute set: course --> " + courseToShow);

                    // setting request attribute "courseId"
                    request.setAttribute(REQUEST_PATH.getCourseId(), courseId);
                    LOG.trace("Attribute has set (request): courseId --> " + courseId);

                }
            } else {
                teacherId = null;
                courseId = null;
            }
        } catch (ServiceException e) {
            LOG.error("Cannot set teacher for course");
            throw new AppException(resourceBundle.getString("course_for_teacher_set_command.app_exception"), e);
        }

        // setting request attribute for using jspf needed page
        request.setAttribute(REQUEST_PATH.getPageNum(), pageNum);
        return Path.PAGE_TEACHER_MANAGE;
    }
}
