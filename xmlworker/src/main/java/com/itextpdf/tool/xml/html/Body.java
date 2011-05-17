/*
 * $Id$
 *
 * This file is part of the iText (R) project. Copyright (c) 1998-2011 1T3XT BVBA Authors: Balder Van Camp, Emiel
 * Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License version 3 as published by the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * 1T3XT, 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public License along with this program; if not,
 * see http://www.gnu.org/licenses or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL: http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions of this program must display Appropriate
 * Legal Notices, as required under Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a covered work must retain the producer
 * line in every PDF that is created or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a commercial license. Buying such a license is
 * mandatory as soon as you develop commercial activities involving the iText software without disclosing the source
 * code of your own applications. These activities include: offering paid services to customers as an ASP, serving PDFs
 * on the fly in a web application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.html;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.apply.NoNewLineParagraphCssApplier;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;
import com.itextpdf.tool.xml.pipeline.Writable;
import com.itextpdf.tool.xml.pipeline.WritableElement;

/**
 * @author redlab_b
 *
 */
public class Body extends AbstractTagProcessor {

//	private final CssUtils utils = CssUtils.getInstance();
	private static final Logger LOGGER = LoggerFactory.getLogger(Body.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.TagProcessor#startElement(com.itextpdf.tool.xml.Tag, java.util.List,
	 * com.itextpdf.text.Document)
	 */
	@Override
	public List<Writable> start(final Tag tag) {
//		Document doc = configuration.getDocument();
//		float pageWidth = configuration.getPageSize().getWidth();
////TODO how to set the margins of the first page of a doc? Before doc is opened, but how to get the margins out of the body-tag?
//		if (null != doc) {
//			float marginLeft = 0;
//			float marginRight = 0;
//			float marginTop = 0;
//			float marginBottom = 0;
//			Map<String, String> css = tag.getCSS();
//			for (Entry<String, String> entry : css.entrySet()) {
//	        	String key = entry.getKey();
//				String value = entry.getValue();
//				if(key.equalsIgnoreCase(CSS.Property.MARGIN_LEFT)) {
//					marginLeft = utils.parseValueToPt(value, pageWidth);
//				} else if(key.equalsIgnoreCase(CSS.Property.MARGIN_RIGHT)) {
//					marginRight = utils.parseValueToPt(value, pageWidth);
//				} else if(key.equalsIgnoreCase(CSS.Property.MARGIN_TOP)) {
//					marginTop = utils.parseValueToPt(value, pageWidth);
//				} else if(key.equalsIgnoreCase(CSS.Property.MARGIN_BOTTOM)) {
//					marginBottom = utils.parseValueToPt(value, pageWidth);
//					configuration.getMemory().put(XMLWorkerConfig.LAST_MARGIN_BOTTOM, marginBottom);
//				}
//			}
//			doc.setMargins(marginLeft, marginRight, marginTop, marginBottom);
//		}
//
		return new ArrayList<Writable>(0);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.TagProcessor#content(com.itextpdf.tool.xml.Tag, java.util.List,
	 * com.itextpdf.text.Document, java.lang.String)
	 */
	@Override
	public List<Writable> content(final Tag tag, final String content) {
		String sanitized = HTMLUtils.sanitize(content);
		List<Writable> l = new ArrayList<Writable>(1);
		if (sanitized.length() > 0) {
			WritableElement we = new WritableElement();
			we.add(new NoNewLineParagraphCssApplier(configuration).apply(new NoNewLineParagraph(sanitized), tag));
			l.add(we);
		}
		return l;
	}

}
