package ua.nure.levchenko.SummaryTask4.db.constants;

/**
 * Class of constant SQL commands.
 *
 * @author K.Levchenko
 */
public class SQLCommands {

    public static final String SQL_CREATE_DICTIONARY =
            "INSERT INTO dictionary(eng, rus) VALUES (?,?)";

    public static final String SQL_CREATE_ROLE =
            "INSERT INTO roles(name, name_id) VALUES (?, ?)";

    public static final String SQL_CREATE_USER =
            "INSERT INTO users(login, password, role_id) VALUES (?,?,?)";

    public static final String SQL_CREATE_TEACHER =
            "INSERT INTO teachers(user_id, firstName_id, lastName_id, " +
                    "patronymic_id, experience_id, email, phone) VALUES (?,?,?,?,?,?,?)";

    public static final String SQL_CREATE_STUDENT =
            "INSERT INTO students (user_id, firstName_id, lastName_id, patronymic_id," +
                    " email, phone, block) VALUES (?,?,?,?,?,?,?)";

    public static final String SQL_CREATE_STATUS =
            "INSERT INTO statuses (name, name_id) VALUES (?,?)";

    public static final String SQL_CREATE_TOPIC =
            "INSERT INTO topics (name, name_id) VALUES (?,?)";

    public static final String SQL_CREATE_COURSE =
            "INSERT INTO courses(teacher_id, topic_id, status_id, name_id," +
                    " description_id, duration_in_month, start_date, end_date) VALUES (?,?,?,?,?,?,?,?)";

    public static final String SQL_CREATE_JOURNAL_ENTITY =
            "INSERT INTO journal(course_id, student_id, mark) VALUES (?,?,?)";

    public static final String SQL_UPDATE_DICTIONARY =
            "UPDATE dictionary SET eng=(?), rus=(?) WHERE id=(?)";

    public static final String SQL_UPDATE_ROLE =
            "UPDATE roles SET name=(?), name_id=(?) WHERE id=(?)";

    public static final String SQL_UPDATE_USER =
            "UPDATE users SET login=(?), password=(?), role_id=(?) WHERE id=(?)";

    public static final String SQL_UPDATE_TEACHER =
            "UPDATE teachers SET firstName_id=(?), lastName_id=(?), " +
                    "patronymic_id=(?), experience_id=(?), email=(?), phone=(?) WHERE user_id=(?)";

    public static final String SQL_UPDATE_STUDENT =
            "UPDATE students SET firstName_id=(?), lastName_id=(?), patronymic_id=(?)," +
                    " email=(?), phone=(?), block=(?) WHERE user_id=(?)";

    public static final String SQL_UPDATE_STATUS =
            "UPDATE statuses SET name=(?), name_id=(?) WHERE id=(?)";

    public static final String SQL_UPDATE_TOPIC =
            "UPDATE topics SET name=(?), name_id=(?) WHERE id=(?)";

    public static final String SQL_UPDATE_COURSE =
            "UPDATE courses SET teacher_id=(?), topic_id=(?), status_id=(?), name_id=(?)," +
                    " description_id=(?), duration_in_month=(?), start_date=(?), end_date=(?) WHERE id=(?)";

    public static final String SQL_UPDATE_JOURNAL_ENTITY =
            "UPDATE journal SET course_id=(?), student_id=(?), mark=(?) WHERE id=(?)";

    public static final String SQL_READ_DICTIONARY_BY_ID =
            "select * from dictionary where id=(?)";

    public static final String SQL_READ_DICTIONARY_BY_ENG_WORD =
            "select * from dictionary where eng=(?)";

    public static final String SQL_READ_DICTIONARY_BY_RUS_WORD =
            "select * from dictionary where rus=(?)";

    public static final String SQL_READ_ROLE_BY_ID =
            "select * from roles where id=(?)";

    public static final String SQL_READ_USER_BY_ID =
            "select * from users where id=(?)";

    public static final String SQL_READ_USER_BY_LOGIN =
            "select * from users where login=(?)";

    public static final String SQL_READ_TEACHER_BY_ID =
            "select * from teachers where user_id=(?)";

