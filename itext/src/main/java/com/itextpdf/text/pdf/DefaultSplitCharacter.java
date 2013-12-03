/*
 * $Id: DefaultSplitCharacter.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
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
package com.itextpdf.text.pdf;

import com.itextpdf.text.SplitCharacter;

/**
 * The default class that is used to determine whether or not a character
 * is a split character. You can subclass this class to define your own
 * split characters.
 * @since	2.1.2
 */
public class DefaultSplitCharacter implements SplitCharacter {
	
	/**
	 * An instance of the default SplitCharacter.
	 */
	public static final SplitCharacter DEFAULT = new DefaultSplitCharacter();
	
	/**
	 * Checks if a character can be used to split a <CODE>PdfString</CODE>.
	 * <P>
	 * for the moment every character less than or equal to SPACE, the character '-'
	 * and some specific unicode ranges are 'splitCharacters'.
	 * 
	 * @param start start position in the array
	 * @param current current position in the array
	 * @param end end position in the array
	 * @param	cc		the character array that has to be checked
	 * @param ck chunk array
	 * @return	<CODE>true</CODE> if the character can be used to split a string, <CODE>false</CODE> otherwise
	 */
    public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
        char c = getCurrentCharacter(current, cc, ck);
        if (c <= ' ' || c == '-' || c == '\u2010') {
            return true;
        }
        if (c < 0x2002)
            return false;
        return ((c >= 0x2002 && c <= 0x200b)
        || (c >= 0x2e80 && c < 0xd7a0)
        || (c >= 0xf900 && c < 0xfb00)
        || (c >= 0xfe30 && c < 0xfe50)
        || (c >= 0xff61 && c < 0xffa0));
    }

    /**
     * Returns the current character
	 * @param current current position in the array
	 * @param	cc		the character array that has to be checked
	 * @param ck chunk array
     * @return	the current character
     */
    protected char getCurrentCharacter(int current, char[] cc, PdfChunk[] ck) {
    	if (ck == null) {
    		return cc[current];
    	}
    	return (char)ck[Math.min(current, ck.length - 1)].getUnicodeEquivalent(cc[current]);
    }
}
