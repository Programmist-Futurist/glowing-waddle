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
 * Validates parameters before all operations with Student entity.
 *
 * @author K.Levchenko
 */
public class StudentValidator extends Validator {

    private static final long serialVersionUID = -6317152343191422708L;

    private static final Logger LOG = Logger.getLogger(StudentValidator.class);

    private static StudentValidator instance;
    ResourceBundle resourceBundle;

    {
        resourceBundle = ResourceBundle.getBundle("resources", new Locale("en", "US"));
    }

    private StudentValidator() {
    }

    public static synchronized StudentValidator getInstance() {
        if (instance == null) {
            instance = new StudentValidator();
            LOG.debug("Validator initialized");
        }
        return instance;
    }

    // methods

    /**
     * Validates input parameters before updating student in DB.
     *
     * @param userId     id of the student whom we are needed to update in DB
     * @param parameters map of parameters that must be validated before
     *                   updating student in DB, to prevent our program
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


            String login = parameters.get("login");
            String oldLogin = parameters.get("oldLogin");
            String password = parameters.get("password");
            String oldPassword = parameters.get("oldPassword");
            String firstName = parameters.get("firstName");
            String lastName = parameters.get("lastName");
            String patronymic = parameters.get("patronymic");
            String email = parameters.get("email");
            String phone = parameters.get("phone");

            User user = userDao.read(userId);


            // checking for the correct mail and phone format
            Pattern patternEmail = Pattern.compile(".+@.+\\..+");
            Matcher matcherEmail = patternEmail.matcher(email);
            Pattern patternPhone = Pattern.compile("(\\+?((\\s?\\d+)($)?)+$)");
            Matcher matcherPhone = patternPhone.matcher(phone);
            // checking for the correct name format
            Pattern patternName = Pattern.compile("(^\\D+$)");
            Matcher matcherFirstName = patternName.matcher(firstName);
            Matcher matcherLastName = patternName.matcher(lastName);
            Matcher matcherPatronymic = patternName.matcher(patronymic);

            if (((login != null && !login.isEmpty()) && (oldLogin == null || oldLogin.isEmpty()))
                    || (login == null || login.isEmpty()) && (oldLogin != null && !oldLogin.isEmpty())) {
                LOG.error("Message for user --> Both fields should be filled (Login)");
                errorMessage = resourceBundle.getString("student_validator.both_field_should_be_filled_login");
            } else if (oldLogin != null && !oldLogin.isEmpty() && !oldLogin.equals(user.getLogin())) {
                LOG.error("Message for user --> Old login is not correct");
                errorMessage = resourceBundle.getString("student_validator.old_login_is_not_correct");
            } else if (((password != null && !password.isEmpty()) && (oldPassword == null || oldPassword.isEmpty()))
                    || (password == null || password.isEmpty()) && (oldPassword != null && !oldPassword.isEmpty())) {
                LOG.error("Message for user --> Both fields should be filled (Password)");
                errorMessage = resourceBundle.getString("student_validator.both_field_should_be_filled_password");
            } else if (oldPassword != null && !oldPassword.isEmpty() && !oldPassword.equals(user.getPassword())) {
                LOG.error("Message for user --> Old password is not correct");
                errorMessage = resourceBundle.getString("student_validator.old_pass_is_not_correct");
            } else if (userDao.read(login) != null) {
                LOG.error("Message for user --> Use with such login is already exist. Use another one.");
                errorMessage = resourceBundle.getString("student_validator.use_another_login");
            } else if (!email.isEmpty() && !matcherEmail.find()) {
                LOG.error("Message for user --> Incorrect Email format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_email");
            } else if (!phone.isEmpty() && !matcherPhone.find()) {
                LOG.error("Message for user --> Incorrect Phone format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_phone");
            } else if (!firstName.isEmpty() && !matcherFirstName.find()) {
                LOG.error("Message for user --> Incorrect Phone format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_name");
            } else if (!lastName.isEmpty() && !matcherLastName.find()) {
                LOG.error("Message for user --> Incorrect Phone format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_surname");
            } else if (!patronymic.isEmpty() && !matcherPatronymic.find()) {
                LOG.error("Message for user --> Incorrect Phone format. Please try again.");
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
