package uk.ac.solent.lunderground.ticketweb.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import uk.ac.solent.lunderground.model.dto.Ticket;
import uk.ac.solent.lunderground.model.service.TicketMachineFacade;
import uk.ac.solent.lunderground.ticketweb.WebClientObjectFactory;

import java.util.Date;

/**
 * Controller for the ticket sales page(s).
 */
@Controller
public class TicketSalesController
{
    /**
     * Logger instance for the TicketSalesController implementation.
     */
    private static final Logger LOG = LogManager.getLogger(TicketSalesController.class);

    @RequestMapping(value = "/sales", method = RequestMethod.GET)
    public String getSalesPage(final ModelMap map)
    {
        TicketMachineFacade facade = WebClientObjectFactory.getServiceFacade();
        map.addAttribute("startStation", WebClientObjectFactory.getStationName());
        map.addAttribute("startZone", WebClientObjectFactory.getStationZone());
        map.addAttribute("stationList", WebClientObjectFactory.getStationList());

        return "ticket-sales";
    }

    @RequestMapping(value="/checkout", method=RequestMethod.POST)
    public String checkoutPage(final ModelMap map, @RequestParam("stationName") final String destination)
    {
        TicketMachineFacade facade = WebClientObjectFactory.getServiceFacade();
        Ticket ticket = facade.getTicket(WebClientObjectFactory.getStationName(), destination);

        map.addAttribute("ticket", ticket);

        return "confirm-sale";
    }

    @RequestMapping(value = "/buyTicket", method = RequestMethod.POST)
    public String buyTicket(final ModelMap map,
                            @RequestParam("cardValid") final boolean cardValid,
                            @RequestParam("startStation") final String startStation,
                            @RequestParam("destStation") final String destStation,
                            @RequestParam("issueDate") final Date issueDate)
    {
        TicketMachineFacade facade = WebClientObjectFactory.getServiceFacade();
        Ticket ticket = facade.getTicket(startStation, destStation, issueDate);

        map.addAttribute("xmlTicket", "Payment Card Not Valid - Sale Cancelled");
        if (cardValid)
        {
            String ticketXml = facade.encodeTicket(ticket);
            // We only add a validation code if the payment card is valid
            map.addAttribute("xmlTicket", ticketXml);
            LOG.debug("Ticket sold");
            LOG.info(ticketXml);
        }
        map.addAttribute("ticket", ticket);
        return "issued-ticket";
    }
}
