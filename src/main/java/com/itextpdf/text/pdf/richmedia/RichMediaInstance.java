/*
 * $Id: RichMediaInstance.java 3821 2009-03-25 10:33:52Z blowagie $
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

import com.itextpdf.text.exceptions.IllegalPdfSyntaxException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;

/**
 * The RichMediaInstance dictionary, referenced by the Instances entry
 * of the RichMediaConfiguration, describes a single instance of an asset
 * with settings to populate the artwork of an annotation.
 * See ExtensionLevel 3 p88
 * @see	RichMediaConfiguration
 * @since	5.0.0
 */
public class RichMediaInstance extends PdfDictionary {

	/** True if the instance is a flash animation. */
	protected boolean flash;
	
	/**
	 * Creates a RichMediaInstance. Also specifies the content type
	 * for the instance. Valid values are 3D, Flash, Sound, and Video.
	 * The subtype must match the asset file type of the instance.
	 * @param	subtype	possible values are:
	 * PdfName._3D, PdfName.FLASH, PdfName.SOUND, and PdfName.VIDEO.
	 */
	public RichMediaInstance(PdfName subtype) {
		super(PdfName.RICHMEDIAINSTANCE);
		put(PdfName.SUBTYPE, subtype);
		flash = PdfName.FLASH.equals(subtype);
	}
	
	/**
	 * Sets the parameters. This will only work for Flash.
	 * @param params	a RichMediaParams object
	 */
	public void setParams(RichMediaParams params) {
		if (flash) {
			put(PdfName.PARAMS, params);
		}
		else {
			throw new IllegalPdfSyntaxException("Parameters can only be set for Flash instances.");
		}
	}
	
	/**
	 * Sets a dictionary that shall be an indirect object reference
	 * to a file specification dictionary that is also referenced
	 * in the Assets name tree of the content of the annotation.
	 * @param	asset	a reference to a dictionary present in the Assets name tree
	 */
	public void setAsset(PdfIndirectReference asset) {
		put(PdfName.ASSET, asset);
	}
}
