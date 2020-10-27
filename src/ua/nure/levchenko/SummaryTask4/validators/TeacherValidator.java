package ua.nure.levchenko.SummaryTask4.validators;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.User;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.ValidationException;
import ua.nure.levchenko.SummaryTask4.internationalization.Language;
import ua.nure.levchenko.SummaryTask4.validators.constants.Messages;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates parameters before all operations with Teacher entity.
 *
 * @author K.Levchenko
 */
public class TeacherValidator extends Validator {
    private static final long serialVersionUID = -4981835883067596686L;

    private static final Logger LOG = Logger.getLogger(TeacherValidator.class);

    private static TeacherValidator instance;
    ResourceBundle resourceBundle;

    {
        resourceBundle = resourceBundle = ResourceBundle.getBundle("resources", new Locale("en", "US"));
    }

    private TeacherValidator() {
    }

    public static synchronized TeacherValidator getInstance() {
        if (instance == null) {
            instance = new TeacherValidator();
            LOG.debug("Validator initialized");
        }
        return instance;
    }

    // methods

    /**
     * Validates input parameters before updating student in DB.
     *
     * @param userId     id of the current user
     * @param parameters map of parameters that must be validated before
     *                   enrolling student on course, to prevent our program
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


            // user
            String login = parameters.get("login");
            String password = parameters.get("password");
            String roleId = parameters.get("roleId");

            // teacher
            String firstName = parameters.get("firstName");
            String firstNameRus = parameters.get("firstNameRus");
            String lastName = parameters.get("lastName");
            String lastNameRus = parameters.get("lastNameRus");
            String patronymic = parameters.get("patronymic");
            String patronymicRus = parameters.get("patronymicRus");
            String experience = parameters.get("experience");
            String experienceRus = parameters.get("experienceRus");
            String email = parameters.get("email");
            String phone = parameters.get("phone");

            // checking for the correct name format
            Pattern patternName = Pattern.compile("(^\\D+$)");
            Matcher matcherFirstName = patternName.matcher(firstName);
            Matcher matcherLastName = patternName.matcher(lastName);
            Matcher matcherPatronymic = patternName.matcher(patronymic);
            Matcher matcherFirstNameRus = patternName.matcher(firstNameRus);
            Matcher matcherLastNameRus = patternName.matcher(lastNameRus);
            Matcher matcherPatronymicRus = patternName.matcher(patronymicRus);

            if (login == null || login.isEmpty() || password == null || password.isEmpty()
                    || firstName.isEmpty() || lastName.isEmpty() || patronymic.isEmpty()
                    || experience == null || experience.isEmpty() || experienceRus == null
                    || experienceRus.isEmpty() || firstNameRus == null || firstNameRus.isEmpty()
                    || lastNameRus == null || lastNameRus.isEmpty() || patronymicRus == null
                    || patronymicRus.isEmpty() || roleId == null || roleId.isEmpty()) {
                errorMessage = resourceBundle.getString("teacher_validator.fields_should_not_be_empty");
            } else if (userDao != null && userDao.read(login) != null) {
                errorMessage = resourceBundle.getString("teacher_validator.use_another_login");
            } else if (email != null && !email.isEmpty()) {
                Pattern patternEmail = Pattern.compile(".+@.+\\..+");
                Matcher matcherEmail = patternEmail.matcher(email);
                if (!matcherEmail.find()) {
                    LOG.error("Incorrect Email format. Please try again.");
                    errorMessage = resourceBundle.getString("teacher_validator.incorrect_email");
                }
            } else if (phone != null && !phone.isEmpty()) {
                Pattern patternPhone = Pattern.compile("(\\+?((\\s?\\d+)($)?)+$)");
                Matcher matcherPhone = patternPhone.matcher(phone);
                if (!matcherPhone.find()) {
                    LOG.error("Message for User --> Incorrect Phone format. Please try again.");
                    errorMessage = resourceBundle.getString("teacher_validator.incorrect_phone");
                } else if (!matcherFirstName.find()) {
                    LOG.error("Message for user --> Incorrect Phone format. Please try again.");
                    errorMessage = resourceBundle.getString("teacher_validator.incorrect_name");
                } else if (!matcherLastName.find()) {
                    LOG.error("Message for user --> Incorrect Phone format. Please try again.");
                    errorMessage = resourceBundle.getString("teacher_validator.incorrect_surname");
                } else if (!matcherPatronymic.find()) {
                    LOG.error("Message for user --> Incorrect Phone format. Please try again.");
                    errorMessage = resourceBundle.getString("teacher_validator.incorrect_patronymic");
                } else if (!matcherFirstNameRus.find()) {
                    LOG.error("Message for user --> Incorrect Name format. Please try again.");
                    errorMessage = resourceBundle.getString("teacher_validator.incorrect_name");
                } else if (!matcherLastNameRus.find()) {
                    LOG.error("Message for user --> Incorrect surname format. Please try again.");
                    errorMessage = resourceBundle.getString("teacher_validator.incorrect_surname");
                } else if (!matcherPatronymicRus.find()) {
                    LOG.error("Message for user --> Incorrect patronymic format. Please try again.");
                    errorMessage = resourceBundle.getString("teacher_validator.incorrect_patronymic");
                }
            }
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.VALIDATION_ERROR, e);
            throw new ValidationException("Validation internal error.", e);
        }
        LOG.debug("Validator finish");
        return errorMessage;
    }


    /**
     * Validates input parameters before updating teacher in DB.
     *
     * @param userId     id of the teacher whom we are needed to update in DB
     * @param parameters map of parameters that must be validated before
     *                   updating teacher in DB, to prevent our program
     *                   on unexpected errors.
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


            // user
            String login = parameters.get("login");
            String oldLogin = parameters.get("oldLogin");
            String password = parameters.get("password");
            String oldPassword = parameters.get("oldPassword");

            // teacher
            String firstName = parameters.get("firstName");
            String firstNameRus = parameters.get("firstNameRus");
            String lastName = parameters.get("lastName");
            String lastNameRus = parameters.get("lastNameRus");
            String patronymic = parameters.get("patronymic");
            String patronymicRus = parameters.get("patronymicRus");
            String experience = parameters.get("experience");
            String experienceRus = parameters.get("experienceRus");
            String email = parameters.get("email");
            String phone = parameters.get("phone");

            // getting User from DB
            User user = userDao.read(userId);

            // checking for the correct mail and phone format
            Pattern patternEmail = Pattern.compile(".+@.+\\..+");
            Matcher matcherEmail = patternEmail.matcher(email);
            Pattern patternPhone = Pattern.compile("(\\+?((\\s?\\d+)($)?)+$)");
            Matcher matcherPhone = patternPhone.matcher(phone);
            // checking for the correct name format
            Pattern patternName = Pattern.compile("(\\D+)");
            Matcher matcherFirstName = patternName.matcher(firstName);
            Matcher matcherLastName = patternName.matcher(lastName);
            Matcher matcherPatronymic = patternName.matcher(patronymic);
            Matcher matcherFirstNameRus = patternName.matcher(firstName);
            Matcher matcherLastNameRus = patternName.matcher(lastName);
            Matcher matcherPatronymicRus = patternName.matcher(patronymic);


            if (((login != null && !login.isEmpty()) && (oldLogin == null || oldLogin.isEmpty()))
                    || (login == null || login.isEmpty()) && (oldLogin != null && !oldLogin.isEmpty())) {
                errorMessage = resourceBundle.getString("teacher_validator.both_field_should_be_filled_login");
            } else if (oldLogin != null && !oldLogin.isEmpty() && !oldLogin.equals(user.getLogin())) {
                errorMessage = resourceBundle.getString("teacher_validator.old_login_is_not_correct");
            } else if (((password != null && !password.isEmpty()) && (oldPassword == null || oldPassword.isEmpty()))
                    || (password == null || password.isEmpty()) && (oldPassword != null && !oldPassword.isEmpty())) {
                errorMessage = resourceBundle.getString("teacher_validator.both_field_should_be_filled_password");
            } else if (oldPassword != null && !oldPassword.isEmpty() && !oldPassword.equals(user.getPassword())) {
                errorMessage = resourceBundle.getString("teacher_validator.old_pass_is_not_correct");
            } else if (userDao.read(login) != null) {
                errorMessage = resourceBundle.getString("teacher_validator.use_another_login");
            } else if (!email.isEmpty() && !matcherEmail.find()) {
                errorMessage = resourceBundle.getString("teacher_validator.incorrect_email");
            } else if (!phone.isEmpty() && !matcherPhone.find()) {
                errorMessage = resourceBundle.getString("teacher_validator.incorrect_phone");
            } else if (!firstName.isEmpty() && (firstNameRus == null || firstNameRus.isEmpty()) || firstName.isEmpty() && firstNameRus != null && !firstNameRus.isEmpty()) {
                errorMessage = resourceBundle.getString("teacher_validator.both_field_should_be_filled_name");
            } else if (!lastName.isEmpty() && (lastNameRus == null || lastNameRus.isEmpty()) || lastName.isEmpty() && lastNameRus != null && !lastNameRus.isEmpty()) {
                errorMessage = resourceBundle.getString("teacher_validator.both_field_should_be_filled_surname");
            } else if (!patronymic.isEmpty() && (patronymicRus == null || patronymicRus.isEmpty()) || patronymic.isEmpty() && patronymicRus != null && !patronymicRus.isEmpty()) {
                errorMessage = resourceBundle.getString("teacher_validator.both_field_should_be_filled_patronymic");
            } else if (((experience != null && !experience.isEmpty())
                    && (experienceRus == null || experienceRus.isEmpty()))
                    || (experience == null || experience.isEmpty()) && (experienceRus != null && !experienceRus.isEmpty())) {
                errorMessage = resourceBundle.getString("teacher_validator.both_field_should_be_filled_experience");
            } else if (!firstName.isEmpty() && !matcherFirstName.find()) {
                LOG.error("Message for user --> Incorrect Name format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_name");
            } else if (!lastName.isEmpty() && !matcherLastName.find()) {
                LOG.error("Message for user --> Incorrect Surname format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_surname");
            } else if (!patronymic.isEmpty() && !matcherPatronymic.find()) {
                LOG.error("Message for user --> Incorrect Patronymic format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_patronymic");
            } else if (!firstNameRus.isEmpty() && !matcherFirstNameRus.find()) {
                LOG.error("Message for user --> Incorrect Name format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_name");
            } else if (!lastNameRus.isEmpty() && !matcherLastNameRus.find()) {
                LOG.error("Message for user --> Incorrect surname format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_surname");
            } else if (!patronymicRus.isEmpty() && !matcherPatronymicRus.find()) {
                LOG.error("Message for user --> Incorrect patronymic format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_patronymic");
            }

        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.VALIDATION_ERROR, e);
            throw new ValidationException(Messages.VALIDATION_ERROR, e);
        }
        LOG.debug("Validator finish");
        return errorMessage;

    }
}
