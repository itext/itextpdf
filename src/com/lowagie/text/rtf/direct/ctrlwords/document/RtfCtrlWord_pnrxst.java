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
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_pnrxst;

/**
 * Description:
 * 	,"The keywords \pnrxst, \pnrrgb, \pnrpnbr, and \pnrnfc
 * 	describe the deleted number text for the paragraph number.
 * 	Their values are binary. Each of these keywords is
 * 	represented as an array. The deleted number is written out
 * 	with a \pnrstart keyword, followed by the array?s keyword,
 * 	followed by the first byte of the array, followed by the
 * 	array?s keyword, followed by the second byte of the array?s
 * 	keyword, followed by the array?s keyword, followed by the
 * 	third byte of the array?s keyword. This sequence is followed
 * 	by the \pnrstop keyword.::is a 32-item Unicode character
 * 	array (double bytes for each character) with a length byte
 * 	as the first number?it has the actual text of the number,
 * 	with level place holders written out as digits from 0
 * 	through 8.
 * Group:
 * 	Paragraph Text
 * Type:
 * 	Value
 * Default Param:
 * 	0
 * Pass Default:
 * 	true
 * RTF Version:
 * 	
 */

public class RtfCtrlWord_pnrxst extends RtfCtrlWordBase_pnrxst {

	public RtfCtrlWord_pnrxst(RtfParser rtfParser){
		super(rtfParser);
	}

}
