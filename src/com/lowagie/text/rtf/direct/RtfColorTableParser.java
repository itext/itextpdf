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

    public static boolean stringMatches(String text, String start) {
        if (!text.startsWith(start))
            return false;
        int first = start.length();
        int last = text.length();
        if (first == last)
            return false;
        for (int k = first; k < last; ++k) {
            char c = text.charAt(k);
            if (c < '0' || c > '9')
                return false;
        }
        return true;
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
        if (stringMatches(ctrlWord, "\\red"))
            this.red = Integer.parseInt(ctrlWord.substring(4));
        else if (stringMatches(ctrlWord, "\\green"))
            this.green = Integer.parseInt(ctrlWord.substring(6));
        else if (stringMatches(ctrlWord, "\\blue"))
            this.blue = Integer.parseInt(ctrlWord.substring(5));
	}
	
	/**
	 * Handle text content. This is to find the end of each color
	 * definition, because they are separated by a semicolon (;).
	 * 
	 * @param text The text to handle.
	 * @param groupLevel Unused.
	 */
	public void handleText(String text, int groupLevel) {
		if(text.indexOf(';') != -1) {
			if(red != -1 && green != -1 && blue != -1) {
				this.importHeader.importColor(Integer.toString(this.colorNr), new Color(this.red, this.green, this.blue));
			}
			this.colorNr++;
		}
	}
}
