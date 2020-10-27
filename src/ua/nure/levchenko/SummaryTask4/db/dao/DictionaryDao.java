package ua.nure.levchenko.SummaryTask4.db.dao;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.db.DBManager;
import ua.nure.levchenko.SummaryTask4.db.constants.Fields;
import ua.nure.levchenko.SummaryTask4.db.constants.SQLCommands;
import ua.nure.levchenko.SummaryTask4.entity.Dictionary;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.Messages;
import ua.nure.levchenko.SummaryTask4.internationalization.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements methods
 * for working with DB for Dictionary entity.
 *
 * @author K.Levchenko
 */
public class DictionaryDao implements DAO<Dictionary, Integer> {
    private static final Logger LOG = Logger.getLogger(DictionaryDao.class);

    private static DictionaryDao instance;
    private final DBManager dbManager;

    private Language language;

    private DictionaryDao() throws DBException {
        dbManager = DBManager.getInstance();
        language = Language.ENG;
    }

    public static synchronized DictionaryDao getInstance() throws DBException {
        if (instance == null) {
            instance = new DictionaryDao();
        }
        return instance;
    }

    /**
     * Creates the new word entity in dataBase.
     *
     * @param dictionary object of Dictionary
     */
    public void create(Dictionary dictionary) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_CREATE_DICTIONARY);
            pstmt.setString(1, dictionary.getEng());
            pstmt.setString(2, dictionary.getRus());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_CREATE_DICTIONARY, e);
            throw new DBException(Messages.ERR_CANNOT_CREATE_DICTIONARY, e);
        }
    }

    /**
     * Extracts the dictionary entity from dataBase.
     *
     * @param id the name of the dictionary that must be extracted
     * @return object of the Dictionary
     */
    @Override
    synchronized public Dictionary read(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_DICTIONARY_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next())
                return extractDictionary(rs);
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_READ_DICTIONARY, e);
            throw new DBException(Messages.ERR_CANNOT_READ_DICTIONARY, e);
        }
        return null;
    }

    /**
     * Updates the dictionary entity in dataBase.
     *
     * @param dictionary object of Dictionary
     */
    @Override
    public void update(Dictionary dictionary) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_UPDATE_DICTIONARY);
            pstmt.setString(1, dictionary.getEng());
            pstmt.setString(2, dictionary.getRus());
            pstmt.setInt(3, dictionary.getId());
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_UPDATE_DICTIONARY, e);
            throw new DBException(Messages.ERR_CANNOT_UPDATE_DICTIONARY, e);
        }
    }

    /**
     * Deletes the dictionary entity from dataBase.
     *
     * @param id id of object of Dictionary
     */
    @Override
    public void delete(Integer id) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_DELETE_DICTIONARY_BY_ID);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            LOG.error(Messages.ERR_CANNOT_DELETE_DICTIONARY, ex);
            throw new DBException(Messages.ERR_CANNOT_DELETE_DICTIONARY, ex);
        }
    }


    /**
     * Returns all journal entities.
     *
     * @return List of journal entities.
     */
    @Override
    synchronized public List<Dictionary> getAll() throws DBException {
        List<Dictionary> dictionaryList = new ArrayList<>();
        Connection con = null;
        try {
            con = dbManager.getConnection();
            PreparedStatement pstmt = con.prepareStatement(SQLCommands.SQL_GET_DICTIONARY_ORDER_BY_ID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                dictionaryList.add(extractDictionary(rs));
            }
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException e) {
                LOG.error(Messages.ERR_ROLLBACK, e);
            }
            LOG.error(Messages.ERR_CANNOT_GET_FULL_DICTIONARY, ex);
            throw new DBException(Messages.ERR_CANNOT_GET_FULL_DICTIONARY, ex);
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException exception) {
                LOG.error(Messages.ERR_CANNOT_CLOSE_CONNECTION, exception);
            }
        }
        return dictionaryList;
    }


    /**
     * Extracts the dictionary entity from dataBase.
     *
     * @param word the name of the dictionary that must be extracted
     * @return object of the Dictionary
     */
    synchronized public Dictionary read(String word) throws DBException {
        try (Connection con = dbManager.getConnection()) {
            PreparedStatement preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_DICTIONARY_BY_ENG_WORD);
            preparedStatement.setString(1, word);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return extractDictionary(rs);
            } else {
                preparedStatement.close();
                rs.close();
            }
            preparedStatement = con.prepareStatement(SQLCommands.SQL_READ_DICTIONARY_BY_RUS_WORD);
            preparedStatement.setString(1, word);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return extractDictionary(rs);
            }
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_READ_DICTIONARY, e);
            throw new DBException(Messages.ERR_CANNOT_READ_DICTIONARY, e);
        }
        return null;
    }


    synchronized private Dictionary extractDictionary(ResultSet rs) throws DBException {
        if (language == null) {
            language = Language.ENG;
        }
        try {
            Dictionary dictionary = new Dictionary();
            dictionary.setId(rs.getInt(Fields.ID));
            dictionary.setEng(rs.getString(Fields.DICTIONARY_ENG));
            dictionary.setRus(rs.getString(Fields.DICTIONARY_RUS));
            dictionary.setLanguage(language);
            return dictionary;
        } catch (SQLException e) {
            LOG.error(Messages.ERR_CANNOT_OBTAIN_DICTIONARY, e);
            throw new DBException(Messages.ERR_CANNOT_OBTAIN_DICTIONARY, e);
        }
    }


    public void setLanguage(Language language) {
        this.language = language;
    }

}
