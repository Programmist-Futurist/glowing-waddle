package ua.nure.levchenko.SummaryTask4.web.command;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.web.command.admin.CourseForTeacherSetCommand;
import ua.nure.levchenko.SummaryTask4.web.command.admin.ManageCoursesCommand;
import ua.nure.levchenko.SummaryTask4.web.command.admin.ManageStudentCommand;
import ua.nure.levchenko.SummaryTask4.web.command.admin.ManageTeacherCommand;
import ua.nure.levchenko.SummaryTask4.web.command.app.SetLanguageCommand;
import ua.nure.levchenko.SummaryTask4.web.command.auth.LoginCommand;
import ua.nure.levchenko.SummaryTask4.web.command.auth.SignUpCommand;
import ua.nure.levchenko.SummaryTask4.web.command.common.LanguageSettingsCommand;
import ua.nure.levchenko.SummaryTask4.web.command.common.LogoutCommand;
import ua.nure.levchenko.SummaryTask4.web.command.student.CourseEnrollCommand;
import ua.nure.levchenko.SummaryTask4.web.command.student.EditStudentDataCommand;
import ua.nure.levchenko.SummaryTask4.web.command.student.StudentCommand;
import ua.nure.levchenko.SummaryTask4.web.command.student.StudentCoursesCommand;
import ua.nure.levchenko.SummaryTask4.web.command.teacher.EditTeacherDataCommand;
import ua.nure.levchenko.SummaryTask4.web.command.teacher.TeacherCommand;
import ua.nure.levchenko.SummaryTask4.web.command.teacher.TeacherJournalsCommand;

import java.util.Map;
import java.util.TreeMap;

/**
 * Holder for all commands.<br/>
 *
 * @author D.Kolesnikov
 */
public class CommandContainer {

    private static final Logger LOG = Logger.getLogger(CommandContainer.class);

    private static final Map<String, Command> commands = new TreeMap<>();

    static {
        // common commands
        commands.put("logout", new LogoutCommand());
        commands.put("setLanguage", new SetLanguageCommand());
        commands.put("languageSettings", new LanguageSettingsCommand());

        //auth
        commands.put("login", new LoginCommand());
        commands.put("signUp", new SignUpCommand());

        //admin
        commands.put("manageCourses", new ManageCoursesCommand());
        commands.put("manageTeacher", new ManageTeacherCommand());
        commands.put("courseForTeacher", new CourseForTeacherSetCommand());
        commands.put("manageStudent", new ManageStudentCommand());

        //teacher
        commands.put("teacher", new TeacherCommand());
        commands.put("teacherJournalsCommand", new TeacherJournalsCommand());
        commands.put("editTeacher", new EditTeacherDataCommand());

        //student
        commands.put("student", new StudentCommand());
        commands.put("filterCourses", new StudentCoursesCommand());
        commands.put("editStudent", new EditStudentDataCommand());
        commands.put("enrollCourse", new CourseEnrollCommand());


        LOG.debug("Command container was successfully initialized");
        LOG.trace("Number of commands --> " + commands.size());
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName Name of the command.
     * @return Command object.
     */
    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            LOG.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }

        return commands.get(commandName);
    }

}