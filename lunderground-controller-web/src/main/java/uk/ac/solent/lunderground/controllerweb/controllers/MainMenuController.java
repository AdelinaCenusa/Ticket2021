package uk.ac.solent.lunderground.controllerweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.ac.solent.lunderground.controllerweb.WebObjectFactory;
import uk.ac.solent.lunderground.model.service.DeveloperFacade;

/**
 * This is the controller for the main landing page http://localhost/lunderground/controller.
 * The controller takes care of serving up the pages related to the various menu items
 */
@Controller("/")
public final class MainMenuController
{
    /**
     *
     */
    private DeveloperFacade devFacade = null;

    /**
     * Serve the main landing page for the root URL.
     *
     * @return Return the .jsp to use for the root of the web app
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage()
    {
        return "index";
    }

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public String reinitialiseLundergroundSystem()
    {
        this.devFacade = WebObjectFactory.getDeveloperFacade();
        devFacade.reinitialise();

        return "redirect:/manage-stations";
    }
}
