package com.itextpdf.text.signature;

import com.itextpdf.text.pdf.XfaXpathConstructor;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;

public class XmlDSigCertTest extends XmlDSigTest {

    public static final String KEYSTORE = "./src/test/resources/com/itextpdf/text/signature/cert/pkcs8.key";
    public static final String CERTIFICATE = "./src/test/resources/com/itextpdf/text/signature/cert/Certificate.crt";
    public static final String Src = "./src/test/resources/com/itextpdf/text/signature/xfa.pdf";
    public static final String CmpDir = "./src/test/resources/com/itextpdf/text/signature/ds-cert/";
    public static final String DestDir = "./target/com/itextpdf/test/signature/ds-cert/";


    @Test
    public void XmlDSigRsaCert() throws Exception {

        (new File(DestDir)).mkdirs();
        super.initialize();

        String filename = "xfa.signed.ds.cert.pdf";
        String output = DestDir + filename;

        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        Certificate cert = cf.generateCertificate(new FileInputStream(CERTIFICATE));
        Certificate[] chain = new Certificate[]{cert};

        // Read Private Key.
        File filePrivateKey = new File(KEYSTORE);
        FileInputStream fis = new FileInputStream(KEYSTORE);
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey pk = keyFactory.generatePrivate(privateKeySpec);

        signDsWithCertificate(Src, output, pk, chain, DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue("Verification", verifyXmlDSig(cmp));
        Assert.assertTrue(compareXmls(cmp, CmpDir + filename.replace(".pdf", ".xml")));
    }

    @Test
    public void XmlDSigRsaCertPackage() throws Exception {

        (new File(DestDir)).mkdirs();
        super.initialize();

        String filename = "xfa.signed.ds.cert.package.pdf";
        String output = DestDir + filename;

        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        Certificate cert = cf.generateCertificate(new FileInputStream(CERTIFICATE));
        Certificate[] chain = new Certificate[]{cert};

        // Read Private Key.
        File filePrivateKey = new File(KEYSTORE);
        FileInputStream fis = new FileInputStream(KEYSTORE);
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey pk = keyFactory.generatePrivate(privateKeySpec);

        signPackageDsWithCertificate(Src, output, XfaXpathConstructor.XdpPackage.Template, pk, chain, DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue("Verification", verifyXmlDSig(cmp));
        Assert.assertTrue(compareXmls(cmp, CmpDir + filename.replace(".pdf", ".xml")));
    }
}
