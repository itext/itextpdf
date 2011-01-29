/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.html.simpleparser;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocListener;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementTags;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.Image;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.TextElementArray;
import com.itextpdf.text.html.Markup;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;

public class HTMLWorker implements SimpleXMLDocHandler, DocListener {
	
	/**
	 * Key used to store the image provider in the providers map.
	 * @since 5.0.6
	 */
	public static final String IMG_PROVIDER = "img_provider";
	
	/**
	 * Key used to store the image processor in the providers map.
	 * @since 5.0.6
	 */
	public static final String IMG_PROCESSOR = "img_interface";
	
	/**
	 * Key used to store the image store in the providers map.
	 * @since 5.0.6
	 */
	public static final String IMG_STORE = "img_static";

	/**
	 * Key used to store the image baseurl provider in the providers map.
	 * @since 5.0.6
	 */
	public static final String IMG_BASEURL = "img_baseurl";
	
	/**
	 * Key used to store the font provider in the providers map.
	 * @since 5.0.6
	 */
	public static final String FONT_PROVIDER = "font_factory";
	
	/**
	 * Key used to store the link provider in the providers map.
	 * @since 5.0.6
	 */
	public static final String LINK_PROVIDER = "alink_interface";

	/**
	 * Map containing providers such as a FontProvider or ImageProvider.
	 * @since 5.0.6 (renamed from interfaceProps)
	 */
	private Map<String, Object> providers = new HashMap<String, Object>();
	
	/**
	 * Factory that is able to create iText Element objects.
	 */
	private ElementFactory factory = new ElementFactory();
	
	/** Object in which CSS information is stored. */
	private StyleSheet style = new StyleSheet();
	
	/** The resulting list of elements. */
	protected List<Element> objectList;

	/**
	 * DocListener that will listen to the Elements
	 * produced by parsing the HTML.
	 * This can be a com.lowagie.text.Document adding
	 * the elements to a Document directly, or an
	 * HTMLWorker instance strong the objects in a List
	 */
	protected DocListener document;

	/**
	 * Keeps the content of the current paragraph
	 * @since iText 5.0.6 (private => protected)
	 */
	protected Paragraph currentParagraph;

	/**
	 * Stack with the Elements that already have been processed.
	 * @since iText 5.0.6 (private => protected)
	 */
	protected Stack<Element> stack = new Stack<Element>();

	/** The current hierarchy chain of tags. */
	private ChainedProperties chain = new ChainedProperties();

	/** Stack to keep track of table tags. */
	private Stack<boolean[]> tableState = new Stack<boolean[]>();
	
	/** Boolean to keep track of TR tags. */
	private boolean pendingTR = false;

	/** Boolean to keep track of TD and TH tags */
	private boolean pendingTD = false;

	/** Boolean to keep track of LI tags */
	private boolean pendingLI = false;

	/** Boolean to keep track of PRE tags */
	private boolean isPRE = false;

	/**
	 * Indicates if text needs to be skipped.
	 * @since iText 5.0.6 (private => protected)
	 */
	protected boolean skipText = false;


	protected Map<String, TagProcessor> tags = new SupportedTags();
	
	/**
	 * Creates a new instance of HTMLWorker
	 * @param document A class that implements <CODE>DocListener</CODE>
	 */
	public HTMLWorker(DocListener document) {
		this(document, null, null);
	}
	
	/**
	 * Creates a new instance of HTMLWorker
	 * @param document	A class that implements <CODE>DocListener</CODE>
	 * @param tags		A map containing the supported tags
	 * @param style		A StyleSheet
	 * @since 5.0.6
	 */
	public HTMLWorker(DocListener document, Map<String, TagProcessor> tags, StyleSheet style) {
		this.document = document;
		setSupportedTags(tags);
		setStyleSheet(style);
	}

	/**
	 * Parses content read from a java.io.Reader object.
	 * @param reader	the content
	 * @throws IOException
	 */
	public void parse(Reader reader) throws IOException {
		SimpleXMLParser.parse(this, null, reader, true);
	}
	
	public void setSupportedTags(Map<String, TagProcessor> tags) {
		if (tags == null)
			tags = new SupportedTags();
		this.tags = tags;
	}
	
	/**
	 * Setter for the providers.
	 * If a FontProvider is added, the ElementFactory is updated.
	 * @param providers a Map with different providers
	 * @since 5.0.6
	 */
	public void setProviders(Map<String, Object> providers) {
		if (providers == null)
			return;
		this.providers = providers;
		FontProvider ff = null;
		if (providers != null)
			ff = (FontProvider) providers.get(FONT_PROVIDER);
		if (ff != null)
			factory.setFontProvider(ff);
	}

