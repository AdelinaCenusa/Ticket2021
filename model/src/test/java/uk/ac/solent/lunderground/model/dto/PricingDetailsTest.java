package uk.ac.solent.lunderground.model.dto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class PricingDetailsTest
{
    private static final Double DEFAULT_RATE = 0.0;
    private static final Double SOME_RATE = 1.0;

    @Test
    public void constructorInitialisesPricingList()
    {
        PricingDetails pricingDetails = new PricingDetails();

        assertThat(pricingDetails.getPriceBands(), notNullValue());
    }

    @Test
    public void setPriceBandsSetsPriceBandsToPassedInValue()
    {
        PricingDetails pricingDetails = new PricingDetails();
        List<PriceBand> bands = new ArrayList<>();
        pricingDetails.setPriceBands(bands);

        assertThat(pricingDetails.getPriceBands(), equalTo(bands));
    }

    @Test
    public void getOffPeakRateReturnsZeroByDefault()
    {
        PricingDetails pricingDetails = new PricingDetails();

        assertThat(pricingDetails.getOffPeakRate(), equalTo(DEFAULT_RATE));
    }

    @Test
    public void setOffPeakRateSetsOffPeakRateToPassedInValue()
    {
        PricingDetails pricingDetails = new PricingDetails();
        pricingDetails.setOffPeakRate(SOME_RATE);

        assertThat(pricingDetails.getOffPeakRate(), equalTo(SOME_RATE));
    }

    @Test
    public void getPeakRateReturnsZeroByDefault()
    {
        PricingDetails pricingDetails = new PricingDetails();

        assertThat(pricingDetails.getPeakRate(), equalTo(DEFAULT_RATE));
    }

    @Test
    public void setPeakRateSetsPeakRateToPassedInValue()
    {
        PricingDetails pricingDetails = new PricingDetails();
        pricingDetails.setPeakRate(SOME_RATE);

        assertThat(pricingDetails.getPeakRate(), equalTo(SOME_RATE));
    }
}
