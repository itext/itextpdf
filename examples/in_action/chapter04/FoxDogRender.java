/* in_action/chapter04/FoxDogRender.java */

package in_action.chapter04;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogRender {

	/**
	 * Generates a PDF file using Chunks that are rendered differently.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogRender");
		System.out.println("-> Creates a PDF file with the text");
		System.out.println("   'Quick brown fox jumps over the lazy dog';");
		System.out.println("   the text is rendered in different ways.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resulting PDF: fox_dog_render.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter04/fox_dog_render.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			Font font = new Font(Font.COURIER, 20);
			Chunk chunk = new Chunk("Quick brown fox jumps over the lazy dog.",
					font);
			chunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL, 0f,
					new Color(0xFF, 0x00, 0x00));
			document.add(new Paragraph(chunk));
			chunk.setTextRenderMode(
					PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE, 0.3f,
					new Color(0xFF, 0x00, 0x00));
			document.add(new Paragraph(chunk));
			chunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE,
					0f, new Color(0x00, 0xFF, 0x00));
			document.add(new Paragraph(chunk));
			chunk.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_STROKE,
					0.3f, new Color(0x00, 0x00, 0xFF));
			document.add(new Paragraph(chunk));
			document.add(Chunk.NEWLINE);
			Chunk bold = new Chunk("This looks like Font.BOLD");
			bold.setTextRenderMode(PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE,
					0.5f, null);
			document.add(bold);

		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}