    public static final String SQL_READ_TEACHER_BY_LOGIN =
            "select * from teachers where login=(?)";

    public static final String SQL_READ_STUDENT_BY_ID =
            "select * from students where user_id=(?)";

    public static final String SQL_READ_STATUS_BY_ID =
            "select * from statuses where id=(?)";

    public static final String SQL_READ_TOPIC_BY_ID =
            "select * from topics where id=(?)";

    public static final String SQL_READ_COURSE_BY_ID =
            "select * from courses where id=(?)";

    public static final String SQL_READ_COURSE_BY_NAME_ID =
            "select * from courses where name_id=(?)";

    public static final String SQL_READ_JOURNAL_ENTITY_BY_ID =
            "select * from journal where id=(?)";

    public static final String SQL_READ_JOURNAL_ENTITY_BY_COURSE_AND_STUDENT_ID =
            "select * from journal where course_id=(?) AND student_id=(?)";

    public static final String SQL_DELETE_DICTIONARY_BY_ID =
            "DELETE from dictionary where id=(?)";

    public static final String SQL_DELETE_ROLE_BY_ID =
            "DELETE from roles where id=(?)";

    public static final String SQL_DELETE_USER_BY_ID =
            "DELETE from users where id=(?)";

    public static final String SQL_DELETE_TEACHER_BY_ID =
            "DELETE from teachers where user_id=(?)";

    public static final String SQL_DELETE_STUDENT_BY_ID =
            "DELETE from students where user_id=(?)";

    public static final String SQL_DELETE_STATUS_BY_ID =
            "DELETE from statuses where id=(?)";

    public static final String SQL_DELETE_TOPIC_BY_ID =
            "DELETE from topics where id=(?)";

    public static final String SQL_DELETE_COURSE_BY_ID =
            "DELETE from courses where id=(?)";

    public static final String SQL_DELETE_JOURNAL_ENTITY_BY_ID =
            "DELETE from journal where id=(?)";

    public static final String SQL_GET_COURSE_BY_NAME =
            "select * from courses where name=(?)";

    public static final String SQL_GET_STATUS_BY_ID =
            "select * from statuses where id=(?)";

    public static final String SQL_GET_TOPIC_BY_ID =
            "select * from topics where id=(?)";

    public static final String SQL_GET_JOURNAL_ENTITIES_ORDER_BY_ID =
            "select * from journal order by id";

    public static final String SQL_GET_DICTIONARY_ORDER_BY_ID =
            "select * from dictionary order by id";

    public static final String SQL_GET_COURSES_ORDER_BY_ID =
            "select * from courses order by id";

    public static final String SQL_GET_STATUSES_ORDER_BY_ID =
            "select * from statuses order by id";

    public static final String SQL_GET_ROLES_ORDER_BY_ID =
            "select * from roles order by id";

    public static final String SQL_GET_TOPICS_ORDER_BY_ID =
            "select * from topics order by id";

    public static final String SQL_GET_COURSES_BY_TOPIC =
            "select * from courses where topic_id=(?)";

    public static final String SQL_GET_COURSES_BY_STATUS =
            "select * from courses where status_id=(?)";

    public static final String SQL_GET_COURSES_BY_TEACHER =
            "select * from courses where teacher_id=(?)";

    public static final String SQL_GET_TEACHER_BY_LOGIN_AND_PASS =
            "select * from teachers where login=(?) AND password=(?)";

    public static final String SQL_GET_STUDENT_BY_LOGIN_AND_PASS =
            "select * from students where login=(?) AND password=(?)";

    public static final String SQL_GET_TEACHERS_ORDER_BY_ID =
            "select * from teachers order by user_id";

    public static final String SQL_GET_USERS_ORDER_BY_ID =
            "select * from users order by id";

    public static final String SQL_GET_JOURNAL_BY_COURSE_ID =
            "select * from journal where course_id=(?)";

    public static final String SQL_GET_JOURNAL_BY_STUDENT_ID =
            "select * from journal where student_id=(?)";

    public static final String SQL_GET_STUDENT_BY_ID =
            "select * from students where user_id=(?)";


    public static final String SQL_GET_STUDENTS_ORDER_BY_ID =
            "select * from students order by user_id";

}
