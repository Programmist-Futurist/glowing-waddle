package ua.nure.levchenko.SummaryTask4.db.dao;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.DBManager;
import ua.nure.levchenko.SummaryTask4.db.constants.Fields;
import ua.nure.levchenko.SummaryTask4.db.constants.SQLCommands;
import ua.nure.levchenko.SummaryTask4.entity.JournalEntity;
import ua.nure.levchenko.SummaryTask4.entity.Student;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.Messages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements methods
 * for working with DB for Student entity.
 *
 * @author K.Levchenko
 */
public class StudentDao implements DAO<Student, Integer> {
    private static final Logger LOG = Logger.getLogger(StudentDao.class);
    static DBManager dbManager;
    private static StudentDao instance;

    private StudentDao() throws DBException {
        dbManager = DBManager.getInstance();
    }

    public static synchronized StudentDao getInstance() throws DBException {
        if (instance == null) {
            instance = new StudentDao();
        }
        return instance;
    }

    /**
     * Creates the Student entity in dataBase.
     *
     * @param student object of the Student
     */
    @Override
    public void create(Student student) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_CREATE_STUDENT);
            pstmt.setInt(1, student.getId());
            pstmt.setInt(2, student.getFirstNameId());
            pstmt.setInt(3, student.getLastNameId());
            pstmt.setInt(4, student.getPatronymicId());
            pstmt.setString(5, student.getEmail());
            pstmt.setString(6, student.getPhone());
            pstmt.setBoolean(7, student.isBlock());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_CREATE_STUDENT, e);
            throw new DBException(Messages.ERR_CANNOT_CREATE_STUDENT, e);
        }
    }

    /**
     * Extracts the Student entity from dataBase.
     *
     * @param id the login of particular Student
     *           by which method must extract the Student entity from dataBase
     * @return Student entity from dataBase
     */
    @Override
    public Student read(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_STUDENT_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return extractStudent(rs);
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_READ_STUDENT, e);
            throw new DBException(Messages.ERR_CANNOT_READ_STUDENT, e);
        }
        return null;
    }

    /**
     * Updates the Student entity in dataBase.
     *
     * @param student object of Student
     */
    @Override
    public void update(Student student) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_UPDATE_STUDENT);
            pstmt.setInt(1, student.getFirstNameId());
            pstmt.setInt(2, student.getLastNameId());
            pstmt.setInt(3, student.getPatronymicId());
            pstmt.setString(4, student.getEmail());
            pstmt.setString(5, student.getPhone());
            pstmt.setBoolean(6, student.isBlock());
            pstmt.setInt(7, student.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_UPDATE_STUDENT, e);
            throw new DBException(Messages.ERR_CANNOT_UPDATE_STUDENT, e);
        }
    }

    /**
     * Deletes the Student entity from dataBase.
     *
     * @param id of object of Student
     */
    @Override
    public void delete(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_DELETE_STUDENT_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_DELETE_STUDENT, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_STUDENT, ex);
        }
    }


    /**
     * Returns all students.
     *
     * @return List of student entities.
     */
    @Override
    public List<Student> getAll() throws DBException {
        List<Student> listOfStudents = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLCommands.SQL_GET_STUDENTS_ORDER_BY_ID);
            while (rs.next()) {
                listOfStudents.add(extractStudent(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_ALL_STUDENTS, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_ALL_STUDENTS, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return listOfStudents;
    }


    /**
     * Returns all students of current journal.
     *
     * @return List of student entities.
     */
    public List<Student> getAllByJournal(List<JournalEntity> journal) throws DBException {
        List<Student> listOfStudents = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            for (JournalEntity journalEntity : journal) {
                PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_GET_STUDENT_BY_ID);
                pstmt.setInt(1, journalEntity.getStudentId());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    listOfStudents.add(extractStudent(rs));
                }
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_ALL_STUDENTS, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_ALL_STUDENTS, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return listOfStudents;
    }


    private Student extractStudent(ResultSet rs) throws DBException {
        Student student = new Student();
        try {
            student.setId(rs.getInt(Fields.STUDENT_USER_ID));
            student.setFirstNameId(rs.getInt(Fields.STUDENT_FIRST_NAME_ID));
            student.setLastNameId(rs.getInt(Fields.STUDENT_LAST_NAME_ID));
            student.setPatronymicId(rs.getInt(Fields.STUDENT_PATRONYMIC_ID));
            student.setEmail(rs.getString(Fields.STUDENT_EMAIL));
            student.setPhone(rs.getString(Fields.STUDENT_PHONE));
            student.setBlock(rs.getBoolean(Fields.STUDENT_BLOCK));
            return student;
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_STUDENT, e);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_STUDENT, e);
        }
    }
}
