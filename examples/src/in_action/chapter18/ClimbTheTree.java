/* in_action/chapter18/ClimbTheTree.java */

package in_action.chapter18;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PRTokeniser;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfLister;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfReader;

/**
 * This example was written by Bruno Lowagie. It is part of the book 'iText in
 * Action' by Manning Publications. 
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php 
 * http://www.manning.com/lowagie/
 */

public class ClimbTheTree {

	/**
	 * Generates a PDF file with the text 'Hello World'
	 * 
	 * @param args
	 *            no arguments needed here
	 */
	public static void main(String[] args) {
		System.out.println("Chapter 18: example Climb the tree");
		System.out.println("-> inspects a PDF file;");
		System.out.println("-> jars needed: iText.jar");
		System.out.println("-> files generated in /results subdirectory:");
		System.out.println("   read_me.pdf and objects.txt");

		in_action.chapter02.HelloWorldBookmarks.createPdf("results/in_action/chapter18/read_me.pdf");
		try {
			PdfReader reader = new PdfReader("results/in_action/chapter18/read_me.pdf");
			PrintStream list = new PrintStream(new FileOutputStream(
					"results/in_action/chapter18/objects.txt"));
			PdfLister lister = new PdfLister(new PrintStream(list));
			PdfDictionary trailer = reader.getTrailer();
			list.println("Climbing the Object Tree");
			list.println("========================");
			list.println("the trailer");
			list.println("-----------");
			lister.listDict(trailer);
			list.println("the reference to the information dictionary");
			list.println("-------------------------------------------");
			PdfIndirectReference info = (PdfIndirectReference) trailer
					.get(PdfName.INFO);
			lister.listAnyObject(info);
			list.println("the info dictionary");
			list.println("-------------------");
			lister.listAnyObject(reader.getPdfObject(info.getNumber()));
			list.println("the catalog dictionary");
			list.println("----------------------");
			PdfDictionary root = reader.getCatalog();
			lister.listDict(root);
			list.println("Outlines");
			list.println("========");
			PdfDictionary outlines = (PdfDictionary) reader
					.getPdfObject(((PdfIndirectReference) root
							.get(PdfName.OUTLINES)).getNumber());
			lister.listDict(outlines);
			list.println("--------------------------------");
			PdfObject first = reader
					.getPdfObject(((PdfIndirectReference) outlines
							.get(PdfName.FIRST)).getNumber());
			lister.listAnyObject(first);
			list.println("Pages");
			list.println("=====");
			PdfDictionary pages = (PdfDictionary) reader
					.getPdfObject(((PdfIndirectReference) root
							.get(PdfName.PAGES)).getNumber());
			lister.listDict(pages);
			list.println("kids");
			list.println("----");
			PdfArray kids = (PdfArray) pages.get(PdfName.KIDS);
			PdfIndirectReference kid_ref;
			PdfDictionary kid = null;
			for (Iterator i = kids.getArrayList().iterator(); i.hasNext();) {
				kid_ref = (PdfIndirectReference) i.next();
				kid = (PdfDictionary) reader.getPdfObject(kid_ref.getNumber());
				lister.listDict(kid);
				list.println("--------------------------------");
			}
			PdfIndirectReference content_ref = (PdfIndirectReference) kid
					.get(PdfName.CONTENTS);
			PRStream content = (PRStream) reader.getPdfObject(content_ref
					.getNumber());
			lister.listDict(content);
			list.println("--------------------------------");
			byte[] contentstream = PdfReader.getStreamBytes(content);
			list.println(new String(contentstream));
			list.println("--------------------------------");
			PRTokeniser tokenizer = new PRTokeniser(contentstream);
			while (tokenizer.nextToken()) {
				if (tokenizer.getTokenType() == PRTokeniser.TK_STRING) {
					list.println(tokenizer.getStringValue());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}