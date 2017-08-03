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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.Test;

public class FreeTextFlatteningTest {

    private final String FOLDER = "./src/test/resources/com/itextpdf/text/pdf/FreeTextFlatteningTest/";


    @Test
    public void flattenCorrectlyTest() throws IOException, DocumentException, InterruptedException {
        String target = "./target/com/itextpdf/test/pdf/FreeTextFlattening/";
        new File(target).mkdirs();
        String outputFile = target + "freetext-flattened.pdf";

        flattenFreeText(new FileInputStream(FOLDER + "freetext.pdf"), new FileOutputStream(outputFile));
        checkFlattenedPdf(new FileInputStream(outputFile), 0);

        String errorMessage = new CompareTool().compare(outputFile, FOLDER + "flattened.pdf", target, "diff");
        if ( errorMessage != null ) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void flattenWithoutDA() throws IOException, DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        flattenFreeText(new FileInputStream(FOLDER + "freetext-no-da.pdf"), baos);
        checkFlattenedPdf(new ByteArrayInputStream(baos.toByteArray()), 1);
    }

    private void checkFlattenedPdf(InputStream inputStream, int expectedAnnotationsSize) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(inputStream);
        PdfDictionary pageDictionary = reader.getPageN(1);
        if ( pageDictionary.contains(PdfName.ANNOTS )) {
            PdfArray annotations = pageDictionary.getAsArray(PdfName.ANNOTS);
            Assert.assertTrue(annotations.size() == expectedAnnotationsSize);
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
}