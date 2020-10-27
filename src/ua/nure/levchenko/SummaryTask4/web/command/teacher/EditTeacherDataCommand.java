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
import java.util.Map;

/**
 * Command to edit teacher data.
 *
 * @author K.Levchenko
 */

public class EditTeacherDataCommand extends Command {
    private static final long serialVersionUID = -2785976616686657267L;

    private static final Logger LOG = Logger.getLogger(EditTeacherDataCommand.class);


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LOG.debug("Command starts");

        String forward = Path.PAGE_SETTINGS;

        //getting session
        HttpSession session = request.getSession();
        Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
        int userId = Integer.parseInt(user.get(EntityFields.ID));

        //initializing parameter list
        Map<String, String> parameterList = new HashMap<>();

        // getting parameter action from the request
        String action = request.getParameter(REQUEST_PATH.getAction());
        parameterList.put("action", action);
        LOG.trace("Parameter required: action --> " + action);

        if ("edit".equals(action)) {

            // getting all needed parameters for this action
            String login = request.getParameter(REQUEST_PATH.getLogin());
            parameterList.put("login", login);
            LOG.trace("Parameter required: login --> " + login);

            String oldLogin = request.getParameter(REQUEST_PATH.getOldLogin());
            parameterList.put("oldLogin", oldLogin);
            LOG.trace("Parameter required: oldLogin --> " + oldLogin);

            String password = request.getParameter(REQUEST_PATH.getPassword());
            parameterList.put("password", password);
            LOG.trace("Parameter required: password --> ***");

            String oldPassword = request.getParameter(REQUEST_PATH.getOldPassword());
            parameterList.put("oldPassword", oldPassword);
            LOG.trace("Parameter required: oldPassword --> ***");

            String firstName = request.getParameter(REQUEST_PATH.getFirstName());
            parameterList.put("firstName", firstName);
            LOG.trace("Parameter required: firstName --> " + firstName);

            String lastName = request.getParameter(REQUEST_PATH.getLastName());
            parameterList.put("lastName", lastName);
            LOG.trace("Parameter required: lastName --> " + lastName);

            String patronymic = request.getParameter(REQUEST_PATH.getPatronymic());
            parameterList.put("patronymic", patronymic);
            LOG.trace("Parameter required: patronymic --> " + patronymic);

            String experience = request.getParameter(REQUEST_PATH.getExperience());
            parameterList.put("experience", experience);
            LOG.trace("Parameter required: experience --> " + experience);

            String firstNameRus = request.getParameter(REQUEST_PATH.getFirstNameRus());
            parameterList.put("firstNameRus", firstNameRus);
            LOG.trace("Parameter required: firstNameRus --> " + firstNameRus);

            String lastNameRus = request.getParameter(REQUEST_PATH.getLastNameRus());
            parameterList.put("lastNameRus", lastNameRus);
            LOG.trace("Parameter required: lastNameRus --> " + lastNameRus);

            String patronymicRus = request.getParameter(REQUEST_PATH.getPatronymicRus());
            parameterList.put("patronymicRus", patronymicRus);
            LOG.trace("Parameter required: patronymicRus --> " + patronymicRus);

            String experienceRus = request.getParameter(REQUEST_PATH.getExperienceRus());
            parameterList.put("experienceRus", experienceRus);
            LOG.trace("Parameter required: experienceRus --> " + experienceRus);

            String email = request.getParameter(REQUEST_PATH.getEmail());
            parameterList.put("email", email);
            LOG.trace("Parameter required: email --> " + email);

            String phone = request.getParameter(REQUEST_PATH.getPhone());
            parameterList.put("phone", phone);
            LOG.trace("Parameter required: phone --> " + phone);

            try {
                // validate
                String errorMessage = TEACHER_VALIDATOR.validateOnUpdate(userId, parameterList);

                if (errorMessage.isEmpty()) {
                    LOG.debug("Starts to updating user");

                    // updating teacher in DB
                    UPDATE_SERVICE.updateTeacher(userId, parameterList);

                    String loginUser = user.get("login");
                    // preparing updated user view
                    Map<String, String> userNames = new HashMap<>(READ_SERVICE.userToCorrectView(loginUser));
                    userNames.putAll(READ_SERVICE.teacherToCorrectView(userId));

                    // updating user in session
                    session.setAttribute(SESSION_PATH.getUser(), userNames);
                    LOG.trace("Session attribute \"user\" has updated");

                    LOG.trace("Message for User --> Information updated successfully");
                    request.setAttribute(REQUEST_PATH.getInfoMessage()
                            , resourceBundle.getString("edit_teacher_data_command.success_on_update"));
                } else {
                    LOG.trace("Message for User --> " + errorMessage);
                    request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                }
            } catch (ServiceException e) {
                LOG.error("Problems with editing user. Sorry for problems. Try later..", e);
                throw new AppException(resourceBundle.getString("edit_teacher_data_command.app_exception"), e);
            }
        }
        return forward;
    }
}
