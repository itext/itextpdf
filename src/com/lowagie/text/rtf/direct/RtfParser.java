/*
 * $Id$
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
package com.lowagie.text.rtf.direct;

import java.awt.Color;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.PrintStream;
import java.util.*;
import com.lowagie.text.Document;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.direct.ctrlwords.*;
import com.lowagie.text.List;

/**
 * The RtfParser allows the importing of RTF documents or
 * RTF document fragments. The RTF document or fragment is tokenised,
 * font and color definitions corrected and then added to
 * the document being written.
 * 
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 * @author Howard Shank (hgshank@yahoo.com)
 */

public class RtfParser {
	/**
	 * Debugging flag.
	 */
	private static final boolean debugParser = false;	// DEBUG Files are unlikely to be read by any reader! 
	/**
	 * The iText document to add the RTF document to.
	 */
	private Document document = null;
	/**
	 * The RtfDocument to add the RTF document or fragment to.
	 */
	private RtfDocument rtfDoc = null;
	/**
	 * The RtfKeywords that creates and handles keywords that are implemented.
	 */
	private RtfCtrlWordMgr rtfKeywordMgr = null;
	/**
	 * The RtfImportHeader to store imported font and color mappings in.
	 */
	private RtfImportMgr importMgr = null;

	/**
	 * Stack for saving states for groups
	 */
	private Stack stackState = null;
	/**
	 * The current parser state.
	 */	
	private RtfParserState currentState = null;

	/**
	 * The pushback reader to read the input stream.
	 */
	private PushbackReader pbReader = null;
	
	/**
	 * Conversion type. Identifies if we are doing in import or a convert.
	 */
	private int conversionType = TYPE_IMPORT_FULL;

	/*
	 * Destinations
	 */
	
	/**
	 * The RtfDestination to use for the document.
	 */
	private RtfDestinationNull destNull = null;

	/**
	 * The RtfDestination to use for the document.
	 */
	private RtfDestinationDocument destDocument = null;
	/**
	 * The RtfFontTableParser to use for parsing the font table.
	 */
	private RtfDestinationFontTable destFontTable = null;
	/**
	 * The RtfColorTableParser to use for parsing the color table.
	 */
	private RtfDestinationColorTable destColorTable = null;
	/**
	 * The RtfDestInfo
	 */
	private RtfDestinationInfo destInfo = null;
	/**
	 * The RtfDestinationStylesheetTable.
	 */
	private RtfDestinationStylesheetTable destStylesheetTable = null;
	/**
	 * The RtfDestinationListTable.
	 */
	private RtfDestinationListTable destListTable = null;
	
	
	/**
	 * Flag to indicate if last token was an open group token '{'
	 */
	private boolean newGroup = false;

	
	/*
	 * Bitmapping:
	 * 
	 * 0111 1111 1111 1111 = Unkown state
	 * 0xxx xxxx xxxx xxxx = In Header
	 * 1xxx xxxx xxxx xxxx = In Document
	 * 2xxx xxxx xxxx xxxx = Reserved
	 * 4xxx xxxx xxxx xxxx = Other
	 * 8xxx xxxx xxxx xxxx = Errors
	 */
	
	/*
	 * Header state values
	 */

	/**
	 * Currently the RTF document header is being parsed.
	 */
	public static final int PARSER_IN_HEADER = (0x0 << 28) | 0x000000;
	/**
	 * Currently the RTF charset is being parsed.
	 */
	public static final int PARSER_IN_CHARSET = PARSER_IN_HEADER | 0x000001;
	/**
	 * Currently the RTF deffont is being parsed.
	 */
	public static final int PARSER_IN_DEFFONT = PARSER_IN_HEADER | 0x000002;
	/**
	 * Currently the RTF font table is being parsed.
	 */
	public static final int PARSER_IN_FONT_TABLE = PARSER_IN_HEADER | 0x000003;
	/**
	 * Currently a RTF font table info element is being parsed.
	 */
	public static final int PARSER_IN_FONT_TABLE_INFO = PARSER_IN_HEADER | 0x000004;
	/**
	 * Currently the RTF filetbl is being parsed.
	 */
	public static final int PARSER_IN_FILE_TABLE = PARSER_IN_HEADER | 0x000005;
	/**
	 * Currently the RTF color table is being parsed.
	 */
	public static final int PARSER_IN_COLOR_TABLE = PARSER_IN_HEADER | 0x000006;
	/**
	 * Currently the RTF  stylesheet is being parsed.
	 */
	public static final int PARSER_IN_STYLESHEET = PARSER_IN_HEADER | 0x000007;
	/**
	 * Currently the RTF listtables is being parsed.
	 */
	public static final int PARSER_IN_LIST_TABLE = PARSER_IN_HEADER | 0x000008;
	/**
	 * Currently the RTF listtable override is being parsed.
	 */
	public static final int PARSER_IN_LISTOVERRIDE_TABLE = PARSER_IN_HEADER | 0x000009;
	/**
	 * Currently the RTF revtbl is being parsed.
	 */
	public static final int PARSER_IN_REV_TABLE = PARSER_IN_HEADER | 0x00000A;
	/**
	 * Currently the RTF rsidtable is being parsed.
	 */
	public static final int PARSER_IN_RSID_TABLE = PARSER_IN_HEADER | 0x0000B;
	/**
	 * Currently the RTF generator is being parsed.
	 */
	public static final int PARSER_IN_GENERATOR = PARSER_IN_HEADER | 0x00000C;
	/**
	 * Currently the RTF Paragraph group properties Table (word 2002)
	 */
	public static final int PARSER_IN_PARAGRAPH_TABLE = PARSER_IN_HEADER | 0x00000E;
	/**
	 * Currently the RTF Old Properties.
	 */
	public static final int PARSER_IN_OLDCPROPS = PARSER_IN_HEADER | 0x00000F;
	/**
	 * Currently the RTF Old Properties.
	 */
	public static final int PARSER_IN_OLDPPROPS = PARSER_IN_HEADER | 0x000010;
	/**
	 * Currently the RTF Old Properties.
	 */
	public static final int PARSER_IN_OLDTPROPS = PARSER_IN_HEADER | 0x000012;
	/**
	 * Currently the RTF Old Properties.
	 */
	public static final int PARSER_IN_OLDSPROPS = PARSER_IN_HEADER | 0x000013;
	/**
	 * Currently the RTF User Protection Information.
	 */
	public static final int PARSER_IN_PROT_USER_TABLE = PARSER_IN_HEADER | 0x000014;
	/**
	 * Currently the Latent Style and Formatting usage restrictions
	 */
	public static final int PARSER_IN_LATENTSTYLES = PARSER_IN_HEADER | 0x000015;
	
