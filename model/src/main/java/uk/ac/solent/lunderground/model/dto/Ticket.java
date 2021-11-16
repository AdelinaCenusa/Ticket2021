package uk.ac.solent.lunderground.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.Objects;

@XmlRootElement(name = "Ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ticket
{
    private String id;
    private Station startStation;
    private Station destStation;
    private Date validFrom;
    private Date validTo;
    private Rate rateBand;
    private Double price;
    private String validationCode;

    public Station getStartStation()
    {
        return startStation;
    }

    public void setStartStation(Station startStation)
    {
        this.startStation = startStation;
    }

    public Station getDestStation()
    {
        return destStation;
    }

    public void setDestStation(Station destStation)
    {
        this.destStation = destStation;
    }

    public Date getValidFrom()
    {
        return validFrom;
    }

    public void setValidFrom(Date validFrom)
    {
        this.validFrom = validFrom;
    }

    public Date getValidTo()
    {
        return validTo;
    }

    public void setValidTo(Date validTo)
    {
        this.validTo = validTo;
    }

    public Rate getRateBand()
    {
        return rateBand;
    }

    public void setRateBand(Rate rateBand)
    {
        this.rateBand = rateBand;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public String getValidationCode()
    {
        return validationCode;
    }

    public void setValidationCode(String validationCode)
    {
        this.validationCode = validationCode;
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
        Ticket ticket = (Ticket) o;
        return Objects.equals(startStation, ticket.startStation) &&
               Objects.equals(destStation, ticket.destStation) &&
               Objects.equals(validFrom, ticket.validFrom) && Objects.equals(validTo, ticket.validTo) &&
               rateBand == ticket.rateBand && Objects.equals(price, ticket.price) &&
               Objects.equals(validationCode, ticket.validationCode);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(startStation, destStation, validFrom, validTo, rateBand, price, validationCode);
    }

    @Override
    public String toString()
    {
        return "Ticket{" +
               "startStation=" + startStation +
               ", destStation=" + destStation +
               ", validFrom=" + validFrom +
               ", validTo=" + validTo +
               ", rateBand=" + rateBand +
               ", price=" + price +
               ", validationCode='" + validationCode + '\'' +
               '}';
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
