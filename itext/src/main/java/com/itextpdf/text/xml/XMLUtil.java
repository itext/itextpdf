/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, Balder Van Camp, et al.
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
package com.itextpdf.text.xml;

/**
 * Contains utility methods for XML.
 * @author Balder
 * @since 5.0.6
 *
 */
public class XMLUtil {

	 /**
     * Escapes a string with the appropriated XML codes.
     * @param s the string to be escaped
     * @param onlyASCII codes above 127 will always be escaped with &amp;#nn; if <CODE>true</CODE>
     * @return the escaped string
     * @since 5.0.6
     */
    public static String escapeXML(final String s, final boolean onlyASCII) {
        char cc[] = s.toCharArray();
        int len = cc.length;
        StringBuffer sb = new StringBuffer();
        for (int k = 0; k < len; ++k) {
            int c = cc[k];
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '\'':
                    sb.append("&apos;");
                    break;
                default:
                	if (isValidCharacterValue(c)) {
                		if (onlyASCII && c > 127)
                			sb.append("&#").append(c).append(';');
                		else
                			sb.append((char)c);
                	}
            }
        }
        return sb.toString();
    }

    /**
     * Unescapes a String, replacing &#nn;, &lt;, &gt;, &amp;, &quot;,
     * and &apos to the corresponding characters.
     * @param s	a String with entities
     * @return the unescaped string
     */
    public static String unescapeXML(final String s) {
        char[] cc = s.toCharArray();
        int len = cc.length;
        StringBuffer sb = new StringBuffer();
        int pos;
        String esc;
        for (int i = 0; i < len; i++) {
            int c = cc[i];
            if (c == '&') {
            	pos = findInArray(';', cc, i + 3);
            	if (pos > -1) {
                	esc = new String(cc, i + 1, pos - i - 1);
                	if (esc.startsWith("#")) {
                		esc = esc.substring(1);
                		if (isValidCharacterValue(esc)) {
                			c = (char)Integer.parseInt(esc);
                			i = pos;
                		} else {
                            i = pos;
                            continue;
                        }
                	}
                	else {
                		int tmp = unescape(esc);
                		if (tmp > 0) {
                			c = tmp;
                			i = pos;
                		}
                	}
            	}
            }
			sb.append((char)c);
        }
        return sb.toString();
    }
 
    /**
     * Unescapes 'lt', 'gt', 'apos', 'quote' and 'amp' to the
     * corresponding character values.
     * @param	s	a string representing a character
     * @return	a character value
     */
    public static int unescape(String s) {
    	if ("apos".equals(s))
    		return '\'';
    	if ("quot".equals(s))
    		return '"';
    	if ("lt".equals(s))
    		return '<';
    	if ("gt".equals(s))
    		return '>';
    	if ("amp".equals(s))
    		return '&';
    	return -1;
    }
    
    /**
     * Checks if a character value should be escaped/unescaped.
     * @param	s	the String representation of an integer
     * @return	true if it's OK to escape or unescape this value 
     */
    public static boolean isValidCharacterValue(String s) {
    	try {
    		int i = Integer.parseInt(s);
    		return isValidCharacterValue(i);
    	}
    	catch (NumberFormatException nfe) {
    		return false;
    	}
    }
    
    /**
     * Checks if a character value should be escaped/unescaped.
     * @param	c	a character value
     * @return	true if it's OK to escape or unescape this value 
     */
    public static boolean isValidCharacterValue(int c) {
    	return (c == 0x9 || c == 0xA || c == 0xD
    			|| c >= 0x20 && c <= 0xD7FF
    			|| c >= 0xE000 && c <= 0xFFFD
    			|| c >= 0x10000 && c <= 0x10FFFF);
    }
    
    /**
     * Looks for a character in a character array, starting from a certain position
     * @param needle	the character you're looking for
     * @param haystack	the character array
     * @param start		the start position
     * @return	the position where the character was found, or -1 if it wasn't found.
     */
    public static int findInArray(char needle, char[] haystack, int start) {
    	for (int i = start; i < haystack.length; i++) {
    		if (haystack[i] == ';')
    			return i;
    	}
    	return -1;
    }
    
    /**
     * Unescapes a string, replacing &amp;lt;, &amp;gt;, &amp;amp;, &amp;apos;, &amp;quot;
     * and and &amp;#nn; by the approriate characters.
     * @param s the string to be unescaped
     * @return the unescaped string
     * @since 5.1.3
     */

    /**
     * Returns the IANA encoding name that is auto-detected from
     * the bytes specified, with the endian-ness of that encoding where appropriate.
     * (method found in org.apache.xerces.impl.XMLEntityManager, originally published
     * by the Apache Software Foundation under the Apache Software License; now being
     * used in iText under the MPL)
     * @param b4    The first four bytes of the input.
     * @return an IANA-encoding string
     * @since 5.0.6
     */
    public static String getEncodingName(final byte[] b4) {

        // UTF-16, with BOM
        int b0 = b4[0] & 0xFF;
        int b1 = b4[1] & 0xFF;
        if (b0 == 0xFE && b1 == 0xFF) {
            // UTF-16, big-endian
            return "UTF-16BE";
        }
        if (b0 == 0xFF && b1 == 0xFE) {
            // UTF-16, little-endian
            return "UTF-16LE";
        }

        // UTF-8 with a BOM
        int b2 = b4[2] & 0xFF;
        if (b0 == 0xEF && b1 == 0xBB && b2 == 0xBF) {
            return "UTF-8";
        }

        // other encodings
        int b3 = b4[3] & 0xFF;
        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x00 && b3 == 0x3C) {
            // UCS-4, big endian (1234)
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x3C && b1 == 0x00 && b2 == 0x00 && b3 == 0x00) {
            // UCS-4, little endian (4321)
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x00 && b1 == 0x00 && b2 == 0x3C && b3 == 0x00) {
            // UCS-4, unusual octet order (2143)
            // REVISIT: What should this be?
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x00) {
            // UCS-4, unusual octet order (3412)
            // REVISIT: What should this be?
            return "ISO-10646-UCS-4";
        }
        if (b0 == 0x00 && b1 == 0x3C && b2 == 0x00 && b3 == 0x3F) {
            // UTF-16, big-endian, no BOM
            // (or could turn out to be UCS-2...
            // REVISIT: What should this be?
            return "UTF-16BE";
        }
        if (b0 == 0x3C && b1 == 0x00 && b2 == 0x3F && b3 == 0x00) {
            // UTF-16, little-endian, no BOM
            // (or could turn out to be UCS-2...
            return "UTF-16LE";
        }
        if (b0 == 0x4C && b1 == 0x6F && b2 == 0xA7 && b3 == 0x94) {
            // EBCDIC
            // a la xerces1, return CP037 instead of EBCDIC here
            return "CP037";
        }

        // default encoding
        return "UTF-8";
    }
}
