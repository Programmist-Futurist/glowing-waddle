package ua.nure.levchenko.SummaryTask4.services;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.dao.*;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.internationalization.SessionContainer;

import java.io.Serializable;

/**
 * Root of all services.
 *
 * @author K.Levchenko
 */
public abstract class Service implements Serializable {

    protected static final SessionContainer SESSION_CONTAINER;
    private static final long serialVersionUID = 9120096996917405674L;
    private static final Logger LOG = Logger.getLogger(Service.class);
    static DictionaryDao dictionaryDao;
    static UserDao userDao;
    static CourseDao courseDao;
    static TeacherDao teacherDao;
    static StudentDao studentDao;
    static TopicDao topicDao;
    static StatusDao statusDao;
    static RoleDao roleDao;
    static JournalEntityDao journalEntityDao;


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