	public static final int PARSER_IN_PARAGRAPH_GROUP_PROPERTIES =PARSER_IN_HEADER | 0x000016;
	
	/*
	 * Document state values
	 */
	
	/**
	 * Currently the RTF document content is being parsed.
	 */
	public static final int PARSER_IN_DOCUMENT = (0x2 << 28 ) | 0x000000;

	/**
	 * Currently the RTF info group is being parsed.
	 */
	public static final int PARSER_IN_INFO_GROUP = PARSER_IN_DOCUMENT | 0x000001;

	
	public static final int PARSER_IN_UPR = PARSER_IN_DOCUMENT | 0x000002;
	/**
	 * The parser is at the beginning or the end of the file.
	 */
	public static final int PARSER_STARTSTOP = (0x4 << 28)| 0x0001;
	
	/**
	 * Currently the parser is in an error state.
	 */
	public static final int PARSER_ERROR = (0x8 << 28) | 0x0000;
	/**
	 * The parser reached the end of the file.
	 */
	public static final int PARSER_ERROR_EOF = PARSER_ERROR | 0x0001;
	/**
	 * Currently the parser is in an unknown state.
	 */
	public static final int PARSER_IN_UNKNOWN = PARSER_ERROR | 0x0FFFFFFF;
	
	
	/**
	 * Conversion type is unknown
	 */
	public static final int TYPE_UNIDENTIFIED = -1;
	/**
	 * Conversion type is an import. Uses direct content to add everything.
	 * This is what the original import does.
	 */
	public static final int TYPE_IMPORT_FULL = 0;
	/**
	 * Conversion type is an import of a partial file/fragment. Uses direct content to add everything.
	 */
	public static final int TYPE_IMPORT_FRAGMENT = 1;
	/**
	 * Conversion type is a conversion. This uses the document (not rtfDoc) to add
	 * all the elements making it a different supported documents depending on the writer used.
	 */
	public static final int TYPE_CONVERT = 2;

	
	public static final int DESTINATION_NORMAL = 0;
	public static final int DESTINATION_SKIP = 1;
	
	//////////////////////////////////// TOKENISE VARIABLES ///////////////////
	/*
	 * State flags use 4/28 bitmask.
	 * First 4 bits (nibble) indicates major state. Used for unknown and error
	 * Last 28 bits indicates the value;
	 */
	
	/**
	 * The RtfTokeniser is in its ground state. Any token may follow.
	 */
	public static final int TOKENISER_NORMAL = 0x00000000;
	/**
	 * The last token parsed was a slash.
	 */
	public static final int TOKENISER_SKIP_BYTES = 0x00000001;
	/**
	 * The RtfTokeniser is currently tokenising a control word.
	 */
	public static final int TOKENISER_SKIP_GROUP = 0x00000002;
	/**
	 * The RtfTokeniser is currently reading binary stream.
	 */
	public static final int TOKENISER_BINARY= 0x00000003;
	/**
	 * The RtfTokeniser is currently reading hex data.
	 */
	public static final int TOKENISER_HEX= 0x00000004;
	/**
	 * The RtfTokeniser is currently in error state
	 */
	public static final int TOKENISER_STATE_IN_ERROR =  0x80000000; // 1000 0000 0000 0000 0000 0000 0000 0000
	/**
	 * The RtfTokeniser is currently in an unkown state
	 */
	public static final int TOKENISER_STATE_IN_UNKOWN = 0xFF000000; // 1111 0000 0000 0000 0000 0000 0000 0000
	
