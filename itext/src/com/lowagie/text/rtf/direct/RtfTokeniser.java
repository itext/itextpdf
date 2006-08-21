package com.lowagie.text.rtf.direct;

import java.io.IOException;
import java.io.Reader;

import com.lowagie.text.DocumentException;

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
	 * @throws DocumentException On errors writing the document.
	 */
	public void tokenise(Reader reader) throws IOException, DocumentException {
		char[] nextChar = new char[1];
		String temp = "";
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
					temp = "";
				} else {
					this.state = TOKENISER_STATE_IN_TEXT;
					temp = temp + nextChar[0];
				}
			} else if((this.state & TOKENISER_STATE_SLASH) == TOKENISER_STATE_SLASH) { // A slash signals a control character or word or an escaped character
				if(nextChar[0] == '{') {
					this.state = TOKENISER_STATE_IN_TEXT;
					temp = temp + "\\{";
				} else if(nextChar[0] == '}') {
					this.state = TOKENISER_STATE_IN_TEXT;
					temp = temp + "\\}";
				} else if(nextChar[0] == '\\') {
					this.state = TOKENISER_STATE_IN_TEXT;
					temp = temp + "\\\\";
				} else {
					if((this.state & TOKENISER_STATE_IN_TEXT) == TOKENISER_STATE_IN_TEXT) { // A control word or character closes previous text token
						this.rtfParser.handleText(temp, this.groupLevel);
						temp = "";
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
						temp = "\\" + nextChar[0];
					}
				}
			} else if(this.state == TOKENISER_STATE_IN_CTRL_WORD) { // Control words run until a space, close or open group or another control word is found.
				if(nextChar[0] == '\n' || nextChar[0] == '\r') {
					nextChar[0] = ' ';
				}
				if(nextChar[0] == '{') {
					this.rtfParser.handleCtrlWord(temp, this.groupLevel);
					this.rtfParser.handleOpenGroup(this.groupLevel);
					groupLevel++;
					this.state = TOKENISER_STATE_READY;
					temp = "";
				} else if(nextChar[0] == '}') {
					this.rtfParser.handleCtrlWord(temp, this.groupLevel);
					this.rtfParser.handleCloseGroup(this.groupLevel);
					groupLevel--;
					this.state = TOKENISER_STATE_READY;
					temp = "";
				} else if(nextChar[0] == '\\') {
					this.rtfParser.handleCtrlWord(temp, this.groupLevel);
					this.state = TOKENISER_STATE_SLASH;
					temp = "";
				} else if(nextChar[0] == ' ') {
					this.rtfParser.handleCtrlWord(temp, this.groupLevel);
					this.rtfParser.handleText(" ", this.groupLevel);
					this.state = TOKENISER_STATE_READY;
					temp = "";
				} else if(nextChar[0] == ';') {
					this.rtfParser.handleCtrlWord(temp, this.groupLevel);
					this.rtfParser.handleText(";", this.groupLevel);
					this.state = TOKENISER_STATE_READY;
					temp = "";
				} else {
					temp = temp + nextChar[0];
				}
			} else if(this.state == TOKENISER_STATE_IN_TEXT) { // Text tokens are closed by control characters or words or open and close groups
				if(nextChar[0] == '{') {
					this.rtfParser.handleText(temp, this.groupLevel);
					this.rtfParser.handleOpenGroup(this.groupLevel);
					groupLevel++;
					this.state = TOKENISER_STATE_READY;
					temp = "";
				} else if(nextChar[0] == '}') {
					this.rtfParser.handleText(temp, this.groupLevel);
					this.rtfParser.handleCloseGroup(this.groupLevel);
					groupLevel--;
					this.state = TOKENISER_STATE_READY;
					temp = "";
				} else if(nextChar[0] == '\\') {
					this.state = TOKENISER_STATE_IN_TEXT | TOKENISER_STATE_SLASH;
				} else {
					temp = temp + nextChar[0];
				}
			}
		}
		if((this.state & TOKENISER_STATE_IN_TEXT) == TOKENISER_STATE_IN_TEXT && !temp.equals("")) { // If at the end a text token was being parsed, emmit that token. Required for RTF fragments
			this.rtfParser.handleText(temp, this.groupLevel);
		}
	}
}
