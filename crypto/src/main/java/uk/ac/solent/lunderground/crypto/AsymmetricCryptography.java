package uk.ac.solent.lunderground.crypto;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.util.Base64;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AsymmetricCryptography
{

    final static Logger LOG = LogManager.getLogger(AsymmetricCryptography.class);

    private Cipher cipher = null;

    public AsymmetricCryptography() throws NoSuchAlgorithmException, NoSuchPaddingException
    {
        this.cipher = Cipher.getInstance("RSA");
    }

    public PrivateKey getPrivateFromClassPath(String filename) throws Exception
    {
        if (filename == null)
        {
            throw new NullPointerException("filename should not be null");
        }
        // using commons ioutils as only way to get bytes from input stream which comes from jar in war
        InputStream in = Thread.currentThread()
                               .getContextClassLoader()
                               .getResourceAsStream(filename);
        byte[] keyBytes = IOUtils.toByteArray(in);
        LOG.debug("getPrivateFromClassPath read keybytes.length bytes:" + keyBytes.length);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    // loads from classpath rather than absolute file location
    
    public PublicKey getPublicFromClassPath(String filename) throws Exception
    {
        if (filename == null)
        {
            throw new NullPointerException("filename should not be null");
        }
        InputStream in = Thread.currentThread()
                               .getContextClassLoader()
                               .getResourceAsStream(filename);
                               
        // using ioutils as only way to get bytes from input stream which comes from jar in war
        
        byte[] keyBytes = IOUtils.toByteArray(in);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String encryptText(String msg, PrivateKey key)
            throws UnsupportedEncodingException, IllegalBlockSizeException,
            BadPaddingException, InvalidKeyException
    {
        this.cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.getEncoder()
                     .encodeToString(cipher.doFinal(msg.getBytes("UTF-8")));
    }

    public String decryptText(String msg, PublicKey key)
            throws InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException
    {
        this.cipher.init(Cipher.DECRYPT_MODE, key);
        return new String(cipher.doFinal(Base64.getDecoder()
                                               .decode(msg)));
    }
}
