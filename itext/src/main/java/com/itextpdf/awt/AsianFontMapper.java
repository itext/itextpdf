/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.awt;

import java.awt.Font;

import com.itextpdf.text.pdf.BaseFont;

public class AsianFontMapper extends DefaultFontMapper {
	
	public static final String ChineseSimplifiedFont = "STSong-Light";
	public static final String ChineseSimplifiedEncoding_H = "UniGB-UCS2-H";
	public static final String ChineseSimplifiedEncoding_V = "UniGB-UCS2-V";
	
	public static final String ChineseTraditionalFont_MHei = "MHei-Medium";
	public static final String ChineseTraditionalFont_MSung = "MSung-Light";
	public static final String ChineseTraditionalEncoding_H = "UniCNS-UCS2-H";
	public static final String ChineseTraditionalEncoding_V = "UniCNS-UCS2-V";
	
	public static final String JapaneseFont_Go = "HeiseiKakuGo-W5";
	public static final String JapaneseFont_Min = "HeiseiMin-W3";
	public static final String JapaneseEncoding_H = "UniJIS-UCS2-H";
	public static final String JapaneseEncoding_V = "UniJIS-UCS2-V";
	public static final String JapaneseEncoding_HW_H = "UniJIS-UCS2-HW-H";
	public static final String JapaneseEncoding_HW_V = "UniJIS-UCS2-HW-V";
	
	public static final String KoreanFont_GoThic = "HYGoThic-Medium";
	public static final String KoreanFont_SMyeongJo = "HYSMyeongJo-Medium";
	public static final String KoreanEncoding_H = "UniKS-UCS2-H";
	public static final String KoreanEncoding_V = "UniKS-UCS2-V";
	
	private final String defaultFont;
	private final String encoding;

	public AsianFontMapper(String font, String encoding) {
		super();
		
		this.defaultFont = font;
		this.encoding = encoding;
	}

	public BaseFont awtToPdf(Font font) {
		try {
			BaseFontParameters p = getBaseFontParameters(font.getFontName());
			if (p != null){
				return BaseFont.createFont(p.fontName, p.encoding, p.embedded, p.cached, p.ttfAfm, p.pfb);
			}else{
				return BaseFont.createFont(defaultFont, encoding, true);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
