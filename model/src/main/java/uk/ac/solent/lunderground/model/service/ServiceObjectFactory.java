package uk.ac.solent.lunderground.model.service;

public interface ServiceObjectFactory
{
    /**
     * Get a service facade that can be used to access the London underground services.
     *
     * @return Service facade that implements the LundergroundServiceFacade interface
     */
    LundergroundServiceFacade getLundergroundFacade();

    /**
     * Get a service facade that can be used for development purposes.
     *
     * @return Service facade that implements the DeveloperFacade interface
     */
    DeveloperFacade getDeveloperFacade();

    /**
     * Get a service facade that can be used to access the functionality related to a ticket machine.
     *
     * @return Service facade that implements the TicketFacade interface
     */
    TicketMachineFacade getTicketMachineFacade();
}
