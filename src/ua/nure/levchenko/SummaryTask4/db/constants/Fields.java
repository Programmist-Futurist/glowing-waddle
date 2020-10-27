package ua.nure.levchenko.SummaryTask4.db.constants;


/**
 * Field of entities in DB.
 *
 * @author K.Levchenko
 */
public class Fields {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String NAME_ID = "name_id";

    public static final String DICTIONARY_ENG = "eng";
    public static final String DICTIONARY_RUS = "rus";

    public static final String USER_ID = "id";
    public static final String USER_LOGIN = "login";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROLE_ID = "role_id";

    public static final String TEACHER_USER_ID = "user_id";
    public static final String TEACHER_FIRST_NAME_ID = "firstName_id";
    public static final String TEACHER_LAST_NAME_ID = "lastName_id";
    public static final String TEACHER_PATRONYMIC_ID = "patronymic_id";
    public static final String TEACHER_EXPERIENCE_ID = "experience_id";
    public static final String TEACHER_EMAIL = "email";
    public static final String TEACHER_PHONE = "phone";

    public static final String STUDENT_USER_ID = "user_id";
    public static final String STUDENT_FIRST_NAME_ID = "firstName_id";
    public static final String STUDENT_LAST_NAME_ID = "lastName_id";
    public static final String STUDENT_PATRONYMIC_ID = "patronymic_id";
    public static final String STUDENT_EMAIL = "email";
    public static final String STUDENT_PHONE = "phone";
    public static final String STUDENT_BLOCK = "block";

    public static final String COURSE_TEACHER_ID = "teacher_id";
    public static final String COURSE_TOPIC_ID = "topic_id";
    public static final String COURSE_STATUS_ID = "status_id";
    public static final String COURSE_NAME_ID = "name_id";
    public static final String COURSE_DESCRIPTION_ID = "description_id";
    public static final String COURSE_DURATION_ID = "duration_in_month";
    public static final String COURSE_START_DATE = "start_date";
    public static final String COURSE_END_DATE = "end_date";

    public static final String JOURNAL_COURSE_ID = "course_id";
    public static final String JOURNAL_STUDENT_ID = "student_id";
    public static final String JOURNAL_STUDENT_MARK = "mark";
}
