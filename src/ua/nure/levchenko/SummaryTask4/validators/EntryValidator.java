package ua.nure.levchenko.SummaryTask4.validators;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.Student;
import ua.nure.levchenko.SummaryTask4.entity.Teacher;
import ua.nure.levchenko.SummaryTask4.entity.User;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.ValidationException;
import ua.nure.levchenko.SummaryTask4.validators.constants.Messages;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates parameters before all logins/signUps.
 *
 * @author K.Levchenko
 */
public class EntryValidator extends Validator {

    private static final long serialVersionUID = -2300702191660021061L;

    private static final Logger LOG = Logger.getLogger(EntryValidator.class);

    private static EntryValidator instance;

    private EntryValidator() {
    }

    public static synchronized EntryValidator getInstance() {
        if (instance == null) {
            instance = new EntryValidator();
            LOG.debug("Validator initialized");
        }
        return instance;
    }


    //methods

    /**
     * Validates input parameters before creating a session for user.
     *
     * @param parameters map of parameters that must be validated before
     *                   creating a session for user, to prevent our program
     *                   on unexpected errors.
     */
    public String validateOnLogin(Map<String, String> parameters) throws ValidationException {
        LOG.debug("Validator starts");
        String errorMessage = "";
        try {
            // setting language of current user to dictionary Dao

            ResourceBundle resourceBundle
                    = ResourceBundle.getBundle("resources", new Locale("en", "US"));

            String login = parameters.get("login");
            String password = parameters.get("password");

            User user = userDao.read(login);
            LOG.trace("Found in DB: user --> " + user);

            if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
                LOG.error("Message for User --> Login/password cannot be empty");
                errorMessage = resourceBundle.getString("entry_validator.empty_fields");
            } else if (user == null || !password.equals(user.getPassword())) {
                LOG.error("Message for User --> Cannot find user with such login/password");
                errorMessage = resourceBundle.getString("entry_validator.wrong_login_password");
            } else {
                // defining is user a Teacher or Student
                Teacher teacher = teacherDao.read(user.getId());
                Student student = studentDao.read(user.getId());
                if (teacher != null) {
                    errorMessage = "teacher";
                } else if (student != null) {
                    errorMessage = "student";
                }
            }

        } catch (NullPointerException | ClassCastException | DBException e) {
            LOG.error(Messages.VALIDATION_ERROR, e);
            throw new ValidationException(Messages.VALIDATION_ERROR, e);
        }
        LOG.debug("Validator finish");
        return errorMessage;
    }

    /**
     * Validates input parameters before creating a new entity and a session for new user.
     *
     * @param parameters map of parameters that must be validated before
     *                   creating na entity and a session for user, to prevent our program
     *                   on unexpected errors.
     */
    public String validateOnSignUp(Map<String, String> parameters) throws ValidationException {
        LOG.debug("Validator starts");

        String errorMessage = "";
        try {
            ResourceBundle resourceBundle
                    = ResourceBundle.getBundle("resources", new Locale("en", "US"));

            String login = parameters.get("login");
            String password = parameters.get("password");
            String firstName = parameters.get("firstName");
            String lastName = parameters.get("lastName");
            String patronymic = parameters.get("patronymic");
            String email = parameters.get("email");
            String phone = parameters.get("phone");

            // checking for the correctness of mail and phone formats
            Pattern patternEmail = Pattern.compile(".+@.+\\..+");
            Matcher matcherEmail = patternEmail.matcher(email);
            Pattern patternPhone = Pattern.compile("\\+?((\\s?\\d+)($)?)+");
            Matcher matcherPhone = patternPhone.matcher(phone);
            // checking for the correct name format
            Pattern patternName = Pattern.compile("(\\w+)");
            Matcher matcherFirstName = patternName.matcher(firstName);
            Matcher matcherLastName = patternName.matcher(lastName);
            Matcher matcherPatronymic = patternName.matcher(patronymic);

            if (login == null || login.isEmpty() || password == null || password.isEmpty()
                    || firstName.isEmpty() || lastName.isEmpty() || patronymic.isEmpty()) {
                LOG.error("Message for user --> Fields should not be empty. Fill in all the fields.");
                errorMessage = resourceBundle.getString("entry_validator.empty_fields_sign_up");
            } else if (userDao != null && userDao.read(login) != null) {
                LOG.error("Message for user --> User with such login is already exist. Try to \"Log in\" in your account.");
                errorMessage = resourceBundle.getString("entry_validator.user_with_such_login_is_exist");
            } else if (!email.isEmpty() && !matcherEmail.find()) {
                LOG.error("Message for user --> Incorrect Email format. Please try again.");
                errorMessage = resourceBundle.getString("entry_validator.incorrect_mail");
            } else if (!phone.isEmpty() && !matcherPhone.find()) {
                LOG.error("Message for user --> Incorrect Phone format. Please try again.");
                errorMessage = resourceBundle.getString("entry_validator.incorrect_phone");
            } else if (!matcherFirstName.find()) {
                LOG.error("Message for user --> Incorrect Phone format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_name");
            } else if (!matcherLastName.find()) {
                LOG.error("Message for user --> Incorrect Phone format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_surname");
            } else if (!matcherPatronymic.find()) {
                LOG.error("Message for user --> Incorrect Phone format. Please try again.");
                errorMessage = resourceBundle.getString("student_validator.incorrect_patronymic");
            }

        } catch (NullPointerException | ClassCastException | DBException e) {
            LOG.error(Messages.VALIDATION_ERROR, e);
            throw new ValidationException(Messages.VALIDATION_ERROR, e);
        }
        LOG.debug("Validator finish");
        return errorMessage;
    }
}
