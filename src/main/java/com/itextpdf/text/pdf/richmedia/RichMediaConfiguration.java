/*
 * $Id: RichMediaConfiguration.java 3821 2009-03-25 10:33:52Z blowagie $
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
 * The RichMediaConfiguration dictionary describes a set of instances
 * that are loaded for a given scene configuration. The configuration
 * to be loaded when an annotation is activated is referenced by the
 * Configuration key in the RichMediaActivation dictionary specified
 * in the RichMediaSettings dictionary.
 * see ExtensionLevel 3 p88
 * @see RichMediaAnnotation
 * @see RichMediaInstance
 * @since	5.0.0
 */
public class RichMediaConfiguration extends PdfDictionary {

	/** An array of indirect object references to RichMediaInstance dictionaries. */
	protected PdfArray instances = new PdfArray();
	
	/**
	 * Creates a RichMediaConfiguration object. Also specifies the primary
	 * content type for the configuration. Valid values are 3D, Flash, Sound,
	 * and Video.
	 * @param	subtype	Possible values are:
	 * PdfName._3D, PdfName.FLASH, PdfName.SOUND, and PdfName.VIDEO.
	 */
	public RichMediaConfiguration(PdfName subtype) {
		super(PdfName.RICHMEDIACONFIGURATION);
		put(PdfName.SUBTYPE, subtype);
		put(PdfName.INSTANCES, instances);
	}
	
	/**
	 * Sets the name of the configuration (must be unique).
	 * @param	name	the name
	 */
	public void setName(PdfString name) {
		put(PdfName.NAME, name);
	}
	
	/**
	 * Adds a RichMediaInstance to the instances array of this
	 * configuration.
	 * @param	instance	a RichMediaInstance
	 */
	public void addInstance(RichMediaInstance instance) {
		instances.add(instance);
	}
}
