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

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class expands a string into a list of numbers. The main use is to select a
 * range of pages.
 * <p>
 * The general syntax is:<br>
 * [!][o][odd][e][even]start-end
 * <p>
 * You can have multiple ranges separated by commas ','. The '!' modifier removes the
 * range from what is already selected. The range changes are incremental, that is,
 * numbers are added or deleted as the range appears. The start or the end, but not both, can be omitted.
 */
public class SequenceList {
    protected static final int COMMA = 1;
    protected static final int MINUS = 2;
    protected static final int NOT = 3;
    protected static final int TEXT = 4;
    protected static final int NUMBER = 5;
    protected static final int END = 6;
    protected static final char EOT = '\uffff';

    private static final int FIRST = 0;
    private static final int DIGIT = 1;
    private static final int OTHER = 2;
    private static final int DIGIT2 = 3;
    private static final String NOT_OTHER = "-,!0123456789";

    protected char text[];
    protected int ptr;
    protected int number;
    protected String other;

    protected int low;
    protected int high;
    protected boolean odd;
    protected boolean even;
    protected boolean inverse;

    protected SequenceList(String range) {
        ptr = 0;
        text = range.toCharArray();
    }

    protected char nextChar() {
        while (true) {
            if (ptr >= text.length)
                return EOT;
            char c = text[ptr++];
            if (c > ' ')
                return c;
        }
    }

    protected void putBack() {
        --ptr;
        if (ptr < 0)
            ptr = 0;
    }

    protected int getType() {
        StringBuffer buf = new StringBuffer();
        int state = FIRST;
        while (true) {
            char c = nextChar();
            if (c == EOT) {
                if (state == DIGIT) {
                    number = Integer.parseInt(other = buf.toString());
                    return NUMBER;
                }
                else if (state == OTHER) {
                    other = buf.toString().toLowerCase();
                    return TEXT;
                }
                return END;
            }
            switch (state) {
                case FIRST:
                    switch (c) {
                        case '!':
                            return NOT;
                        case '-':
                            return MINUS;
                        case ',':
                            return COMMA;
                    }
                    buf.append(c);
                    if (c >= '0' && c <= '9')
                        state = DIGIT;
                    else
                        state = OTHER;
                    break;
                case DIGIT:
                    if (c >= '0' && c <= '9')
                        buf.append(c);
                    else {
                        putBack();
                        number = Integer.parseInt(other = buf.toString());
                        return NUMBER;
                    }
                    break;
                case OTHER:
                    if (NOT_OTHER.indexOf(c) < 0)
                        buf.append(c);
                    else {
                        putBack();
                        other = buf.toString().toLowerCase();
                        return TEXT;
                    }
                    break;
            }
        }
    }

    private void otherProc() {
        if (other.equals("odd") || other.equals("o")) {
            odd = true;
            even = false;
        }
        else if (other.equals("even") || other.equals("e")) {
            odd = false;
            even = true;
        }
    }

    protected boolean getAttributes() {
        low = -1;
        high = -1;
        odd = even = inverse = false;
        int state = OTHER;
        while (true) {
            int type = getType();
            if (type == END || type == COMMA) {
                if (state == DIGIT)
                    high = low;
                return type == END;
            }
            switch (state) {
                case OTHER:
                    switch (type) {
                        case NOT:
                            inverse = true;
                            break;
                        case MINUS:
                            state = DIGIT2;
                            break;
                        default:
                            if (type == NUMBER) {
                                low = number;
                                state = DIGIT;
                            }
                            else
                                otherProc();
                            break;
                    }
                    break;
                case DIGIT:
                    switch (type) {
                        case NOT:
                            inverse = true;
                            state = OTHER;
                            high = low;
                            break;
                        case MINUS:
                            state = DIGIT2;
                            break;
                        default:
                            high = low;
                            state = OTHER;
                            otherProc();
                            break;
                    }
                    break;
                case DIGIT2:
                    switch (type) {
                        case NOT:
                            inverse = true;
                            state = OTHER;
                            break;
                        case MINUS:
                            break;
                        case NUMBER:
                            high = number;
                            state = OTHER;
                            break;
                        default:
                            state = OTHER;
                            otherProc();
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * Generates a list of numbers from a string.
     * @param ranges the comma separated ranges
     * @param maxNumber the maximum number in the range
     * @return a list with the numbers as <CODE>Integer</CODE>
     */
    public static List<Integer> expand(String ranges, int maxNumber) {
        SequenceList parse = new SequenceList(ranges);
        LinkedList<Integer> list = new LinkedList<Integer>();
        boolean sair = false;
        while (!sair) {
            sair = parse.getAttributes();
            if (parse.low == -1 && parse.high == -1 && !parse.even && !parse.odd)
                continue;
            if (parse.low < 1)
                parse.low = 1;
            if (parse.high < 1 || parse.high > maxNumber)
                parse.high = maxNumber;
            if (parse.low > maxNumber)
                parse.low = maxNumber;

            //System.out.println("low="+parse.low+",high="+parse.high+",odd="+parse.odd+",even="+parse.even+",inverse="+parse.inverse);
            int inc = 1;
            if (parse.inverse) {
                if (parse.low > parse.high) {
                    int t = parse.low;
                    parse.low = parse.high;
                    parse.high = t;
                }
                for (ListIterator<Integer> it = list.listIterator(); it.hasNext();) {
                    int n = it.next().intValue();
                    if (parse.even && (n & 1) == 1)
                        continue;
                    if (parse.odd && (n & 1) == 0)
                        continue;
                    if (n >= parse.low && n <= parse.high)
                        it.remove();
                }
            }
            else {
                if (parse.low > parse.high) {
                    inc = -1;
                    if (parse.odd || parse.even) {
                        --inc;
                        if (parse.even)
                            parse.low &= ~1;
                        else
                            parse.low -= (parse.low & 1) == 1 ? 0 : 1;
                    }
                    for (int k = parse.low; k >= parse.high; k += inc)
                        list.add(Integer.valueOf(k));
                }
                else {
                    if (parse.odd || parse.even) {
                        ++inc;
                        if (parse.odd)
                            parse.low |= 1;
                        else
                            parse.low += (parse.low & 1) == 1 ? 1 : 0;
                    }
                    for (int k = parse.low; k <= parse.high; k += inc) {
                        list.add(Integer.valueOf(k));
                    }
                }
            }
//            for (int k = 0; k < list.size(); ++k)
//                System.out.print(((Integer)list.get(k)).intValue() + ",");
//            System.out.println();
        }
        return list;
    }
}
