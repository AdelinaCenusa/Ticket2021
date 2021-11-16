package uk.ac.solent.lunderground.clientrest;

import uk.ac.solent.lunderground.model.service.DeveloperFacade;
import uk.ac.solent.lunderground.model.service.LundergroundServiceFacade;
import uk.ac.solent.lunderground.model.service.ServiceObjectFactory;
import uk.ac.solent.lunderground.model.service.TicketMachineFacade;

public class RestClientObjectFactory implements ServiceObjectFactory
{
    static final String baseUrl = "http://localhost/lunderground/rest/v1/";

    /**
     * Instance of the facade to use to access the London Underground service.
     */
    private TicketMachineFacade restFacade;

    @Override
    public LundergroundServiceFacade getLundergroundFacade()
    {
        return null;
    }

    @Override
    public DeveloperFacade getDeveloperFacade()
    {
        return null;
    }

    @Override
    public TicketMachineFacade getTicketMachineFacade()
    {
        if (restFacade == null)
        {
            synchronized (this)
            {
                if (restFacade == null)
                {
                    this.restFacade = new RestServiceFacade(baseUrl);
                }
            }
        }

        return restFacade;
    }
}
