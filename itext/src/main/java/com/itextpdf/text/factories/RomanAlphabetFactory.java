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
package com.itextpdf.text.factories;

import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * This class can produce String combinations representing a number.
 * "a" to "z" represent 1 to 26, "AA" represents 27, "AB" represents 28,
 * and so on; "ZZ" is followed by "AAA".
 */
public class RomanAlphabetFactory {

	/**
	 * Translates a positive integer (not equal to zero)
	 * into a String using the letters 'a' to 'z';
	 * 1 = a, 2 = b, ..., 26 = z, 27 = aa, 28 = ab,...
	 * @param index the index
	 * @return a translated string
	 */
	public static final String getString(int index) {
    	if (index < 1) throw new NumberFormatException(MessageLocalization.getComposedMessage("you.can.t.translate.a.negative.number.into.an.alphabetical.value"));

    	index--;
    	int bytes = 1;
    	int start = 0;
    	int symbols = 26;
    	while(index >= symbols + start) {
    		bytes++;
    	    start += symbols;
    		symbols *= 26;
    	}

    	int c = index - start;
    	char[] value = new char[bytes];
    	while(bytes > 0) {
    		value[--bytes] = (char)( 'a' + (c % 26));
    		c /= 26;
    	}

    	return new String(value);
	}

	/**
	 * Translates a positive integer (not equal to zero)
	 * into a String using the letters 'a' to 'z';
	 * 1 = a, 2 = b, ..., 26 = z, 27 = aa, 28 = ab,...
	 * @param index the int to translate
	 * @return the string in lowercase
	 */
	public static final String getLowerCaseString(final int index) {
		return getString(index);
	}

	/**
	 * Translates a positive integer (not equal to zero)
	 * into a String using the letters 'A' to 'Z';
	 * 1 = A, 2 = B, ..., 26 = Z, 27 = AA, 28 = AB,...
	 * @param index the int to translate
	 * @return the string in uppercase
	 */
	public static final String getUpperCaseString(final int index) {
		return getString(index).toUpperCase();
	}


	/**
	 * Translates a positive integer (not equal to zero)
	 * into a String using the letters 'a' to 'z'
	 * (a = 1, b = 2, ..., z = 26, aa = 27, ab = 28,...).
	 * @param index the int to translate
	 * @param lowercase true if the string sshould be lowercase false otherwise
	 * @return the translated string
	 */
	public static final String getString(final int index, final boolean lowercase) {
		if (lowercase) {
			return getLowerCaseString(index);
		}
		else {
			return getUpperCaseString(index);
		}
	}
}