	/**
	 * The current group nesting level.
	 */
	private int groupLevel = 0;
	/**
	 * The current document group nesting level. Used for fragments.
	 */
	private int docGroupLevel = 0;
	/**
	 * When the tokeniser is Binary.
	 */
	private long binByteCount = 0;
	/**
	 * When the tokeniser is set to skip bytes, binSkipByteCount is the number of bytes to skip.
	 */
	private long binSkipByteCount = 0;
	/**
	 * When the tokeniser is set to skip to next group, this is the group indentifier to return to.
	 */
	private int skipGroupLevel = 0;

	//RTF parser error codes
	public static final int  errOK =0;                        // Everything's fine!
	public static final int  errStackUnderflow   =  -1;       // Unmatched '}'
	public static final int  errStackOverflow    =  -2;       // Too many '{' -- memory exhausted
	public static final int  errUnmatchedBrace   =  -3;       // RTF ended during an open group.
	public static final int  errInvalidHex       =  -4;       // invalid hex character found in data
	public static final int  errBadTable         =  -5;       // RTF table (sym or prop) invalid
	public static final int  errAssertion        =  -6;       // Assertion failure
	public static final int  errEndOfFile        =  -7;       // End of file reached while reading RTF
	public static final int  errCtrlWordNotFound =  -8;		  // control word was not found
	//////////////////////////////////// TOKENISE VARIABLES ///////////////////
	
	
	//////////////////////////////////// STATS VARIABLES ///////////////////
	private long byteCount = 0;
	private long ctrlWordCount = 0;
	private long openGroupCount = 0;
	private long closeGroupCount = 0;
	private long characterCount = 0;
	private long ctrlWordHandledCount = 0;
	private long ctrlWordNotHandledCount = 0;
	private long ctrlWordSkippedCount = 0;
	private long groupSkippedCount = 0;
	private long startTime = 0;
	private long endTime = 0;
	private Date startDate = null;
	private Date endDate = null;
	//////////////////////////////////// STATS VARIABLES ///////////////////

	
	/* *********
	 *  READER *
	 ***********/
	/**
	 * Imports a complete RTF document.
	 * 
	 * @param readerIn 
	 * 		The Reader to read the RTF document from.
	 * @param rtfDoc 
	 * 		The RtfDocument to add the imported document to.
	 * @throws IOException On I/O errors.
	 */
	public void importRtfDocument(Reader readerIn, RtfDocument rtfDoc) throws IOException {
		if(readerIn == null || rtfDoc == null) return;
		this.init(TYPE_IMPORT_FULL, rtfDoc, readerIn, null);
		this.setCurrentDestination(this.destNull);
		startDate = new Date();
		startTime = System.currentTimeMillis();
		this.groupLevel = 0;
		this.tokenise();
		endTime = System.currentTimeMillis();
		endDate = new Date();
	}
	
	/**
	 * Converts an RTF document to an iText document.
	 * 
	 * @param readerIn 
	 * 		The Reader to read the RTF file from.
	 * @param rtfDoc 
	 * 		The RTF document to add the RTF file to.
	 * @param doc 
	 * 		The iText document that the RTF file is to be added to.
	 * @throws IOException 
	 * 		On I/O errors.
	 */
	public void convertRtfDocument(Reader readerIn, RtfDocument rtfDoc, Document doc) throws IOException {
		if(readerIn == null || rtfDoc == null || doc == null) return;
		this.init(TYPE_CONVERT, rtfDoc, readerIn, doc);
		this.setCurrentDestination(this.destNull);
		startDate = new Date();
		startTime = System.currentTimeMillis();
		this.groupLevel = 0;
		this.tokenise();
		endTime = System.currentTimeMillis();
		endDate = new Date();
	}


	/**
	 * Imports an RTF fragment.
	 * 
	 * @param readerIn 
	 * 		The Reader to read the RTF fragment from.
	 * @param rtfDoc 
	 * 		The RTF document to add the RTF fragment to.
	 * @param importMappings 
	 * 		The RtfImportMappings defining font and color mappings for the fragment.
	 * @throws IOException 
	 * 		On I/O errors.
	 */
	public void importRtfFragment(Reader readerIn, RtfDocument rtfDoc, RtfImportMappings importMappings) throws IOException {
		if(readerIn == null || rtfDoc == null || importMappings==null) return;
		this.init(TYPE_IMPORT_FRAGMENT, rtfDoc, readerIn, null);
		this.handleImportMappings(importMappings);
		this.setCurrentDestination(this.destDocument);
		this.groupLevel = 1;
		setParserState(RtfParser.PARSER_IN_DOCUMENT);
		startDate = new Date();
		startTime = System.currentTimeMillis();
		this.tokenise();
		endTime = System.currentTimeMillis();
		endDate = new Date();
	}

