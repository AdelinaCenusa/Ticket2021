package uk.ac.solent.lunderground.jpadao;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import uk.ac.solent.lunderground.model.dao.DaoFactory;
import uk.ac.solent.lunderground.model.dao.StationDao;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.MockedStatic;
import uk.ac.solent.lunderground.model.dao.ZoneDao;
import uk.ac.solent.lunderground.simpledao.ZoneDaoSimple;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DaoFactoryJpaTest
{
    /**
     * Check that a StationDaoJpa instance is created when getStation() is called.
     */
    @Test
    public void getStationDaoReturnsStationDaoJpaInstance()
    {
        DaoFactory factory = new DaoFactoryJpa();

        StationDao stationDao = factory.getStationDao();

        assertThat(stationDao, instanceOf(StationDaoJpa.class));
    }

    /**
     * Check that a StationDaoJpa instance is created when getStation() is called.
     */
    @Test
    public void getZoneDaoReturnsZoneDaoSimpleInstance()
    {
        DaoFactory factory = new DaoFactoryJpa();

        ZoneDao zoneDao = factory.getZoneDao();

        assertThat(zoneDao, instanceOf(ZoneDaoSimple.class));
    }
}
