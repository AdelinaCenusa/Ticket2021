package uk.ac.solent.lunderground.model.dao;

import uk.ac.solent.lunderground.model.dto.Station;

import java.util.List;

public interface StationDao
{
    /**
     * Get a list of all stations that exist.
     *
     * @return List of Station objects
     */
    List<Station> retrieveAll();

    /**
     * Add the passed in Station to the persistence database.
     *
     * @param newStation The Station to be saved to the database
     */
    void addStation(Station newStation);

    /**
     * Add the passed in list of stations to the persistence database
     *
     * @param stationList The list of stations to be saved to the database
     */
    void setStationList(List<Station> stationList);

    /**
     * Remove the station with passed in ID from the database.
     *
     * @param stationId The Long ID of the station to be removed
     */
    void deleteStation(long stationId);

    /**
     * Remove all Station instances from the database.
     */
    void deleteAll();

    /**
     * Get a station with the passed in name from the persistence database.
     *
     * @param stationName name of the station to retrieve from the database
     * @return Station instance with retrieved details. Null if Station doesn't exist
     */
    Station getStation(String stationName);

    /**
     * Get a station with the passed in ID from the persistence database.
     *
     * @param stationId id of the station to retrieve from the database
     * @return Station instance with retrieved details. Null if Station doesn't exist
     */
    Station getStation(Long stationId);

    /**
     * Update the station with the passed in ID to use the passed in name and zone.
     *
     * @param newDetails Station instance containing the new details to persist
     */
    void updateStation(Station newDetails);
}
