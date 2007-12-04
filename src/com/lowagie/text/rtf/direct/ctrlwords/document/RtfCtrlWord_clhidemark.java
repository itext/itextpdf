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
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_clhidemark;

/**
 * Description:
 * 	,"This control word specifies whether the end of cell glyph
 * 	shall influence the height of the given table row in the
 * 	table. If it is specified, then only printing characters in
 * 	this cell shall be used to determine the row height.
 *
 * 		Note   Typically, the height of a table row is determined
 * 	by the height of all glyphs in all cells in that row,
 * 	including the non-printing end of cell glyph characters.
 * 	However, if these characters are not formatted, they are
 * 	always created with the document default style properties.
 * 	This means that the height of a table row cannot ever be
 * 	reduced below the size of the end of cell marker glyph
 * 	without manually formatting each paragraph in that run.
 *
 * 		In a typical document, this behavior is desirable as it
 * 	prevents table rows from 'disappearing' if they have no
 * 	content. However, if a table row is being used as a border
 * 	(for example, by shading its cells or putting an image in
 * 	them), then this behavior makes it impossible to have a
 * 	virtual border that is reasonably small without formatting
 * 	each cell's content directly. This setting specifies that
 * 	the end of cell glyph shall be ignored for this cell,
 * 	allowing it to collapse to the height of its contents
 * 	without formatting each cell's end of cell marker, which
 * 	would have the side effect of formatting any text ever
 * 	entered into that cell.
 * 	If this control word is omitted,
 * 	then the end of cell marker shall be included in the
 * 	determination of the height of this row.
 * 	Example:
 * 	Consider the following RTF table:
 * 	Notice that the only
 * 	printing content in this table row is displayed using 5
 * 	point font, yet the row height is influenced by the end of
 * 	cell markers in the empty cells.
 * 	If each cell in the
 * 	second row in this table was set to exclude the table cell
 * 	from this calculation, using the following RTF: \clhidemark,
 * 	the resulting table shall exclude the cell markers from the
 * 	row height calculation:
 * 	The \clhidemark control word
 * 	specified that each cell marker was excluded, resulting in
 * 	the row height being defined by the actual run contents.
 * Group:
 * 	Table Definitions
 * Type:
 * 	Flag
 * Default Param:
 * 	0
 * Pass Default:
 * 	false
 * RTF Version:
 * 	
 */

public class RtfCtrlWord_clhidemark extends RtfCtrlWordBase_clhidemark {

	public RtfCtrlWord_clhidemark(RtfParser rtfParser){
		super(rtfParser);
	}

}