	/**
	 * Getter for the providers
	 * @return a Map containing providers
	 * @since 5.0.6 (renamed)
	 */
	public Map<String, Object> getProviders() {
		return providers;
	}

	/**
	 * Setter for the StyleSheet
	 * @param style the StyleSheet
	 */
	public void setStyleSheet(StyleSheet style) {
		if (style == null)
			style = new StyleSheet();
		this.style = style;
	}

	/**
	 * Getter for the StyleSheet
	 * @return the StyleSheet
	 */
	public StyleSheet getStyleSheet() {
		return style;
	}
	


	/**
	 * @see com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler#startDocument()
	 */
	public void startDocument() {
		HashMap<String, String> attrs = new HashMap<String, String>();
		style.applyStyle("body", attrs);
		chain.addToChain("body", attrs);
	}

    /**
     * @see com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler#startElement(java.lang.String, java.util.HashMap)
     */
    public void startElement(String tag, HashMap<String, String> attrs) {
		TagProcessor htmlTag = tags.get(tag);
		if (htmlTag == null) {
			return;
		}
		// apply the styles to attrs
		style.applyStyle(tag, attrs);
		// deal with the style attribute
		resolveStyleAttribute(attrs, chain);
		// process the tag
		try {
			htmlTag.startElement(this, tag, attrs);
		} catch (DocumentException e) {
			throw new ExceptionConverter(e);
		} catch (IOException e) {
			throw new ExceptionConverter(e);
		}
	}

	/**
	 * @see com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler#text(java.lang.String)
	 */
	public void text(String content) {
		if (skipText)
			return;
		if (isPRE) {
			if (currentParagraph == null) {
				currentParagraph = factory.createParagraph(chain);
			}
			Chunk chunk = factory.createChunk(content, chain);
			currentParagraph.add(chunk);
			return;
		}
		// newlines and carriage returns are ignored
		if (content.trim().length() == 0 && content.indexOf(' ') < 0) {
			return;
		}
		// multiple spaces are reduced to one,
		// newlines are treated as spaces,
		// tabs, carriage returns are ignored.
		StringBuffer buf = new StringBuffer();
		int len = content.length();
		char character;
		boolean newline = false;
		for (int i = 0; i < len; i++) {
			switch (character = content.charAt(i)) {
			case ' ':
				if (!newline) {
					buf.append(character);
				}
				break;
			case '\n':
				if (i > 0) {
					newline = true;
					buf.append(' ');
				}
				break;
			case '\r':
				break;
			case '\t':
				break;
			default:
				newline = false;
				buf.append(character);
			}
		}
		if (currentParagraph == null) {
			currentParagraph = factory.createParagraph(chain);
		}
		Chunk chunk = factory.createChunk(buf.toString(), chain);
		currentParagraph.add(chunk);
	}

	/**
	 * @see com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler#endElement(java.lang.String)
	 */
	public void endElement(String tag) {
		TagProcessor htmlTag = tags.get(tag);
		if (htmlTag == null) {
			return;
		}
		// process the tag
		try {
			htmlTag.endElement(this, tag);
		} catch (DocumentException e) {
			throw new ExceptionConverter(e);
		}
	}

