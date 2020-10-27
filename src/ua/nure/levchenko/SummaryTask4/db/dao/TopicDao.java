package ua.nure.levchenko.SummaryTask4.db.dao;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.DBManager;
import ua.nure.levchenko.SummaryTask4.db.constants.Fields;
import ua.nure.levchenko.SummaryTask4.db.constants.SQLCommands;
import ua.nure.levchenko.SummaryTask4.entity.Topic;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.Messages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements methods
 * for working with DB for Topic entity.
 *
 * @author K.Levchenko
 */
public class TopicDao implements DAO<Topic, Integer> {
    private static final Logger LOG = Logger.getLogger(StudentDao.class);

    private static TopicDao instance;
    private final DBManager dbManager;

    private TopicDao() throws DBException {
        dbManager = DBManager.getInstance();
    }

    public static synchronized TopicDao getInstance() throws DBException {
        if (instance == null) {
            instance = new TopicDao();
        }
        return instance;
    }

    /**
     * Creates the Topic entity in dataBase.
     *
     * @param topic object of Topic
     */
    @Override
    public void create(Topic topic) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_CREATE_TOPIC);
            pstmt.setString(1, topic.name());
            pstmt.setInt(2, topic.getNameId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_CREATE_TOPIC, e);
            throw new DBException(Messages.ERR_CANNOT_CREATE_TOPIC, e);
        }
    }

    /**
     * Extracts the Topic entity from the dataBase.
     *
     * @param id the characteristics of the Topic
     *           by which topic will be extracted from dataBase
     * @return topic entity.
     */
    @Override
    public Topic read(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_GET_TOPIC_BY_ID);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next())
                return extractTopic(rs);
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_READ_TOPIC, e);
            throw new DBException(Messages.ERR_CANNOT_READ_TOPIC, e);
        }
        return null;
    }


    /**
     * Updates the Topic entity in dataBase.
     *
     * @param topic object of Topic
     */
    @Override
    public void update(Topic topic) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_UPDATE_TOPIC);
            pstmt.setString(1, topic.name());
            pstmt.setInt(2, topic.getNameId());
            pstmt.setInt(3, topic.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_UPDATE_TOPIC, e);
            throw new DBException(Messages.ERR_CANNOT_UPDATE_TOPIC, e);
        }
    }

    /**
     * Deletes the topic entity from dataBase.
     *
     * @param id object of Topic
     */
    @Override
    public void delete(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_DELETE_TOPIC_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
            LOG.error(Messages.ERR_CANNOT_DELETE_TOPIC, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_TOPIC, ex);
        }
    }


    /**
     * Returns all topics.
     *
     * @return List of topic entities.
     */
    @Override
    public List<Topic> getAll() throws DBException {
        List<Topic> listOfTopics = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SQLCommands.SQL_GET_TOPICS_ORDER_BY_ID);
            while (rs.next()) {
                listOfTopics.add(extractTopic(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, ex);
            }
            LOG.error(Messages.ERR_CANNOT_GET_ALL_TOPICS, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_ALL_TOPICS, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return listOfTopics;
    }

    private Topic extractTopic(ResultSet rs) throws DBException {
        try {
            Topic topic = Topic.valueOf(rs.getString(Fields.NAME));
            topic.setNameId(rs.getInt(Fields.NAME_ID));
            topic.setId(rs.getInt(Fields.ID));
            return topic;
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_TOPIC, e);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_TOPIC, e);
        }
    }
}
