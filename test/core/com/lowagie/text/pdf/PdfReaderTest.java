package com.lowagie.text.pdf;

import static org.junit.Assert.assertTrue;

import com.lowagie.text.*;
import java.io.ByteArrayOutputStream;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

public class PdfReaderTest {

    @Test
    public void testPartialReadOpenFile() throws Exception {
	/* commit 3265 incorrectly closed the input stream, make sure
	 * the constructor contract is kept, i.e. file is still open
	 */
        RandomAccessFileOrArray f = new RandomAccessFileOrArray("RomeoJuliet.pdf");
        PdfReader r = new PdfReader(f, null);

        assertTrue("kept open", f.isOpen());
    }

    @Ignore("validity of test needs to be resolved")
    @Test
    public void testGetLink() throws Exception {
	PdfReader currentReader = new PdfReader("getLinkTest1.pdf");
	Document document = new Document(PageSize.A4, 0, 0, 0, 0);
	PdfWriter writer = PdfWriter.getInstance(document, new
		ByteArrayOutputStream());
	document.open();
	document.newPage();
	List links = currentReader.getLinks(1);
	PdfAnnotation.PdfImportedLink link =
		(PdfAnnotation.PdfImportedLink) links.get(0);
	writer.addAnnotation(link.createAnnotation(writer));
	document.close();
    }

    @Test
    public void testGetLink2() throws Exception {
        String filename = "getLinkTest2.pdf";
	PdfReader rdr = new PdfReader(new
		RandomAccessFileOrArray(filename), new byte[0]);
	// this one works: PdfReader rdr = new PdfReader(filename);
	rdr.consolidateNamedDestinations(); // does not help
	rdr.getLinks(1);
    }

}
