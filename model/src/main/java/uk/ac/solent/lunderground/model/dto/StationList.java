package uk.ac.solent.lunderground.model.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "StationList")
@XmlAccessorType(XmlAccessType.FIELD)
public final class StationList
{
    @XmlElement(name = "Station")
    private List<Station> stationList = null;

    /**
     *
     * @return
     */
    public List<Station> getStationList()
    {
        return this.stationList;
    }

    /**
     *
     * @param stations
     */
    public void setStationList(final List<Station> stations)
    {
        this.stationList = stations;
    }
}
