package com.lowagie.text.rtf.direct;

import java.awt.Color;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;

import com.lowagie.text.DocumentException;
import com.lowagie.text.rtf.document.RtfDocument;

/**
 * The RtfParser allows the importing of RTF documents or
 * RTF document fragments. The RTF document or fragment is tokenised,
 * font and color definitions corrected and then added to
 * the document being written.
 * 
 * @version $Revision$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfParser {
	/**
	 * Currently the RTF document header is being parsed.
	 */
	private static final int PARSER_IN_HEADER = 0;
	/**
	 * Currently the RTF font table is being parsed.
	 */
	private static final int PARSER_IN_FONT_TABLE = 1;
	/**
	 * Currently the RTF color table is being parsed.
	 */
	private static final int PARSER_IN_COLOR_TABLE = 2;
	/**
	 * Currently the RTF info group is being parsed.
	 */
	private static final int PARSER_IN_INFO_GROUP = 4;
	/**
	 * Currently the RTF document content is being parsed.
	 */
	private static final int PARSER_IN_DOCUMENT = 8;
	
	/**
	 * The RtfDocument to add the RTF document or fragment to.
	 */
	private RtfDocument rtfDoc = null;
	/**
	 * The RtfTokeniser to use for tokenising the RTF document or fragment.
	 */
	private RtfTokeniser tokeniser = null;
	/**
	 * The RtfImportHeader to store imported font and color mappings in.
	 */
	private RtfImportHeader importHeader = null;
	/**
	 * The RtfFontTableParser to use for parsing the font table.
	 */
	private RtfFontTableParser fontTableParser = null;
	/**
	 * The RtfColorTableParser to use for parsing the color table.
	 */
	private RtfColorTableParser colorTableParser = null;
	/**
	 * The current parser state.
	 */
	private int state = PARSER_IN_HEADER;
	
	/**
	 * Imports a complete RTF document.
	 * 
	 * @param reader The Reader to read the RTF document from.
	 * @param rtfDoc The RtfDocument to add the imported document to.
	 * @throws IOException On I/O errors.
	 * @throws DocumentException On document writing errors.
	 */
	public void importRtfDocument(Reader reader, RtfDocument rtfDoc) throws IOException, DocumentException {
		this.rtfDoc = rtfDoc;
		this.state = PARSER_IN_HEADER;
		this.importHeader = new RtfImportHeader(this.rtfDoc);
		this.fontTableParser = new RtfFontTableParser(this.importHeader);
		this.colorTableParser = new RtfColorTableParser(this.importHeader);
		this.tokeniser = new RtfTokeniser(this, 0);
		this.tokeniser.tokenise(reader);
	}
	
	/**
	 * Imports an RTF fragment.
	 * 
	 * @param reader The Reader to read the RTF fragment from.
	 * @param rtfDoc The RTF document to add the RTF fragment to.
	 * @param importMappings The RtfImportMappings defining font and color mappings for the fragment.
	 * @throws IOException On I/O errors.
	 * @throws DocumentException On document writing errors.
	 */
	public void importRtfFragment(Reader reader, RtfDocument rtfDoc, RtfImportMappings importMappings) throws IOException, DocumentException {
		this.rtfDoc = rtfDoc;
		this.state = PARSER_IN_DOCUMENT;
		this.importHeader = new RtfImportHeader(this.rtfDoc);
		this.fontTableParser = new RtfFontTableParser(this.importHeader);
		this.colorTableParser = new RtfColorTableParser(this.importHeader);
		handleImportMappings(importMappings);
		this.tokeniser = new RtfTokeniser(this, 1);
		this.tokeniser.tokenise(reader);
	}

	/**
	 * Imports the mappings defined in the RtfImportMappings into the
	 * RtfImportHeader of this RtfParser.
	 * 
	 * @param importMappings The RtfImportMappings to import.
	 */
	private void handleImportMappings(RtfImportMappings importMappings) {
		Iterator it = importMappings.getFontMappings().keySet().iterator();
		while(it.hasNext()) {
			String fontNr = (String) it.next();
			this.importHeader.importFont(fontNr, (String) importMappings.getFontMappings().get(fontNr));
		}
		it = importMappings.getColorMappings().keySet().iterator();
		while(it.hasNext()) {
			String colorNr = (String) it.next();
			this.importHeader.importColor(colorNr, (Color) importMappings.getColorMappings().get(colorNr));
		}
	}
	
	/**
	 * Handles open group tokens.
	 * 
	 * @param groupLevel The current group nesting level.
	 * @throws DocumentException On document writing errors.
	 */
	public void handleOpenGroup(int groupLevel) throws DocumentException {
		if(this.state == PARSER_IN_DOCUMENT) {
			this.rtfDoc.add(new RtfDirectContent("{"));
		}
	}
	
	/**
	 * Handles close group tokens. Depending on what is currently
	 * being parsed the parse state may change.
	 * 
	 * @param groupLevel The current group nesting level.
	 * @throws DocumentException On document writing errors.
	 */
	public void handleCloseGroup(int groupLevel) throws DocumentException {
		if(this.state == PARSER_IN_DOCUMENT && groupLevel > 1) {
			this.rtfDoc.add(new RtfDirectContent("}"));
		} else if(this.state == PARSER_IN_INFO_GROUP && groupLevel == 2) {
			this.state = PARSER_IN_DOCUMENT;
		} else if(this.state == PARSER_IN_FONT_TABLE) {
			this.fontTableParser.handleCloseGroup(groupLevel);
			if(groupLevel == 2) {
				this.state = PARSER_IN_HEADER;
			}
		} else if(this.state == PARSER_IN_COLOR_TABLE) {
			this.state = PARSER_IN_HEADER;
		}
	}
	
	/**
	 * Handles single control character tokens.
	 * 
	 * @param ctrlCharacter The control character to handle.
	 * @param groupLevel The current group nesting level.
	 * @throws DocumentException On document writing errors.
	 */
	public void handleCtrlCharacter(String ctrlCharacter, int groupLevel) throws DocumentException {
		if(this.state == PARSER_IN_DOCUMENT) {
			this.rtfDoc.add(new RtfDirectContent(ctrlCharacter));
		}
	}
	
	/**
	 * Handles control word tokens. Depending on the current
	 * state a control word can lead to a state change. When
	 * parsing the actual document contents, The font number,
	 * color number and background color number are remapped.
	 * 
	 * @param ctrlWord The control word to handle.
	 * @param groupLevel The current group nesting level.
	 * @throws DocumentException On document writing errors.
	 */
	public void handleCtrlWord(String ctrlWord, int groupLevel) throws DocumentException {
		if(this.state == PARSER_IN_DOCUMENT) {
			if(ctrlWord.matches("^\\\\f[0-9]+$")) {
				ctrlWord = "\\f" + this.importHeader.mapFontNr(ctrlWord.substring(2));
			} else if(ctrlWord.matches("^\\\\cf[0-9]+$")) {
				ctrlWord = "\\cf" + this.importHeader.mapColorNr(ctrlWord.substring(3));
			} else if(ctrlWord.matches("^\\\\cb[0-9]+$")) {
				ctrlWord = "\\cb" + this.importHeader.mapColorNr(ctrlWord.substring(3));
			}
			this.rtfDoc.add(new RtfDirectContent(ctrlWord));
		} else if(this.state == PARSER_IN_FONT_TABLE) {
			this.fontTableParser.handleCtrlWord(ctrlWord, groupLevel);
		} else if(this.state == PARSER_IN_COLOR_TABLE) {
			this.colorTableParser.handleCtrlWord(ctrlWord, groupLevel);
		} else if(this.state == PARSER_IN_HEADER) {
			if(ctrlWord.equals("\\info")) {
				this.state = PARSER_IN_INFO_GROUP;
			} else if(ctrlWord.equals("\\fonttbl")) {
				this.state = PARSER_IN_FONT_TABLE;
			} else if(ctrlWord.equals("\\colortbl")) {
				this.state = PARSER_IN_COLOR_TABLE;
			}
		}
	}
	
	/**
	 * Handles text tokens. These are either handed on to the
	 * RtfColorTableParser or RtfFontTableParser or added directly
	 * to the document.
	 * 
	 * @param text The text token to handle.
	 * @param groupLevel The current group nesting level.
	 * @throws DocumentException On document writing errors.
	 */
	public void handleText(String text, int groupLevel) throws DocumentException {
		if(this.state == PARSER_IN_DOCUMENT) {
			this.rtfDoc.add(new RtfDirectContent(text));
		} else if(this.state == PARSER_IN_FONT_TABLE) {
			this.fontTableParser.handleText(text, groupLevel);
		} else if(this.state == PARSER_IN_COLOR_TABLE) {
			this.colorTableParser.handleText(text, groupLevel);
		}
	}
}
