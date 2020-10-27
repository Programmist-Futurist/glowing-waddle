package ua.nure.levchenko.SummaryTask4.web.command.app;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.Student;
import ua.nure.levchenko.SummaryTask4.entity.Teacher;
import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.internationalization.Language;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Setting needed language command.
 *
 * @author K.Levchenko
 */
public class SetLanguageCommand extends Command {

    private static final long serialVersionUID = 153684855881471574L;

    private static final Logger LOG = Logger.getLogger(SetLanguageCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LOG.debug("Command starts");

        //getting session
        HttpSession session = request.getSession();
        Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
        int userId = Integer.parseInt(user.get(EntityFields.ID));

        String locale = request.getParameter(REQUEST_PATH.getLocale());
        LOG.trace("Parameter required: locale -->" + locale);

        if ("ru".equals(locale)) {
//             dictionary for entities
            SESSION_CONTAINER.replace(userId, Language.RUS);

            resourceBundle = ResourceBundle.getBundle("resources", new Locale("ru", "Ru"));

            LOG.trace("Language was set --> " + Language.RUS);
        } else {
            // dictionary for entities
            SESSION_CONTAINER.replace(userId, Language.ENG);

            resourceBundle = ResourceBundle.getBundle("resources", new Locale("en", "US"));

            LOG.trace("Language was set --> " + Language.ENG);
        }

        // updating user values
        String login = user.get(EntityFields.USER_LOGIN);
        Map<String, String> userNames = READ_SERVICE.userToCorrectView(login);

        if (user.get(EntityFields.USER_CLASS).equals(Teacher.class.getSimpleName())) {
            userNames.putAll(READ_SERVICE.teacherToCorrectView(userId));
        } else if (user.get(EntityFields.USER_CLASS).equals(Student.class.getSimpleName())) {
            userNames.putAll(READ_SERVICE.studentToCorrectView(userId));
        }

        // updating user in session
        session.setAttribute(SESSION_PATH.getUser(), userNames);
        LOG.trace("Session updated");
        LOG.debug("Command finished");

        return Path.PAGE_CHANGE_LOCALE;
    }
}
