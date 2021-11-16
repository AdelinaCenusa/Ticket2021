package uk.ac.solent.lunderground.clientrest;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import uk.ac.solent.lunderground.model.dto.Rate;
import uk.ac.solent.lunderground.model.dto.Station;
import uk.ac.solent.lunderground.model.dto.Ticket;
import uk.ac.solent.lunderground.model.service.TicketMachineFacade;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RestServiceFacadeTest
{
    private static final String START_STATION = "Upney";
    private static final String DEST_STATION = "Whitechapel";
    private static final int START_ZONE = 2;
    private static final int DEST_ZONE = 4;
    private static final int VALID_ZONE = 3;
    private static final int INVALID_ZONE_LOW = START_ZONE - 1;
    private static final int INVALID_ZONE_HIGH = DEST_ZONE + 1;

    private static final int ISSUE_HOUR = 12;
    private static final int ISSUE_MIN = 30;
    private static final int EXPIRE_HOUR = 14;
    private static final int EXPIRE_MIN = 30;
    private static final int VALID_ENTRY_HOUR = ISSUE_HOUR + 1;
    private static final int BEFORE_VALID_FROM = ISSUE_HOUR  - 1;
    private static final int AFTER_VALID_TO    = EXPIRE_HOUR + 1;

    private static final int VALID_ENTRY_MIN = 31;
    private static final Double SOME_PRICE = 5.0;

    @Test
    public void verifyGateAccessReturnsTrueWhenTicketValidAndJourneyValid()
    {
        TicketMachineFacade facade = new RestServiceFacade("");
        // Create valid ticket
        Ticket validTicket = new Ticket();
        validTicket.setValidFrom(getDate(ISSUE_HOUR, ISSUE_MIN));
        validTicket.setValidTo(getDate(EXPIRE_HOUR, EXPIRE_MIN));
        validTicket.setRateBand(Rate.OffPeak);
        validTicket.setPrice(SOME_PRICE);
        Station start = new Station();
        start.setName(START_STATION);
        start.setZone(START_ZONE);
        validTicket.setStartStation(start);
        Station dest = new Station();
        dest.setName(DEST_STATION);
        dest.setZone(DEST_ZONE);
        validTicket.setDestStation(dest);

        // Invalidate the validation code
        String validXml = facade.encodeTicket(validTicket);

        boolean gateOpen = facade.verifyGateAccess(validXml, VALID_ZONE, VALID_ENTRY_HOUR, VALID_ENTRY_MIN);

        assertThat(gateOpen, equalTo(true));
    }

    @Test
    public void verifyGateAccessReturnsFalseWhenTicketInvalid()
    {
        TicketMachineFacade facade = new RestServiceFacade("");
        // Create valid ticket
        Ticket validTicket = new Ticket();
        validTicket.setValidFrom(getDate(ISSUE_HOUR, ISSUE_MIN));
        validTicket.setValidTo(getDate(EXPIRE_HOUR, EXPIRE_MIN));
        validTicket.setRateBand(Rate.OffPeak);
        validTicket.setPrice(SOME_PRICE);
        Station start = new Station();
        start.setName(START_STATION);
        start.setZone(START_ZONE);
        validTicket.setStartStation(start);
        Station dest = new Station();
        dest.setName(DEST_STATION);
        dest.setZone(DEST_ZONE);
        validTicket.setDestStation(dest);

        // Invalidate the validation code
        String validXml = facade.encodeTicket(validTicket);
        Document doc = parseXml(validXml);
        doc.getElementsByTagName("validationCode").item(0).setTextContent("invalid code");
        String invalidXml = toXml(doc);

        boolean gateOpen = facade.verifyGateAccess(invalidXml, START_ZONE, VALID_ENTRY_HOUR, VALID_ENTRY_MIN);

        assertThat(gateOpen, equalTo(false));
    }

    @Test
    public void verifyGateAccessReturnsFalseWhenLowGateZoneNotInJourneyZone()
    {
        TicketMachineFacade facade = new RestServiceFacade("");
        // Create valid ticket
        Ticket validTicket = new Ticket();
        validTicket.setValidFrom(getDate(ISSUE_HOUR, ISSUE_MIN));
        validTicket.setValidTo(getDate(EXPIRE_HOUR, EXPIRE_MIN));
        validTicket.setRateBand(Rate.OffPeak);
        validTicket.setPrice(SOME_PRICE);
        Station start = new Station();
        start.setName(START_STATION);
        start.setZone(START_ZONE);
        validTicket.setStartStation(start);
        Station dest = new Station();
        dest.setName(DEST_STATION);
        dest.setZone(DEST_ZONE);
        validTicket.setDestStation(dest);
        String validXml = facade.encodeTicket(validTicket);

        boolean gateOpen = facade.verifyGateAccess(validXml, INVALID_ZONE_LOW, VALID_ENTRY_HOUR, VALID_ENTRY_MIN);

        assertThat(gateOpen, equalTo(false));
    }

    @Test
    public void verifyGateAccessReturnsFalseWhenHighGateZoneNotInJourneyZone()
    {
        TicketMachineFacade facade = new RestServiceFacade("");
        // Create valid ticket
        Ticket validTicket = new Ticket();
        validTicket.setValidFrom(getDate(ISSUE_HOUR, ISSUE_MIN));
        validTicket.setValidTo(getDate(EXPIRE_HOUR, EXPIRE_MIN));
        validTicket.setRateBand(Rate.OffPeak);
        validTicket.setPrice(SOME_PRICE);
        Station start = new Station();
        start.setName(START_STATION);
        start.setZone(START_ZONE);
        validTicket.setStartStation(start);
        Station dest = new Station();
        dest.setName(DEST_STATION);
        dest.setZone(DEST_ZONE);
        validTicket.setDestStation(dest);
        String validXml = facade.encodeTicket(validTicket);

        boolean gateOpen = facade.verifyGateAccess(validXml, INVALID_ZONE_HIGH, VALID_ENTRY_HOUR, VALID_ENTRY_MIN);

        assertThat(gateOpen, equalTo(false));
    }

    @Test
    public void verifyGateAccessReturnsFalseWhenValidFromTimeAfterEntryGateTime()
    {
        TicketMachineFacade facade = new RestServiceFacade("");
        // Create valid ticket
        Ticket validTicket = new Ticket();
        validTicket.setValidFrom(getDate(ISSUE_HOUR, ISSUE_MIN));
        validTicket.setValidTo(getDate(EXPIRE_HOUR, EXPIRE_MIN));
        validTicket.setRateBand(Rate.OffPeak);
        validTicket.setPrice(SOME_PRICE);
        Station start = new Station();
        start.setName(START_STATION);
        start.setZone(START_ZONE);
        validTicket.setStartStation(start);
        Station dest = new Station();
        dest.setName(DEST_STATION);
        dest.setZone(DEST_ZONE);
        validTicket.setDestStation(dest);
        String validXml = facade.encodeTicket(validTicket);

        boolean gateOpen = facade.verifyGateAccess(validXml, VALID_ZONE, BEFORE_VALID_FROM, VALID_ENTRY_MIN);

        assertThat(gateOpen, equalTo(false));
    }

    @Test
    public void verifyGateAccessReturnsFalseWhenValidToTimeBeforeEntryGateTime()
    {
        TicketMachineFacade facade = new RestServiceFacade("");
        // Create valid ticket
        Ticket validTicket = new Ticket();
        validTicket.setValidFrom(getDate(ISSUE_HOUR, ISSUE_MIN));
        validTicket.setValidTo(getDate(EXPIRE_HOUR, EXPIRE_MIN));
        validTicket.setRateBand(Rate.OffPeak);
        validTicket.setPrice(SOME_PRICE);
        Station start = new Station();
        start.setName(START_STATION);
        start.setZone(START_ZONE);
        validTicket.setStartStation(start);
        Station dest = new Station();
        dest.setName(DEST_STATION);
        dest.setZone(DEST_ZONE);
        validTicket.setDestStation(dest);
        String validXml = facade.encodeTicket(validTicket);

        boolean gateOpen = facade.verifyGateAccess(validXml, VALID_ZONE, AFTER_VALID_TO, VALID_ENTRY_MIN);

        assertThat(gateOpen, equalTo(false));
    }

    // TO DO: Additional testing that should be added for verifyGateAccess
    //       -- Gate zone is on the boundary of the travelled zones
    //       -- Start zone greater than destination zone
    //       -- Combinations of invalid entry hours / minutes
    //       -- What happens if stations are  missing in the ticket XML
    //       -- What happens if other fields are missing in the ticket XML


    // Helper methods to simplify test cases
    private Date getDate(int hour, int minute)
    {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }

    /**
     * Helper method to convert an XML string into a Document.
     *
     * @param xmlString String containing XML fragment
     * @return XML Document
     */
    private Document parseXml(String xmlString)
    {
        Document doc = null;
        try
        {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                                                            .newDocumentBuilder();
            InputSource src = new InputSource();
            src.setCharacterStream(new StringReader(xmlString));
            doc = builder.parse(src);
        }
        catch (Exception ex)
        {
            System.out.println(ex.toString());
        }

        return doc;
    }

    /**
     * Helper method to convert a Document into an XML string.
     *
     * @param xmlDoc XML Document
     * @return String containing XML
     */
    private String toXml(Document xmlDoc)
    {
        String tmpString = null;
        try
        {
            DOMSource domSource = new DOMSource(xmlDoc);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            transformer.transform(domSource, result);
            tmpString = result.getWriter().toString();
        }
        catch (TransformerException ex)
        {
            System.out.println(ex.toString());
        }

        return tmpString;
    }
}
