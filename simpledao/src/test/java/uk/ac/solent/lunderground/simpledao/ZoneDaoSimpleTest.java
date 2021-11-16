package uk.ac.solent.lunderground.simpledao;

import org.junit.Test;
import uk.ac.solent.lunderground.model.dao.ZoneDao;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ZoneDaoSimpleTest
{
    /**
     * List of the expected zones, in numerical order.
     */
    private static final List<Integer> EXPECTED_ZONES = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

    /**
     * Check that retrieveAll returns a list of the expected zones.
     */
    @Test
    public void retrieveAllReturnsListOfIntegersFromOneToNine()
    {
        ZoneDao zoneDao = new ZoneDaoSimple();

        assertThat(zoneDao.retrieveAll(), equalTo(EXPECTED_ZONES));
    }
}
