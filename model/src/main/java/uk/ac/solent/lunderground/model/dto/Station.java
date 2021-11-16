package uk.ac.solent.lunderground.model.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement(name = "Station")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public final class Station
{
    /**
     * Unique ID for the Station instance.
     */
    private Long id;
    /**
     * The name of the Station.
     */
    private String name;
    /**
     * The zone that the Station belongs to.
     */
    private int zone;

    /**
     * Get the ID of the Station.
     *
     * @return Long integer representing the ID of the station
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId()
    {
        return id;
    }

    /**
     * Set the ID of the Station.
     *
     * @param newId the new ID value for the Station
     */
    public void setId(final Long newId)
    {
        this.id = newId;
    }

    /**
     * Get the name of the Station.
     *
     * @return String containing the Station's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of the Station.
     *
     * @param newName String containing the Station name
     */
    public void setName(final String newName)
    {
        this.name = newName;
    }

    /**
     * Get the zone number that the Station is in.
     *
     * @return Integer containing the zone number
     */
    public int getZone()
    {
        return zone;
    }

    /**
     * Set the zone number that the Station is in.
     *
     * @param newZone the zone number that the station is in
     */
    public void setZone(final int newZone)
    {
        this.zone = newZone;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Station station = (Station) o;
        return zone == station.zone
               && Objects.equals(id, station.id)
               && Objects.equals(name, station.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, zone);
    }

    /**
     * String representation of the Station instance.
     */
    @Override
    public String toString()
    {
        return "Station{" + "name=" + name + ", zone=" + zone + ", id=" + id + "}";
    }
}
