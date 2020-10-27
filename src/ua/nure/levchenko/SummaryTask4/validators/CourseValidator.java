package ua.nure.levchenko.SummaryTask4.validators;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.Course;
import ua.nure.levchenko.SummaryTask4.entity.Dictionary;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.ValidationException;
import ua.nure.levchenko.SummaryTask4.internationalization.Language;
import ua.nure.levchenko.SummaryTask4.validators.constants.Messages;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates parameters before all operations with Course entity.
 *
 * @author K.Levchenko
 */
public class CourseValidator extends Validator {

    private static final long serialVersionUID = -4981835883067596686L;

    private static final Logger LOG = Logger.getLogger(CourseValidator.class);

    private static CourseValidator instance;
    ResourceBundle resourceBundle;

    {
        resourceBundle = ResourceBundle.getBundle("resources", new Locale("en", "US"));
    }

    private CourseValidator() {
    }

    public static synchronized CourseValidator getInstance() {
        if (instance == null) {
            instance = new CourseValidator();
            LOG.debug("Validator initialized");
        }
        return instance;
    }

    // methods

    /**
     * Validates input course parameters before creating it in DB
     *
     * @param userId     id of the current user
     * @param parameters map of parameters that must be validated before
     *                   creating course in DB, to prevent our program on unexpected errors.
     */
    public String validateOnCreate(int userId, Map<String, String> parameters) throws ValidationException {
        LOG.debug("Validator starts");

        String errorMessage = "";
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);
            if (userLanguage.equals(Language.RUS)) {
                resourceBundle = ResourceBundle.getBundle("resources", new Locale("ru", "Ru"));
            } else {
                resourceBundle = ResourceBundle.getBundle("resources", new Locale("en", "US"));
            }


            String courseTeacherId = parameters.get("courseTeacherId");
            String courseTopicId = parameters.get("courseTopicId");
            String courseName = parameters.get("courseName");
            String courseNameRus = parameters.get("courseNameRus");
            String courseDescription = parameters.get("courseDescription");
            String courseDescriptionRus = parameters.get("courseDescriptionRus");
            String courseStartDate = parameters.get("courseStartDate");
            String courseEndDate = parameters.get("courseEndDate");

            // getting nameId of the course by eng word
            Dictionary dictionaryEng = dictionaryDao.read(courseName);
            Course courseWithTheSameNameEng = null;
            if (dictionaryEng != null) {
                int nameIdEng = dictionaryEng.getId();
                courseWithTheSameNameEng = courseDao.readByNameId(nameIdEng);
            }

            // getting nameId of the course by rus word
            Dictionary dictionaryRus = dictionaryDao.read(courseName);
            Course courseWithTheSameNameRus = null;
            if (dictionaryRus != null) {
                int nameIdEng = dictionaryRus.getId();
                courseWithTheSameNameRus = courseDao.readByNameId(nameIdEng);
            }

            // checking for the correct name format
            Pattern patternName = Pattern.compile("(^\\w+$)");
            Matcher matcherName = patternName.matcher(courseName);
            Matcher matcherNameRus = patternName.matcher(courseNameRus);

