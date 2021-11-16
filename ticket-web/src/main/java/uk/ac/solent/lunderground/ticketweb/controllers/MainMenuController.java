package uk.ac.solent.lunderground.ticketweb.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.ac.solent.lunderground.model.service.TicketMachineFacade;
import uk.ac.solent.lunderground.ticketweb.WebClientObjectFactory;

/**
 * This is the controller for the main landing page http://localhost/ticket.
 */
@Controller
public class MainMenuController
{
    /**
     * Logger instance for the MainMenuController implementation.
     */
    private static final Logger LOG = LogManager.getLogger(MainMenuController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage(ModelMap map)
    {
        return serveMainPage(map);
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST)
    public String getHomePage(ModelMap map)
    {
        return serveMainPage(map);
    }

    private String serveMainPage(ModelMap map)
    {
        TicketMachineFacade facade = WebClientObjectFactory.getServiceFacade();
        map.addAttribute("uuid", WebClientObjectFactory.getTicketMachineUuid());
        return "index";
    }
}
