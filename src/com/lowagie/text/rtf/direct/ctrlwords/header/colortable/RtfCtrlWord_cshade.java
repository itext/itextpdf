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
 
package com.lowagie.text.rtf.direct.ctrlwords.header.colortable;

import com.lowagie.text.rtf.direct.RtfParser;
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_cshade;

/**
 * Description:
 * 	,"Specifies the shade of the given theme when specifying a theme color. If the entry references a theme color, \cshadeN specifies its shade. If not, \cshadeN is ignored.
 * 	Acceptable values for this control wordâ€™s parameter are integers from 0 to 255 inclusive; where 255 means no shade, and 0 means full shade (resulting in black color).
 * 	If this control word is not specified, a value of 255 is implied.
 * 	Note   If the parameter of this control word is less than 255, the parameter of the ctint control word must be equal to 255.  A tint or a shade, may be specified, but not both.
 * Group:
 * 	Color Table
 * Type:
 * 	Value
 * RTF Version:
 * 	
 */

public class RtfCtrlWord_cshade extends RtfCtrlWordBase_cshade {

	public RtfCtrlWord_cshade(RtfParser rtfParser){
		super(rtfParser);
		this.defaultParserState = RtfParser.PARSER_IN_COLOR_TABLE;
	}
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.ctrlwords.KwdBase#handleControlWord()
	 */
	public boolean handleControlWord() {
		// if we're doing an import, let rtfDoc put the control word in the document.
		if(this.rtfParser.getConversionType() == RtfParser.TYPE_IMPORT_FULL) {
			return true;	// don't do anything because the rtfDoc handles this statically
		}
		// TODO Set the document code page here.
		// the code page value is in the parameter.
		
		return true;
	}
}
