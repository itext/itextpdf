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
import com.lowagie.text.rtf.direct.ctrlwords.basectrlwords.RtfCtrlWordBase_mmaddfieldname;

/**
 * Description:
 * 	,"Specifies the column within a given external data source
 * 	that contains e-mail addresses. This control word is
 * 	specified independently of the field mappings specified for
 * 	a given merged document via the <mmodsofldmpdata> control
 * 	word.
 * 	If this control word is omitted, or if no column
 * 	exists in the data source with this column name, the source
 * 	document specifies that no e-mail address data shall be
 * 	associated with this mail merge.
 * 	Note   This control
 * 	word is generally used to allow you to send in e-mail 
 * 	merged documents resulting from populating the fields within
 * 	a merged document with external data.
 * 	This control word
 * 	is independent of the field mapping that is specified for a
 * 	given merged document via the <mmodsofldmpdata> control
 * 	word. This separation enables applications to e-mail the
 * 	documents resulting from the population of RTF fields with
 * 	external data regardless of the presence or absence of a
 * 	field mapped to external data specifying e-mail addresses.
 * Group:
 * 	Mail Merge
 * Type:
 * 	Destination
 * Default Param:
 * 	0
 * Pass Default:
 * 	false
 * RTF Version:
 * 	
 */

public class RtfCtrlWord_mmaddfieldname extends RtfCtrlWordBase_mmaddfieldname {

	public RtfCtrlWord_mmaddfieldname(RtfParser rtfParser){
		super(rtfParser);
	}

}