	/**
	 * Initialize the parser object values. 
	 * 
	 * @param type Type of conversion or import
	 * @param rtfDoc The <code>RtfDocument</code>
	 * @param readerIn The input stream
	 * @param doc The iText <code>Document</code>
	 */
	private void init(int type, RtfDocument rtfDoc, Reader readerIn, Document doc) {

		init_stats();
		// initialize reader to a PushbackReader
		this.pbReader = init_Reader(readerIn);
		
		this.conversionType = type;
		this.rtfDoc = rtfDoc;
		this.document = doc;
		this.currentState = new RtfParserState();
		this.stackState = new Stack();
		this.setParserState(PARSER_STARTSTOP);
		this.importMgr = new RtfImportMgr(this.rtfDoc, this.document);
		
		this.destFontTable = new RtfDestinationFontTable(this.importMgr);
		this.destColorTable = new RtfDestinationColorTable(this.importMgr);
		this.destInfo = new RtfDestinationInfo();
		this.destStylesheetTable = new RtfDestinationStylesheetTable(this.importMgr);
		this.destNull = new RtfDestinationNull();
		this.destDocument = new RtfDestinationDocument(this.rtfDoc, this.document, this.conversionType);
		
		this.rtfKeywordMgr = new RtfCtrlWordMgr(this, this.pbReader);		
	}
	/**
	 * Initialize the statistics values.
	 *
	 */
	protected void init_stats() {
		byteCount = 0;
		ctrlWordCount = 0;
		openGroupCount = 0;
		closeGroupCount = 0;
		characterCount = 0;
		ctrlWordHandledCount = 0;
		ctrlWordNotHandledCount = 0;
		ctrlWordSkippedCount = 0;
		groupSkippedCount = 0;
		startTime = 0;
		endTime = 0;
		startDate = null;
		endDate = null;
	}
	
	/**
	 * Casts the input reader to a PushbackReader or 
	 * creates a new PushbackReader from the Reader passed in.
	 * 
	 * @param readerIn
	 * 		The Reader object for the input file.
	 * @return
	 * 		PushbackReader object.
	 */
	private PushbackReader init_Reader(Reader readerIn) {
		Reader newReader = readerIn;
		// Initializing the reader as a BufferedReader 
		// cut test processing time by approximately 50%
		// default uses 8192 character buffer
		if(!(newReader instanceof BufferedReader)) {
			newReader = new BufferedReader(newReader);	// Since JDK1.1
		}
		// Initializing the reader as a PushbackReader is
		// a requirement of the parser to be able to put back
		// read ahead characters.
		if(!(newReader instanceof PushbackReader)) {
			newReader = new PushbackReader(newReader);	// Since JDK1.1
		}
		
		// return the proper reader object to the parser setup
		return  (PushbackReader)newReader;
	}
	
	/**
	 * Imports the mappings defined in the RtfImportMappings into the
	 * RtfImportHeader of this RtfParser2.
	 * 
	 * @param importMappings 
	 * 		The RtfImportMappings to import.
	 */
	private void handleImportMappings(RtfImportMappings importMappings) {
		Iterator it = importMappings.getFontMappings().keySet().iterator();
		while(it.hasNext()) {
			String fontNr = (String) it.next();
			this.importMgr.importFont(fontNr, (String) importMappings.getFontMappings().get(fontNr));
		}
		it = importMappings.getColorMappings().keySet().iterator();
		while(it.hasNext()) {
			String colorNr = (String) it.next();
			this.importMgr.importColor(colorNr, (Color) importMappings.getColorMappings().get(colorNr));
		}
		it = importMappings.getListMappings().keySet().iterator();
		while(it.hasNext()) {
			String listNr = (String) it.next();
			this.importMgr.importList(listNr, (List) importMappings.getListMappings().get(listNr));
		}
		it = importMappings.getStylesheetListMappings().keySet().iterator();
		while(it.hasNext()) {
			String stylesheetListNr = (String) it.next();
			this.importMgr.importStylesheetList(stylesheetListNr, (List) importMappings.getStylesheetListMappings().get(stylesheetListNr));
		}
		
	}
	
	
	/* *****************************************
	 *   DOCUMENT CONTROL METHODS
	 *   
	 *   Handles -
	 *   handleOpenGroup: 	Open groups		- '{'
	 *   handleCloseGroup: 	Close groups	- '}'
	 *   handleCtrlWord: 	Ctrl Words		- '\...'
	 *   handleCharacter: 	Characters		- Plain Text, etc.
	 * 
	 */
	
	/**
	 * Handles open group tokens. ({)
	 */
	public int handleOpenGroup() {
		int result = errOK;
		this.openGroupCount++;	// stats
		if (this.getTokeniserState() == TOKENISER_SKIP_GROUP) { 
			this.groupSkippedCount++;
		}

		this.groupLevel++;		// current group level in tokeniser
		this.docGroupLevel++;	// current group level in document
		this.newGroup = true;
		
		this.stackState.push(this.currentState);
		this.currentState = new RtfParserState(this.currentState);
		if(debugParser) {
			this.rtfDoc.add(new RtfDirectContent("\n DEBUG: handleOpenGroup()"));
			this.rtfDoc.add(new RtfDirectContent("\n DEBUG: grouplevel=" + Integer.toString(groupLevel) + "\n"));
		}
		return result;
	}
	
