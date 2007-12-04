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
 
package com.lowagie.text.rtf.direct.ctrlwords.header.listtable;

import com.lowagie.text.rtf.direct.RtfParser;
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_levelnfc;

/**
 * Description:
 * 	,"Specifies the number type for the level:
 * 	0 Arabic (1, 2, 3)
 * 	1 Uppercase Roman numeral (I, II, III)
 * 	2 Lowercase Roman numeral (i, ii, iii)
 * 	3 Uppercase letter (A, B, C)
 * 	4 Lowercase letter (a, b, c)
 * 	5 Ordinal number (1st, 2nd, 3rd)
 * 	6 Cardinal text number (One, Two Three)
 * 	7 Ordinal text number (First, Second, Third)
 * 	10 Kanji numbering without the digit character (*dbnum1)
 * 	11 Kanji numbering with the digit character (*dbnum2)
 * 	12 46 phonetic katakana characters in aiueo order (*aiueo)
 * 	13 46 phonetic katakana characters in iroha order (*iroha)
 * 	14 Double-byte character
 * 	15 Single-byte character
 * 	16 Kanji numbering 3 (*dbnum3)
 * 	17 Kanji numbering 4 (*dbnum4)
 * 	18 Circle numbering (*circlenum)
 * 	19 Double-byte Arabic numbering
 * 	20 46 phonetic double-byte katakana characters (*aiueo*dbchar)
 * 	21 46 phonetic double-byte katakana characters (*iroha*dbchar)
 * 	22 Arabic with leading zero (01, 02, 03, ..., 10, 11)
 * 	23 Bullet (no number at all)
 * 	24 Korean numbering 2 (*ganada)
 * 	25 Korean numbering 1 (*chosung)
 * 	26 Chinese numbering 1 (*gb1)
 * 	27 Chinese numbering 2 (*gb2)
 * 	28 Chinese numbering 3 (*gb3)
 * 	29 Chinese numbering 4 (*gb4)
 * 	30 Chinese Zodiac numbering 1 (* zodiac1)
 * 	31 Chinese Zodiac numbering 2 (* zodiac2)
 * 	32 Chinese Zodiac numbering 3 (* zodiac3)
 * 	33 Taiwanese double-byte numbering 1
 * 	34 Taiwanese double-byte numbering 2
 * 	35 Taiwanese double-byte numbering 3
 * 	36 Taiwanese double-byte numbering 4
 * 	37 Chinese double-byte numbering 1
 * 	38 Chinese double-byte numbering 2
 * 	39 Chinese double-byte numbering 3
 * 	40 Chinese double-byte numbering 4
 * 	41 Korean double-byte numbering 1
 * 	42 Korean double-byte numbering 2
 * 	43 Korean double-byte numbering 3
 * 	44 Korean double-byte numbering 4
 * 	45 Hebrew non-standard decimal
 * 	46 Arabic Alif Ba Tah
 * 	47 Hebrew Biblical standard
 * 	48 Arabic Abjad style
 * 	255 No number
 * Group:
 * 	List Table
 * Type:
 * 	Value
 * RTF Version:
 * 	
 */

public class RtfCtrlWord_levelnfc extends RtfCtrlWordBase_levelnfc {

	public RtfCtrlWord_levelnfc(RtfParser rtfParser){
		super(rtfParser);
	}

}
