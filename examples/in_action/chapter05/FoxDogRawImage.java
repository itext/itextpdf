/* in_action/chapter05/FoxDogRawImage.java */

package in_action.chapter05;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class FoxDogRawImage {

	/**
	 * Generates a PDF file with Images.
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 4: example FoxDogImageTypes");
		System.out.println("-> Creates a PDF file with images");
		System.out.println("   of a brown fox and a lazy dog.");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> resources needed: foxdog.jpg");
		System.out.println("-> resulting PDF: fox_dog_raw_image.pdf");
		// step 1: creation of a document-object
		Document document = new Document();
		try {
			// step 2:
			// we create a writer
			PdfWriter.getInstance(
			// that listens to the document
					document,
					// and directs a PDF-stream to a file
					new FileOutputStream("results/in_action/chapter05/fox_dog_raw_image.pdf"));
			// step 3: we open the document
			document.open();
			// step 4: we add a paragraph to the document
			// creation a jpeg passed as an array of bytes to the Image
			RandomAccessFile rf = new RandomAccessFile(
					"resources/in_action/chapter05/foxdog.jpg", "r");
			int size = (int) rf.length();
			byte imagedata[] = new byte[size];
			rf.readFully(imagedata);
			rf.close();
			Image img1 = Image.getInstance(imagedata);
			System.out.println(img1.getClass().getName());
			document
					.add(new Paragraph(
							"Image constructed with a byte array containing the image data."));
			document.add(img1);

			// creation of an image of 100 x 100 pixels (x 3 bytes for the Red,
			// Green and Blue value)
			byte data[] = new byte[100 * 100 * 3];
			for (int k = 0; k < 100; ++k) {
				for (int j = 0; j < 300; j += 3) {
					data[k * 300 + j] = (byte) (255 * Math.sin(j * .5 * Math.PI
							/ 300));
					data[k * 300 + j + 1] = (byte) (256 - j * 256 / 300);
					data[k * 300 + j + 2] = (byte) (255 * Math.cos(k * .5
							* Math.PI / 100));
				}
			}
			Image img2 = Image.getInstance(100, 100, 3, 8, data);
			System.out.println(img2.getClass().getName());
			document.add(new Paragraph(
					"Image constructed with a byte array of raw image data."));
			document.add(img2);
		} catch (DocumentException de) {
			System.err.println(de.getMessage());
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}

		// step 5: we close the document
		document.close();
	}
}
