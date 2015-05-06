package com.itextpdf.text.signature;

import com.itextpdf.text.pdf.XfaXpathConstructor;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

public class XmlDSigKSTest extends XmlDSigTest {

    public static final String KEYSTORE = "./src/test/resources/com/itextpdf/text/signature/ds-ks/ks";
    public static final char[] PASSWORD = "password".toCharArray();
    public static final String Src = "./src/test/resources/com/itextpdf/text/signature/xfa.pdf";
    public static final String CmpDir = "./src/test/resources/com/itextpdf/text/signature/ds-ks/";
    public static final String DestDir = "./target/com/itextpdf/test/signature/ds-ks/";


    @Test
    public void XmlDSigRsaKS() throws Exception {

        (new File(DestDir)).mkdirs();
        super.initialize();

        String filename = "xfa.signed.ds.ks.pdf";
        String output = DestDir + filename;

        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(KEYSTORE), PASSWORD);
        String alias = ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
        Certificate[] chain = ks.getCertificateChain(alias);
        signDsWithCertificate(Src, output, pk, chain, DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue("Verification", verifyXmlDSig(cmp));
        Assert.assertTrue(compareXmls(cmp, CmpDir + filename.replace(".pdf", ".xml")));
    }

    @Test
    public void XmlDSigRsaKSPackage() throws Exception {

        (new File(DestDir)).mkdirs();
        super.initialize();

        String filename = "xfa.signed.ds.ks.package.pdf";
        String output = DestDir + filename;

        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(KEYSTORE), PASSWORD);

        String alias = ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
        Certificate[] chain = ks.getCertificateChain(alias);
        signPackageDsWithCertificate(Src, output, XfaXpathConstructor.XdpPackage.Template, pk, chain, DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);
        Assert.assertTrue("Verification", verifyXmlDSig(cmp));
        Assert.assertTrue(compareXmls(cmp, CmpDir + filename.replace(".pdf", ".xml")));
    }

}
