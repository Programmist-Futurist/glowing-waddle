package ua.nure.levchenko.SummaryTask4.web.command.student;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.Status;
import ua.nure.levchenko.SummaryTask4.entity.Topic;
import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * Command to show and filter courses of some student.
 *
 * @author K.Levchenko
 */
public class StudentCoursesCommand extends Command {

    private static final long serialVersionUID = -3071536593627692473L;

    private static final Logger LOG = Logger.getLogger(StudentCoursesCommand.class);
    private static List<Map<String, String>> studentCourses;
    private Map<String, String> user;
    private String courses;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws AppException {
        LOG.debug("Command starts");

        //getting session
        HttpSession session = request.getSession();
        Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
        int userId = Integer.parseInt(user.get(EntityFields.ID));

        // setting request attributes
        request.setAttribute(REQUEST_PATH.getTopics(), READ_SERVICE.getAllTopics(userId));
        request.setAttribute(REQUEST_PATH.getStatuses(), READ_SERVICE.getAllStatuses(userId));
        request.setAttribute(REQUEST_PATH.getTeachers(), READ_SERVICE.getAllTeachers(userId));
        LOG.trace("Attributes updated");

        // initializing parameter list
        Map<String, String> parameterList = new HashMap<>();

        // getting parameter action/button from request
        String action = request.getParameter(REQUEST_PATH.getAction());
        parameterList.put("action", action);
        LOG.trace("Parameter found: action --> " + action);
        String button = request.getParameter(REQUEST_PATH.getButton());
        parameterList.put("button", button);
        LOG.trace("Parameter found: button --> " + button);

        try {
            // getting parameter courses (if not null)
            if (request.getParameter(REQUEST_PATH.getCourses()) != null) {
                courses = request.getParameter(REQUEST_PATH.getCourses());
                parameterList.put("courses", courses);
                LOG.trace("Parameter found: courses --> " + courses);
            }

            //getting needed courses
            if ("myCourses".equals(courses) && !"sort".equals(action)) {
                int studentId = Integer.parseInt(user.get(EntityFields.ID));
                studentCourses = READ_SERVICE.getAllCoursesOfStudent(studentId);
            } else if ("allCourses".equals(courses) && !"sort".equals(action)) {
                studentCourses = READ_SERVICE.getAllCourses(userId);
            }

            // determine enrolled student or not
            int studentId = Integer.parseInt(user.get(EntityFields.ID));
            List<Map<String, String>> coursesOfStudent = READ_SERVICE.getAllCoursesOfStudent(studentId);

            for (Map<String, String> courseFromAll : studentCourses) {
                String courseId = courseFromAll.get(EntityFields.ID);
                courseFromAll.put("enrolled", "false");
                for (Map<String, String> courseFromMy : coursesOfStudent) {
                    if (courseId.equals(courseFromMy.get("id"))) {
                        courseFromAll.replace("enrolled", "true");
                    }
                }
            }


            if (button != null) {
                switch (button) {
                    case "Apply":
                        List<Map<String, String>> coursesToRemove = new ArrayList<>();
                        switch (action) {
                            case "status": {
                                // getting parameter from request
                                String statusId = request.getParameter(REQUEST_PATH.getStatus());
                                parameterList.put("statusId", statusId);
                                LOG.trace("Parameter found: statusId --> " + statusId);

                                int userStatusId;
                                if (Status.NOT_STARTED.getIntKey() == Integer.parseInt(statusId)) {
                                    for (Map<String, String> courseNames : studentCourses) {
                                        userStatusId = Integer.parseInt(courseNames.get(EntityFields.COURSE_STATUS_ID));
                                        if (userStatusId != Status.NOT_STARTED.getIntKey()) {
                                            coursesToRemove.add(courseNames);
                                        }
                                    }
                                } else if (Status.PROCESSING.getIntKey() == Integer.parseInt(statusId)) {
                                    for (Map<String, String> courseNames : studentCourses) {
                                        userStatusId = Integer.parseInt(courseNames.get(EntityFields.COURSE_STATUS_ID));
                                        if (userStatusId != Status.PROCESSING.getIntKey()) {
                                            coursesToRemove.add(courseNames);
                                        }
                                    }
                                } else if (Status.FINISHED.getIntKey() == Integer.parseInt(statusId)) {
                                    for (Map<String, String> courseNames : studentCourses) {
                                        userStatusId = Integer.parseInt(courseNames.get(EntityFields.COURSE_STATUS_ID));
                                        if (userStatusId != Status.FINISHED.getIntKey()) {
                                            coursesToRemove.add(courseNames);
                                        }
                                    }
                                }

                                // deleting unneeded courses
                                for (Map<String, String> courseNames : coursesToRemove) {
                                    studentCourses.remove(courseNames);
                                }
                                break;
                            }
                            case "topic": {
                                // getting parameter from request
                                String topicId = request.getParameter(REQUEST_PATH.getTopic());
                                parameterList.put("topicId", topicId);
                                LOG.trace("Parameter found: topicId --> " + topicId);

                                int userTopicId;
                                if (Topic.PROGRAMMING.getIntKey() == Integer.parseInt(topicId)) {
                                    for (Map<String, String> courseNames : studentCourses) {
                                        userTopicId = Integer.parseInt(courseNames.get(EntityFields.COURSE_TOPIC_ID));
                                        if (userTopicId != Topic.PROGRAMMING.getIntKey()) {
                                            coursesToRemove.add(courseNames);
                                        }
                                    }
                                } else if (Topic.SELF_DEVELOPMENT.getIntKey() == Integer.parseInt(topicId)) {
                                    for (Map<String, String> courseNames : studentCourses) {
                                        userTopicId = Integer.parseInt(courseNames.get(EntityFields.COURSE_TOPIC_ID));
                                        if (userTopicId != Topic.SELF_DEVELOPMENT.getIntKey()) {
                                            coursesToRemove.add(courseNames);
                                        }
                                    }
                                }
                                // deleting unneeded courses
                                for (Map<String, String> courseNames : coursesToRemove) {
                                    studentCourses.remove(courseNames);
                                }
                                break;
                            }
                            case "teacherSort": {
                                // getting parameter from request
                                String teacherId = request.getParameter(REQUEST_PATH.getTeacherId());
                                parameterList.put("teacherId", teacherId);
                                LOG.trace("Parameter found: teacherId --> " + teacherId);

                                for (Map<String, String> courseNames : studentCourses) {
                                    int courseTeacherId
                                            = Integer.parseInt(courseNames.get(EntityFields.COURSE_TEACHER_ID));
                                    if (courseTeacherId != Integer.parseInt(teacherId)) {
                                        coursesToRemove.add(courseNames);
                                    }
                                }
                                for (Map<String, String> courseNames : coursesToRemove) {
                                    studentCourses.remove(courseNames);
                                }
                                break;
                            }
                        }
                        break;
                    case "Sort":
                        // getting parameter from request
                        String sort = request.getParameter(REQUEST_PATH.getSort());
                        LOG.trace("Parameter found: sort --> " + sort);

                        switch (sort) {
                            case "a-z":
                                studentCourses = sortByNameAZ(studentCourses);
                                break;
                            case "z-a":
                                studentCourses = sortByNameZA(studentCourses);
                                break;
                            case "duration":
                                studentCourses = sortByDuration(studentCourses);
                                break;
                            case "amount":
                                studentCourses = sortByAmount(studentCourses);
                                break;
                        }
                        break;
                    case "Show All courses":
                        break;
                }
            }
        } catch (ServiceException e) {
            LOG.error("Cannot process command StudentCourses");
            throw new AppException(resourceBundle.getString("student_courses_command.app_exception"), e);
        }
        // setting outgoing parameters
        request.setAttribute(REQUEST_PATH.getStudentCourses(), studentCourses);

        return Path.PAGE_COURSES;
    }


