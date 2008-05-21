package in_action.chapterX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class Page {

	public static final int UNDEFINED = -1;
	public static final int PLAIN_CONTENT = 0;
	public static final int CACHE_START = 1;
	public static final int CACHE_END = 2;
	public static final int REPLACE = 3;
	
	/** The reader object that holds the page. */
	protected PdfReader reader;
	/** The number of the page as it is stored in the reader object. */
	protected int number;
	
	/** The page type (plain content, cache start, cache end or replace) */
	protected int type = UNDEFINED;
	/** A key referring to the cache. */
	protected String key;
	
	/**
	 * Creates a page object.
	 * @param reader	the reader object that holds the page
	 * @param number	the page number
	 * @param k			a key referring to the cache
	 */
	public Page(PdfReader reader, int number, String k) {
		this.reader = reader;
		this.number = number;
		try {
			defineTypeAndKey(reader.getPageContent(number), k);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adjusts the page number (in case pages were dropped from the reader).
	 * @param dropped_pages the number of pages that were dropped so far
	 */
	public void dropPages(int dropped_pages) {
		number -= dropped_pages;
	}
	
	/**
	 * Returns a PdfTemplate with the current page content and resources.
	 */
	public PdfImportedPage getImportedPage(PdfStamper stamper) {
		return stamper.getImportedPage(reader, number);
	}
	
	/**
	 * Returns the content stream of a page (PDF Syntax).
	 * @throws IOException 
	 */
	public String getContent() throws IOException {
		return new String(reader.getPageContent(number));
	}
	
	/**
	 * Adds the resources of this page to another page in another PdfStamper object.
	 * It is assumed that the fonts of this page are known and present in the PdfStamper object.
	 * XObjects are added to the PdfStamper, unless an identical stream is already present in the xobjectcache
	 * @param	stamper			a stamper object to which you want to add resources
	 * @param	page			the page number of the page in the new document
	 * @param	xobjectcache	a cache with the raw bytes of the xobjects
	 * @throws IOException 
	 */
	public void addResources(PdfStamper stamper, int page, HashMap xobjectcache) throws IOException {
		// this is the new stuff
		PdfReader new_reader = stamper.getReader();
		PdfDictionary new_page_dict = new_reader.getPageNRelease(page);
		PdfDictionary new_resources = (PdfDictionary)new_page_dict.get(PdfName.RESOURCES);
		// this is the old stuff
		PdfDictionary page_dict = reader.getPageN(number);
		PdfDictionary resources = (PdfDictionary)page_dict.get(PdfName.RESOURCES);
		// we assume that the fonts are present (potentially dangerous)
		new_resources.put(PdfName.FONT, resources.get(PdfName.FONT));
		// we do a more thorough job with XObjects
		PdfDictionary xobjects = (PdfDictionary)resources.get(PdfName.XOBJECT);
		PdfName key;
		PRStream stream;
		PRIndirectReference ref;
		String cachekey;
		if (xobjects != null) {
			PdfDictionary new_xobjects = new PdfDictionary();
			for (Iterator i = xobjects.getKeys().iterator(); i.hasNext(); ) {
				key = (PdfName)i.next();
				stream = (PRStream)PdfReader.getPdfObject(xobjects.get(key));
				cachekey = new String(PdfReader.getStreamBytesRaw(stream));
				ref = (PRIndirectReference)xobjectcache.get(cachekey);
				if (ref == null) {
					ref = new_reader.addPdfObject(stream);
					xobjectcache.put(cachekey, ref);
				}
				new_xobjects.put(key, ref);
			}
			new_resources.put(PdfName.XOBJECT, new_xobjects);
		}
	}
	
	/**
	 * Returns the key that refers to the cache.
	 */
	public String getKey() {
		return key;
	}
	/**
	 * Returns the type of page.
	 * Possible values are
	 * <ul>
	 * 	<li><b>Page.UNDEFINED:</b> this situation shouldn't occur
	 * 	<li><b>Page.PLAIN_CONTENT:</b> an ordinary page with content. 
	 * 	<li><b>Page.CACHE_START:</b> a page that indicates we should start caching the next page. 
	 * 	<li><b>Page.CACHE_END:</b> a page that indicates that the previous page is the last one that needs caching. 
	 * 	<li><b>Page.REPLACE:</b> a page that needs to be replaced with one or more cached pages.
	 * </ul>
	 */
	public int getType() {
		return type;
	}
	/**
	 * Returns the page size.
	 */
	public Rectangle getMediabox() {
		return reader.getPageSizeWithRotation(number);
	}
	
	/**
	 * Partially implemented code that inspects the
	 * content stream of a page to decide which type
	 * of page we are currently dealing with.
	 * @param b	the bytes from the page content stream
	 * @param k a key referring to the cache 
	 */
	protected void defineTypeAndKey(byte[] b, String k) {
		String s = new String(b);
		int pos;
		pos = s.indexOf("cache_start=");
		if (pos > 0) {
			type = CACHE_START;
			key = s.substring(pos + 12, pos + 13);
			return;
		}
		pos = s.indexOf("cache_end=");
		if (pos > 0) {
			type = CACHE_END;
			key = s.substring(pos + 10, pos + 11);
			return;
		}
		pos = s.indexOf("replace=");
		if (pos > 0) {
			type = REPLACE;
			key = s.substring(pos + 8, pos + 9);
			return;
		}
		type = PLAIN_CONTENT;
		key = k;
	}
}