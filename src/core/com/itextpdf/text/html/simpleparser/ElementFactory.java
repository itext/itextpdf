/*
 * $Id: FactoryProperties.java 4610 2010-11-02 17:28:50Z blowagie $
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.text.html.simpleparser;

import java.util.Map;
import java.util.StringTokenizer;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementTags;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.Markup;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.HyphenationAuto;
import com.itextpdf.text.pdf.HyphenationEvent;
import com.itextpdf.text.pdf.draw.LineSeparator;
/**
 * Factory that produces iText Element objects,
 * based on tags and their properties.
 * @author blowagie
 * @author psoares
 */
public class ElementFactory {

	/**
	 * The font provider that will be used to fetch fonts.
	 * @since	iText 5.0	This used to be a FontFactoryImp
	 */
	private FontProvider provider = FontFactory.getFontImp();

	/**
	 * Creates a new instance of FactoryProperties.
	 */
	public ElementFactory() {
	}

	/**
	 * Setter for the font provider
	 * @param provider
	 * @since	5.0.6 renamed from setFontImp
	 */
	public void setFontProvider(FontProvider provider) {
		this.provider = provider;
	}
	
	/**
	 * Getter for the font provider
	 * @return provider
	 * @since 5.0.6 renamed from getFontImp
	 */
	public FontProvider getFontProvider() {
		return provider;
	}

	/**
	 * Creates a Font object based on a chain of properties.
	 * @param	chain	chain of properties
	 * @return	an iText Font object
	 */
	public Font getFont(ChainedProperties chain) {
		
		// [1] font name
		
		String face = chain.getProperty(ElementTags.FACE);
		// try again, under the CSS key.  
		//ISSUE: If both are present, we always go with face, even if font-family was  
		//  defined more recently in our ChainedProperties.  One solution would go like this: 
		//    Map all our supported style attributes to the 'normal' tag name, so we could   
		//    look everything up under that one tag, retrieving the most current value.
		if (face == null || face.trim().length() == 0) {
			face = chain.getProperty(Markup.CSS_KEY_FONTFAMILY);
		}
		// if the font consists of a comma separated list,
		// take the first font that is registered
		if (face != null) {
			StringTokenizer tok = new StringTokenizer(face, ",");
			while (tok.hasMoreTokens()) {
				face = tok.nextToken().trim();
				if (face.startsWith("\""))
					face = face.substring(1);
				if (face.endsWith("\""))
					face = face.substring(0, face.length() - 1);
				if (provider.isRegistered(face))
					break;
			}
		}
		
		// [2] encoding
		String encoding = chain.getProperty("encoding");
		if (encoding == null)
			encoding = BaseFont.WINANSI;
		
		// [3] embedded
		
		// [4] font size
		String value = chain.getProperty(ElementTags.SIZE);
		float size = 12;
		if (value != null)
			size = Float.parseFloat(value);
		
		// [5] font style
		int style = 0;
		
		// text-decoration
		String decoration = chain.getProperty(Markup.CSS_KEY_TEXTDECORATION);
		if (decoration != null && decoration.trim().length() != 0) {
		  if (Markup.CSS_VALUE_UNDERLINE.equals(decoration)) {
		    style |= Font.UNDERLINE;
		  } else if (Markup.CSS_VALUE_LINETHROUGH.equals(decoration)) {
		    style |= Font.STRIKETHRU;
		  }
		}
		// italic
		if (chain.hasProperty(HtmlTags.I))
			style |= Font.ITALIC;
		// bold
		if (chain.hasProperty(HtmlTags.B))
			style |= Font.BOLD;
		// underline
		if (chain.hasProperty(HtmlTags.U))
			style |= Font.UNDERLINE;
		// strikethru
		if (chain.hasProperty(HtmlTags.S))
			style |= Font.STRIKETHRU;
		
		// [6] Color
		BaseColor color = Markup.decodeColor(chain.getProperty("color"));
		
		// Get the font object from the provider
		return provider.getFont(face, encoding, true, size, style, color);
	}
	
	
	/**
	 * Creates an iText Chunk
	 * @param content the content of the Chunk
	 * @param chain the hierarchy chain
	 * @return a Chunk
	 */
	public Chunk createChunk(String content, ChainedProperties chain) {
		Font font = getFont(chain);
		Chunk ck = new Chunk(content, font);
		if (chain.hasProperty("sub"))
			ck.setTextRise(-font.getSize() / 2);
		else if (chain.hasProperty("sup"))
			ck.setTextRise(font.getSize() / 2);
		ck.setHyphenation(getHyphenation(chain));
		return ck;
	}

	/**
	 * Creates an iText Paragraph object using the properties
	 * of the different tags and properties in the hierarchy chain.
	 * @param	chain	the hierarchy chain
	 * @return	a Paragraph without any content
	 */
	public Paragraph createParagraph(ChainedProperties chain) {
		Paragraph paragraph = new Paragraph();
		updateElement(paragraph, chain);
		return paragraph;
	}

