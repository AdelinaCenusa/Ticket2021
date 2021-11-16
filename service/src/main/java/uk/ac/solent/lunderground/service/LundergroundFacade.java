package uk.ac.solent.lunderground.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.solent.lunderground.model.dao.StationDao;
import uk.ac.solent.lunderground.model.dao.TicketMachineDao;
import uk.ac.solent.lunderground.model.dao.TicketPricingDao;
import uk.ac.solent.lunderground.model.dao.ZoneDao;
import uk.ac.solent.lunderground.model.dto.Station;
import uk.ac.solent.lunderground.model.dto.TicketMachine;
import uk.ac.solent.lunderground.model.dto.TicketMachineConfig;
import uk.ac.solent.lunderground.model.service.DeveloperFacade;
import uk.ac.solent.lunderground.model.service.LundergroundServiceFacade;

import java.util.Date;
import java.util.List;

public final class LundergroundFacade implements LundergroundServiceFacade, DeveloperFacade
{
    /**
     * Logger instance for the Lunderground implementation.
     */
    private static final Logger LOG = LogManager.getLogger(LundergroundFacade.class);

    /**
     * StationDao instance used to access the database implementation.
     */
    private StationDao stationDao = null;

    /**
     * ZoneDao instance used to access the database implementation.
     */
    private ZoneDao zoneDao = null;

    /**
     * TicketMachineDao instance used to access the database implementation.
     */
    private TicketMachineDao ticketMachineDao = null;

    /**
     * TicketPricingDao instance used during development activities.
     */
    private TicketPricingDao pricingDao = null;

    /**
     * StationDao instance used during development activities.
     */
    private StationDao devStationDao = null;

    /**
     * Set the StationDao instance to use for accessing the database layer.
     *
     * @param newStationDao StationDao instance to set
     */
    public void setStationDao(final StationDao newStationDao)
    {
        this.stationDao = newStationDao;
    }

    /**
     * Set the ZoneDao instance to use for accessing the database layer.
     *
     * @param newZoneDao ZoneDao to use
     */
    public void setZoneDao(final ZoneDao newZoneDao)
    {
        this.zoneDao = newZoneDao;
    }

    public void setTicketMachineDao(final TicketMachineDao newTicketMachineDao)
    {
        this.ticketMachineDao = newTicketMachineDao;
    }

    @Override
    public Station getStation(final String stationName)
    {
        return stationDao.getStation(stationName);
    }

    @Override
    public Station getStation(final long stationId)
    {
        return stationDao.getStation(stationId);
    }

    @Override
    public List<Station> getAllStations()
    {
        return stationDao.retrieveAll();
    }

    @Override
    public void addStation(final String stationName, final int zoneNumber)
    {
        Station station = new Station();
        station.setName(stationName);
        station.setZone(zoneNumber);
        stationDao.addStation(station);
    }

    @Override
    public void deleteStation(final long stationId)
    {
        // TODO: remove any dependant ticket machines first
        stationDao.deleteStation(stationId);
    }

    @Override
    public void deleteAllStations()
    {
        stationDao.deleteAll();
    }

    @Override
    public void updateStation(final long stationId, final String newName, final int newZone)
    {
        // TODO: What happens if station name changes and it already has ticket machines associated with it.
        Station station = new Station();
        station.setId(stationId);
        station.setName(newName);
        station.setZone(newZone);
        stationDao.updateStation(station);
    }

    @Override
    public List<Integer> getAllZones()
    {
        return zoneDao.retrieveAll();
    }

    @Override
    public TicketMachineConfig getTicketMachineConfig(String uuid)
    {
        TicketMachineConfig config = new TicketMachineConfig();
        TicketMachine ticketMachine = ticketMachineDao.getTicketMachine(uuid);

        if (ticketMachine != null)
        {
            config.setUuid(ticketMachine.getUuid());
            config.setStation(ticketMachine.getStation());
        }
        config.setStationList(stationDao.retrieveAll());
        config.setPricingDetails(pricingDao.getPricingDetails());
        LOG.debug("Prepared config for ticket machine UUID: " + uuid);
        LOG.debug(config.toString());
        return config;
    }

    @Override
    public void addTicketMachine(TicketMachine newTicketMachine)
    {
        ticketMachineDao.addTicketMachine(newTicketMachine);
    }

    @Override
    public TicketMachine getTicketMachine(String uuid)
    {
        return ticketMachineDao.getTicketMachine(uuid);
    }

    @Override
    public void updateTicketMachine(TicketMachine ticketMachine)
    {
        ticketMachineDao.updateTicketMachine(ticketMachine);
    }

    @Override
    public void setDevStationDao(StationDao newStationDao)
    {
        this.devStationDao = newStationDao;
    }

    public void setPricingDao(TicketPricingDao newTicketPricingDao)
    {
        this.pricingDao = newTicketPricingDao;
    }

    /**
     * Reinitialise the Lundeground system.
     * Removes all ticket machine instances and stations
     */
    @Override
    public void reinitialise()
    {
        List<Station> stationList = devStationDao.retrieveAll();

        // Remove ticket machines before stations.
        // Trying to delete a station that has ticket machines assigned can cause issues
        ticketMachineDao.deleteAll();
        stationDao.deleteAll();
        for (Station station: stationList)
        {
            stationDao.addStation(station);
        }
    }
}
