/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.text.signature;

import com.itextpdf.text.pdf.XfaXpathConstructor;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
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

/**
 * @deprecated For internal use only. If you want to use iText, please use a dependency on iText 7.
 */
@Deprecated
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
