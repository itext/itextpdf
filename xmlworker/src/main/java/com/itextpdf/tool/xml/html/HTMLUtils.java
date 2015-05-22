/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.html;

import com.itextpdf.text.Chunk;

import java.util.ArrayList;
import java.util.List;

/**
 * @author redlab_b
 *
 */
public class HTMLUtils {

	/**
	 * @param str the string to sanitize
	 * @param preserveWhiteSpace to trim or not to trim
	 * @return sanitized string
	 */
	private static List<Chunk> sanitize(final String str, final boolean preserveWhiteSpace, final boolean replaceNonBreakableSpaces) {
		StringBuilder builder = new StringBuilder();
        StringBuilder whitespaceBuilder = new StringBuilder();
		char[] chars = str.toCharArray();
        ArrayList<Chunk> chunkList = new ArrayList<Chunk>();
		boolean isWhitespace = chars.length > 0 ? Character.isWhitespace(chars[0]) : true;
		for (char c : chars) {
			if (isWhitespace && !Character.isWhitespace(c)) {
                if (builder.length() == 0) {
                    chunkList.add(Chunk.createWhitespace(whitespaceBuilder.toString(), preserveWhiteSpace));
                } else {
                    builder.append(preserveWhiteSpace ? whitespaceBuilder : " ");
                }
                whitespaceBuilder = new StringBuilder();
            }

            isWhitespace = Character.isWhitespace(c);
            if (isWhitespace) {
                whitespaceBuilder.append(c);
            } else {
                builder.append(c);
            }
		}

        if (builder.length() > 0) {
            chunkList.add(new Chunk(replaceNonBreakableSpaces ? builder.toString().replace(Character.valueOf('\u00a0'), Character.valueOf(' ')) : builder.toString()));
        }

        if (whitespaceBuilder.length() > 0) {
            chunkList.add(Chunk.createWhitespace(whitespaceBuilder.toString(), preserveWhiteSpace));
        }

		return chunkList;
	}

    public static List<Chunk> sanitize(final String str, final boolean preserveWhiteSpace) {
        return sanitize(str, preserveWhiteSpace, false);
    }
	/**
	 * Sanitize the String for use in in-line tags.
	 * @param str the string to sanitize
	 * @return a sanitized String for use in in-line tags
	 */
	public static List<Chunk> sanitizeInline(final String str, final boolean preserveWhiteSpace) {
		return sanitize(str, preserveWhiteSpace, false);
	}

    public static List<Chunk> sanitizeInline(final String str, final boolean preserveWhiteSpace, final boolean replaceNonBreakableSpaces) {
		return sanitize(str, preserveWhiteSpace, replaceNonBreakableSpaces);
	}
}
