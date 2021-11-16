package uk.ac.solent.lunderground.controllerweb.controllers;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import uk.ac.solent.lunderground.controllerweb.WebObjectFactory;
import uk.ac.solent.lunderground.model.dto.Station;
import uk.ac.solent.lunderground.model.service.LundergroundServiceFacade;
import uk.ac.solent.lunderground.service.LundergroundFacade;

import java.util.ArrayList;
import java.util.List;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ContextConfiguration(
        locations =
                {
                        "classpath:spring/controller-application-context.xml",
                        "classpath:spring/Lunderground-servlet.xml"
                }
)
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ManageStationsControllerTest
{
    /**
     * Zone number used for testing, specific value is not important.
     */
    private static final Integer SOME_ZONE = 4;
    /**
     * Zone number parameter used for testing, must be the same as SOME_ZONE but in string form.
     */
    private static final String SOME_ZONE_PARAM = SOME_ZONE.toString();
    /**
     * Station name used for testing, specific value is not important.
     */
    private static final String SOME_STATION = "some station";
    /**
     * ID used for testing, specific value is not important.
     */
    private static final Long SOME_ID = 123L;
    /**
     * ID used for testing, specific value is not important.
     */
    private static final String SOME_ID_PARAM = SOME_ID.toString();


    /**
     * Web Application Context used to allow the test cases to run.
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * MockMvc instance used to request endpoints and verify that the correct outcomes occur.
     */
    private MockMvc mockMvc;

    /**
     * Code to be executed before each test case to ensure that each one starts froms the same configuration.
     */
    @Before
    public void setup()
    {
        mockMvc = webAppContextSetup(this.context).build();
    }

    /**
     * Check that getManageStationsPage returns the name of the manage stations page.
     */
    @Test
    public void getManageStationsPageReturnsManageStations() throws Exception
    {
        mockMvc.perform(get("/manage-stations"))
               .andExpect(forwardedUrl("/WEB-INF/jsp/manage-stations.jsp"));
    }

    /**
     * Check that a default editStationName attribute is added to the ModelMap.
     */
    @Test
    public void getManageStationPageAddsDefaultEditStationNameToModelMap() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);
            when(mockFacade.getStation(anyString())).thenReturn(mock(Station.class));

            mockMvc.perform(get("/manage-stations"))
                   .andExpect(model().attribute("editStationName", ""));
        }
    }

    /**
     * Check that a default editStationZone attribute is added to the ModelMap.
     */
    @Test
    public void getManageStationPageAddsDefaultEditStationZoneToModelMap() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);
            when(mockFacade.getStation(anyString())).thenReturn(mock(Station.class));

            mockMvc.perform(get("/manage-stations"))
                   .andExpect(model().attribute("editStationZone", 1));
        }
    }

    /**
     * Check that a list of all available stations is added as an attribute to the ModelMap.
     */
    @Test
    public void getManageStationPageAddsListOfValidZonesFromFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);
            List<Integer> zoneList = new ArrayList<>();
            when(mockFacade.getAllZones()).thenReturn(zoneList);

            mockMvc.perform(get("/manage-stations"))
                   .andExpect(model().attribute("zones", zoneList));
        }
    }

    /**
     * Check that newly added station is added to the ModelMap from the lunderground facade.
     */
    @Test
    public void getManageStationsPageAddsNewStationAttributeFromFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);
            Station station = new Station();
            station.setName(SOME_STATION);
            when(mockFacade.getStation(anyString())).thenReturn(station);

            mockMvc.perform(get("/manage-stations").param("newStation", SOME_STATION))
                   .andExpect(model().attribute("newStation", station));
        }
    }

    /**
     * Check that newly deleted station name is added to the ModelMap.
     */
    @Test
    public void getManageStationsPageAddsDelStationAttribute() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(get("/manage-stations").param("delStation", SOME_STATION))
                   .andExpect(model().attribute("delStation", SOME_STATION));
        }
    }

    /**
     * Check that newly added station is added to the ModelMap from the lunderground facade.
     */
    @Test
    public void getManageStationsPageUsesPassedInNewStationNameToGetStationDetailsFromFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);
            Station station = new Station();
            station.setName(SOME_STATION);
            when(mockFacade.getStation(anyString())).thenReturn(station);

            mockMvc.perform(get("/manage-stations").param("newStation", SOME_STATION));

            verify(mockFacade).getStation(SOME_STATION);
        }
    }

    /**
     * Check that getManageStationsPage adds a station list to the ModelMap from the Lunderground facade.
     */
    @Test
    public void getManageStationsPageAddStationListToModelMap() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);
            List<Station> expectedList = new ArrayList<>();
            when(mockFacade.getAllStations()).thenReturn(expectedList);

            mockMvc.perform(get("/manage-stations"))
                   .andExpect(model().attribute("stations", expectedList));
        }
    }

    /**
     * Check that getManageStationsPage adds passed in station ID to the ModelMap.
     */
    @Test
    public void getManageStationsPageAddsEditStationIdToModelMap() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);
            List<Station> expectedList = new ArrayList<>();
            when(mockFacade.getAllStations()).thenReturn(expectedList);

            mockMvc.perform(get("/manage-stations")
                    .param("editStationId", SOME_ID_PARAM)
                    .param("editStationName", SOME_STATION)
                    .param("editStationZone", SOME_ZONE_PARAM))
                   .andExpect(model().attribute("editStationId", SOME_ID));
        }
    }

    /**
     * Check that getManageStationsPage adds passed in station name to the ModelMap.
     */
    @Test
    public void getManageStationsPageAddsEditStationNameToModelMap() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);
            List<Station> expectedList = new ArrayList<>();
            when(mockFacade.getAllStations()).thenReturn(expectedList);

            mockMvc.perform(get("/manage-stations")
                    .param("editStationId", SOME_ID_PARAM)
                    .param("editStationName", SOME_STATION)
                    .param("editStationZone", SOME_ZONE_PARAM))
                   .andExpect(model().attribute("editStationName", SOME_STATION));
        }
    }

    /**
     * Check that getManageStationsPage adds passed in station zone to the ModelMap.
     */
    @Test
    public void getManageStationsPageAddsEditStationZoneToModelMap() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);
            List<Station> expectedList = new ArrayList<>();
            when(mockFacade.getAllStations()).thenReturn(expectedList);

            mockMvc.perform(get("/manage-stations")
                    .param("editStationId", SOME_ID_PARAM)
                    .param("editStationName", SOME_STATION)
                    .param("editStationZone", SOME_ZONE_PARAM))
                   .andExpect(model().attribute("editStationZone", SOME_ZONE));
        }
    }

    /**
     * Check that getManageStationsAddPage adds a station using the facade.
     */
    @Test
    public void getManageStationsAddPageAddsStationUsingFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/add")
                    .param("stationName", SOME_STATION)
                    .param("zoneNumber", SOME_ZONE_PARAM));

            verify(mockFacade).addStation(anyString(), anyInt());
        }
    }

    /**
     * Check that getManageStationsAddPage passes the station name param to the facade.
     */
    @Test
    public void getManageStationsAddPagePassesStationNameToFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/add")
                    .param("stationName", SOME_STATION)
                    .param("zoneNumber", SOME_ZONE_PARAM));

            verify(mockFacade).addStation(eq(SOME_STATION), anyInt());
        }
    }

    /**
     * Check that getManageStationsAddPage passes the zone number param to the facade.
     */
    @Test
    public void getManageStationsAddPagePassesZoneNumberToFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/add")
                    .param("stationName", SOME_STATION)
                    .param("zoneNumber", SOME_ZONE_PARAM));

            verify(mockFacade).addStation(anyString(), eq(SOME_ZONE));
        }
    }

    /**
     * Check that we're asking for a redirect back to the page responsible for managing stations.
     */
    @Test
    public void getManageStationsAddPageRedirectsToManageStationsPage() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/add")
                    .param("stationName", SOME_STATION)
                    .param("zoneNumber", SOME_ZONE_PARAM))
                   .andExpect(redirectedUrl("/manage-stations?newStation=some+station"));
        }
    }

    /**
     * Check that getManageStationsDeletePage deletes a station using the facade.
     */
    @Test
    public void getManageStationsDeletePageDeletesStationUsingFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            station.setName(SOME_STATION);
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/delete")
                    .param("id", SOME_ID_PARAM));

            verify(mockFacade).deleteStation(anyLong());
        }
    }

    /**
     * Check that getManageStationsDeletePage passes the station ID param to the facade.
     */
    @Test
    public void getManageStationsDeletePagePassesStationIdToTheFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            station.setName(SOME_STATION);
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/delete")
                    .param("id", SOME_ID_PARAM));

            verify(mockFacade).deleteStation(eq(SOME_ID));
        }
    }

    /**
     * Check that we're asking for a redirect to the page responsible for managing stations.
     */
    @Test
    public void getManageStationsDeletePageRedirectsToManageStationsPage() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/delete")
                    .param("id", SOME_ID_PARAM))
                   .andExpect(redirectedUrl("/manage-stations"));
        }
    }

    /**
     * Check that we're passing the name of the deleted station in the redirected URL.
     */
    @Test
    public void getManageStationsDeletePagePassesStationNameInRedirect() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            station.setName(SOME_STATION);
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/delete")
                    .param("id", SOME_ID_PARAM))
                   .andExpect(redirectedUrl("/manage-stations?delStation=some+station"));
        }
    }

    /**
     * Check that getManageStationsEditPage retrieves a station using the facade.
     */
    @Test
    public void getManageStationsEditPageGetStationDetailsUsingFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            station.setName(SOME_STATION);
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/edit")
                    .param("id", SOME_ID_PARAM));

            verify(mockFacade).getStation(anyLong());
        }
    }

    /**
     * Check that getManageStationsEditPage retrieves a station using the passed in stationId.
     */
    @Test
    public void getManageStationsEditPagePassesStationIdToTheFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            station.setName(SOME_STATION);
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/edit")
                    .param("id", SOME_ID_PARAM));

            verify(mockFacade).getStation(eq(SOME_ID));
        }
    }

    /**
     * Check that we're asking for a redirect to the page responsible for managing stations.
     */
    @Test
    public void getManageStationsEditPageRedirectsToManageStationsPage() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/edit")
                    .param("id", SOME_ID_PARAM))
                   .andExpect(redirectedUrl("/manage-stations?editStationZone=0"));
        }
    }

    /**
     * Check that we're passing the ID of the station to be edited in the redirected URL.
     */
    @Test
    public void getManageStationsEditPagePassesStationIdInRedirect() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            station.setId(SOME_ID);
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/edit")
                    .param("id", SOME_ID_PARAM))
                   .andExpect(redirectedUrl("/manage-stations?editStationId=123&editStationZone=0"));
        }
    }

    /**
     * Check that we're passing the names of the station to be edited in the redirected URL.
     */
    @Test
    public void getManageStationsEditPagePassesStationNameInRedirect() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            station.setName(SOME_STATION);
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/edit")
                    .param("id", SOME_ID_PARAM))
                   .andExpect(redirectedUrl("/manage-stations?editStationName=some+station&editStationZone=0"));
        }
    }

    /**
     * Check that we're passing the names of the station to be edited in the redirected URL.
     */
    @Test
    public void getManageStationsEditPagePassesStationZoneInRedirect() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            station.setZone(SOME_ZONE);
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/edit")
                    .param("id", SOME_ID_PARAM))
                   .andExpect(redirectedUrl("/manage-stations?editStationZone=4"));
        }
    }

    /**
     * Check that we're passing all params of the station to be edited in the redirected URL.
     */
    @Test
    public void getManageStationsEditPagePassesStationIdNameAndZoneInRedirect() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            station.setId(SOME_ID);
            station.setName(SOME_STATION);
            station.setZone(SOME_ZONE);
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/edit")
                    .param("id", SOME_ID_PARAM))
                   .andExpect(redirectedUrl(
                           "/manage-stations?editStationId=123&editStationName=some+station&editStationZone=4"));
        }
    }

    /**
     * Check that getManageStationsUpdatePage uses facade to update station.
     */
    @Test
    public void getManageStationsUpdatePageUsesFacadeWhenUpdatingStation() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/update")
                    .param("id", SOME_ID_PARAM)
                    .param("stationName", SOME_STATION)
                    .param("zoneNumber", SOME_ZONE_PARAM));

            verify(mockFacade).updateStation(anyLong(), anyString(), anyInt());
        }
    }

    /**
     * Check that getManageStationsUpdatePage passed station ID to facade.
     */
    @Test
    public void getManageStationsUpdatePagePassesStationIdToTheFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/update")
                    .param("id", SOME_ID_PARAM)
                    .param("stationName", SOME_STATION)
                    .param("zoneNumber", SOME_ZONE_PARAM));

            verify(mockFacade).updateStation(eq(SOME_ID), anyString(), anyInt());
        }
    }

    /**
     * Check that getManageStationsUpdatePage passed the new station name to facade.
     */
    @Test
    public void getManageStationsUpdatePagePassesNewStationNameToTheFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/update")
                    .param("id", SOME_ID_PARAM)
                    .param("stationName", SOME_STATION)
                    .param("zoneNumber", SOME_ZONE_PARAM));

            verify(mockFacade).updateStation(anyLong(), eq(SOME_STATION), anyInt());
        }
    }

    /**
     * Check that getManageStationsUpdatePage passed the new station zone to facade.
     */
    @Test
    public void getManageStationsUpdatePagePassesNewStationZoneToTheFacade() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            Station station = new Station();
            when(mockFacade.getStation(anyLong())).thenReturn(station);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/update")
                    .param("id", SOME_ID_PARAM)
                    .param("stationName", SOME_STATION)
                    .param("zoneNumber", SOME_ZONE_PARAM));

            verify(mockFacade).updateStation(anyLong(), anyString(), eq(SOME_ZONE));
        }
    }

    /**
     * Check that we're asking for a redirect back to the page responsible for managing stations.
     */
    @Test
    public void getManageStationsUpdatePageRedirectsToManageStationsPage() throws Exception
    {
        try (MockedStatic<WebObjectFactory> factory = Mockito.mockStatic(WebObjectFactory.class))
        {
            LundergroundServiceFacade mockFacade = mock(LundergroundFacade.class);
            factory.when(WebObjectFactory::getServiceFacade)
                   .thenReturn(mockFacade);

            mockMvc.perform(post("/manage-stations/update")
                    .param("id", SOME_ID_PARAM)
                    .param("stationName", SOME_STATION)
                    .param("zoneNumber", SOME_ZONE_PARAM))
                   .andExpect(redirectedUrl("/manage-stations?updatedStation=123"));
        }
    }

    // Additional tests that could / should be added
    // * Check adding the same station twice
    // * Check trying to remove a station that doesn't exist
}
