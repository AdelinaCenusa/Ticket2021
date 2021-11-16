package uk.ac.solent.lunderground.controllerweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.solent.lunderground.controllerweb.WebObjectFactory;
import uk.ac.solent.lunderground.model.dto.Station;
import uk.ac.solent.lunderground.model.service.LundergroundServiceFacade;

import javax.validation.constraints.NotNull;

@Controller("/manage-stations")
public class ManageStationsController
{
    /**
     * Reference to the London Underground Service Facade used to access the service available.
     */
    private LundergroundServiceFacade lunderGroundFacade = null;

    /**
     * Default name to use if no other name has been supplied.
     */
    private static final String DEFAULT_NAME = "";
    /**
     * Default zone to use if no other zone has been supplied.
     */
    private static final int DEFAULT_ZONE = 1;

    // The following methods are all represented as 'getManageStationsPage' in the UML model as
    // they all have the same essential functionality.
    // The number of arguments to the original method were making if difficult to see the relatively
    // simple operation taking place therefore it was broken down into a number of smaller methods.

    /**
     * Serve the page responsible for managing underground stations.
     *
     * @param map attributes map, used to inject data into the view
     * @return Return the .jsp to use for managing underground stations
     */
    @RequestMapping(value = "/manage-stations", method = RequestMethod.GET)
    public String getManageStationsPageNoParams(final ModelMap map)
    {
        return getManageStationsModelMapView(map);
    }

    /**
     * Serve the page responsible for managing underground stations.
     *
     * @param map        attributes map, used to inject data into the view
     * @param newStation the name of the newly added station
     * @return Return the .jsp to use for managing underground stations
     */
    @RequestMapping(value = "/manage-stations", method = RequestMethod.GET, params = {"newStation"})
    public String getManageStationsPageNewStation(final ModelMap map,
                                                  @RequestParam(name = "newStation") final String newStation)
    {
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        map.addAttribute("newStation", lunderGroundFacade.getStation(newStation));
        return getManageStationsModelMapView(map);
    }

    /**
     * Serve the page responsible for managing underground stations.
     *
     * @param map        attributes map, used to inject data into the view
     * @param delStation the name of the newly deleted station
     * @return Return the .jsp to use for managing underground stations
     */
    @RequestMapping(value = "/manage-stations", method = RequestMethod.GET, params = {"delStation"})
    public String getManageStationsPageDeleteStation(final ModelMap map,
                                                     @RequestParam(name = "delStation") final String delStation)
    {
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        map.addAttribute("delStation", delStation);
        return getManageStationsModelMapView(map);
    }

    /**
     * Serve the page responsible for managing underground stations.
     *
     * @param map        attributes map, used to inject data into the view
     * @param updatedStation the name of the newly deleted station
     * @return Return the .jsp to use for managing underground stations
     */
    @RequestMapping(value = "/manage-stations", method = RequestMethod.GET, params = {"updatedStation"})
    public String getManageStationsPageUpdateStation(final ModelMap map,
                                                     @RequestParam(name = "updatedStation") final String updatedStation)
    {
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        map.addAttribute("updatedStation", updatedStation);
        return getManageStationsModelMapView(map);
    }

    /**
     * Serve the page responsible for managing underground stations.
     *
     * @param map        attributes map, used to inject data into the view
     * @param editId     ID of the station that the user wants to modify
     * @param editName   Current name of the station that the user wants to modify
     * @param editZone   Current zone of the station that the user wants to modify
     * @return Return the .jsp to use for managing underground stations
     */
    @RequestMapping(value = "/manage-stations", method = RequestMethod.GET, params = {"editStationId",
                                                                                         "editStationName",
                                                                                         "editStationZone"})
    public String getManageStationsPageEditStation(final ModelMap map,
                                                   @RequestParam(name = "editStationId") final Long editId,
                                                   @RequestParam(name = "editStationName") final String editName,
                                                   @RequestParam(name = "editStationZone") final int editZone)
    {
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        map.addAttribute("editStationId", editId);
        map.addAttribute("editStationName", editName);
        map.addAttribute("editStationZone", editZone);
        return getManageStationsModelMapView(map);
    }

