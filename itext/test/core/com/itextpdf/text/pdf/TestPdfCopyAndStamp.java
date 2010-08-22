/*
 * Created on Oct 10, 2008
 * (c) 2008 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;

/**
 * @author kevin day, Trumpet, Inc.
 */
public class TestPdfCopyAndStamp {

    File base = new File(".");
    String[] in;
    Map<String, byte[]> pdfContent = new HashMap<String, byte[]>();
    String out;
    String stamp;
    String multiPageStamp;

    private void createReader(String name, String[] pageContents) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();

        for (int i = 0; i < pageContents.length; i++) {
            if (i != 0)
                document.newPage();

            String content = pageContents[i];
            Chunk contentChunk = new Chunk(content);
            document.add(contentChunk);
        }


        document.close();

        pdfContent.put(name, baos.toByteArray());
    }


    @Before
    public void setUp() throws Exception {
        in = new String[]{
                "content1.pdf",
                "content2.pdf",
                };

        stamp = "Stamp.PDF";
        multiPageStamp = "MultiStamp.PDF";
        out = "TestOut.pdf";

        createReader(in[0], new String[]{"content 1"});
        createReader(in[1], new String[]{"content 2"});

        createReader(stamp, new String[]{"          This is a stamp"});
        createReader(multiPageStamp, new String[]{"          This is a stamp - page 1", "          This is a stamp - page 2"});
    }

    @After
    public void tearDown() throws Exception {
    }

    public void mergeAndStampPdf(boolean resetStampEachPage, String[] in, String out, String stamp) throws Exception {
        Document document = new Document();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfCopy writer = new PdfSmartCopy(document, baos);

        document.open();

        int stampPageNum = 1;

        PdfReader stampReader = new PdfReader(pdfContent.get(stamp));
        for (String element : in) {
            // create a reader for the input document
            PdfReader documentReader = new PdfReader(pdfContent.get(element));

            for (int pageNum = 1; pageNum <= documentReader.getNumberOfPages(); pageNum++){

                // import a page from the main file
                PdfImportedPage mainPage = writer.getImportedPage(documentReader, pageNum);

                // make a stamp from the page and get under content...
                PdfCopy.PageStamp pageStamp = writer.createPageStamp(mainPage);

                // import a page from a file with the stamp...
                if (resetStampEachPage)
                    stampReader = new PdfReader(pdfContent.get(stamp));
                PdfImportedPage stampPage = writer.getImportedPage(stampReader, stampPageNum++);

                // add the stamp template, update stamp, and add the page
                pageStamp.getOverContent().addTemplate(stampPage, 0, 0);
                pageStamp.alterContents();
                writer.addPage(mainPage);

                if (stampPageNum > stampReader.getNumberOfPages())
                    stampPageNum = 1;
            }
        }

        writer.close();
        document.close();

        pdfContent.put(out, baos.toByteArray());
    }

    protected void testXObject(boolean shouldExist, int page, String xObjectName) throws Exception{
        PdfReader reader = null;
        RandomAccessFileOrArray raf = null;
        raf = new RandomAccessFileOrArray(pdfContent.get(out));
        reader = new PdfReader(raf, null);
        try{
            PdfDictionary dictionary = reader.getPageN(page);

            PdfDictionary resources = (PdfDictionary)dictionary.get(PdfName.RESOURCES);
            PdfDictionary xobject = (PdfDictionary)resources.get(PdfName.XOBJECT);
            PdfObject directXObject = xobject.getDirectObject(new PdfName(xObjectName));
            PdfObject indirectXObject = xobject.get(new PdfName(xObjectName));

            if (shouldExist){
                assertNotNull(indirectXObject);
                assertNotNull(directXObject);
            } else {
                assertNull(indirectXObject);
                assertNull(directXObject);
            }
        } finally {
            reader.close();
        }


    }

    @Test
    public void testWithReloadingStampReader() throws Exception{
        mergeAndStampPdf(true, in, out, stamp);

        testXObject(true, 1, "Xi0");
        testXObject(true, 2, "Xi1");

    }

    
    @Test
    public void testWithoutReloadingStampReader() throws Exception{
        mergeAndStampPdf(false, in, out, stamp);

        //openFile(out); // if you open the resultant PDF at this point and go to page 2, you will get a nice error message

        testXObject(true, 1, "Xi0");
        testXObject(true, 2, "Xi1"); // if we are able to optimize iText so it re-uses the same XObject for multiple imports of the same page from the same PdfReader, then switch this to false

    }

    
    @Test
    public void testMultiPageStampWithoutReloadingStampReader() throws Exception{
        mergeAndStampPdf(false, in, out, multiPageStamp);

        // openFile(out); // if you open the resultant PDF at this point and go to page 2, you will get a nice error message

        testXObject(true, 1, "Xi0");
        testXObject(true, 2, "Xi1");

    }

    @Test
    public void testMultiPageStampWithReloadingStampReader() throws Exception{
        mergeAndStampPdf(true, in, out, multiPageStamp);

        // openFile(out); // if you open the resultant PDF at this point and go to page 2, you will get a nice error message

        testXObject(true, 1, "Xi0");
        testXObject(true, 2, "Xi1");

    }


//    private void openFile(File f) throws IOException{
//        String[] params = new String[]{
//                "rundll32",
//                "url.dll,FileProtocolHandler",
//                "\"" + f.getCanonicalPath() + "\""
//        };
//        Runtime.getRuntime().exec(params);
//    }

}