	/**
	 * @see com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler#endDocument()
	 */
	public void endDocument() {
		try {
			// flush the stack
			for (int k = 0; k < stack.size(); ++k)
				document.add(stack.elementAt(k));
			// add current paragraph
			if (currentParagraph != null)
				document.add(currentParagraph);
			currentParagraph = null;
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

	public boolean isStackEmpty() {
		return stack.empty();
	}
	
	public boolean updateParagraph() {
		if (currentParagraph == null) {
			currentParagraph = new Paragraph();
			return true;
		}
		return false;
	}
	
	public void addLineSeparator(Map<String, String> attrs) {
		// Attempting to duplicate the behavior seen on Firefox with
		// http://www.w3schools.com/tags/tryit.asp?filename=tryhtml_hr_test
		// where an initial break is only inserted when the preceding element doesn't
		// end with a break, but a trailing break is always inserted.
		if (!updateParagraph()) {
			int numChunks = currentParagraph.getChunks().size();
			if (numChunks > 0 &&
				!currentParagraph.getChunks().get(numChunks - 1).getContent().endsWith("\n")) {
				newLine();
			}
		}
		currentParagraph.add(factory.createLineSeparator(attrs, currentParagraph.getLeading()/2));
		newLine();
	}
	
	/**
	 */
	public void pushParagraph() {
		updateParagraph();
		stack.push(currentParagraph);
		currentParagraph = new Paragraph();
	}
	
	public void pushToStack(Element element) {
		stack.push(element);
	}
	
	public void mergeParagraph() {
		Paragraph tmp = (Paragraph) stack.pop();
		Phrase tmp2 = new Phrase();
		tmp2.add(currentParagraph);
		tmp.add(tmp2);
		currentParagraph = tmp;
	}
	
	public Element createList(String tag) {
		return factory.createList(tag, chain);
	}
	public Element createListItem() {
		return factory.createListItem(chain);
	}
	
	public void createImage(String tag, Map<String, String> attrs) throws DocumentException, IOException {
		String src = attrs.get(ElementTags.SRC);
		if (src == null)
			return;
		 updateChain(tag, attrs);
		Image img = factory.createImage(
				src, attrs, chain, document,
				(ImageProvider)providers.get(IMG_PROVIDER),
				(ImageStore)providers.get(IMG_STORE),
				(String)providers.get(IMG_BASEURL));
		ImageProcessor processor = (ImageProcessor)getProvider(HTMLWorker.IMG_PROCESSOR);
		boolean skip = false;
		if (processor != null)
			skip = processor.process(img, attrs, chain, document);
		if (!skip) {
			String align = attrs.get("align");
			if (align != null) {
				addParagraph();
				int ralign = Image.MIDDLE;
				if (align.equalsIgnoreCase("left"))
					ralign = Image.LEFT;
				else if (align.equalsIgnoreCase("right"))
					ralign = Image.RIGHT;
				img.setAlignment(ralign);	
				document.add(img);
				chain.removeChain(tag);
			} else {
				chain.removeChain(tag);
				if (currentParagraph == null) {
					currentParagraph = factory.createParagraph(chain);
				}
				currentParagraph.add(new Chunk(img, 0, 0, true));
			}
		}
	}
	
	public void newLine() {
		currentParagraph.add(factory.createChunk("\n", chain));
	}
	
	public void addList() throws DocumentException {
		if (isStackEmpty())
			return;
		Element obj = stack.pop();
		if (!(obj instanceof com.itextpdf.text.List)) {
			stack.push(obj);
			return;
		}
		if (stack.empty())
			document.add(obj);
		else
			((TextElementArray) stack.peek()).add(obj);
	}
	
	public void addListItem() throws DocumentException {
		if (stack.empty())
			return;
		Element obj = stack.pop();
		if (!(obj instanceof ListItem)) {
			stack.push(obj);
			return;
		}
		if (stack.empty()) {
			document.add(obj);
			return;
		}
		Element list = stack.pop();
		if (!(list instanceof com.itextpdf.text.List)) {
			stack.push(list);
			return;
		}
		ListItem item = (ListItem) obj;
		((com.itextpdf.text.List) list).add(item);
		ArrayList<Chunk> cks = item.getChunks();
		if (!cks.isEmpty())
			item.getListSymbol()
					.setFont(cks.get(0).getFont());
		stack.push(list);
	}
	
	public void addTable() throws DocumentException{

		TableWrapper table = (TableWrapper) stack.pop();
		PdfPTable tb = table.createTable();
		tb.setSplitRows(true);
		if (stack.empty())
			document.add(tb);
		else
			((TextElementArray) stack.peek()).add(tb);
		boolean state[] = tableState.pop();
		pendingTR = state[0];
		pendingTD = state[1];
		skipText = false;
	}
	
	public void addRow() {
		ArrayList<PdfPCell> row = new ArrayList<PdfPCell>();
        ArrayList<Float> cellWidths = new ArrayList<Float>();
        boolean percentage = false;
        float width;
        float totalWidth = 0;
        int zeroWidth = 0;
		TableWrapper table = null;
		while (true) {
			Element obj = stack.pop();
			if (obj instanceof CellWrapper) {
                CellWrapper cell = (CellWrapper)obj;
                width = cell.getWidth();
                cellWidths.add(new Float(width));
                percentage |= cell.isPercentage();
                if (width == 0) {
                	zeroWidth++;
                }
                else {
                	totalWidth += width;
                }
                row.add(cell.getCell());
			}
			if (obj instanceof TableWrapper) {
				table = (TableWrapper) obj;
				break;
			}
		}
        table.addRow(row);
        if (cellWidths.size() > 0) {
            // cells come off the stack in reverse, naturally
        	totalWidth = 100 - totalWidth;
            Collections.reverse(cellWidths);
            float[] widths = new float[cellWidths.size()];
            for (int i = 0; i < widths.length; i++) {
                widths[i] = cellWidths.get(i).floatValue();
                if (widths[i] == 0 && percentage && zeroWidth > 0) {
                	widths[i] = totalWidth / zeroWidth;
                }
            }
            table.setColWidths(widths);
        }
		stack.push(table);
		skipText = true;
	}
	
	/**
	 * Adds the current paragraph (if any) to the stack (if existing);
	 * or to the Document.
	 * @since 5.0.6
	 */
	public void addParagraph() throws DocumentException {
		if (currentParagraph != null) {
			if (stack.empty())
				document.add(currentParagraph);
			else {
				Element obj = stack.pop();
				if (obj instanceof TextElementArray) {
					TextElementArray current = (TextElementArray) obj;
					current.add(currentParagraph);
				}
				stack.push(obj);
			}
		}
		currentParagraph = null;
	}
	
	/**
	 */
	public void updateChain(String tag, Map<String, String> attrs) {
		chain.addToChain(tag, attrs);
	}
	
	public void updateChain(String tag) {
		chain.removeChain(tag);
	}
	
	public Object getProvider(String key) {
		return providers.get(key);
	}
	
	public Paragraph getCurrentParagraph() {
		return currentParagraph;
	}
	
	public ChainedProperties getChain() {
		return chain;
	}
	
	public boolean isPendingTR() {
		return pendingTR;
	}

	public void setPendingTR(boolean pendingTR) {
		this.pendingTR = pendingTR;
	}

	public boolean isPendingTD() {
		return pendingTD;
	}

	public void setPendingTD(boolean pendingTD) {
		this.pendingTD = pendingTD;
	}

	public void pushTableState() {
		tableState.push(new boolean[] { pendingTR, pendingTD });
	}
	
	public boolean isPendingLI() {
		return pendingLI;
	}

	public void setPendingLI(boolean pendingLI) {
		this.pendingLI = pendingLI;
	}

	public boolean isPRE() {
		return isPRE;
	}

	public void setPRE(boolean isPRE) {
		this.isPRE = isPRE;
	}

	public boolean isSkipText() {
		return skipText;
	}

	public void setSkipText(boolean skipText) {
		this.skipText = skipText;
	}




	/**
	 * Method contributed by Lubos Strapko
	 * @param h
	 * @param chain
	 * @since 2.1.3
	 */
	public void resolveStyleAttribute(Map<String, String> h, ChainedProperties chain) {
		String style = h.get("style");
		if (style == null)
			return;
		Properties prop = Markup.parseAttributes(style);
		for (Object element : prop.keySet()) {
			String key = (String) element;
			if (key.equals(Markup.CSS_KEY_FONTFAMILY)) {
				h.put(ElementTags.FACE, prop.getProperty(key));
			} else if (key.equals(Markup.CSS_KEY_FONTSIZE)) {
				float actualFontSize = Markup.parseLength(chain
						.getProperty(ElementTags.SIZE),
						Markup.DEFAULT_FONT_SIZE);
				if (actualFontSize <= 0f)
					actualFontSize = Markup.DEFAULT_FONT_SIZE;
				h.put(ElementTags.SIZE, Float.toString(Markup.parseLength(prop
						.getProperty(key), actualFontSize))
						+ "pt");
			} else if (key.equals(Markup.CSS_KEY_FONTSTYLE)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals("italic") || ss.equals("oblique"))
					h.put("i", null);
			} else if (key.equals(Markup.CSS_KEY_FONTWEIGHT)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals("bold") || ss.equals("700") || ss.equals("800")
						|| ss.equals("900"))
					h.put("b", null);
			} else if (key.equals(Markup.CSS_KEY_TEXTDECORATION)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				if (ss.equals(Markup.CSS_VALUE_UNDERLINE))
					h.put("u", null);
			} else if (key.equals(Markup.CSS_KEY_COLOR)) {
				BaseColor c = Markup.decodeColor(prop.getProperty(key));
				if (c != null) {
					int hh = c.getRGB();
					String hs = Integer.toHexString(hh);
					hs = "000000" + hs;
					hs = "#" + hs.substring(hs.length() - 6);
					h.put("color", hs);
				}
			} else if (key.equals(Markup.CSS_KEY_LINEHEIGHT)) {
				String ss = prop.getProperty(key).trim();
				float actualFontSize = Markup.parseLength(chain
						.getProperty(ElementTags.SIZE),
						Markup.DEFAULT_FONT_SIZE);
				if (actualFontSize <= 0f)
					actualFontSize = Markup.DEFAULT_FONT_SIZE;
				float v = Markup.parseLength(prop.getProperty(key),
						actualFontSize);
				if (ss.endsWith("%")) {
					h.put("leading", "0," + v / 100);
					return;
				}
				if ("normal".equalsIgnoreCase(ss)) {
					h.put("leading", "0,1.5");
					return;
				}
				h.put("leading", v + ",0");
			} else if (key.equals(Markup.CSS_KEY_TEXTALIGN)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				h.put("align", ss);
			} else if (key.equals(Markup.CSS_KEY_PADDINGLEFT)) {
				String ss = prop.getProperty(key).trim().toLowerCase();
				h.put("indent", Float.toString(Markup.parseLength(ss)));
			}
		}
	}


