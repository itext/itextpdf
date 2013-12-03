/*
 * $Id: HTMLTagProcessors.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
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
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
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
import com.itextpdf.text.html.HtmlTags;

/**
 * This class maps tags such as div and span to their corresponding
 * TagProcessor classes.
 * @deprecated
 */
public class HTMLTagProcessors extends HashMap<String, HTMLTagProcessor> {

	/**
	 * Creates a Map containing supported tags.
	 */
	public HTMLTagProcessors() {
		super();
		put(HtmlTags.A, A);
		put(HtmlTags.B, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.BODY, DIV);
		put(HtmlTags.BR, BR);
		put(HtmlTags.DIV, DIV);
		put(HtmlTags.EM, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.FONT, SPAN);
		put(HtmlTags.H1, H);
		put(HtmlTags.H2, H);
		put(HtmlTags.H3, H);
		put(HtmlTags.H4, H);
		put(HtmlTags.H5, H);
		put(HtmlTags.H6, H);
		put(HtmlTags.HR, HR);
		put(HtmlTags.I, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.IMG, IMG);
		put(HtmlTags.LI, LI);
		put(HtmlTags.OL, UL_OL);
		put(HtmlTags.P, DIV);
		put(HtmlTags.PRE, PRE);
		put(HtmlTags.S, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.SPAN, SPAN);
		put(HtmlTags.STRIKE, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.STRONG, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.SUB, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.SUP, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.TABLE, TABLE);
		put(HtmlTags.TD, TD);
		put(HtmlTags.TH, TD);
		put(HtmlTags.TR, TR);
		put(HtmlTags.U, EM_STRONG_STRIKE_SUP_SUP);
		put(HtmlTags.UL, UL_OL);
	}

	/**
	 * Object that processes the following tags:
	 * i, em, b, strong, s, strike, u, sup, sub
	 */
	public static final HTMLTagProcessor EM_STRONG_STRIKE_SUP_SUP = new HTMLTagProcessor() {
		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
			tag = mapTag(tag);
			attrs.put(tag, null);
			worker.updateChain(tag, attrs);
		}
		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
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
			if (HtmlTags.EM.equalsIgnoreCase(tag))
				return HtmlTags.I;
			if (HtmlTags.STRONG.equalsIgnoreCase(tag))
				return HtmlTags.B;
			if (HtmlTags.STRIKE.equalsIgnoreCase(tag))
				return HtmlTags.S;
			return tag;
		}

	};

	/**
	 * Object that processes the a tag.
	 */
	public static final HTMLTagProcessor A = new HTMLTagProcessor() {
		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
			worker.updateChain(tag, attrs);
			worker.flushContent();
		}
		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) {
			worker.processLink();
			worker.updateChain(tag);
		}
	};

	/**
	 * Object that processes the br tag.
	 */
	public static final HTMLTagProcessor BR = new HTMLTagProcessor(){
		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
			worker.newLine();
		}
		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) {
		}

	};

	public static final HTMLTagProcessor UL_OL = new HTMLTagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingLI())
				worker.endElement(HtmlTags.LI);
			worker.setSkipText(true);
			worker.updateChain(tag, attrs);;
			worker.pushToStack(worker.createList(tag));
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingLI())
				worker.endElement(HtmlTags.LI);
			worker.setSkipText(false);
			worker.updateChain(tag);
			worker.processList();
		}

	};

	public static final HTMLTagProcessor HR = new HTMLTagProcessor(){

		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			worker.pushToStack(worker.createLineSeparator(attrs));
		}

		public void endElement(HTMLWorker worker, String tag) {
		}

	};

	public static final HTMLTagProcessor SPAN = new HTMLTagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) {
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) {
			worker.updateChain(tag);
		}

	};

	public static final HTMLTagProcessor H = new HTMLTagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (!attrs.containsKey(HtmlTags.SIZE)) {
				int v = 7 - Integer.parseInt(tag.substring(1));
				attrs.put(HtmlTags.SIZE, Integer.toString(v));
			}
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.carriageReturn();
			worker.updateChain(tag);
		}

	};

	public static final HTMLTagProcessor LI = new HTMLTagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingLI())
				worker.endElement(tag);
			worker.setSkipText(false);
			worker.setPendingLI(true);
			worker.updateChain(tag, attrs);
			worker.pushToStack(worker.createListItem());
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.carriageReturn();
			worker.setPendingLI(false);
			worker.setSkipText(true);
			worker.updateChain(tag);
			worker.processListItem();
		}

	};

	public static final HTMLTagProcessor PRE = new HTMLTagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (!attrs.containsKey(HtmlTags.FACE)) {
				attrs.put(HtmlTags.FACE, "Courier");
			}
			worker.updateChain(tag, attrs);
			worker.setInsidePRE(true);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.carriageReturn();
			worker.updateChain(tag);
			worker.setInsidePRE(false);
		}

	};

	public static final HTMLTagProcessor DIV = new HTMLTagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.carriageReturn();
			worker.updateChain(tag);
		}

	};


	public static final HTMLTagProcessor TABLE = new HTMLTagProcessor(){

		/**
		 * @throws DocumentException
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			TableWrapper table = new TableWrapper(attrs);
			worker.pushToStack(table);
			worker.pushTableState();
			worker.setPendingTD(false);
			worker.setPendingTR(false);
			worker.setSkipText(true);
			// Table alignment should not affect children elements, thus remove
			attrs.remove(HtmlTags.ALIGN);
            // In case this is a nested table reset colspan and rowspan
			attrs.put(HtmlTags.COLSPAN, "1");
			attrs.put(HtmlTags.ROWSPAN, "1");
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTR())
				worker.endElement(HtmlTags.TR);
			worker.updateChain(tag);
			worker.processTable();
			worker.popTableState();
			worker.setSkipText(false);
		}

	};
	public static final HTMLTagProcessor TR = new HTMLTagProcessor(){

		/**
		 * @throws DocumentException
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTR())
				worker.endElement(tag);
			worker.setSkipText(true);
			worker.setPendingTR(true);
			worker.updateChain(tag, attrs);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTD())
				worker.endElement(HtmlTags.TD);
			worker.setPendingTR(false);
			worker.updateChain(tag);
			worker.processRow();
			worker.setSkipText(true);
		}

	};
	public static final HTMLTagProcessor TD = new HTMLTagProcessor(){

		/**
		 * @throws DocumentException
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException {
			worker.carriageReturn();
			if (worker.isPendingTD())
				worker.endElement(tag);
			worker.setSkipText(false);
			worker.setPendingTD(true);
			worker.updateChain(HtmlTags.TD, attrs);
			worker.pushToStack(worker.createCell(tag));
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) throws DocumentException {
			worker.carriageReturn();
			worker.setPendingTD(false);
			worker.updateChain(HtmlTags.TD);
			worker.setSkipText(true);
		}

	};

	public static final HTMLTagProcessor IMG = new HTMLTagProcessor(){

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#startElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String, java.util.Map)
		 */
		public void startElement(HTMLWorker worker, String tag, Map<String, String> attrs) throws DocumentException, IOException {
			worker.updateChain(tag, attrs);
			worker.processImage(worker.createImage(attrs), attrs);
			worker.updateChain(tag);
		}

		/**
		 * @see com.itextpdf.text.html.simpleparser.HTMLTagProcessors#endElement(com.itextpdf.text.html.simpleparser.HTMLWorker, java.lang.String)
		 */
		public void endElement(HTMLWorker worker, String tag) {
		}

	};

	/** Serial version UID. */
	private static final long serialVersionUID = -959260811961222824L;
}
