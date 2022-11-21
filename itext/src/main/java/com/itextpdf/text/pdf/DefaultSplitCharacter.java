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
package com.itextpdf.text.pdf;

import com.itextpdf.text.SplitCharacter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * The default class that is used to determine whether or not a character
 * is a split character.
 * <p/>
 * You can add an array of characters or a single character on which iText
 * should split the chunk. If custom characters have been set, iText will ignore
 * the default characters this class uses to split chunks.
 *
 * @since 2.1.2
 */
public class DefaultSplitCharacter implements SplitCharacter {

    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{2,4}-\\d{2}-\\d{2,4})");

    /**
     * An instance of the default SplitCharacter.
     */
    public static final SplitCharacter DEFAULT = new DefaultSplitCharacter();

    protected char[] characters;

    /**
     * Default constructor, has no custom characters to check.
     */
    public DefaultSplitCharacter() {
        // empty body
    }

    /**
     * Constructor with one splittable character.
     *
     * @param character char
     */
    public DefaultSplitCharacter(char character) {
        this(new char[]{character});
    }

    /**
     * Constructor with an array of splittable characters
     *
     * @param characters char[]
     */
    public DefaultSplitCharacter(char[] characters) {
        this.characters = characters;
    }

    /**
     * <p>
     * Checks if a character can be used to split a <CODE>PdfString</CODE>.
     * <p/>
     * <p>
     * The default behavior is that every character less than or equal to SPACE, the character '-'
     * and some specific unicode ranges are 'splitCharacters'.
     * </P>
     * <p/>
     * If custom splittable characters are set using the specified constructors,
     * then this class will ignore the default characters described in the
     * previous paragraph.
     * </P>
     *
     * @param start   start position in the array
     * @param current current position in the array
     * @param end     end position in the array
     * @param ck      chunk array
     * @param cc      the character array that has to be checked
     * @return <CODE>true</CODE> if the character can be used to split a string, <CODE>false</CODE> otherwise
     */
    public boolean isSplitCharacter(int start, int current, int end, char[] cc, PdfChunk[] ck) {
        char c = getCurrentCharacter(current, cc, ck);
        if (c == '-') {
            int beginDateidx = Math.max(current - 8, 0);
            int dateLength = Math.min(16, cc.length - beginDateidx);
            if (containsDate(String.valueOf(cc, beginDateidx, dateLength))) {
                return false;
            }
        }

        if (characters != null) {
            for (int i = 0; i < characters.length; i++) {
                if (c == characters[i]) {
                    return true;
                }
            }
            return false;
        }

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
     *
     * @param current current position in the array
     * @param ck      chunk array
     * @param cc      the character array that has to be checked
     * @return the current character
     */
    protected char getCurrentCharacter(int current, char[] cc, PdfChunk[] ck) {
        if (ck == null) {
            return cc[current];
        }
        return (char) ck[Math.min(current, ck.length - 1)].getUnicodeEquivalent(cc[current]);
    }

    private static boolean containsDate(String data) {
        Matcher m = DATE_PATTERN.matcher(data);
        return m.find();
    }
}
