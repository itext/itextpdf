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

public class XadesTest extends XmlDSigTest {

    public static final String KEYSTORE = "./src/test/resources/com/itextpdf/text/signature/cert/pkcs8.key";
    public static final String CERTIFICATE = "./src/test/resources/com/itextpdf/text/signature/cert/Certificate.crt";
    public static final String Src = "./src/test/resources/com/itextpdf/text/signature/xfa.pdf";
    public static final String DestDirBes = "./target/com/itextpdf/test/signature/xades-bes/";
    public static final String DestDirEpes = "./target/com/itextpdf/test/signature/xades-epes/";


    @Test
    public void XadesBesRsaCert() throws Exception {

        (new File(DestDirBes)).mkdirs();
        super.initialize();

        String filename = "xfa.signed.xades-bes.pdf";
        String output = DestDirBes + filename;

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

        signXadesWithCertificate(Src, output, pk, chain, DigestAlgorithms.SHA1, provider.getName(), false);

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue("Verification", verifyXmlDSig(cmp));
    }

    @Test
    public void XadesBesRsaCertPackage() throws Exception {

        (new File(DestDirBes)).mkdirs();
        super.initialize();

        String filename = "xfa.signed.xades-bes.package.pdf";
        String output = DestDirBes + filename;

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

        signPackageXadesWithCertificate(Src, output, XfaXpathConstructor.XdpPackage.Template, pk, chain, DigestAlgorithms.SHA1, provider.getName(), false);

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue("Verification", verifyXmlDSig(cmp));
    }

    @Test
    public void XadesEpesRsaCert() throws Exception {

        (new File(DestDirEpes)).mkdirs();
        super.initialize();

        String filename = "xfa.signed.xades-epes.pdf";
        String output = DestDirEpes + filename;

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

        signXadesWithCertificate(Src, output, pk, chain, DigestAlgorithms.SHA1, provider.getName(), true);

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue("Verification", verifyXmlDSig(cmp));
    }

    @Test
    public void XadesEpesRsaCertPackage() throws Exception {

        (new File(DestDirEpes)).mkdirs();
        super.initialize();

        String filename = "xfa.signed.xades-epes.package.pdf";
        String output = DestDirEpes + filename;

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

        signPackageXadesWithCertificate(Src, output, XfaXpathConstructor.XdpPackage.Template, pk, chain, DigestAlgorithms.SHA1, provider.getName(), true);

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue("Verification", verifyXmlDSig(cmp));
    }
}
