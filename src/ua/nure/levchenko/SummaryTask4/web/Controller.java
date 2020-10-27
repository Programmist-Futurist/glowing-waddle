package ua.nure.levchenko.SummaryTask4.web;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.command.CommandContainer;
import ua.nure.levchenko.SummaryTask4.web.path.Path;
import ua.nure.levchenko.SummaryTask4.web.path.RequestPath;
import ua.nure.levchenko.SummaryTask4.web.path.SessionPath;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Main servlet controller.
 *
 * @author K.Levchenko
 */

@WebServlet(name = "Controller", value = "/controller")
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 2423353715955164816L;

    private static final Logger LOG = Logger.getLogger(Controller.class);
    private static final RequestPath REQUEST_PATH = new RequestPath();
    private static final SessionPath SESSION_PATH = new SessionPath();

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    /**
     * Main method of this controller.
     */
    private void process(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("Controller starts");

        String forward = Path.PAGE_ERROR_PAGE;

        // extract command name from the request
        String commandName = request.getParameter(REQUEST_PATH.getCommand());
        LOG.trace("Request parameter: command --> " + commandName);

        // obtain command object by its name
        Command command = CommandContainer.get(commandName);
        LOG.trace("Obtained command --> " + command);

        try {
            forward = command.execute(request, response);
        } catch (AppException ex) {
            request.setAttribute(REQUEST_PATH.getErrorMessage(), ex.getMessage());
        }

        //getting session
        HttpSession session = request.getSession();

        // execute command and get forward address
        if (session != null) {
            session.setAttribute(SESSION_PATH.getCurrentPage(), forward);
        }

        LOG.trace("Forward address --> " + forward);

        LOG.debug("Controller finished, now go to forward address --> " + forward);

        // go to forward
        request.getRequestDispatcher(forward).forward(request, response);
    }
}