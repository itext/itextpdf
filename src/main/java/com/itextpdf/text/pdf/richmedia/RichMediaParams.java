/*
 * $Id: PdfAnnotationsImp.java 4113 2009-12-01 11:08:59Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf.richmedia;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;

/**
 * Dictionary containing parameters related to an active Flash subtype
 * in a RichMediaInstance dictionary.
 * See ExtensionLevel 3 p90
 * @since	5.0.0
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
