package uk.ac.solent.lunderground.service;

import org.junit.Test;

import org.mockito.ArgumentCaptor;
import uk.ac.solent.lunderground.model.dao.StationDao;
import uk.ac.solent.lunderground.model.dao.ZoneDao;
import uk.ac.solent.lunderground.model.dto.Station;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LundergroundFacadeTest
{
    /**
     * Station name used for tests, specific value is not important.
     */
    private static final String SOME_NAME = "some name";
    /**
     * Zone number used for tests, specific value is not important.
     */
    private static final int SOME_ZONE = 2;
    /**
     * Station ID used for tests, specific value is not important.
     */
    private static final long SOME_ID = 123;

    /**
     * Check that the configured StationDao is used to access the underlying database.
     */
    @Test
    public void setStationDaoSetsStationDaoToUseWhenRetrievingStationDetails()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();

        facade.setStationDao(mockStationDao);
        facade.getAllStations();

        verify(mockStationDao).retrieveAll();
    }

    /**
     * Check that the passed in name is used for the new station.
     */
    @Test
    public void addStationUsesStationDaoToAddStationWithPassedInName()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);
        ArgumentCaptor<Station> args = ArgumentCaptor.forClass(Station.class);

        facade.addStation(SOME_NAME, SOME_ZONE);

        verify(mockStationDao).addStation(args.capture());
        assertThat(args.getValue()
                       .getName(), equalTo(SOME_NAME));
    }

    /**
     * Check that the passed in zone is used for the new station.
     */
    @Test
    public void addStationUsesStationDaoToAddStationWithPassedInZone()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);
        ArgumentCaptor<Station> args = ArgumentCaptor.forClass(Station.class);

        facade.addStation(SOME_NAME, SOME_ZONE);

        verify(mockStationDao).addStation(args.capture());
        assertThat(args.getValue()
                       .getZone(), equalTo(SOME_ZONE));
    }

    /**
     * Check that all stations are removed from the database.
     */
    @Test
    public void deleteAllUsesStationDaoToRemoveAllStations()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);

        facade.deleteAllStations();

        verify(mockStationDao).deleteAll();
    }

    /**
     * Check that the StationDao is being used to remove a station from the database.
     */
    @Test
    public void deleteStationUsesStationDaoToRemoveStation()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);

        facade.deleteStation(SOME_ID);

        verify(mockStationDao).deleteStation(anyLong());
    }

    /**
     * Check that the station ID is passed to the StationDao.
     */
    @Test
    public void deleteStationPassesStationIdToStationDao()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);

        facade.deleteStation(SOME_ID);

        verify(mockStationDao).deleteStation(eq(SOME_ID));
    }

    /**
     * Check that the StationDao is used to get station from the database.
     */
    @Test
    public void getStationUsesStationDaoToRetrieveStationByName()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);

        facade.getStation(SOME_NAME);

        verify(mockStationDao).getStation(anyString());
    }

    /**
     * Check that the station name is passed to the StationDao.
     */
    @Test
    public void getStationPassesStationNameToStationDao()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);

        facade.getStation(SOME_NAME);

        verify(mockStationDao).getStation(eq(SOME_NAME));
    }

    /**
     * Check that the StationDao is used to get station from the database.
     */
    @Test
    public void getStationUsesStationDaoToRetrieveStationById()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);

        facade.getStation(SOME_ID);

        verify(mockStationDao).getStation(anyLong());
    }

    /**
     * Check that the station name is passed to the StationDao.
     */
    @Test
    public void getStationPassesStationIdToStationDao()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);

        facade.getStation(SOME_ID);

        verify(mockStationDao).getStation(eq(SOME_ID));
    }

    /**
     * Check that the configured StationDao is used to access the underlying database.
     */
    @Test
    public void setZoneDaoSetsZoneDaoToUseWhenRetrievingStationDetails()
    {
        ZoneDao mockZoneDao = mock(ZoneDao.class);
        LundergroundFacade facade = new LundergroundFacade();

        facade.setZoneDao(mockZoneDao);
        facade.getAllZones();

        verify(mockZoneDao).retrieveAll();
    }

    /**
     * Check that the passed in ID is used to update the station.
     */
    @Test
    public void updateStationUsesStationIdToUpdateStation()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);
        ArgumentCaptor<Station> args = ArgumentCaptor.forClass(Station.class);

        facade.updateStation(SOME_ID, SOME_NAME, SOME_ZONE);

        verify(mockStationDao).updateStation(args.capture());
        assertThat(args.getValue()
                       .getId(), equalTo(SOME_ID));
    }

    /**
     * Check that the passed in ID is used to update the station.
     */
    @Test
    public void updateStationUsesStationNameToUpdateStation()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);
        ArgumentCaptor<Station> args = ArgumentCaptor.forClass(Station.class);

        facade.updateStation(SOME_ID, SOME_NAME, SOME_ZONE);

        verify(mockStationDao).updateStation(args.capture());
        assertThat(args.getValue()
                       .getName(), equalTo(SOME_NAME));
    }

    /**
     * Check that the passed in ID is used to update the station.
     */
    @Test
    public void updateStationUsesStationZoneToUpdateStation()
    {
        StationDao mockStationDao = mock(StationDao.class);
        LundergroundFacade facade = new LundergroundFacade();
        facade.setStationDao(mockStationDao);
        ArgumentCaptor<Station> args = ArgumentCaptor.forClass(Station.class);

        facade.updateStation(SOME_ID, SOME_NAME, SOME_ZONE);

        verify(mockStationDao).updateStation(args.capture());
        assertThat(args.getValue()
                       .getZone(), equalTo(SOME_ZONE));
    }
}
