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


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.error_messages.MessageLocalization;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PdfACopyTest {

    protected static final String outputDir = "./target/test/copy/";

    static {
        new File(outputDir).mkdirs();
        try {
            MessageLocalization.setLanguage("en", "US");
        } catch (IOException e) {
        }
    }

    @Test
    public void testCreatePdfA_1() throws DocumentException, IOException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a.pdf";
        String testName = "testCreatePdfA_1.pdf";

        FileOutputStream outputPdfStream = new FileOutputStream(outputDir+testName);
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_1B);
        copy.createXmpMetadata();
        document.open();
        document.addLanguage("en-US");
        PdfReader reader = new PdfReader(f1);

        PdfImportedPage page = copy.getImportedPage(reader, 1);
        PdfCopy.PageStamp stamp = copy.createPageStamp(page);
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 24);
        ColumnText.showTextAligned(stamp.getUnderContent(), Element.ALIGN_CENTER, new Phrase("Hello world!", font), 100, 500, 0);
        stamp.alterContents();
        copy.addPage(page);
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        copy.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        copy.close();
    }

    @Test
    public void testMergeFields1() throws IOException, DocumentException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a-2.pdf";
        String testName = "testMergeFields1.pdf";

        FileOutputStream outputPdfStream = new FileOutputStream(outputDir+testName);
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_1A);
        copy.setMergeFields();
        copy.createXmpMetadata();
        copy.setTagged();
        document.open();
        document.addLanguage("en-US");
        for (String f : new String[] {f1, f2}) {
            PdfReader reader = new PdfReader(f);
            copy.addDocument(reader);
        }
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        copy.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        copy.close();
    }

    @Test
    public void testMergeFields2() throws DocumentException, IOException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a-2.pdf";
        String f3 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1b.pdf";

        OutputStream outputPdfStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_1A);
        copy.setMergeFields();
        copy.createXmpMetadata();
        copy.setTagged();
        document.open();
        document.addLanguage("en-US");

        boolean exceptionThrown = false;
        try {
            for (String f : new String[]{f1, f2, f3}) {
                PdfReader reader = new PdfReader(f);
                copy.addDocument(reader);
            }
        } catch (PdfAConformanceException e) {
            if (e.getMessage().contains("Incompatible PDF/A conformance level"))
                exceptionThrown = true;
        }

        if (!exceptionThrown)
            junit.framework.Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void testMergeFields3() throws DocumentException, IOException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-2a.pdf";

        OutputStream outputPdfStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_1A);
        copy.setMergeFields();
        copy.createXmpMetadata();
        copy.setTagged();
        document.open();
        document.addLanguage("en-US");

        boolean exceptionThrown = false;
        try {
            for (String f : new String[]{f1, f2}) {
                PdfReader reader = new PdfReader(f);
                copy.addDocument(reader);
            }
        } catch (PdfAConformanceException e) {
            if (e.getMessage().contains("Different PDF/A version"))
                exceptionThrown = true;
        }

        if (!exceptionThrown)
            junit.framework.Assert.fail("PdfAConformanceException should be thrown.");


    }

    @Test
    public void testMergeFields4() throws DocumentException, IOException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/copy/source16.pdf";

        OutputStream outputPdfStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_1B);
        copy.setMergeFields();
        copy.createXmpMetadata();
        copy.setTagged();
        document.open();
        document.addLanguage("en-US");
        boolean exceptionThrown = false;
        try {
            for (String f : new String[]{f1, f2}) {
                PdfReader reader = new PdfReader(f);
                copy.addDocument(reader);
            }
        } catch (PdfAConformanceException e) {
            if (e.getMessage().contains("Only PDF/A documents can be added in PdfACopy"))
                exceptionThrown = true;
        }

        if (!exceptionThrown)
            junit.framework.Assert.fail("PdfAConformanceException should be thrown.");
    }


    @Test
    public void testImportedPage1() throws IOException, DocumentException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a-2.pdf";
        String testName = "testImportedPage1.pdf";

        FileOutputStream outputPdfStream = new FileOutputStream(outputDir+testName);
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_1A);
        copy.createXmpMetadata();
        copy.setTagged();
        document.open();
        document.addLanguage("en-US");
        for (String f : new String[] {f1, f2}) {
            PdfReader reader = new PdfReader(f);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                copy.addPage(copy.getImportedPage(reader, i, true));
            }
        }
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        copy.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        copy.close();
    }

    @Test
    public void testImportedPage2() throws DocumentException, IOException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a-2.pdf";
        String f3 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-2a.pdf";

        OutputStream outputPdfStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_1A);
        copy.createXmpMetadata();
        copy.setTagged();
        document.open();
        document.addLanguage("en-US");

        boolean exceptionThrown = false;
        try {
            for (String f : new String[]{f1, f2, f3}) {
                PdfReader reader = new PdfReader(f);
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    copy.addPage(copy.getImportedPage(reader, i, true));
                }
            }
        } catch (PdfAConformanceException e) {
            if (e.getMessage().contains("Different PDF/A version"))
                exceptionThrown = true;
        }

        if (!exceptionThrown)
            junit.framework.Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void testImportedPage3() throws DocumentException, IOException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1b-2.pdf";
        String testName = "testImportedPage3.pdf";

        OutputStream outputPdfStream = new FileOutputStream(outputDir + testName);
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_1B);
        copy.createXmpMetadata();
        document.open();
        for (String f : new String[] {f1, f2}) {
            PdfReader reader = new PdfReader(f);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                copy.addPage(copy.getImportedPage(reader, i));
            }
        }
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        copy.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        copy.close();
    }

    @Test
    public void testImportedPage4() throws DocumentException, IOException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-2a.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-2u.pdf";
        String f3 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-2b.pdf";
        String testName = "testImportedPage4.pdf";

        OutputStream outputPdfStream = new FileOutputStream(outputDir + testName);
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_2B);
        copy.createXmpMetadata();
        document.open();
        for (String f : new String[] {f1, f2, f3}) {
            PdfReader reader = new PdfReader(f);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                copy.addPage(copy.getImportedPage(reader, i));
            }
        }
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        copy.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        copy.close();
    }

    @Test
    public void testImportedPage5() throws DocumentException, IOException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-3a.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-3u.pdf";
        String testName = "testImportedPage5.pdf";

        OutputStream outputPdfStream = new FileOutputStream(outputDir + testName);
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_3U);
        copy.createXmpMetadata();
        document.open();

        for (String f : new String[]{f1, f2}) {
            PdfReader reader = new PdfReader(f);
            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                copy.addPage(copy.getImportedPage(reader, i));
            }
        }
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        copy.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        copy.close();
    }

    @Test
    public void testImportedPage6() throws DocumentException, IOException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-3a.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-3u.pdf";
        String f3 = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-3b.pdf";
        String testName = "testImportedPage5.pdf";

        OutputStream outputPdfStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfACopy copy = new PdfACopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_3U);
        copy.createXmpMetadata();
        document.open();

        boolean exceptionThrown = false;
        try {
            for (String f : new String[]{f1, f2, f3}) {
                PdfReader reader = new PdfReader(f);
                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    copy.addPage(copy.getImportedPage(reader, i));
                }
            }
        } catch (PdfAConformanceException e) {
            if (e.getMessage().contains("Incompatible PDF/A conformance level"))
                exceptionThrown = true;
        }

        if (!exceptionThrown)
            junit.framework.Assert.fail("PdfAConformanceException should be thrown.");
    }

    @Test
    public void testSmartCopyCreatePdfA_1() throws DocumentException, IOException {
        String fileName = "./src/test/resources/com/itextpdf/text/pdf/copy/pdfa-1a.pdf";
        String testName = "testSmartCopyPdfA_1.pdf";

        FileOutputStream outputPdfStream = new FileOutputStream(outputDir+testName);
        Document document = new Document();
        PdfCopy copy = new PdfASmartCopy(document, outputPdfStream, PdfAConformanceLevel.PDF_A_1B);
        copy.createXmpMetadata();
        document.open();
        document.addLanguage("en-US");
        PdfReader reader = new PdfReader(fileName);
        PdfImportedPage page = copy.getImportedPage(reader, 1);
        copy.addPage(page);
        copy.close();
    }
}
