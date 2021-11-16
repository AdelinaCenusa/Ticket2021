package uk.ac.solent.lunderground.model.dao;

import java.util.List;

public interface ZoneDao
{
    /**
     * Retrieve an ordered list of the zones within the London underground system.
     *
     * @return Ordered list of all the zones within the London underground system
     */
    List<Integer> retrieveAll();
}
