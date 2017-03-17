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
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.io.TempFileCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class PdfAWriterTest {

    private static final String outputDir = "./target/test/writer/";

    static {
        new File(outputDir).mkdirs();
    }

    @Test
    public void testCreatePdfA_1() throws DocumentException, IOException {
        Document document;
        PdfAWriter writer;
        try {
            document = new Document();
            writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "testCreatePdfA_1.pdf"), PdfAConformanceLevel.PDF_A_1B);
            writer.createXmpMetadata();
            document.open();
            Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
            document.add(new Paragraph("Hello World", font));
            ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
            writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
            document.close();
        } catch (PdfAConformanceException e) {
            Assert.fail("PdfAConformance exception should not be thrown: " + e.getLocalizedMessage());
        }
    }

    @Test
    public void testCreatePdfA_2() throws DocumentException, IOException {
        boolean exceptionThrown = false;
        Document document;
        PdfAWriter writer;
        try {
            document = new Document();
            writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "testCreatePdfA_1.pdf"), PdfAConformanceLevel.PDF_A_1A);
            writer.createXmpMetadata();
            document.open();
            Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.NOT_EMBEDDED, 12, Font.BOLD);
            document.add(new Paragraph("Hello World", font));
            ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
            writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
            document.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");
    }

    @Test
    public void testPdfAStamper1() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "testPdfAStamper.pdf"), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();

        PdfReader reader = new PdfReader(outputDir + "testPdfAStamper.pdf");
        PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "testPdfAStamper_.pdf"), PdfAConformanceLevel.PDF_A_1B);
        stamper.close();
        reader.close();
    }

    @Test
    public void testPdfAStamper2() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "testPdfAStamper.pdf"), PdfAConformanceLevel.PDF_A_2B);
        writer.createXmpMetadata();
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.close();

        PdfReader reader = new PdfReader(outputDir + "testPdfAStamper.pdf");
        boolean exceptionThrown = false;
        try {
            PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "testPdfAStamper_.pdf"), PdfAConformanceLevel.PDF_A_1B);
            stamper.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        reader.close();
        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");
    }

    @Test
    public void testPdfAStamper3() throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputDir + "testPdfAStamper.pdf"));
        writer.createXmpMetadata();
        document.open();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        document.add(new Paragraph("Hello World", font));
        document.close();

        PdfReader reader = new PdfReader(outputDir + "testPdfAStamper.pdf");
        boolean exceptionThrown = false;
        try {
            PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(outputDir + "testPdfAStamper_.pdf"), PdfAConformanceLevel.PDF_A_1A);
            stamper.close();
        } catch (PdfAConformanceException e) {
            exceptionThrown = true;
        }
        reader.close();
        if (!exceptionThrown)
            Assert.fail("PdfAConformance exception should be thrown");
    }

    @Test
    public void taggedPdfADocumentUsingExternalCacheTest() throws IOException, DocumentException, InterruptedException, ParserConfigurationException, SAXException {
        int NUMBER_OF_ITERATIONS = 1000;

        Document document = new Document();
        Font font = FontFactory.getFont("./src/test/resources/com/itextpdf/text/pdf/FreeMonoBold.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED, 12);
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(outputDir + "MemoryPDF.pdf"), PdfAConformanceLevel.PDF_A_2A);

        TempFileCache fileCache = new TempFileCache(outputDir + "cacheFile");

        writer.createXmpMetadata();
        writer.setPdfVersion(PdfAWriter.PDF_VERSION_1_7);
        writer.addDeveloperExtension(PdfDeveloperExtension.ADOBE_1_7_EXTENSIONLEVEL3);
        writer.setTagged();

        writer.useExternalCacheForPdfA(fileCache);
        writer.useExternalCacheForTagStructure(fileCache);

        document.open();
        writer.createXmpMetadata();
        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        document.addLanguage("en");
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
        document.addTitle("Sample pdf for ada test");
        PdfPCell cell = null;
        for (int k = 1; k < NUMBER_OF_ITERATIONS; k++) {
            PdfPTable table = new PdfPTable(4);

            if (k % 1000 == 0)
                System.out.println("Row ::" + k);

            cell = new PdfPCell();
            cell.addElement(new Phrase(String.valueOf(k) + "_1", font));
            table.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Phrase(String.valueOf(k) + "_2", font));
            table.addCell(cell);

            cell = new PdfPCell();
            cell.addElement(new Phrase(String.valueOf(k) + "_3", font));
            table.addCell(cell);

            cell = new PdfPCell();
            PdfPTable inner_table = new PdfPTable(4);
            PdfPCell inner_cell = null;
            for (int j = 1; j < 5; j++) {
                inner_cell = new PdfPCell();
                inner_cell.addElement(new Phrase(String.valueOf(k) + "_" + j, font));
                inner_table.addCell(inner_cell);
            }
            cell.addElement(inner_table);
            table.addCell(cell);
            document.add(table);
        }
        document.close();
        writer.close();

        fileCache.close();


        String errorMessage;
        String outPdf = outputDir + "MemoryPDF.pdf";
        String cmpPdf = "./src/test/resources/com/itextpdf/text/pdf/externalCaching/cmp_MemoryPDF.pdf";

        CompareTool compareTool = new CompareTool();
        errorMessage = compareTool.compareByContent(outPdf, cmpPdf, outputDir, "diff");
        if (errorMessage != null)
            Assert.fail(errorMessage);

//        errorMessage = compareTool.compareTagStructures(outPdf, cmpPdf);
//        if (errorMessage != null)
//            Assert.fail(errorMessage);
    }
}
