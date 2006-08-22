package com.lowagie.text.rtf.direct;

import java.awt.Color;

/**
 * The RtfColorTableParser handles the events generated
 * by the RtfTokeniser while the RTF color table is
 * being parsed.
 * 
 * @version $Revision$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 */
public class RtfColorTableParser {
	/**
	 * The RtfImportHeader to add color mappings to.
	 */
	private RtfImportHeader importHeader = null;
	/**
	 * The number of the current color being parsed.
	 */
	private int colorNr = 0;
	/**
	 * The red component of the current color being parsed.
	 */
	private int red = -1;
	/**
	 * The green component of the current color being parsed.
	 */
	private int green = -1;
	/**
	 * The blue component of the current color being parsed.
	 */
	private int blue = -1;
	
	/**
	 * Constructs a new RtfColorTableParser.
	 * 
	 * @param importHeader The RtfImportHeader to add the color mappings to.
	 */
	public RtfColorTableParser(RtfImportHeader importHeader) {
		this.importHeader = importHeader;
		this.colorNr = 0;
		this.red = -1;
		this.green = -1;
		this.blue = -1;
	}

	/**
	 * Handle RTF control words. The relevant control words are
	 * \red, \green and \blue each with a number specifying the
	 * value for that component.
	 * 
	 * @param ctrlWord The control word to handle.
	 * @param groupLevel Unused
	 */
	public void handleCtrlWord(String ctrlWord, int groupLevel) {
		if(ctrlWord.matches("^\\\\red[0-9]+$")) {
			this.red = Integer.parseInt(ctrlWord.substring(4));
		} else if(ctrlWord.matches("^\\\\green[0-9]+$")) {
			this.green = Integer.parseInt(ctrlWord.substring(6));
		} else if(ctrlWord.matches("^\\\\blue[0-9]+$")) {
			this.blue = Integer.parseInt(ctrlWord.substring(5));
		}
	}
	
	/**
	 * Handle text content. This is to find the end of each color
	 * definition, because they are separated by a semicolon (;).
	 * 
	 * @param text The text to handle.
	 * @param groupLevel Unused.
	 */
	public void handleText(String text, int groupLevel) {
		if(text.indexOf(";") != -1) {
			if(red != -1 && green != -1 && blue != -1) {
				this.importHeader.importColor(Integer.toString(this.colorNr), new Color(this.red, this.green, this.blue));
			}
			this.colorNr++;
		}
	}
}
