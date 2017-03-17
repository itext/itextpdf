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

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;

/**
 * The RichMediaActivation dictionary specifies the style of presentation,
 * default script behavior, default view information, and animation style
 * when the annotation is activated.
 * See ExtensionLevel 3 p78
 * @since	5.0.0
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
