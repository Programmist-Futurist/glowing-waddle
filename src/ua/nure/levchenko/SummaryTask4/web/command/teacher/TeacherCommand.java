package ua.nure.levchenko.SummaryTask4.web.command.teacher;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.command.auth.LoginCommand;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to direct user on the page of Teacher.
 *
 * @author K.Levchenko
 */
public class TeacherCommand extends Command {

    private static final long serialVersionUID = 178620351865849053L;

    private static final Logger LOG = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Command starts");
        LOG.debug("Command ends");
        return Path.PAGE_TEACHER;
    }
}
