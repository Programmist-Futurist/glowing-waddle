package ua.nure.levchenko.SummaryTask4.db.dao;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.DBManager;
import ua.nure.levchenko.SummaryTask4.db.constants.Fields;
import ua.nure.levchenko.SummaryTask4.db.constants.SQLCommands;
import ua.nure.levchenko.SummaryTask4.entity.Role;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.Messages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements methods
 * for working with DB for Role entity.
 *
 * @author K.Levchenko
 */
public class RoleDao implements DAO<Role, Integer> {
    private static final Logger LOG = Logger.getLogger(RoleDao.class);

    private static RoleDao instance;
    private final DBManager dbManager;

    private RoleDao() throws DBException {
        dbManager = DBManager.getInstance();
    }

    public static synchronized RoleDao getInstance() throws DBException {
        if (instance == null) {
            instance = new RoleDao();
        }
        return instance;
    }

    /**
     * Creates a role entity in dataBase.
     *
     * @param role object of Role
     */
    @Override
    public void create(Role role) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_CREATE_ROLE);
            pstmt.setString(1, role.getName());
            pstmt.setInt(2, role.getNameId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_CREATE_STATUS, e);
            throw new DBException(Messages.ERR_CANNOT_CREATE_STATUS, e);
        }
    }

    /**
     * Extracts the rote from the dataBase.
     *
     * @param id the characteristics of the role
     *           by which role will be extracted from dataBase
     * @return role entity.
     */
    @Override
    public Role read(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_READ_ROLE_BY_ID);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                return extractRole(rs);
        } catch (SQLException | DBException e) {
            LOG.error(Messages.ERR_CANNOT_READ_STATUS, e);
            throw new DBException(Messages.ERR_CANNOT_READ_STATUS, e);
        }
        return null;
    }

    /**
     * Updates the role entity in dataBase.
     *
     * @param role object of Role
     */
    @Override
    public void update(Role role) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_UPDATE_ROLE);
            pstmt.setString(1, role.getName());
            pstmt.setInt(2, role.getNameId());
            pstmt.setInt(3, role.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException | DBException e) {
            LOG.error(Messages.ERR_CANNOT_UPDATE_STATUS, e);
            throw new DBException(Messages.ERR_CANNOT_UPDATE_STATUS, e);
        }
    }

    /**
     * Deletes the role entity from dataBase.
     *
     * @param id object of Role
     */
    @Override
    public void delete(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_DELETE_ROLE_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException | DBException ex) {
            LOG.error(Messages.ERR_CANNOT_DELETE_STATUS, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_STATUS, ex);
        }
    }

    /**
     * Returns all Roles.
     *
     * @return List of role entities.
     */
    @Override
    public List<Role> getAll() throws DBException {
        List<Role> listOfRoles = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLCommands.SQL_GET_ROLES_ORDER_BY_ID);
            while (rs.next()) {
                listOfRoles.add(extractRole(rs));
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
        return listOfRoles;
    }

    private Role extractRole(ResultSet rs) throws DBException {
        try {
            Role role = Role.valueOf(rs.getString(Fields.NAME));
            role.setId(rs.getInt(Fields.ID));
            role.setNameId(rs.getInt(Fields.NAME_ID));
            return role;
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_ROLE, e);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_ROLE, e);
        }
    }
}
