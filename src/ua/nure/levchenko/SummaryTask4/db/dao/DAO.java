package ua.nure.levchenko.SummaryTask4.db.dao;

import ua.nure.levchenko.SummaryTask4.exception.DBException;

import java.sql.SQLException;
import java.util.List;


/**
 * Interface of CRUD
 * operations for DB entities.
 *
 * @author K.Levchenko
 */
public interface DAO<Entity, Key> {
    void create(Entity entity) throws SQLException, DBException;

    Entity read(Key key) throws DBException;

    void update(Entity entity) throws DBException;

    void delete(Key key) throws SQLException, DBException;

    List<Entity> getAll() throws DBException;
}
