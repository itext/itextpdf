/* in_action/chapter15/AnnotatedChunks.java */

package in_action.chapter15;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfAnnotation;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class AnnotatedChunks {

	/**
	 * Creates a PDF file with annotations.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 15: example Annotations");
		System.out.println("-> Creates a PDF file with annotations;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   annotated_chunks.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {

			// step 2:
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream("results/in_action/chapter15/annotated_chunks.pdf"));
			// step 3:
			document.open();
			// step 4:
			PdfAnnotation text = PdfAnnotation.createText(writer,
					new Rectangle(200f, 250f, 300f, 350f), "Fox",
					"The fox is quick", true, "Comment");
			PdfAnnotation attachment = PdfAnnotation.createFileAttachment(
					writer, new Rectangle(100f, 650f, 150f, 700f),
					"Image of the fox and the dog", getBytesFromFile(new File(
							"resources/in_action/chapter05/foxdog.jpg")), null,
					"foxdog.jpg");
			PdfAnnotation javascript = new PdfAnnotation(writer, 200f, 550f,
					300f, 650f, PdfAction.javaScript(
							"app.alert('Wake up dog!');\r", writer));
			Chunk fox = new Chunk("quick brown fox").setAnnotation(text);
			Chunk jumps = new Chunk(" jumps over ").setAnnotation(attachment);
			Chunk dog = new Chunk("the lazy dog").setAnnotation(javascript);
			document.add(fox);
			document.add(jumps);
			document.add(dog);
		} catch (Exception de) {
			de.printStackTrace();
		}

		// step 5: we close the document
		document.close();
	}

	private static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		byte[] bytes = new byte[(int) length];
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		is.close();
		return bytes;
	}
}