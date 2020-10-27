package ua.nure.levchenko.SummaryTask4.validators;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.JournalEntity;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.ValidationException;
import ua.nure.levchenko.SummaryTask4.internationalization.Language;
import ua.nure.levchenko.SummaryTask4.validators.constants.Messages;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Validates parameters before all operations with Journal entity.
 *
 * @author K.Levchenko
 */
public class JournalValidator extends Validator {

    private static final long serialVersionUID = -5545760449600104308L;

    private static final Logger LOG = Logger.getLogger(JournalValidator.class);

    private static JournalValidator instance;
    ResourceBundle resourceBundle;

    {
        resourceBundle = ResourceBundle.getBundle("resources", new Locale("en", "US"));
    }

    private JournalValidator() {
    }

    public static synchronized JournalValidator getInstance() {
        if (instance == null) {
            instance = new JournalValidator();
            LOG.debug("Validator initialized");
        }
        return instance;
    }

    // methods

    /**
     * Validates input parameters before creating a new journal entity.
     *
     * @param userId     id of current user
     * @param parameters map of parameters that must be validated before
     *                   creating a new journal entity, to prevent our program
     *                   on unexpected errors.
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

            String courseId = parameters.get("courseId");
            String studentId = parameters.get("studentId");

            if (courseId == null || studentId == null) {
                throw new NullPointerException();
            } else {
                List<JournalEntity> studentEntities = journalEntityDao.getAllByStudentId(Integer.parseInt(studentId));
                for (JournalEntity studentEntity : studentEntities) {
                    if (studentEntity.getCourseId() == Integer.parseInt(courseId)) {
                        errorMessage = resourceBundle.getString("journal_validator.already_enrolled");
                    }
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
     * Validates input parameters before enrolling student on course.
     *
     * @param userId     id of the student whom we are trying to register on course
     * @param parameters map of parameters that must be validated before
     *                   enrolling student on course, to prevent our program
     *                   on unexpected errors.
     */
    public String validateOnEnroll(int userId, Map<String, String> parameters) throws ValidationException {
        LOG.debug("Validator starts");
        String errorMessage = "";
        try {
            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);
            ResourceBundle resourceBundle;
            if (userLanguage.equals(Language.RUS)) {
                resourceBundle = ResourceBundle.getBundle("resources", new Locale("ru", "Ru"));
            } else {
                resourceBundle = ResourceBundle.getBundle("resources", new Locale("en", "US"));
            }


            String courseId = parameters.get("courseId");

            List<JournalEntity> journalEntitiesOfStudent = journalEntityDao.getAllByStudentId(userId);

            for (JournalEntity journalEntity : journalEntitiesOfStudent) {
                if (Integer.parseInt(courseId) == journalEntity.getCourseId()) {
                    LOG.debug("Validator finish");
                    errorMessage = resourceBundle.getString("journal_validator.already_enrolled2");
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
     * Validates input parameters before deleting student from course.
     *
     * @param userId     id of the student whom we are trying to delete from course
     * @param parameters map of parameters that must be validated before
     *                   deleting student from course, to prevent our program
     *                   on unexpected errors.
     */
    public String validateOnLeave(int userId, Map<String, String> parameters) throws ValidationException {
        LOG.debug("Validator starts");
        String errorMessage = "";
        try {

            // setting language of current user to dictionary Dao
            Language userLanguage = SESSION_CONTAINER.get(userId);
            ResourceBundle resourceBundle;
            if (userLanguage.equals(Language.RUS)) {
                resourceBundle = ResourceBundle.getBundle("resources", new Locale("ru", "Ru"));
            } else {
                resourceBundle = ResourceBundle.getBundle("resources", new Locale("en", "US"));
            }

            String courseId = parameters.get("courseId");

            List<JournalEntity> journalEntitiesOfStudent = journalEntityDao.getAllByStudentId(userId);

            int det = 0;

            for (JournalEntity journalEntity : journalEntitiesOfStudent) {
                if (Integer.parseInt(courseId) == journalEntity.getCourseId()) {
                    det++;
                }
            }

            if (det == 0) {
                errorMessage = resourceBundle.getString("journal_validator.not_enrolled_yet");
            }
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.VALIDATION_ERROR, e);
            throw new ValidationException(Messages.VALIDATION_ERROR, e);
        }
        LOG.debug("Validator finish");
        return errorMessage;
    }
}
