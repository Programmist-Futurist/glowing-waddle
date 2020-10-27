package ua.nure.levchenko.SummaryTask4.web.command.student;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.exception.ValidationException;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Command to edit student.
 *
 * @author K.Levchenko
 */
public class EditStudentDataCommand extends Command {
    private static final long serialVersionUID = -945966818886063096L;

    private static final Logger LOG = Logger.getLogger(EditStudentDataCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LOG.debug("Command starts");

        //getting session
        HttpSession session = request.getSession();
        Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
        int userId = Integer.parseInt(user.get(EntityFields.ID));

        String forward = Path.PAGE_SETTINGS;

        // initializing parameter list
        Map<String, String> parameterList = new HashMap<>();

        // getting action from request
        String action = request.getParameter(REQUEST_PATH.getAction());
        parameterList.put("action", action);
        LOG.trace("Parameter required: action --> " + action);
        try {
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

                String email = request.getParameter(REQUEST_PATH.getEmail());
                parameterList.put("email", email);
                LOG.trace("Parameter required: email --> " + email);

                String phone = request.getParameter(REQUEST_PATH.getPhone());
                parameterList.put("phone", phone);
                LOG.trace("Parameter required: phone --> " + phone);

                //validate
                String errorMessage = STUDENT_VALIDATOR.validateOnUpdate(userId, parameterList);

                if (errorMessage.isEmpty()) {
                    LOG.debug("Starts to updating user");

                    // updating student in DB
                    UPDATE_SERVICE.updateStudent(userId, parameterList);


                    String loginUser = user.get("login");
                    // preparing updated user view
                    Map<String, String> userNames = new HashMap<>(READ_SERVICE.userToCorrectView(loginUser));
                    userNames.putAll(READ_SERVICE.studentToCorrectView(userId));

                    // updating user in session
                    session.setAttribute(SESSION_PATH.getUser(), userNames);
                    LOG.trace("Session attribute \"user\" has updated");

                    request.setAttribute(REQUEST_PATH.getInfoMessage()
                            , resourceBundle.getString("edit_student_data.success_info_update"));
                } else {
                    request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                }
            }
        } catch (ServiceException | ValidationException e) {
            LOG.error("Problems with editing user. Sorry for problems. Try later..", e);
            throw new AppException(resourceBundle.getString("edit_student_data.app_exception"), e);
        }
        return forward;
    }
}
