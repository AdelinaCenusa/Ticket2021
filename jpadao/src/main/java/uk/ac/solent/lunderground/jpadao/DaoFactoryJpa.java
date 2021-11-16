package uk.ac.solent.lunderground.jpadao;

import uk.ac.solent.lunderground.model.dao.DaoFactory;
import uk.ac.solent.lunderground.model.dao.StationDao;
import uk.ac.solent.lunderground.model.dao.TicketMachineDao;
import uk.ac.solent.lunderground.model.dao.TicketPricingDao;
import uk.ac.solent.lunderground.model.dao.ZoneDao;

import uk.ac.solent.lunderground.simpledao.ZoneDaoSimple;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class DaoFactoryJpa implements DaoFactory
{
    /**
     * Name of the persistence unit as required by the entity manager.
     */
    private static final String PERSISTENCE_UNIT_NAME = "lundergroundPersistence";

    /**
     * Factory used to create the entity manager used to persist the DAO entities.
     */
    private static EntityManagerFactory factory = null;

    /**
     * Entity manager used to persist the DAO entities.
     */
    private static EntityManager em = null;

    /**
     * DAO singleton used to access data related to the station entities.
     */
    private static StationDao stationDao;

    /**
     * DAO singleton used to access data related to the ticket machines.
     */
    private static TicketMachineDao ticketMachineDao;

    /**
     * DAO singleton used to access data related to the zones.
     */
    private static ZoneDao zoneDao;

    /**
     * Do a one time initialisation, across all instances of this class.
     * This sets up the entity manager used for persistence and creates the DAO objects
     */
    static {
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();

        stationDao = new StationDaoJpa(em);
        ticketMachineDao = new TicketMachineDaoJpa(em);
        zoneDao = new ZoneDaoSimple();
    }

    @Override
    public StationDao getStationDao()
    {
        return stationDao;
    }

    @Override
    public TicketMachineDao getTicketMachineDao()
    {
        return ticketMachineDao;
    }

    @Override
    public TicketPricingDao getTicketPricingDao()
    {
        // Not implemented
        return null;
    }

    @Override
    public ZoneDao getZoneDao()
    {
        return zoneDao;
    }

    @Override
    public void shutDown()
    {

    }
}