    /**
     * Handle the common part of serving up the manage stations URL.
     *
     * @param map attributes map, used to inject data into the view
     * @return Return the .jsp to use for managing underground stations
     */
    private String getManageStationsModelMapView(final ModelMap map)
    {
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        map.addAttribute("stations", this.lunderGroundFacade.getAllStations());
        map.addAttribute("zones", this.lunderGroundFacade.getAllZones());
        setDefaultStationDetails(map);
        return "manage-stations";
    }

    private void setDefaultStationDetails(@NotNull final ModelMap map)
    {
        if ((map.getAttribute("editStationId") == null))
        {
            map.addAttribute("editStationName", DEFAULT_NAME);
            map.addAttribute("editStationZone", DEFAULT_ZONE);
        }
    }

    /**
     * Serve the page responsible for adding a station to the database.
     *
     * @param redirectAttributes attributes to be added to the the redirect request
     * @param name               The name of the station to be added
     * @param zone               The zone that the station is in
     * @return Return a redirect back to the page responsible form managing stations
     */
    @RequestMapping(value = "/manage-stations/add", method = RequestMethod.POST)
    public String getManageStationsAddPage(final RedirectAttributes redirectAttributes,
                                           @RequestParam("stationName") final String name,
                                           @RequestParam("zoneNumber") final Integer zone)
    {
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        this.lunderGroundFacade.addStation(name, zone);
        redirectAttributes.addAttribute("newStation", name);
        return "redirect:/manage-stations";
    }

    /**
     * Serve the page responsible for removing a station from the database.
     *
     * @param redirectAttributes attributes to be added to the the redirect request
     * @param stationId          The ID of the station to remove from the database
     * @return Return a redirect back to the page responsible form managing stations
     */
    @RequestMapping(value = "/manage-stations/delete", method = RequestMethod.POST)
    public String getManageStationsDeletePage(final RedirectAttributes redirectAttributes,
                                              @RequestParam("id") final Long stationId)
    {
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        redirectAttributes.addAttribute("delStation", this.lunderGroundFacade.getStation(stationId)
                                                                                .getName());
        this.lunderGroundFacade.deleteStation(stationId);
        return "redirect:/manage-stations";
    }

    /**
     * Serve the page responsible for getting the details of the station to be edited.
     *
     * @param redirectAttributes attributes to be added to the the redirect request
     * @param stationId          The ID of the station to be edited
     * @return Return a redirect back to the page responsible form managing stations
     */
    @RequestMapping(value = "/manage-stations/edit", method = RequestMethod.POST)
    public String getManageStationsEditPage(final RedirectAttributes redirectAttributes,
                                            @RequestParam("id") final Long stationId)
    {
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        Station editStation = this.lunderGroundFacade.getStation(stationId);
        redirectAttributes.addAttribute("editStationId", editStation.getId());
        redirectAttributes.addAttribute("editStationName", editStation.getName());
        redirectAttributes.addAttribute("editStationZone", editStation.getZone());
        return "redirect:/manage-stations";
    }

    /**
     * Serve the page responsible for updating the station with the supplied details.
     *
     * @param redirectAttributes attributes to be added to the the redirect request
     * @param editId ID of the station to be updates
     * @param name new name to use for the station
     * @param zone new zone to use for the station
     * @return Return a redirect back to the page responsible form managing stations
     */
    @RequestMapping(value = "/manage-stations/update", method = RequestMethod.POST)
    public String getManageStationsUpdatePage(final RedirectAttributes redirectAttributes,
                                              @RequestParam(name = "id") final Long editId,
                                              @RequestParam(name = "stationName") final String name,
                                              @RequestParam(name = "zoneNumber") final Integer zone)
    {
        this.lunderGroundFacade = WebObjectFactory.getServiceFacade();
        this.lunderGroundFacade.updateStation(editId, name, zone);

        redirectAttributes.addAttribute("updatedStation", editId);
        return "redirect:/manage-stations";
    }
}
