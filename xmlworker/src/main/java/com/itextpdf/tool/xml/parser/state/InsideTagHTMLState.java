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
package com.itextpdf.tool.xml.parser.state;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.parser.State;
import com.itextpdf.tool.xml.parser.TagState;
import com.itextpdf.tool.xml.parser.XMLParser;

/**
 * @author redlab_b
 *
 */
public class InsideTagHTMLState implements State {

	private final XMLParser parser;
	private final List<String> noSanitize = new ArrayList<String>(1);
	private final List<String> ignoreLastChars = new ArrayList<String>(9);
	/**
	 * @param parser the XMLParser
	 */
	public InsideTagHTMLState(final XMLParser parser) {
		this.parser = parser;
		noSanitize.add(HTML.Tag.PRE);
		ignoreLastChars.add(HTML.Tag.P);
		ignoreLastChars.add(HTML.Tag.DIV);
		ignoreLastChars.add(HTML.Tag.H1);
		ignoreLastChars.add(HTML.Tag.H2);
		ignoreLastChars.add(HTML.Tag.H3);
		ignoreLastChars.add(HTML.Tag.H4);
		ignoreLastChars.add(HTML.Tag.H5);
		ignoreLastChars.add(HTML.Tag.H6);
		ignoreLastChars.add(HTML.Tag.TD);
		ignoreLastChars.add(HTML.Tag.TH);
		ignoreLastChars.add(HTML.Tag.UL);
		ignoreLastChars.add(HTML.Tag.OL);
		ignoreLastChars.add(HTML.Tag.LI);
		ignoreLastChars.add(HTML.Tag.DD);
		ignoreLastChars.add(HTML.Tag.DT);
		ignoreLastChars.add(HTML.Tag.HR);
		ignoreLastChars.add(HTML.Tag.BR);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.parser.State#process(int)
	 */
	public void process(final char character) {
		if (character == '<') {
			if (this.parser.bufferSize() > 0) {
				this.parser.text(this.parser.current());
			}
			this.parser.flush();
			this.parser.selectState().tagEncountered();
		} else if (character == '&') {
			this.parser.selectState().specialChar();
		} else  {
			if (character == '*' && this.parser.memory().lastChar() == '/') {
				this.parser.selectState().starComment();
				this.parser.memory().current().deleteCharAt(this.parser.memory().current().lastIndexOf("/"));
				if (this.parser.bufferSize() > 0) {
					this.parser.memory().setStoredString(this.parser.current());
				}
				this.parser.flush();
			} else {
				String tag = this.parser.currentTag();
				TagState state = this.parser.currentTagState();
				if (noSanitize.contains(tag) && TagState.OPEN == state) {
					this.parser.append(character);
				} else {
					if (this.parser.memory().whitespaceTag().length() != 0) {
						if (ignoreLastChars.contains(this.parser.memory().whitespaceTag().toLowerCase())) {
							parser.memory().lastChar(' ');
						}
						this.parser.memory().whitespaceTag("");
					}
					boolean whitespace = Character.isWhitespace(parser.memory().lastChar());
					boolean noWhiteSpace = !Character.isWhitespace(character);
					if (!whitespace || (whitespace && noWhiteSpace)) {
						if (noWhiteSpace) {
							this.parser.append(character);
						} else {
							this.parser.append(' ');
						}
					}
					parser.memory().lastChar(character);
				}
			}
		}
	}
}
