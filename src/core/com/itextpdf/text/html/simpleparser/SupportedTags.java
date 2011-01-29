/*
 * $Id: HTMLWorker.java 4666 2011-01-29 12:53:09Z blowagie $
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2010 1T3XT BVBA
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
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ElementTags;
import com.itextpdf.text.html.HtmlTags;

/**
 * This class maps tags such as div and span to their corresponding
 * TagProcessor classes.
 * @since 5.0.6
 */
public class SupportedTags extends HashMap<String, TagProcessor> {

	/**
	 * Creates a Map containing supported tags.
	 */
	public SupportedTags() {
		super();
		put("a", A);
		put("b", EM_STRONG_STRIKE_SUP_SUP);
		put("body", DIV);
		put("br", BR);
		put("div", DIV);
		put("em", EM_STRONG_STRIKE_SUP_SUP);
		put("font", SPAN);
		put("h1", H);
		put("h2", H);
		put("h3", H);
		put("h4", H);
		put("h5", H);
		put("h6", H);
		put("hr", HR);
		put("i", EM_STRONG_STRIKE_SUP_SUP);
		put("img", IMG);
		put("li", LI);
		put("ol", UL_OL);
		put("p", DIV);
		put("pre", PRE);
		put("s", EM_STRONG_STRIKE_SUP_SUP);
		put("span", SPAN);
		put("strike", EM_STRONG_STRIKE_SUP_SUP);
		put("strong", EM_STRONG_STRIKE_SUP_SUP);
		put("sub", EM_STRONG_STRIKE_SUP_SUP);
		put("sup", EM_STRONG_STRIKE_SUP_SUP);
		put("table", TABLE);
		put("td", TD);
		put("th", TD);
		put("tr", TR);
		put("u", EM_STRONG_STRIKE_SUP_SUP);
		put("ul", UL_OL);
	}
	
	/**
	 * Object that processes the following tags:
	 * i, em, b, strong, s, strike, u, sup, sub
	 */
	public static final TagProcessor EM_STRONG_STRIKE_SUP_SUP = new TagProcessor() {
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) {
			tag = mapTag(tag);
			attrs = new HashMap<String, String>();
			attrs.put(tag, null);
			chain.addToChain(tag, attrs);;
		}
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) {
			tag = mapTag(tag);
			chain.removeChain(tag);
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
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) {
			chain.addToChain(tag, attrs);;
			worker.flushContent();
		}
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) {
			worker.addLink();
			chain.removeChain(tag);
		}
	};
	
	/**
	 * Object that processes the br tag.
	 */
	public static final TagProcessor BR = new TagProcessor(){
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) {
			worker.newLine();
		}
		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) {
		}
		
	};
	
	public static final TagProcessor UL_OL = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingLI())
				worker.endElement(HtmlTags.LISTITEM);
			worker.setSkipText(true);
			chain.addToChain(tag, attrs);;
			worker.pushToStack(worker.createList(tag));
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingLI())
				worker.endElement(HtmlTags.LISTITEM);
			worker.setSkipText(false);
			chain.removeChain(tag);
			worker.addList();
		}
		
	};
	
	public static final TagProcessor HR = new TagProcessor(){

		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			worker.pushToStack(worker.createLineSeparator(attrs));
		}

		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) {
		}
		
	};
	
	public static final TagProcessor SPAN = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) {
			chain.addToChain(tag, attrs);;
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) {
			chain.removeChain(tag);
		}
		
	};
	
	public static final TagProcessor H = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			if (!attrs.containsKey(ElementTags.SIZE)) {
				int v = 7 - Integer.parseInt(tag.substring(1));
				attrs.put(ElementTags.SIZE, Integer.toString(v));
			}
			chain.addToChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			chain.removeChain(tag);
		}
		
	};
	
	public static final TagProcessor LI = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingLI())
				worker.endElement(HtmlTags.LISTITEM);
			worker.setSkipText(false);
			worker.setPendingLI(true);
			chain.addToChain(tag, attrs);
			worker.pushToStack(worker.createListItem());
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();

			worker.setPendingLI(false);
			worker.setSkipText(true);
			chain.removeChain(tag);
			worker.addListItem();
		}
		
	};

	public static final TagProcessor PRE = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			if (!attrs.containsKey(ElementTags.FACE)) {
				attrs.put(ElementTags.FACE, "Courier");
			}
			chain.addToChain(tag, attrs);
			worker.setInsidePRE(true);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			chain.removeChain(tag);
			worker.setInsidePRE(false);
		}
		
	};
	
	public static final TagProcessor DIV = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			chain.addToChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			chain.removeChain(tag);
		}
		
	};
	

	public static final TagProcessor TABLE = new TagProcessor(){

		/**
		 * @throws DocumentException 
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			TableWrapper table = new TableWrapper(attrs);
			worker.pushToStack(table);
			worker.pushTableState();
			worker.setPendingTD(false);
			worker.setPendingTR(false);
			worker.setSkipText(true);
			// Table alignment should not affect children elements, thus remove
			attrs.remove("align");
			chain.addToChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTR())
				worker.endElement("tr");
			chain.removeChain(tag);
			worker.addTable();
		}
		
	};
	public static final TagProcessor TR = new TagProcessor(){

		/**
		 * @throws DocumentException 
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTR())
				worker.endElement(tag);
			worker.setSkipText(true);
			worker.setPendingTR(true);
			chain.addToChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTD())
				worker.endElement("td");
			worker.setPendingTR(false);
			chain.removeChain(tag);
			worker.addRow();
		}
		
	};
	public static final TagProcessor TD = new TagProcessor(){

		/**
		 * @throws DocumentException 
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTD())
				worker.endElement(tag);
			worker.setSkipText(false);
			worker.setPendingTD(true);
			chain.addToChain("td", attrs);
			worker.pushToStack(new CellWrapper(tag, chain));
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) throws DocumentException {
			worker.carriageReturn();
			worker.setPendingTD(false);
			chain.removeChain("td");
			worker.setSkipText(true);
		}
		
	};
	
	public static final TagProcessor IMG = new TagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs, AttributeChain chain) throws DocumentException, IOException {
			chain.addToChain(tag, attrs);
			worker.addImage(worker.createImage(attrs), attrs);
			chain.removeChain(tag);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.SupportedTags#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag, AttributeChain chain) {
		}
		
	};
	
	/** Serial version UID. */
	private static final long serialVersionUID = -959260811961222824L;
}
