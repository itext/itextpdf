/*
 * $Id: SplitCharacter.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.text;

import com.itextpdf.text.pdf.PdfChunk;

/** Interface for customizing the split character.
 *
 * @author Paulo Soares
 */

public interface SplitCharacter {
    
    /**
     * Returns <CODE>true</CODE> if the character can split a line. The splitting implementation
     * is free to look ahead or look behind characters to make a decision.
     * <p>
     * The default implementation is:
     * <p>
     * <pre>
     * public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
     *    char c;
     *    if (ck == null)
     *        c = cc[current];
     *    else
     *        c = (char) ck[Math.min(current, ck.length - 1)].getUnicodeEquivalent(cc[current]);
     *    if (c <= ' ' || c == '-') {
     *        return true;
     *    }
     *    if (c < 0x2e80)
     *        return false;
     *    return ((c >= 0x2e80 && c < 0xd7a0)
     *    || (c >= 0xf900 && c < 0xfb00)
     *    || (c >= 0xfe30 && c < 0xfe50)
     *    || (c >= 0xff61 && c < 0xffa0));
     * }
     * </pre>
     * @param start the lower limit of <CODE>cc</CODE> inclusive
     * @param current the pointer to the character in <CODE>cc</CODE>
     * @param end the upper limit of <CODE>cc</CODE> exclusive
     * @param cc an array of characters at least <CODE>end</CODE> sized
     * @param ck an array of <CODE>PdfChunk</CODE>. The main use is to be able to call
     * {@link PdfChunk#getUnicodeEquivalent(int)}. It may be <CODE>null</CODE>
     * or shorter than <CODE>end</CODE>. If <CODE>null</CODE> no conversion takes place.
     * If shorter than <CODE>end</CODE> the last element is used
     * @return <CODE>true</CODE> if the character(s) can split a line
     */
    public boolean isSplitCharacter(int start, int current, int end, char cc[], PdfChunk ck[]);
}
