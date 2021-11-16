package uk.ac.solent.lunderground.jpadao;

import org.junit.Before;
import org.junit.Test;
import uk.ac.solent.lunderground.model.dao.DaoFactory;
import uk.ac.solent.lunderground.model.dao.StationDao;
import uk.ac.solent.lunderground.model.dto.Station;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class StationDaoJpaTest
{
    /*
     *  These tests are not strictly unit tests as we are testing
     *  that station instances are being persisted to the disk rather
     *  than mocking the entity manager responsible and checking that
     *  it is being used correctly. As a result these tests are relatively
     *  slow
     */

    /**
     * Station name used for test purposes, specific value is unimportant.
     */
    private static final String SOME_STATION = "some station";
    /**
     * Station name used for test purposes, name helps identify specific instance.
     */
    private static final String STATION_1 = "Station 1";
    /**
     * Station name used for test purposes, name helps identify specific instance.
     */
    private static final String STATION_2 = "Station 2";

    /**
     * Zone number used for testing purposes, specific value is unimportant.
     */
    private static final int SOME_ZONE = 1;

    /**
     * Zone number used for testing purposes, value must be different to SOME_ZONE.
     */
    private static final int SOME_OTHER_ZONE = 3;

    /**
     * Id number used for testing purposes, specific value is unimportant.
     */
    private static final long SOME_ID = 123;

    /**
     * Zero items in a list.
     */
    private static final int ZERO_ITEMS = 0;
    /**
     * 2 items in a list.
     */
    private static final int TWO_ITEMS = 2;

    /**
     * Index of the first item in a list.
     */
    private static final int FIRST_ITEM = 0;

    /**
     * Used to hold a reference to the StationDao instance being tested.
     */
    private StationDao stationDao;

    /**
     * Setup used to initialise the test environment before each test case.
     * - Initialises the StationDao instance to be tested
     * - Removes all of the stations from the persistence database
     */
    @Before
    public void setup()
    {
        // Remove everything from the database before each test to ensure that
        // each unit test is as atomic as possible
        DaoFactory factory = new DaoFactoryJpa();
        stationDao = factory.getStationDao();
        stationDao.deleteAll();
    }

    /**
     * Check that the addStation method saves the passed in station to the database.
     */
    @Test
    public void addStationAddsPassedInStationToPersistenceDatabase()
    {
        Station testStation = new Station();
        testStation.setName(SOME_STATION);
        testStation.setZone(SOME_ZONE);
        stationDao.addStation(testStation);

        List<Station> stationList = stationDao.retrieveAll();
        assertThat(stationList, hasItem(testStation));
    }

    /**
     * Check that the persisted Stations can be retrieved from the database.
     */
    @Test
    public void retrieveAllReturnsListOfAddedStations()
    {
        DaoFactory factory = new DaoFactoryJpa();
        stationDao = factory.getStationDao();

        // We need to create 2 station instances because if we add the same instance
        // twice the persistence layer treats it as the same item and we only get
        // one item in the database
        Station testStation = new Station();
        stationDao.addStation(testStation);
        testStation = new Station();
        stationDao.addStation(testStation);

        List<Station> stationList = stationDao.retrieveAll();
        assertThat(stationList.size(), equalTo(TWO_ITEMS));
    }

    /**
     * Check that all stations can be removed from the persistence database.
     */
    @Test
    public void deleteAllRemovesAllStationsFromTheDatabase()
    {
        DaoFactory factory = new DaoFactoryJpa();
        stationDao = factory.getStationDao();
        Station testStation = new Station();
        stationDao.addStation(testStation);
        testStation = new Station();
        stationDao.addStation(testStation);

        stationDao.deleteAll();

        List<Station> stationList = stationDao.retrieveAll();
        assertThat(stationList.size(), equalTo(ZERO_ITEMS));
    }

    /**
     * Check that only the station with the specified ID is removed from the database.
     */
    @Test
    public void deleteRemovesStationWithPassedInIdFromTheDatabase()
    {
        DaoFactory factory = new DaoFactoryJpa();
        stationDao = factory.getStationDao();
        Station testStation = new Station();
        testStation.setName(STATION_1);
        stationDao.addStation(testStation);
        testStation = new Station();
        testStation.setName(STATION_2);
        stationDao.addStation(testStation);
        testStation = stationDao.retrieveAll()
                                .get(FIRST_ITEM);

        stationDao.deleteStation(testStation.getId());

        List<Station> stationList = stationDao.retrieveAll();
        assertThat(stationList, not(hasItem(testStation)));
    }

    /**
     * Check that a station is correctly retrieved by name when one exists.
     */
    @Test
    public void getStationRetrievesStationWithPassedInNameFromDatabase()
    {
        DaoFactory factory = new DaoFactoryJpa();
        stationDao = factory.getStationDao();
        Station testStation = new Station();
        testStation.setName(STATION_1);
        stationDao.addStation(testStation);

        Station station = stationDao.getStation(STATION_1);

        assertThat(station.getName(), equalTo(STATION_1));
    }

    /**
     * Check that null is returned if the requested station doesn't exist.
     */
    @Test
    public void getStationReturnsNullIfNoMatchFoundForName()
    {
        DaoFactory factory = new DaoFactoryJpa();
        stationDao = factory.getStationDao();

        Station station = stationDao.getStation(STATION_2);

        assertThat(station, nullValue());
    }

    /**
     * Check that a station is correctly retrieved by id when one exists.
     */
    @Test
    public void getStationRetrievesStationWithPassedInIdFromDatabase()
    {
        DaoFactory factory = new DaoFactoryJpa();
        stationDao = factory.getStationDao();
        Station testStation = new Station();
        testStation.setName(STATION_1);
        stationDao.addStation(testStation);
        Long id = stationDao.getStation(STATION_1)
                            .getId();

        Station station = stationDao.getStation(id);

        assertThat(station.getName(), equalTo(STATION_1));
    }

    /**
     * Check that null is returned if the requested station doesn't exist.
     */
    @Test
    public void getStationReturnsNullIfNoMatchFoundForId()
    {
        DaoFactory factory = new DaoFactoryJpa();
        stationDao = factory.getStationDao();

        Station station = stationDao.getStation(SOME_ID);

        assertThat(station, nullValue());
    }

    /**
     * Check that we can update the name of a station that has been persisted.
     */
    @Test
    public void updateStationSetsStationNameToPassedInStationName()
    {
        DaoFactory factory = new DaoFactoryJpa();
        stationDao = factory.getStationDao();
        Station testStation = new Station();
        testStation.setName(STATION_1);
        stationDao.addStation(testStation);

        Long id = stationDao.getStation(STATION_1).getId();
        // We create a new station instance and set the appropriate properties
        // rather than retrieve the station with the required ID and modify that
        // as modifying the retrieved station also modifies thd instance which
        // was originally persisted.
        Station newDetails = new Station();
        newDetails.setId(id);
        newDetails.setName(STATION_2);

        stationDao.updateStation(newDetails);
        Station updatedStation = stationDao.getStation(id);
        assertThat(updatedStation.getName(), equalTo(STATION_2));
    }

    /**
     * Check that we can update the name of a station that has been persisted.
     */
    @Test
    public void updateStationSetsStationZoneToPassedInStationZone()
    {
        DaoFactory factory = new DaoFactoryJpa();
        stationDao = factory.getStationDao();
        Station testStation = new Station();
        testStation.setName(SOME_STATION);
        testStation.setZone(SOME_ZONE);
        stationDao.addStation(testStation);

        Long id = stationDao.getStation(SOME_STATION).getId();
        // We create a new station instance and set the appropriate properties
        // rather than retrieve the station with the required ID and modify that
        // as modifying the retrieved station also modifies thd instance which
        // was originally persisted.
        Station newDetails = new Station();
        newDetails.setId(id);
        newDetails.setZone(SOME_OTHER_ZONE);

        stationDao.updateStation(newDetails);
        Station updatedStation = stationDao.getStation(id);
        assertThat(updatedStation.getZone(), equalTo(SOME_OTHER_ZONE));
    }
}
