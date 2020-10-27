package ua.nure.levchenko.SummaryTask4.db.dao;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.DBManager;
import ua.nure.levchenko.SummaryTask4.db.constants.Fields;
import ua.nure.levchenko.SummaryTask4.db.constants.SQLCommands;
import ua.nure.levchenko.SummaryTask4.entity.Status;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.Messages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements methods
 * for working with DB for Status entity.
 *
 * @author K.Levchenko
 */
public class StatusDao implements DAO<Status, Integer> {
    private static final Logger LOG = Logger.getLogger(StudentDao.class);

    private static StatusDao instance;
    private final DBManager dbManager;

    private StatusDao() throws DBException {
        dbManager = DBManager.getInstance();
    }

    public static synchronized StatusDao getInstance() throws DBException {
        if (instance == null) {
            instance = new StatusDao();
        }
        return instance;
    }

    /**
     * Creates a status entity in dataBase.
     *
     * @param status object of Status
     */
    @Override
    public void create(Status status) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_CREATE_STATUS);
            pstmt.setString(1, status.name());
            pstmt.setInt(2, status.getNameId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException | DBException e) {
            LOG.error(Messages.ERR_CANNOT_CREATE_STATUS, e);
            throw new DBException(Messages.ERR_CANNOT_CREATE_STATUS, e);
        }
    }

    /**
     * Extracts a Status from the dataBase.
     *
     * @param id the characteristics of the Status
     *           by which student will be extracted from dataBase
     * @return status entity.
     */
    @Override
    public Status read(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_GET_STATUS_BY_ID);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                return extractStatus(rs);
        } catch (SQLException | DBException e) {
            LOG.error(Messages.ERR_CANNOT_READ_STATUS, e);
            throw new DBException(Messages.ERR_CANNOT_READ_STATUS, e);
        }
        return null;
    }

    /**
     * Updates the status entity in dataBase.
     *
     * @param status object of Status
     */
    @Override
    public void update(Status status) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_UPDATE_STATUS);
            pstmt.setString(1, status.name());
            pstmt.setInt(2, status.getNameId());
            pstmt.setInt(3, status.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_UPDATE_STATUS, e);
            throw new DBException(Messages.ERR_CANNOT_UPDATE_STATUS, e);
        }
    }

    /**
     * Deletes the status entity from dataBase.
     *
     * @param id id of object of Status
     */
    @Override
    public void delete(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_DELETE_STATUS_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_DELETE_STATUS, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_STATUS, ex);
        }
    }

    /**
     * Returns all statuses.
     *
     * @return List of status entities.
     */
    @Override
    public List<Status> getAll() throws DBException {
        List<Status> listOfStatuses = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLCommands.SQL_GET_STATUSES_ORDER_BY_ID);
            while (rs.next()) {
                listOfStatuses.add(extractStatus(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_ALL_STATUSES, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_ALL_STATUSES, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return listOfStatuses;
    }

    private Status extractStatus(ResultSet rs) throws DBException {
        try {
            Status status = Status.valueOf(rs.getString(Fields.NAME));
            status.setNameId(rs.getInt(Fields.NAME_ID));
            status.setId(rs.getInt(Fields.ID));
            return status;
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_STATUS, e);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_STATUS, e);
        }
    }
}
