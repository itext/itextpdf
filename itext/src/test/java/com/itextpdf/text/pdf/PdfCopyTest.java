/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;


import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;


/**
 * @author kevin
 */
public class PdfCopyTest {

    @Before
    public void setUp() throws Exception {
        TestResourceUtils.purgeTempFiles();
        new File("./target/com/itextpdf/test/pdf/PdfCopyTest").mkdirs();
    }

    @After
    public void tearDown() throws Exception {
        TestResourceUtils.purgeTempFiles();
    }

    @Test
    /**
     * Test to demonstrate issue https://sourceforge.net/tracker/?func=detail&aid=3013642&group_id=15255&atid=115255
     */
    public void testExtraXObjects() throws Exception {
        PdfReader sourceR = new PdfReader(createImagePdf());
        try {
            int sourceXRefCount = sourceR.getXrefSize();

            final Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfCopy copy = new PdfCopy(document, out);
            document.open();
            PdfImportedPage importedPage = copy.getImportedPage(sourceR, 1);
            copy.addPage(importedPage);
            document.close();

            PdfReader targetR = new PdfReader(out.toByteArray());
            int destinationXRefCount = targetR.getXrefSize();

            //        TestResourceUtils.saveBytesToFile(createImagePdf(), new File("./source.pdf"));
            //        TestResourceUtils.saveBytesToFile(out.toByteArray(), new File("./result.pdf"));

            Assert.assertEquals(sourceXRefCount, destinationXRefCount);
        } finally {
            sourceR.close();
        }
    }

    @Test
    /**
     * Test to make sure that the following issue is fixed: http://sourceforge.net/mailarchive/message.php?msg_id=30891213
     */
    public void testDecodeParmsArrayWithNullItems() throws IOException, DocumentException {
        Document document = new Document();
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        PdfSmartCopy pdfSmartCopy = new PdfSmartCopy(document, byteStream);
        document.open();

        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, "imgWithDecodeParms.pdf");
        pdfSmartCopy.addPage(pdfSmartCopy.getImportedPage(reader, 1));

        document.close();
        reader.close();

        reader = new PdfReader(byteStream.toByteArray());
        PdfDictionary page = reader.getPageN(1);
        PdfDictionary resources = page.getAsDict(PdfName.RESOURCES);
        PdfDictionary xObject = resources.getAsDict(PdfName.XOBJECT);
        PdfStream img = xObject.getAsStream(new PdfName("Im0"));
        PdfArray decodeParms = img.getAsArray(PdfName.DECODEPARMS);
        Assert.assertEquals(2, decodeParms.size());
        Assert.assertTrue(decodeParms.getPdfObject(0) instanceof PdfNull);

