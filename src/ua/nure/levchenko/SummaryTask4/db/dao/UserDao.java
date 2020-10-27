package ua.nure.levchenko.SummaryTask4.db.dao;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.DBManager;
import ua.nure.levchenko.SummaryTask4.db.constants.Fields;
import ua.nure.levchenko.SummaryTask4.db.constants.SQLCommands;
import ua.nure.levchenko.SummaryTask4.entity.Student;
import ua.nure.levchenko.SummaryTask4.entity.User;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.Messages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements methods
 * for working with DB for User entity.
 *
 * @author K.Levchenko
 */
public class UserDao implements DAO<User, Integer> {
    private static final Logger LOG = Logger.getLogger(TeacherDao.class);

    private static UserDao instance;

    private static TeacherDao teacherDao;
    private static StudentDao studentDao;
    private final DBManager dbManager;

    private UserDao() throws DBException {
        dbManager = DBManager.getInstance();
        teacherDao = TeacherDao.getInstance();
        studentDao = StudentDao.getInstance();
    }

    public static synchronized UserDao getInstance() throws DBException {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    /**
     * Creates the user entity in dataBase.
     *
     * @param user object of User
     */
    @Override
    public void create(User user) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_CREATE_USER);
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getRoleId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_CREATE_USER, e);
            throw new DBException(Messages.ERR_CANNOT_CREATE_USER, e);
        }
    }

    /**
     * Extracts the user entity from dataBase.
     *
     * @param id the id of particular User
     *           by which method must extract the User entity from dataBase
     * @return User entity from dataBase
     */
    @Override
    public User read(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_USER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return extractUser(rs);
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_READ_USER, e);
            throw new DBException(Messages.ERR_CANNOT_READ_USER, e);
        }
        return null;
    }

    /**
     * Updates the user entity in dataBase.
     *
     * @param user object of User
     */
    @Override
    public void update(User user) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_UPDATE_USER);
            pstmt.setString(1, user.getLogin());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getRoleId());
            pstmt.setInt(4, user.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_UPDATE_USER, e);
            throw new DBException(Messages.ERR_CANNOT_UPDATE_USER, e);
        }
    }

    /**
     * Deletes the user entity from dataBase.
     *
     * @param id id of object of User
     */
    @Override
    public void delete(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_DELETE_USER_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_DELETE_USER, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_USER, ex);
        }
    }

    /**
     * Returns all users.
     *
     * @return List of user entities.
     */
    @Override
    public List<User> getAll() throws DBException {
        List<User> listOfUsers = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLCommands.SQL_GET_USERS_ORDER_BY_ID);
            while (rs.next()) {
                listOfUsers.add(extractUser(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, ex);
            }
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return listOfUsers;
    }


    /**
     * Extracts the user entity from dataBase.
     *
     * @param login the login of particular User
     *              by which method must extract the User entity from dataBase
     * @return User entity from dataBase
     */
    public User read(String login) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return extractUser(rs);
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_READ_USER, e);
            throw new DBException(Messages.ERR_CANNOT_READ_USER, e);
        }
        return null;
    }


    /**
     * Creates the user entity in dataBase.
     * Used only inside the methods of creating
     * User, Teacher or Student entities in DataBase
     *
     * @param user    object of User
     * @param student object of needed Student
     */
    public void signUpStudent(User user, Student student) throws DBException {
        try {
            create(user);
            student.setId(read(user.getLogin()).getId());
            studentDao.create(student);
        } catch (DBException e) {
            delete(user.getId());
            LOG.error("Cannot add user (teacher) to the DataBase");
            throw new DBException("Cannot add user (teacher) to the DataBase", e);
        }
    }

    private User extractUser(ResultSet rs) throws DBException {
        User user = new User();
        try {
            user.setId(rs.getInt(Fields.ID));
            user.setLogin(rs.getString(Fields.USER_LOGIN));
            user.setPassword(rs.getString(Fields.USER_PASSWORD));
            user.setRoleId(rs.getInt(Fields.USER_ROLE_ID));
            return user;
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_USER, e);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER, e);
        }
    }

}
