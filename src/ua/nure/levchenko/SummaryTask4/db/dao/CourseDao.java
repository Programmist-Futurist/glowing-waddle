package ua.nure.levchenko.SummaryTask4.db.dao;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.DBManager;
import ua.nure.levchenko.SummaryTask4.db.constants.Fields;
import ua.nure.levchenko.SummaryTask4.db.constants.SQLCommands;
import ua.nure.levchenko.SummaryTask4.entity.Course;
import ua.nure.levchenko.SummaryTask4.entity.Status;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.Messages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements methods
 * for working with DB for Course entity.
 *
 * @author K.Levchenko
 */
public class CourseDao implements DAO<Course, Integer> {
    private static final Logger LOG = Logger.getLogger(CourseDao.class);

    private static CourseDao instance;
    private final DBManager dbManager;

    private CourseDao() throws DBException {
        dbManager = DBManager.getInstance();
    }

    public static synchronized CourseDao getInstance() throws DBException {
        if (instance == null) {
            instance = new CourseDao();
        }
        return instance;
    }

    /**
     * Creates the course entity in dataBase.
     *
     * @param course object of Course
     */
    public void create(Course course) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_CREATE_COURSE);
            pstmt.setInt(1, course.getTeacherId());
            pstmt.setInt(2, course.getTopicId());
            pstmt.setInt(3, course.getStatusId());
            pstmt.setInt(4, course.getNameId());
            pstmt.setInt(5, course.getDescriptionId());
            pstmt.setInt(6, course.getDurationInMonths());
            pstmt.setDate(7, new java.sql.Date(course.getStartDate().getTime()));
            pstmt.setDate(8, new java.sql.Date(course.getEndDate().getTime()));
            pstmt.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_CREATE_COURSE, e);
            throw new DBException(Messages.ERR_CANNOT_CREATE_COURSE, e);
        }
    }

    /**
     * Extracts the course entity from dataBase.
     *
     * @param id the name of the Course that must be extracted
     * @return object of the Course
     */
    @Override
    public Course read(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_COURSE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return extractCourse(rs);
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_READ_COURSE, e);
            throw new DBException(Messages.ERR_CANNOT_READ_COURSE, e);
        }
        return null;
    }

    /**
     * Updates the course entity in dataBase.
     *
     * @param course object of Course
     */
    @Override
    public void update(Course course) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_UPDATE_COURSE);
            pstmt.setInt(1, course.getTeacherId());
            pstmt.setInt(2, course.getTopicId());
            pstmt.setInt(3, course.getStatusId());
            pstmt.setInt(4, course.getNameId());
            pstmt.setInt(5, course.getDescriptionId());
            pstmt.setInt(6, course.getDurationInMonths());
            pstmt.setDate(7, new java.sql.Date(course.getStartDate().getTime()));
            pstmt.setDate(8, new java.sql.Date(course.getEndDate().getTime()));
            pstmt.setInt(9, course.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_UPDATE_COURSE, e);
            throw new DBException(Messages.ERR_CANNOT_UPDATE_COURSE, e);
        }
    }

    /**
     * Deletes the course entity from dataBase.
     *
     * @param id id of needed Course
     */
    @Override
    public void delete(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_DELETE_COURSE_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_DELETE_COURSE, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_COURSE, ex);
        }
    }

    /**
     * Returns all courses.
     *
     * @return List of course entities.
     */
    @Override
    public List<Course> getAll() throws DBException {
        List<Course> listOfCourses = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLCommands.SQL_GET_COURSES_ORDER_BY_ID);
            while (rs.next()) {
                listOfCourses.add(extractCourse(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_ALL_COURSES, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_ALL_COURSES, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return listOfCourses;
    }

    /**
     * Returns all courses that contains such topic id.
     *
     * @return List of course entities.
     */
    public List<Course> getAllByTopic(int topicId) throws DBException {
        List<Course> listOfCourses = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_GET_COURSES_BY_TOPIC);
            pstmt.setInt(1, topicId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                listOfCourses.add(extractCourse(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_ALL_COURSES, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_ALL_COURSES, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return listOfCourses;
    }


    /**
     * Returns all courses that contain such teacher id.
     *
     * @return List of course entities.
     */
    public List<Course> getAllByTeacher(int teacherId) throws DBException {
        List<Course> listOfCourses = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_GET_COURSES_BY_TEACHER);
            pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                listOfCourses.add(extractCourse(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_ALL_COURSES, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_ALL_COURSES, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return listOfCourses;
    }


    /**
     * Returns all courses that contains such status id.
     *
     * @return List of course entities.
     */
    public List<Course> getAllByStatus(Status status) throws DBException {
        List<Course> listOfCourses = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_GET_COURSES_BY_STATUS);
            pstmt.setInt(1, Status.getIntValue(status.getName()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                listOfCourses.add(extractCourse(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
                throw new DBException(Messages.ERR_ROLLBACK, ex);
            }
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return listOfCourses;
    }

    /**
     * Extracts the course entity from dataBase.
     *
     * @param nameId the name of the Course that must be extracted
     * @return object of the Course
     */

    public Course readByNameId(Integer nameId) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_COURSE_BY_NAME_ID);
            preparedStatement.setInt(1, nameId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return extractCourse(rs);
        } catch (SQLException | DBException e) {
            LOG.error(Messages.ERR_CANNOT_READ_COURSE, e);
            throw new DBException(Messages.ERR_CANNOT_READ_COURSE, e);
        }
        return null;
    }

    private Course extractCourse(ResultSet rs) throws DBException {
        try {
            Course course = new Course();
            course.setId(rs.getInt(Fields.ID));
            course.setTeacherId(rs.getInt(Fields.COURSE_TEACHER_ID));
            course.setTopicId(rs.getInt(Fields.COURSE_TOPIC_ID));
            course.setStatusId(rs.getInt(Fields.COURSE_STATUS_ID));
            course.setNameId(rs.getInt(Fields.COURSE_NAME_ID));
            course.setDescriptionId(rs.getInt(Fields.COURSE_DESCRIPTION_ID));
            course.setDurationInMonths(rs.getInt(Fields.COURSE_DURATION_ID));
            course.setStartDate(rs.getDate(Fields.COURSE_START_DATE));
            course.setEndDate(rs.getDate(Fields.COURSE_END_DATE));
            return course;
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_COURSE, e);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_COURSE, e);
        }
    }
}
