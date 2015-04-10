package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultithreadedTtfTest {

    private static HashMap<Integer, Exception> exceptions;
    private int numberOfThreads = 100;

    @Test
    public void TtfTest() throws IOException, InterruptedException, DocumentException {
        new File("./target/com/itextpdf/test/pdf/MultithreadedTtfTest/").mkdirs();
        exceptions = new HashMap<Integer, Exception>();

        List<Runnable> runners = new ArrayList<Runnable>();
        for (int i = 1; i <= numberOfThreads; i++) {
            runners.add(new TtfTestRunner(i));
        }
        ExecutorService exec = Executors.newCachedThreadPool();
        for (Runnable runner : runners) {
            exec.submit(runner);
        }
        exec.shutdown();
        exec.awaitTermination(60, TimeUnit.SECONDS);

        Assert.assertEquals(0, exceptions.size());
    }

    synchronized static public void registerException(int thread, Exception exception) {
        exceptions.put(thread, exception);
    }

    static private final class TtfTestRunner implements Runnable {
        private int threadNumber;

        public TtfTestRunner(int threadNumber) throws IOException,DocumentException {
            this.threadNumber = threadNumber;
        }

        public void run() {
            try {
                PdfReader reader = new PdfReader(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/MultithreadedTtfTest/test.pdf"));
                PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("./target/com/itextpdf/test/pdf/MultithreadedTtfTest/out" + Integer.toString(threadNumber) + ".pdf"));
                PdfContentByte cb = stamper.getOverContent(1);
                cb.beginText();
                BaseFont font = BaseFont.createFont("./src/test/resources/com/itextpdf/text/pdf/MultithreadedTtfTest/FreeSans.ttf", threadNumber % 2 == 0 ? BaseFont.IDENTITY_H : BaseFont.WINANSI, BaseFont.EMBEDDED);
                cb.setFontAndSize(font, 12);
                cb.moveText(30, 600);
                cb.showText("`1234567890-=qwertyuiop[]asdfghjkl;'\\zxcvbnm,./");
                cb.endText();
                stamper.close();
                reader.close();
            } catch (Exception exc) {
                registerException(threadNumber, exc);
            }
        }
    }


}
