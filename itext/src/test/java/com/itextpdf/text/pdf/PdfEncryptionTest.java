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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

import static junit.framework.Assert.*;

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
}
