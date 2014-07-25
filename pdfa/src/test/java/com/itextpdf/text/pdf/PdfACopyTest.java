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

    private static final String outputDir = "./target/test/copy/";

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
}
