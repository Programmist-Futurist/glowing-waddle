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
 * Service for works with update operations in DB.
 *
 * @author K.Levchenko
 */
public class UpdateService extends Service {

    private static final long serialVersionUID = -5770675106860643570L;

    private static final Logger LOG = Logger.getLogger(UpdateService.class);

    private static UpdateService instance;

    private UpdateService() {
    }

    public static synchronized UpdateService getInstance() {
        if (instance == null) {
            instance = new UpdateService();
            LOG.debug("Service initialized");
        }
        return instance;
    }

    // methods

    /**
     * Updates dictionary entities in DB by list of dictionaries
     *
     * @param dictionaryList list of dictionary objects needed to
     *                       be updated in DB
     */
    public void updateDictionaries(List<Dictionary> dictionaryList) throws ServiceException {
        LOG.debug("Service starts");

        try {
            for (Dictionary dictionary : dictionaryList) {
                if (dictionary != null) {
                    dictionaryDao.update(dictionary);
                    LOG.trace("Dictionary successfully updated in DataBase");
                }
            }
        } catch (DBException e) {
            LOG.error(Messages.SERVICE_DICTIONARY_UPDATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_DICTIONARY_UPDATE_ERROR, e);
        }
        LOG.debug("Service finish");
    }

    /**
     * Updates course by parameters required
     *
     * @param parameters map of parameter keys and their values
     *                   by which course will be updated in DB
     */
    public void editCourse(Map<String, String> parameters) throws ServiceException {
        LOG.debug("Service starts");

        try {
            String courseTeacherId = parameters.get("courseTeacherId");
            String courseTopicId = parameters.get("courseTopicId");
            String courseName = parameters.get("courseName");
            String courseNameRus = parameters.get("courseNameRus");
            String courseDescription = parameters.get("courseDescription");
            String courseDescriptionRus = parameters.get("courseDescriptionRus");
            String courseStartDate = parameters.get("courseStartDate");
            String courseEndDate = parameters.get("courseEndDate");
            String courseId = parameters.get("courseId");

            Course courseToUpdate = courseDao.read(Integer.parseInt(courseId));
            Dictionary dictionary;

            // Date classes needed to format date
            SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
            in.setLenient(false);
            Calendar calendar = new GregorianCalendar();

            // preparing course to update in DB
            if (courseName != null && !courseName.isEmpty()) {
                dictionary = dictionaryDao.read(courseToUpdate.getNameId());
                dictionary.setEng(courseName);
                dictionary.setRus(courseNameRus);
                dictionaryDao.update(dictionary);
            }
            if (courseDescription != null && !courseDescription.isEmpty()) {
                dictionary = dictionaryDao.read(courseToUpdate.getDescriptionId());
                dictionary.setEng(courseDescription);
                dictionary.setRus(courseDescriptionRus);
                dictionaryDao.update(dictionary);
            }
            if (courseTopicId != null && !courseTopicId.isEmpty()) {
                int topicId = Integer.parseInt(courseTopicId);
                courseToUpdate.setTopicId(topicId);
            }
            if (courseTeacherId != null && !courseTeacherId.isEmpty()) {
                int teacherId = Integer.parseInt(courseTeacherId);
                courseToUpdate.setTeacherId(teacherId);
            }
            if (courseStartDate != null && !courseStartDate.isEmpty()) {
                Date startDateDayBefore = in.parse(courseStartDate);
                calendar.setTime(startDateDayBefore);
                calendar.add(Calendar.DATE, 1);
                Date startDate = calendar.getTime();

                courseToUpdate.setStartDate(startDate);
            }
            if (courseEndDate != null && !courseEndDate.isEmpty()) {
                Date endDateDayBefore = in.parse(courseEndDate);
                calendar.setTime(endDateDayBefore);
                calendar.add(Calendar.DATE, 1);
                Date endDate = calendar.getTime();

                courseToUpdate.setEndDate(endDate);
            }
            //updating course in DB
            courseDao.update(courseToUpdate);

            LOG.trace("Course successfully updated in DataBase");

        } catch (DBException | ParseException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_COURSE_UPDATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_COURSE_UPDATE_ERROR, e);
        }
        LOG.debug("Service finish");
    }

    /**
     * Updates course's teacher by parameters required
     *
     * @param parameters map of parameter keys and their values
     *                   by which course's teacher will be updated in DB
     */
    public void teacherCourseUpdate(Map<String, String> parameters) throws ServiceException {
        LOG.debug("Service starts");

        try {
            String teacherId = parameters.get("teacherId");
            String courseId = parameters.get("courseId");

            // setting new teacher for the course (if not null)
            Course course = courseDao.read(Integer.parseInt(courseId));
            course.setTeacherId(Integer.parseInt(teacherId));
            courseDao.update(course);
            LOG.trace("Course successfully updated in DataBase");
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_TEACHER_FOR_COURSE_UPDATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_TEACHER_FOR_COURSE_UPDATE_ERROR, e);
        }
        LOG.debug("Service finish");
    }

    /**
     * Updates journal entity, especially mark for some student
     *
     * @param parameters map of parameter keys and their values
     *                   by which journal entity will be updated
     *                   in DB
     */
    public void updateJournalMark(Map<String, String> parameters) throws ServiceException {
        LOG.debug("Service starts");

        try {
            String journalId = parameters.get("journalId");
            String mark = parameters.get("mark");

            // updating journal entity (changing student mark in particular journal)
            JournalEntity journalEntityToUpdate = journalEntityDao.read(Integer.parseInt(journalId));
            journalEntityToUpdate.setMark(Integer.parseInt(mark));
            journalEntityDao.update(journalEntityToUpdate);
            LOG.trace("Journal entity successfully updated in DataBase");
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_STUDENT_JOURNAL_MARK_UPDATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_STUDENT_JOURNAL_MARK_UPDATE_ERROR, e);
        }
        LOG.debug("Service finish");

    }

    /**
     * Blocks particular student by id
     *
     * @param studentId id of particular student, the value by which
     *                  student will be found and blocked in DB
     */
    public void blockStudent(int studentId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // blocking Student
            Student studentToBlock = studentDao.read(studentId);
            studentToBlock.setBlock(true);
            studentDao.update(studentToBlock);

            // list of journal entities of blocked student
            List<JournalEntity> studentJournalEntities
                    = journalEntityDao.getAllByStudentId(studentId);

            // deleting student in all journals
            for (JournalEntity journalEntity : studentJournalEntities) {
                journalEntityDao.delete(journalEntity.getId());
            }
            LOG.trace("Student successfully blocked in DataBase");
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_STUDENT_BLOCK_ERROR, e);
            throw new ServiceException(Messages.SERVICE_STUDENT_BLOCK_ERROR, e);
        }
        LOG.debug("Service finish");
    }

    /**
     * Unblocks particular student by id
     *
     * @param studentId id of particular student, the value, by which
     *                  student will be found and unblocked in DB
     */
    public void unblockStudent(int studentId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // Unblocking student
            Student studentToBlock = studentDao.read(studentId);
            studentToBlock.setBlock(false);
            studentDao.update(studentToBlock);
            LOG.trace("Student successfully unblocked in DataBase");
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(
                    Messages.SERVICE_STUDENT_UNBLOCK_ERROR, e);
            throw new ServiceException(Messages.SERVICE_STUDENT_UNBLOCK_ERROR, e);
        }
        LOG.debug("Service finish");
    }

    /**
     * Updates particular student by id and map of parameters
     *
     * @param userId     id of particular student, the value, by which
     *                   student will be found and unblocked in DB
     * @param parameters the map of parameters that a needed to update
     *                   student in DB
     */
    public void updateStudent(int userId, Map<String, String> parameters) throws ServiceException {
        LOG.debug("Service starts");
        try {
            String login = parameters.get("login");
            String password = parameters.get("password");
            String firstName = parameters.get("firstName");
            String lastName = parameters.get("lastName");
            String patronymic = parameters.get("patronymic");
            String email = parameters.get("email");
            String phone = parameters.get("phone");

            User user = userDao.read(userId);
            Student student = studentDao.read(user.getId());


            // preparing Dictionaries
            List<Dictionary> dictionaries = new ArrayList<>();

            int wordId;
            if (firstName != null && !firstName.isEmpty()) {
                wordId = student.getFirstNameId();
                dictionaries.add(new Dictionary(wordId, firstName, firstName));
            }
            if (lastName != null && !lastName.isEmpty()) {
                wordId = student.getLastNameId();
                dictionaries.add(new Dictionary(wordId, lastName, lastName));
            }
            if (patronymic != null && !patronymic.isEmpty()) {
                wordId = student.getPatronymicId();
                dictionaries.add(new Dictionary(wordId, patronymic, patronymic));
            }

            //creating dictionaries
            updateDictionaries(dictionaries);

            // setting updated fields for User
            if (login != null && !login.isEmpty()) {
                user.setLogin(login);
            }
            if (password != null && !password.isEmpty()) {
                user.setPassword(password);
            }
            LOG.trace("Dictionary successfully updated in DataBase");

            // updating student in DB
            userDao.update(user);
            LOG.trace("User successfully updated in DataBase");

            // setting updated fields for Student
            student.setEmail(email);
            student.setPhone(phone);

            // updating student in DB
            studentDao.update(student);
            LOG.trace("Student  successfully updated in DataBase");
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_STUDENT_UPDATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_STUDENT_UPDATE_ERROR, e);
        }
        LOG.debug("Service starts");
    }

    /**
     * Updates particular teacher by id and map of parameters
     *
     * @param userId     id of particular teacher, the value, by which
     *                   teacher will be found and unblocked in DB
     * @param parameters the map of parameters that a needed to update
     *                   teacher in DB
     */
    public void updateTeacher(int userId, Map<String, String> parameters) throws ServiceException {
        try {
            String login = parameters.get("login");
            String password = parameters.get("password");
            String firstName = parameters.get("firstName");
            String lastName = parameters.get("lastName");
            String patronymic = parameters.get("patronymic");
            String experience = parameters.get("experience");
            String firstNameRus = parameters.get("firstNameRus");
            String lastNameRus = parameters.get("lastNameRus");
            String patronymicRus = parameters.get("patronymicRus");
            String experienceRus = parameters.get("experienceRus");
            String email = parameters.get("email");
            String phone = parameters.get("phone");

            User user = userDao.read(userId);
            Teacher teacher = teacherDao.read(user.getId());


            // updating Dictionary
            List<Dictionary> dictionaries = new ArrayList<>();
            int wordId;
            if (!firstName.isEmpty()) {
                wordId = teacher.getFirstNameId();
                dictionaries.add(new Dictionary(wordId, firstName, firstNameRus));
            }
            if (!lastName.isEmpty()) {
                wordId = teacher.getLastNameId();
                dictionaries.add(new Dictionary(wordId, lastName, lastNameRus));
            }
            if (!patronymic.isEmpty()) {
                wordId = teacher.getPatronymicId();
                dictionaries.add(new Dictionary(wordId, patronymic, patronymicRus));
            }
            if (!experience.isEmpty()) {
                wordId = teacher.getExperienceId();
                dictionaries.add(new Dictionary(wordId, experience, experienceRus));
            }

            //updating dictionaries
            updateDictionaries(dictionaries);

            // setting updated fields for User
            if (login != null && !login.isEmpty()) {
                user.setLogin(login);
            }
            if (password != null && !password.isEmpty()) {
                user.setPassword(password);
            }
            LOG.trace("Dictionary successfully updated in DataBase");

            // updating user in DB
            userDao.update(user);
            LOG.trace("User successfully updated in DataBase");

            // setting updated fields for Teacher
            teacher.setEmail(email);
            teacher.setPhone(phone);

            // updating teacher in DB
            teacherDao.update(teacher);
            LOG.trace("User successfully updated in db");

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_TEACHER_UPDATE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_TEACHER_UPDATE_ERROR, e);
        }
    }
}
