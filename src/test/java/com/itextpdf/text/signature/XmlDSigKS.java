package com.itextpdf.text.signature;

import com.itextpdf.text.pdf.security.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;

public class XmlDSigKS extends XmlDSigTests {

    public static final String KEYSTORE = "./src/test/resources/com/itextpdf/text/signature/rsa-ks/ks";
    public static final char[] PASSWORD = "password".toCharArray();
    public static final String SRC = "./src/test/resources/com/itextpdf/text/signature/xfa.pdf";
    public static final String CMP = "./src/test/resources/com/itextpdf/text/signature/rsa-ks/xfa.signed.xml";
    public static final String DEST = "./target/com/itextpdf/test/signature/rsa-ks";


    @Test
    public void XmlDSigRsaKS() throws Exception {

        (new File(DEST)).mkdirs();
        super.initialize();

        String output = DEST + "/xfa.signed.pdf";

        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(new FileInputStream(KEYSTORE), PASSWORD);
        String alias = ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, PASSWORD);
        Certificate[] chain = ks.getCertificateChain(alias);
        signWithCertificate(SRC, output, pk, chain, DigestAlgorithms.SHA1, provider.getName());

        String cmp = saveXmlFromResult(output);

        Assert.assertTrue(compareXmls(cmp, CMP));
    }
}
