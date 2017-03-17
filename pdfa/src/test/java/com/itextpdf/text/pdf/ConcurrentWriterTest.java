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
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ConcurrentWriterTest {

    @Test
    public void test() throws InterruptedException {

        final PDFAConcurrencyIssue prog = new PDFAConcurrencyIssue();


        System.out.println("Creating only PDF/A documents");
        prog.runTest(true, false);


        System.out.println("Creating only normal PDF/A documents");
        prog.runTest(false, true);


        System.out.println("Creating PDF/A and normal documents");
        prog.runTest(true, true);

        System.out.println("Done");

    }

    public class PDFAConcurrencyIssue {


        public PDFAConcurrencyIssue() {
            FontFactory.register("./src/test/resources/com/itextpdf/text/pdf/FreeSans.ttf");
        }


        class Archive implements Runnable {
            public void run() {
                try {
                    createPdfA(PdfAConformanceLevel.PDF_A_1B);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }

            public void createPdfA(PdfAConformanceLevel level) throws IOException, DocumentException {
                final Document doc = new Document();

                OutputStream out = new ByteArrayOutputStream();
                final PdfAWriter writer = PdfAWriter.getInstance(doc, out, level);

                doc.open();

                final ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
                writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
                writer.createXmpMetadata();

                final Font font = FontFactory.getFont("FreeSans", BaseFont.IDENTITY_H, true, 12);
                doc.add(new Phrase("Hello, " + level + " world!", font));
                doc.add(new Phrase(UUID.randomUUID().toString(), font));

                doc.close();


            }
        }

        class Normal implements Runnable {

            public void run() {
                try {
                    final Document doc = new Document();
                    OutputStream out = new ByteArrayOutputStream();
                    final PdfWriter writer = PdfWriter.getInstance(doc, out);

                    doc.open();

                    final Font font = FontFactory.getFont("FreeSans", BaseFont.IDENTITY_H, true, 12);
                    doc.add(new Phrase("Hello, PDF world!", font));
                    doc.add(new Phrase(UUID.randomUUID().toString(), font));

                    doc.close();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        private void runTest(boolean createPDFA, boolean createNormal) throws InterruptedException {
            final ExecutorService service = Executors.newFixedThreadPool(8);
            Archive a = new Archive();
            Normal n = new Normal();
            for (int i = 0; i < 1000; i++) {
                if (createPDFA) {
                    service.submit(a);
                }
                if (createNormal) {
                    service.submit(n);
                }
            }

            service.shutdown();
            service.awaitTermination(3600, TimeUnit.SECONDS);
        }

    }

}
