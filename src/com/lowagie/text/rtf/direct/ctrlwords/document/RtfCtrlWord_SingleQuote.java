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
 
package com.lowagie.text.rtf.direct.ctrlwords.document;

import com.lowagie.text.rtf.direct.RtfParser;
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_SingleQuote;

/**
 * Description:
 * 	,"An escaped expression (for example, \'hh, \\, or \{) is
 * 	usable in all RTF control words.
 * 	Writer
 * 	In general
 * 	RTF should be written out with all characters above 0x80 in
 * 	the escaped form, \'hh. The following table shows values for
 * 	character codes.
 * 	Character code Write out as
 * 	0x00 <=
 * 	ch < 0x20 Escaped (\'hh)
 * 	0x20 <= ch < 0x80 Raw
 * 	(non-escaped) character
 * 	0x80 <= ch <= 0xFF Escaped
 * 	(\'hh)
 * 	0x5C, 0x7B, 0x7D (special RTF characters \,{, or
 * 	}) Escaped (\'hh)
 * 	Reader
 * 	When the RTF reader
 * 	encounters raw characters in the leading-byte range of the
 * 	double-byte character, it regards the next character as the
 * 	trailing byte of the double-byte character and combines the
 * 	two characters into one double-byte character. The following
 * 	table shows possible byte combinations.
 * 	Leading byte
 * 	Trailing byte Validity
 * 	Escaped Raw (0x20 <= ch <= 0x7f) 
 * 	Valid (standard format for double-byte character)
 *
 * 		Escaped Escaped (other) Valid (standard format for
 * 	double-byte character)
 * 	Raw Raw Valid (RTF-J format for
 * 	double-byte character)
 * 	Raw Escaped Invalid
 * Group:
 * 	Special Characters
 * Type:
 * 	Symbol
 * Default Param:
 * 	0
 * Pass Default:
 * 	false
 * RTF Version:
 * 	
 */

public class RtfCtrlWord_SingleQuote extends RtfCtrlWordBase_SingleQuote {

	public RtfCtrlWord_SingleQuote(RtfParser rtfParser){
		super(rtfParser);
	}

}
