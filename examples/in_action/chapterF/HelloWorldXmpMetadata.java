/* in_action/chapterG/HelloWorldXmpMetadata.java */

package in_action.chapterF;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.xml.xmp.DublinCoreSchema;
import com.lowagie.text.xml.xmp.PdfSchema;
import com.lowagie.text.xml.xmp.XmpArray;
import com.lowagie.text.xml.xmp.XmpSchema;
import com.lowagie.text.xml.xmp.XmpWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class HelloWorldXmpMetadata {

	/**
	 * Generates a PDF, RTF and HTML file with the text 'Hello World' with some
	 * metadata.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Appendix F: example HelloWorldXmpMetadata");
		System.out.println("-> Creates a PDF with the text 'Hello World'");
		System.out.println("   and adds XMP metadata.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   HelloWorldXmpMetadata.pdf");

		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			PdfWriter writer = PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapterF/HelloWorldXmpMetadata.pdf"));
			// step 3: we add metadata and open the document
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			XmpWriter xmp = new XmpWriter(os);
			XmpSchema dc = new DublinCoreSchema();
			XmpArray subject = new XmpArray(XmpArray.UNORDERED);
			subject.add("Hello World");
			subject.add("XMP & Metadata");
			subject.add("Metadata");
			dc.setProperty(DublinCoreSchema.SUBJECT, subject);
			xmp.addRdfDescription(dc);
			PdfSchema pdf = new PdfSchema();
			pdf.setProperty(PdfSchema.KEYWORDS, "Hello World, XMP, Metadata");
			pdf.setProperty(PdfSchema.VERSION, "1.4");
			xmp.addRdfDescription(pdf);
			xmp.close();
			writer.setXmpMetadata(os.toByteArray());
			document.open();
			// step 4: we add a paragraph to the document
			document.add(new Paragraph("Hello World"));
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}