package ua.nure.levchenko.SummaryTask4.web.command.admin;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.Dictionary;
import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.exception.ValidationException;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Managing course command
 *
 * @author K.Levchenko
 */
public class ManageCoursesCommand extends Command {

    private static final long serialVersionUID = -2571541296953712073L;

    private static final Logger LOG = Logger.getLogger(ManageCoursesCommand.class);

    private static String courseId;
    private static int pageNum;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LOG.debug("Command starts");

        //getting session
        HttpSession session = request.getSession();
        Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
        int userId = Integer.parseInt(user.get(EntityFields.ID));

        // setting request attributes
        request.setAttribute(REQUEST_PATH.getTopics(), READ_SERVICE.getAllTopics(userId));
        request.setAttribute(REQUEST_PATH.getCourses(), READ_SERVICE.getAllCourses(userId));
        request.setAttribute(REQUEST_PATH.getTeachers(), READ_SERVICE.getAllTeachers(userId));
        LOG.trace("Attributes updated");

        // setting num of the page jstl to show
        if (request.getParameter(REQUEST_PATH.getManageCourses()) != null) {
            pageNum = Integer.parseInt(request.getParameter(REQUEST_PATH.getManageCourses()));
            LOG.trace("Parameter required: pageNum --> " + pageNum);
            courseId = null;
        }

        // initializing parameters container
        Map<String, String> parameterList = new LinkedHashMap<>();

        // getting action parameter from the request
        String action = request.getParameter(REQUEST_PATH.getAction());
        LOG.trace("Parameter required: action --> " + action);

        String forward = Path.PAGE_COURSE_CREATE;

        if (action != null) {

            // getting parameter courseId of the course on which operation is made
            if (request.getParameter(REQUEST_PATH.getCourseId()) != null) {
                courseId = request.getParameter(REQUEST_PATH.getCourseId());
                parameterList.put("courseId", courseId);
                LOG.trace("Parameter required: courseId --> " + courseId);
            }

            if ("show".equals(action) || courseId != null) {
                // getting needed view of current course
                if (courseId != null) {
                    Map<String, String> currentCourseNeededView
                            = READ_SERVICE.courseToCorrectView(userId, Integer.parseInt(courseId));

                    // setting attribute with current course with needed view form
                    request.setAttribute(REQUEST_PATH.getCourse(), currentCourseNeededView);
                    LOG.trace("Attribute set: currentCourseNeededView --> " + currentCourseNeededView);
                }
            }

            // getting needed parameters for the following actions
            String courseTeacherId = request.getParameter(REQUEST_PATH.getCourseTeacher());
            parameterList.put("courseTeacherId", courseTeacherId);
            LOG.trace("Parameter required: courseTeacherId --> " + courseTeacherId);

            String courseTopicId = request.getParameter(REQUEST_PATH.getCourseTopic());
            parameterList.put("courseTopicId", courseTopicId);
            LOG.trace("Parameter required: courseTopicId --> " + courseTopicId);

            String courseName = request.getParameter(REQUEST_PATH.getCourseName());
            parameterList.put("courseName", courseName);
            LOG.trace("Parameter required: courseName --> " + courseName);

            String courseNameRus = request.getParameter(REQUEST_PATH.getCourseNameRus());
            parameterList.put("courseNameRus", courseNameRus);
            LOG.trace("Parameter required: courseNameRus --> " + courseNameRus);

            String courseDescription = request.getParameter(REQUEST_PATH.getDescription());
            parameterList.put("courseDescription", courseDescription);
            LOG.trace("Parameter required: courseDescription --> " + courseDescription);

            String courseDescriptionRus = request.getParameter(REQUEST_PATH.getDescriptionRus());
            parameterList.put("courseDescriptionRus", courseDescriptionRus);
            LOG.trace("Parameter required: courseDescriptionRus --> " + courseDescriptionRus);

            String courseStartDate = request.getParameter(REQUEST_PATH.getStartDate());
            parameterList.put("courseStartDate", courseStartDate);
            LOG.trace("Parameter required: courseStartDate --> " + courseStartDate);

            String courseEndDate = request.getParameter(REQUEST_PATH.getEndDate());
            parameterList.put("courseEndDate", courseEndDate);
            LOG.trace("Parameter required: courseEndDate --> " + courseEndDate);
            try {
                String errorMessage;
                switch (action) {
                    case "create":
                        // validating input parameters
                        errorMessage = COURSE_VALIDATOR.validateOnCreate(userId, parameterList);

                        if (errorMessage.isEmpty()) {
                            LOG.debug("Starts to creating new course");

                            // forming dictionary list
                            List<Dictionary> dictionaries = new ArrayList<>();
                            dictionaries.add(new Dictionary(courseName, courseNameRus));
                            dictionaries.add(new Dictionary(courseDescription, courseDescriptionRus));

                            // adding new dictionary words in DB
                            CREATE_SERVICE.createDictionaries(dictionaries);

                            // adding new course to DB
                            CREATE_SERVICE.createCourse(parameterList);

                            // setting message to the UI
                            request.setAttribute(REQUEST_PATH.getInfoMessage()
                                    , resourceBundle.getString("course_manage_command.success_on_create"));
                        } else {
                            request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                        }
                        pageNum = 1;
                        break;
                    case "update":
                        //validate
                        errorMessage = COURSE_VALIDATOR.validateOnUpdate(userId, parameterList);

                        if (errorMessage.isEmpty()) {
                            //updating course
                            UPDATE_SERVICE.editCourse(parameterList);

                            request.setAttribute(REQUEST_PATH.getInfoMessage()
                                    , resourceBundle.getString("course_manage_command.success_on_update"));
                        } else {
                            request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                        }
                        pageNum = 2;
                        break;
                    case "delete":
                        if (courseId != null) {
                            // validating input parameters
                            errorMessage = COURSE_VALIDATOR.validateOnDelete(userId, parameterList);
                            if (errorMessage.isEmpty()) {
                                // deleting course
                                DELETE_SERVICE.deleteCourse(Integer.parseInt(courseId));

                                request.setAttribute(REQUEST_PATH.getInfoMessage()
                                        , resourceBundle.getString("course_manage_command.success_on_delete"));
                            } else {
                                request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                            }
                        }
                        pageNum = 3;
                        break;
                }
            } catch (ServiceException | ValidationException e) {
                LOG.error("Problems with creating new course. Sorry for problems. Try later..", e);
                throw new AppException(resourceBundle.getString("course_manage_command.app_exception"), e);
            }

        }
        // setting attribute for defining jstl page
        request.setAttribute(REQUEST_PATH.getPageNum(), pageNum);
        LOG.trace("Go to page # --> " + pageNum);

        return forward;
    }

}
