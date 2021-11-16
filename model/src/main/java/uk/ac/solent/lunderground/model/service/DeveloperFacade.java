package uk.ac.solent.lunderground.model.service;

import uk.ac.solent.lunderground.model.dao.StationDao;

public interface DeveloperFacade
{
    void setDevStationDao(StationDao devStationDao);
    void reinitialise();
}
