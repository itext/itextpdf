/* $Id$
 * $Name$
 *
 * Copyright 2007 by Howard Shank (hgshank@yahoo.com)
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
package com.lowagie.text.rtf.direct.ctrlwords; 

import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.direct.RtfParser;
import com.lowagie.text.rtf.direct.RtfDirectContent;
import java.util.*;
import java.io.PushbackReader;

public abstract class RtfCtrlWordBase implements Cloneable {
	public static final boolean debug = false;

	/**
	 * Local variable referencing the parser object. 
	 */
	protected RtfParser rtfParser = null;
	/**
	 * The control word for this class.
	 */
	protected String ctrlWord = "";
	/**
	 * The default value for this control word.
	 * Not all control words use a default parameter value.
	 */
	protected int defaultParameterValue = 0;
	/**
	 * Does this control word use the default value?
	 */
	protected boolean passDefaultParaeterValue = false;
	/**
	 * Control Word type. Destination, toggle, value, etc.
	 */
	protected int ctrlWordType = RtfCtrlWordType.UNIDENTIFIED;
	/**
	 * Unused at this time.
	 */
	protected int index = RtfCtrlWordPropertyType.UNIDENTIFIED;
	/**
	 * What version of the RTF spec the control word was introduced.
	 */
	protected float rtfVersionSupported = -1.0f;	// -1.0 unknown. Each class should override this as implemented.
	
	/**
	 * The control word as parsed by the parser.
	 */
	protected RtfCtrlWordData ctrlWordData = null;
	/**
	 * Group level indicator.
	 */
	protected int groupLevel = 0;
	/**
	 * Reference to the PushbackReader object that is the current input stream.
	 */
	protected PushbackReader pushbackReaderInput = null;
	/**
	 * Flag to indicate if this is the first control word in a new group.
	 */
	protected boolean isFirstInGroup = false;
	/**
	 * Flag to indicate if this control word began with a \*.
	 */
	protected boolean bExtendedDestination = false;
	
	/**
	 * String containing the value of "{" or "" (blank) depending on if this is the
	 * first control word in a group.
	 */
	protected String groupPrefix = "";
	/**
	 * The prefix for all control words.
	 */
	protected String ctrlWordPrefix = "\\";
	/**
	 * Flag to override if this control word should set itself as the handler for the
	 * processing of text, just before end of gruop processing, etc.
	 * Known overrides at this time:
	 * Classes: \red, \green, \blue
	 */
	protected boolean setThisDefaultCtrlWordHandler = true;
	
	/**
	 * Default parser state for this control word.
	 * Overrides include header control words, i.e. \rtf, \ansi, \deff, etc.
	 */
	protected int defaultParserState = RtfParser.PARSER_IN_DOCUMENT;
	
	/**
	 * Constructor:
	 *
	 * @param rtfParser
	 * 		The parser for this control word.
	 * @param ctrlWord
	 * 		The string value of this control word.
	 * @param dfltValue
	 * 		The default value of this control word. Not all control words have values.
	 * @param passDfltValue
	 * 		Flag indicating if this control word should use the3 default value.
	 * @param kwdTypeValue
	 * 		Indicator of the type of control word this is. Destination, value, toggle, etc.
	 * @param indexValue
	 * 		Unused at this time.
	 */
	protected RtfCtrlWordBase(RtfParser rtfParser, String ctrlWord, int defaultParameterValue, boolean passDefaultParaeterValue, int ctrlWordType, int index) {
		super();
		this.rtfParser = rtfParser;
		this.ctrlWord = ctrlWord;
		this.defaultParameterValue = defaultParameterValue;
		this.passDefaultParaeterValue = passDefaultParaeterValue;
		this.ctrlWordType = ctrlWordType;
		this.index = index;
	}
	
	/**
	 * The primary control word handler method.
	 * Called by the parser once it has a control word and parameter if applicable.
	 * 
	 * @param param
	 * 		The control word and associated parameter if applicable.
	 * @param groupLevel
	 * 		The current group level from the parser.
	 * @param reader
	 * 		The PushbackReader object for the current input stream.
	 * @return
	 * 		True or false if the control word was handled.
	 */
	public final boolean handleControlword(RtfCtrlWordData ctrlWordData, int groupLevel, PushbackReader pushbackReaderInput){
		if(debug) printDebug("handleKeyword");
		boolean result = false;
		this.ctrlWordData = ctrlWordData;
		this.groupLevel = groupLevel;
		this.pushbackReaderInput = pushbackReaderInput;

		processNewGroup();
		processExtendedDestination();
		if(this.setThisDefaultCtrlWordHandler) {
			this.setControlWordHandler(this);
		}
		
		if((result = this.handleControlWord()) == false) {
			switch(rtfParser.getParserState())
			{
				case RtfParser.PARSER_STARTSTOP:
					result = handleParserStartup();
					break;
				case RtfParser.PARSER_ERROR:
					break;
				case RtfParser.PARSER_IN_UNKNOWN:
					break;
				case RtfParser.PARSER_IN_HEADER:
					result = handleParserInHeader();
					break;
				case RtfParser.PARSER_IN_CHARSET:
					result = handleParserInCharset();
					break;
				case RtfParser.PARSER_IN_DEFFONT:
					result = handleParserInDeffont();
					break;
				case RtfParser.PARSER_IN_FONT_TABLE:
					result = handleParserInFontTable();
					break;
				case RtfParser.PARSER_IN_FONT_TABLE_INFO:
					result = handleParserInFontTableInfo();
					break;
				case RtfParser.PARSER_IN_FILE_TABLE:
					result = handleParserInFileTable();
					break;
				case RtfParser.PARSER_IN_COLOR_TABLE:
					result = handleParserInColorTable();
					break;
				case RtfParser.PARSER_IN_STYLESHEET:
					result = handleParserInStylesheet();
					break;
				case RtfParser.PARSER_IN_LIST_TABLE:
					result = handleParserInListTable();
					break;
				case RtfParser.PARSER_IN_REV_TABLE:
					result = handleParserInRevTable();
					break;
				case RtfParser.PARSER_IN_RSID_TABLE:
					result = handleParserInRsidTable();
					break;
				case RtfParser.PARSER_IN_GENERATOR:
					result = handleParserInGenerator();
					break;
				case RtfParser.PARSER_IN_INFO_GROUP:
					result = handleParserInInfoGroup();
					break;
				case RtfParser.PARSER_IN_DOCUMENT:
					result = handleParserInDocument();
					break;
				case RtfParser.PARSER_IN_UPR:
					result = handleParserInUpr();
					break;
				default:
					result = handleDefault();
					break;
			}
			
			if(result == false && this.rtfParser.isImport()) {
				this.rtfParser.getRtfDocument().add(new RtfDirectContent(this.toString()));
				return true;
			}
		}
		return result;
	}
	
	/**
	 * The first handle method for the control word.
	 */
	protected boolean handleControlWord() {
		if(debug) printDebug("handleCtrlWord");
		return false;
	}
	
	/*
	 * Default handlers for each state 
	 */
	
	protected boolean handleParserStartup() {
		if(debug) printDebug("handleParserStartup");
		return false;
	}
	protected boolean handleDefault() {
		if(debug) printDebug("handleDefault");
		return false;
	}
	protected boolean handleParserInHeader() {
		if(debug) printDebug("handleParserInHeader");
		return false;	
	}
	protected boolean handleParserInCharset() {		
		if(debug) printDebug("handleParserInCharset");
		return false;
	}
	protected boolean handleParserInDeffont() {		
		if(debug) printDebug("handleParserInDeffont");
		return false;	
	}
	protected boolean handleParserInFontTable() {		
		if(debug) printDebug("handleParserInFontTable");
		return false;	
	}
	protected boolean handleParserInFontTableInfo() {		
		if(debug) printDebug("handleParserInFontTableInfo");
		return false;	
	}
	protected boolean handleParserInFileTable() {		
		if(debug) printDebug("handleParserInFileTable");
		return false;	
	}
	protected boolean handleParserInColorTable() {		
		if(debug) printDebug("handleParserInColorTable");
		return false;	
	}
	protected boolean handleParserInStylesheet() {		
		if(debug) printDebug("handleParserInStylesheet");
		return false;	
	}
	protected boolean handleParserInListTable() {		
		if(debug) printDebug("handleParserInListTable");
		return false;	
	}
	protected boolean handleParserInRevTable() {		
		if(debug) printDebug("handleParserInRevTable");
		return false;	
	}
	protected boolean handleParserInRsidTable() {		
		if(debug) printDebug("handleParserInRsidTable");
		return false;	
	}
	protected boolean handleParserInGenerator() {		
		if(debug) printDebug("handleParserInGenerator");
		return false;	
	}
	protected boolean handleParserInInfoGroup() {		
		if(debug) printDebug("handleParserInInfoGroup");
		return false;	
	}
	protected boolean handleParserInDocument() {		
		if(debug) printDebug("handleParserInDocument");
		return false;	
	}
	protected boolean handleParserInUpr() {		
		if(debug) printDebug("handleParserInUpr");
		return false;	
	}
	
	/**
	 * Get the RTF Specification version this control word was introduced in.
	 * @return
	 */
	public float getVersionSupported() {
		return this.rtfVersionSupported;
	}
	/**
	 * Set the RTF Specification version this control word was introduced in.
	 * @param version
	 * @return
	 */
	public float setVersionSupported(float version) {
		this.rtfVersionSupported = version;
		return this.rtfVersionSupported;
	}
	
	/**
	 * Default handler when parser encounters an open group token "{"
	 * @param newStateValue
	 * @return
	 */
	public boolean handleOpenGroup(int newStateValue){
		return false;	// true
	}
	
	/**
	 * Default handler when parser encounters a close group token "}"
	 * @param newStateValue
	 * @return
	 */
	public boolean handleCloseGroup() {
		if(this.isFirstInGroup && 
				this.rtfParser.isImport() && 
				this.inDocument() ) {
			this.rtfParser.getRtfDocument().add(new RtfDirectContent("}"));
			return true;
		}
		return false;	
	}
	/**
	 * Default handler when parser encounters a character toekn.
	 * @param ch
	 * @return
	 */
	public boolean handleCharacter(char[] ch) {
		return false;
	}
	/**
	 * Default handler when parser encounters a new control word/end of group and it has a string.
	 * 
	 * @param value
	 * 		The string value for this control word. (Plain Text)
	 * @return
	 */
	public boolean handleText(String value){
		return false;
	}
	/**
	 * Default handler when parser encounters a a new control word or end of a group.
	 * This is called when this control word is set to be a handler.
	 * 
	 * @return
	 */
	public boolean handleEndControlWord() {
		return false;	
	}

	
	public String toString() {
		String out = ctrlWordPrefix + this.ctrlWord;
		if(this.ctrlWordData.hasParam) {
			if(this.ctrlWordData.isNeg) out += "-";
			out += this.ctrlWordData.param;
		} else {
			if(this.passDefaultParaeterValue == true) {
				out += Integer.toString(this.defaultParameterValue);
			}
		}
		return out;
	}
	
	/**
	 * Set this object values depending on if this is the first
	 * control word in a new group.
	 *
	 */
	private final void processNewGroup() {
		if(this.isNewGroup()) {
			this.isFirstInGroup = true;
			this.setNewGroup(false);
			this.groupPrefix = "{";
			//this.setGroupHandler(this);
			try {
				Object ob = this.clone();
				RtfCtrlWordBase b = (RtfCtrlWordBase)ob;
				this.setGroupHandler(b);
			} catch (CloneNotSupportedException e) {
				this.setGroupHandler(this);
			}
		} else {
			this.isFirstInGroup = false;
			this.groupPrefix = "";
		}
	}
	
	/**
	 * Set this object values depending on if this is an extended
	 * destination 
	 * Note from: Microsoft Office Word 2007 Rich Text Format (RTF) Specification document.
	 * Destinations added after the RTF Specification published in 
	 * the March 1987 Microsoft Systems Journal may be preceded by 
	 * the control symbol \* (backslash asterisk). 
	 */
	private final void processExtendedDestination() {
		if(this.getExtendedDestination()) {
			this.bExtendedDestination = true;
			this.setExtendedDestination(false);
			this.ctrlWordPrefix = this.groupPrefix + "\\*\\";
		} else {
			this.bExtendedDestination = false;
			this.ctrlWordPrefix = this.groupPrefix + "\\";
		}
	}
	/**
	 * Wrapper method to get flag indicating Extended Destinations.
	 * @return
	 */
	protected final boolean getExtendedDestination() {
		return this.rtfParser.getExtendedDestination();
	}
	/**
	 * Wrapper method to set flag indicating Extended Destinations. Called only by /* control word!
	 * @return
	 */
	protected final boolean setExtendedDestination(boolean value) {
		this.rtfParser.setExtendedDestination(value);
		return value;
	}
	/**
	 * Wrapper method to set parser/tokeniser state to skip to the end of a group.
	 */
	protected final void skipToEndOfGroup() {
		this.rtfParser.setTokeniserStateSkipGroup();
	}
	
	/**
	 * Wrapper method to set parser/tokeniser state to skip some byte count.
	 */
	protected final void skipBytes(long numberOfBytesToSkip) {
		this.rtfParser.setTokeniserSkipBytes(numberOfBytesToSkip);
	}
	
	/**
	 * Wrapper method to set parser group handler object.
	 */
	protected final void setGroupHandler(RtfCtrlWordBase kwd) {
		this.rtfParser.setGroupHandler(kwd);
	}
	/**
	 * Wrapper method to set parser control word handler object.
	 */
	protected final void setControlWordHandler(RtfCtrlWordBase kwd) {
		this.rtfParser.setControlWordHandler(kwd);
	}
	
	/**
	 * Wrapper method to set parser state.
	 */
	protected final void setParserState(int state) {
		this.rtfParser.setParserState(state);
		return;
	}
	/**
	 * Wrapper method to set the parser->tokeniser state
	 */
	protected final void setTokeniserState(int state) {
		this.rtfParser.setTokeniserState(state);
	}

	/**
	 * Wrapper method to get parser flag indicator for a new group.
	 */
	protected final boolean isNewGroup() {
		return this.rtfParser.isNewGroup();
	}
	/**
	 * Wrapper method to set parser flag indicator for a new group.
	 */
	protected final boolean setNewGroup(boolean value) {
		return this.rtfParser.setNewGroup(value);
	}
	
	protected final void pushOldHandler(Object oldHandler) {
		this.rtfParser.getState().ctrlWordHandlers.push(oldHandler);
	}
	
	protected final Object popOldHandler() {  
		try {
			return this.rtfParser.getState().ctrlWordHandlers.pop();
		} catch (EmptyStackException e) {
		}
		return null;
	}
	
	public final void addDirectContent(boolean useText) {
		String text = "";
		if(useText) text = this.getText();
		addDirectContent(text);
		return;
	}
	
	public final void addDirectContent(String text) {
		if(text.length() > 0) text = " " + text;
		this.rtfParser.getRtfDocument().add(
				new RtfDirectContent(this.toString() + text));
	}
	
	public final void addContentControlWord() {
		int param = this.defaultParameterValue;
		boolean useParam = false;
		if(this.ctrlWordData.hasParam) {
			param = Integer.parseInt(this.ctrlWordData.param);
			if(this.ctrlWordData.isNeg) {
				param = -param;
			}
			useParam = true;
		} else {
			useParam = this.passDefaultParaeterValue;
		}
		
		if(useParam) {
			this.rtfParser.getRtfDocument().add(
				new RtfDirectContent(this.toString() +  Integer.toString(param)));
		} else {
			this.rtfParser.getRtfDocument().add(
				new RtfDirectContent(this.toString()));
		}
		//this.rtfParser.getDocument().add(new )	
	}
	
	protected final String getText() {
		return this.rtfParser.getState().text.toString();
	}
	
	/**
	 * Wrapper method to get the RtfDocument used by this parser.
	 */
	protected final RtfDocument rtfDocument() {
		return this.rtfParser.getRtfDocument();
	}
	/**
	 * Debug function to print class/method
	 * @param txt
	 */
	private final void printDebug(final String txt) {
		 System.out.println(this.getClass().getName() + " : " + txt);
	}
	/**
	 * Helper function to determin if the parser current state is one of the RtfParser2.PARSER_IN_DOCUMENT states.
	 * 
	 * @return
	 * 		True if the parser state is one of the PARSER_IN_DOCUMENT values, otherwise false.
	 */
	private final boolean inDocument() {
		return ((this.rtfParser.getState().parserState & RtfParser.PARSER_IN_DOCUMENT) == RtfParser.PARSER_IN_DOCUMENT);
	}
	/**
	 * Helper function to determin if the parser current state is one of the RtfParser2.PARSER_IN_HEADER states.
	 * 
	 * @return
	 * 		True if the parser state is one of the PARSER_IN_HEADER values, otherwise false.
	 */
	private final boolean inHeader() {
		return ((this.rtfParser.getState().parserState & RtfParser.PARSER_IN_HEADER) == RtfParser.PARSER_IN_HEADER);
	}
	
	protected Object clone() throws CloneNotSupportedException {
		RtfCtrlWordBase rcwb = (RtfCtrlWordBase)super.clone();
//		 kwdParam is the only object we want a deep copy of
//		 The other objects, pbr and rtfParser we want to be the original refrence.
		rcwb.ctrlWordData = (RtfCtrlWordData)ctrlWordData.clone();	
		
		return rcwb; 
	}
}
