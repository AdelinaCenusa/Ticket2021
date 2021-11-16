package uk.ac.solent.lunderground.ticketweb.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.solent.lunderground.model.dao.StationDao;
import uk.ac.solent.lunderground.model.dto.Station;
import uk.ac.solent.lunderground.model.service.TicketMachineFacade;
import uk.ac.solent.lunderground.ticketweb.WebClientObjectFactory;

@Controller
public class GateController
{
    /**
     * Logger instance for the GateController implementation.
     */
    private static final Logger LOG = LogManager.getLogger(GateController.class);

    private static final String ENTRY_GATE = "entry";
    private static final String EXIT_GATE  = "exit";

    @RequestMapping(value = "/gate", method = RequestMethod.GET)
    public String getGateMachine(final ModelMap map,
                  @RequestParam(value = "gateOpen", required = false, defaultValue = "false") final Boolean gateOpen)
    {
        TicketMachineFacade facade = WebClientObjectFactory.getServiceFacade();

        map.addAttribute("stationList", WebClientObjectFactory.getStationList());
        map.addAttribute("gateOpen", gateOpen);
        return "gate-machine";
    }

    @RequestMapping(value = "/opengate", method = RequestMethod.POST)
    public String validateTicket(final RedirectAttributes redirectAttributes,
                                 @RequestParam("access") final String gateAccess,
                                 @RequestParam("stationName") final String stationName,
                                 @RequestParam("hour") final int hour,
                                 @RequestParam("minutes") final int minutes,
                                 @RequestParam("ticketXml") final String ticketXml)
    {
        TicketMachineFacade facade = WebClientObjectFactory.getServiceFacade();
        StationDao stationDao = facade.getStationDao();
        Station gateStation = stationDao.getStation(stationName);
        boolean gateOpen = facade.verifyGateAccess(ticketXml, gateStation.getZone(), hour, minutes);

        if (gateAccess.equals(ENTRY_GATE))
        {
            LOG.debug("Entry gate accessed");
        }
        else if (gateAccess.equals(EXIT_GATE))
        {
            LOG.debug("Exit gate accessed");
        }
        else
        {
            // Belt and braces assertion for gate to remain closed
            gateOpen = false;
            LOG.error("Invalid Gate Access Request:" + gateAccess);
        }
        LOG.debug("Gate opened: " + gateOpen);

        redirectAttributes.addAttribute("gateOpen", gateOpen);
        return "redirect:/gate";
    }
}
