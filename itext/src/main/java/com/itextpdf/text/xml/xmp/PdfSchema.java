/*
 * $Id: PdfSchema.java 5934 2013-08-06 15:21:45Z eugenemark $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
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
package com.itextpdf.text.xml.xmp;

import com.itextpdf.text.Version;

/**
 * An implementation of an XmpSchema.
 */
@Deprecated
public class PdfSchema extends XmpSchema {

	private static final long serialVersionUID = -1541148669123992185L;
	/** default namespace identifier*/
	public static final String DEFAULT_XPATH_ID = "pdf";
	/** default namespace uri*/
	public static final String DEFAULT_XPATH_URI = "http://ns.adobe.com/pdf/1.3/";
	
	/** Keywords. */
	public static final String KEYWORDS = "pdf:Keywords";
	/** The PDF file version (for example: 1.0, 1.3, and so on). */
	public static final String VERSION = "pdf:PDFVersion";
	/** The Producer. */
	public static final String PRODUCER = "pdf:Producer";


	public PdfSchema() {
		super("xmlns:" + DEFAULT_XPATH_ID + "=\"" + DEFAULT_XPATH_URI + "\"");
		addProducer(Version.getInstance().getVersion());
	}
	
	/**
	 * Adds keywords.
	 * @param keywords
	 */
	public void addKeywords(String keywords) {
		setProperty(KEYWORDS, keywords);
	}
	
	/**
	 * Adds the producer.
	 * @param producer
	 */
	public void addProducer(String producer) {
		setProperty(PRODUCER, producer);
	}

	/**
	 * Adds the version.
	 * @param version
	 */
	public void addVersion(String version) {
		setProperty(VERSION, version);
	}
}