            if (courseTeacherId == null || courseTeacherId.isEmpty() || courseTopicId == null
                    || courseTopicId.isEmpty() || courseName.isEmpty() || courseDescription == null
                    || courseDescription.isEmpty() || courseStartDate == null || courseStartDate.isEmpty()
                    || courseEndDate == null || courseEndDate.isEmpty() || courseNameRus.isEmpty()
                    || courseDescriptionRus == null || courseDescriptionRus.isEmpty()) {
                LOG.error("Message for User --> Fields should not be empty. Fill in all the fields.");
                errorMessage = resourceBundle.getString("course_validator.empty_fields_exception");
            } else if (courseWithTheSameNameEng != null || courseWithTheSameNameRus != null) {

                // check if the course with such name exist
                LOG.error("Message for User --> Course with such name has already exist.");
                errorMessage = resourceBundle.getString("course_validator.the_same_course_name");
            } else if (!matcherName.find()) {
                // check if the course with such name exist
                LOG.error("Message for User --> Incorrect course name.");
                errorMessage = resourceBundle.getString("course_validator.incorrect_course_name");
            } else if (!matcherNameRus.find()) {
                // check if the course with such name exist
                LOG.error("Message for User --> Incorrect course name.");
                errorMessage = resourceBundle.getString("course_validator.incorrect_course_name");
            } else {

                // classes for work with date
                SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = new GregorianCalendar();
                Date startDate;
                Date endDate;

                Date startDateDayBefore = in.parse(courseStartDate);
                calendar.setTime(startDateDayBefore);
                calendar.add(Calendar.DATE, 1);
                startDate = calendar.getTime();

                Date endDateDayBefore = in.parse(courseEndDate);
                calendar.setTime(endDateDayBefore);
                calendar.add(Calendar.DATE, 1);
                endDate = calendar.getTime();

                // check if startDate is bigger than endDate
                if (endDate.compareTo(startDate) < 0) {
                    LOG.error("Incorrect date putted");
                    errorMessage = resourceBundle.getString("course_validator.start_date_is_bigger_than_end_date");
                }
            }

        } catch (NullPointerException | ClassCastException | DBException |
                ParseException e) {
            LOG.error(Messages.VALIDATION_ERROR, e);
            throw new ValidationException(Messages.VALIDATION_ERROR, e);
        }
        LOG.debug("Validator finish");

        return errorMessage;
    }


    /**
     * Validates input course parameters before deleting it in DB
     *
     * @param userId     id of current user
     * @param parameters map of parameters that must be validated before
     *                   deleting course in DB, to prevent our program on unexpected errors.
     */
    public String validateOnDelete(int userId, Map<String, String> parameters) throws ValidationException {
        LOG.debug("Validator starts");
        String errorMessage = "";
        try {

            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);
            if (userLanguage.equals(Language.RUS)) {
                resourceBundle = ResourceBundle.getBundle("resources", new Locale("ru", "Ru"));
            } else {
                resourceBundle = ResourceBundle.getBundle("resources", new Locale("en", "US"));
            }

            String courseId = parameters.get("courseId");
            if (courseId != null) {
                if (courseDao.read(Integer.parseInt(courseId)) == null) {
                    courseDao.delete(Integer.parseInt(courseId));
                    LOG.error("Wrong courseId. You need to check the correctness of program code while deleting user!");
                    errorMessage = resourceBundle.getString("course_validator.course_do_not_exist");
                }
            }
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.VALIDATION_ERROR, e);
            throw new ValidationException(Messages.VALIDATION_ERROR, e);
        }
        LOG.debug("Validator finish");
        return errorMessage;
    }

    /**
     * Validates input course parameters before updating it in DB
     *
     * @param userId     id of current user
     * @param parameters map of parameters that must be validated before
     *                   updating course in DB, to prevent our program on unexpected errors.
     */
    public String validateOnUpdate(int userId, Map<String, String> parameters) throws ValidationException {
        LOG.debug("Validator starts");
        String errorMessage = "";
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);
            if (userLanguage.equals(Language.RUS)) {
                resourceBundle = ResourceBundle.getBundle("resources", new Locale("ru", "Ru"));
            } else {
                resourceBundle = ResourceBundle.getBundle("resources", new Locale("en", "US"));
            }

            String courseName = parameters.get("courseName");
            String courseNameRus = parameters.get("courseNameRus");
            String courseDescription = parameters.get("courseDescription");
            String courseDescriptionRus = parameters.get("courseDescriptionRus");
            String courseStartDate = parameters.get("courseStartDate");
            String courseEndDate = parameters.get("courseEndDate");
            String courseId = parameters.get("courseId");

            if (((courseName != null && !courseName.isEmpty()) && (courseNameRus == null
                    || courseNameRus.isEmpty())) || (courseName == null || courseName.isEmpty())
                    && (courseNameRus != null && !courseNameRus.isEmpty())) {
                LOG.error("Message for user --> Both fields should be filled (Name)");
                errorMessage = resourceBundle.getString("course_validator.not_filled_name_fields");
            } else if (((courseDescription != null && !courseDescription.isEmpty())
                    && (courseDescriptionRus == null || courseDescriptionRus.isEmpty()))
                    || (courseDescription == null || courseDescription.isEmpty())
                    && (courseDescriptionRus != null && !courseDescriptionRus.isEmpty())) {
                LOG.error("Message for user --> Both fields should be filled (Description)");
                errorMessage = resourceBundle.getString("course_validator.not_filled_description_fields");
            } else if (dictionaryDao.read(courseName) != null) {
                // getting nameId of the course
                int nameId = dictionaryDao.read(courseName).getId();
                Course courseWithTheSameName = courseDao.readByNameId(nameId);

                // check if the course with such name exist
                if (courseWithTheSameName != null
                        && Integer.parseInt(courseId) != courseWithTheSameName.getId()) {
                    LOG.error("Message for User --> Course with such name has already exist.");
                    errorMessage = resourceBundle.getString("course_validator.course_with_the_same_name");
                }
            } else {
                // classes for work with date
                SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = new GregorianCalendar();
                Date startDate = null;
                Date endDate = null;

                if (courseStartDate != null && !courseStartDate.isEmpty()) {
                    Date startDateDayBefore = in.parse(courseStartDate);
                    calendar.setTime(startDateDayBefore);
                    calendar.add(Calendar.DATE, 1);
                    startDate = calendar.getTime();
                }
                if (courseEndDate != null && !courseEndDate.isEmpty()) {
                    Date endDateDayBefore = in.parse(courseEndDate);
                    calendar.setTime(endDateDayBefore);
                    calendar.add(Calendar.DATE, 1);
                    endDate = calendar.getTime();

                }
                if (startDate != null && endDate != null && endDate.compareTo(startDate) < 0) {
                    LOG.error("Incorrect date putted");
                    errorMessage = resourceBundle.getString("course_validator.start_date_is_bigger_than_end_date");
                }
            }
        } catch (DBException | NullPointerException | ClassCastException | ParseException e) {
            LOG.error(Messages.VALIDATION_ERROR, e);
            throw new ValidationException(Messages.VALIDATION_ERROR, e);
        }
        LOG.debug("Validator finish");
        return errorMessage;
    }

    /**
     * Validates input course parameters before setting new teacher for course
     *
     * @param parameters map of parameters that must be validated before
     *                   setting new teacher for course, to prevent our program
     *                   on unexpected errors.
     */
    public String validateOnConnect(Map<String, String> parameters) throws ValidationException {
        LOG.debug("Validator starts");
        String errorMessage = "";
        try {

            String courseId = parameters.get("courseId");
            String teacherId = parameters.get("teacherId");

            if (teacherId == null && courseId == null) {
                throw new NullPointerException();
            }

        } catch (NullPointerException | ClassCastException e) {
            LOG.error(Messages.VALIDATION_ERROR, e);
            throw new ValidationException(Messages.VALIDATION_ERROR, e);
        }
        LOG.debug("Validator finish");
        return errorMessage;
    }
}
