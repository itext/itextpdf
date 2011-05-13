/*
 * $Id: XMLWorkerConfigurationImpl.java 53 2011-05-12 13:33:22Z redlab_b $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.taglistener;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.TagListener;
import com.itextpdf.tool.xml.XMLWorkerConfigurationImpl;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.HTML;

/**
 * @author redlab_b
 *
 */
public class AutoPDFTagListener implements TagListener, ElementHandler {

	private Document doc;
	private PdfWriter writer;
	private final Rectangle pageSize;
	private final FileMaker fileMaker;
	private final XMLWorkerConfigurationImpl config;
	private final CssFile file;

	/**
	 * @param fileMaker
	 * @param config
	 * @param pagesize
	 * @param file (nullable)
	 */
	public AutoPDFTagListener(final FileMaker fileMaker, final XMLWorkerConfigurationImpl config, final Rectangle pagesize, final CssFile file) {
		this.fileMaker = fileMaker;
		this.config = config;
		this.file = file;
		this.pageSize = pagesize;
	}

	public void open(final Tag tag) {
		String tagName = tag.getTag();
		if (HTML.Tag.HTML.equalsIgnoreCase(tagName)) {
			CssFilesImpl cssFilesImpl = new CssFilesImpl();
			if (null != file) {
				cssFilesImpl.add(file);
			}
			try {
			OutputStream os = fileMaker.getStream();
			this.doc = new Document(pageSize);
			this.writer = PdfWriter.getInstance(doc, os);
			this.config.document(doc).pdfWriter(writer).cssResolver(new StyleAttrCSSResolver(cssFilesImpl));
			} catch (DocumentException e) {
				throw new RuntimeWorkerException(e);
			} catch (IOException e) {
				throw new RuntimeWorkerException(e);
			}
		} else if (tag.getTag().equalsIgnoreCase(HTML.Tag.BODY)) {
			new DocMarginTagListener(writer, doc).open(tag);
		}

	}

	public void close(final Tag tag) {
		if (tag.getTag().equalsIgnoreCase(HTML.Tag.BODY)) {
			doc.close();
		}
	}

	public void text(final Tag tag, final String text) {
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.ElementHandler#addAll(java.util.List)
	 */
	public void addAll(final List<Element> currentContent) throws DocumentException {
		for (Element e : currentContent) {
			this.doc.add(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.ElementHandler#add(com.itextpdf.text.Element)
	 */
	public void add(final Element e) throws DocumentException {
		this.doc.add(e);
	}
}