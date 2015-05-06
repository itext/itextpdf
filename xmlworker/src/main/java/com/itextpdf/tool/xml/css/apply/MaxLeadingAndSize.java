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

import java.util.List;
import java.util.Map;

import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.FontSizeTranslator;

/**
 * Serves as a container class for the largest font size and/or largest leading found in a tag and all its siblings.
 *
 * @author Emiel Ackermann
 *
 */
public final class MaxLeadingAndSize {
	private final CssUtils utils = CssUtils.getInstance();
	private final FontSizeTranslator fontSizeTranslator = FontSizeTranslator.getInstance();
	private float largestLeading;
	private float largestFont;

	/**
	 *
	 * @return largest leading
	 */
	public float getLargestLeading() {
		return largestLeading;
	}
	/**
	 *
	 * @return largest font size
	 */
	public float getLargestFont() {
		return largestFont;
	}
	/**
	 * Sets largest font size and largest leading based on the css styles "font-size" and "line-height" of a given tag and its children.
	 * @param t tag of which the variables need to be set.
	 */
	public void setVariablesBasedOnChildren(final Tag t) {
		float fontSizeParent = fontSizeTranslator.getFontSize(t);
		float largestFontChildren = getLargestFontFromChildren(t.getChildren());
		largestFont = fontSizeParent>largestFontChildren?fontSizeParent:largestFontChildren;
		float leadingParent = calculateLeading(t);
		float largestLeadingChildren = getLargestLeadingFromChildren(t.getChildren());
		largestLeading = leadingParent>largestLeadingChildren?leadingParent:largestLeadingChildren;
	}

	/**
	 * Set the largest leading based on calculateLeading only. (Children not taken into account)
	 * @param tag the tag
	 */
	public void setLeading(final Tag tag) {
		largestLeading = calculateLeading(tag);
	}
	/**
	 * Iterates over all children in a List and returns the largest font size found.
	 * @param children List<Tag> containing a list of children.
	 * @return float largest font size.
	 */
	public float getLargestFontFromChildren(final List<Tag> children) {
		float largestFont = 12;
		for(Tag tag: children) {
			float fontSize = fontSizeTranslator.getFontSize(tag);
			if(fontSize > largestFont) {
				largestFont = fontSize;
			}
		}
		return largestFont;
	}
	/**
	 * Iterates over all children in a List and returns the largest leading found.
	 * @param children List<Tag> containing a list of children.
	 * @return float largest leading.
	 */
	private float getLargestLeadingFromChildren(final List<Tag> children) {
		float leading = 0;
		for(Tag tag: children) {
			Float calculatedLineHeight = calculateLeading(tag);
			if(calculatedLineHeight > leading) {
				leading = calculatedLineHeight;
			}
			getLargestLeadingFromChildren(tag.getChildren());
		}
		return leading;
	}

	/**
	 * Calculates the leading of the given tag.
	 * <br />
	 * First checks which line-height string is present in the css of the tag, if any. Following strings are allowed;
	 * <ul>
	 * <li>a constant (containing px, in, cm, mm, pc, em, ex or pt),</li>
	 * <li>percentage (containing %),</li>
	 * <li>multiplier (only digits),</li>
	 * </ul>
	 * Then this methods calculates the leading based on the font-size and the line-height.<br /><br />
	 * If no line-height was given or if the line-height:normal was given, leading = font-size * 1.5f.
	 * @param t tag of which the leading has to be calculated.
	 * @return float containing the leading of the tag.
	 */
	public Float calculateLeading(final Tag t) {
		float leading = 0;
		Map <String, String> css = t.getCSS();
		float fontSize = fontSizeTranslator.getFontSize(t);
		if(css.get(CSS.Property.LINE_HEIGHT) != null) {
			String value = css.get(CSS.Property.LINE_HEIGHT);
			if(utils.isNumericValue(value)) {
				leading = Float.parseFloat(value) * fontSize;
			} else if (utils.isRelativeValue(value)) {
				leading = utils.parseRelativeValue(value, fontSize);
			} else if (utils.isMetricValue(value)){
				leading = utils.parsePxInCmMmPcToPt(value);
			}
			// a faulty value was entered for line-height.
			if (leading == 0) {
				leading = fontSize*1.5f;
			}
		} else {
			leading = fontSize*1.5f;
		}
		return leading;
	}
}