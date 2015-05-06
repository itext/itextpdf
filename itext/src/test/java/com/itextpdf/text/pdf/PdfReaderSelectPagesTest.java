package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

public class PdfReaderSelectPagesTest {
	byte[] data;
	File dataFile;
	
	@Before
	public void setUp() throws Exception {
		TestResourceUtils.purgeTempFiles();
		dataFile = TestResourceUtils.getResourceAsTempFile(this, "RomeoJuliet.pdf");
		data = TestResourceUtils.getResourceAsByteArray(this, "RomeoJuliet.pdf");
	}

	@After
	public void tearDown() throws Exception {
		TestResourceUtils.purgeTempFiles();
	}

	@Test
	public void test() throws Exception {
        PdfReader reader = new PdfReader(dataFile.getAbsolutePath());
        try{
	        reader.selectPages("4-8");
	        manipulateWithStamper(reader);
	        manipulateWithCopy(reader);
        } finally {
        	reader.close();
        }
	}


    /**
     * Creates a new PDF based on the one in the reader
     * @param reader a reader with a PDF file
     * @throws IOException
     * @throws DocumentException
     */
    private void manipulateWithStamper(PdfReader reader)
        throws IOException, DocumentException {
        PdfStamper stamper = new PdfStamper(reader, new ByteArrayOutputStream());
        stamper.close();
    }

    /**
     * Creates a new PDF based on the one in the reader
     * @param reader a reader with a PDF file
     * @throws IOException
     * @throws DocumentException
     */
    private void manipulateWithCopy(PdfReader reader)
        throws IOException, DocumentException {
        int n = reader.getNumberOfPages();
        Document document = new Document();
        PdfCopy copy = new PdfCopy(document, new ByteArrayOutputStream());
        document.open();
        for (int i = 0; i < n;) {
            copy.addPage(copy.getImportedPage(reader, ++i));
        }
        document.close();
    }

}