        reader.close();
    }

    @Test
    public void testNeedAppearances() throws DocumentException, IOException, InterruptedException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances1.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances2.pdf";
        String f3 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances3.pdf";
        String f4 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances4.pdf";

        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();
        FileOutputStream outputPdfStream = new FileOutputStream("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances.pdf");
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, outputPdfStream);
        copy.setMergeFields();
        document.open();
        for (String f : new String[]{f1, f2, f3, f4}) {
            PdfReader r = new PdfReader(f);
            copy.addDocument(r);
        }
        copy.close();
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances.pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_appearances.pdf", "./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testNeedAppearancesFalse() throws DocumentException, IOException, InterruptedException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances1(needAppearancesFalse).pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances2(needAppearancesFalse).pdf";
        String f3 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances3(needAppearancesFalse).pdf";
        String f4 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances4(needAppearancesFalse).pdf";

        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();
        FileOutputStream outputPdfStream = new FileOutputStream("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances(needAppearancesFalse).pdf");
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, outputPdfStream);
        copy.setMergeFields();
        document.open();
        for (String f : new String[]{f1, f2, f3, f4}) {
            PdfReader r = new PdfReader(f);
            copy.addDocument(r);
        }
        copy.close();
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances(needAppearancesFalse).pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_appearances(needAppearancesFalse).pdf", "./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testNeedAppearancesFalseWithStreams() throws DocumentException, IOException, InterruptedException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances1(needAppearancesFalseWithStreams).pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances2(needAppearancesFalseWithStreams).pdf";
        String f3 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances3(needAppearancesFalseWithStreams).pdf";
        String f4 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances4(needAppearancesFalseWithStreams).pdf";

        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();
        FileOutputStream outputPdfStream = new FileOutputStream("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances(needAppearancesFalseWithStreams).pdf");
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, outputPdfStream);
        copy.setMergeFields();
        document.open();
        for (String f : new String[]{f1, f2, f3, f4}) {
            PdfReader r = new PdfReader(f);
            copy.addDocument(r);
        }
        copy.close();
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances(needAppearancesFalseWithStreams).pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_appearances(needAppearancesFalseWithStreams).pdf", "./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testNeedAppearancesMixed() throws DocumentException, IOException, InterruptedException {
        String f1 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances1.pdf";
        String f2 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances2(needAppearancesFalse).pdf";
        String f3 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances3(needAppearancesFalseWithStreams).pdf";
        String f4 = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances4.pdf";

        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();
        FileOutputStream outputPdfStream = new FileOutputStream("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances(mixed).pdf");
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, outputPdfStream);
        copy.setMergeFields();
        document.open();
        for (String f : new String[]{f1, f2, f3, f4}) {
            PdfReader r = new PdfReader(f);
            copy.addDocument(r);
        }
        copy.close();
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances(mixed).pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_appearances(mixed).pdf", "./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void testFullCompression1() throws DocumentException, IOException {
        String outfile = "./target/com/itextpdf/test/pdf/PdfCopyTest/out-noforms.pdf";
        String first = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/hello.pdf";
        String second = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/hello_memory.pdf";
        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();

        OutputStream out = new FileOutputStream(outfile);
        PdfReader reader = new PdfReader(first);
        PdfReader reader2 = new PdfReader(second);
        Document pdfDocument = new Document();
        PdfCopy pdfCopy = new PdfCopy(pdfDocument, out);
        pdfCopy.setMergeFields();
        pdfCopy.setFullCompression();
        pdfCopy.setCompressionLevel(PdfStream.BEST_COMPRESSION);
        pdfDocument.open();
        pdfCopy.addDocument(reader);
        pdfCopy.addDocument(reader2);
        pdfCopy.close();
        reader.close();
        reader2.close();
        out.close();

        reader = new PdfReader("./target/com/itextpdf/test/pdf/PdfCopyTest/out-noforms.pdf");
        Assert.assertNotNull(reader.getPageN(1));
        reader.close();
    }

    @Test
    public void testFullCompression2() throws DocumentException, IOException {
        String outfile = "./target/com/itextpdf/test/pdf/PdfCopyTest/out-forms.pdf";
        String first = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/subscribe.pdf";
        String second = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/filled_form_1.pdf";
        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();

        OutputStream out = new FileOutputStream(outfile);
        PdfReader reader = new PdfReader(first);
        PdfReader reader2 = new PdfReader(second);
        Document pdfDocument = new Document();
        PdfCopy pdfCopy = new PdfCopy(pdfDocument, out);
        pdfCopy.setMergeFields();
        pdfCopy.setFullCompression();
        pdfCopy.setCompressionLevel(PdfStream.BEST_COMPRESSION);
        pdfDocument.open();
        pdfCopy.addDocument(reader);
        pdfCopy.addDocument(reader2);
        pdfCopy.close();
        reader.close();
        reader2.close();
        out.close();

        reader = new PdfReader("./target/com/itextpdf/test/pdf/PdfCopyTest/out-forms.pdf");
        Assert.assertNotNull(reader.getPageN(1));
        reader.close();
    }

    @Test
    public void copyFields1Test() throws DocumentException, IOException, InterruptedException {
        Document pdfDocument = new Document();
        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();
        PdfCopy copier = new PdfCopy(pdfDocument, new FileOutputStream("./target/com/itextpdf/test/pdf/PdfCopyTest/copyFields.pdf"));
        copier.setMergeFields();

        pdfDocument.open();

        PdfReader readerMain = new PdfReader("./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/fieldsOn3-sPage.pdf");
        PdfReader secondSourceReader = new PdfReader("./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/fieldsOn2-sPage.pdf");
        PdfReader thirdReader = new PdfReader("./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/appearances1.pdf");

        copier.addDocument(readerMain);
        copier.copyDocumentFields(secondSourceReader);
        copier.addDocument(thirdReader);

        copier.close();
        readerMain.close();
        secondSourceReader.close();
        thirdReader.close();
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent("./target/com/itextpdf/test/pdf/PdfCopyTest/copyFields.pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_copyFields.pdf", "./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void copyFields2Test() throws DocumentException, IOException, InterruptedException {
        Document pdfDocument = new Document();
        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();
        PdfCopy copier = new PdfCopy(pdfDocument, new FileOutputStream("./target/com/itextpdf/test/pdf/PdfCopyTest/copyFields2.pdf"));
        copier.setMergeFields();
        pdfDocument.open();

        PdfReader reader = new PdfReader("./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/hello_with_comments.pdf");
        copier.addDocument(reader);
        copier.close();
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent("./target/com/itextpdf/test/pdf/PdfCopyTest/copyFields2.pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_copyFields2.pdf", "./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void copyFields3Test() throws DocumentException, IOException, InterruptedException {
        Document pdfDocument = new Document();
        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();
        PdfCopy copier = new PdfCopy(pdfDocument, new FileOutputStream("./target/com/itextpdf/test/pdf/PdfCopyTest/copyFields3.pdf"));
        copier.setMergeFields();
        pdfDocument.open();

        PdfReader reader = new PdfReader("./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/hello2_with_comments.pdf");
        copier.addDocument(reader);
        copier.close();
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent("./target/com/itextpdf/test/pdf/PdfCopyTest/copyFields3.pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_copyFields3.pdf", "./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void copyFields4Test() throws IOException, DocumentException, InterruptedException {
        String outputFolder = "./target/com/itextpdf/test/pdf/PdfCopyTest/";
        String outputFile = "copyFields4.pdf";
        String resources = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/";
        String inputFile1 = "link.pdf";


        Document doc = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, baos);
        com.itextpdf.text.Font font = new com.itextpdf.text.Font(BaseFont.createFont(
                resources + "fonts/georgia.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED), 9);
        doc.open();
        doc.add(new Phrase("text", font));
        doc.close();

        Document pdfDocument = new Document();
        new File(outputFolder).mkdirs();
        PdfCopy copier = new PdfCopy(pdfDocument, new FileOutputStream(outputFolder + outputFile));
        copier.setMergeFields();
        pdfDocument.open();

        PdfReader reader1 = new PdfReader(resources + inputFile1);
        PdfReader reader2 = new PdfReader(baos.toByteArray());

        copier.addDocument(reader1);
        copier.addDocument(reader2);
        copier.close();
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outputFolder + outputFile, resources + "cmp_" + outputFile, outputFolder, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test(timeout = 60000)
    public void largeFilePerformanceTest() throws IOException, DocumentException, InterruptedException {
        String target = "./target/com/itextpdf/test/pdf/PdfCopyTest/";
        String resources = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/";
        String output = "copyLargeFile.pdf";
        String cmp = "cmp_copyLargeFile.pdf";

        new File(target).mkdirs();

        long timeStart = System.nanoTime();

        PdfReader firstSourceReader = new PdfReader( resources +"frontpage.pdf");
        PdfReader secondSourceReader = new PdfReader(resources + "large_pdf.pdf");

        Document document = new Document();

        PdfCopy copy = new PdfCopy(document, new FileOutputStream(target + output));
        copy.setMergeFields();

        document.open();
        copy.addDocument(firstSourceReader);
        copy.addDocument(secondSourceReader);

        copy.close();
        document.close();

        System.out.println(((System.nanoTime() - timeStart) / 1000 / 1000));


        CompareTool cmpTool = new CompareTool();
        String errorMessage = cmpTool.compareByContent(target + output, resources + cmp, target, "diff");

        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void mergeNamedDestinationsTest() throws IOException, DocumentException, InterruptedException {
        String outputFolder = "./target/com/itextpdf/test/pdf/PdfCopyTest/";
        String outputFile = "namedDestinations.pdf";
        String resources = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/";

        // Create simple document
        ByteArrayOutputStream main = new ByteArrayOutputStream();
        Document doc = new Document(new Rectangle(612f,792f),54f,54f,36f,36f);
        PdfWriter pdfwrite = PdfWriter.getInstance(doc,main);
        doc.open();
        doc.add(new Paragraph("Testing Page"));
        doc.close();

        // Create TOC document
        ByteArrayOutputStream two = new ByteArrayOutputStream();
        Document doc2 = new Document(new Rectangle(612f,792f),54f,54f,36f,36f);
        PdfWriter pdfwrite2 = PdfWriter.getInstance(doc2, two);
        doc2.open();
        Chunk chn = new Chunk("<<-- Link To Testing Page -->>");
        chn.setRemoteGoto("DUMMY.PDF","page-num-1");
        doc2.add(new Paragraph(chn));
        doc2.close();

        // Merge documents
        ByteArrayOutputStream three = new ByteArrayOutputStream();
        PdfReader reader1 = new PdfReader(main.toByteArray());
        PdfReader reader2 = new PdfReader(two.toByteArray());
        Document doc3 = new Document();
        PdfCopy DocCopy = new PdfCopy(doc3,three);
        doc3.open();
        DocCopy.addPage(DocCopy.getImportedPage(reader2,1));
        DocCopy.addPage(DocCopy.getImportedPage(reader1,1));
        DocCopy.addNamedDestination("page-num-1",2,new PdfDestination(PdfDestination.FIT));
        doc3.close();

        // Fix references and write to file
        PdfReader finalReader = new PdfReader(three.toByteArray());
        finalReader.makeRemoteNamedDestinationsLocal();
        PdfStamper stamper = new PdfStamper(finalReader,new FileOutputStream(outputFolder + outputFile));
        stamper.close();


        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outputFolder + outputFile, resources + "cmp_" + outputFile, outputFolder, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void recursiveSmartMergeTest() throws Exception {

        String inputDocPath = "recursiveSmartMerge.pdf";
        String outputFolder = "./target/com/itextpdf/test/pdf/PdfCopyTest/";
        String resources = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/";
        byte[] part1 = ExtractPages(resources + inputDocPath, 1, 2);
        OutputStream os1 = new FileOutputStream(outputFolder + "part1_c.pdf");
        os1.write(part1);
        File outputPath1 = new File(outputFolder, "part1_c.pdf");


        byte[] part2 = ExtractPages(resources + inputDocPath, 3, 7);
        OutputStream os2 = new FileOutputStream(outputFolder + "part2_c.pdf");
        os2.write(part2);
        File outputPath2 = new File(outputFolder, "part2_c.pdf");
        byte[] merged = Merge(new File[] { outputPath1, outputPath2 });

        OutputStream os3 = new FileOutputStream(outputFolder + "outputRecursiveSmartMerge.pdf");
        os3.write(merged);

        File mergedPath = new File(outputFolder, "outputRecursiveSmartMerge.pdf");


        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outputFolder + "outputRecursiveSmartMerge.pdf", resources + "cmp_" + inputDocPath, outputFolder, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void copySignedDocuments() throws IOException, DocumentException {
        String file = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/hello_signed1.pdf";
        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();
        Document pdfDocument = new Document();
        PdfCopy copier = new PdfCopy(pdfDocument, new FileOutputStream("./target/com/itextpdf/test/pdf/PdfCopyTest/CopySignedDocuments.pdf"));
        pdfDocument.open();

        PdfReader reader1 = new PdfReader(file);
        copier.addPage(copier.getImportedPage(reader1, 1));
        copier.freeReader(reader1);

        reader1 = new PdfReader(file);
        copier.addPage(copier.getImportedPage(reader1, 1));
        copier.freeReader(reader1);

        pdfDocument.close();

        PdfReader reader = new PdfReader("./target/com/itextpdf/test/pdf/PdfCopyTest/CopySignedDocuments.pdf");
        PdfDictionary sig = (PdfDictionary)reader.getPdfObject(9);
        PdfDictionary sigRef = sig.getAsArray(PdfName.REFERENCE).getAsDict(0);
        Assert.assertTrue(PdfName.SIGREF.equals(sigRef.getAsName(PdfName.TYPE)));
        Assert.assertFalse(sigRef.contains(PdfName.DATA));
        sig = (PdfDictionary)reader.getPdfObject(21);
        sigRef = sig.getAsArray(PdfName.REFERENCE).getAsDict(0);
        Assert.assertTrue(PdfName.SIGREF.equals(sigRef.getAsName(PdfName.TYPE)));
        Assert.assertFalse(sigRef.contains(PdfName.DATA));
    }

    @Test
    public void smartCopySignedDocuments() throws IOException, DocumentException {
        String file = "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/hello_signed1.pdf";
        new File("./target/com/itextpdf/test/pdf/PdfCopyTest/").mkdirs();
        Document pdfDocument = new Document();
        PdfSmartCopy copier = new PdfSmartCopy(pdfDocument, new FileOutputStream("./target/com/itextpdf/test/pdf/PdfCopyTest/SmartCopySignedDocuments.pdf"));
        pdfDocument.open();

        PdfReader reader1 = new PdfReader(file);
        copier.addPage(copier.getImportedPage(reader1, 1));
        copier.freeReader(reader1);

        reader1 = new PdfReader(file);
        copier.addPage(copier.getImportedPage(reader1, 1));
        copier.freeReader(reader1);

        pdfDocument.close();

        PdfReader reader = new PdfReader("./target/com/itextpdf/test/pdf/PdfCopyTest/SmartCopySignedDocuments.pdf");
        PdfDictionary sig = (PdfDictionary)reader.getPdfObject(8);
        PdfDictionary sigRef = sig.getAsArray(PdfName.REFERENCE).getAsDict(0);
        Assert.assertTrue(PdfName.SIGREF.equals(sigRef.getAsName(PdfName.TYPE)));
        Assert.assertFalse(sigRef.contains(PdfName.DATA));
    }

    public static byte[] Merge(File[] documentPaths) throws IOException, DocumentException
    {
        byte[] mergedDocument;

        ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();

        Document document = new Document();
        PdfSmartCopy pdfSmartCopy = new PdfSmartCopy(document, memoryStream);
        document.open();

        for (File docPath : documentPaths)
        {
            PdfReader reader = new PdfReader(docPath.toString());
            try
            {
                reader.consolidateNamedDestinations();
                int numberOfPages = reader.getNumberOfPages();
                for (int page = 0; page < numberOfPages;)
                {
                    PdfImportedPage pdfImportedPage = pdfSmartCopy.getImportedPage(reader, ++page);
                    pdfSmartCopy.addPage(pdfImportedPage);
                }
            }
            finally
            {
                reader.close();
            }
        }

        document.close();
        mergedDocument = memoryStream.toByteArray();


        return mergedDocument;
    }

    public static byte[] ExtractPages(String pdfDocument, int startPage, int endPage) throws IOException, DocumentException {
        InputStream pdfDocumentStream = new FileInputStream(pdfDocument);

        PdfReader reader = new PdfReader(pdfDocumentStream);
        int numberOfPages = reader.getNumberOfPages();
        int endPageResolved = endPage > 0 ? endPage : numberOfPages;
        if (startPage > numberOfPages || endPageResolved > numberOfPages)
            System.err.printf("Error: page indices (%s, %s) out of bounds. Document has {2} pages.", startPage, endPageResolved, numberOfPages);

        byte[] outputDocument;
        ByteArrayOutputStream msOut = new ByteArrayOutputStream();

        Document doc = new Document();
        PdfCopy pdfCopyProvider = new PdfCopy(doc, msOut);
        doc.open();
        for (int i = startPage; i <= endPageResolved; i++)
        {
            PdfImportedPage page = pdfCopyProvider.getImportedPage(reader, i);
            pdfCopyProvider.addPage(page);
        }
        doc.close();
        reader.close();
        outputDocument = msOut.toByteArray();


        return outputDocument;

    }

    private static byte[] createImagePdf() throws Exception {

        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.setPageSize(PageSize.LETTER);

        document.open();

        BufferedImage awtImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = awtImg.createGraphics();
        g2d.setColor(Color.green);
        g2d.fillRect(10, 10, 80, 80);
        g2d.dispose();

        com.itextpdf.text.Image itextImg = com.itextpdf.text.Image.getInstance(awtImg, null);
        document.add(itextImg);

        document.close();

        final byte[] pdfBytes = byteStream.toByteArray();

        return pdfBytes;
    }
}
