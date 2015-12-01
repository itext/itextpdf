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
            service.awaitTermination(1, TimeUnit.HOURS);
        }

    }

}
