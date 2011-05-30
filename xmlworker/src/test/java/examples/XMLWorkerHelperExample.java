/**
 *
 */
package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * @author Balder Van Camp
 *
 */
public class XMLWorkerHelperExample extends Setup {

	/**
	 * Create a PDF from a document.<br />
	 * With the XMLWorkerHelper this is done in no time. Create a Document and a
	 * PdfWriter. Don't forget to open the document and call
	 * <code>XMLWorkerHelper.getInstance().parseXHtml()</code>. This test takes
	 * html from <code>columbus.html</code>. This document contains &lt;img&gt;
	 * tags with relative src attributes. You'll see that the images are not
	 * added, unless they are absolute url's or file paths.
	 *
	 * @throws DocumentException
	 * @throws IOException
	 */
	@Test
	public void defaultSetup() throws DocumentException, IOException {
		Document doc = new Document(PageSize.A4);
		PdfWriter instance = PdfWriter.getInstance(doc, new FileOutputStream(new File(
				"./target/test-classes/examples/columbus.pdf")));
		doc.open();
		XMLWorkerHelper.getInstance().parseXHtml(instance, doc,
				XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"));
		doc.close();
	}
}
