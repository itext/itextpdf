/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
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
