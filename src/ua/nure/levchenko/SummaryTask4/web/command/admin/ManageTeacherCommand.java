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
 * Managing teacher command
 *
 * @author K.Levchenko
 */
public class ManageTeacherCommand extends Command {

    private static final long serialVersionUID = -2571541296953712073L;

    private static final Logger LOG = Logger.getLogger(ManageTeacherCommand.class);

    private static final int pageNum = 1;


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LOG.debug("Command starts");

        String forward = Path.PAGE_TEACHER_MANAGE;

        //getting session
        HttpSession session = request.getSession();
        Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
        int userId = Integer.parseInt(user.get(EntityFields.ID));

        // setting request attributes
        request.setAttribute(REQUEST_PATH.getCourses(), READ_SERVICE.getAllCourses(userId));
        request.setAttribute(REQUEST_PATH.getRoles(), READ_SERVICE.getAllRoles(userId));
        request.setAttribute(REQUEST_PATH.getTeachers(), READ_SERVICE.getAllTeachers(userId));
        LOG.trace("Attributes updated");

        // initialize parameters container
        Map<String, String> parameterList = new LinkedHashMap<>();

        // getting action parameter from the request
        String action = request.getParameter(REQUEST_PATH.getAction());
        parameterList.put("action", action);
        LOG.trace("Parameter required: action --> " + action);

        if ("signUp".equals(action)) {
            // getting needed parameters for doing current action
            String login = request.getParameter(REQUEST_PATH.getLogin());
            parameterList.put("login", login);
            LOG.trace("Parameter required: login --> " + login);

            String password = request.getParameter(REQUEST_PATH.getPassword());
            parameterList.put("password", password);
            LOG.trace("Parameter required: password --> ***");

            String firstName = request.getParameter(REQUEST_PATH.getFirstName());
            parameterList.put("firstName", firstName);
            LOG.trace("Parameter required: firstName --> " + firstName);

            String firstNameRus = request.getParameter(REQUEST_PATH.getFirstNameRus());
            parameterList.put("firstNameRus", firstNameRus);
            LOG.trace("Parameter required: firstNameRus --> " + firstNameRus);

            String lastName = request.getParameter(REQUEST_PATH.getLastName());
            parameterList.put("lastName", lastName);
            LOG.trace("Parameter required: lastName --> " + lastName);

            String lastNameRus = request.getParameter(REQUEST_PATH.getLastNameRus());
            parameterList.put("lastNameRus", lastNameRus);
            LOG.trace("Parameter required: lastNameRus --> " + lastNameRus);

            String patronymic = request.getParameter(REQUEST_PATH.getPatronymic());
            parameterList.put("patronymic", patronymic);
            LOG.trace("Parameter required: patronymic --> " + patronymic);

            String patronymicRus = request.getParameter(REQUEST_PATH.getPatronymicRus());
            parameterList.put("patronymicRus", patronymicRus);
            LOG.trace("Parameter required: patronymicRus --> " + patronymicRus);

            String email = request.getParameter(REQUEST_PATH.getEmail());
            parameterList.put("email", email);
            LOG.trace("Parameter required: email --> " + email);

            String phone = request.getParameter(REQUEST_PATH.getPhone());
            parameterList.put("phone", phone);
            LOG.trace("Parameter required: phone --> " + phone);

            String experience = request.getParameter(REQUEST_PATH.getExperience());
            parameterList.put("experience", experience);
            LOG.trace("Parameter required: experience --> " + experience);

            String experienceRus = request.getParameter(REQUEST_PATH.getExperienceRus());
            parameterList.put("experienceRus", experienceRus);
            LOG.trace("Parameter required: experienceRus --> " + experienceRus);

            String roleId = request.getParameter(REQUEST_PATH.getRoleId());
            parameterList.put("roleId", roleId);
            LOG.trace("Parameter required: roleId --> " + roleId);

            String courseId = request.getParameter(REQUEST_PATH.getCourseId());
            parameterList.put("courseId", courseId);
            LOG.trace("Parameter required: courseId --> " + courseId);


            try {
                // validating input parameters
                String errorMessage = TEACHER_VALIDATOR.validateOnCreate(userId, parameterList);

                if (errorMessage.isEmpty()) {

                    // forming dictionary list
                    List<Dictionary> dictionaries = new ArrayList<>();
                    dictionaries.add(new Dictionary(firstName, firstNameRus));
                    dictionaries.add(new Dictionary(lastName, lastNameRus));
                    dictionaries.add(new Dictionary(patronymic, patronymicRus));
                    dictionaries.add(new Dictionary(experience, experienceRus));

                    // adding new dictionary words in DB
                    CREATE_SERVICE.createDictionaries(dictionaries);

                    // adding new teacher in DB
                    CREATE_SERVICE.createTeacher(parameterList);

                    request.setAttribute(REQUEST_PATH.getInfoMessage(), resourceBundle.getString("teacher_manage_command.success_on_create"));
                } else {
                    request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                }
            } catch (ServiceException | ValidationException e) {
                LOG.error("Problems with singing in new user. Sorry for problems. Try later..", e);
                throw new AppException(resourceBundle.getString("teacher_manage_command.app_exception"), e);
            }
        }
        // setting request attribute for using jspf needed page
        request.setAttribute(REQUEST_PATH.getPageNum(), pageNum);
        return forward;
    }
}
