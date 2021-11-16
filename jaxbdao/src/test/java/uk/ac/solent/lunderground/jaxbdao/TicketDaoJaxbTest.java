package uk.ac.solent.lunderground.jaxbdao;

import uk.ac.solent.lunderground.model.dao.TicketDao;
import uk.ac.solent.lunderground.model.dto.Ticket;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TicketDaoJaxbTest
{
    @Test
    public void encodeTicketReturnsXmlRepresentationOfPassedInTicket()
    {
        // We cannot simply use an XML string with a pre-generated validationCode.
        // This is due to the crypto keys being generated with different values if
        // they don't already exist. This is especially problematic when using a
        // CI/CD server (e.g. github actions) that builds from a fresh start every time.

        TicketDao ticketDao = new TicketDaoJaxb();

        String xml = ticketDao.encodeTicket(new Ticket());

        assertThat(xml, notNullValue());
    }

    @Test
    public void decodeTicketReturnsTicketFromPassedInXml()
    {
        TicketDao ticketDao = new TicketDaoJaxb();
        Ticket ticket = new Ticket();
        String ticketXml = ticketDao.encodeTicket(ticket);

        Ticket decodedTicket = ticketDao.getTicket(ticketXml);

        assertThat(decodedTicket, equalTo(ticket));
    }

    @Test
    public void validateTicketReturnsTrueWhenTicketHasValidValidationCode()
    {
        TicketDao ticketDao = new TicketDaoJaxb();
        String validXml = ticketDao.encodeTicket(new Ticket());
        Ticket decodedTicket = ticketDao.getTicket(validXml);

        assertThat(ticketDao.validateTicket(decodedTicket), equalTo(true));
    }

    @Test
    public void validateTicketReturnsTrueWhenTicketHasInvalidValidationCode()
    {
        // We cannot simply use an XML string with a pre-generated validationCode.
        // This is due to the crypto keys being generated with different values if
        // they don't already exist. This is especially problematic when using a
        // CI/CD server (e.g. github actions) that builds from a fresh start every time.

        TicketDao ticketDao = new TicketDaoJaxb();
        String validXml = ticketDao.encodeTicket(new Ticket());
        Document doc = parseXml(validXml);
        doc.getElementsByTagName("validationCode").item(0).setTextContent("invalid code");
        String invalidXml = toXml(doc);
        Ticket decodedTicket = ticketDao.getTicket(invalidXml);

        assertThat(ticketDao.validateTicket(decodedTicket), equalTo(false));
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

    // TODO: additional tests that should be added
    //       -- check each field marshalled correctly
    //       -- check each field unmarshalled correctly
    //       -- check that changing fields in the generated XML causes validation to return false
    //       -- should a ticket with missing fields, e.g. price, zones, stations, etc fail to encode?
    //          This would have an impact on tests that use minimal ticket configuration in order to be more readable
    //       --
}
