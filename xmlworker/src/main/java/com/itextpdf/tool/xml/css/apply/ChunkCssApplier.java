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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssApplier;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.css.FontSizeTranslator;

/**
 * Applies CSS Rules to Chunks
 *
 */
public class ChunkCssApplier implements CssApplier<Chunk> {
	/**
	 * FF4 and IE8 provide normal text and bold text. All other values are translated to one of these 2 styles <br />
	 * 100 - 500 and "lighter" = normal.<br />
	 * 600 - 900 and "bolder" = bold.
	 */
	public static final List<String> BOLD = Arrays.asList(new String[] { "bold", "bolder", "600", "700", "800", "900" });
	private final CssUtils utils = CssUtils.getInstance();

	 /*
     * (non-Javadoc)
     *
     * @see
     * com.itextpdf.tool.xml.css.CssApplier#apply(com.itextpdf.text.Element,
     * com.itextpdf.tool.xml.Tag)
     */
	public Chunk apply(final Chunk c, final Tag t) {
		String fontName = BaseFont.HELVETICA;
		String encoding = BaseFont.CP1252;
		float size = new FontSizeTranslator().getFontSize(t);
		int style = Font.NORMAL;
		BaseColor color = BaseColor.BLACK;
		Map<String, String> rules = t.getCSS();
		for (Entry<String, String> entry : rules.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			if (CSS.Property.FONT_WEIGHT.equalsIgnoreCase(key) && CSS.Value.BOLD.contains(value)) {
				if (style == Font.ITALIC)
					style = Font.BOLDITALIC;
				else
					style = Font.BOLD;
			} else if (CSS.Property.FONT_STYLE.equalsIgnoreCase(key)) {
				if (value.equalsIgnoreCase(CSS.Value.ITALIC)) {
					if (style == Font.BOLD)
						style = Font.BOLDITALIC;
					else
						style = Font.ITALIC;
				}
				if (value.equalsIgnoreCase(CSS.Value.OBLIQUE)) {
					c.setSkew(0, 12);
				}
			} else if (CSS.Property.FONT_FAMILY.equalsIgnoreCase(key)) {
				if(value.contains(",")){
					String[] fonts = value.split(",");
					for(String s: fonts) {
						s = s.trim();
						if(!FontFactory.getFont(s).getFamilyname().equalsIgnoreCase("unknown")){
							fontName = s;
							break;
						}
					}
				} else {
					fontName = value;
				}
			} else if (CSS.Property.COLOR.equalsIgnoreCase(key)) {
				color = HtmlUtilities.decodeColor(value);
			} else if (CSS.Property.LETTER_SPACING.equalsIgnoreCase(key)) {
				c.setCharacterSpacing(utils.parsePxInCmMmPcToPt(value));
			} else if (null != rules.get(CSS.Property.XFA_FONT_HORIZONTAL_SCALE)) { // only % allowed; need a catch block NumberFormatExc?
				c.setHorizontalScaling(Float.parseFloat(rules.get(CSS.Property.XFA_FONT_HORIZONTAL_SCALE).replace("%", ""))/100);
			}
		}
		// following styles are separate from the for each loop, because they are based on font settings like size.
		if (null != rules.get(CSS.Property.VERTICAL_ALIGN)) {
			String value = rules.get(CSS.Property.VERTICAL_ALIGN);
			if (value.equalsIgnoreCase(CSS.Value.SUPER)||value.equalsIgnoreCase(CSS.Value.TOP)||value.equalsIgnoreCase(CSS.Value.TEXT_TOP)) {
				c.setTextRise((float) (size / 2 + 0.5));
			} else if (value.equalsIgnoreCase(CSS.Value.SUB)||value.equalsIgnoreCase(CSS.Value.BOTTOM)||value.equalsIgnoreCase(CSS.Value.TEXT_BOTTOM)) {
				c.setTextRise(-size / 2);
			} else {
				c.setTextRise(utils.parsePxInCmMmPcToPt(value));
			}
		}
		String xfaVertScale = rules.get(CSS.Property.XFA_FONT_VERTICAL_SCALE);
		if (null != xfaVertScale) { // only % allowed; need a catch block NumberFormatExc?
			if(xfaVertScale.contains("%")) {
				size *= Float.parseFloat(xfaVertScale.replace("%", ""))/100;
				c.setHorizontalScaling(100/Float.parseFloat(xfaVertScale.replace("%", "")));
			}
		}
		if (null != rules.get(CSS.Property.TEXT_DECORATION)) { // Restriction? In html a underline and a line-through is possible on one piece of text. A Chunk can set an underline only once.
			String value = rules.get(CSS.Property.TEXT_DECORATION);
			if (CSS.Value.UNDERLINE.equalsIgnoreCase(value)) {
				c.setUnderline(0.75f, -size/8f);
			}
			if (CSS.Value.LINE_THROUGH.equalsIgnoreCase(value)) {
				c.setUnderline(0.75f, size/4f);
			}
		}
		if (null != rules.get(CSS.Property.BACKGROUND_COLOR)) {
			c.setBackground(HtmlUtilities.decodeColor(rules.get(CSS.Property.BACKGROUND_COLOR)));
		}
		Font f  = FontFactory.getFont(fontName, encoding, BaseFont.EMBEDDED, size, style, color);
		c.setFont(f);
		return c;
	}
	/**
	 * Method used for retrieving the widest word of a chunk of text. All styles of the chunk will be taken into account when calculating the width of the words.
	 * @param c chunk of which the widest word is required.
	 * @return float containing the width of the widest word.
	 */
	public float getWidestWord(final Chunk c) {
		String[] words = c.getContent().split("\\s");
		float widestWord = 0;
		for(int i = 0; i<words.length ; i++) {
			Chunk word = new Chunk(words[i]);
			copyChunkStyles(c, word);
			if(word.getWidthPoint() > widestWord) {
				widestWord = word.getWidthPoint();
			}
		}
		return widestWord;
	}
	/**
	 * Method used for copying styles from one chunk to another. Could be deprecated if the content of a chunk can be overwritten.
	 * @param source chunk which contains the required styles.
	 * @param target chunk which needs the required styles.
	 */
	public void copyChunkStyles(final Chunk source, final Chunk target) {
		target.setFont(source.getFont());
		target.setAttributes(source.getAttributes());
		target.setCharacterSpacing(source.getCharacterSpacing());
		target.setHorizontalScaling(source.getHorizontalScaling());
		target.setHorizontalScaling(source.getHorizontalScaling());
	}
}