	/**
	 * Handles close group tokens. (})
	 */
	public int handleCloseGroup() {
		int result = errOK;
		this.closeGroupCount++;	// stats

		if (this.getTokeniserState() != TOKENISER_SKIP_GROUP) {
			if(debugParser) {
				this.rtfDoc.add(new RtfDirectContent("\n DEBUG: handleCloseGroup()"));
				this.rtfDoc.add(new RtfDirectContent("\n DEBUG: grouplevel=" + Integer.toString(groupLevel)));
				RtfCtrlWordBase kwd = (RtfCtrlWordBase)this.getControlwordHandler();
				this.rtfDoc.add(new RtfDirectContent("\n DEBUG: ControlWordHandler=[" + kwd.toString() + "]"));
				kwd = (RtfCtrlWordBase)this.getGroupHandler();
				this.rtfDoc.add(new RtfDirectContent("\n DEBUG: GroupHandler=[" + kwd.toString() + "]"));
				this.rtfDoc.add(new RtfDirectContent("\n "));
			}

			RtfCtrlWordBase kwd = (RtfCtrlWordBase)this.getControlwordHandler();
			if(kwd != null) {
				kwd.handleEndControlWord();
			}
			
			kwd = (RtfCtrlWordBase)this.getGroupHandler();
			if(kwd != null) {
				kwd.handleCloseGroup();
			}
		}
		
		if(this.stackState.size() >0 ) {
			this.currentState = (RtfParserState)this.stackState.pop();
		} else {
			result = errStackUnderflow;
		}
		
		this.docGroupLevel--;
		this.groupLevel--;
		
		if (this.getTokeniserState() == TOKENISER_SKIP_GROUP && this.groupLevel < this.skipGroupLevel) {
			this.setTokeniserState(TOKENISER_NORMAL);
		}

		return result;	
	}
	

	/**
	 * Handles control word tokens. Depending on the current
	 * state a control word can lead to a state change. When
	 * parsing the actual document contents, certain tabled
	 * values are remapped. i.e. colors, fonts, styles, etc.
	 * 
	 * @param kwdParam The control word to handle.
	 */
	public int handleCtrlWord(RtfCtrlWordData kwdParam) {
		this.ctrlWordCount++; // stats

		if (this.getTokeniserState() == TOKENISER_SKIP_GROUP) { 
			this.ctrlWordSkippedCount++;
			return errOK;
		}
		
		int result = this.rtfKeywordMgr.handleKeyword(kwdParam, this.groupLevel); 
		if( result == errOK) {
			this.ctrlWordHandledCount++;
		} else {
			this.ctrlWordNotHandledCount++;
			result = errOK;	// hack for now.
		}
		return result;
	}

	/**
	 * Handles text tokens. These are either handed on to the
	 * appropriate destination handler.
	 * 
	 * @param nextChar
	 * 		The text token to handle.
	 */
	public int handleCharacter(char[] nextChar) {		
		this.characterCount++;	// stats

		if (this.getTokeniserState() == TOKENISER_SKIP_GROUP) { 
			return errOK;
		}

		boolean handled = false;

		RtfDestination dest = (RtfDestination)this.getCurrentDestination();
		if(dest != null) {
			handled = dest.handleCharacter(nextChar);
		}

		return errOK;
	}

	/**
	 * Get the state of the parser.
	 *
	 * @return
	 * 		The current RtfParserState state object.
	 */
	public RtfParserState getState(){
		return this.currentState;
	}	

	/**
	 * Get the current state of the parser.
	 * 
	 * @return 
	 * 		The current state of the parser.
	 */
	public int getParserState(){
		return this.currentState.parserState;
	}
	
	/**
	 * Set the state value of the parser.
	 *
	 * @param newState
	 * 		The new state for the parser
	 * @return
	 * 		The state of the parser.
	 */
	public int setParserState(int newState){
		this.currentState.parserState = newState;
		return this.currentState.parserState;
	}
	
	/**
	 * Get the primary handler for this group.
	 * The primary handler is used after all the group options and text have been set appropriately.
	 * 
	 * @return
	 * 		Returns KwdBase object that is the current group handler
	 */
	public Object getGroupHandler() {
		return this.currentState.groupHandler;
	}
	
	/**
	 * Set the primary handler for this group.
	 * The primary handler is used after all the group options and text have been set appropriately.
	 * 
	 * @param kwdbase
	 */
	public void setGroupHandler(Object kwdbase) {
		this.currentState.groupHandler = kwdbase;
	}
	
	/**
	 * Set the handler for this control word.
	 * The handler is invoked when a new control word is found.
	 * 
	 * @param kwdbase
	 */
	public void setControlWordHandler(RtfCtrlWordBase kwd) {
		if(this.currentState.ctrlWordHandler != null) {
			((RtfCtrlWordBase)this.currentState.ctrlWordHandler).handleEndControlWord();
		}
		this.currentState.ctrlWordHandler = kwd;
	}

	/**
	 * Get the handler for this control word.
	 * The handler is invoked when a new control word is found.
	 * 
	 * @return
	 * 	 Object representing the current handler
	 */
	public Object getControlwordHandler() {
		return this.currentState.ctrlWordHandler;
	}

