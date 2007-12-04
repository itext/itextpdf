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

/**
 * <code>RtfDestinationDocument</code> handles data destined for the font table destination
 * according to the RTF Specification.
 * 
 * @author Howard Shank (hgshank@yahoo.com)
 *
 */
public final class RtfDestinationFontTable extends RtfDestinationBase {
	/**
	 * The RtfImportHeader to add font mappings to.
	 */
	private RtfImportMgr importHeader = null;
	
	/**
	 * The theme (Office 2007)
	 */
	private String themeFont = "";
	/**
	 * The number of the font being parsed.
	 */
	private String fontNr = "";
	/**
	 * The family of the font being parsed.
	 */
	private String fontFamily = "";
	/**
	 * The \charset value
	 */
	private String charset = "";
	/**
	 * The \fprq
	 */
	private int fprq = 0;
	/**
	 * The \*\panose font matching value if primary font is not available.
	 */
	private String panose = "";
	/**
	 * The \*\fname
	 */
	private String nontaggedname = "";
	/**
	 * The name of the font being parsed.
	 */
	private String fontName = "";
	/**
	 * The \falt alternate font if primary font is not available.
	 */
	private String falt = "";
	/**
	 * The \falt alternate font if primary font is not available.
	 */
	private String fontemb = "";
	/**
	 * The \falt alternate font if primary font is not available.
	 */
	private String fontType = "";
	/**
	 * The \falt alternate font if primary font is not available.
	 */
	private String fontFile = "";
	/**
	 * The \falt alternate font if primary font is not available.
	 */
	private String fontFileCpg = "";
	/**
	 * The \fbias
	 */
	private int fbias = 0;

	
	/**
	 * Constructs a new RtfFontTableParser.
	 * 
	 * @param importHeader The RtfImportHeader to add font mappings to.
	 */
	public RtfDestinationFontTable(RtfImportMgr importHeader) {
		this.importHeader = importHeader;
		clear();
	}
	
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.RtfDestinationBase#clear()
	 */
	protected void clear() {
		this.themeFont = "";
		this.fontNr = "";
		this.fontName = "";
		this.fontFamily = "";
		
		this.charset = "";
		this.fprq = 0;
		this.panose = "";
		this.nontaggedname = "";
		this.falt = "";
		this.fontemb = "";
		this.fontType = "";
		this.fontFile = "";
		this.fontFileCpg = "";
		this.fbias = 0;
	}
	/**
	 * Set the font name to the parsed value.
	 * 
	 * @param fontName The font name.
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	/**
	 * Set the font family to the parsed value.
	 * 
	 * @param fontFamily The font family.
	 */
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}
	/**
	 * Set the font number to the parsed value.
	 * This is used for mapping fonts to the new font numbers
	 * 
	 * @param fontNr The font number.
	 */
	public void setFontNumber(String fontNr) {
		this.fontNr = fontNr;
	}
	/**
	 * Set the alternate font name.
	 * 
	 * @param fontAlternate The falt font value
	 */
	public void setFontAlternate(String fontAlternate) {
		this.falt = fontAlternate;
	}
	/**
	 * Set the character-set to the parsed value.
	 * 
	 * @param charset The charset value
	 */
	public void setCharset(String charset) {
		if(charset.length() == 0) {
			charset = "0";
		}
		this.charset = charset;
	}

	// Interface definitions
	
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#closeDestination()
	 */
	public boolean closeDestination() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#handleBinaryData(byte[])
	 */
	public boolean handleBinaryData(byte[] binData) {

		return false;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#handleControlWord(java.lang.String, int)
	 */
	public boolean handleControlWord(String ctrlWord, int param) {

		return false;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#handleControlWord(java.lang.String)
	 */
	public boolean handleControlWord(String ctrlWord) {

		return false;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#handleGroupEnd()
	 */
	public boolean handleGroupEnd() {
		processFont();

		return false;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#handleGroupStart()
	 */
	public boolean handleGroupStart() {

		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.RtfDestinationBase#handleCharacter(char[])
	 */
	public boolean handleCharacter(char[] ch) {
		this.fontName += ch[0];
		return true;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#handleText(char)
	 */
	public boolean handleText(char ch) {

		return false;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#handleText(java.lang.String)
	 */
	public boolean handleText(String text) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.RtfDestinationBase#setDefaults()
	 */
	public void setDefaults() {
	}
	/**
	 * Process the font information that was parsed from the input.
	 */
	private void processFont() {
		//TODO: If primary font fails, use the alternate
		this.fontName = this.fontName.trim();
		if(this.fontNr.length() > 0 && this.fontName.length() > 0) {
			if(fontName.indexOf(";") >= 0) {
				fontName = fontName.substring(0,fontName.indexOf(";"));
			}

			//TODO: Problem: RtfFont defaults family to \froman and doesn't allow any other family.
			// if you set the family, it changes the font name and not the family in the Font.java class.
			
//			if(this.fontFamily.length() > 0) {
//				if(this.importHeader.importFont(this.fontNr, this.fontName, this.fontFamily, Integer.parseInt(this.charset)) == false) {
//					if(this.falt.length() > 0) {
//						this.importHeader.importFont(this.fontNr, this.falt, this.fontFamily, Integer.parseInt(this.charset));
//					}
//				}
//			} else {
				if(this.importHeader.importFont(this.fontNr, this.fontName, Integer.parseInt(this.charset)) == false) {
					if(this.falt.length() > 0) {
						this.importHeader.importFont(this.fontNr, this.falt, Integer.parseInt(this.charset));
					}
				}
//			}
			clear();
		}
	}
}
