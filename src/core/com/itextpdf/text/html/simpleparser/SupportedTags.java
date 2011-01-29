package com.itextpdf.text.html.simpleparser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ElementTags;
import com.itextpdf.text.html.HtmlTags;

public abstract class SupportedTags {

	public static TagProcessor get(String tag) {
		return TAGS.get(tag);
	}

	/**
	 * Object that processes the following tags:
	 * i, em, b, strong, s, strike, u, sup, sub
	 */
	public static final TagProcessor EM_STRONG_STRIKE_SUP_SUP = new TagProcessor() {
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
			tag = mapTag(tag);
			attrs = new HashMap<String, String>();
			attrs.put(tag, null);
			worker.updateChain(tag, attrs);
		}
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) {
			tag = mapTag(tag);
			worker.updateChain(tag);
		}
		/**
		 * Maps em to i, strong to b, and strike to s.
		 * This is a convention: the style parser expects i, b and s.
		 * @param tag the original tag
		 * @return the mapped tag
		 */
		private String mapTag(String tag) {
			if ("em".equalsIgnoreCase(tag))
				return "i";
			if ("strong".equalsIgnoreCase(tag))
				return "b";
			if ("strike".equalsIgnoreCase(tag))
				return "s";
			return tag;
		}
		
	};
	
	/**
	 * Object that processes the a tag.
	 */
	public static final TagProcessor A = new TagProcessor() {
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
			worker.updateChain(tag, attrs);
			worker.pushParagraph();
		}
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) {
			worker.updateParagraph();
			boolean skip = false;
			LinkProvider i = (LinkProvider) worker.getProvider(HTMLWorker.LINK_PROVIDER);
			if (i != null)
				skip = i.process(worker.getCurrentParagraph(), worker.getChain());
			if (!skip) {
				String href = worker.getChain().getProperty("href");
				if (href != null) {
					for (Chunk ck : worker.getCurrentParagraph().getChunks()) {
						ck.setAnchor(href);
					}
				}
			}
			worker.mergeParagraph();
			worker.updateChain("a");
		}
	};
	
	/**
	 * Object that processes the br tag.
	 */
	public static final TagProcessor BR = new TagProcessor(){
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
			worker.updateParagraph();
			worker.newLine();
		}
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) {
		}
		
	};
	
	public static final TagProcessor UL_OL = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.addParagraph();
			if (worker.isPendingLI())
				worker.endElement(HtmlTags.LISTITEM);
			worker.setSkipText(true);
			worker.updateChain(tag, attrs);
			worker.pushToStack(worker.createList(tag));
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.addParagraph();
			if (worker.isPendingLI())
				worker.endElement(HtmlTags.LISTITEM);
			worker.setSkipText(false);
			worker.updateChain(tag);
			worker.addList();
		}
		
	};
	
	public static final TagProcessor HR = new TagProcessor(){

		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
			worker.addLineSeparator(attrs);
		}

		public void endElement(HTMLWorker worker, String tag) {
		}
		
	};
	
	public static final TagProcessor SPAN = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) {
			worker.updateChain(tag);
		}
		
	};
	
	public static final TagProcessor H = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.addParagraph();
			if (!attrs.containsKey(ElementTags.SIZE)) {
				int v = 7 - Integer.parseInt(tag.substring(1));
				attrs.put(ElementTags.SIZE, Integer.toString(v));
			}
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.addParagraph();
			worker.updateChain(tag);
		}
		
	};
	
	public static final TagProcessor LI = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.addParagraph();
			if (worker.isPendingLI())
				worker.endElement(HtmlTags.LISTITEM);
			worker.setSkipText(false);
			worker.setPendingLI(true);
			worker.updateChain(tag, attrs);
			worker.pushToStack(worker.createListItem());
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.addParagraph();

			worker.setPendingLI(false);
			worker.setSkipText(true);
			worker.updateChain(tag);
			worker.addListItem();
		}
		
	};

	public static final TagProcessor PRE = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.addParagraph();
			if (!attrs.containsKey(ElementTags.FACE)) {
				attrs.put(ElementTags.FACE, "Courier");
			}
			worker.updateChain(tag, attrs);
			worker.setPRE(true);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.addParagraph();
			worker.updateChain(tag);
			worker.setPRE(false);
		}
		
	};
	
	public static final TagProcessor DIV = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.addParagraph();
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.addParagraph();
			worker.updateChain(tag);
		}
		
	};
	

	public static final TagProcessor TABLE = new TagProcessor(){

		/**
		 * @throws DocumentException 
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.addParagraph();
			TableWrapper table = new TableWrapper(attrs);
			worker.pushToStack(table);
			worker.pushTableState();
			worker.setPendingTD(false);
			worker.setPendingTR(false);
			worker.setSkipText(true);
			// Table alignment should not affect children elements, thus remove
			attrs.remove("align");
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.addParagraph();
			if (worker.isPendingTR())
				worker.endElement("tr");
			worker.updateChain("table");
			worker.addTable();
		}
		
	};
	public static final TagProcessor TR = new TagProcessor(){

		/**
		 * @throws DocumentException 
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.addParagraph();
			if (worker.isPendingTR())
				worker.endElement(tag);
			worker.setSkipText(true);
			worker.setPendingTR(true);
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.addParagraph();
			if (worker.isPendingTD())
				worker.endElement("td");
			worker.setPendingTR(false);
			worker.updateChain(tag);
			worker.addRow();
		}
		
	};
	public static final TagProcessor TD = new TagProcessor(){

		/**
		 * @throws DocumentException 
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.addParagraph();
			if (worker.isPendingTD())
				worker.endElement(tag);
			worker.setSkipText(false);
			worker.setPendingTD(true);
			worker.updateChain("td", attrs);
			worker.pushToStack(new CellWrapper(tag, worker.getChain()));
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.addParagraph();
			worker.setPendingTD(false);
			worker.updateChain("td");
			worker.setSkipText(true);
		}
		
	};
	
	public static final TagProcessor IMG = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException, IOException {
			worker.createImage(tag, attrs);		
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) {
		}
		
	};
	
	public static final Map<String, TagProcessor> TAGS;
	static {
		TAGS = new HashMap<String, TagProcessor>();
		TAGS.put("a", A);
		TAGS.put("b", EM_STRONG_STRIKE_SUP_SUP);
		TAGS.put("body", DIV);
		TAGS.put("br", BR);
		TAGS.put("div", DIV);
		TAGS.put("em", EM_STRONG_STRIKE_SUP_SUP);
		TAGS.put("font", SPAN);
		TAGS.put("h1", H);
		TAGS.put("h2", H);
		TAGS.put("h3", H);
		TAGS.put("h4", H);
		TAGS.put("h5", H);
		TAGS.put("h6", H);
		TAGS.put("hr", HR);
		TAGS.put("i", EM_STRONG_STRIKE_SUP_SUP);
		TAGS.put("img", IMG);
		TAGS.put("li", LI);
		TAGS.put("ol", UL_OL);
		TAGS.put("p", DIV);
		TAGS.put("pre", PRE);
		TAGS.put("s", EM_STRONG_STRIKE_SUP_SUP);
		TAGS.put("span", SPAN);
		TAGS.put("strike", EM_STRONG_STRIKE_SUP_SUP);
		TAGS.put("strong", EM_STRONG_STRIKE_SUP_SUP);
		TAGS.put("sub", EM_STRONG_STRIKE_SUP_SUP);
		TAGS.put("sup", EM_STRONG_STRIKE_SUP_SUP);
		TAGS.put("table", TABLE);
		TAGS.put("td", TD);
		TAGS.put("th", TD);
		TAGS.put("tr", TR);
		TAGS.put("u", EM_STRONG_STRIKE_SUP_SUP);
		TAGS.put("ul", UL_OL);
	}
}
