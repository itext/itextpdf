/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;

/**
 * Object that stores an item that is part of the document structure.
 * It can refer to a marked-content sequence in a page or an object
 * reference (in this case the subclass StructureObject is used).
 */
public class StructureMCID extends StructureItem {
	
	/** The mcid of the structure element. */
	protected int mcid = -1;
	
	/**
	 * Creates a StructureMCID using an MCID.
	 * @param mcid	an MCID
	 */
	public StructureMCID(PdfIndirectReference pg, PdfNumber mcid) {
		this.pageref = pg.getNumber();
		this.mcid = mcid.intValue();
	}
	
	/**
	 * Creates a StructurItem using an MCR dictionary.
	 * @param mcr	an MCR dictionary
	 */
	public StructureMCID(PdfDictionary mcr) {
		mcid = mcr.getAsNumber(PdfName.MCID).intValue();
		pageref = mcr.getAsIndirectObject(PdfName.PG).getNumber();
	}
	
	/**
	 * Checks if the MCID in this object corresponds with the stored number
	 * @param mcid the MCID
	 * @return 1 in case the MCIDs corresponds with obj,
	 *         -1 in case the MCID doesn't correspond
	 */
	public int checkMCID(int pg, int mcid) {
		if (pageref == -1)
			throw new RuntimeException();
		if (pg == pageref && this.mcid == mcid)
			return 1;
		return -1;
	}
	
	/**
	 * Creates a String representation of the object.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "MCID " + mcid + " on page with reference " + pageref;
	}
}
