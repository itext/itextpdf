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

import com.itextpdf.text.SpecialSymbol;

/**
 * This class can produce String combinations representing a number built with
 * Greek letters (from alpha to omega, then alpha alpha, alpha beta, alpha gamma).
 * We are aware of the fact that the original Greek numbering is different;
 * See http://www.cogsci.indiana.edu/farg/harry/lan/grknum.htm#ancient
 * but this isn't implemented yet; the main reason being the fact that we
 * need a font that has the obsolete Greek characters qoppa and sampi.
 *
 * @since 2.0.7 (was called GreekNumberFactory in earlier versions)
 */
public class GreekAlphabetFactory {
	/**
	 * Changes an int into a lower case Greek letter combination.
	 * @param index the original number
	 * @return the letter combination
	 */
	public static final String getString(final int index) {
		return getString(index, true);
	}

	/**
	 * Changes an int into a lower case Greek letter combination.
	 * @param index the original number
	 * @return the letter combination
	 */
	public static final String getLowerCaseString(final int index) {
		return getString(index);
	}

	/**
	 * Changes an int into a upper case Greek letter combination.
	 * @param index the original number
	 * @return the letter combination
	 */
	public static final String getUpperCaseString(final int index) {
		return getString(index).toUpperCase();
	}

	/**
	 * Changes an int into a Greek letter combination.
	 * @param index the original number
	 * @param lowercase set to lowercase
	 * @return the letter combination
	 */
	public static final String getString(int index, final boolean lowercase) {
		if (index < 1) return "";
	    index--;

	    int bytes = 1;
	    int start = 0;
	    int symbols = 24;
	   	while(index >= symbols + start) {
	   		bytes++;
	   	    start += symbols;
	   		symbols *= 24;
	   	}

	   	int c = index - start;
	   	char[] value = new char[bytes];
	   	while(bytes > 0) {
	   		bytes--;
	   		value[bytes] = (char)(c % 24);
	   		if (value[bytes] > 16) value[bytes]++;
	   		value[bytes] += (lowercase ? 945 : 913);
	   		value[bytes] = SpecialSymbol.getCorrespondingSymbol(value[bytes]);
	   		c /= 24;
	   	}

	   	return String.valueOf(value);
	}
}
