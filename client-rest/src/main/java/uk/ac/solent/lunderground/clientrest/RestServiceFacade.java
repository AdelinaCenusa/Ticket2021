package uk.ac.solent.lunderground.clientrest;

import org.springframework.web.client.RestTemplate;
import uk.ac.solent.lunderground.jaxbdao.StationDaoJaxb;
import uk.ac.solent.lunderground.jaxbdao.TicketDaoJaxb;
import uk.ac.solent.lunderground.jaxbdao.TicketPricingDaoJaxb;
import uk.ac.solent.lunderground.model.dao.StationDao;
import uk.ac.solent.lunderground.model.dao.TicketDao;
import uk.ac.solent.lunderground.model.dao.TicketPricingDao;
import uk.ac.solent.lunderground.model.dto.Station;
import uk.ac.solent.lunderground.model.dto.Ticket;
import uk.ac.solent.lunderground.model.dto.TicketMachine;
import uk.ac.solent.lunderground.model.dto.TicketMachineConfig;
import uk.ac.solent.lunderground.model.service.TicketMachineFacade;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestServiceFacade implements TicketMachineFacade
{
    private final static String TMP_DIR = System.getProperty("java.io.tmpdir");
    private final static String STATION_PATH = TMP_DIR + File.separator
                                               + "ticket_machine_client" + File.separator + "clientStationList.xml";
    private final static String PRICING_PATH = TMP_DIR + File.separator
                                               + "ticket_machine_client" + File.separator + "clientPricingDetails.xml";

    private static StationDao stationDAO = null;
    private static TicketPricingDao ticketPricingDao = null;
    private static TicketDao ticketDao = null;

    private Runnable configChangedCallback = null;
    /**
     *
     * instance for the RestServiceFacade implementation.
     */
    private static final Logger LOG = LogManager.getLogger(RestServiceFacade.class);

    private final String baseUrl;

    public RestServiceFacade(final String baseRestUrl)
    {
        this.baseUrl = baseRestUrl;
    }

    @Override
    public void setTicketMachineConfigChangedCallback(final Runnable callback)
    {
        configChangedCallback = callback;
    }

    @Override
    public TicketMachineConfig getTicketMachineConfig(final String uuid)
    {
        final String uri = baseUrl + "ticketMachineConfig/{uuid}";

        RestTemplate template = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);

        // We use a get as we are retrieving information
        
        
        return template.getForObject(uri, TicketMachineConfig.class, params);
    }

    @Override
    public void addTicketMachine(final String uuid)
    {
        final String uri = baseUrl + "ticketMachine";

        RestTemplate template = new RestTemplate();

        TicketMachine newMachine = new TicketMachine();
        newMachine.setUuid(uuid);

        TicketMachine result = template.postForObject(uri, newMachine, TicketMachine.class);
        LOG.info("added new ticket machine: " + result);
        configUpdated();
    }

    @Override
    public void updateTicketMachine(final String uuid, final String stationName)
    {
        final String uri = baseUrl + "ticketMachine/{uuid}";

        RestTemplate template = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);

        TicketMachine tm = getTicketMachine(uuid);
        Station station = getStation(stationName);
        tm.setStation(station);

        // We use a put as we're updating an existing station
        
        
        template.put(uri, tm, params);
        configUpdated();
    }

    private TicketMachine getTicketMachine(final String uuid)
    {
        final String uri = baseUrl + "ticketMachine/{uuid}";

        RestTemplate template = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("uuid", uuid);

        // We use a get as we are retrieving information
        
        
        
        return template.getForObject(uri, TicketMachine.class, params);
    }

    private Station getStation(final String stationName)
    {
        final String uri = baseUrl + "station/{name}";

        RestTemplate template = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);

        // We use a get as we are retrieving information
        
        
        return template.getForObject(uri, Station.class, params);
    }

    @Override
    public Ticket getTicket(final String startStation, final String destStation)
    {
        return getTicket(startStation, destStation, new Date());
    }

    @Override
    public Ticket getTicket(final String startStation, final String destStation, final Date issueDate)
    {
        stationDAO = getStationDao();
        ticketPricingDao = getTicketPricingDao();
        ticketDao = getTicketDao();

        Ticket ticket = new Ticket();
        ticket.setId(UUID.randomUUID().toString());
        ticket.setValidFrom(issueDate);
        ticket.setValidTo(ticketPricingDao.getExpiryDate(ticket.getValidFrom()));
        ticket.setStartStation(stationDAO.getStation(startStation));
        ticket.setDestStation(stationDAO.getStation(destStation));
        ticket.setRateBand(ticketPricingDao.getRateBand(ticket.getValidFrom()));
        ticket.setPrice(ticketPricingDao.getJourneyPrice(ticket.getStartStation(),
                ticket.getDestStation(),
                ticket.getValidFrom()));

        return ticket;
    }

    @Override
    public String encodeTicket(final Ticket ticket)
    {
        ticketDao = getTicketDao();
        return ticketDao.encodeTicket(ticket);
    }

    @Override
    public Boolean verifyGateAccess(final String ticketXml, final int gateZone, final int hour, final int minutes)
    {
        ticketDao = getTicketDao();
        boolean gateOpen;

        Date date = getDate(hour, minutes);
        Ticket ticket = ticketDao.getTicket(ticketXml);
        if ((ticket != null) && ticketDao.validateTicket(ticket))
        {
            LOG.debug("Ticket Validation passed");

            List<Integer> journeyZones = getJourneyZones(ticket);
            gateOpen = journeyZones.contains(gateZone);
            LOG.debug("Gate inside zone: " + gateOpen);

            gateOpen &= ticket.getValidFrom().before(date);
            gateOpen &= ticket.getValidTo().after(date);
            LOG.debug("ValidFrom: " + ticket.getValidFrom());
            LOG.debug("ValidTo:   " + ticket.getValidTo());
            LOG.debug("Gate Time: " + date);
            LOG.debug("Gate time inside valid range: " + gateOpen);
        }
        else
        {
            LOG.debug("Ticket  not valid");
            LOG.debug(ticketXml);
            gateOpen = false;
        }

        return gateOpen;
    }

    /**
     * Return a list with the zones where the ticket is valid
     * @param ticket Ticket instance to determine the journey zones for
     * @return A list of the zones that the ticket allows travel within
     */
    private List<Integer> getJourneyZones(final Ticket ticket)
    {
        return IntStream.rangeClosed(ticket.getStartStation().getZone(),
                                     ticket.getDestStation().getZone())
                        .boxed()
                        .collect(Collectors.toList());
    }

    private Date getDate(final int hour, final int minute)
    {
        Calendar refDate = new GregorianCalendar();
        refDate.setTime(new Date());

        Calendar calendar = new GregorianCalendar();
        calendar.set(refDate.get(Calendar.YEAR), refDate.get(Calendar.MONTH), refDate.get(Calendar.DAY_OF_MONTH),
                hour, minute, refDate.get(Calendar.SECOND));
        return calendar.getTime();
    }

    /**
     * Execute the registered callback.
     */
    private void configUpdated()
    {
        if (configChangedCallback != null)
        {
            configChangedCallback.run();
        }
    }

    /**
     * Get an instance of a StationDao for the rest client instance to use
     *
     * @return
     */
    @Override
    
    public StationDao getStationDao()
    {
        if (stationDAO == null)
        {
            synchronized (this)
            {
                if (stationDAO == null)
                {
                    LOG.debug("creating new StationDao");
                    stationDAO = new StationDaoJaxb(STATION_PATH);
                }
            }
        }
        return stationDAO;
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
                    LOG.debug("creating new priceCalculatorDao");
                    ticketPricingDao = new TicketPricingDaoJaxb(PRICING_PATH);
                }
            }
        }
        return ticketPricingDao;
    }

    private TicketDao getTicketDao()
    {
        if (ticketDao == null)
        {
            synchronized (this)
            {
                if (ticketDao == null)
                {
                    LOG.debug("creating new ticketDao");
                    ticketDao = new TicketDaoJaxb();
                }
            }
        }
        return ticketDao;
    }
}
