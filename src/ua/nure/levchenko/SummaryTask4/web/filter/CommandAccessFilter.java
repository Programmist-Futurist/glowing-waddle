package ua.nure.levchenko.SummaryTask4.web.filter;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.Role;
import ua.nure.levchenko.SummaryTask4.entity.Student;
import ua.nure.levchenko.SummaryTask4.entity.Teacher;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.web.path.Path;
import ua.nure.levchenko.SummaryTask4.web.path.RequestPath;
import ua.nure.levchenko.SummaryTask4.web.path.SessionPath;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Security filter.
 *
 * @author K.Levchenko
 */

public class CommandAccessFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(CommandAccessFilter.class);

    // path
    private static final RequestPath REQUEST_PATH;
    private static final SessionPath SESSION_PATH;
    private static final ResourceBundle RESOURCE_BUNDLE;

    static {
        REQUEST_PATH = new RequestPath();
        SESSION_PATH = new SessionPath();

        RESOURCE_BUNDLE = ResourceBundle.getBundle("resources", new Locale("en", "US"));
    }

    // commands access
    private List<String> outOfControl;
    private List<String> common;

    private List<String> adminCommands;
    private List<String> teacherCommands;
    private List<String> studentCommands;
    private String message;
    private String forward;

    public void destroy() {
        LOG.debug("Filter destruction starts");
        // do nothing
        LOG.debug("Filter destruction finished");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        LOG.debug("Filter starts");

        if (accessAllowed(request)) {
            LOG.debug("Filter finished");
            chain.doFilter(request, response);
        } else {
            request.setAttribute(REQUEST_PATH.getErrorMessage(), message);
            LOG.error("Set the request attribute: infoMessage --> " + message);

            request.getRequestDispatcher(forward)
                    .forward(request, response);
        }
    }

    private boolean accessAllowed(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String commandName = request.getParameter(REQUEST_PATH.getCommand());
        if (commandName == null || commandName.isEmpty()) {
            message = RESOURCE_BUNDLE.getString("filter.empty_command_exception");
            forward = Path.PAGE_ERROR_PAGE;
            return false;
        }

        if (outOfControl.contains(commandName)) {
            return true;
        }

        HttpSession session = httpRequest.getSession();
        if (session.getAttribute(SESSION_PATH.getUser()) != null) {

            Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
            int currentRoleId = Integer.parseInt(user.get(EntityFields.USER_ROLE_ID));

            String userClassName = user.get(EntityFields.USER_CLASS);

            if (userClassName.equals(Teacher.class.getSimpleName())) {
                if (teacherCommands.contains(commandName)) {
                    return true;
                }
            } else if (userClassName.equals(Student.class.getSimpleName())) {
                if ("true".equals(user.get(EntityFields.STUDENT_BLOCK))) {
                    message = RESOURCE_BUNDLE.getString("filter.blocked_account_exception");
                    forward = Path.PAGE_ERROR_PAGE;
                    return false;
                } else if (studentCommands.contains(commandName)) {
                    return true;
                }
            }

            if (common.contains(commandName)) {
                return true;
            }
            if (Role.ADMIN.getIntKey() == currentRoleId && adminCommands.contains(commandName)) {
                return true;
            }
            message = RESOURCE_BUNDLE.getString("filter.unknown_command_exception");
            forward = Path.PAGE_ERROR_PAGE;

        } else {
            Locale.getDefault();
            message = RESOURCE_BUNDLE.getString("filter.did_not_log_in");
            forward = Path.PAGE_LOGIN;
        }

        return false;
    }

    public void init(FilterConfig fConfig) {
        LOG.debug("Filter initialization starts");

        // roles
        adminCommands = asList(fConfig.getInitParameter("admin"));
        LOG.trace("Admin commands --> " + adminCommands);
        teacherCommands = asList(fConfig.getInitParameter("teacher"));
        LOG.trace("Teacher commands --> " + teacherCommands);
        studentCommands = asList(fConfig.getInitParameter("student"));
        LOG.trace("Student commands --> " + studentCommands);

        outOfControl = asList(fConfig.getInitParameter("out-of-control"));
        LOG.trace("Out of control commands --> " + outOfControl);
        common = asList(fConfig.getInitParameter("common"));
        LOG.trace("Common commands --> " + common);

        LOG.debug("Filter initialization finished");
    }

    /**
     * Extracts parameter values from string.
     *
     * @param str parameter values string.
     * @return list of parameter values.
     */
    private List<String> asList(String str) {
        List<String> list = new ArrayList<>();
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return list;
    }

}