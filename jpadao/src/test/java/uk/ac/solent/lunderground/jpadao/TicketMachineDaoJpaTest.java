package uk.ac.solent.lunderground.jpadao;

import org.junit.Before;
import org.junit.Test;
import uk.ac.solent.lunderground.model.dao.DaoFactory;
import uk.ac.solent.lunderground.model.dao.TicketMachineDao;
import uk.ac.solent.lunderground.model.dto.TicketMachine;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

public class TicketMachineDaoJpaTest
{
    /*
     *  These tests are not strictly unit tests as we are testing
     *  that ticket machine instances are being persisted to the disk rather
     *  than mocking the entity manager responsible and checking that
     *  it is being used correctly. As a result these tests are relatively
     *  slow
     */

    private static final String SOME_UUID = "some uuid";
    private TicketMachineDao machineDao;

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
        machineDao = factory.getTicketMachineDao();
        machineDao.deleteAll();
    }

    /**
     * Check that the addStation method saves the passed in station to the database.
     */
    @Test
    public void addTicketMachineAddsPassedInTicketMachineToPersistenceDatabase()
    {
        TicketMachine testMachine = new TicketMachine();
        testMachine.setUuid(SOME_UUID);
        machineDao.addTicketMachine(testMachine);

        List<TicketMachine> machineList = machineDao.retrieveAll();
        assertThat(machineList, hasItem(testMachine));
    }

}
