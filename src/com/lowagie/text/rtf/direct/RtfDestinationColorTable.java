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

/**
 * <code>RtfDestinationDocument</code> handles data destined for the color table destination
 * according to the RTF Specification.
 * 
 * @author Howard Shank (hgshank@yahoo.com)
 *
 */
public class RtfDestinationColorTable extends RtfDestinationBase  {
	/**
	 * The RtfImportHeader to add color mappings to.
	 */
	private RtfImportMgr importHeader = null;
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
	public RtfDestinationColorTable(RtfImportMgr importHeader) {
		this.importHeader = importHeader;
		this.clear();
	}
	/**
	 * Reset this object color values.
	 */
	protected void clear() {
		this.red = -1;
		this.green = -1;
		this.blue = -1;
		// do not reset colorNr
	}
	/**
	 * Set the red color to value.
	 * @param value Value to set red to.
	 */
	public void setRed(int value) {
		this.red = value;
	}
	/**
	 * Set the green color value.
	 * @param value Value to set green to.
	 */
	public void setGreen(int value) {
		this.green = value;
	}
	/**
	 * Set the blue color value.
	 * @param value Value to set blue to.
	 */
	public void setBlue(int value) {
		this.blue = value;		
	}
	
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#closeDestination()
	 */
	public boolean closeDestination() {
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
		processColor();
		return true;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#handleGroupStart()
	 */
	public boolean handleGroupStart() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#handleText(char)
	 */
	public boolean handleText(char ch) {
		if(ch == ';') {
			this.processColor();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.IRtfDestination#handleText(java.lang.String)
	 */
	public boolean handleText(String text) {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.RtfDestinationBase#handleCharacter(char[])
	 */
	public boolean handleCharacter(char[] ch) {
		if(ch[0] == ';') {
			this.processColor();
		}
		return true;
	}
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.RtfDestinationBase#setDefaults()
	 */
	public void setDefaults() {
	}
	/**
	 * Processes the color triplet parsed from the document.
	 * Add it to the import mapping so colors can be mapped when encountered
	 * in the RTF import or conversion.
	 *
	 */
	private void processColor() {
		if(red != -1 && green != -1 && blue != -1) {
			this.importHeader.importColor(Integer.toString(this.colorNr), new Color(this.red, this.green, this.blue));
			this.clear();
		}
		this.colorNr++;
	}
}
