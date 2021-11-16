package uk.ac.solent.lunderground.jaxbdao;

import org.springframework.core.io.ClassPathResource;
import uk.ac.solent.lunderground.model.dao.TicketPricingDao;
import uk.ac.solent.lunderground.model.dto.PriceBand;
import uk.ac.solent.lunderground.model.dto.PricingDetails;
import uk.ac.solent.lunderground.model.dto.Rate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.solent.lunderground.model.dto.Station;

import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class TicketPricingDaoJaxb implements TicketPricingDao
{
    private static final Logger LOG = LogManager.getLogger(TicketPricingDaoJaxb.class);

    private static final int VALID_LENGTH = 2;

    private final InputStream resourceFile;
    private Double offPeakRate = 0.0;
    private Double peakRate = 0.0;
    private final List<PriceBand> priceBands = new ArrayList<>();

    public TicketPricingDaoJaxb(@NotNull final String resourceName)
    {
        InputStream tmpFile = null;
        try
        {
            tmpFile = new ClassPathResource(resourceName).getInputStream();
        }
        catch (IOException e)
        {
            LOG.error("unable to find " + resourceName + " in the class path");
        }
        resourceFile = tmpFile;
    }

    @Override
    public synchronized void setOffPeakRate(final Double newRate)
    {
        offPeakRate = newRate;
    }

    @Override
    public synchronized void setPeakRate(final Double newRate)
    {
        peakRate = newRate;
    }

    @Override
    public synchronized Rate getRateBand(final Date date)
    {
        PriceBand testBand = getTestBand(date);

        Rate rate = Rate.OffPeak;
        for (PriceBand priceBand: priceBands)
        {
            if (testBand.getTimeInMinutes() > priceBand.getTimeInMinutes())
            {
                rate = priceBand.getPricingRate();
                break;
            }
        }
        return rate;
    }

    private PriceBand getTestBand(final Date date)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        PriceBand testBand = new PriceBand();
        testBand.setHour(cal.get(Calendar.HOUR_OF_DAY));
        testBand.setMinute(cal.get(Calendar.MINUTE));
        return testBand;
    }

    @Override
    public synchronized Double getPricePerZone(final Date date)
    {
        return getRateBand(date) == Rate.OffPeak ? offPeakRate : peakRate;
    }

    @Override
    public synchronized void addPriceBand(PriceBand newBand)
    {
        priceBands.add(newBand);
        setRequiredPriceBandOrder();
    }

    private void setRequiredPriceBandOrder()
    {
        Collections.sort(priceBands);
        Collections.reverse(priceBands);
    }

    @Override
    public synchronized void deleteAllPriceBands()
    {
        priceBands.clear();
    }

    @Override
    public Double getJourneyPrice(Station startStation, Station destinationStation, Date issueDate)
    {
        int zonesTravelled = getZonesTravelled(startStation, destinationStation);
        return getPricePerZone(issueDate) * zonesTravelled;
    }

    @Override
    public Date getExpiryDate(final Date issueDate)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTime(issueDate);
        cal.add(Calendar.HOUR_OF_DAY, VALID_LENGTH);

        return cal.getTime();
    }

    private int getZonesTravelled(final Station startStation, final Station destinationStation)
    {
        // We add 1 because we always travel in at least one zone.
        LOG.debug("Start station: " + startStation);
        LOG.debug("Dest station:  " + destinationStation);
        return Math.abs(startStation.getZone() - destinationStation.getZone()) + 1;
    }

    @Override
    public synchronized PricingDetails getPricingDetails()
    {
        PricingDetails details = new PricingDetails();
        details.setOffPeakRate(offPeakRate);
        details.setPeakRate(peakRate);
        details.setPriceBands(priceBands);
        return details;
    }

    @Override
    public synchronized void setPricingDetails(final PricingDetails newDetails)
    {
        storePricingDetails(newDetails);
    }

    public void save(final String savePath)
    {
        File file = new File(savePath);
        LOG.debug("Saving to: " + file.getAbsolutePath());

        if (file.exists())
        {
            LOG.debug("file already in specified location, removing it");
            file.delete();
        }
        saveToFile(file);
    }

    private void saveToFile(final File file)
    {
        try
        {
            createSavePaths(file);
            JAXBContext jaxbContext = JAXBContext.newInstance(PricingDetails.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            PricingDetails pricingDetails = getPricingDetails();
            jaxbMarshaller.marshal(pricingDetails, file);
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

    public void load()
    {
        PricingDetails pricingDetails = getPricingDetailsFromStream(resourceFile);
        storePricingDetails(pricingDetails);
    }

    private PricingDetails getPricingDetailsFromStream(final InputStream stream)
    {
        PricingDetails pricingDetails = null;
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(PricingDetails.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            pricingDetails = (PricingDetails) jaxbUnmarshaller.unmarshal(stream);
        }
        catch (JAXBException ex)
        {
            LOG.error("Unable to unmarshall the PricingDetails");
            LOG.error("Error code: " + ex.getErrorCode());
            LOG.error(ex.getLocalizedMessage());
        }
        return pricingDetails;
    }

    private void storePricingDetails(final PricingDetails pricingDetails)
    {
        priceBands.clear();
        if (pricingDetails != null)
        {
            peakRate = pricingDetails.getPeakRate();
            offPeakRate = pricingDetails.getOffPeakRate();
            priceBands.addAll(pricingDetails.getPriceBands());
            setRequiredPriceBandOrder();
        }
    }
}
