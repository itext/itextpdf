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
