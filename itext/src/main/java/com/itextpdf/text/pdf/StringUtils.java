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

import com.itextpdf.text.DocWriter;

public class StringUtils {

    final static private byte[] r = DocWriter.getISOBytes("\\r");
    final static private byte[] n = DocWriter.getISOBytes("\\n");
    final static private byte[] t = DocWriter.getISOBytes("\\t");
    final static private byte[] b = DocWriter.getISOBytes("\\b");
    final static private byte[] f = DocWriter.getISOBytes("\\f");

    private StringUtils() {

    }

    /**
     * Escapes a <CODE>byte</CODE> array according to the PDF conventions.
     *
     * @param bytes the <CODE>byte</CODE> array to escape
     * @return an escaped <CODE>byte</CODE> array
     */
    public static byte[] escapeString(final byte bytes[]) {
        ByteBuffer content = new ByteBuffer();
        escapeString(bytes, content);
        return content.toByteArray();
    }

    /**
     * Escapes a <CODE>byte</CODE> array according to the PDF conventions.
     *
     * @param bytes the <CODE>byte</CODE> array to escape
     * @param content the content
     */
    public static void escapeString(final byte bytes[], final ByteBuffer content) {
        content.append_i('(');
        for (int k = 0; k < bytes.length; ++k) {
            byte c = bytes[k];
            switch (c) {
                case '\r':
                    content.append(r);
                    break;
                case '\n':
                    content.append(n);
                    break;
                case '\t':
                    content.append(t);
                    break;
                case '\b':
                    content.append(b);
                    break;
                case '\f':
                    content.append(f);
                    break;
                case '(':
                case ')':
                case '\\':
                    content.append_i('\\').append_i(c);
                    break;
                default:
                    content.append_i(c);
            }
        }
        content.append_i(')');
    }

    
    /**
     * Converts an array of unsigned 16bit numbers to an array of bytes.
     * The input values are presented as chars for convenience.
     * 
     * @param chars the array of 16bit numbers that should be converted
     * @return the resulting byte array, twice as large as the input
     */
    public static byte[] convertCharsToBytes(char[] chars) {
        byte[] result = new byte[chars.length*2];
        for (int i=0; i<chars.length;i++) {
            result[2*i] = (byte) (chars[i] / 256);
            result[2*i+1] = (byte) (chars[i] % 256);
        }
        return result;
    }
}
