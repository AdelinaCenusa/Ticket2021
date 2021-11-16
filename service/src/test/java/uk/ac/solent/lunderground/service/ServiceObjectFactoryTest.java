package uk.ac.solent.lunderground.service;

import org.junit.Test;
import uk.ac.solent.lunderground.model.service.LundergroundServiceFacade;
import uk.ac.solent.lunderground.model.service.ServiceObjectFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class ServiceObjectFactoryTest
{
    /**
     * Test that the correct facade instance is created.
     */
    @Test
    public void getLundergroundFacadeReturnsInstanceOfLundergroundFacade()
    {
        ServiceObjectFactory factory = new ServiceObjectFactoryImpl();

        LundergroundServiceFacade facade = factory.getLundergroundFacade();

        assertThat(facade, instanceOf(LundergroundFacade.class));
    }

    // Additional tests to consider, these are a bit trickier to implement, as ideally the DAO factory would be
    // mocked. To implement this robustly, it might be worth taking time to implement a dependency injection
    // framework that would allow test cases to supply the required mock instances.
    // * Is the required StationDao passed into the facade as expected
    // * Is the required ZoneDao passed into the facade as expected
}
