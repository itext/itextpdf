package in_action.chapterX;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class RotatePages {
	public static void main(String[] args) {
		try {
			PdfDictionary pageDict;
			int rot;
			createPdf("results/in_action/chapterX/not_rotated.pdf");
			PdfReader reader = new PdfReader("results/in_action/chapterX/not_rotated.pdf");
			int n = reader.getNumberOfPages();
			for (int i = 1; i <= n; i++) {
				rot = reader.getPageRotation(i);
				System.out.println("page " + i + ": " + rot + " degrees");
				pageDict = reader.getPageN(i);
				pageDict.put(PdfName.ROTATE, new PdfNumber(rot + 90));
			}
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("results/in_action/chapterX/rotated.pdf"));
			stamper.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private static void createPdf(String filename) {
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(filename));
			document.open();
			document.add(new Paragraph("Hello!"));
			document.newPage();
			document.add(new Paragraph("Hello!"));
			document.newPage();
			document.add(new Paragraph("Hello!"));
			document.newPage();
			document.add(new Paragraph("Hello!"));
			document.newPage();
			document.add(new Paragraph("Hello!"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		document.close();
	}
}