package ua.nure.levchenko.SummaryTask4.web.command.student;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Command to direct user on the page of Student.
 *
 * @author K.Levchenko
 */
public class StudentCommand extends Command {

    private static final long serialVersionUID = 3398313222639147177L;

    private static final Logger LOG = Logger.getLogger(StudentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        LOG.debug("Command starts");
        LOG.debug("Command ends");
        return Path.PAGE_STUDENT;
    }
}
