package uk.ac.solent.lunderground.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "PricingDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class PricingDetails
{
    @XmlElementWrapper(name = "PriceBandList")
    @XmlElement(name = "PriceBand")
    private List<PriceBand> priceBands;

    private Double offPeakRate = 0.0;
    private Double peakRate = 0.0;

    public PricingDetails()
    {
        priceBands = new ArrayList<>();
    }

    public List<PriceBand> getPriceBands()
    {
        return priceBands;
    }

    public void setPriceBands(List<PriceBand> newPriceBandList)
    {
        this.priceBands = newPriceBandList;
    }

    public Double getOffPeakRate()
    {
        return offPeakRate;
    }

    public void setOffPeakRate(Double newRate)
    {
        this.offPeakRate = newRate;
    }

    public Double getPeakRate()
    {
        return peakRate;
    }

    public void setPeakRate(Double newRate)
    {
        this.peakRate = newRate;
    }

    @Override
    public String toString()
    {
        return "PricingDetails{" +
               "priceBands=" + priceBands +
               ", offPeakRate=" + offPeakRate +
               ", peakRate=" + peakRate +
               '}';
    }
}
