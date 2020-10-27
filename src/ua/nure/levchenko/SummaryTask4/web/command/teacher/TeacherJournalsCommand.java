package ua.nure.levchenko.SummaryTask4.web.command.teacher;

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
import java.util.List;
import java.util.Map;

/**
 * Command to manage current teacher journals.
 *
 * @author K.Levchenko
 */
public class TeacherJournalsCommand extends Command {

    private static final long serialVersionUID = -3071536593627692473L;

    private static final Logger LOG = Logger.getLogger(TeacherJournalsCommand.class);

    private static int courseShowId;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws AppException {
        LOG.debug("Command starts");

        String forward = Path.PAGE_JOURNALS_PAGE;

        //getting session
        HttpSession session = request.getSession();
        Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
        int userId = Integer.parseInt(user.get(EntityFields.ID));

        // setting request attributes
        request.setAttribute(REQUEST_PATH.getStudents(), READ_SERVICE.getAllStudents(userId));
        LOG.trace("Attributes updated");

        //initializing parameter list
        Map<String, String> parameterList = new HashMap<>();

        // getting action parameter
        String action = request.getParameter(REQUEST_PATH.getAction());
        parameterList.put("action", action);
        LOG.trace("Parameter found: action --> " + action);

        try {
            // getting teacherCourses of current teacher
            List<Map<String, String>> teacherCourses
                    = READ_SERVICE.getAllCoursesOfTeacher(userId);

            // getting total number of students
            int totalStudentsAmount = 0;
            for (Map<String, String> courseNames : teacherCourses) {
                int studentAmount = Integer.parseInt(courseNames.get(EntityFields.COURSE_STUDENT_AMOUNT));
                totalStudentsAmount += studentAmount;
            }

            request.setAttribute(REQUEST_PATH.getTotalStudentAmount(), totalStudentsAmount);

            //setting request attribute teacherCourses
            request.setAttribute(REQUEST_PATH.getTeacherCourses(), teacherCourses);
            LOG.trace("Found in DB: current teacher's Journals --> " + teacherCourses);

            if (action != null) {

                //getting request parameters needed for further actions
                String journalId = request.getParameter(REQUEST_PATH.getJournalId());
                parameterList.put("journalId", journalId);
                LOG.trace("Parameter found: journalId --> " + journalId);

                String mark = request.getParameter(REQUEST_PATH.getStudentMark());
                parameterList.put("mark", mark);
                LOG.trace("Parameter found: mark --> " + mark);

                switch (action) {
                    case "Delete":
                        if (journalId != null) {
                            DELETE_SERVICE.deleteJournalEntity(Integer.parseInt(journalId));
                            // setting Messages on the page
                            request.setAttribute(REQUEST_PATH.getInfoMessage(),
                                    resourceBundle.getString("teacher_journals_command.success_on_journal_entity_delete"));
                        }
                        break;

                    case "Edit":

                        if (mark == null || mark.isEmpty()) {
                            // setting infoMessage attribute
                            request.setAttribute(REQUEST_PATH.getInfoMessage()
                                    , resourceBundle.getString("teacher_journals_command.nothing_change"));
                        } else {

                            // Update journal
                            UPDATE_SERVICE.updateJournalMark(parameterList);

                            // setting infoMessage attribute
                            request.setAttribute(REQUEST_PATH.getInfoMessage()
                                    , resourceBundle.getString("teacher_journals_command.success_on_mark_update"));
                        }
                        // because we do not need to close opened journal on page
                        break;

                    case "enroll":

                        // getting needed parameters for current action
                        String studentId = request.getParameter(REQUEST_PATH.getStudentId());
                        parameterList.put("studentId", studentId);
                        LOG.trace("Parameter required: studentId --> " + studentId);

                        String courseId = request.getParameter(REQUEST_PATH.getCourseId());
                        parameterList.put("courseId", courseId);
                        LOG.trace("Parameter required: courseId --> " + courseId);

                        // setting new student for the course
                        String errorMessage = JOURNAL_VALIDATOR.validateOnCreate(userId, parameterList);

                        if (errorMessage.isEmpty()) {
                            // creating new journalEntity in DB
                            CREATE_SERVICE.createJournalEntity(Integer.parseInt(studentId), parameterList);

                            // setting Messages on the page
                            request.setAttribute(REQUEST_PATH.getErrorMessage(), null);
                            request.setAttribute(REQUEST_PATH.getInfoMessage()
                                    , resourceBundle.getString("teacher_journals_command.success_on_journal_entity_create"));
                        } else {
                            // setting Messages on the page
                            request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                            request.setAttribute(REQUEST_PATH.getInfoMessage(), null);
                        }
                        break;
                }
            }

            // forming needed journal to display on page
            if (request.getParameter(REQUEST_PATH.getCourseShowId()) != null
                    && !request.getParameter(REQUEST_PATH.getCourseShowId()).isEmpty()) {

                // getting parameter courseShowId from the request
                courseShowId = Integer.parseInt(request.getParameter(REQUEST_PATH.getCourseShowId()));
                parameterList.put("courseShowId", String.valueOf(courseShowId));
                LOG.trace("Parameter found: courseShowId --> " + courseShowId);
            }
            if (courseShowId != 0) {
                //forming journal needed
                List<Map<String, String>> journalToShow
                        = READ_SERVICE.getAllJournalEntitiesByCourse(userId, courseShowId);

                Map<String, String> courseNames
                        = READ_SERVICE.courseToCorrectView(userId, courseShowId);

                // setting attributes journal/course
                request.setAttribute(REQUEST_PATH.getJournal(), journalToShow);
                request.setAttribute(REQUEST_PATH.getCourse(), courseNames);
                LOG.trace("Request attribute has set --> " + journalToShow);

            }
        } catch (ServiceException e) {
            LOG.error(ua.nure.levchenko.SummaryTask4.exception.Messages.ERR_SERVLET_LOG_IN, e);
            throw new AppException(resourceBundle.getString("teacher_journals_command.app_exception"), e);
        }
        return forward;
    }
}
