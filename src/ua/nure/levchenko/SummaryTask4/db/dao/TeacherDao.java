package ua.nure.levchenko.SummaryTask4.db.dao;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.DBManager;
import ua.nure.levchenko.SummaryTask4.db.constants.Fields;
import ua.nure.levchenko.SummaryTask4.db.constants.SQLCommands;
import ua.nure.levchenko.SummaryTask4.entity.Teacher;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.Messages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Class that implements methods
 * for working with DB for Teacher entity.
 *
 * @author K.Levchenko
 */
public class TeacherDao implements DAO<Teacher, Integer> {
    private static final Logger LOG = Logger.getLogger(TeacherDao.class);

    private static TeacherDao instance;
    private final DBManager dbManager;

    private TeacherDao() throws DBException {
        dbManager = DBManager.getInstance();
    }

    public static synchronized TeacherDao getInstance() throws DBException {
        if (instance == null) {
            instance = new TeacherDao();
        }
        return instance;
    }

    /**
     * Creates the teacher entity in dataBase.
     *
     * @param teacher object of Teacher
     */
    @Override
    public void create(Teacher teacher) throws DBException {
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_CREATE_TEACHER);
            pstmt.setInt(1, teacher.getId());
            pstmt.setInt(2, teacher.getFirstNameId());
            pstmt.setInt(3, teacher.getLastNameId());
            pstmt.setInt(4, teacher.getPatronymicId());
            pstmt.setInt(5, teacher.getExperienceId());
            pstmt.setString(6, teacher.getEmail());
            pstmt.setString(7, teacher.getPhone());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
                con.close();
            } catch (SQLException ex) {
                LOG.error(Messages.ERR_ROLLBACK, ex);
            }
            LOG.error(Messages.ERR_CANNOT_CREATE_TEACHER, e);
            throw new DBException(Messages.ERR_CANNOT_CREATE_TEACHER, e);
        }
    }

    /**
     * Extracts the teacher entity from dataBase.
     *
     * @param id the id of particular Teacher
     *           by which method must extract the Teacher entity from dataBase
     * @return Teacher entity from dataBase
     */
    @Override
    public Teacher read(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_TEACHER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return extractTeacher(rs);
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_READ_TEACHER, e);
            throw new DBException(Messages.ERR_CANNOT_READ_TEACHER, e);
        }
        return null;
    }

    /**
     * Updates the teacher entity in dataBase.
     *
     * @param teacher object of Teacher
     */
    @Override
    public void update(Teacher teacher) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_UPDATE_TEACHER);
            pstmt.setInt(1, teacher.getFirstNameId());
            pstmt.setInt(2, teacher.getLastNameId());
            pstmt.setInt(3, teacher.getPatronymicId());
            pstmt.setInt(4, teacher.getExperienceId());
            pstmt.setString(5, teacher.getEmail());
            pstmt.setString(6, teacher.getPhone());
            pstmt.setInt(7, teacher.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_UPDATE_TEACHER, e);
            throw new DBException(Messages.ERR_CANNOT_READ_TEACHER, e);
        }
    }

    /**
     * Deletes the teacher entity from dataBase.
     *
     * @param id id of object of Teacher
     */
    @Override
    public void delete(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_DELETE_TEACHER_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_DELETE_TEACHER, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_TEACHER, ex);
        }
    }


    /**
     * Returns all teachers.
     *
     * @return List of teacher entities.
     */
    @Override
    public List<Teacher> getAll() throws DBException {
        List<Teacher> listOfTeachers = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLCommands.SQL_GET_TEACHERS_ORDER_BY_ID);
            while (rs.next()) {
                listOfTeachers.add(extractTeacher(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_ALL_TEACHERS, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_ALL_TEACHERS, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return listOfTeachers;
    }


    private Teacher extractTeacher(ResultSet rs) throws DBException {
        Teacher teacher = new Teacher();
        try {
            teacher.setId(rs.getInt(Fields.TEACHER_USER_ID));
            teacher.setFirstNameId(rs.getInt(Fields.TEACHER_FIRST_NAME_ID));
            teacher.setLastNameId(rs.getInt(Fields.TEACHER_LAST_NAME_ID));
            teacher.setPatronymicId(rs.getInt(Fields.TEACHER_PATRONYMIC_ID));
            teacher.setExperienceId(rs.getInt(Fields.TEACHER_EXPERIENCE_ID));
            teacher.setEmail(rs.getString(Fields.TEACHER_EMAIL));
            teacher.setPhone(rs.getString(Fields.TEACHER_PHONE));
            return teacher;
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_TEACHER, e);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_TEACHER, e);
        }
    }
}
