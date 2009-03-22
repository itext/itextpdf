/*
 * $Id:  $
 *
 * Copyright 2009 by Bruno Lowagie.
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
 * the Initial Developer are Copyright (C) 1999-2009 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2009 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
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

package com.lowagie.text.pdf.richmedia;

import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;

/**
 * The position of the window in the reader presentation area is described
 * by the RichMediaPosition dictionary. The position of the window remains
 * fixed, regardless of the page translation.
 * See ExtensionLevel 3 p84
 * @since	2.1.6
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
