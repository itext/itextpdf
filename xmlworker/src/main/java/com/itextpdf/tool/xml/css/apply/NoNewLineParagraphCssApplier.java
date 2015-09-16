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
package com.itextpdf.tool.xml.css.apply;

import java.util.Map;
import java.util.Map.Entry;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;

/**
 *
 * @author itextpdf.com
 *
 */
public class NoNewLineParagraphCssApplier {
	private final CssUtils utils = CssUtils.getInstance();



	/**
	 * Styles a NoNewLineParagraph
	 * 
	 * @param p the paragraph to style
	 * @param t the tag
	 * @param configuration the MarginMemory to check margin sizes
	 * @return a styled NoNewLineParagraph
	 */
	public NoNewLineParagraph apply(final NoNewLineParagraph p, final Tag t, final MarginMemory configuration) {
		/*MaxLeadingAndSize m = new MaxLeadingAndSize();
		if (configuration.getRootTags().contains(t.getName())) {
			m.setLeading(t);
		} else {
			m.setVariablesBasedOnChildren(t);
		}*/
		float fontSize = FontSizeTranslator.getInstance().getFontSize(t);
		float lmb = 0;
		boolean hasLMB = false;
		Map<String, String> css = t.getCSS();
        for (Entry<String, String> entry : css.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if(CSS.Property.MARGIN_TOP.equalsIgnoreCase(key)) {
				p.setSpacingBefore(p.getSpacingBefore() + utils.calculateMarginTop(value, fontSize, configuration));
			} else if(CSS.Property.PADDING_TOP.equalsIgnoreCase(key)) {
				p.setSpacingBefore(p.getSpacingBefore() + utils.parseValueToPt(value, fontSize));
			} else if (CSS.Property.MARGIN_BOTTOM.equalsIgnoreCase(key)) {
				float after = utils.parseValueToPt(value, fontSize);
				p.setSpacingAfter(p.getSpacingAfter() + after);
				lmb = after;
				hasLMB = true;
			} else if (CSS.Property.PADDING_BOTTOM.equalsIgnoreCase(key)) {
				p.setSpacingAfter(p.getSpacingAfter() + utils.parseValueToPt(value, fontSize));
			} else if(CSS.Property.MARGIN_LEFT.equalsIgnoreCase(key)) {
				p.setIndentationLeft(p.getIndentationLeft() + utils.parseValueToPt(value, fontSize));
			} else if(CSS.Property.MARGIN_RIGHT.equalsIgnoreCase(key)) {
				p.setIndentationRight(p.getIndentationRight() + utils.parseValueToPt(value, fontSize));
			} else if (CSS.Property.PADDING_LEFT.equalsIgnoreCase(key)) {
				p.setIndentationLeft(p.getIndentationLeft() + utils.parseValueToPt(value, fontSize));
			} else if (CSS.Property.PADDING_RIGHT.equalsIgnoreCase(key)) {
				p.setIndentationRight(p.getIndentationRight() + utils.parseValueToPt(value, fontSize));
			} else if(CSS.Property.TEXT_ALIGN.equalsIgnoreCase(key)) {
				p.setAlignment(CSS.getElementAlignment(value));
			} else if (CSS.Property.TEXT_INDENT.equalsIgnoreCase(key)) {
				p.setFirstLineIndent(utils.parseValueToPt(value, fontSize));
			}
		}
		// setDefaultMargin to largestFont if no margin-top is set and p-tag is child of the root tag.
        if (null != t.getParent()) {
			String parent = t.getParent().getName();
			if(css.get(CSS.Property.MARGIN_TOP) == null && configuration.getRootTags().contains(parent)) {
				p.setSpacingBefore(p.getSpacingBefore()+utils.calculateMarginTop(fontSize+"pt", 0, configuration));
			}
			if(css.get(CSS.Property.MARGIN_BOTTOM) == null && configuration.getRootTags().contains(parent)) {
				p.setSpacingAfter(p.getSpacingAfter()+fontSize);
				css.put(CSS.Property.MARGIN_BOTTOM, fontSize+"pt");
				lmb = fontSize;
				hasLMB = true;
			}
			//p.setLeading(m.getLargestLeading());
			if(p.getAlignment() == -1) {
				p.setAlignment(Element.ALIGN_LEFT);
			}
		}

		if (hasLMB) {
			configuration.setLastMarginBottom(lmb);
		}
		return p;
	}

}
