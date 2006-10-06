/**
 * $Id$
 * $Name$
 *
 * Copyright 2006 by Mark Hall
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999-2006 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2006 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the ?GNU LIBRARY GENERAL PUBLIC LICENSE?), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.rtf.direct;

import java.io.IOException;
import java.io.Reader;

/**
 * The RtfTokeniser takes an RTF document stream and
 * turns it into a set of RTF tokens. Five groups of
 * tokens are differentiated:
 * 
 * <ul>
 *   <li>Group opening: {</li>
 *   <li>Group closing: }</li>
 *   <li>Control characters</li>
 *   <li>Control words</li>
 *   <li>Text</li>
 * </ul>
 * 
 * @version $Revision$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 * @author Bullo (bullo70@users.sourceforge.net)
 */
public class RtfTokeniser {
	/**
	 * The RtfTokeniser is in its ground state. Any token may follow.
	 */
	private static final int TOKENISER_STATE_READY = 0;
	/**
	 * The last token parsed was a slash.
	 */
	private static final int TOKENISER_STATE_SLASH = 1;
	/**
	 * The RtfTokeniser is currently tokenising a control word.
	 */
	private static final int TOKENISER_STATE_IN_CTRL_WORD = 2;
	/**
	 * The RtfTokeniser is currently tokenising a text.
	 */
	private static final int TOKENISER_STATE_IN_TEXT = 4;
	
	/**
	 * The current state of this RtfTokeniser.
	 */
	private int state = TOKENISER_STATE_READY;
	/**
	 * The current group nesting level.
	 */
	private int groupLevel = 0;
	/**
	 * The RtfParser to send tokens to.
	 */
	private RtfParser rtfParser = null;

	/**
	 * Constructs a new RtfTokeniser. The startGroupLevel is required when parsing
	 * RTF fragments, since they are missing the opening group and closing group
	 * and thus this has to be set at the beginning.
	 * 
	 * @param rtfParser The RtfParser to send tokens to.
	 * @param startGroupLevel The starting group nesting level. 0 for full documents, 1 for fragments.
	 */
	public RtfTokeniser(RtfParser rtfParser, int startGroupLevel) {
		this.rtfParser = rtfParser;
		this.groupLevel = startGroupLevel;
	}
	
