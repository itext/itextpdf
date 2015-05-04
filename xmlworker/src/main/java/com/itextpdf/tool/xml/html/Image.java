/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
package com.itextpdf.tool.xml.html;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.xml.XMLUtil;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.net.ImageRetrieve;
import com.itextpdf.tool.xml.net.exc.NoImageException;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.NoImageProviderException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author redlab_b
 *
 */
public class Image extends AbstractTagProcessor {

	private final CssUtils utils = CssUtils.getInstance();
	private static final Logger logger = LoggerFactory.getLogger(Image.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.TagProcessor#endElement(com.itextpdf.tool.xml.Tag,
	 * java.util.List, com.itextpdf.text.Document)
	 */
	@Override
	public List<Element> end(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
		Map<String, String> attributes = tag.getAttributes();
		String src = attributes.get(HTML.Attribute.SRC);
		com.itextpdf.text.Image img = null;
		List<Element> l = new ArrayList<Element>(1);
		if (null != src && src.length() > 0) {
			// check if the image was already added once
            src = XMLUtil.unescapeXML(src);
            src = src.trim();
			try {
				if (logger.isLogging(Level.TRACE)) {
					logger.trace(String.format(LocaleMessages.getInstance().getMessage(LocaleMessages.HTML_IMG_USE), src));
				}
				img = new ImageRetrieve(getHtmlPipelineContext(ctx).getImageProvider()).retrieveImage(src);
			} catch (IOException e) {
				if (logger.isLogging(Level.ERROR)) {
					logger.error(String.format(LocaleMessages.getInstance().getMessage(LocaleMessages.HTML_IMG_RETRIEVE_FAIL), src), e);
				}
			} catch (NoImageException e) {
				if (logger.isLogging(Level.ERROR)) {
					logger.error("", e);
				}
			} catch (NoCustomContextException e) {
				throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e);
			}
			if (null != img) {
				try {
                    if ( attributes.get(HTML.Attribute.ALT) != null) {
                        img.setAccessibleAttribute(PdfName.ALT, new PdfString(attributes.get(HTML.Attribute.ALT)));
                    }
					HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
					l.add(getCssAppliers().apply(new Chunk((com.itextpdf.text.Image) getCssAppliers().apply(img, tag, htmlPipelineContext), 0, 0, true), tag, htmlPipelineContext));
				} catch (NoCustomContextException e) {
					throw new RuntimeWorkerException(e);
				}
			}
		}
		return l;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.TagProcessor#isStackOwner()
	 */
	@Override
	public boolean isStackOwner() {
		return false;
	}


}
