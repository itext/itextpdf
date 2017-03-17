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
import com.itextpdf.text.Rectangle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class NamedDestinationsTest {

    private String srcFolder = "./src/test/resources/com/itextpdf/text/pdf/NamedDestinationsTest/";
    private String outFolder = "./target/com/itextpdf/test/pdf/NamedDestinationsTest/";

    @Before
    public void setUp() throws Exception {
        new File(outFolder).mkdirs();
    }

    @Test
    public void nameDestinationsTest01() throws IOException, DocumentException, InterruptedException {

        String outFile = outFolder+"namedDestinations01.pdf";

        FileOutputStream file = new FileOutputStream(outFile);
        PdfReader reader = new PdfReader(new FileInputStream(srcFolder+"documentWithoutDestinations.pdf"));

        PdfStamper stamper = new PdfStamper(reader, file);

        stamper.addNamedDestination("Destination2", 2, new PdfDestination(1, 100, 100, 10));
        stamper.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outFile, srcFolder + "cmp_namedDestinations01.pdf", outFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void nameDestinationsTest02() throws IOException, DocumentException, InterruptedException {

        String outFile = outFolder+"namedDestinations02.pdf";

        FileOutputStream file = new FileOutputStream(outFile);
        PdfReader reader = new PdfReader(new FileInputStream(srcFolder+"documentWithDestinations.pdf"));

        PdfStamper stamper = new PdfStamper(reader, file);

        stamper.addNamedDestination("Destination2", 2, new PdfDestination(1, 100, 100, 10));
        stamper.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(outFile, srcFolder + "cmp_namedDestinations02.pdf", outFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void addNavigationTest() throws IOException, DocumentException, InterruptedException {
        String src = srcFolder + "primes.pdf";
        String dest = outFolder + "primes_links.pdf";
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfDestination d = new PdfDestination(PdfDestination.FIT);
        Rectangle rect = new Rectangle(0, 806, 595, 842);
        PdfAnnotation a10 = PdfAnnotation.createLink(stamper.getWriter(), rect, PdfAnnotation.HIGHLIGHT_INVERT, 2, d);
        stamper.addAnnotation(a10, 1);
        PdfAnnotation a1 = PdfAnnotation.createLink(stamper.getWriter(), rect, PdfAnnotation.HIGHLIGHT_PUSH, 1, d);
        stamper.addAnnotation(a1, 2);
        stamper.close();

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(dest, srcFolder + "cmp_primes_links.pdf", outFolder, "diff_");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}
