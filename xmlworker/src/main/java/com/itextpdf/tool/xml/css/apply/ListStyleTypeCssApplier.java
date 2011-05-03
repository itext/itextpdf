/*
 * $Id$
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
package com.itextpdf.tool.xml.css.apply;

import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.GreekList;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.RomanList;
import com.itextpdf.text.ZapfDingbatsList;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.XMLWorkerConfig;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssApplier;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.net.ImageRetrieve;

/**
 * @author Balder Van Camp
 *
 */
public class ListStyleTypeCssApplier implements CssApplier<List> {

	private final XMLWorkerConfig configuration;
	private final CssUtils utils = CssUtils.getInstance();

	/**
	 * @param configuration the provider
	 *
	 */
	public ListStyleTypeCssApplier(final XMLWorkerConfig configuration) {
		this.configuration = configuration;
	}

	/**
	 * The ListCssApplier has the capabilities to change the type of the given {@link List} dependable on the css.
	 * This means: <strong>Always replace your list with the returned one and add content to the list after applying!</strong>
	 */
	// not implemented: list-style-type:armenian, georgian, decimal-leading-zero.
	public List apply(final List list, final Tag t) {
		List lst = list;
		Map<String, String> css = t.getCSS();
		String styleType = css.get(CSS.Property.LIST_STYLE_TYPE);
		if (null != styleType) {
			if (styleType.equalsIgnoreCase(CSS.Value.NONE)) {
				list.setLettered(false);
				list.setNumbered(false);
				list.setListSymbol("");
			} else if (CSS.Value.DECIMAL.equalsIgnoreCase(styleType)) {
				lst = new List(List.ORDERED);
			} else if (CSS.Value.DISC.equalsIgnoreCase(styleType)) {
				lst = new ZapfDingbatsList(108);
			} else if (CSS.Value.SQUARE.equalsIgnoreCase(styleType)) {
				lst = new ZapfDingbatsList(110);
			} else if (CSS.Value.CIRCLE.equalsIgnoreCase(styleType)) {
				lst = new ZapfDingbatsList(109);
			} else if (CSS.Value.LOWER_ROMAN.equals(styleType)) {
				lst = new RomanList(true, 0);
			} else if (CSS.Value.UPPER_ROMAN.equals(styleType)) {
				lst = new RomanList(false, 0);
			} else if (CSS.Value.LOWER_GREEK.equals(styleType)) {
				lst = new GreekList(true, 0);
			} else if (CSS.Value.UPPER_GREEK.equals(styleType)) {
				lst = new GreekList(false, 0);
			} else if (CSS.Value.LOWER_ALPHA.equals(styleType) || CSS.Value.LOWER_LATIN.equals(styleType)) {
				lst = new List(List.ORDERED, List.ALPHABETICAL);
				lst.setLowercase(true);
			} else if (CSS.Value.UPPER_ALPHA.equals(styleType) || CSS.Value.UPPER_LATIN.equals(styleType)) {
				lst = new List(List.ORDERED, List.ALPHABETICAL);
				lst.setLowercase(false);
			}
		} else if (t.getTag().equalsIgnoreCase(HTML.Tag.OL)) {
			lst = new List(List.ORDERED);
		} else if (t.getTag().equalsIgnoreCase(HTML.Tag.UL)) {
			lst = new ZapfDingbatsList(108);
		}
		if (null != css.get(CSS.Property.LIST_STYLE_IMAGE)
				&& !css.get(CSS.Property.LIST_STYLE_IMAGE).equalsIgnoreCase(CSS.Value.NONE)) {
			Image img = new ImageRetrieve(configuration.getProvider())
					.retrieveImage(utils.extractUrl(css.get(
							CSS.Property.LIST_STYLE_IMAGE)));
			list.setListSymbol(new Chunk(img, 0, 0, true));
		}
		lst.setAlignindent(false);
		lst.setAutoindent(false);
		lst.setSymbolIndent(20);
		float leftIndent = 0;
		if (null != css.get(CSS.Property.LIST_STYLE_POSITION) && css.get(CSS.Property.LIST_STYLE_POSITION).equalsIgnoreCase(CSS.Value.INSIDE)) {
			leftIndent += 30;
		} else {
			leftIndent += 15;
		}
		float fontSize = FontSizeTranslator.getInstance().getFontSize(t);
		leftIndent += css.get(CSS.Property.MARGIN_LEFT)!=null?utils.parseValueToPt(css.get(CSS.Property.MARGIN_LEFT),fontSize):0;
		leftIndent += css.get(CSS.Property.PADDING_LEFT)!=null?utils.parseValueToPt(css.get(CSS.Property.PADDING_LEFT),fontSize):0;
		lst.setIndentationLeft(leftIndent);
		return lst;
	}

}
