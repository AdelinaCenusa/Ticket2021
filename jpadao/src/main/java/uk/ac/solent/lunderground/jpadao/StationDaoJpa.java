package uk.ac.solent.lunderground.jpadao;

import uk.ac.solent.lunderground.model.dao.StationDao;
import uk.ac.solent.lunderground.model.dto.Station;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.List;

public final class StationDaoJpa implements StationDao
{
    /**
     * Entity manager used for persistence.
     */
    private final EntityManager entityManager;

    /**
     * Public constructor for the StationDAO JPA implementation.
     *
     * @param em Entity manager required for persistence
     */
    @NotNull
    public StationDaoJpa(final EntityManager em)
    {
        this.entityManager = em;
    }

    @Override
    public List<Station> retrieveAll()
    {
        TypedQuery<Station> q = entityManager.createQuery("SELECT s FROM Station s", Station.class);
        return q.getResultList();
    }

    @Override
    public void addStation(@NotNull final Station newStation)
    {
        entityManager.getTransaction()
                     .begin();
        entityManager.persist(newStation);
        entityManager.getTransaction()
                     .commit();
    }

    @Override
    public void setStationList(List<Station> stationList)
    {
        for (Station station : stationList)
        {
            addStation(station);
        }
    }

    @Override
    public void deleteStation(final long stationId)
    {
        entityManager.getTransaction()
                     .begin();
        entityManager.createQuery("DELETE FROM Station s WHERE s.id=:id")
                     .setParameter("id", stationId)
                     .executeUpdate();
        entityManager.getTransaction()
                     .commit();
    }

    @Override
    public void deleteAll()
    {
        entityManager.getTransaction()
                     .begin();
        entityManager.createQuery("DELETE FROM Station ")
                     .executeUpdate();
        entityManager.getTransaction()
                     .commit();
    }

    @Override
    public Station getStation(final String stationName)
    {
        TypedQuery<Station> q = entityManager.createQuery("SELECT s FROM Station s WHERE s.name=:name",
                Station.class)
                                             .setParameter("name", stationName);
        return runGetStationQuery(q);
    }

    @Override
    public Station getStation(final Long stationId)
    {
        TypedQuery<Station> q = entityManager.createQuery("SELECT s FROM Station s WHERE s.id=:id",
                Station.class)
                                             .setParameter("id", stationId);
        return runGetStationQuery(q);
    }

    @Override
    public void updateStation(final Station newDetails)
    {
        entityManager.merge(newDetails);
    }

    private Station runGetStationQuery(final TypedQuery<Station> q)
    {
        Station result;
        try
        {
            result = q.getSingleResult();
        }
        catch (NoResultException ex)
        {
            result = null;
        }
        return result;
    }
}