	/**
	 * The main tokenisation method. Implements a LL(1) parser.
	 * 
	 * @param reader The Reader to read the RTF document from.
	 * @throws IOException On I/O errors.
	 */
	public void tokenise(Reader reader) throws IOException {
		char[] nextChar = new char[1];
		StringBuffer temp = new StringBuffer();
		this.state = TOKENISER_STATE_READY;
		this.groupLevel = 0;
		while(reader.read(nextChar) != -1) {
			if(this.state == TOKENISER_STATE_READY) { // No influence from previous characters.
				if(nextChar[0] == '{') { // Open a group
					this.rtfParser.handleOpenGroup(this.groupLevel);
					groupLevel++;
				} else if(nextChar[0] == '}') { // Close a group
					this.rtfParser.handleCloseGroup(this.groupLevel);
					groupLevel--;
				} else if(nextChar[0] == '\\') {
					this.state = TOKENISER_STATE_SLASH;
					temp = new StringBuffer();
				} else {
					this.state = TOKENISER_STATE_IN_TEXT;
					temp.append(nextChar[0]);
				}
			} else if((this.state & TOKENISER_STATE_SLASH) == TOKENISER_STATE_SLASH) { // A slash signals a control character or word or an escaped character
				if(nextChar[0] == '{') {
					this.state = TOKENISER_STATE_IN_TEXT;
					temp.append("\\{");
				} else if(nextChar[0] == '}') {
					this.state = TOKENISER_STATE_IN_TEXT;
					temp.append("\\}");
				} else if(nextChar[0] == '\\') {
					this.state = TOKENISER_STATE_IN_TEXT;
					temp.append("\\\\");
				} else {
					if((this.state & TOKENISER_STATE_IN_TEXT) == TOKENISER_STATE_IN_TEXT) { // A control word or character closes previous text token
						this.rtfParser.handleText(temp.toString(), this.groupLevel);
						temp = new StringBuffer();
					}
					if(nextChar[0] == '|') {
						this.state = TOKENISER_STATE_READY;
						this.rtfParser.handleCtrlCharacter("\\|", this.groupLevel);
					} else if(nextChar[0] == '~') {
						this.state = TOKENISER_STATE_READY;
						this.rtfParser.handleCtrlCharacter("\\~", this.groupLevel);
					} else if(nextChar[0] == '-') {
						this.state = TOKENISER_STATE_READY;
						this.rtfParser.handleCtrlCharacter("\\-", this.groupLevel);
					} else if(nextChar[0] == '_') {
						this.state = TOKENISER_STATE_READY;
						this.rtfParser.handleCtrlCharacter("\\_", this.groupLevel);
					} else if(nextChar[0] == ':') {
						this.state = TOKENISER_STATE_READY;
						this.rtfParser.handleCtrlCharacter("\\:", this.groupLevel);
					} else if(nextChar[0] == '*') {
						this.state = TOKENISER_STATE_READY;
						this.rtfParser.handleCtrlCharacter("\\*", this.groupLevel);
					} else {
						this.state = TOKENISER_STATE_IN_CTRL_WORD;
						temp = new StringBuffer("\\");
						temp.append(nextChar[0]);
					}
				}
			} else if(this.state == TOKENISER_STATE_IN_CTRL_WORD) { // Control words run until a space, close or open group or another control word is found.
				if(nextChar[0] == '\n' || nextChar[0] == '\r') {
					nextChar[0] = ' ';
				}
				if(nextChar[0] == '{') {
					this.rtfParser.handleCtrlWord(temp.toString(), this.groupLevel);
					this.rtfParser.handleOpenGroup(this.groupLevel);
					groupLevel++;
					this.state = TOKENISER_STATE_READY;
					temp = new StringBuffer();
				} else if(nextChar[0] == '}') {
					this.rtfParser.handleCtrlWord(temp.toString(), this.groupLevel);
					this.rtfParser.handleCloseGroup(this.groupLevel);
					groupLevel--;
					this.state = TOKENISER_STATE_READY;
					temp = new StringBuffer();
				} else if(nextChar[0] == '\\') {
					this.rtfParser.handleCtrlWord(temp.toString(), this.groupLevel);
					this.state = TOKENISER_STATE_SLASH;
					temp = new StringBuffer();
				} else if(nextChar[0] == ' ') {
					this.rtfParser.handleCtrlWord(temp.toString(), this.groupLevel);
					this.rtfParser.handleText(" ", this.groupLevel);
					this.state = TOKENISER_STATE_READY;
					temp = new StringBuffer();
				} else if(nextChar[0] == ';') {
					this.rtfParser.handleCtrlWord(temp.toString(), this.groupLevel);
					this.rtfParser.handleText(";", this.groupLevel);
					this.state = TOKENISER_STATE_READY;
					temp = new StringBuffer();
				} else {
					temp.append(nextChar[0]);
				}
			} else if(this.state == TOKENISER_STATE_IN_TEXT) { // Text tokens are closed by control characters or words or open and close groups
				if(nextChar[0] == '{') {
					this.rtfParser.handleText(temp.toString(), this.groupLevel);
					this.rtfParser.handleOpenGroup(this.groupLevel);
					groupLevel++;
					this.state = TOKENISER_STATE_READY;
					temp = new StringBuffer();
				} else if(nextChar[0] == '}') {
					this.rtfParser.handleText(temp.toString(), this.groupLevel);
					this.rtfParser.handleCloseGroup(this.groupLevel);
					groupLevel--;
					this.state = TOKENISER_STATE_READY;
					temp = new StringBuffer();
				} else if(nextChar[0] == '\\') {
					this.state = TOKENISER_STATE_IN_TEXT | TOKENISER_STATE_SLASH;
				} else {
					temp.append(nextChar[0]);
				}
			}
		}
		if((this.state & TOKENISER_STATE_IN_TEXT) == TOKENISER_STATE_IN_TEXT && !temp.toString().equals("")) { // If at the end a text token was being parsed, emmit that token. Required for RTF fragments
			this.rtfParser.handleText(temp.toString(), this.groupLevel);
		}
	}
}
