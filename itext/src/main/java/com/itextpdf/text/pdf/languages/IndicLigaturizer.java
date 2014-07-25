/*
 * $Id: IndicLigaturizer.java 5561 2012-11-22 16:22:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Ram Narayan, Bruno Lowagie, et al.
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
package com.itextpdf.text.pdf.languages;

/**
 * Superclass for processors that can convert a String of bytes in an Indic
 * language to a String in the same language of which the bytes are reordered
 * for rendering using a font that contains the necessary glyphs.
 */
public abstract class IndicLigaturizer implements LanguageProcessor {

	// Hashtable Indexes
	public static final int MATRA_AA = 0;
	public static final int MATRA_I = 1;
	public static final int MATRA_E = 2;
	public static final int MATRA_AI = 3;
	public static final int MATRA_HLR = 4;
	public static final int MATRA_HLRR = 5;
	public static final int LETTER_A = 6;
	public static final int LETTER_AU = 7;
	public static final int LETTER_KA = 8;
	public static final int LETTER_HA = 9;
	public static final int HALANTA = 10;

	/**
	 * The table mapping specific character indexes to the characters in a
	 * specific language.
	 */
	protected char[] langTable;

	/**
	 * Reorders the bytes in a String making Indic ligatures
	 * 
	 * @param s
	 *            the original String
	 * @return the ligaturized String
	 */
	public String process(String s) {
		if (s == null || s.length() == 0)
			return "";
		StringBuilder res = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char letter = s.charAt(i);

			if (IsVyanjana(letter) || IsSwaraLetter(letter)) {
				res.append(letter);
			} else if (IsSwaraMatra(letter)) {
				int prevCharIndex = res.length() - 1;

				if (prevCharIndex >= 0) {
					// a Halanta followed by swara matra, causes it to lose its
					// identity
					if (res.charAt(prevCharIndex) == langTable[HALANTA])
						res.deleteCharAt(prevCharIndex);

					res.append(letter);
					int prevPrevCharIndex = res.length() - 2;

					if (letter == langTable[MATRA_I] && prevPrevCharIndex >= 0)
						swap(res, prevPrevCharIndex, res.length() - 1);
				} else {
					res.append(letter);
				}
			} else {
				res.append(letter);
			}
		}

		return res.toString();
	}

	/**
	 * Indic languages are written from right to left.
	 * 
	 * @return false
	 * @see com.itextpdf.text.pdf.languages.LanguageProcessor#isRTL()
	 */
	public boolean isRTL() {
		return false;
	}

	/**
	 * Checks if a character is vowel letter.
	 * 
	 * @param ch
	 *            the character that needs to be checked
	 * @return true if the characters is a vowel letter
	 */
	protected boolean IsSwaraLetter(char ch) {
		return (ch >= langTable[LETTER_A] && ch <= langTable[LETTER_AU]);
	}

	/**
	 * Checks if a character is vowel sign.
	 * 
	 * @param ch
	 *            the character that needs to be checked
	 * @return true if the characters is a vowel sign
	 */
	protected boolean IsSwaraMatra(char ch) {
		return ((ch >= langTable[MATRA_AA] && ch <= langTable[MATRA_AI])
				|| ch == langTable[MATRA_HLR] || ch == langTable[MATRA_HLRR]);
	}

	/**
	 * Checks if a character is consonant letter.
	 * 
	 * @param ch
	 *            the character that needs to be checked
	 * @return true if the chracter is a consonant letter
	 */
	protected boolean IsVyanjana(char ch) {
		return (ch >= langTable[LETTER_KA] && ch <= langTable[LETTER_HA]);
	}

	/**
	 * Swaps two characters in a StringBuilder object
	 * 
	 * @param s
	 *            the StringBuilder
	 * @param i
	 *            the index of one character
	 * @param j
	 *            the index of the other character
	 */
	private static void swap(StringBuilder s, int i, int j) {
		char temp = s.charAt(i);
		s.setCharAt(i, s.charAt(j));
		s.setCharAt(j, temp);
	}
}
