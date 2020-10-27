package ua.nure.levchenko.SummaryTask4.web.command.admin;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.exception.AppException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.web.command.Command;
import ua.nure.levchenko.SummaryTask4.web.path.Path;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Blocking/unblocking students command
 *
 * @author K.Levchenko
 */
public class ManageStudentCommand extends Command {

    private static final long serialVersionUID = -2571541296953712073L;

    private static final Logger LOG = Logger.getLogger(ManageStudentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
        LOG.debug("Command starts");

        //getting session
        HttpSession session = request.getSession();
        Map<String, String> user = (Map<String, String>) session.getAttribute(SESSION_PATH.getUser());
        int userId = Integer.parseInt(user.get(EntityFields.ID));

        // setting request attributes
        request.setAttribute(REQUEST_PATH.getBlockedStudents(),
                READ_SERVICE.getAllBlockedStudents(userId));
        request.setAttribute(REQUEST_PATH.getUnblockedStudents(),
                READ_SERVICE.getAllUnblockedStudents(userId));
        LOG.trace("Attributes updated");


        //getting action parameter
        String action = request.getParameter(REQUEST_PATH.getAction());
        LOG.trace("Parameter found: action --> " + action);

        if (action != null) {
            //getting studentId parameter
            String studentId = request.getParameter(REQUEST_PATH.getStudentId());
            LOG.trace("Parameter found: studentId --> " + studentId);

            try {
                if ("Block".equals(action)) {

                    //blocking Student
                    UPDATE_SERVICE.blockStudent(Integer.parseInt(studentId));

                    //setting message ro user on page
                    request.setAttribute(REQUEST_PATH.getInfoMessage()
                            , resourceBundle.getString("student_manage_command.success_on_block"));

                } else if ("Unblock".equals(action)) {

                    // Unblocking student
                    UPDATE_SERVICE.unblockStudent(Integer.parseInt(studentId));

                    //setting message ro user on page
                    request.setAttribute(REQUEST_PATH.getInfoMessage()
                            , resourceBundle.getString("student_manage_command.success_on_unblock"));
                }
            } catch (ServiceException e) {
                LOG.error("Problems with blocking student. Sorry for problems. Try later..", e);
                throw new AppException(resourceBundle.getString("student_manage_command.app_exception"), e);
            }
        }
        return Path.PAGE_BLOCK_STUDENT;
    }
}
