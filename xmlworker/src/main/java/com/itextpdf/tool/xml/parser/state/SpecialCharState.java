/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.tool.xml.parser.state;

import com.itextpdf.text.xml.simpleparser.EntitiesToUnicode;
import com.itextpdf.tool.xml.parser.State;
import com.itextpdf.tool.xml.parser.XMLParser;

/**
 * @author redlab_b
 *
 */
public class SpecialCharState implements State {

	private final XMLParser parser;

	/**
	 * @param parser the XMLParser
	 */
	public SpecialCharState(final XMLParser parser) {
		this.parser =parser;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.parser.State#process(int)
	 */
	public void process(final char character) {
		StringBuilder entity = this.parser.memory().currentEntity();
		if(character == ';') {
//			if ("nbsp".equals(entity.toString())) {
//				parser.append(' '); // TODO check yes or no if it's good idea to transform &nbsp into a space ?
//			} else {
				char decoded = EntitiesToUnicode.decodeEntity(entity.toString());
				if (decoded == '\0') {
					parser.append('&').append(entity.toString()).append(';');
					parser.memory().lastChar(';');
				} else {
//					CharBuffer cb = CharBuffer.wrap(new char[] {decoded});
//					Normalizer.normalize(target_chars, Normalizer.Form.NFD);
					parser.append(Character.toString(decoded));
					parser.memory().lastChar(decoded);
				}
//			}
            parser.selectState().inTag();
            this.parser.memory().currentEntity().setLength(0);
		 } else if (character != '#' && (character < '0' || character > '9') && (character < 'a' || character > 'z')
                && (character < 'A' || character > 'Z') || entity.length() >= 7) {
			 parser.append('&').append(entity.toString());
			 parser.selectState().inTag();
			 this.parser.memory().currentEntity().setLength(0);
        } else {
        	entity.append(character);
        }
	}

}
