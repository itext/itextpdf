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
 
package com.lowagie.text.rtf.direct.ctrlwords.header.revtable;

import com.lowagie.text.rtf.direct.RtfParser;
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_revtbl;

/**
 * Description:
 * 	Control word	Meaning
 * \*\revtbl	This group consists of subgroups that each identify the author of a revision in the document, as in {Author1;}. This is a destination control word.
Revision conflicts, such as those that result when one author deletes another's additions, are stored as one group, in the following form:
CurrentAuthor\'00\'<length of previous author's name>PreviousAuthor\'00
PreviousRevisionTime
The 4 bytes of the Date/Time (DTTM) structure are emitted as ASCII characters, so values greater than 127 should be emitted as hexadecimal values enclosed in quotation marks.

All time references for revision marks use the following bit field structure, DTTM.
Bit numbers	Information	Range
0�5	Minute	0�59
6�10	Hour	0�23
11�15	Day of month	1�31
16�19	Month	1�12
20�28	Year	= Year - 1900
29�31	Day of week	0 (Sun)�6 (Sat)


 * Group:
 * 	Track Changes
 * Type:
 * 	Destination
 * RTF Version:
 * 	
 */

public class RtfCtrlWord_revtbl extends RtfCtrlWordBase_revtbl {

	public RtfCtrlWord_revtbl(RtfParser rtfParser){
		super(rtfParser);
		this.defaultParserState = RtfParser.PARSER_IN_REV_TABLE;
	}
	/* (non-Javadoc)
	 * @see com.lowagie.text.rtf.direct.ctrlwords.KwdBase#handleControlWord()
	 */
	protected boolean handleControlWord() {
		setParserState(RtfParser.PARSER_IN_REV_TABLE);
		if(this.rtfParser.isImport()) {
			this.rtfParser.setTokeniserStateSkipGroup();
			this.rtfParser.setDestinationNull();
			return true;
		}
		return super.handleControlWord();
	}
}
