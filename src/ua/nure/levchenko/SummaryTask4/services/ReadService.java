package ua.nure.levchenko.SummaryTask4.services;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.*;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.internationalization.Language;
import ua.nure.levchenko.SummaryTask4.services.constants.EntityFields;
import ua.nure.levchenko.SummaryTask4.services.constants.Messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for working with read operations in DB.
 *
 * @author K.Levchenko
 */
public class ReadService extends Service {

    private static final long serialVersionUID = 540636270441481972L;

    private static final Logger LOG = Logger.getLogger(ReadService.class);

    private static ReadService instance;
    int currentUserId;

    private ReadService() {
    }

    public static synchronized ReadService getInstance() {
        if (instance == null) {
            instance = new ReadService();
            LOG.debug("Service initialized");
        }
        return instance;
    }


    //methods

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the current user
     * @return list of maps with internationalized values of fields
     * of all course entities from the Dictionary
     */
    public List<Map<String, String>> getAllCourses(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<Course> allCourses = courseDao.getAll();
            List<Map<String, String>> coursesCorrectView = new ArrayList<>();
            for (Course course : allCourses) {
                coursesCorrectView.add(courseToCorrectView(userId, course.getId()));
            }
            LOG.trace("Courses successfully got from DataBase");
            LOG.debug("Service finish");
            return coursesCorrectView;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId the id of the teacher whose courses needed to find
     * @return list of maps with internationalized values of fields
     * of all course entities of some teacher from the Dictionary
     */
    public List<Map<String, String>> getAllCoursesOfTeacher(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<Course> allCourses = courseDao.getAllByTeacher(userId);
            List<Map<String, String>> coursesCorrectView = new ArrayList<>();
            for (Course course : allCourses) {
                coursesCorrectView.add(courseToCorrectView(userId, course.getId()));
            }
            LOG.trace("Courses of current teacher successfully got from DataBase");

            LOG.debug("Service finish");
            return coursesCorrectView;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }


    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId the id of the student whose courses needed to find
     * @return list of maps with internationalized values of fields
     * of all course entities of some teacher from the Dictionary
     */
    public List<Map<String, String>> getAllCoursesOfStudent(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<Course> allCourses = courseDao.getAll();
            List<JournalEntity> journalEntitiesOfStudent = journalEntityDao.getAllByStudentId(userId);

            List<Map<String, String>> coursesCorrectView = new ArrayList<>();

            for (Course course : allCourses) {
                for (JournalEntity journalEntity : journalEntitiesOfStudent) {
                    if (course.getId() == journalEntity.getCourseId()) {
                        coursesCorrectView.add(courseToCorrectView(userId, course.getId()));
                    }
                }
            }
            LOG.trace("Courses of current student successfully got from DataBase");

            LOG.debug("Service finish");
            return coursesCorrectView;
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }


    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the current user
     * @return list of maps with internationalized values of fields
     * of all teacher entities from the Dictionary
     */
    public List<Map<String, String>> getAllTeachers(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<Teacher> allTeachers = teacherDao.getAll();
            List<Map<String, String>> teachersCorrectView = new ArrayList<>();
            for (Teacher teacher : allTeachers) {
                teachersCorrectView.add(teacherToCorrectView(teacher.getId()));
            }
            LOG.trace("Teachers successfully got from DataBase");

            LOG.debug("Service finish");
            return teachersCorrectView;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the current user
     * @return list of maps with internationalized values of fields
     * of all student entities from the Dictionary
     */
    public List<Map<String, String>> getAllStudents(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<Student> allStudents = studentDao.getAll();
            List<Map<String, String>> studentsCorrectView = new ArrayList<>();
            for (Student student : allStudents) {
                studentsCorrectView.add(studentToCorrectView(student.getId()));
            }
            LOG.trace("Student successfully got from DataBase");

            LOG.debug("Service finish");
            return studentsCorrectView;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the current user
     * @return list of maps with internationalized values of fields
     * of all blocked student entities from the Dictionary
     */
    public List<Map<String, String>> getAllBlockedStudents(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<Student> allStudents = studentDao.getAll();

            List<Map<String, String>> allBlockedStudents = new ArrayList<>();

            for (Student student : allStudents) {
                if (student.isBlock()) {
                    allBlockedStudents.add(studentToCorrectView(student.getId()));
                }
            }
            LOG.trace("Blocked students successfully got from DataBase");

            LOG.debug("Service finish");
            return allBlockedStudents;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the current user
     * @return list of maps with internationalized values of fields
     * of all unblocked student entities from the Dictionary
     */
    public List<Map<String, String>> getAllUnblockedStudents(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<Student> allStudents = studentDao.getAll();

            List<Map<String, String>> allUnblockedStudents = new ArrayList<>();

            for (Student student : allStudents) {
                if (!student.isBlock()) {
                    allUnblockedStudents.add(studentToCorrectView(student.getId()));
                }
            }
            LOG.trace("Unblocked students successfully got from DataBase");

            LOG.debug("Service finish");
            return allUnblockedStudents;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }


    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the current user
     * @return list of maps with internationalized values of fields
     * of all topic entities from the Dictionary
     */
    public List<Map<String, String>> getAllTopics(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<Topic> allTopics = topicDao.getAll();
            List<Map<String, String>> topicsCorrectView = new ArrayList<>();
            for (Topic topic : allTopics) {
                topicsCorrectView.add(topicToCorrectView(userId, topic.getId()));
            }
            LOG.trace("Topics successfully got from DataBase");
            LOG.debug("Service finish");
            return topicsCorrectView;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the current user
     * @return list of maps with internationalized values of fields
     * of all status entities from the Dictionary
     */
    public List<Map<String, String>> getAllStatuses(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<Status> allStatuses = statusDao.getAll();
            List<Map<String, String>> statusesCorrectView = new ArrayList<>();
            for (Status status : allStatuses) {
                statusesCorrectView.add(statusToCorrectView(userId, status.getId()));
            }
            LOG.trace("Statuses successfully got from DataBase");
            LOG.debug("Service finish");
            return statusesCorrectView;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the current user
     * @return list of maps with internationalized values of fields
     * of all role entities from the Dictionary
     */
    public List<Map<String, String>> getAllRoles(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<Role> allRoles = roleDao.getAll();
            List<Map<String, String>> rolesCorrectView = new ArrayList<>();
            for (Role role : allRoles) {
                rolesCorrectView.add(roleToCorrectView(userId, role.getId()));
            }
            LOG.trace("Roles successfully got from DataBase");
            LOG.debug("Service finish");
            return rolesCorrectView;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the current user
     * @return list of maps with internationalized values of fields
     * of all journal entities from the Dictionary
     */
    public List<Map<String, String>> getAllJournalEntities(int userId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<JournalEntity> allJournalEntities = journalEntityDao.getAll();
            List<Map<String, String>> journalEntitiesCorrectView = new ArrayList<>();
            for (JournalEntity journalEntity : allJournalEntities) {
                journalEntitiesCorrectView.add(journalEntityToCorrectView(userId, journalEntity.getId()));
            }
            LOG.trace("Journal entities successfully got from DataBase");
            LOG.debug("Service finish");
            return journalEntitiesCorrectView;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId   id of the current user
     * @param courseId the id of the course which journal entities
     *                 needed to find
     * @return list of maps with internationalized values of fields
     * of all journal entities of current course from the Dictionary
     */
    public List<Map<String, String>> getAllJournalEntitiesByCourse(int userId, int courseId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            List<JournalEntity> allJournalEntities = journalEntityDao.getAll();
            List<Map<String, String>> journalEntitiesCorrectView = new ArrayList<>();
            for (JournalEntity journalEntity : allJournalEntities) {
                if (courseId == journalEntity.getCourseId()) {
                    journalEntitiesCorrectView.add(journalEntityToCorrectView(userId, journalEntity.getId()));
                }
            }
            LOG.trace("Journal entities of some course successfully got from DataBase");
            LOG.debug("Service finish");
            return journalEntitiesCorrectView;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }


    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId   id of the current user
     * @param courseId id of the course that we need to process to needed view.
     * @return map with internationalized values from the Dictionary
     */
    public Map<String, String> courseToCorrectView(int userId, int courseId) throws ServiceException {
        LOG.debug("Starting to form course to correct view");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            Course currentCourse = courseDao.read(courseId);

            HashMap<String, String> names = new HashMap<>();
            if (currentCourse != null) {
                String id = String.valueOf(currentCourse.getId());
                names.put(EntityFields.ID, id);

                String nameId = String.valueOf(currentCourse.getNameId());
                names.put(EntityFields.COURSE_NAME_ID, nameId);

                String name = dictionaryDao.read(currentCourse.getNameId()).getWord();
                names.put(EntityFields.COURSE_NAME, name);

                String nameEng = dictionaryDao.read(currentCourse.getNameId()).getEng();
                names.put(EntityFields.COURSE_NAME_ENG, nameEng);

                String nameRus = dictionaryDao.read(currentCourse.getNameId()).getRus();
                names.put(EntityFields.COURSE_NAME_RUS, nameRus);

                String descriptionId = String.valueOf(currentCourse.getDescriptionId());
                names.put(EntityFields.COURSE_DESCRIPTION_ID, descriptionId);

                String description = dictionaryDao.read(currentCourse.getDescriptionId()).getWord();
                names.put(EntityFields.COURSE_DESCRIPTION, description);

                String descriptionEng = dictionaryDao.read(currentCourse.getDescriptionId()).getEng();
                names.put(EntityFields.COURSE_DESCRIPTION_ENG, descriptionEng);

                String descriptionRus = dictionaryDao.read(currentCourse.getDescriptionId()).getRus();
                names.put(EntityFields.COURSE_DESCRIPTION_RUS, descriptionRus);

                String startDate = currentCourse.getStartDate().toString();
                names.put(EntityFields.COURSE_START_DATE, startDate);

                String endDate = currentCourse.getEndDate().toString();
                names.put(EntityFields.COURSE_END_DATE, endDate);

                String durationInMonths = String.valueOf(currentCourse.getDurationInMonths());
                names.put(EntityFields.COURSE_DURATION_IN_MONTHS, durationInMonths);

                String topicId = String.valueOf(currentCourse.getTopicId());
                names.put(EntityFields.COURSE_TOPIC_ID, topicId);


                String topicName = dictionaryDao.read(topicDao.read(currentCourse.getTopicId()).getNameId()).getWord();
                names.put(EntityFields.COURSE_TOPIC_NAME, topicName);

                String statusId = String.valueOf(currentCourse.getStatusId());
                names.put(EntityFields.COURSE_STATUS_ID, statusId);

                String statusName = dictionaryDao.read(statusDao.read(currentCourse.getStatusId()).getNameId()).getWord();
                names.put(EntityFields.COURSE_STATUS_NAME, statusName);

                String teacherId = String.valueOf(currentCourse.getTeacherId());
                names.put(EntityFields.COURSE_TEACHER_ID, teacherId);

                String teacherName = teacherNameCorrectView(teacherDao.read(currentCourse.getTeacherId()));
                names.put(EntityFields.COURSE_TEACHER_NAME, teacherName);

                String studentAmount = String.valueOf(journalEntityDao.getAllByCourseId(currentCourse.getId()).size());
                names.put(EntityFields.COURSE_STUDENT_AMOUNT, studentAmount);
            }
            LOG.trace("Course successfully got from DataBase with the needed view");

            LOG.debug("Finish to form course to correct view");
            return names;

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the teacher that we need to process to needed view.
     * @return map with internationalized values from the Dictionary
     */
    public Map<String, String> teacherToCorrectView(int userId) throws ServiceException {
        LOG.debug("Starting to form teacher to correct view");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            Teacher currentTeacher = teacherDao.read(userId);

            HashMap<String, String> names = new HashMap<>();
            if (currentTeacher != null) {
                String id = String.valueOf(currentTeacher.getId());
                names.put(EntityFields.ID, id);

                String fullName = teacherNameCorrectView(currentTeacher);
                names.put(EntityFields.TEACHER_FULL_NAME, fullName);

                String firstNameId = String.valueOf(currentTeacher.getFirstNameId());
                names.put(EntityFields.TEACHER_FIRST_NAME_ID, firstNameId);

                String firstName = dictionaryDao.read(currentTeacher.getFirstNameId()).getWord();
                names.put(EntityFields.TEACHER_FIRST_NAME, firstName);

                String firstNameEng = dictionaryDao.read(currentTeacher.getFirstNameId()).getEng();
                names.put(EntityFields.TEACHER_FIRST_NAME_ENG, firstNameEng);

                String firstNameRus = dictionaryDao.read(currentTeacher.getFirstNameId()).getRus();
                names.put(EntityFields.TEACHER_FIRST_NAME_RUS, firstNameRus);

                String lastNameId = String.valueOf(currentTeacher.getLastNameId());
                names.put(EntityFields.TEACHER_LAST_NAME_ID, lastNameId);

                String lastName = dictionaryDao.read(currentTeacher.getLastNameId()).getWord();
                names.put(EntityFields.TEACHER_LAST_NAME, lastName);

                String lastNameEng = dictionaryDao.read(currentTeacher.getLastNameId()).getEng();
                names.put(EntityFields.TEACHER_LAST_NAME_ENG, lastNameEng);

                String lastNameRus = dictionaryDao.read(currentTeacher.getLastNameId()).getRus();
                names.put(EntityFields.TEACHER_LAST_NAME_RUS, lastNameRus);

                String patronymicId = String.valueOf(currentTeacher.getPatronymicId());
                names.put(EntityFields.TEACHER_PATRONYMIC_ID, patronymicId);

                String patronymic = dictionaryDao.read(currentTeacher.getPatronymicId()).getWord();
                names.put(EntityFields.TEACHER_PATRONYMIC, patronymic);

                String patronymicEng = dictionaryDao.read(currentTeacher.getPatronymicId()).getEng();
                names.put(EntityFields.TEACHER_PATRONYMIC_ENG, patronymicEng);

                String patronymicRus = dictionaryDao.read(currentTeacher.getPatronymicId()).getRus();
                names.put(EntityFields.TEACHER_PATRONYMIC_RUS, patronymicRus);

                String experienceId = String.valueOf(currentTeacher.getPatronymicId());
                names.put(EntityFields.TEACHER_EXPERIENCE_ID, experienceId);

                String experience = dictionaryDao.read(currentTeacher.getExperienceId()).getWord();
                names.put(EntityFields.TEACHER_EXPERIENCE, experience);

                String experienceEng = dictionaryDao.read(currentTeacher.getExperienceId()).getEng();
                names.put(EntityFields.TEACHER_EXPERIENCE_ENG, experienceEng);

                String experienceRus = dictionaryDao.read(currentTeacher.getExperienceId()).getRus();
                names.put(EntityFields.TEACHER_EXPERIENCE_RUS, experienceRus);

                String email = currentTeacher.getEmail();
                names.put(EntityFields.TEACHER_EMAIL, email);

                String phone = currentTeacher.getPhone();
                names.put(EntityFields.TEACHER_PHONE, phone);

                String clazz = Teacher.class.getSimpleName();
                names.put(EntityFields.USER_CLASS, clazz);

            }
            LOG.debug("Finish to form teacher to correct view");
            return names;
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param login login of the user that we need to process to needed view.
     * @return map with internationalized values from the Dictionary
     */
    public Map<String, String> userToCorrectView(String login) throws ServiceException {
        LOG.debug("Starting to form user to correct view");

        int userId = 0;
        try {
            userId = userDao.read(login).getId();

            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            User currentUser = userDao.read(login);

            HashMap<String, String> names = new HashMap<>();
            if (currentUser != null) {
                String id = String.valueOf(currentUser.getId());
                names.put(EntityFields.ID, id);

                login = currentUser.getLogin();
                names.put(EntityFields.USER_LOGIN, login);

                String password = currentUser.getPassword();
                names.put(EntityFields.USER_PASSWORD, password);

                String roleId = String.valueOf(currentUser.getRoleId());
                names.put(EntityFields.USER_ROLE_ID, roleId);

                int roleNameId = roleDao.read(currentUser.getRoleId()).getNameId();
                String roleName = dictionaryDao.read(roleNameId).getWord();
                names.put(EntityFields.USER_ROLE_NAME, roleName);
            }
            LOG.debug("Finish to form usser to correct view");
            return names;
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the student that we need to process to needed view.
     * @return map with internationalized values from the Dictionary
     */
    public Map<String, String> studentToCorrectView(int userId) throws ServiceException {
        LOG.debug("Starting to form student to correct view");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            Student currentStudent = studentDao.read(userId);

            HashMap<String, String> names = new HashMap<>();
            if (currentStudent != null) {
                String id = String.valueOf(currentStudent.getId());
                names.put(EntityFields.ID, id);

                String fullName = studentNameCorrectView(currentStudent);
                names.put(EntityFields.STUDENT_FULL_NAME, fullName);

                String firstNameId = String.valueOf(currentStudent.getFirstNameId());
                names.put(EntityFields.STUDENT_FIRST_NAME_ID, firstNameId);

                String firstName = dictionaryDao.read(currentStudent.getFirstNameId()).getWord();
                names.put(EntityFields.STUDENT_FIRST_NAME, firstName);

                String lastNameId = String.valueOf(currentStudent.getLastNameId());
                names.put(EntityFields.STUDENT_LAST_NAME_ID, lastNameId);

                String lastName = dictionaryDao.read(currentStudent.getLastNameId()).getWord();
                names.put(EntityFields.STUDENT_LAST_NAME, lastName);

                String patronymicId = String.valueOf(currentStudent.getPatronymicId());
                names.put(EntityFields.STUDENT_PATRONYMIC_ID, patronymicId);

                String patronymic = dictionaryDao.read(currentStudent.getPatronymicId()).getWord();
                names.put(EntityFields.STUDENT_PATRONYMIC, patronymic);

                String email = currentStudent.getEmail();
                names.put(EntityFields.STUDENT_EMAIL, email);

                String phone = currentStudent.getPhone();
                names.put(EntityFields.STUDENT_PHONE, phone);

                String block = String.valueOf(currentStudent.isBlock());
                names.put(EntityFields.STUDENT_BLOCK, block);

                String clazz = Student.class.getSimpleName();
                names.put(EntityFields.USER_CLASS, clazz);
            }
            LOG.debug("Finish to form student to correct view");
            return names;
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId  id of the current user
     * @param topicId id of the topic that we need to process to needed view.
     * @return map with internationalized values from the Dictionary
     */
    public Map<String, String> topicToCorrectView(int userId, int topicId) throws ServiceException {
        LOG.debug("Starting to form topic to correct view");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            Topic currentTopic = topicDao.read(topicId);

            HashMap<String, String> names = new HashMap<>();
            if (currentTopic != null) {
                String id = String.valueOf(currentTopic.getId());
                names.put(EntityFields.ID, id);

                String topicName = dictionaryDao.read(currentTopic.getNameId()).getWord();
                names.put(EntityFields.TOPIC_NAME, topicName);
            }
            LOG.debug("Finish to form topic to correct view");
            return names;
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param statusId id of the status that we need to process to needed view.
     * @return map with internationalized values from the Dictionary
     */
    public Map<String, String> statusToCorrectView(int userId, int statusId) throws ServiceException {
        LOG.debug("Starting to form status to correct view");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            Status currentStatus = statusDao.read(statusId);

            HashMap<String, String> names = new HashMap<>();
            if (currentStatus != null) {
                String id = String.valueOf(currentStatus.getId());
                names.put(EntityFields.ID, id);

                String statusName = dictionaryDao.read(currentStatus.getNameId()).getWord();
                names.put(EntityFields.STATUS_NAME, statusName);
            }
            LOG.debug("Finish to form status to correct view");
            return names;
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }


    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId id of the current user
     * @param roleId id of the role that we need to process to needed view.
     * @return map with internationalized values from the Dictionary
     */
    public Map<String, String> roleToCorrectView(int userId, int roleId) throws ServiceException {
        LOG.debug("Starting to form role to correct view");

        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            Role currentRole = roleDao.read(roleId);

            HashMap<String, String> names = new HashMap<>();
            if (currentRole != null) {
                String id = String.valueOf(currentRole.getId());
                names.put(EntityFields.ID, id);

                String roleName = dictionaryDao.read(currentRole.getNameId()).getWord();
                names.put(EntityFields.ROLE_NAME, roleName);
            }
            LOG.debug("Finish to form role to correct view");
            return names;
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }

    /**
     * Creates list with internationalized values from the Dictionary
     *
     * @param userId          id of the current user
     * @param journalEntityId id of the journalEntity that we need to process to needed view.
     * @return map with internationalized values from the Dictionary
     */
    public Map<String, String> journalEntityToCorrectView(int userId, int journalEntityId) throws ServiceException {
        LOG.debug("Starting to form journal entity to correct view");
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);

            // blocking dictionary language
            if (currentUserId == 0) {
                currentUserId = userId;
                dictionaryDao.setLanguage(userLanguage);
            }

            JournalEntity currentJournalEntity = journalEntityDao.read(journalEntityId);

            HashMap<String, String> names = new HashMap<>();
            if (currentJournalEntity != null) {
                String entityId = String.valueOf(currentJournalEntity.getId());
                names.put(EntityFields.ID, entityId);

                String entityCourseId = String.valueOf(currentJournalEntity.getCourseId());
                names.put(EntityFields.JOURNAL_ENTITY_COURSE_ID, entityCourseId);

                Course course = courseDao.read(currentJournalEntity.getCourseId());
                String entityCourseName = dictionaryDao.read(course.getNameId()).getWord();
                names.put(EntityFields.JOURNAL_ENTITY_COURSE_NAME, entityCourseName);

                String entityStudentId = String.valueOf(currentJournalEntity.getStudentId());
                names.put(EntityFields.JOURNAL_ENTITY_STUDENT_ID, entityStudentId);

                Student student = studentDao.read(currentJournalEntity.getStudentId());
                names.put(EntityFields.JOURNAL_ENTITY_STUDENT_NAME, studentNameCorrectView(student));

                String entityMark = String.valueOf(currentJournalEntity.getMark());
                names.put(EntityFields.JOURNAL_ENTITY_MARK, entityMark);
            }
            LOG.debug("Finish to form journal entity to correct view");
            return names;
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_READ_ENTITY_ERROR, e);
            throw new ServiceException(Messages.SERVICE_READ_ENTITY_ERROR, e);
        } finally {
            // unblocking dictionary lang
            if (currentUserId == userId) {
                currentUserId = 0;
                dictionaryDao.setLanguage(Language.ENG);
            }
        }
    }


    /**
     * forms a suitable appearance for the name of the teacher
     *
     * @param teacher object of Teacher that we need to process
     * @return suitable appearance for the name of the teacher
     */
    private String teacherNameCorrectView(Teacher teacher) throws DBException {
        if (teacher != null) {
            return dictionaryDao.read(teacher.getLastNameId()).getWord().concat(" ")
                    .concat(dictionaryDao.read(teacher.getFirstNameId()).getWord().substring(0, 1).concat(".")
                            .concat(dictionaryDao.read(teacher.getPatronymicId()).getWord().substring(0, 1))
                            .concat("."));
        }
        return null;
    }

    /**
     * forms a suitable appearance for the name of the student
     *
     * @param student object of Teacher that we need to process
     * @return suitable appearance for the name of the student
     */
    private String studentNameCorrectView(Student student) throws DBException {
        if (student != null) {
            return dictionaryDao.read(student.getLastNameId()).getWord().concat(" ")
                    .concat(dictionaryDao.read(student.getFirstNameId()).getWord().substring(0, 1).concat(".")
                            .concat(dictionaryDao.read(student.getPatronymicId()).getWord().substring(0, 1))
                            .concat("."));
        }
        return null;
    }

}
