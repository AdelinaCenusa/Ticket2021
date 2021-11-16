package uk.ac.solent.lunderground.controllerweb;

import org.junit.Test;
import uk.ac.solent.lunderground.model.service.LundergroundServiceFacade;
import uk.ac.solent.lunderground.service.LundergroundFacade;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class WebObjectFactoryTest
{
    /**
     * Check that the correct type of facade is created.
     */
    @Test
    public void getServiceFacadeReturnsInstanceOfLundergroundFacade()
    {
        LundergroundServiceFacade facade = WebObjectFactory.getServiceFacade();

        assertThat(facade, instanceOf(LundergroundFacade.class));
    }

    /**
     * Check that the same instance of the facade is returned each time one is requested.
     */
    @Test
    public void getServiceFacadeReturnsSameInstanceWhenCalledMultipleTimes()
    {
        LundergroundServiceFacade facade1 = WebObjectFactory.getServiceFacade();

        LundergroundServiceFacade facade2 = WebObjectFactory.getServiceFacade();

        Boolean sameInstance = (facade1 == facade2);
        assertThat(sameInstance, equalTo(true));
    }
}
