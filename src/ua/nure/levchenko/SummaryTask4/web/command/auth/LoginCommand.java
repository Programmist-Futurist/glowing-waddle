package ua.nure.levchenko.SummaryTask4.web.command.auth;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.internationalization.Language;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Login command.
 *
 * @author K.Levchenko
 */
public class LoginCommand extends Command {
    private static final long serialVersionUID = -3071536593627692473L;

    private static final Logger LOG = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws AppException {

        LOG.debug("Command starts");

        //getting session
        HttpSession session = request.getSession();

        // initializing parameter list
        Map<String, String> parameterList = new HashMap<>();

        // getting action parameter from the request
        String action = request.getParameter(REQUEST_PATH.getAction());
        parameterList.put("action", action);
        LOG.trace("Request parameter: action --> " + action);

        String forward = Path.PAGE_LOGIN;
        try {
            if ("singIn".equals(action)) {
                forward = Path.PAGE_SING_IN;
            } else if ("login".equals(action)) {

                // obtain login and password from a request
                String login = request.getParameter(REQUEST_PATH.getLogin());
                parameterList.put("login", login);
                LOG.trace("Request parameter: login --> " + login);

                String password = request.getParameter(REQUEST_PATH.getPassword());
                parameterList.put("password", password);
                LOG.trace("Request parameter: password --> " + password);

                // validate
                String message = ENTRY_VALIDATOR.validateOnLogin(parameterList);

                if (message != null) {

                    if ("student".equals(message)) {
                        // getting user correct view
                        Map<String, String> userNames = READ_SERVICE.userToCorrectView(login);
                        int userId = Integer.parseInt(userNames.get(EntityFields.ID));

                        // put user into container of sessions and setting default language
                        SESSION_CONTAINER.put(userId, Language.ENG);

                        // prepare student needed view
                        userNames.putAll(READ_SERVICE.studentToCorrectView(userId));

                        //setting student like a user
                        session.setAttribute(SESSION_PATH.getUser(), userNames);


                        LOG.trace("Found: student --> " + userNames);
                        forward = Path.PAGE_STUDENT;

                    } else if ("teacher".equals(message)) {
                        // getting user correct view
                        Map<String, String> userNames = READ_SERVICE.userToCorrectView(login);
                        int userId = Integer.parseInt(userNames.get(EntityFields.ID));

                        // put user into container of sessions and setting default language
                        SESSION_CONTAINER.put(userId, Language.ENG);

                        // user to correct view
                        userNames.putAll(READ_SERVICE.teacherToCorrectView(userId));

                        //setting teacher like a user
                        session.setAttribute(SESSION_PATH.getUser(), userNames);

                        LOG.trace("Found: teacher --> " + userNames);
                        forward = Path.PAGE_TEACHER;
                    } else {
                        request.setAttribute(REQUEST_PATH.getErrorMessage(), message);
                    }
                }
            }
        } catch (ServiceException e) {
            LOG.error("Problems with logging in. Sorry for problems. Try later..", e);
            throw new AppException(resourceBundle.getString("log_in_command.app_exception"), e);
        }
        return forward;
    }
}