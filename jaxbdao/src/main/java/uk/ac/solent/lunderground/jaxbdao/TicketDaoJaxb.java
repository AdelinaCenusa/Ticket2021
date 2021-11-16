package uk.ac.solent.lunderground.jaxbdao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.solent.lunderground.crypto.AsymmetricCryptography;
import uk.ac.solent.lunderground.model.dao.TicketDao;
import uk.ac.solent.lunderground.model.dto.Ticket;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class TicketDaoJaxb implements TicketDao
{
    /**
     * Logger instance for the TicketDaoJaxb implementation.
     */
    private static final Logger LOG = LogManager.getLogger(TicketDaoJaxb.class);

    private static final String PUBLIC_KEY_FILE = "publicKey";
    private static final String PRIVATE_KEY_FILE = "privateKey";

    @Override
    public String encodeTicket(final Ticket ticket)
    {
        String ticketXml = getMarshalledTicket(ticket);
        LOG.debug("Ticket to encode: \n" + ticketXml);

        String ticketXmlHash = getHash(ticketXml);
        LOG.debug("Unencoded ticket MD5 hash: \n" + ticketXmlHash);

        String encodedTicketHash = getEncodedHash(ticketXmlHash);
        LOG.debug("Encrypted ticket MD5 hash: " + encodedTicketHash);

        ticket.setValidationCode(encodedTicketHash);
        String validatedTicketXml = getMarshalledTicket(ticket);
        LOG.debug("ticket with encrypted hash: " + validatedTicketXml);

        return validatedTicketXml;
    }

    private String getMarshalledTicket(final Ticket ticket)
    {
        String ticketXml = null;
        //marshal ticket to xml
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(Ticket.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter sw1 = new StringWriter();
            jaxbMarshaller.marshal(ticket, sw1);

            ticketXml = sw1.toString();
        }
        catch (JAXBException ex)
        {
            LOG.error("problem marshalling ticket", ex);
        }
        return ticketXml;
    }

    private String getHash(final String ticketXml)
    {
        String ticketXmlHash = null;
        try
        {
            // hash the ticket XML
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(ticketXml.getBytes());
            byte[] digest = md.digest();
            ticketXmlHash = DatatypeConverter.printHexBinary(digest)
                                             .toUpperCase();
        }
        catch (NoSuchAlgorithmException ex)
        {
            LOG.error("Problem hashing ticket XML", ex);
        }
        return ticketXmlHash;
    }

    private String getEncodedHash(final String ticketXmlHash)
    {
        String encodedHash = null;
        try
        {
            // encode the hash using private key
            AsymmetricCryptography ac = new AsymmetricCryptography();
            PrivateKey privateKey = ac.getPrivateFromClassPath(PRIVATE_KEY_FILE);

            encodedHash = ac.encryptText(ticketXmlHash, privateKey);
        }
        catch (Exception ex)
        {
            LOG.error("problem encrypting ticket hash", ex);
        }
        return encodedHash;
    }

    @Override
    public Ticket getTicket(final String ticketXml)
    {
        Ticket decodedTicket = null;
        try
        {
            LOG.debug("Getting ticket from:");
            LOG.debug(ticketXml);

            // unmarshal encodedTicket ticket from XML
            JAXBContext jaxbContext = JAXBContext.newInstance(Ticket.class);
            Unmarshaller jaxbUnMarshaller = jaxbContext.createUnmarshaller();
            StringReader sr = new StringReader(ticketXml);
            decodedTicket = (Ticket) jaxbUnMarshaller.unmarshal(sr);
        }
        catch (Exception ex)
        {
            LOG.error("problem unmarshalling ticket", ex);
        }

        return decodedTicket;
    }

    @Override
    public boolean validateTicket(final Ticket ticket)
    {
        // get the encodedTicketHash and set encodedTicketHash null
        String encodedTicketHash = ticket.getValidationCode();
        ticket.setValidationCode(null);

        String ticketXml = getMarshalledTicket(ticket);
        String ticketHash = getHash(ticketXml);
        LOG.debug("Regenerated ticket hash : " + ticketHash);

        String decodedHash = getDecodedHash(encodedTicketHash);
        LOG.debug("DecryptedTicketHash     : " + decodedHash);

        return (ticketHash.equals(decodedHash));
    }

    private String getDecodedHash(final String encodedHash)
    {
        String hash = null;
        try
        {
            // decode encodedTicketHash
            AsymmetricCryptography ac = new AsymmetricCryptography();
            PublicKey publicKey = ac.getPublicFromClassPath(PUBLIC_KEY_FILE);

            hash = ac.decryptText(encodedHash, publicKey);
        }
        catch (Exception ex)
        {
            LOG.error("problem decrypting ticket hash", ex);
        }

        return hash;
    }
}
