package ua.nure.levchenko.SummaryTask4.web.path;

public class Path {
    // pages

    //outOfControl pages
    public static final String PAGE_LOGIN = "/login.jsp";
    public static final String PAGE_SING_IN = "/sign_up.jsp";

    // common
    public static final String PAGE_TEACHER = "/WEB-INF/jsp/teacher/teacher.jsp";
    public static final String PAGE_TEACHER_MANAGE = "/WEB-INF/jsp/admin/teacher_manage.jsp";
    public static final String PAGE_STUDENT = "/WEB-INF/jsp/student/student.jsp";
    public static final String PAGE_BLOCK_STUDENT = "/WEB-INF/jsp/admin/student_manage.jsp";
    public static final String PAGE_ERROR_PAGE = "/WEB-INF/jsp/common/error_page.jsp";
    public static final String PAGE_JOURNALS_PAGE = "/WEB-INF/jsp/teacher/journals.jsp";
    public static final String PAGE_SETTINGS = "/WEB-INF/jsp/common/settings.jsp";
    public static final String PAGE_COURSES = "/WEB-INF/jsp/student/courses.jsp";
    public static final String PAGE_COURSE_CREATE = "/WEB-INF/jsp/admin/course_manage.jsp";
    public static final String PAGE_CHANGE_LOCALE = "/WEB-INF/jsp/common/changeLocale.jsp";
    public static final String PAGE_LANGUAGE_SETTINGS = "/WEB-INF/jsp/common/settings_language.jsp";

    // commands
    public static final String COMMAND_STUDENT_COURSES = "controller?command=studentCoursesCommand";

}
