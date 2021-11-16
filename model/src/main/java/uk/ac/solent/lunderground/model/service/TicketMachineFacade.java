package uk.ac.solent.lunderground.model.service;

import uk.ac.solent.lunderground.model.dao.StationDao;
import uk.ac.solent.lunderground.model.dao.TicketPricingDao;
import uk.ac.solent.lunderground.model.dto.Ticket;
import uk.ac.solent.lunderground.model.dto.TicketMachineConfig;

import java.util.Date;

public interface TicketMachineFacade
{
    /**
     * Create and add a new ticket machine instance.
     * @param uuid The uuid of the new machine to create and add.
     */
    void addTicketMachine(String uuid);

    String encodeTicket(Ticket ticket);

    StationDao getStationDao();

    Ticket getTicket(String startStation, String destStation);
    Ticket getTicket(String startStation, String destStation, Date issueDate);

    TicketMachineConfig getTicketMachineConfig(String uuid);
    TicketPricingDao getTicketPricingDao();

    /**
     * register a callback method to run when the ticket machine config is changed.
     * @param configChangedCallback the method to run when the callback is triggered
     */
    void setTicketMachineConfigChangedCallback(Runnable configChangedCallback);

    /**
     * Update the ticket machine with the passed in uuid.
     * @param uuid uuid of the ticket machine to update
     * @param stationName name of the station to set in the ticket machine
     */
    void updateTicketMachine(String uuid, String stationName);


    // Here for testing purposes
    Boolean verifyGateAccess(String ticketXml, int stationZone, int hour, int minutes);
}
