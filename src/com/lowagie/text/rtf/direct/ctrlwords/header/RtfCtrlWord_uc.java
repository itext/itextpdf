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
 
package com.lowagie.text.rtf.direct.ctrlwords.header;

import com.lowagie.text.rtf.direct.RtfParser;
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_uc;

/**
 * Description:
 * 	,"This keyword represents the number of bytes corresponding
 * 	to a given \ uN Unicode character. This keyword may be used
 * 	at any time, and values are scoped like character
 * 	properties. That is, a \ ucN keyword applies only to text
 * 	following the keyword, and within the same (or deeper)
 * 	nested braces. On exiting the group, the previous \ uc value
 * 	is restored. The reader must keep a stack of counts seen and
 * 	use the most recent one to skip the appropriate number of
 * 	characters when it encounters a \ uN keyword. When leaving
 * 	an RTF group that specified a \ uc value, the reader must
 * 	revert to the previous value. A default of 1 should be
 * 	assumed if no \ uc keyword has been seen in the current or
 * 	outer scopes.
 * 	A common practice is to emit no ANSI
 * 	representation for Unicode characters within a Unicode
 * 	destination context (that is, inside a \ ud destination).
 * 	Typically, the destination will contain a \ uc0 control
 * 	sequence. There is no need to reset the count on leaving the
 * 	\ ud destination, because the scoping rules will ensure the
 * 	previous value is restored.
 * Group:
 * 	Unicode RTF
 * Type:
 * 	Value
 * Default Param:
 * 	0
 * Pass Default:
 * 	true
 * RTF Version:
 * 	
 */

public class RtfCtrlWord_uc extends RtfCtrlWordBase_uc {

	public RtfCtrlWord_uc(RtfParser rtfParser){
		super(rtfParser);
	}
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.ctrlwords.KwdBase#handleControlWord()
	 */
	protected boolean handleControlWord() {
		// if we're doing an import, put the group tag in the document.
		if(this.rtfParser.isImport()) {
			return true;	// don't do anything because the rtfDoc handles this statically
		}
		// TODO Set the document UC value here.
		
		return super.handleParserInHeader();
	}
}
