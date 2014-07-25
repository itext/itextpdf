/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @author kevin
 */
public class PdfCopyTest {

    @Before
    public void setUp() throws Exception {
        TestResourceUtils.purgeTempFiles();
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
        try{
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
        for (String f : new String[] {f1, f2, f3, f4}) {
            PdfReader r = new PdfReader(f);
            copy.addDocument(r);
        }
        copy.close();
        CompareTool compareTool = new CompareTool("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances.pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_appearances.pdf");
        String errorMessage = compareTool.compare("./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
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
        for (String f : new String[] {f1, f2, f3, f4}) {
            PdfReader r = new PdfReader(f);
            copy.addDocument(r);
        }
        copy.close();
        CompareTool compareTool = new CompareTool("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances(needAppearancesFalse).pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_appearances(needAppearancesFalse).pdf");
        String errorMessage = compareTool.compare("./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
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
        for (String f : new String[] {f1, f2, f3, f4}) {
            PdfReader r = new PdfReader(f);
            copy.addDocument(r);
        }
        copy.close();
        CompareTool compareTool = new CompareTool("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances(needAppearancesFalseWithStreams).pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_appearances(needAppearancesFalseWithStreams).pdf");
        String errorMessage = compareTool.compare("./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
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
        for (String f : new String[] {f1, f2, f3, f4}) {
            PdfReader r = new PdfReader(f);
            copy.addDocument(r);
        }
        copy.close();
        CompareTool compareTool = new CompareTool("./target/com/itextpdf/test/pdf/PdfCopyTest/appearances(mixed).pdf", "./src/test/resources/com/itextpdf/text/pdf/PdfCopyTest/cmp_appearances(mixed).pdf");
        String errorMessage = compareTool.compare("./target/com/itextpdf/test/pdf/PdfCopyTest/", "diff");
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
