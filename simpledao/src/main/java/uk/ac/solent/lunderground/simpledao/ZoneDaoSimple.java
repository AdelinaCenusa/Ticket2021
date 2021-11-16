package uk.ac.solent.lunderground.simpledao;

import uk.ac.solent.lunderground.model.dao.ZoneDao;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class ZoneDaoSimple implements ZoneDao
{
    /**
     * The first zone number in the system.
     */
    private static final int FIRST_ZONE = 1;
    /**
     * The last zone number in the system.
     */
    private static final int LAST_ZONE  = 9;

    @Override
    public List<Integer> retrieveAll()
    {
        // Create an int stream from the first zone to the last zone inclusive.
        // box from int to Integer (as List doesn't support primitive types).
        // package into a list.
        return IntStream.rangeClosed(FIRST_ZONE, LAST_ZONE)
                        .boxed()
                        .collect(Collectors.toList());
    }
}