	/**
	 * Get the conversion type.
	 * 
	 * @return
	 * 		The type of the conversion. Import or Convert.
	 */
	public int getConversionType() {
		return this.conversionType;
	}
	
	/**
	 * Get the RTF Document object.
	 * @return
	 * 		Returns the object rtfDoc.
	 */
	public RtfDocument getRtfDocument() {
		return this.rtfDoc;
	}
	
	/**
	 * Get the Document object.
	 * @return
	 * 		Returns the object rtfDoc.
	 */
	public Document getDocument() {
		return this.document;
	}

	/**
	 * Get the RtfImportHeader object.
	 * @return
	 * 		Returns the object importHeader.
	 */
	public RtfImportMgr getImportManager() {
		return importMgr;
	}
	
	
	/////////////////////////////////////////////////////////////
	// accessors for destinations
	/**
	 * Set the current destination object for the current state.
	 * @param dest The destination value to set.
	 */
	public void setCurrentDestination(RtfDestination dest) {
		this.currentState.destination = dest;
		return;
	}
	/**
	 * Get the current destination object.
	 * 
	 * @return The current state destination
	 */
	public RtfDestination getCurrentDestination() {
		return this.currentState.destination;
	}
	
	////////////////////////////////////////////////////////////
	//
	/**
	 * Helper method to set destination to the List Table destination.
	 */
	public void setDestinationListTable() {	
	}
	/**
	 * Helper method to set destination to the document destination.
	 */
	public void setDestinationDocument() {
		this.setCurrentDestination(this.destDocument);
	}
	/**
	 * Helper method to set destination to the NULL destination.
	 */
	public void setDestinationNull() {
		this.setCurrentDestination(this.destNull);
	}
	/**
	 * Helper method to set destination to the Font Table destination.
	 */
	public void setDestinationFontTable() {
		this.setCurrentDestination(this.destFontTable);
	}
	/**
	 * Helper method to set destination to the Color Table destination.
	 */
	public void setDestinationColorTable() {
		this.setCurrentDestination(this.destColorTable);
	}
	/**
	 * Helper method to set destination to the Info Table destination.
	 */
	public void setDestinationInfo() {
		this.setCurrentDestination(this.destInfo);
	}
	/**
	 * Helper method to set destination to the Stylesheet Table destination.
	 */
	public void setDestinationStylesheetTable() {
		this.setCurrentDestination(this.destStylesheetTable);
	}
	/**
	 * Get the <code>RtfDestinationFontTable</code> object.
	 * @return destFontTable
	 */
	public RtfDestinationFontTable getDestFontTable() {
		return this.destFontTable;
	}
	/**
	 * Get the <code>RtfColorTableParser</code>o bject.
	 * @return destColorTable
	 */
	public RtfDestinationColorTable getDestColorTable() {
		return this.destColorTable;
	}
	/**
	 * Get the <code>RtfDestinationInfo</code>o bject.
	 * @return destInfo
	 */
	public RtfDestinationInfo getDestInfo() {
		return this.destInfo;
	}
	/**
	 * Get the <code>RtfDestinationStylesheetTable</code>o bject.
	 * @return destStylesheetTable
	 */
	public RtfDestinationStylesheetTable getDestStylesheet() {
		return this.destStylesheetTable;
	}

	// TODO: add additional destinations
	/**
	 * Helper method to determine if this is a new group.
	 * @return true if this is a new group, otherwise it returns false.
	 */
	public boolean isNewGroup() {
		return this.newGroup;
	}
	/**
	 * Helper method to set the new group flag
	 * @param value The boolean value to set the flag
	 * @return The value of newGroup
	 */
	public boolean setNewGroup(boolean value) {
		this.newGroup = value;
		return this.newGroup;
	}
	
	/* ************
	 *  TOKENISER *
	 **************/
	
	/**
	 * Read through the input file and parse the data stream into tokens.
	 */	
	public void tokenise() throws IOException {
		int errorCode = errOK;	// error code
		char[] nextChar = new char[1]; // input variable
		nextChar[0]=0;	// set to 0
		this.setTokeniserState(TOKENISER_NORMAL);	// set initial tokeniser state
		
		
		while(this.pbReader.read(nextChar) != -1) {
			this.byteCount++;
			
	        if (this.getTokeniserState() == TOKENISER_BINARY)                      // if we're parsing binary data, handle it directly
	        {
	            if ((errorCode = parseChar(nextChar)) != errOK)
	                return; 
	        }  else {
				switch(nextChar[0]) {
					case '{':	// scope delimiter - Open
						this.handleOpenGroup();
						break;
					case '}':  // scope delimiter - Close
						this.handleCloseGroup();
						break;
					case 0x0a:	// noise character
					case 0x0d:	// noise character
//						if(this.isImport()) {
//							this.rtfDoc.add(new RtfDirectContent(new String(nextChar)));
//						}
						break;
					case '\\':	// Control word start delimiter
							if(parseCtrlWord(pbReader) != errOK) {
							// TODO: Indicate some type of error
							return;
						}
						break;
					default:
						if(groupLevel == 0) { // BOMs
							break;
						}
						if(this.getTokeniserState() == TOKENISER_HEX) {
							StringBuffer hexChars = new StringBuffer();
							hexChars.append(nextChar);
							if(pbReader.read(nextChar) == -1) {
								return;
							}
							this.byteCount++;
							hexChars.append(nextChar);
	                    	try {
								nextChar[0]=(char)Integer.parseInt(hexChars.toString(), 16);
							} catch (NumberFormatException e) {
								return;
							}
		                    this.setTokeniserState(TOKENISER_NORMAL);
						}
					
						if ((errorCode = parseChar(nextChar)) != errOK) {
                        	return; // some error occurred. we should send a
									// real error
						}
						break;
				}	// switch(nextChar[0])
			}	// end if (this.getTokeniserState() == TOKENISER_BINARY)
		}// end while(reader.read(nextChar) != -1)
	}
	
