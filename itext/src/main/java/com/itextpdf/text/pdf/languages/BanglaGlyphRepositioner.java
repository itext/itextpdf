/*
 * $Id: FontsResourceAnchor.java 4784 2011-03-15 08:33:00Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, et al.
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
package com.itextpdf.text.pdf.languages;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.pdf.Glyph;

/**
 * 
 * @author <a href="mailto:paawak@gmail.com">Palash Ray</a>
 */
public class BanglaGlyphRepositioner extends IndicGlyphRepositioner {
	
	private static final String[] CHARCTERS_TO_BE_SHIFTED_LEFT_BY_1 = new String[] {
			"\u09BF", "\u09C7", "\u09C8"};
	
	private final Map<Integer, int[]> cmap31;
	private final Map<String, Glyph> glyphSubstitutionMap;

	public BanglaGlyphRepositioner(Map<Integer, int[]> cmap31, Map<String, Glyph> glyphSubstitutionMap) {
		this.cmap31 = cmap31;
		this.glyphSubstitutionMap = glyphSubstitutionMap;
	}
	
	@Override
	public void repositionGlyphs(List<Glyph> glyphList) {
		
		for (int i = 0; i < glyphList.size(); i++) {
			Glyph glyph = glyphList.get(i);
			
			if (glyph.chars.equals("\u09CB")) {
				handleOKaarAndOUKaar(i, glyphList, '\u09C7', '\u09BE');
			} else if (glyph.chars.equals("\u09CC")) {
				handleOKaarAndOUKaar(i, glyphList, '\u09C7', '\u09D7');
			}
		}
		
		super.repositionGlyphs(glyphList);
	}

	@Override
	public List<String> getCharactersToBeShiftedLeftByOnePosition() {
		return Arrays.asList(CHARCTERS_TO_BE_SHIFTED_LEFT_BY_1);
	}
	
	/**
	 * This is a dirty hack to display O-Kar (\u09cb) and Ou-Kar (\u09cc). Since this spans before and after 
	 * a Byanjan Borno like Ka (\u0995), the O-kar is split into two characters: the E-Kar (\u09C7) and the A-Kar (\u09BE).
	 * Similar the Ou-Kar is split into two characters: the E-Kar (\u09C7) and the char (\u09D7).
	 * 
	 */
	private void handleOKaarAndOUKaar(int currentIndex, List<Glyph> glyphList, char first, char second) {
        Glyph g1 = getGlyph(first);
        Glyph g2 = getGlyph(second);
        glyphList.set(currentIndex, g1);
        glyphList.add(currentIndex + 1, g2);
	}
	
	private Glyph getGlyph(char c) {
		
		Glyph glyph = glyphSubstitutionMap.get(String.valueOf(c));
		
		if (glyph != null) {
			return glyph;
		}
		
		int[] metrics = cmap31.get(Integer.valueOf(c));
        int glyphCode = metrics[0];
        int glyphWidth = metrics[1];
        return new Glyph(glyphCode, glyphWidth, String.valueOf(c));
	}

}
