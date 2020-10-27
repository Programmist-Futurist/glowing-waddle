package ua.nure.levchenko.SummaryTask4.db;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.Messages;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class for getting connections with DB.
 *
 * @author K.Levchenko
 */
public class DBManager {
    private static final Logger LOG = Logger.getLogger(DBManager.class);
    private static DBManager instance;
    private final DataSource ds;

    private DBManager() throws DBException {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            ds = (DataSource) envContext.lookup("jdbc/test_final_project_db");
            LOG.trace("Data source ==> " + ds);
        } catch (NamingException ex) {
            LOG.error(Messages.ERR_CANNOT_GET_DATA_SOURCE, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_DATA_SOURCE, ex);
        }
    }

    public static synchronized DBManager getInstance() throws DBException {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    /**
     * Returns a DB connection from the Pool Connections. Before using this
     * method you must configure the Date Source and the Connections Pool in
     * WEB_APP_ROOT/META-INF/context.xml file.
     *
     * @return DB connection.
     */
    public Connection getConnection() throws DBException {
        Connection con;
        try {
            con = ds.getConnection();
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_GET_CONNECTION, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_CONNECTION, ex);
        }
        return con;
    }

}
