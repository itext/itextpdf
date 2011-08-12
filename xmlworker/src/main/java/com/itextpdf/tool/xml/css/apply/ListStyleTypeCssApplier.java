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

import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.GreekList;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.RomanList;
import com.itextpdf.text.ZapfDingbatsList;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.net.ImageRetrieve;
import com.itextpdf.tool.xml.net.exc.NoImageException;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.NoImageProviderException;

/**
 * @author itextpdf.com
 *
 */
public class ListStyleTypeCssApplier {

	private final CssUtils utils = CssUtils.getInstance();
	private static final Logger LOG = LoggerFactory.getLogger(ListStyleTypeCssApplier.class);

	/**
	 *
	 */
	public ListStyleTypeCssApplier() {
	}

	/**
	 * The ListCssApplier has the capabilities to change the type of the given {@link List} dependable on the css.
	 * This means: <strong>Always replace your list with the returned one and add content to the list after applying!</strong>
	 * @param list the list to style
	 * @param t the tag
	 * @param htmlPipelineContext the context
	 * @return the changed {@link List}
	 */
	// not implemented: list-style-type:armenian, georgian, decimal-leading-zero.
	public List apply(final List list, final Tag t, final HtmlPipelineContext htmlPipelineContext) {
		float fontSize = FontSizeTranslator.getInstance().getFontSize(t);
		List lst = list;
		Map<String, String> css = t.getCSS();
		String styleType = css.get(CSS.Property.LIST_STYLE_TYPE);
		if (null != styleType) {
			if (styleType.equalsIgnoreCase(CSS.Value.NONE)) {
				lst.setLettered(false);
				lst.setNumbered(false);
				lst.setListSymbol("");
			} else if (CSS.Value.DECIMAL.equalsIgnoreCase(styleType)) {
				lst = new List(List.ORDERED);
				synchronizeSymbol(fontSize, lst);
			} else if (CSS.Value.DISC.equalsIgnoreCase(styleType)) {
				lst = new ZapfDingbatsList(108);
				shrinkSymbol(lst, fontSize);
			} else if (CSS.Value.SQUARE.equalsIgnoreCase(styleType)) {
				lst = new ZapfDingbatsList(110);
				shrinkSymbol(lst, fontSize);
			} else if (CSS.Value.CIRCLE.equalsIgnoreCase(styleType)) {
				lst = new ZapfDingbatsList(109);
				shrinkSymbol(lst, fontSize);
			} else if (CSS.Value.LOWER_ROMAN.equals(styleType)) {
				lst = new RomanList(true, 0);
				synchronizeSymbol(fontSize, lst);
			} else if (CSS.Value.UPPER_ROMAN.equals(styleType)) {
				lst = new RomanList(false, 0);
				synchronizeSymbol(fontSize, lst);
			} else if (CSS.Value.LOWER_GREEK.equals(styleType)) {
				lst = new GreekList(true, 0);
				synchronizeSymbol(fontSize, lst);
			} else if (CSS.Value.UPPER_GREEK.equals(styleType)) {
				lst = new GreekList(false, 0);
				synchronizeSymbol(fontSize, lst);
			} else if (CSS.Value.LOWER_ALPHA.equals(styleType) || CSS.Value.LOWER_LATIN.equals(styleType)) {
				lst = new List(List.ORDERED, List.ALPHABETICAL);
				synchronizeSymbol(fontSize, lst);
				lst.setLowercase(true);
			} else if (CSS.Value.UPPER_ALPHA.equals(styleType) || CSS.Value.UPPER_LATIN.equals(styleType)) {
				lst = new List(List.ORDERED, List.ALPHABETICAL);
				synchronizeSymbol(fontSize, lst);
				lst.setLowercase(false);
			}
		} else if (t.getName().equalsIgnoreCase(HTML.Tag.OL)) {
			lst = new List(List.ORDERED);
			synchronizeSymbol(fontSize, lst);
		} else if (t.getName().equalsIgnoreCase(HTML.Tag.UL)) {
			lst = new List(List.UNORDERED);
			shrinkSymbol(lst, fontSize);
		}
		if (null != css.get(CSS.Property.LIST_STYLE_IMAGE)
				&& !css.get(CSS.Property.LIST_STYLE_IMAGE).equalsIgnoreCase(CSS.Value.NONE)) {
			lst = new List();
			String url = utils.extractUrl(css.get(CSS.Property.LIST_STYLE_IMAGE));
			Image img = null;
			try {
				if (htmlPipelineContext == null) {
					img = new ImageRetrieve().retrieveImage(url);
				} else {
					try {
						img = new ImageRetrieve(htmlPipelineContext.getImageProvider()).retrieveImage(url);
					} catch (NoImageProviderException e) {
						if (LOG.isLogging(Level.TRACE)) {
							LOG.trace(String.format(LocaleMessages.getInstance().getMessage("pipeline.html.noimageprovider"), htmlPipelineContext.getClass().getName()));
						}
						img = new ImageRetrieve().retrieveImage(url);
					}
				}
				lst.setListSymbol(new Chunk(img, 0, 0, false));
				lst.setSymbolIndent(img.getWidth());
				if (LOG.isLogging(Level.TRACE)) {
					LOG.trace(String.format(LocaleMessages.getInstance().getMessage("html.tag.list"), url));
				}
			} catch (IOException e) {
				if (LOG.isLogging(Level.ERROR)) {
					LOG.error(String.format(LocaleMessages.getInstance().getMessage("html.tag.list.failed"), url), e);
				}
				lst = new List(List.UNORDERED);
			} catch (NoImageException e) {
				if (LOG.isLogging(Level.ERROR)) {
					LOG.error(e.getLocalizedMessage(), e);
				}
				lst = new List(List.UNORDERED);
			}
		}
		lst.setAlignindent(false);
		lst.setAutoindent(false);
		float leftIndent = 0;
		if (null != css.get(CSS.Property.LIST_STYLE_POSITION) && css.get(CSS.Property.LIST_STYLE_POSITION).equalsIgnoreCase(CSS.Value.INSIDE)) {
			leftIndent += 30;
		} else {
			leftIndent += 15;
		}
		leftIndent += css.get(CSS.Property.MARGIN_LEFT)!=null?utils.parseValueToPt(css.get(CSS.Property.MARGIN_LEFT),fontSize):0;
		leftIndent += css.get(CSS.Property.PADDING_LEFT)!=null?utils.parseValueToPt(css.get(CSS.Property.PADDING_LEFT),fontSize):0;
		lst.setIndentationLeft(leftIndent);
		return lst;
	}

	private void synchronizeSymbol(final float fontSize, final List lst) {
		lst.getSymbol().getFont().setSize(fontSize);
		lst.setSymbolIndent(fontSize);
	}

	private void shrinkSymbol(final List lst, final float fontSize) {
		lst.setSymbolIndent(12);
		Chunk symbol = lst.getSymbol();
		symbol.setTextRise(2);
		symbol.getFont().setSize(7);
	}
}
