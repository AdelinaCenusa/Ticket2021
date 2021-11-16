package uk.ac.solent.lunderground.jaxbdao;

import org.junit.Test;
import uk.ac.solent.lunderground.model.dto.Station;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests for the JAXB Station DAO implementation.
 * These are closer to integration tests as we are reading from and writing to real test files and not mocked versions.
 */
public class StationDaoJaxbTest
{
    /*
     *  These tests are not strictly unit tests as we are testing
     *  that station instances are being persisted to the disk
     *  As a result these tests are relatively slow.
     */

    private final static String TMP_DIR = System.getProperty("java.io.tmpdir");
    private final static String FILE_PATH = TMP_DIR + File.separator + "station_test" + File.separator;
    private static final int ZERO_ELEMENTS = 0;
    private static final int EXPECTED_STATION_COUNT = 304;

    /**
     * Path to test XML file unmarshalling. Doesn't need to be super robust for testing purposes.
     *
     * Contains 2 stations:
     *     Abbey Road, Zone 2
     *     Acton Town, Zone 3
     */
    private final String testXmlUrl = "stationTest.xml";

    /**
     * Expected name of the first station in the StationList.
     */
    private static final String STATION_1_NAME = "Abbey Road";

    /**
     * Expected name of the first station in the StationList.
     */
    private static final String STATION_2_NAME = "Acton Town";

    /**
     * Expected zone of the first station in the StationList.
     */
    private static final int STATION_1_ZONE = 2;

    /**
     * Expected zone of the first station in the StationList.
     */
    private static final int STATION_2_ZONE = 3;

    /**
     * First element in a collection.
     */
    private static final int FIRST_ELEMENT = 0;

    /**
     * Number of elements to expect in a collection.
     */
    private static final int TWO_ELEMENTS = 2;

    @Test
    public void getStationReturnsStationWithPassedInName()
    {
        StationDaoJaxb dao = new StationDaoJaxb(testXmlUrl);
        dao.load();

        Station station = dao.getStation(STATION_1_NAME);

        assertThat(station.getName(), equalTo(STATION_1_NAME));
    }

    @Test
    public void deleteAllRemovesAllStations()
    {
        StationDaoJaxb dao = new StationDaoJaxb(testXmlUrl);
        dao.load();
        System.out.println(dao.retrieveAll());

        dao.deleteAll();
        System.out.println(dao.retrieveAll());

        assertThat(dao.retrieveAll().size(), equalTo(ZERO_ELEMENTS));
    }

    @Test
    public void loadPopulatesMasterListOfStations()
    {
        StationDaoJaxb dao = new StationDaoJaxb("londonStations.xml");

        dao.load();

        List<?> list = dao.retrieveAll();
        assertThat(list.size(), equalTo(EXPECTED_STATION_COUNT));
    }

    /**
     * Check that the stationTest.xml can be unmarshalled into a List of all Stations.
     */
    @Test
    public void loadUnmarshalsStationsFromXmlFile()
    {
        StationDaoJaxb dao = new StationDaoJaxb(testXmlUrl);

        dao.load();
        List<?> list = dao.retrieveAll();

        assertThat(list.size(), equalTo(TWO_ELEMENTS));
    }

    /**
     * Check that the station name is unmarshalled correctly.
     */
    @Test
    public void loadUnmarshalsStationNameFromXmlFile()
    {
        StationDaoJaxb dao = new StationDaoJaxb(testXmlUrl);

        dao.load();
        List<Station> list = dao.retrieveAll();

        Station firstStation = list.get(FIRST_ELEMENT);
        assertThat(firstStation.getName(), equalTo(STATION_1_NAME));
    }

    /**
     * Check that the station zone is unmarshalled correctly.
     */
    @Test
    public void loadUnmarshalsStationZoneFromXmlFile()
    {
        StationDaoJaxb dao = new StationDaoJaxb(testXmlUrl);

        dao.load();
        List<Station> list = dao.retrieveAll();

        Station firstStation = list.get(FIRST_ELEMENT);
        assertThat(firstStation.getZone(), equalTo(STATION_1_ZONE));
    }

    @Test
    public void saveCreatesFileInSpecifiedPath()
    {
        String testPath = FILE_PATH + "testFile.xml";
        StationDaoJaxb dao = new StationDaoJaxb(testXmlUrl);
        dao.load();

        dao.save(testPath);

        File file = new File(testPath);
        assertThat(file.exists(), equalTo(true));
    }

    @Test
    public void savePopulatesFileWithStationList()
    {
        String testPath = FILE_PATH + "testFile.xml";
        StationDaoJaxb dao = new StationDaoJaxb(testXmlUrl);
        dao.load();
        List<Station> before = dao.retrieveAll();

        dao.save(testPath);
        dao.deleteAll();

        dao.load(testPath);
        List<Station> after = dao.retrieveAll();
        assertThat(after, equalTo(before));
    }

    /**
     * Check that we can round trip a StationList using the DAO.
     */
    @Test
    public void SaveMarshalsStationListToXMLFile()
    {
        String testPath = FILE_PATH + "testFile.xml";
        String testXml2Url = "stationTest2.xml";
        StationDaoJaxb dao = new StationDaoJaxb(testXml2Url);

        Station station1 = new Station();
        station1.setName(STATION_1_NAME);
        station1.setZone(STATION_1_ZONE);
        dao.addStation(station1);
        Station station2 = new Station();
        station2.setName(STATION_2_NAME);
        station2.setZone(STATION_2_ZONE);
        dao.addStation(station2);
        dao.save(testPath);
        List<Station> before = dao.retrieveAll();
        System.out.println(before);
        dao.deleteAll();

        dao.load(testPath);
        List<Station> after = dao.retrieveAll();
        System.out.println(after);

        assertThat(after, equalTo(before));
    }
}