	/**
	 * Process the character and send it to the current destination.
	 * @param ch
	 * 		The character to process
	 * @return
	 * 		Returns an error code or errOK if no error.
	 */
	private int parseChar(char[] ch) {
		// figure out where to put the character
		// needs to handle group levels for parsing
		// examples
		/*
		 * {\f3\froman\fcharset2\fprq2{\*\panose 05050102010706020507}Symbol;}
		 * {\f7\fswiss\fcharset0\fprq2{\*\panose 020b0604020202030204}Helv{\*\falt Arial};} <- special case!!!!
		 * {\f5\froman\fcharset0 Tahoma;}
		 * {\f6\froman\fcharset0 Arial Black;}
		 * {\info(\author name}{\company company name}}
		 * ... document text ...
		 */
	    if (this.getTokeniserState() == TOKENISER_BINARY && --binByteCount <= 0)
	    	this.setTokeniserStateNormal();
	    if (this.getTokeniserState() == TOKENISER_SKIP_BYTES && --binSkipByteCount <= 0)
	    	this.setTokeniserStateNormal();
	    return this.handleCharacter(ch);
	}
	
	/**
	 * Parses a keyword and it's parameter if one exists
	 * @param reader
	 * 		This is a pushback reader for file input.
	 * @return
	 * 		Returns an error code or errOK if no error.
	 * @throws IOException
	 * 		Catch any file read problem.
	 */
	private int parseCtrlWord(PushbackReader reader) throws IOException {
		char[] nextChar = new char[1];
		
		if(reader.read(nextChar) == -1) {
				return errEndOfFile;
		}
		this.byteCount++;

		StringBuffer parsedCtrlWord = new StringBuffer();
		StringBuffer parsedParam= new StringBuffer();
		RtfCtrlWordData ctrlWordParam = new RtfCtrlWordData();
		
		if(!Character.isLetterOrDigit(nextChar[0])) {
			parsedCtrlWord.append(nextChar[0]);
			ctrlWordParam.ctrlWord = parsedCtrlWord.toString();
			return this.handleCtrlWord(ctrlWordParam);
		}
		
//		for( ; Character.isLetter(nextChar[0]); reader.read(nextChar) ) {
//			parsedCtrlWord.append(nextChar[0]);
//		}
		do {
			parsedCtrlWord.append(nextChar[0]);
			//TODO: catch EOF
			reader.read(nextChar);
			this.byteCount++;
		} while  (Character.isLetter(nextChar[0]));
		
		ctrlWordParam.ctrlWord = parsedCtrlWord.toString();

		if(nextChar[0] == '-') {
			ctrlWordParam.isNeg = true;
			if(reader.read(nextChar) == -1) {
					return errEndOfFile;
			}
			this.byteCount++;
		}
		
		if(Character.isDigit(nextChar[0])) {
			ctrlWordParam.hasParam = true;
//			for( ; Character.isDigit(nextChar[0]); reader.read(nextChar) ) {
//				parsedParam.append(nextChar[0]);
//			}
			do {
				parsedParam.append(nextChar[0]);
				//TODO: catch EOF
				reader.read(nextChar);
				this.byteCount++;
			} while  (Character.isDigit(nextChar[0]));
			
			ctrlWordParam.param = parsedParam.toString();
		}
		
		// push this character back into the stream
		if(nextChar[0] != ' ' || this.isImport() ) {
			reader.unread(nextChar);
		}
		
	    if(debugParser) {
	//	    // debug: insrsid6254399
	//	    if(ctrlWordParam.ctrlWord.equals("proptype") && ctrlWordParam.param.equals("30")) {
	//	    	System.out.print("Debug value found\n");
	//	    }
	//	    if(ctrlWordParam.ctrlWord.equals("panose") ) {
	//	    	System.out.print("Debug value found\n");
	//	    }
	    }
	    
		return this.handleCtrlWord(ctrlWordParam);
	}
	
	/**
	 * Set the current state of the tokeniser.
	 * @param value The new state of the tokeniser.
	 * @return The state of the tokeniser.
	 */
	public int setTokeniserState(int value) {
		this.currentState.tokeniserState = value;
		return this.currentState.tokeniserState;
	}
	
