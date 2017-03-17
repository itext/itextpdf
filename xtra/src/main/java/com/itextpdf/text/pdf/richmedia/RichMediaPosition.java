/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf.richmedia;

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;

/**
 * The position of the window in the reader presentation area is described
 * by the RichMediaPosition dictionary. The position of the window remains
 * fixed, regardless of the page translation.
 * See ExtensionLevel 3 p84
 * @since	5.0.0
 */
public class RichMediaPosition extends PdfDictionary {

	/**
	 * Constructs a RichMediaPosition dictionary.
	 */
	public RichMediaPosition() {
		super(PdfName.RICHMEDIAPOSITION);
	}
	
	/**
	 * Set the horizontal alignment.
	 * @param	hAlign possible values are
	 * PdfName.NEAR, PdfName.CENTER, or PdfName.FAR
	 */
	public void setHAlign(PdfName hAlign) {
		put(PdfName.HALIGN, hAlign);
	}
	
	/**
	 * Set the horizontal alignment.
	 * @param	vAlign possible values are
	 * PdfName.NEAR, PdfName.CENTER, or PdfName.FAR
	 */
	public void setVAlign(PdfName vAlign) {
		put(PdfName.VALIGN, vAlign);
	}
	
	/**
	 * Sets the offset from the alignment point specified by the HAlign key.
	 * A positive value for HOffset, when HAlign is either Near or Center,
	 * offsets the position towards the Far direction. A positive value for
	 * HOffset, when HAlign is Far, offsets the position towards the Near
	 * direction.
	 * @param	hOffset	an offset
	 */
	public void setHOffset(float hOffset) {
		put(PdfName.HOFFSET, new PdfNumber(hOffset));
	}
	
	/**
	 * Sets the offset from the alignment point specified by the VAlign key.
	 * A positive value for VOffset, when VAlign is either Near or Center,
	 * offsets the position towards the Far direction. A positive value for
	 * VOffset, when VAlign is Far, offsets the position towards the Near
	 * direction.
	 * @param	vOffset	an offset
	 */
	public void setVOffset(float vOffset) {
		put(PdfName.VOFFSET, new PdfNumber(vOffset));
	}
}
