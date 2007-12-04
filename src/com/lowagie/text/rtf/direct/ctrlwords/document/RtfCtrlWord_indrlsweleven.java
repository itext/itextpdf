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
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_indrlsweleven;

/**
 * Description:
 * 	,"This control word specifies whether applications should
 * 	ignore the presence of floating objects when calculating the
 * 	starting position of paragraphs that are wrapped around
 * 	floating objects defined using the Vector Mark-up Language
 * 	(VML) syntax.
 * 	Typically, the presence of a floating
 * 	object on the same line or lines as a paragraph shall only
 * 	affect the text when the floating object occurs where that
 * 	text would normally be presented.
 * 	Example: Text at a 1
 * 	indentation would only be displaced by a floating object
 * 	that appears at that position and not one that appears from
 * 	0 to 0.5 on the same line.
 * 	This control word specifies
 * 	that floating objects shall always impact paragraphs on the
 * 	same line in two ways:
 * 	1. If the paragraph is not
 * 	numbered, then it shall tightly wrap any floating object
 * 	that precedes it on the same line, ignoring its own
 * 	indentation settings.
 * 	Example: A paragraph with a 1 left
 * 	indent shall tightly wrap a floating object that appears at
 * 	only 0.25 on the same line.
 * 	2. If the paragraph is
 * 	numbered, then it shall calculate and use its full indent
 * 	relative to the edge of the floating object, not relative to
 * 	the edge of the page.
 * 	Example: A numbered paragraph with
 * 	a 1 left indent shall appear 1.5 into the page if it is
 * 	preceded by a floating object that appears at 0.5 on the
 * 	same line.
 * 	Example: Consider an RTF document with a
 * 	narrow floating object at 0.5 on the page, surrounded by
 * 	both numbered and unnumbered paragraphs.
 * 	The default
 * 	presentation would have no impact on the paragraphs based on
 * 	that floating object, since the two do not intersect:
 *
 * 		However, if this control word was present, then the two
 * 	alternate rules defined above would apply, resulting in the
 * 	following output:
 * 	Note   This control word is used to
 * 	maintain compatibility with documents created by Microsoft
 * 	Office Word 2003.
 * Group:
 * 	Document Formatting Properties
 * Type:
 * 	Flag
 * Default Param:
 * 	0
 * Pass Default:
 * 	false
 * RTF Version:
 * 	
 */

public class RtfCtrlWord_indrlsweleven extends RtfCtrlWordBase_indrlsweleven {

	public RtfCtrlWord_indrlsweleven(RtfParser rtfParser){
		super(rtfParser);
	}

}
