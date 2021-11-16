package uk.ac.solent.lunderground.jaxbdao;

import uk.ac.solent.lunderground.model.dao.DaoFactory;
import uk.ac.solent.lunderground.model.dao.StationDao;
import uk.ac.solent.lunderground.model.dao.TicketMachineDao;
import uk.ac.solent.lunderground.model.dao.TicketPricingDao;
import uk.ac.solent.lunderground.model.dao.ZoneDao;
import uk.ac.solent.lunderground.simpledao.ZoneDaoSimple;

public class DaoFactoryJaxb implements DaoFactory
{
    /**
     * URL to the master station list.
     */
    private final String STATION_XML = "londonStations.xml";
    /**
     * URL to the master schedule and pricing list.
     */
    private final String PRICING_XML = "ticketPricing.xml";

    /**
     * DAO singleton used to access data related to the station entities.
     */
    private static StationDao stationDao;

    /**
     * DAO singleton used to access data related to the schedule and pricing entities.
     */
    private static TicketPricingDao ticketPricingDao;

    /**
     * DAO singleton used to access data related to the zones.
     */
    private static ZoneDao zoneDao;


    @Override
    public StationDao getStationDao()
    {
        if (stationDao == null)
        {
            synchronized (this)
            {
                if (stationDao == null)
                {
                    try
                    {
                        StationDaoJaxb stationDaoJaxb = new StationDaoJaxb(STATION_XML);
                        stationDaoJaxb.load();
                        stationDao = stationDaoJaxb;
                    }
                    catch (Exception ex)
                    {
                        throw new RuntimeException("problem creating StationDaoJaxb ", ex);
                    }
                }
            }
        }
        return stationDao;
    }

    @Override
    public ZoneDao getZoneDao()
    {
        if (zoneDao == null)
        {
            synchronized (this)
            {
                if (zoneDao == null)
                {
                    // This is not ideal but creating a new DaoFactory in the SimpleDao module
                    // just for this one simple DAO instance would consume a lot of time for
                    // little immediate benefit. Should more simple DAOs be required then
                    // this decision can be revisited.
                    zoneDao = new ZoneDaoSimple();
                }
            }
        }
        return zoneDao;
    }

    @Override
    public TicketPricingDao getTicketPricingDao()
    {
        if (ticketPricingDao == null)
        {
            synchronized (this)
            {
                if (ticketPricingDao == null)
                {
                    TicketPricingDaoJaxb ticketPricingDaoJaxb = new TicketPricingDaoJaxb(PRICING_XML);
                    ticketPricingDaoJaxb.load();
                    ticketPricingDao = ticketPricingDaoJaxb;
                }
            }
        }
        return ticketPricingDao;
    }

    @Override
    public TicketMachineDao getTicketMachineDao()
    {
        return null;
    }

    @Override
    public void shutDown()
    {

    }
}
