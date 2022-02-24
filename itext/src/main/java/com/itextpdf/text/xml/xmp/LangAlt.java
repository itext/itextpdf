/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
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
package com.itextpdf.text.xml.xmp;

import java.util.Enumeration;
import java.util.Properties;

import com.itextpdf.text.xml.XMLUtil;

@Deprecated
public class LangAlt extends Properties {

	/** A serial version id. */
	private static final long serialVersionUID = 4396971487200843099L;

	/** Key for the default language. */
	public static final String DEFAULT = "x-default";

	/** Creates a Properties object that stores languages for use in an XmpSchema */
	public LangAlt(String defaultValue) {
		super();
		addLanguage(DEFAULT, defaultValue);
	}

	/** Creates a Properties object that stores languages for use in an XmpSchema */
	public LangAlt() {
		super();
	}

	/**
	 * Add a language.
	 */
	public void addLanguage(String language, String value) {
		setProperty(language, XMLUtil.escapeXML(value, false));
	}

	/**
	 * Process a property.
	 */
	protected void process(StringBuffer buf, Object lang) {
		buf.append("<rdf:li xml:lang=\"");
		buf.append(lang);
		buf.append("\" >");
		buf.append(get(lang));
		buf.append("</rdf:li>");
	}

	/**
	 * Creates a String that can be used in an XmpSchema.
	 */
	@Override
    public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("<rdf:Alt>");
		for (Enumeration<?> e = this.propertyNames(); e.hasMoreElements();) {
			process(sb, e.nextElement());
		}
		sb.append("</rdf:Alt>");
		return sb.toString();
	}

}