	/**
	 * Parses an HTML source to a List of Element objects
	 * @param reader	the HTML source
	 * @param style		a StyleSheet object
	 * @return a List of Element objects
	 * @throws IOException
	 */
	public static List<Element> parseToList(Reader reader, StyleSheet style)
			throws IOException {
		return parseToList(reader, style, null);
	}

	/**
	 * Parses an HTML source to a List of Element objects
	 * @param reader	the HTML source
	 * @param style		a StyleSheet object
	 * @param providers	map containing classes with extra info
	 * @return a List of Element objects
	 * @throws IOException
	 */
	public static List<Element> parseToList(Reader reader, StyleSheet style,
			HashMap<String, Object> providers) throws IOException {
		return parseToList(reader, style, null, providers);
	}
	
	public static List<Element> parseToList(Reader reader, StyleSheet style,
			Map<String, TagProcessor> tags, HashMap<String, Object> providers) throws IOException {
		HTMLWorker worker = new HTMLWorker(null, tags, style);
		worker.document = worker;
		worker.setProviders(providers);
		worker.objectList = new ArrayList<Element>();
		worker.parse(reader);
		return worker.objectList;
	}
	
	// DocListener interface

	/**
	 * @see com.itextpdf.text.ElementListener#add(com.itextpdf.text.Element)
	 */
	public boolean add(Element element) throws DocumentException {
		objectList.add(element);
		return true;
	}

