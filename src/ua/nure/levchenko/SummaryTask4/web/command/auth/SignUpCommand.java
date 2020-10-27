package ua.nure.levchenko.SummaryTask4.web.command.auth;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.Dictionary;
import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.exception.ValidationException;
import ua.nure.levchenko.SummaryTask4.internationalization.Language;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.utils.wordTransformer.WordTransformer;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sign Up command.
 *
 * @author K.Levchenko
 */
public class SignUpCommand extends Command {

    private static final long serialVersionUID = -8506576564315805157L;

    private static final Logger LOG = Logger.getLogger(SignUpCommand.class);


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LOG.debug("Command starts");
        // getting session
        //getting session
        HttpSession session = request.getSession();

        String forward = Path.PAGE_SING_IN;

        // initializing parameter list
        Map<String, String> parameterList = new HashMap<>();

        // getting action parameter
        String action = request.getParameter(REQUEST_PATH.getAction());
        parameterList.put("action", action);
        LOG.trace("Parameter required: action --> " + action);

        int userId = 0;
        if ("login".equals(action)) {
            LOG.trace("forward to the Login page");
            forward = Path.PAGE_LOGIN;
        } else if ("signUp".equals(action)) {

            // getting needed parameters
            String login = request.getParameter(REQUEST_PATH.getLogin());
            parameterList.put("login", login);
            LOG.trace("Parameter required: login --> " + login);

            String password = request.getParameter(REQUEST_PATH.getPassword());
            parameterList.put("password", password);
            LOG.trace("Parameter required: password --> ***");

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

            try {
                // validation
                String errorMessage = ENTRY_VALIDATOR.validateOnSignUp(parameterList);

                if (errorMessage.isEmpty()) {
                    LOG.debug("Starts to creating new user");

                    // forming Dictionary list
                    List<Dictionary> dictionaryList = new ArrayList<>();
                    dictionaryList.add(new Dictionary(WordTransformer.rus2eng(firstName), firstName));
                    dictionaryList.add(new Dictionary(WordTransformer.rus2eng(lastName), lastName));
                    dictionaryList.add(new Dictionary(WordTransformer.rus2eng(patronymic), patronymic));

                    // creating dictionaries in DB
                    CREATE_SERVICE.createDictionaries(dictionaryList);
                    LOG.trace("Dictionary successfully added to DataBase");

                    // creating student
                    CREATE_SERVICE.createStudent(parameterList);
                    LOG.trace("Student successfully added to DataBase");

                    // forming user before set
                    // getting user correct view
                    Map<String, String> userNames = READ_SERVICE.userToCorrectView(login);
                    userId = Integer.parseInt(userNames.get(EntityFields.ID));
                    // prepare student needed view
                    userNames.putAll(READ_SERVICE.studentToCorrectView(userId));

                    //setting session parameter student
                    session.setAttribute(SESSION_PATH.getUser(), userNames);

                    // prepare role needed view
                    int roleId = Integer.parseInt(userNames.get(EntityFields.USER_ROLE_ID));
                    Map<String, String> roleNames = READ_SERVICE.roleToCorrectView(userId, roleId);

                    //setting attribute role
                    session.setAttribute(SESSION_PATH.getUserRole(), roleNames);

                    LOG.trace("Session attribute \"user\" has set");

                    forward = Path.PAGE_STUDENT;
                } else {
                    request.setAttribute(REQUEST_PATH.getErrorMessage(), errorMessage);
                }
            } catch (ServiceException | ValidationException e) {
                LOG.error("Problems with singing in new user. Sorry for problems. Try later..", e);
                throw new AppException(resourceBundle.getString("sign_up_command.app_exception"), e);
            }
        }
        // put user into container of sessions and setting default language
        SESSION_CONTAINER.put(userId, Language.ENG);

        return forward;
    }
}
