package ua.nure.levchenko.SummaryTask4.services;

import org.apache.log4j.Logger;
import ua.nure.levchenko.SummaryTask4.entity.JournalEntity;
import ua.nure.levchenko.SummaryTask4.exception.DBException;
import ua.nure.levchenko.SummaryTask4.exception.ServiceException;
import ua.nure.levchenko.SummaryTask4.services.constants.Messages;

import java.util.List;

/**
 * Service for working with update operations in DB.
 *
 * @author K.Levchenko
 */
public class DeleteService extends Service {
    private static final long serialVersionUID = 7095995803527928502L;

    private static final Logger LOG = Logger.getLogger(DeleteService.class);

    private static DeleteService instance;

    private DeleteService() {
    }

    public static synchronized DeleteService getInstance() {
        if (instance == null) {
            instance = new DeleteService();
            LOG.debug("Service initialized");
        }
        return instance;
    }


    // methods

    /**
     * Deletes course entity from DB by id
     *
     * @param courseId id of particular course needed
     *                 to be deleted from DB
     */
    public void deleteCourse(int courseId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            courseDao.delete(courseId);
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_COURSE_DELETE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_COURSE_DELETE_ERROR, e);
        }
        LOG.debug("Service finish");
    }

    /**
     * Deletes journal entity from DB by course and student id
     *
     * @param studentId id of student whose journal entity we
     *                  need to delete from current course
     * @param courseId  id of particular course which journal
     *                  entity needed to be deleted from DB
     */
    public void deleteJournalEntityByCourse(int studentId, int courseId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            List<JournalEntity> journalEntitiesOfStudent = journalEntityDao.getAllByStudentId(studentId);

            for (JournalEntity journalEntity : journalEntitiesOfStudent) {
                if (courseId == journalEntity.getCourseId()) {
                    journalEntityDao.delete(journalEntity.getId());
                }
            }
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_JOURNAL_ENTITY_DELETE_BY_COURSE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_JOURNAL_ENTITY_DELETE_BY_COURSE_ERROR, e);
        }
        LOG.debug("Service finish");
    }

    /**
     * Deletes journal entity from DB by id
     *
     * @param journalId id of particular journal entity
     *                  needed to be deleted from DB
     */
    public void deleteJournalEntity(int journalId) throws ServiceException {
        LOG.debug("Service starts");
        try {
            journalEntityDao.delete(journalId);
        } catch (DBException | NullPointerException | ClassCastException e) {
            LOG.error(Messages.SERVICE_JOURNAL_ENTITY_DELETE_ERROR, e);
            throw new ServiceException(Messages.SERVICE_JOURNAL_ENTITY_DELETE_ERROR, e);
        }
        LOG.debug("Service finish");
    }
}
