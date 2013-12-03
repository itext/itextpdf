/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.io.WindowRandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;

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
        PdfReader stampReader = new PdfReader(pdfContent.get(stamp));
        List<PdfReader> readersToClose = new ArrayList<PdfReader>();
        readersToClose.add(stampReader);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try{
	    	Document document = new Document();
	
	        PdfCopy writer = new PdfSmartCopy(document, baos);
	        try{
	
		        document.open();
		
		        int stampPageNum = 1;
	
		        for (String element : in) {
		            // create a reader for the input document
		            PdfReader documentReader = new PdfReader(
		            		new RandomAccessFileOrArray(
           						new RandomAccessSourceFactory().createSource(pdfContent.get(element))
		            		)
		            	, null);
		
		            for (int pageNum = 1; pageNum <= documentReader.getNumberOfPages(); pageNum++){
		
		                // import a page from the main file
		                PdfImportedPage mainPage = writer.getImportedPage(documentReader, pageNum);
		
		                // make a stamp from the page and get under content...
		                PdfCopy.PageStamp pageStamp = writer.createPageStamp(mainPage);
		
		                // import a page from a file with the stamp...
		                if (resetStampEachPage){
		                    stampReader = new PdfReader(pdfContent.get(stamp));
		                    readersToClose.add(stampReader);
		                }
		                PdfImportedPage stampPage = writer.getImportedPage(stampReader, stampPageNum++);
		
		                // add the stamp template, update stamp, and add the page
		                pageStamp.getOverContent().addTemplate(stampPage, 0, 0);
		                pageStamp.alterContents();
		                writer.addPage(mainPage);
		
		                if (stampPageNum > stampReader.getNumberOfPages())
		                    stampPageNum = 1;
		            }
		        }
	        } finally {
		        writer.close();
		        document.close();
	        }
        } finally {
        	for (PdfReader stampReaderToClose : readersToClose) {
        		stampReaderToClose.close();	
			}
        }
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
