package uk.ac.solent.lunderground.controllerweb.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.solent.lunderground.controllerweb.WebObjectFactory;
import uk.ac.solent.lunderground.model.dto.Station;
import uk.ac.solent.lunderground.model.dto.TicketMachine;
import uk.ac.solent.lunderground.model.dto.TicketMachineConfig;
import uk.ac.solent.lunderground.model.service.LundergroundServiceFacade;

@RestController
public class RestEndPointController
{
    /**
     * Logger instance for the RestEndPointController implementation.
     */
    private static final Logger LOG = LogManager.getLogger(RestEndPointController.class);

    private LundergroundServiceFacade lunderGroundFacade = null;

    @GetMapping(value = "/rest/v1/station/{stationName}")
    public Station getStation(@PathVariable final String stationName)
    {
        LOG.debug("Requested station with name: " + stationName);
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();

        return lunderGroundFacade.getStation(stationName);
    }

    @GetMapping(value = "/rest/v1/ticketMachineConfig/{uuid}")
    public TicketMachineConfig getTicketMachineConfig(@PathVariable final String uuid)
    {
        LOG.debug("Requested ticket machine config for machine with uuid: " + uuid);
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();

        TicketMachineConfig config = lunderGroundFacade.getTicketMachineConfig(uuid);

        return config;
    }

    @PostMapping(value = "/rest/v1/ticketMachine")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketMachine addTicketMachine(@RequestBody final TicketMachine ticketMachine)
    {
        LOG.debug("Add ticket machine with uuid: " + ticketMachine.getUuid());
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        lunderGroundFacade.addTicketMachine(ticketMachine);

        return lunderGroundFacade.getTicketMachine(ticketMachine.getUuid());
    }

    @GetMapping(value = "/rest/v1/ticketMachine/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public TicketMachine getTicketMachine(@PathVariable final String uuid)
    {
        LOG.debug("Get ticket machine with uuid: " + uuid);
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        return lunderGroundFacade.getTicketMachine(uuid);
    }

    @PutMapping(value = "/rest/v1/ticketMachine/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public void updateTicketMachine(@PathVariable final String uuid,
                                    @RequestBody final TicketMachine ticketMachine)
    {
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        TicketMachine updatedMachine = this.lunderGroundFacade.getTicketMachine(ticketMachine.getUuid());
        updatedMachine.setStation(ticketMachine.getStation());

        LOG.debug("Rest uuid: " + uuid);
        LOG.debug("Update ticket machine with ID: " + updatedMachine.getId());
        LOG.debug("Update ticket machine with uuid: " + updatedMachine.getUuid());
        LOG.debug("Set station ID to " + updatedMachine.getStation().getId());
        LOG.debug("Set station name to " + updatedMachine.getStation().getName());
        LOG.debug("Set station zone to " + updatedMachine.getStation().getZone());

        lunderGroundFacade.updateTicketMachine(updatedMachine);
    }
}
