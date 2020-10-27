package ua.nure.levchenko.SummaryTask4.web.command.common;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;


/**
 * Log out command.
 *
 * @author K.Levchenko
 */
public class LogoutCommand extends Command {

    private static final long serialVersionUID = 8009200679528096874L;

    private static final Logger LOG = Logger.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Command starts");

        HttpSession session = request.getSession(false);
        if (session != null) {
            // getting current user
            Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
            int userId = Integer.parseInt(user.get(EntityFields.ID));

            //setting default locale
            Locale.getDefault();

            // deletes current user from session container and session
            SESSION_CONTAINER.remove(userId);
            session.invalidate();
        }

        LOG.debug("Command finished");
        return Path.PAGE_LOGIN;
    }
}
