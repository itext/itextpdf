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
package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static junit.framework.Assert.assertNull;

public class PdfEncryptionTest {

    public static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/PdfEncryptionTest/";
    public static final String SOURCE_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/PdfEncryptionTest/";

    public static byte[] ownerPassword = "ownerPassword".getBytes();

    @Before
    public void setUp() {
        new File(DEST_FOLDER).mkdirs();
    }

    @Test
    public void encryptAES256() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "AES256Encrypted.pdf";
        String cmpPdf = SOURCE_FOLDER + "cmp_AES256Encrypted.pdf";
        Document doc = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(outPdf));
//        byte[] userPassword = "userPassword".getBytes();
        byte[] userPassword = null;
        pdfWriter.setEncryption(userPassword, ownerPassword, -1852, PdfWriter.ENCRYPTION_AES_256);
        doc.open();
        doc.add(new Paragraph("hello encrypted world"));
        doc.close();
        pdfWriter.close();

        assertNull(new CompareTool().compareByContent(outPdf, cmpPdf, DEST_FOLDER, "diff_"));
    }

    @Test
    public void stampAES256() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "stampAES256.pdf";
        String cmpPdf = SOURCE_FOLDER + "cmp_stampAES256.pdf";
        PdfReader reader = new PdfReader(SOURCE_FOLDER + "AES256EncryptedDocument.pdf", ownerPassword);
        PdfStamper pdfStamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        pdfStamper.close();

        assertNull(new CompareTool().compareByContent(outPdf, cmpPdf, DEST_FOLDER, "diff_"));

    }

    @Test
    public void unethicalStampAES256() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "unethicalStampAES256.pdf";
        String cmpPdf = SOURCE_FOLDER + "cmp_unethicalStampAES256.pdf";
        PdfReader reader = new PdfReader(SOURCE_FOLDER + "AES256EncryptedDocument.pdf");
        PdfReader.unethicalreading = true;
        PdfStamper pdfStamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        pdfStamper.close();

        assertNull(new CompareTool().compareByContent(outPdf, cmpPdf, DEST_FOLDER, "diff_"));
    }

    @Test
    public void computeUserPasswordAES256() throws Exception {
        String encryptedPdf = SOURCE_FOLDER + "cmp_AES256Encrypted.pdf";
        PdfReader reader = new PdfReader(encryptedPdf, ownerPassword);
        byte[] password = reader.computeUserPassword();
        reader.close();

        assertNull(password);
    }

    @Test
    public void encryptWithCertificateAndSignTest() throws IOException, DocumentException, GeneralSecurityException {
        removeCryptographyRestrictions();
        Security.addProvider(new BouncyCastleProvider());
        String inPdf = SOURCE_FOLDER + "in.pdf";
        String outPdf = DEST_FOLDER + "encrypt_cert_signed.pdf";
        String tmpPdf = DEST_FOLDER + "encrypt_cert.pdf";

        encryptPdfWithCertificate(inPdf, tmpPdf, SOURCE_FOLDER + "test.cer");

        Certificate cert = getPublicCertificate(SOURCE_FOLDER + "test.cer");
        PrivateKey privateKey = getPrivateKey(SOURCE_FOLDER + "test.p12");
        certSign(getPublicCertificate(SOURCE_FOLDER + "test.cer"), privateKey, outPdf, new PdfReader(tmpPdf, cert, privateKey, new BouncyCastleProvider().getName()), "reason", "location");
        restoreCryptographyRestrictions();
    }

    private static void encryptPdfWithCertificate(String sourceDocument, String targetDocument, String certPath) throws IOException, DocumentException, CertificateException {
        Certificate cert = getPublicCertificate(certPath);
        Certificate[] certs = new Certificate[] {cert};
        PdfReader reader = new PdfReader(sourceDocument);
        PdfStamper st = new PdfStamper(reader, new FileOutputStream(targetDocument), '\0', false);
        int[] x = new int[1];
        x[0] = PdfWriter.ALLOW_SCREENREADERS;
        st.setEncryption(certs, x, PdfWriter.STANDARD_ENCRYPTION_40);
        st.close();
    }

    private static Certificate getPublicCertificate(String path) throws IOException, CertificateException {
        FileInputStream is = new FileInputStream(path);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
        return cert;
    }

    private static PrivateKey getPrivateKey(String path) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream(path), "kspass".toCharArray());
        String alias = ks.aliases().nextElement();
        PrivateKey pk = (PrivateKey) ks.getKey(alias, "kspass".toCharArray());
        return pk;
    }

    private static void certSign(Certificate cert, PrivateKey privateKey, String destinationPath, PdfReader reader, String reason, String location) throws IOException, DocumentException, GeneralSecurityException {
        Certificate[] chain = new Certificate[] {cert};

        BouncyCastleProvider provider = new BouncyCastleProvider();
        ExternalSignature pks = new PrivateKeySignature(privateKey, DigestAlgorithms.SHA1, provider.getName());

        FileOutputStream fout = new FileOutputStream(destinationPath);
        PdfStamper stamper = PdfStamper.createSignature(reader, fout, '\0', null, true);
        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
        appearance.setReason(reason);
        appearance.setLocation(location);
        ExternalDigest digest = new BouncyCastleDigest();
        MakeSignature.signDetached(appearance, digest, pks, chain, null, null, null, 0,
                MakeSignature.CryptoStandard.CADES);
        stamper.close();
    }

    /**
     * Due to import control restrictions by the governments of a few countries,
     * the encryption libraries shipped by default with the Java SDK restrict the
     * length, and as a result the strength, of encryption keys. Be aware that by
     * using this method we remove cryptography restrictions via reflection for
     * testing purposes.
     * <br/>
     * For more conventional way of solving this problem you need to replace the
     * default security JARs in your Java installation with the Java Cryptography
     * Extension (JCE) Unlimited Strength Jurisdiction Policy Files. These JARs
     * are available for download from http://java.oracle.com/ in eligible countries.
     */
    public static void removeCryptographyRestrictions() {
        try {
            Field field = Class.forName("javax.crypto.JceSecurity").
                    getDeclaredField("isRestricted");
            if (field.isAccessible()) {
                // unexpected case
                return;
            }

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            modifiersField.setAccessible(false);

            field.setAccessible(true);
            if (field.getBoolean(null)) {
                field.set(null, java.lang.Boolean.FALSE);
            } else {
                field.setAccessible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * By using this method we restore cryptography restrictions via reflection.
     */
    public static void restoreCryptographyRestrictions() {
        try {
            Field field = Class.forName("javax.crypto.JceSecurity").
                    getDeclaredField("isRestricted");
            if (field.isAccessible()) {
                field.set(null, java.lang.Boolean.TRUE);
                field.setAccessible(false);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
