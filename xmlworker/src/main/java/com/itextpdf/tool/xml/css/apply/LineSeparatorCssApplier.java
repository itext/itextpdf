/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Balder Van Camp, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with this program; if not, see http://www.gnu.org/licenses or
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a
 * covered work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.tool.xml.css.apply;

import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;

/**
 * @author Emiel Ackermann
 *
 */
public class LineSeparatorCssApplier {

	/**
	 * Applies CSS to LineSeparators
	 * 
	 * @param ls the LineSeparator
	 * @param t the tag with styles
	 * @param psc the {@link PageSize} container
	 * @return the styled {@link LineSeparator}
	 */
	public LineSeparator apply(final LineSeparator ls, final Tag t, final PageSizeContainable psc) {
    	float lineWidth = 1;
    	Map<String, String> css = t.getCSS();
		if(css.get(CSS.Property.HEIGHT) != null) {
    		lineWidth = CssUtils.getInstance().parsePxInCmMmPcToPt(css.get(CSS.Property.HEIGHT));
    	}
		ls.setLineWidth(lineWidth);
		BaseColor lineColor = BaseColor.BLACK;
		if(css.get(CSS.Property.COLOR) != null) {
			lineColor  = HtmlUtilities.decodeColor(css.get(CSS.Property.COLOR));
		} else if (css.get(CSS.Property.BACKGROUND_COLOR) != null) {
			lineColor = HtmlUtilities.decodeColor(css.get(CSS.Property.BACKGROUND_COLOR));
		}
		ls.setLineColor(lineColor);
		float percentage = 100;
		String widthStr = css.get(CSS.Property.WIDTH);
		if(widthStr != null) {
			if(widthStr.contains("%")) {
				percentage = Float.parseFloat(widthStr.replace("%", ""));
			} else {
				percentage = (CssUtils.getInstance().parsePxInCmMmPcToPt(widthStr)/psc.getPageSize().getWidth())*100;
			}
		}
		ls.setPercentage(percentage);
		ls.setOffset(9);
		return ls;
	}

}
