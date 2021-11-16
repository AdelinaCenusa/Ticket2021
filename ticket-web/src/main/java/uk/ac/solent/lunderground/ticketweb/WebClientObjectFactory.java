package uk.ac.solent.lunderground.ticketweb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.solent.lunderground.clientrest.ConfigurationPoller;
import uk.ac.solent.lunderground.clientrest.RestClientObjectFactory;
import uk.ac.solent.lunderground.model.dto.Station;
import uk.ac.solent.lunderground.model.service.ServiceObjectFactory;
import uk.ac.solent.lunderground.model.service.TicketMachineFacade;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@WebListener
public class WebClientObjectFactory implements ServletContextListener
{
    /**
     * Logger instance for the WebClientObjectFactory implementation.
     */
    private static final Logger LOG = LogManager.getLogger(WebClientObjectFactory.class);

    /**
     * Time to wait before requesting first config update
     */
    private static final long INITIAL_DELAY = 0;
    /**
     * Time to wait between each subsequent config update request
     */
    private static final long POLL_PERIOD = 30;

    private static TicketMachineFacade ticketFacade = null;
    private static ServiceObjectFactory clientObjectFactory = null;
    private static ConfigurationPoller configPoller = null;

    public static String getTicketMachineUuid()
    {
        return configPoller.getTicketMachineUuid();
    }

    public static void setTicketMachineUuid()
    {
        // For testing purposes we're just generating a new, random, uuid
        // each and everytime this method is called.
        // In a real ticket machine this would be a value that is either
        // baked in during manufacturing, or generated on the first power
        // up and then persisted for reuse thereafter.
        configPoller.setTicketMachineUuid(UUID.randomUUID().toString());
    }

    public static String getStationName()
    {
        return configPoller.getStationName();
    }

    public static int getStationZone()
    {
        return configPoller.getStationZone();
    }

    public static List<Station> getStationList()
    {
        return configPoller.getStationList();
    }

    public static Date getLastUpdateAttempt()
    {
        return configPoller.getLastUpdateAttempt();
    }

    public static Date getLastUpdateTime()
    {
        return configPoller.getLastUpdateTime();
    }

    public static TicketMachineFacade getServiceFacade()
    {
        if (ticketFacade == null)
        {
            synchronized (WebClientObjectFactory.class)
            {
                if (ticketFacade == null)
                {
                    clientObjectFactory = new RestClientObjectFactory();
                    ticketFacade = clientObjectFactory.getTicketMachineFacade();

                    configPoller = new ConfigurationPoller(ticketFacade);
                    configPoller.init(INITIAL_DELAY, POLL_PERIOD);
                }
            }
        }
        return ticketFacade;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        LOG.debug("Ticket machine web app started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent)
    {
        LOG.debug("Ticket machine web app stopped");
        if (configPoller != null)
        {
            LOG.debug("Shutting down config poller");
            configPoller.shutdown();
        }
    }
}
