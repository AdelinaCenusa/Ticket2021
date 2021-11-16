package uk.ac.solent.lunderground.model.dao;

import uk.ac.solent.lunderground.model.dto.PriceBand;
import uk.ac.solent.lunderground.model.dto.PricingDetails;
import uk.ac.solent.lunderground.model.dto.Rate;
import uk.ac.solent.lunderground.model.dto.Station;

import java.util.Date;

public interface TicketPricingDao
{
    void addPriceBand(PriceBand newBand);
    void deleteAllPriceBands();
    Date getExpiryDate(Date issueDate);
    Double getJourneyPrice(Station startStation, Station destinationStation, Date issueDate);
    Double getPricePerZone(Date date);
    PricingDetails getPricingDetails();
    Rate getRateBand(Date date);
    void setOffPeakRate(Double newRate);
    void setPeakRate(Double newRate);
    void setPricingDetails(PricingDetails pricingDetails);
}
