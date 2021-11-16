package uk.ac.solent.lunderground.jaxbdao;

import org.springframework.core.io.ClassPathResource;
import uk.ac.solent.lunderground.model.dao.StationDao;
import uk.ac.solent.lunderground.model.dto.Station;
import uk.ac.solent.lunderground.model.dto.StationList;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class StationDaoJaxb implements StationDao
{
    /**
     * Logger instance for the StationDaoJaxb implementation.
     */
    private static final Logger LOG = LogManager.getLogger(StationDaoJaxb.class);

    private boolean fromClassPath = false;
    private final String path;
    private final InputStream resourceFile;

    /**
     * Tree map of the various stations.
     * The station name is the key and the station instance is the value
     */
    private final TreeMap<String, Station> stationMap = new TreeMap<>();

    /**
     * Constructor for the JAXB StationDao implementation.
     *
     * @param resource the XML file to use. This can be either the name of a file in the classPath or
     *                 the path to an XML file. The full path takes precedence over the filename.
     */
    public StationDaoJaxb(@NotNull final String resource)
    {
        InputStream tmpFile = getStreamFromPath(resource);
        if (tmpFile == null)
        {
            tmpFile = getStreamFromClassPath(resource);
        }

        path = resource;
        resourceFile = tmpFile;
        LOG.debug("Station list sourced from: " + resourceFile);
    }

    private InputStream getStreamFromPath(final String resource)
    {
        File file = new File(resource);

        InputStream tmpStream = null;
        try
        {
            tmpStream = new FileInputStream(file);
        }
        catch (FileNotFoundException e)
        {
            LOG.debug("No station list at: " + resource);
        }
        return tmpStream;
    }

    private InputStream getStreamFromClassPath(final String resource)
    {
        InputStream tmpStream = null;
        try
        {
            tmpStream = new ClassPathResource(resource).getInputStream();
            fromClassPath = true;
        }
        catch (IOException e)
        {
            LOG.error("unable to find " + resource + " in the class path");
            fromClassPath = false;
        }
        return tmpStream;
    }

    @Override
    public synchronized List<Station> retrieveAll()
    {
        return new ArrayList<>(stationMap.values());
    }

    @Override
    public synchronized void addStation(final Station newStation)
    {
        stationMap.put(newStation.getName(), newStation);
        save();
    }

    @Override
    public synchronized void setStationList(final List<Station> stationList)
    {
        storeStationList(stationList);
        save();
    }

    @Override
    public synchronized void deleteAll()
    {
        LOG.debug("Removed all stations");
        stationMap.clear();
        save();
    }

    /**
     * Load a list of stations from XML.
     */
    public void load()
    {
        StationList stationList = getStationsFromStream(resourceFile);
        storeStationList(stationList.getStationList());
    }

    public void load(final String path)
    {
        File file = new File(path);

        try
        {
            InputStream stream = new FileInputStream(file);

            StationList stationList = getStationsFromStream(stream);
            storeStationList(stationList.getStationList());
        }
        catch (FileNotFoundException e)
        {
            LOG.error("File at specified path does not exist: " + file.getAbsolutePath());
        }
    }

    private StationList getStationsFromStream(final InputStream stream)
    {
        StationList stationList = null;
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(StationList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            stationList = (StationList) jaxbUnmarshaller.unmarshal(stream);
        }
        catch (JAXBException ex)
        {
            LOG.error("Unable to unmarshall the Stations list");
            LOG.error("Error code: " + ex.getErrorCode());
            LOG.error(ex.getLocalizedMessage());
        }
        return stationList;
    }

    private void storeStationList(final List<Station> stationList)
    {
        stationMap.clear();
        if (stationList != null)
        {
            for (Station station : stationList)
            {
                stationMap.put(station.getName(), station);
            }
        }
        else
        {
            LOG.debug("Station list is null, unable to store.");
        }
        save();
    }

    private void save()
    {
        // If the file has come from the class path it's likely to be embedded in a compiled jar file
        // Trying to write to such a file will throw an error.
        // This could be worked around but has not been deemed necessary for this assignment.
        if (!fromClassPath)
        {
            LOG.debug("Saving changes to: " + path);
            save(path);
        }
    }

    /**
     * Save the configured station list to the directory specified
     * @param savePath Path to the directory to save the station list to
     */
    public void save(final String savePath)
    {
        File file = new File(savePath);
        LOG.debug("Saving to: " + file.getAbsolutePath());

        saveToFile(file);
    }

    private void saveToFile(final File file)
    {
        if (file.exists())
        {
            LOG.debug("file already in specified location, removing it");
            file.delete();
        }

        try
        {
            createSavePaths(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(StationList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StationList stationList = new StationList();
            stationList.setStationList(retrieveAll());
            jaxbMarshaller.marshal(stationList, file);
        }
        catch (JAXBException ex)
        {
            LOG.error("Problem marshalling to XML", ex);
            LOG.error("Error code: " + ex.getErrorCode());
            LOG.error(ex.getLocalizedMessage());
        }
    }

    private void createSavePaths(final File file)
    {
        try
        {
            File parent = new File(file.getParent());
            parent.mkdirs();
        }
        catch (Exception ex)
        {
            LOG.error("Problem create parent directories", ex);
        }
    }

    @Override
    public void deleteStation(final long stationId)
    {
        LOG.debug("Not implemented, as not required by any client use cases");
    }

    @Override
    public Station getStation(final String stationName)
    {
        return stationMap.get(stationName);
    }

    @Override
    public Station getStation(final Long stationId)
    {
        LOG.debug("Not implemented, as not required by any client use cases");
        return null;
    }

    @Override
    public void updateStation(final Station newDetails)
    {
        LOG.debug("Not implemented, as not required by any client use cases");
    }
}
