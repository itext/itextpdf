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
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfString;

/**
 * Dictionary containing parameters related to an active Flash subtype
 * in a RichMediaInstance dictionary.
 * See ExtensionLevel 3 p90
 * @since	2.1.6
 */
public class RichMediaParams extends PdfDictionary {

	/**
	 * Creates a RichMediaParams object.
	 */
	public RichMediaParams() {
		super(PdfName.RICHMEDIAPARAMS);
	}
	
	/**
	 * Sets a text string containing formatted name value pairs passed
	 * to the Flash Player context when activated.
	 * @param	flashVars	a String with the Flash variables
	 */
	public void setFlashVars(String flashVars) {
		put(PdfName.FLASHVARS, new PdfString(flashVars));
	}
	
	/**
	 * Sets the binding.
	 * @param	binding	possible values:
	 * PdfName.NONE, PdfName.FOREGROUND, PdfName.BACKGROUND, PdfName.MATERIAL
	 */
	public void setBinding(PdfName binding) {
		put(PdfName.BINDING, binding);
	}
	
	/**
	 * Stores the material name that content is to be bound to.
	 * Required if Binding value is Material.
	 * @param	bindingMaterialName	a material name
	 */
	public void setBindingMaterialName(PdfString bindingMaterialName) {
		put(PdfName.BINDINGMATERIALNAME, bindingMaterialName);
	}
	
	/**
	 * Sets an array of CuePoint dictionaries containing points
	 * in time within a Flash animation.
	 * @param	cuePoints	a PdfArray with CuePoint objects
	 */
	public void setCuePoints(PdfArray cuePoints) {
		put(PdfName.CUEPOINTS, cuePoints);
	}
	
	/**
	 * A text string used to store settings information associated
	 * with a Flash RichMediaInstance. It is to be stored and loaded
	 * by the scripting run time.
	 * @param	settings	a PdfString
	 */
	public void setSettings(PdfString settings) {
		put(PdfName.SETTINGS, settings);
	}
}
