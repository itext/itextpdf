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
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.itextpdf.text.pdf.parser.ContentByteUtils;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FreeTextFlatteningTest {

    private final static String FOLDER = "./src/test/resources/com/itextpdf/text/pdf/FreeTextFlatteningTest/";
    private final static String TARGET = "./target/com/itextpdf/test/pdf/FreeTextFlattening/";

    @BeforeClass
    public static void setUp() {
        new File(TARGET).mkdirs();
    }

    @Test
    public void flattenCorrectlyTest() throws IOException, DocumentException, InterruptedException {
        String outputFile = TARGET + "freetext-flattened.pdf";

        flattenFreeText(FOLDER + "freetext.pdf", outputFile);
        checkAnnotationSize(outputFile, 0);

        String errorMessage = new CompareTool().compareByContent(outputFile, FOLDER + "flattened.pdf", TARGET, "diff");
        if ( errorMessage != null ) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void checkPageContentTest() throws IOException, DocumentException, InterruptedException {
        checkPageContent(FOLDER + "flattened.pdf");
    }

    @Test
    public void flattenWithoutDA() throws IOException, DocumentException {
        String outputFile = TARGET + "freetext-flattened-no-da.pdf";

        flattenFreeText(FOLDER + "freetext-no-da.pdf", outputFile);
        checkAnnotationSize(outputFile, 1);
    }

    @Test
    public void flattenAndCheckCourier() throws IOException, DocumentException, InterruptedException {
        String inputFile = FOLDER + "freetext-courier.pdf";
        String outputFile = TARGET + "freetext-courier-flattened.pdf";

        flattenFreeText(inputFile, outputFile);
        checkPageContent(outputFile);
    }

    @Test
    public void flattenAndCheckShortFontName() throws IOException, DocumentException, InterruptedException {
        String inputFile = FOLDER + "freetext-times-short.pdf";
        String outputFile = TARGET + "freetext-times-short-flattened.pdf";

        flattenFreeText(inputFile, outputFile);
        checkPageContent(outputFile);

        String errorMessage = new CompareTool().compareByContent(outputFile, FOLDER + "cmp_freetext-times-short-flattened.pdf", TARGET, "diff_short");
        if ( errorMessage != null ) {
            Assert.fail(errorMessage);
        }
    }

    private void checkAnnotationSize(String path, int expectedAnnotationsSize) throws IOException, DocumentException {
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(path);
            checkAnnotationSize(fin, expectedAnnotationsSize);
        } finally {
            if (fin != null) {
                fin.close();
            }
        }
    }

    private void checkAnnotationSize(InputStream inputStream, int expectedAnnotationsSize) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputStream);
        PdfDictionary pageDictionary = reader.getPageN(1);
        if ( pageDictionary.contains(PdfName.ANNOTS )) {
            PdfArray annotations = pageDictionary.getAsArray(PdfName.ANNOTS);
            Assert.assertTrue(annotations.size() == expectedAnnotationsSize);
        }
    }

    private void flattenFreeText(String inputPath, String outputPath) throws IOException, DocumentException {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        try {
            fin = new FileInputStream(inputPath);
            fout = new FileOutputStream(outputPath);
            flattenFreeText(fin, fout);
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }

    private void flattenFreeText(final InputStream inputStream, OutputStream outputStream) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputStream);
        PdfStamper stamper = new PdfStamper(reader, outputStream);

        stamper.setFormFlattening(true);
        stamper.setFreeTextFlattening(true);
        stamper.setAnnotationFlattening(true);

        stamper.close();
    }

    private void checkPageContent(String path) throws IOException, DocumentException {
        PdfReader pdfReader = new PdfReader(path);
        try {
            PdfDictionary pageDic = pdfReader.getPageN(1);

            RenderListener dummy = new RenderListener() {
                public void beginTextBlock() {
                }

                public void renderText(TextRenderInfo renderInfo) {
                }

                public void endTextBlock() {
                }

                public void renderImage(ImageRenderInfo renderInfo) {
                }
            };
            PdfContentStreamProcessor processor = new PdfContentStreamProcessor(dummy);

            PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
            processor.processContent(ContentByteUtils.getContentBytesForPage(pdfReader, 1), resourcesDic);
        } finally {
            pdfReader.close();
        }
    }
}