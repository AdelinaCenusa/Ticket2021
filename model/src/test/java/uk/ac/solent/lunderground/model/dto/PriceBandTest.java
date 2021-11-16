package uk.ac.solent.lunderground.model.dto;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

public class PriceBandTest
{
    private static final int SOME_HOUR = 14;
    private static final int LOW_HOUR = -1;
    private static final int HIGH_HOUR = 24;
    private static final int SOME_MINUTE = 33;
    private static final int LOW_MINUTE = -1;
    private static final int HIGH_MINUTE = 60;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int EXPECTED_TIME_IN_MINUTES = (SOME_HOUR * MINUTES_IN_HOUR) + SOME_MINUTE;
    private static final String EXPECTED_STRING = "PriceBand{hour=14, minute=33, pricingRate=Peak}";

    @Test
    public void setHourSetsHourOfBandToPassedInValue()
    {
        PriceBand band = new PriceBand();
        band.setHour(SOME_HOUR);

        assertThat(band.getHour(), equalTo(SOME_HOUR));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHourThrowsExceptionWhenPassedInValueTooLow()
    {
        PriceBand band = new PriceBand();
        band.setHour(LOW_HOUR);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHourThrowsExceptionWhenPassedInValueTooHigh()
    {
        PriceBand band = new PriceBand();
        band.setHour(HIGH_HOUR);
    }

    @Test
    public void setMinutesSetsMinutesOfBandToPassedInValue()
    {
        PriceBand band = new PriceBand();
        band.setMinute(SOME_MINUTE);

        assertThat(band.getMinute(), equalTo(SOME_MINUTE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinuteThrowsExceptionWhenPassedInValueTooLow()
    {
        PriceBand band = new PriceBand();
        band.setMinute(LOW_MINUTE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setMinuteThrowsExceptionWhenPassedInValueTooHigh()
    {
        PriceBand band = new PriceBand();
        band.setMinute(HIGH_MINUTE);
    }

    @Test
    public void getTimeInMinutesUsesHourValueInCalculation()
    {
        PriceBand band = new PriceBand();
        band.setHour(SOME_HOUR);

        assertThat(band.getTimeInMinutes(), equalTo(SOME_HOUR * MINUTES_IN_HOUR));
    }

    @Test
    public void getTimeInMinutesUsesMinutesValueInCalculation()
    {
        PriceBand band = new PriceBand();
        band.setMinute(SOME_MINUTE);

        assertThat(band.getTimeInMinutes(), equalTo(SOME_MINUTE));
    }


    @Test
    public void getTimeInMinutesUsesHoursAndMinutesValueInCalculation()
    {
        PriceBand band = new PriceBand();
        band.setHour(SOME_HOUR);
        band.setMinute(SOME_MINUTE);

        assertThat(band.getTimeInMinutes(), equalTo(EXPECTED_TIME_IN_MINUTES));
    }

    @Test
    public void getPricingRateReturnsOffPeakByDefault()
    {
        PriceBand band = new PriceBand();

        assertThat(band.getPricingRate(), equalTo(Rate.OffPeak));
    }

    @Test
    public void setPricingRateSetsRateToPassedInValue()
    {
        PriceBand band = new PriceBand();
        band.setPricingRate(Rate.Peak);

        assertThat(band.getPricingRate(), equalTo(Rate.Peak));
    }

    @Test
    public void toStringReturnsFormattedString()
    {
        PriceBand band = new PriceBand();
        band.setHour(SOME_HOUR);
        band.setMinute(SOME_MINUTE);
        band.setPricingRate(Rate.Peak);

        assertThat(band.toString(), equalTo(EXPECTED_STRING));
    }

    @Test
    public void compareToReturnsZeroWhenGetTimeInMinutesReturnSameValue()
    {
        PriceBand band = new PriceBand();
        band.setHour(SOME_HOUR);
        band.setMinute(SOME_MINUTE);

        PriceBand other = new PriceBand();
        other.setHour(SOME_HOUR);
        other.setMinute(SOME_MINUTE);

        assertThat(band.compareTo(other), equalTo(0));
    }

    @Test
    public void compareToReturnsGreaterThanZeroWhenOtherGetTimeInMinutesReturnSmallerValue()
    {
        PriceBand band = new PriceBand();
        band.setHour(SOME_HOUR);
        band.setMinute(SOME_MINUTE);

        PriceBand other = new PriceBand();
        other.setHour(SOME_HOUR);
        other.setMinute(SOME_MINUTE - 1);

        assertThat(band.compareTo(other), greaterThan(0));
    }

    @Test
    public void compareToReturnsLessThanZeroWhenOtherGetTimeInMinutesReturnBiggerValue()
    {
        PriceBand band = new PriceBand();
        band.setHour(SOME_HOUR);
        band.setMinute(SOME_MINUTE);

        PriceBand other = new PriceBand();
        other.setHour(SOME_HOUR);
        other.setMinute(SOME_MINUTE + 1);

        assertThat(band.compareTo(other), lessThan(0));
    }

    @Test
    public void equalsReturnsTrueWhenInstancesHaveSameConfiguration()
    {
        PriceBand band = new PriceBand();
        PriceBand other = new PriceBand();

        assertThat(band, equalTo(other));
    }
}