	/**
	 * @see com.itextpdf.text.DocListener#close()
	 */
	public void close() {
	}

	/**
	 * @see com.itextpdf.text.DocListener#newPage()
	 */
	public boolean newPage() {
		return true;
	}

	/**
	 * @see com.itextpdf.text.DocListener#open()
	 */
	public void open() {
	}

	/**
	 * @see com.itextpdf.text.DocListener#resetPageCount()
	 */
	public void resetPageCount() {
	}

	/**
	 * @see com.itextpdf.text.DocListener#setMarginMirroring(boolean)
	 */
	public boolean setMarginMirroring(boolean marginMirroring) {
		return false;
	}

	/**
     * @see com.itextpdf.text.DocListener#setMarginMirroring(boolean)
	 * @since	2.1.6
	 */
	public boolean setMarginMirroringTopBottom(boolean marginMirroring) {
		return false;
	}

	/**
	 * @see com.itextpdf.text.DocListener#setMargins(float, float, float, float)
	 */
	public boolean setMargins(float marginLeft, float marginRight,
			float marginTop, float marginBottom) {
		return true;
	}

	/**
	 * @see com.itextpdf.text.DocListener#setPageCount(int)
	 */
	public void setPageCount(int pageN) {
	}

	/**
	 * @see com.itextpdf.text.DocListener#setPageSize(com.itextpdf.text.Rectangle)
	 */
	public boolean setPageSize(Rectangle pageSize) {
		return true;
	}

	// deprecated methods
	
	/**
	 * Sets the providers.
	 * @deprecated use setProviders() instead
	 */
	public void setInterfaceProps(HashMap<String, Object> providers) {
		setProviders(providers);
	}
	/**
	 * Gets the providers
	 * @deprecated use getProviders() instead
	 */
	public Map<String, Object> getInterfaceProps() {
		return providers;
	}

}
