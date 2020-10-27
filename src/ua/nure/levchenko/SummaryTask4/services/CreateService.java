package ua.nure.levchenko.SummaryTask4.services;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.Dictionary;
import ua.nure.levchenko.SummaryTask4.entity.*;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.services.constants.Messages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service for working with create operations in DB.
 *
 * @author K.Levchenko
 */
public class CreateService extends Service {

    private static final long serialVersionUID = 1593731253754870226L;

    private static final Logger LOG = Logger.getLogger(CreateService.class);

    private static CreateService instance;

    private CreateService() {
    }

    public static synchronized CreateService getInstance() {
        if (instance == null) {
            instance = new CreateService();
            LOG.debug("Service initialized");
        }
        return instance;
    }

    //methods

    /**
     * Creates dictionary entities in DB using the objects from
     * dictionary list parameter
     *
     * @param dictionaryList list of dictionary entities  that we
     *                       need to create in DB.
     */
    public void createDictionaries(List<Dictionary> dictionaryList) throws ServiceException {
        LOG.debug("Service starts");
        try {
            for (Dictionary dictionary : dictionaryList) {
                if (dictionary != null) {
                    dictionaryDao.create(dictionary);
                    LOG.trace("Dictionary successfully added in DataBase");
                }
            }
        } catch (DBException e) {
            LOG.error(Messages.SERVICE_DICTIONARY_CREATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_DICTIONARY_CREATE_ERROR, e);
        }
        LOG.debug("Service finish");
    }


    /**
     * Creates teacher entity in DB using the parameters list
     *
     * @param parameters list of parameters needed for the creating new
     *                   teacher in the DB
     */
    public void createTeacher(Map<String, String> parameters) throws ServiceException {
        LOG.debug("Service starts");
        LOG.debug("Starts to creating new teacher");

        User user = null;
        Teacher teacher = null;

        // user
        String login = parameters.get("login");
        String password = parameters.get("password");
        String roleId = parameters.get("roleId");

        // teacher
        String firstName = parameters.get("firstName");
        String lastName = parameters.get("lastName");
        String patronymic = parameters.get("patronymic");
        String experience = parameters.get("experience");
        String email = parameters.get("email");
        String phone = parameters.get("phone");

        // course
        String courseId = parameters.get("courseId");

        try {

            // preparing classes to create in DB
            user = new User(login, password, Integer.parseInt(roleId));
            teacher = new Teacher(dictionaryDao.read(firstName).getId(),
                    dictionaryDao.read(lastName).getId(), dictionaryDao.read(patronymic).getId(),
                    dictionaryDao.read(experience).getId(), email, phone);

            // creating user in DB
            userDao.create(user);

            // getting user id for teacher
            int teacherId = userDao.read(user.getLogin()).getId();
            teacher.setId(teacherId);

            // creating teacher in DB
            teacherDao.create(teacher);
            LOG.trace("Teacher successfully added in DataBase");

            // setting course for teacher
            if (courseId != null && !courseId.isEmpty()) {
                Course course = courseDao.read(Integer.parseInt(courseId));
                course.setTeacherId(teacher.getId());
                courseDao.update(course);
                LOG.trace("Course successfully updated in DataBase");
            }

            LOG.debug("Service finish");

        } catch (DBException | NullPointerException | ClassCastException e) {
            if (user != null) {
                userDao.delete(user.getId());
            }
            if (teacher != null) {
                teacherDao.delete(user.getId());
            }

            LOG.error(Messages.SERVICE_TEACHER_CREATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_TEACHER_CREATE_ERROR, e);
        }
    }


    /**
     * Creates course entity in DB using the parameters list
     *
     * @param parameters list of parameters needed for the creating new
     *                   course in the DB
     */
    public void createCourse(Map<String, String> parameters) throws ServiceException {
        LOG.debug("Service starts");
        try {
            String courseTeacherId = parameters.get("courseTeacherId");
            String courseTopicId = parameters.get("courseTopicId");
            String courseName = parameters.get("courseName");
            String courseDescription = parameters.get("courseDescription");
            String courseStartDate = parameters.get("courseStartDate");
            String courseEndDate = parameters.get("courseEndDate");

            // parameters for constructor gets
            int teacherId = Integer.parseInt(courseTeacherId);
            int topicId = Integer.parseInt(courseTopicId);
            int courseNameId = dictionaryDao.read(courseName).getId();
            int courseDescriptionId = dictionaryDao.read(courseDescription).getId();

            // Dates converted to needed format
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
            in.setLenient(false);
            Calendar calendar = new GregorianCalendar();

            Date startDateDayBefore = in.parse(courseStartDate);
            calendar.setTime(startDateDayBefore);
            calendar.add(Calendar.DATE, 1);
            Date startDate = calendar.getTime();

            Date endDateDayBefore = in.parse(courseEndDate);
            calendar.setTime(endDateDayBefore);
            calendar.add(Calendar.DATE, 1);
            Date endDate = calendar.getTime();

            // Course preparing
            Course newCourse = new Course(teacherId, topicId, courseNameId,
                    courseDescriptionId, startDate, endDate);

            // Course creating
            courseDao.create(newCourse);
            LOG.trace("Course successfully created in DB");

        } catch (DBException | ParseException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_COURSE_CREATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_COURSE_CREATE_ERROR, e);
        }
        LOG.debug("Service finish");
    }

    /**
     * Creates journal entity in DB using the parameters list
     *
     * @param studentId  id of student whom we need to enroll on course
     * @param parameters list of parameters needed for the creating new
     *                   journal entity in the DB
     */
    public void createJournalEntity(int studentId, Map<String, String> parameters) throws ServiceException {
        LOG.debug("Service starts");
        try {
            String courseId = parameters.get("courseId");

            // preparing journalEntity object
            JournalEntity journalEntity
                    = new JournalEntity(Integer.parseInt(courseId), studentId);

            // creating new entity
            journalEntityDao.create(journalEntity);
            LOG.trace("Journal Entity created");

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_JOURNAL_CREATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_JOURNAL_CREATE_ERROR, e);
        }
        LOG.debug("Service finish");
    }

    /**
     * Creates student entity in DB using the parameters list
     *
     * @param parameters list of parameters needed for the creating new
     *                   student in the DB
     */
    public void createStudent(Map<String, String> parameters) throws ServiceException {
        LOG.debug("Service starts");
        try {
            String login = parameters.get("login");
            String password = parameters.get("password");
            String firstName = parameters.get("firstName");
            String lastName = parameters.get("lastName");
            String patronymic = parameters.get("patronymic");
            String email = parameters.get("email");
            String phone = parameters.get("phone");

            // forming user and student classes
            assert userDao != null;
            User user = new User(login, password, 2);

            Student student = new Student(dictionaryDao.read(firstName).getId()
                    , dictionaryDao.read(lastName).getId(), dictionaryDao.read(patronymic).getId()
                    , email, phone, false);

            // creating new Student
            userDao.signUpStudent(user, student);

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_STUDENT_CREATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_STUDENT_CREATE_ERROR, e);
        }
        LOG.debug("Service finish");
    }
}