	/**
	 * Creates an iText Paragraph object using the properties
	 * of the different tags and properties in the hierarchy chain.
	 * @param	chain	the hierarchy chain
	 * @return	a ListItem without any content
	 */
	public ListItem createListItem(ChainedProperties chain) {
		ListItem item = new ListItem();
		updateElement(item, chain);
		return item;
	}
	
	/**
	 * Method that does the actual Element creating for
	 * the createParagraph and createListItem method.
	 * @param paragraph
	 * @param chain
	 */
	protected void updateElement(Paragraph paragraph, ChainedProperties chain) {
		// Alignment
		String value = chain.getProperty("align");
		paragraph.setAlignment(ElementTags.alignmentValue(value));
		// hyphenation
		paragraph.setHyphenation(getHyphenation(chain));
		// leading
		setParagraphLeading(paragraph, chain.getProperty("leading"));
		// spacing before
		value = chain.getProperty("before");
		if (value != null) {
			try {
				paragraph.setSpacingBefore(Float.parseFloat(value));
			} catch (Exception e) {
			}
		}
		// spacing after
		value = chain.getProperty("after");
		if (value != null) {
			try {
				paragraph.setSpacingAfter(Float.parseFloat(value));
			} catch (Exception e) {
			}
		}
		// extra paragraph space
		value = chain.getProperty("extraparaspace");
		if (value != null) {
			try {
				paragraph.setExtraParagraphSpace(Float.parseFloat(value));
			} catch (Exception e) {
			}
		}
		// indentation
		value = chain.getProperty("indent");
		if (value != null) {
			try {
				paragraph.setIndentationLeft(Float.parseFloat(value));
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Sets the leading of a Paragraph object.
	 * @param	paragraph	the Paragraph for which we set the leading
	 * @param	leading		the String value of the leading
	 */
	protected static void setParagraphLeading(Paragraph paragraph, String leading) {
		// default leading
		if (leading == null) {
			paragraph.setLeading(0, 1.5f);
			return;
		}
		try {
			StringTokenizer tk = new StringTokenizer(leading, " ,");
			// absolute leading
			String v = tk.nextToken();
			float v1 = Float.parseFloat(v);
			if (!tk.hasMoreTokens()) {
				paragraph.setLeading(v1, 0);
				return;
			}
			// relative leading
			v = tk.nextToken();
			float v2 = Float.parseFloat(v);
			paragraph.setLeading(v1, v2);
		} catch (Exception e) {
			// default leading
			paragraph.setLeading(0, 1.5f);
		}
	}


	/**
	 * Gets a HyphenationEvent based on the hyphenation entry in
	 * the hierarchy chain.
	 * @param	chain	the hierarchy chain
	 * @return	a HyphenationEvent
	 * @since	2.1.2
	 */
	public HyphenationEvent getHyphenation(ChainedProperties chain) {
		String value = chain.getProperty("hyphenation");
		// no hyphenation defined
		if (value == null || value.length() == 0) {
			return null;
		}
		// language code only
		int pos = value.indexOf('_');
		if (pos == -1) {
			return new HyphenationAuto(value, null, 2, 2);
		}
		// language and country code
		String lang = value.substring(0, pos);
		String country = value.substring(pos + 1);
		// no leftMin or rightMin
		pos = country.indexOf(',');
		if (pos == -1) {
			return new HyphenationAuto(lang, country, 2, 2);
		}
		// leftMin and rightMin value
		int leftMin;
		int rightMin = 2;
		value = country.substring(pos + 1);
		country = country.substring(0, pos);
		pos = value.indexOf(',');
		if (pos == -1) {
			leftMin = Integer.parseInt(value);
		} else {
			leftMin = Integer.parseInt(value.substring(0, pos));
			rightMin = Integer.parseInt(value.substring(pos + 1));
		}
		return new HyphenationAuto(lang, country, leftMin, rightMin);
	}
	
	/**
	 * Creates a LineSeparator.
	 * @since 5.0.6
	 */
	public LineSeparator createLineSeparator(Map<String, String> attrs, float offset) {
		// line thickness
		float lineWidth = 1;
		String size = attrs.get("size");
		if (size != null) {
			float tmpSize = Markup.parseLength(size, Markup.DEFAULT_FONT_SIZE);
			if (tmpSize > 0)
				lineWidth = tmpSize;
		}
		// width percentage
		String width = attrs.get("width");
		float percentage = 100;
		if (width != null) {
			float tmpWidth = Markup.parseLength(width, Markup.DEFAULT_FONT_SIZE);
			if (tmpWidth > 0) percentage = tmpWidth;
			if (!width.endsWith("%"))
				percentage = 100; // Treat a pixel width as 100% for now.
		}
		// line color
		BaseColor lineColor = null;
		// alignment
		int align = ElementTags.alignmentValue(attrs.get("align"));
		return new LineSeparator(lineWidth, percentage, lineColor, align, offset);
	}
}
