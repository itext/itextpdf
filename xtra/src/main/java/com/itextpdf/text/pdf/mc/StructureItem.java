/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.mc;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;

/**
 * Object that stores an item that is part of the document structure.
 * It can refer to a Marked Content sequence in a page or an object
 * reference.
 */
public class StructureItem {
	/** The structure element of which the properties are stored. */
	PdfDictionary structElem;
	/** The reference of the structure element of which the properties are stored. */
	PdfIndirectReference ref;
	/** MarkedContent IDs in case we're dealing with an MC sequence on a page. */
	List<Integer> mcids = new ArrayList<Integer>();
	/** Object reference in case we're dealing with an object. */
	PdfDictionary objr;
	
	/**
	 * Creates a structure item based on a dictionary.
	 * The dictionary can be of type StructElem, MCR or OBJR.
	 * @param dict	the dictionary that needs to be examined
	 */
	public StructureItem(PdfDictionary structElem, PdfIndirectReference ref) {
		this.structElem = structElem;
		this.ref = ref;
		PdfObject object = structElem.getDirectObject(PdfName.K);
		if (object == null)
			return;
		inspectKids(object);
	}
	
	/**
	 * Inspects the value of a K entry and stores all MCIDs
	 * or object references that are encountered.
	 * @param object the value of a K-entry
	 */
	protected void inspectKids(PdfObject object) {
		if (object == null)
			return;
		switch(object.type()) {
		case PdfObject.NUMBER:
			mcids.add(((PdfNumber)object).intValue());
			break;
		case PdfObject.ARRAY:
			PdfArray array = (PdfArray)object;
			for (int i = 0; i < array.size(); i++) {
				inspectKids(array.getDirectObject(i));
			}
			break;
		case PdfObject.DICTIONARY:
			PdfDictionary dict = (PdfDictionary)object;
			if (dict.checkType(PdfName.MCR)) {
				mcids.add(dict.getAsNumber(PdfName.MCID).intValue());
			}
			else if (dict.checkType(PdfName.OBJR)) {
				objr = dict;
			}
		}
	}
	
	/**
	 * Checks if we're dealing with real content.
	 * @return true if there's something to process
	 */
	public boolean isRealContent() {
		return (mcids.size() > 0 || objr != null);
	}

	/**
	 * Processes a MCID.
	 * @param mcid the MCID
	 * @return 0 in case there's an OBJR dictionary,
	 *         1 in case all MCIDs are now encountered
	 *         2 in case there are still MCIDs to process.
	 */
	public int process(int mcid) {
		if (mcids.contains(mcid)) {
			mcids.remove(new Integer(mcid));
			return mcids.size() > 0 ? 2 : 1;
		}
		if (objr != null)
			return 0;
		return -1;
	}
	
	/**
	 * Returns the structure element.
	 * @return a dictionary
	 */
	public PdfDictionary getStructElem() {
		return structElem;
	}
	
	/**
	 * Returns the structure element's reference.
	 * @return a dictionary
	 */
	public PdfIndirectReference getRef() {
		return ref;
	}
	
	/**
	 * Returns the OBJR dictionary (if present).
	 * @return a dictionary of type OBJR or null
	 */
	public PdfDictionary getObjr() {
		return objr;
	}
	
	/**
	 * Returns the object referred to by the OBJR dictionary.
	 * Note that this method returns a dictionary which means
	 * that only the stream dictionary will be passed in case
	 * of an XObject.
	 * @return the object referred to by OBJR as a dictionary
	 */
	public PdfDictionary getObj() {
		if (objr == null)
			return null;
		return objr.getAsDict(PdfName.OBJ);
	}
	
	/**
	 * Creates a String representation of the object.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (mcids.size() > 0) {
			buf.append("MCID: ");
			for (int i : mcids) {
				buf.append(String.valueOf(i) + " ");
			}
		}
		if (objr != null) {
			buf.append(objr);
		}
		return buf.toString();
	}
}
