package ua.nure.levchenko.SummaryTask4.db.dao;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.DBManager;
import ua.nure.levchenko.SummaryTask4.db.constants.Fields;
import ua.nure.levchenko.SummaryTask4.db.constants.SQLCommands;
import ua.nure.levchenko.SummaryTask4.entity.JournalEntity;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.Messages;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements methods
 * for working with DB for Journal entity.
 *
 * @author K.Levchenko
 */
public class JournalEntityDao implements DAO<JournalEntity, Integer> {
    private static final Logger LOG = Logger.getLogger(JournalEntityDao.class);
    static DBManager dbManager;
    private static JournalEntityDao instance;

    private JournalEntityDao() throws DBException {
        dbManager = DBManager.getInstance();
    }

    public static synchronized JournalEntityDao getInstance() throws DBException {
        if (instance == null) {
            instance = new JournalEntityDao();
        }
        return instance;
    }

    /**
     * Creates a journalEntity entity in dataBase.
     *
     * @param journalEntity object of JournalEntity
     */
    @Override
    public void create(JournalEntity journalEntity) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_CREATE_JOURNAL_ENTITY);
            pstmt.setInt(1, journalEntity.getCourseId());
            pstmt.setInt(2, journalEntity.getStudentId());
            pstmt.setInt(3, journalEntity.getMark());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_CREATE_JOURNAL_ENTITY, e);
            throw new DBException(Messages.ERR_CANNOT_CREATE_JOURNAL_ENTITY, e);
        }
    }

    /**
     * Extracts a journal from the dataBase.
     *
     * @param id the characteristics of the journalEntity
     *           by which student will be extracted from dataBase
     * @return journal entity.
     */
    @Override
    public JournalEntity read(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_JOURNAL_ENTITY_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return extractJournalEntity(rs);
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_READ_JOURNAL_ENTITY, e);
            throw new DBException(Messages.ERR_CANNOT_READ_JOURNAL_ENTITY, e);
        }
        return null;
    }

    /**
     * Updates the journalEntity entity in dataBase.
     *
     * @param journalEntity object of JournalEntity
     */
    @Override
    public void update(JournalEntity journalEntity) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_UPDATE_JOURNAL_ENTITY);
            pstmt.setInt(1, journalEntity.getCourseId());
            pstmt.setInt(2, journalEntity.getStudentId());
            pstmt.setInt(3, journalEntity.getMark());
            pstmt.setInt(4, journalEntity.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_UPDATE_JOURNAL_ENTITY, e);
            throw new DBException(Messages.ERR_CANNOT_UPDATE_JOURNAL_ENTITY, e);
        }
    }

    /**
     * Deletes the status entity from dataBase.
     *
     * @param id id of object of JournalEntity
     */
    @Override
    public void delete(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_DELETE_JOURNAL_ENTITY_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_DELETE_JOURNAL_ENTITY, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_JOURNAL_ENTITY, ex);
        }
    }

    /**
     * Returns all journal entities.
     *
     * @return List of journal entities.
     */
    @Override
    public List<JournalEntity> getAll() throws DBException {
        List<JournalEntity> journal = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_GET_JOURNAL_ENTITIES_ORDER_BY_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                journal.add(extractJournalEntity(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_FULL_JOURNAL, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_FULL_JOURNAL, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return journal;
    }


    /**
     * Returns all journal entities that contain such course id.
     *
     * @return List of journal entities.
     */
    public List<JournalEntity> getAllByCourseId(int courseId) throws DBException {
        List<JournalEntity> journal = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_GET_JOURNAL_BY_COURSE_ID);
            pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                journal.add(extractJournalEntity(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_FULL_JOURNAL, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_FULL_JOURNAL, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return journal;
    }

    /**
     * Returns all journal entities that contain such student id.
     *
     * @return List of journal entities.
     */
    public ArrayList<JournalEntity> getAllByStudentId(int studentId) throws DBException {
        ArrayList<JournalEntity> journal = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_GET_JOURNAL_BY_STUDENT_ID);
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                journal.add(extractJournalEntity(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_FULL_JOURNAL, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_FULL_JOURNAL, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return journal;
    }


    /**
     * Extracts a journal from the dataBase.
     *
     * @param courseId  the id of the course entity
     * @param studentId the id of the student entity
     * @return journal entity.
     */
    public JournalEntity readByCourseAndStudentId(Integer courseId, Integer studentId) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_JOURNAL_ENTITY_BY_COURSE_AND_STUDENT_ID);
            preparedStatement.setInt(1, courseId);
            preparedStatement.setInt(2, studentId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return extractJournalEntity(rs);
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_READ_JOURNAL_ENTITY, e);
            throw new DBException(Messages.ERR_CANNOT_READ_JOURNAL_ENTITY, e);
        }
        return null;
    }


    private JournalEntity extractJournalEntity(ResultSet rs) throws DBException {
        try {
            JournalEntity journalEntity = new JournalEntity();
            journalEntity.setId(rs.getInt(Fields.ID));
            journalEntity.setCourseId(rs.getInt(Fields.JOURNAL_COURSE_ID));
            journalEntity.setStudentId(rs.getInt(Fields.JOURNAL_STUDENT_ID));
            journalEntity.setMark(rs.getInt(Fields.JOURNAL_STUDENT_MARK));
            return journalEntity;
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_JOURNAL_ENTITY, e);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_JOURNAL_ENTITY, e);
        }
    }

}
