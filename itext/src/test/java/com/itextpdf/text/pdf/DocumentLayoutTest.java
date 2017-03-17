/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DocumentLayoutTest {
    private static String CMP_FOLDER ="./src/test/resources/com/itextpdf/text/pdf/DocumentLayoutTest/";
    private static String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/DocumentLayoutTest/";

    @BeforeClass
    public static void init() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void textLeadingTest() throws IOException, DocumentException, InterruptedException {
        String file = "phrases.pdf";

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + file));
        document.open();

        Phrase p1 = new Phrase("first, leading of 150");
        p1.setLeading(150);
        document.add(p1);
        document.add(Chunk.NEWLINE);

        Phrase p2 = new Phrase("second, leading of 500");
        p2.setLeading(500);
        document.add(p2);
        document.add(Chunk.NEWLINE);

        Phrase p3 = new Phrase();
        p3.add(new Chunk("third, leading of 20"));
        p3.setLeading(20);
        document.add(p3);

        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Test
    public void waitingImageTest() throws IOException, DocumentException, InterruptedException {
        String file = "waitingImage.pdf";

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + file));
        document.open();

        String longTextString = "asdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddas" +
                "asdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddas" +
                "asdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddas" +
                "asdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddas" +
                "asdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddas" +
                "asdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddas" +
                "asdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddasasdsaddsdadasddas";
        String extraLongTextString = longTextString + longTextString;
        document.add(new Paragraph(extraLongTextString));
        String imageFile = "Desert.jpg";
        document.add(Image.getInstance(CMP_FOLDER + imageFile));

        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}