	/**
	 * Get the current state of the tokeniser.
	 * @return The current state of the tokeniser.
	 */
	public int getTokeniserState() {
		return this.currentState.tokeniserState;
	}

	/**
	 * Gets the current group level
	 * 
	 * @return
	 * 		The current group level value.
	 */
	public int getLevel() {
		return this.groupLevel;
	}
	

	/**
	 * Set the tokeniser state to skip to the end of the group.
	 * Sets the state to TOKENISER_SKIP_GROUP and skipGroupLevel to the current group level.
	 */
	public void setTokeniserStateNormal() {
		this.setTokeniserState(TOKENISER_NORMAL);
		return;
	}

	/**
	 * Set the tokeniser state to skip to the end of the group.
	 * Sets the state to TOKENISER_SKIP_GROUP and skipGroupLevel to the current group level.
	 */
	public void setTokeniserStateSkipGroup() {
		this.setTokeniserState(TOKENISER_SKIP_GROUP);
		this.skipGroupLevel = this.groupLevel;
		return;
	}
	
	/**
	 * Sets the number of bytes to skip and the state of the tokeniser.
	 * 
	 * @param numberOfBytesToSkip
	 * 			The numbere of bytes to skip in the file.
	 */
	public void setTokeniserSkipBytes(long numberOfBytesToSkip) {
		this.setTokeniserState(TOKENISER_SKIP_BYTES);
		this.binSkipByteCount = numberOfBytesToSkip;
		return;
	}
	
	/**
	 * Sets the number of binary bytes.
	 * 
	 * @param binaryCount
	 * 			The numbere of binary bytes.
	 */
	public void setTokeniserStateBinary(int binaryCount) {
		this.setTokeniserState(TOKENISER_BINARY);
		this.binByteCount = binaryCount;
		return;
	}
	/**
	 * Helper method to determin if conversion is TYPE_CONVERT
	 * @return true if TYPE_CONVERT, otherwise false
	 * @see com.lowagie.text.rtf.direct.RtfParser#TYPE_CONVERT
	 */
	public boolean isConvert() {
		return (this.getConversionType() == RtfParser.TYPE_CONVERT);
	}
	
	/**
	 * Helper method to determin if conversion is TYPE_IMPORT_FULL or TYPE_IMPORT_FRAGMENT
	 * @return true if TYPE_CONVERT, otherwise false
	 * @see com.lowagie.text.rtf.direct.RtfParser#TYPE_IMPORT_FULL
	 * @see com.lowagie.text.rtf.direct.RtfParser#TYPE_IMPORT_FRAGMENT
	 */
	public boolean isImport() {
		return (isImportFull() || this.isImportFragment());
	}
	/**
	 * Helper method to determin if conversion is TYPE_IMPORT_FULL
	 * @return true if TYPE_CONVERT, otherwise false
	 * @see com.lowagie.text.rtf.direct.RtfParser#TYPE_IMPORT_FULL
	 */
	public boolean isImportFull() {
		return (this.getConversionType() == RtfParser.TYPE_IMPORT_FULL);
	}
	/**
	 * Helper method to determin if conversion is TYPE_IMPORT_FRAGMENT
	 * @return true if TYPE_CONVERT, otherwise false
	 * @see com.lowagie.text.rtf.direct.RtfParser#TYPE_IMPORT_FRAGMENT
	 */
	public boolean isImportFragment() {
		return (this.getConversionType() == RtfParser.TYPE_IMPORT_FRAGMENT);
	}
	/**
	 * Helper method to indicate if this control word was a \* control word.
	 * @return true if it was a \* control word, otherwise false
	 */
	public boolean getExtendedDestination() {
		return this.currentState.isExtendedDestination;
	}
	/**
	 * Helper method to set the extended control word flag.
	 * @param value Boolean to set the value to.
	 * @return isExtendedDestination.
	 */
	public boolean setExtendedDestination(boolean value) {
		this.currentState.isExtendedDestination = value;
		return this.currentState.isExtendedDestination;
	}

/*	
 *	Statistics
 *
 	public void printStats(PrintStream out) {
		if(out == null) return;
		
		out.println("");
		out.println("Parser statistics:");
		out.println("Process start date: " + startDate.toLocaleString());
		out.println("Process end date  : " + endDate.toLocaleString());
		out.println("  Elapsed time    : " + Long.toString(endTime - startTime) + " milliseconds.");
		out.println("Total bytes read  : " + Long.toString(byteCount));
		out.println("Open group count  : " + Long.toString(openGroupCount));
		out.print("Close group count : " + Long.toString(closeGroupCount));
		out.println(" (Groups Skipped): " + Long.toString(groupSkippedCount));
		out.print("Control word count: " + Long.toString(ctrlWordCount));
		out.print(" - Handled: " + Long.toString(ctrlWordHandledCount));
		out.print(" Not Handled: " + Long.toString(ctrlWordNotHandledCount));
		out.println(" Skipped: " + Long.toString(ctrlWordSkippedCount));
		out.println("Plain text char count: " + Long.toString(characterCount));		
	}*/
}