    /**
     * Sorts courses by name from A to Z
     *
     * @param mapList mapList of courses with their string internationalized parameters
     * @return sorted mapList of needed courses names
     */
    public List<Map<String, String>> sortByNameAZ(List<Map<String, String>> mapList) {
        List<Map<String, String>> result = new ArrayList<>();
        Stream<Map<String, String>> st = mapList.stream();

        //sorting
        st.sorted(Comparator.comparing(o -> o.get(EntityFields.COURSE_NAME)))
                .forEach(result::add);

        return result;
    }


    /**
     * Sorts courses by name from Z to A
     *
     * @param mapList mapList of courses with their string internationalized parameters
     * @return sorted mapList of needed courses names
     */
    public List<Map<String, String>> sortByNameZA(List<Map<String, String>> mapList) {
        List<Map<String, String>> result = new ArrayList<>();
        Stream<Map<String, String>> st = mapList.stream();

        //sorting
        st.sorted((o1, o2) -> o2.get(EntityFields.COURSE_NAME).compareTo(o1.get(EntityFields.COURSE_NAME)))
                .forEach(result::add);

        return result;
    }


    /**
     * Sorts courses by name by their duration,
     * from the least lengthy course to the longest
     *
     * @param mapList mapList of courses with their string internationalized parameters
     * @return sorted mapList of needed courses names
     */
    public List<Map<String, String>> sortByDuration(List<Map<String, String>> mapList) {
        List<Map<String, String>> result = new ArrayList<>();
        Stream<Map<String, String>> st = mapList.stream();

        //class for parsing date
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");

        //sorting
        st.sorted((o1, o2) -> {
            try {
                return new Date(in.parse(o1.get(EntityFields.COURSE_END_DATE)).getTime()
                        - in.parse(o1.get(EntityFields.COURSE_START_DATE)).getTime())
                        .compareTo(new Date(in.parse(o2.get(EntityFields.COURSE_END_DATE)).getTime()
                                - in.parse(o2.get(EntityFields.COURSE_START_DATE)).getTime()));
            } catch (ParseException e) {
                return 0;
            }
        })
                .forEach(result::add);

        return result;
    }


    /**
     * Sorts courses by name by their amount of student,
     * from smallest to largest
     *
     * @param mapList mapList of courses with their string internationalized parameters
     * @return sorted mapList of needed courses names
     */
    public List<Map<String, String>> sortByAmount(List<Map<String, String>> mapList) {
        List<Map<String, String>> result = new ArrayList<>();
        Stream<Map<String, String>> st = mapList.stream();

        // sorting
        st.sorted(Comparator.comparingInt(o -> Integer.parseInt(o.get(EntityFields.COURSE_STUDENT_AMOUNT))))
                .forEach(result::add);

        return result;
    }

}
