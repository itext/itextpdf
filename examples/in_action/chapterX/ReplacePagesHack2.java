package in_action.chapterX;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/**
 * This example was written by Bruno Lowagie.
 * It is an extra example for the book 'iText in Action' by Manning Publications.
 * ISBN: 1932394796
 * http://www.1t3xt.com/docs/book.php
 * http://www.manning.com/lowagie/
 */

public class ReplacePagesHack2 {
	
	public static void main(String[] args) {
		try {
			// create test PDFs
			ReplacePagesHack.createPdf("results/in_action/chapterX/hack_1.pdf", 1);
			ReplacePagesHack.createPdf("results/in_action/chapterX/hack_2.pdf", 2);
			ReplacePagesHack.createPdf("results/in_action/chapterX/hack_3.pdf", 3);
			// parse test PDFs
			ReplacePagesHack2 parser = new ReplacePagesHack2();
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
	/** XObject stream cache */
	protected HashMap xobjectcache;
	
	/** Code to parse and manipulate one of the test PDFs we created. */
	public void parse(String file) throws IOException, DocumentException {
		xobjectcache = new HashMap();
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
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("results/in_action/chapterX/another_" + file));
		Integer p;
		String k;
		PdfContentByte cb;
		// we loop over the pages to insert backwarts (page count is important)
		for (Iterator i = pagesToInsert.keySet().iterator(); i.hasNext(); ) {
			p = (Integer)i.next();
			k = (String)pagesToInsert.get(p);
			pages = (ArrayList)cache.get(k);
			for (Iterator it = pages.iterator(); it.hasNext(); ) {
				page = (Page)it.next();
				stamper.insertPage(p.intValue(), page.getMediabox());
				page.addResources(stamper, p.intValue(), xobjectcache);
				cb = stamper.getOverContent(p.intValue());
				cb.setLiteral(page.getContent());
			}
		}
		stamper.close();
	}
}