package in_action.chapterX;

import java.io.IOException;
import java.util.Iterator;

import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class ExtractPageLabels {

	public static void main(String[] args) {
		try {
			PdfReader reader = new PdfReader("resources/in_action/chapterX/page_labels.pdf");
			PdfDictionary dict = reader.getCatalog();
			PdfDictionary labels = (PdfDictionary)PdfReader.getPdfObject((PdfObject)dict.get(PdfName.PAGELABELS));
			PdfArray numbers = (PdfArray)PdfReader.getPdfObject((PdfObject)labels.get(PdfName.NUMS));
			PdfNumber pageIndex;
			PdfDictionary pageLabel;
			for (Iterator i = numbers.listIterator(); i.hasNext(); ) {
				pageIndex = (PdfNumber)i.next();
				pageLabel = (PdfDictionary) PdfReader.getPdfObject((PdfObject)i.next());
				System.out.println("Starting from page " + (pageIndex.intValue() + 1) + " the page labels are defined like this:");
				showDict(pageLabel);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void showDict(PdfDictionary dict) {
		PdfName key;
		PdfObject value;
		for (Iterator i = dict.getKeys().iterator(); i.hasNext(); ) {
			key = (PdfName)i.next();
			value = dict.get(key);
			System.out.println(key + ": " + value);
		}
	}
}
