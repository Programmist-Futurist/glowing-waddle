package ua.nure.levchenko.SummaryTask4.validators;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.dao.*;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.internationalization.SessionContainer;

import java.io.Serializable;

/**
 * Root of all validators.
 *
 * @author K.Levchenko
 */
public abstract class Validator implements Serializable {

    protected static final SessionContainer SESSION_CONTAINER;
    private static final long serialVersionUID = -4981835883067596686L;
    private static final Logger LOG = Logger.getLogger(Validator.class);
    protected static DictionaryDao dictionaryDao;
    protected static UserDao userDao;
    protected static CourseDao courseDao;
    protected static TeacherDao teacherDao;
    protected static StudentDao studentDao;
    protected static TopicDao topicDao;
    protected static StatusDao statusDao;
    protected static RoleDao roleDao;
    protected static JournalEntityDao journalEntityDao;

    static {
        SESSION_CONTAINER = SessionContainer.getInstance();
        try {
            dictionaryDao = DictionaryDao.getInstance();
            userDao = UserDao.getInstance();
            courseDao = CourseDao.getInstance();
            teacherDao = TeacherDao.getInstance();
            studentDao = StudentDao.getInstance();
            studentDao = StudentDao.getInstance();
            topicDao = TopicDao.getInstance();
            statusDao = StatusDao.getInstance();
            roleDao = RoleDao.getInstance();
            journalEntityDao = JournalEntityDao.getInstance();
        } catch (DBException e) {
            LOG.error("Cannot initialize Dao class in Service", e);
        }
    }
}
