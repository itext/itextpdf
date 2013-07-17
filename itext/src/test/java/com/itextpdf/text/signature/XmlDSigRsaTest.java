package com.itextpdf.text.signature;

import com.itextpdf.text.pdf.XfaXpathConstructor;
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

public class XmlDSigRsaTest extends XmlDSigTest {

    public static final String KeyPairStore = "./src/test/resources/com/itextpdf/text/signature/ds/";
    public static final String Src = "./src/test/resources/com/itextpdf/text/signature/xfa.pdf";
    public static final String CmpDir = "./src/test/resources/com/itextpdf/text/signature/ds/";
    public static final String DestDir = "./target/com/itextpdf/test/signature/ds/";


    public static KeyPair loadKeyPair(String path, String algorithm) throws Exception {
        // Read Public Key.
        File filePublicKey = new File(path + "public.key");
        FileInputStream fis = new FileInputStream(path + "public.key");
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File(path + "private.key");
        fis = new FileInputStream(path + "private.key");
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
        (new File(DestDir)).mkdirs();
        keyPair = loadKeyPair(KeyPairStore, "RSA");
    }

    KeyPair keyPair;

    @Test
    public void XmlDSigRSAWithPublicKey() throws Exception {

        String filename = "xfa.signed.ds.pk.pdf";
        String output = DestDir + filename;
        signDsWithPublicKey(Src, output, keyPair.getPrivate(), keyPair.getPublic(),
                DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);

        Assert.assertTrue(compareXmls(cmp, CmpDir + filename.replace(".pdf", ".xml")));
    }

    @Test
    public void XmlDSigRSAWithKeyInfo() throws Exception {

        String filename = "xfa.signed.ds.ki.pdf";
        String output = DestDir + filename;

        signDsWithKeyInfo(Src, output, keyPair.getPrivate(), keyPair.getPublic(),
                DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue(compareXmls(cmp, CmpDir + filename.replace(".pdf", ".xml")));
    }

    @Test
    public void XmlDSigRSAWithPublicKeyPackage() throws Exception {

        String filename = "xfa.signed.ds.pk.package.pdf";
        String output = DestDir + filename;
        signPackageDsWithPublicKey(Src, output, XfaXpathConstructor.XdpPackage.Template, keyPair.getPrivate(), keyPair.getPublic(), DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue(compareXmls(cmp, CmpDir + filename.replace(".pdf", ".xml")));
    }

    @Test
    public void XmlDSigRSAWithKeyInfoPackage() throws Exception {

        String filename = "xfa.signed.ds.ki.package.pdf";
        String output = DestDir + filename;

        signPackageDsWithKeyInfo(Src, output, XfaXpathConstructor.XdpPackage.Template, keyPair.getPrivate(), keyPair.getPublic(), DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue(compareXmls(cmp, CmpDir + filename.replace(".pdf", ".xml")));
    }
}