package uk.ac.solent.lunderground.controllerweb;

import uk.ac.solent.lunderground.model.service.DeveloperFacade;
import uk.ac.solent.lunderground.service.ServiceObjectFactoryImpl;
import uk.ac.solent.lunderground.model.service.LundergroundServiceFacade;
import uk.ac.solent.lunderground.model.service.ServiceObjectFactory;

public final class WebObjectFactory
{
    /**
     * Singleton facade instance used to access the services of the London Underground ticket system.
     * The instance is lazy loaded at the point it is first required for use
     */
    private static LundergroundServiceFacade lundergroundFacade = null;
    private static DeveloperFacade devFacade = null;

    private WebObjectFactory()
    {
    }

    /**
     * Provide access to the London Underground Ticket System facade.
     *
     * @return London Underground Service Facade instance that can be used to access the available services.
     */
    public static LundergroundServiceFacade getServiceFacade()
    {
        if (lundergroundFacade == null)
        {
            synchronized (WebObjectFactory.class)
            {
                if (lundergroundFacade == null)
                {
                    ServiceObjectFactory clientObjectFactory = new ServiceObjectFactoryImpl();
                    lundergroundFacade = clientObjectFactory.getLundergroundFacade();
                }
            }
        }
        return lundergroundFacade;
    }

    public static DeveloperFacade getDeveloperFacade()
    {
        if (devFacade == null)
        {
            synchronized (WebObjectFactory.class)
            {
                if (devFacade == null)
                {
                    ServiceObjectFactory clientObjectFactory = new ServiceObjectFactoryImpl();
                    devFacade = clientObjectFactory.getDeveloperFacade();
                }
            }
        }
        return devFacade;
    }
}
