/*
 * $Id$
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

package com.lowagie.text.rtf.parser.destinations;

import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.rtf.list.RtfList;
import com.lowagie.text.rtf.list.RtfListTable;
import com.lowagie.text.rtf.parser.RtfImportMgr;
import com.lowagie.text.rtf.parser.RtfParser;
import com.lowagie.text.rtf.parser.ctrlwords.RtfCtrlWordData;

/**
 * <code>RtfDestinationListTable</code> handles data destined for the List
 * Table destination
 * 
 * @author Howard Shank (hgshank@yahoo.com)
 * @since 2.1.0
 */
public class RtfDestinationListTable extends RtfDestination {
	/**
	 * The RtfImportHeader to add List mappings to.
	 */
	private RtfImportMgr importHeader = null;

	private RtfList newList = null;

	public RtfDestinationListTable() {
		super(null);
	}

	public RtfDestinationListTable(RtfParser parser) {
		super(parser);
		this.importHeader = parser.getImportManager();
	}

	public void setParser(RtfParser parser) {
		this.rtfParser = parser;
		this.importHeader = parser.getImportManager();
		this.setToDefaults();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lowagie.text.rtf.parser.destinations.RtfDestination#handleOpenNewGroup()
	 */
	public boolean handleOpeningSubGroup() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lowagie.text.rtf.direct.RtfDestination#closeDestination()
	 */
	public boolean closeDestination() {
//		RtfListTable t = this.rtfParser.getRtfDocument().getDocumentHeader().getListTable();
//		List newlist = new List(List.UNORDERED, List.NUMERICAL, 0xb7);
//		// newlist.set
//		newList = new RtfList(this.rtfParser.getRtfDocument(), newlist);
//		this.rtfParser.getRtfDocument().add(newList);
//		//newList.set
//		// mylist.
//		// this.rtfParser.setTokeniserStateSkipGroup();
//		
//		// may have to create an import mapping for lists because
		// there may be existing lists in the document with a duplicate ID.
		if (this.newList != null) {
			this.rtfParser.getRtfDocument().add(this.newList);
		}
		return true;
	}

	public boolean handleControlWord(RtfCtrlWordData ctrlWordData) {
		boolean result = true;
		boolean skipCtrlWord = false;

		if (this.rtfParser.isImport()) {
			skipCtrlWord = true;
			if (ctrlWordData.ctrlWord.equals("listtable")) {
				result = true;
			} else
			/* Picture info for icons/images for lists */
			if (ctrlWordData.ctrlWord.equals("listpicture"))/* DESTINATION */{
				skipCtrlWord = true;
				// this.rtfParser.setTokeniserStateSkipGroup();
				result = true;
			} else
			/* list */
			if (ctrlWordData.ctrlWord.equals("list")) /* DESTINATION */{
				skipCtrlWord = true;
				this.newList = new RtfList(this.rtfParser.getRtfDocument());
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("listtemplateid")) {	// List item
				skipCtrlWord = true;
				result = true;
				// this gets set internally. Don't think it should be imported
			} else if (ctrlWordData.ctrlWord.equals("listsimple")) {	// List item
				skipCtrlWord = true;
				result = true;
				// this gets set internally. Don't think it should be imported
			} else if (ctrlWordData.ctrlWord.equals("listhybrid")) {	// List item
				skipCtrlWord = true;
				result = true;
				// this gets set internally. Don't think it should be imported
			} else if (ctrlWordData.ctrlWord.equals("listrestarthdn")) {	// List item
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("listid")) {	// List item cannot be between -1 and -5
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("listname")) {// List item
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("liststyleid")) {// List item
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("liststylename")) {// List item
				skipCtrlWord = true;
				result = true;
			} else
			/* listlevel */
			if (ctrlWordData.ctrlWord.equals("listlevel")) /* DESTINATION There are 1 or 9 listlevels per list */{
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levejcn")) { // listlevel item
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelstartat")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("lvltentative")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelold")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelprev")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelprevspace")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelspace")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelindent")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("leveltext")) {/* FIX */
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelnumbers")) {/* FIX */
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelfollow")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levellegal")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelnorestart")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("chrfmt")) {/* FIX */
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelpicture")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("li")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("fi")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("jclisttab")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("tx")) {
				skipCtrlWord = true;
				result = true;
			} else
			/* number */
			if (ctrlWordData.ctrlWord.equals("levelnfc")) /* old style */ {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("levelnfcn")) /* new style takes priority over levelnfc.*/ {
				skipCtrlWord = true;
				result = true;
			} else
			/* justification */
			if (ctrlWordData.ctrlWord.equals("leveljc")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("leveljcn")) {
				skipCtrlWord = true;
				result = true;
			} else
			/* level text */
			if (ctrlWordData.ctrlWord.equals("leveltext")) {
				skipCtrlWord = true;
				result = true;
			} else if (ctrlWordData.ctrlWord.equals("leveltemplateid")) {
				skipCtrlWord = true;
				result = true;
			} else
			/* levelnumber */
			if (ctrlWordData.ctrlWord.equals("levelnumbers")) {
				skipCtrlWord = true;
				result = true;
			}
		}

		if (this.rtfParser.isConvert()) {
			if (ctrlWordData.ctrlWord.equals("shppict")) {
				result = true;
			}
			if (ctrlWordData.ctrlWord.equals("nonshppict")) {
				skipCtrlWord = true;
				this.rtfParser.setTokeniserStateSkipGroup();
				result = true;
			}
		}
		if (!skipCtrlWord) {
			switch (this.rtfParser.getConversionType()) {
			case RtfParser.TYPE_IMPORT_FULL:
				// writeBuffer();
				// writeText(ctrlWordData.toString());
				result = true;
				break;
			case RtfParser.TYPE_IMPORT_FRAGMENT:
				// writeBuffer();
				// writeText(ctrlWordData.toString());
				result = true;
				break;
			case RtfParser.TYPE_CONVERT:
				result = true;
				break;
			default: // error because is should be an import or convert
				result = false;
				break;
			}
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lowagie.text.rtf.direct.RtfDestination#handleGroupEnd()
	 */
	public boolean handleCloseGroup() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lowagie.text.rtf.direct.RtfDestination#handleGroupStart()
	 */
	public boolean handleOpenGroup() {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lowagie.text.rtf.direct.RtfDestination#handleCharacter(int)
	 */
	public boolean handleCharacter(int ch) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.lowagie.text.rtf.parser.destinations.RtfDestination#setToDefaults()
	 */
	public void setToDefaults() {
		// TODO Auto-generated method stub

	}

}
