package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
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

public class ReplacePagesHack {
	
	public static void main(String[] args) {
		try {
			// create test PDFs
			createPdf("results/in_action/chapterX/hack_1.pdf", 1);
			createPdf("results/in_action/chapterX/hack_2.pdf", 2);
			createPdf("results/in_action/chapterX/hack_3.pdf", 3);
			// parse test PDFs
			ReplacePagesHack parser = new ReplacePagesHack();
			parser.parse("hack_1.pdf");
			parser.parse("hack_2.pdf");
			parser.parse("hack_3.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	/** The current key of the pages that need to be cached. */
	protected String key = null;
	/** In this map, pages are cached using a specific key. */
	protected HashMap cache = new HashMap();
	
	/** Code to parse and manipulate one of the test PDFs we created. */
	public void parse(String file) throws IOException, DocumentException {
		PdfReader reader = new PdfReader("results/in_action/chapterX/" + file);
		int n = reader.getNumberOfPages();
		int dropped_pages = 0;
		LinkedList pagesToKeep = new LinkedList();
		TreeMap pagesToInsert = new TreeMap(Collections.reverseOrder());
		Page page;
		ArrayList pages;
		for (int i = 0; i < n; ) {
			i++;
			page = new Page(reader, i, key);
			switch(page.getType()) {
			case Page.PLAIN_CONTENT:
				// we keep all the plain pages
				pagesToKeep.add(new Integer(i));
				// if the key is not null, the page needs to be cached
				if (key != null) {
					pages = (ArrayList) cache.get(key);
					if (pages == null) {
						pages = new ArrayList();
					}
					page.dropPages(dropped_pages);
					pages.add(0, page);
					cache.put(key, pages);
				}
				break;
			case Page.CACHE_START:
				// we set the current key, the page will not be kept
				key = page.getKey();
				dropped_pages++;
				break;
			case Page.CACHE_END:
				// we reset the current key, the page will not be kept
				key = null;
				dropped_pages++;
				break;
			case Page.REPLACE:
				// we'll have to insert other pages here
				pagesToInsert.put(new Integer(i - dropped_pages), page.getKey());
				dropped_pages++;
				key = null;
			}
		}
		// we select all the pages that have plain content
		reader.selectPages(pagesToKeep);
		// we create a new document with these pages
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("results/in_action/chapterX/altered_" + file));
		Integer p;
		String k;
		// we loop over the pages to insert backwarts (page count is important)
		for (Iterator i = pagesToInsert.keySet().iterator(); i.hasNext(); ) {
			p = (Integer)i.next();
			k = (String)pagesToInsert.get(p);
			pages = (ArrayList)cache.get(k);
			for (Iterator it = pages.iterator(); it.hasNext(); ) {
				page = (Page)it.next();
				stamper.insertPage(p.intValue(), page.getMediabox());
				stamper.getOverContent(p.intValue()).addTemplate(page.getImportedPage(stamper), 0, 0);
			}
		}
		stamper.close();
	}

	/** Ordinary code to create some test PDF documents. */
	public static void createPdf(String file, int type) throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(file));
		Image img1 = Image.getInstance("resources/in_action/chapter05/fox.gif");
		Image img2 = Image.getInstance("resources/in_action/chapter05/dog.gif");
		Image img3 = Image.getInstance("resources/in_action/chapter05/hitchcock.gif");
		document.open();
		for (int i = 0; i < 20; i++) {
			document.add(new Paragraph("This is a page in document '" + file + "' (" + (i + 1) + ")"));
			document.newPage();
			if (type == 1 && i == 4) {
				document.add(new Paragraph("cache_start=A"));
				document.newPage();
				i++;
				document.add(new Paragraph("This is a page in document '" + file + "' (" + (i + 1) + ")"));
				document.add(img1);
				document.add(img3);
				document.newPage();
			}
			if (type == 1 && i == 6) {
				document.add(new Paragraph("cache_end=A"));
				document.newPage();
			}
			if (type == 1 && i == 10) {
				document.add(new Paragraph("cache_start=B"));
				document.newPage();
				i++;
				document.add(new Paragraph("This is a page in document '" + file + "' (" + (i + 1) + ")"));
				document.add(img2);
				document.newPage();
			}
			if (type == 1 && i == 12) {
				document.add(new Paragraph("cache_end=B"));
				document.newPage();
			}
			if (type == 2 && i == 3) {
				document.add(new Paragraph("cache_start=C"));
				document.newPage();
				i++;
				document.add(new Paragraph("This is a page in document '" + file + "' (" + (i + 1) + ")"));
				document.add(img3);
				document.newPage();
			}
			if (type == 2 && i == 8) {
				document.add(new Paragraph("cache_end=C"));
				document.newPage();
			}
			if (type == 2 && i == 14) {
				document.add(new Paragraph("replace=A"));
				document.newPage();
			}
			if (type == 3 && i == 6) {
				document.add(new Paragraph("replace=A"));
				document.newPage();
			}
			if (type == 3 && i == 14) {
				document.add(new Paragraph("replace=B"));
				document.newPage();
			}
			if (type == 3 && i == 16) {
				document.add(new Paragraph("replace=C"));
				document.newPage();
			}
		}
		document.close();
	}
}