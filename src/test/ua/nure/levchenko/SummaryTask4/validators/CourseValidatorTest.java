package test.ua.nure.levchenko.SummaryTask4.validators;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ua.nure.levchenko.SummaryTask4.exception.ValidationException;
import ua.nure.levchenko.SummaryTask4.internationalization.SessionContainer;
import ua.nure.levchenko.SummaryTask4.validators.CourseValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * CourseValidator Tester.
 *
 * @author <K.Levchenko>
 * @version 1.0
 * @since <pre>апр. 28, 2020</pre>
 */
public class CourseValidatorTest {
    Map<String, String> parameters;
    int userId;
    CourseValidator courseValidator;
    ResourceBundle resourceBundle;
    SessionContainer sessionContainer;

    @Before
    public void before() throws Exception {
        courseValidator = CourseValidator.getInstance();
        resourceBundle = ResourceBundle.getBundle("resources");

        sessionContainer = SessionContainer.getInstance();
        userId = 1;
        parameters = new HashMap<>();
        parameters.put("courseTeacherId", "1");
        parameters.put("courseTopicId", "1");
        parameters.put("courseName", "Course Name");
        parameters.put("courseNameRus", "Имя курса");
        parameters.put("courseDescription", "Course description");
        parameters.put("courseDescriptionRus", "Описание курса");
        parameters.put("courseStartDate", "2020-04-30");
        parameters.put("courseEndDate", "2020-07-25");
    }

    @After
    public void after() throws Exception {

    }

    /**
     * Method: validateOnCreate(int userId, Map<String, String> parameters)
     */
    @Test(expected = ValidationException.class)
    public void testValidateOnCreateOnException() throws Exception {
        userId = 0;
        courseValidator.validateOnCreate(userId, parameters);
    }

    /**
     * Method: validateOnCreate(int userId, Map<String, String> parameters)
     */
    @Test
    public void testValidateOnCreateOnMessage() throws Exception {

    }

    /**
     * Method: validateOnDelete(int userId, Map<String, String> parameters)
     */
    @Test
    public void testValidateOnDelete() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: validateOnUpdate(int userId, Map<String, String> parameters)
     */
    @Test
    public void testValidateOnUpdate() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: validateOnConnect(Map<String, String> parameters)
     */
    @Test
    public void testValidateOnConnect() throws Exception {
//TODO: Test goes here... 
    }


} 
