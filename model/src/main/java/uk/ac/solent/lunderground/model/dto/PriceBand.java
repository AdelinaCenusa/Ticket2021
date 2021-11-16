package uk.ac.solent.lunderground.model.dto;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "PriceBand")
@XmlAccessorType(XmlAccessType.FIELD)
public class PriceBand implements Comparable<PriceBand>
{
    /**
     * The minimum allowable hour value.
     */
    private static final int HOUR_MIN = 0;
    /**
     * The maximum allowable hour value.
     */
    private static final int HOUR_MAX = 23;

    /**
     * The minimum allowable minute value.
     */
    private static final int MINUTE_MIN = 0;
    /**
     * The maximum allowable minute value.
     */
    private static final int MINUTE_MAX = 59;

    /**
     * The number of minutes in an hour.
     */
    private static final int MINUTES_IN_HOUR = 60;

    /**
     * The hour that this pricing band ends.
     */
    private int hour = HOUR_MIN;
    /**
     * The minute that this pricing band ends.
     */
    private int minute = MINUTE_MIN;
    /**
     * The pricing band rate to apply.
     */
    private Rate pricingRate = Rate.OffPeak;

    /**
     * Get the hour that this pricing band ends at.
     * @return the hour that this pricing band ends at
     */
    public int getHour()
    {
        return hour;
    }

    /**
     * Set the hour that this pricing band starts from.
     * @param startHour the hour that this pricing band starts from
     */
    public void setHour(int startHour)
    {
        if (startHour < HOUR_MIN || startHour > HOUR_MAX)
        {
            String message = "startHour out of bounds: " + startHour
                           + "\nstartHour must be between " + HOUR_MIN + " and " + HOUR_MAX + " inclusive";
            throw new IllegalArgumentException(message);
        }
        this.hour = startHour;
    }

    /**
     * Get the minutes that this pricing band ends at.
     * @return the minutes that this pricing band ends at
     */
    public int getMinute()
    {
        return minute;
    }

    /**
     * Set the minutes that this pricing band ends at.
     * @param startMinute the minutes that this pricing band ends at
     */
    public void setMinute(int startMinute)
    {
        if (startMinute < MINUTE_MIN || startMinute > MINUTE_MAX)
        {
            String message = "startMinute out of bounds: " + startMinute
                             + "\nstartMinute must be between " + MINUTE_MIN + " and " + MINUTE_MAX + " inclusive";
            throw new IllegalArgumentException(message);
        }
        this.minute = startMinute;
    }

    /**
     * The pricing Rate to be applied to this band.
     * @return The Rate to apply to this band
     */
    public Rate getPricingRate()
    {
        return pricingRate;
    }

    /**
     * Set the Rate to be applied to this band.
     * @param pricingRate the Rate to apply to this band
     */
    public void setPricingRate(Rate pricingRate)
    {
        this.pricingRate = pricingRate;
    }

    /**
     * The total number of minutes since midnight.
     * @return the total number of minutes since midnight.
     */
    public int getTimeInMinutes()
    {
        return (hour * MINUTES_IN_HOUR) + minute;
    }

    @Override
    public String toString()
    {
        return "PriceBand{" + "hour=" + hour + ", minute=" + minute + ", pricingRate=" + pricingRate + "}";
    }

    @Override
    public int compareTo(@NotNull PriceBand other)
    {
        return Integer.compare(this.getTimeInMinutes(), other.getTimeInMinutes());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        PriceBand priceBand = (PriceBand) o;
        return hour == priceBand.hour
               && minute == priceBand.minute
               && pricingRate == priceBand.pricingRate;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(hour, minute, pricingRate);
    }
}
