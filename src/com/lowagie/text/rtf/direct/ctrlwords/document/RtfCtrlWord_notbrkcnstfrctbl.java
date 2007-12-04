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
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_notbrkcnstfrctbl;

/**
 * Description:
 * 	," This control word specifies whether applications shall
 * 	allow a table row to be split in two when its contents are
 * 	displayed under the following circumstances:
 * 	The table
 * 	row exceeds one page in height (it must be split across two
 * 	pages)
 * 	The table row would need to be split to
 * 	accommodate a floating table also on the page
 * 	This
 * 	control word, when present, specifies that table rows that
 * 	exceed one page in height shall never be split around
 * 	floating tables in the document, and shall instead be
 * 	displayed on the first page below the floating table, even
 * 	if that means that part of the table row is clipped by the
 * 	edge of the page.
 * 	Example: Consider an RTF document with
 * 	a long single table row that must be split across two
 * 	separate pages in the document, to accommodate a floating
 * 	table anchored in the footer, as follows:
 * 	The default
 * 	presentation of this document forces that row to be split as
 * 	needed around that floating table.
 * 	However, if this
 * 	control word is present, then that table row is never split
 * 	around the floating table, so it is always placed below that
 * 	floating table on the page, and allowed to flow off the page
 * 	as needed, resulting in the following output:
 * 	This
 * 	example, while extreme, shows how the row is placed below
 * 	the floating table, rather than breaking around it.
 * 	Note
 * 	  This control word is used to maintain compatibility with
 * 	documents created by Microsoft Office Word 2003.
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

public class RtfCtrlWord_notbrkcnstfrctbl extends RtfCtrlWordBase_notbrkcnstfrctbl {

	public RtfCtrlWord_notbrkcnstfrctbl(RtfParser rtfParser){
		super(rtfParser);
	}

}
