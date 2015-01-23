/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.tool.xml.css;

import java.util.List;

import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.HTML;

/**
 * @author Emiel Ackermann
 *
 */
public class WidthCalculator {

	private final CssUtils utils = CssUtils.getInstance();

	/**
	 * Tries to calculate a width from a tag and it's ancestors.
	 * @param tag the tag to use
	 * @param roottags the root tags,
	 * @param pagewidth the page width
	 * @return the width
	 */
	public float getWidth(final Tag tag, final List<String> roottags, final float pagewidth){
		return getWidth(tag, roottags, pagewidth, -1);
	}

	public float getWidth(final Tag tag, final List<String> roottags, final float pagewidth, final float initialTotalWidth){
		float width = 0;
		String widthValue = tag.getCSS().get(HTML.Attribute.WIDTH);
		if(widthValue == null) {
			widthValue = tag.getAttributes().get(HTML.Attribute.WIDTH);
		}
		if(widthValue != null) {
			if(utils.isNumericValue(widthValue) || utils.isMetricValue(widthValue)) {
				width = utils.parsePxInCmMmPcToPt(widthValue);
			} else if (utils.isRelativeValue(widthValue)) {
				Tag ancestor = tag;
				float firstAncestorsWidth = 0;
				while(firstAncestorsWidth == 0 && ancestor.getParent() != null) {
					ancestor = ancestor.getParent();
					firstAncestorsWidth = getWidth(ancestor, roottags, pagewidth, initialTotalWidth);
				}
				if (firstAncestorsWidth == 0) {
					width = utils.parseRelativeValue(widthValue, pagewidth);
				} else {
					width = utils.parseRelativeValue(widthValue, firstAncestorsWidth);
				}
			}
		} else if (roottags.contains(tag.getName())){
			if (Float.compare(initialTotalWidth, -1) == 0)
				width = pagewidth;
			else
			    width = initialTotalWidth;
		}
		return width;
	}
}
