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

import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfIndirectReference;
import com.lowagie.text.pdf.PdfName;

/**
 * The RichMediaActivation dictionary specifies the style of presentation,
 * default script behavior, default view information, and animation style
 * when the annotation is activated.
 * See ExtensionLevel 3 p78
 * @since	2.1.6
 */
public class RichMediaActivation extends PdfDictionary {
	
	/**
	 * Creates a RichMediaActivation dictionary.
	 */
	public RichMediaActivation() {
		super(PdfName.RICHMEDIAACTIVATION);
	}
	
	/**
	 * Sets the activation condition.
	 * Set it to XA if the annotation is explicitly activated by a user action
	 * or script (this is the default).
	 * To PO, if the annotation is activated as soon as the page that contains
	 * the annotation receives focus as the current page.
	 * To PV, if the annotation is activated as soon as any part of the page
	 * that contains the annotation becomes visible. One example is in a
	 * multiple-page presentation. Only one page is the current page although
	 * several are visible.
	 * @param	condition	possible values are:
	 * 		PdfName.XA, PdfName.PO, or PdfName.PV
	 */
	public void setCondition(PdfName condition) {
		put(PdfName.CONDITION, condition);
	}
	
	/**
	 * Sets the animation dictionary describing the preferred method
	 * that conforming readers should use to drive keyframe animations
	 * present in this artwork.
	 * @param	animation	a RichMediaAnimation dictionary
	 */
	public void setAnimation(RichMediaAnimation animation) {
		put(PdfName.ANIMATION, animation);
	}
	
	/**
	 * Sets an indirect object reference to a 3D view dictionary
	 * that shall also be referenced by the Views array within the
	 * annotation's RichMediaContent dictionary.
	 * @param	view	an indirect reference
	 */
	public void setView(PdfIndirectReference view) {
		put(PdfName.VIEW, view);
	}
	
	/**
	 * Sets an indirect object reference to a RichMediaConfiguration
	 * dictionary that shall also be referenced by the Configurations
	 * array in the RichMediaContent dictionary (which is part of
	 * the RichMediaAnnotation object).
	 * @param	configuration	an indirect reference
	 */
	public void setConfiguration(PdfIndirectReference configuration) {
		put(PdfName.CONFIGURATION, configuration);
	}
	
	/**
	 * Sets a RichMediaPresentation dictionary that contains information
	 * as to how the annotation and user interface elements will be visually
	 * laid out and drawn.
	 * @param	richMediaPresentation	a RichMediaPresentation object
	 */
	public void setPresentation(RichMediaPresentation richMediaPresentation) {
		put(PdfName.PRESENTATION, richMediaPresentation);
	}
	
	/**
	 * Sets an array of indirect object references to file specification
	 * dictionaries, each of which describe a JavaScript file that shall
	 * be present in the Assets name tree of the RichMediaContent dictionary.
	 * @param	scripts	a PdfArray
	 */
	public void setScripts(PdfArray scripts) {
		put(PdfName.SCRIPTS, scripts);
	}
}
