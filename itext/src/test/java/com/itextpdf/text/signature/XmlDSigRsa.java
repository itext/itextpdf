package com.itextpdf.text.signature;

import com.itextpdf.text.pdf.security.DigestAlgorithms;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class XmlDSigRsa extends XmlDSigTests {

    public static final String KeyPairStore = "./src/test/resources/com/itextpdf/text/signature/rsa";
    public static final String SRC = "./src/test/resources/com/itextpdf/text/signature/xfa.pdf";
    public static final String CMP = "./src/test/resources/com/itextpdf/text/signature/rsa/xfa.signed.xml";
    public static final String DEST = "./target/com/itextpdf/test/signature/rsa";


    public static KeyPair loadKeyPair(String path, String algorithm) throws Exception {
        // Read Public Key.
        File filePublicKey = new File(path + "/public.key");
        FileInputStream fis = new FileInputStream(path + "/public.key");
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File(path + "/private.key");
        fis = new FileInputStream(path + "/private.key");
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

    @Before
    public void initialize() throws Exception {
        super.initialize();
        (new File(DEST)).mkdirs();
        keyPair = loadKeyPair(KeyPairStore, "RSA");
    }

    KeyPair keyPair;

    @Test
    public void XmlDSigRSAWithPublicKey() throws Exception {

        String output = DEST + "/xfa.signed.pk.pdf";
        signWithPublicKey(SRC, output, keyPair.getPrivate(), keyPair.getPublic(),
                DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue(compareXmls(cmp, CMP));
    }

    @Test
    public void XmlDSigRSAWithKeyInfo() throws Exception {

        String output = DEST + "/xfa.signed.ki.pdf";
        signWithKeyInfo(SRC, output, keyPair.getPrivate(), keyPair.getPublic(),
                DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue(compareXmls(cmp, CMP));
    }
}