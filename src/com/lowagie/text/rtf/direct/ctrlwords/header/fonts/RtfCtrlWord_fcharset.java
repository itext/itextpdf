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
 
package com.lowagie.text.rtf.direct.ctrlwords.header.fonts;

import com.lowagie.text.rtf.direct.RtfParser;
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_fcharset;

/**
 * Description:
 * 	 Specifies the character set of a font in the font table. Values for N are defined by Windows header files:
 * 	0 ANSI
 * 	1 Default
 * 	2 Symbol
 * 	3 Invalid
 * 	77 Mac
 * 	128 Shift Jis
 * 	129 Hangul
 * 	130 Johab
 * 	134 GB2312
 * 	136 Big5
 * 	161 Greek
 * 	162 Turkish
 * 	163 Vietnamese
 * 	177 Hebrew
 * 	178 Arabic
 * 	179 Arabic Traditional
 * 	180 Arabic user
 * 	181 Hebrew user
 * 	186 Baltic
 * 	204 Russian
 * 	222 Thai
 * 	238 Eastern European
 * 	254 PC 437
 * 	255 OEM
 * Group:
 * 	Font Table
 * Type:
 * 	Value
 * RTF Version:
 * 	
 */

public class RtfCtrlWord_fcharset extends RtfCtrlWordBase_fcharset {

	public RtfCtrlWord_fcharset(RtfParser rtfParser){
		super(rtfParser);
		this.defaultParserState = RtfParser.PARSER_IN_FONT_TABLE_INFO;
	}

	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.ctrlwords.KwdBase#handleParserInFontTableInfo()
	 */
	protected boolean handleParserInFontTableInfo() {
		//this.rtfParser.getDestFontTable().setCharset("0");
		this.rtfParser.getDestFontTable().setCharset(this.ctrlWordData.param);
		return true;
	}

}
