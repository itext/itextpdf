/*
 * $Id: XmpSchema.java 5934 2013-08-06 15:21:45Z eugenemark $
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

import java.util.Enumeration;
import java.util.Properties;

import com.itextpdf.text.xml.XMLUtil;

/**
 * Abstract superclass of the XmpSchemas supported by iText.
 */
@Deprecated
public abstract class XmpSchema extends Properties {

	private static final long serialVersionUID = -176374295948945272L;

	/** the namesspace */
	protected String xmlns;

	/** Constructs an XMP schema.
	 * @param xmlns
	 */
	public XmpSchema(String xmlns) {
		super();
		this.xmlns = xmlns;
	}
	/**
	 * The String representation of the contents.
	 * @return a String representation.
	 */
	@Override
    public String toString() {
		StringBuffer buf = new StringBuffer();
		for (Enumeration<?> e = this.propertyNames(); e.hasMoreElements(); ) {
			process(buf, e.nextElement());
		}
		return buf.toString();
	}
	/**
	 * Processes a property
	 * @param buf
	 * @param p
	 */
	protected void process(StringBuffer buf, Object p) {
		buf.append('<');
		buf.append(p);
		buf.append('>');
		buf.append(this.get(p));
		buf.append("</");
		buf.append(p);
		buf.append('>');
	}
	/**
	 * @return Returns the xmlns.
	 */
	public String getXmlns() {
		return xmlns;
	}

	/**
	 * @param key
	 * @param value
	 * @return the previous property (null if there wasn't one)
	 */
	public Object addProperty(String key, String value) {
		return this.setProperty(key, value);
	}

	/**
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	@Override
    public Object setProperty(String key, String value) {
		return super.setProperty(key, XMLUtil.escapeXML(value, false));
	}

	/**
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 *
	 * @param key
	 * @param value
	 * @return the previous property (null if there wasn't one)
	 */
	public Object setProperty(String key, XmpArray value) {
		return super.setProperty(key, value.toString());
	}

	/**
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 *
	 * @param key
	 * @param value
	 * @return the previous property (null if there wasn't one)
	 */
	public Object setProperty(String key, LangAlt value) {
		return super.setProperty(key, value.toString());
	 }

	/**
	 * @param content
	 * @return an escaped string
	 * @deprecated use XMLUtil.escapeXml(String s, boolean onlyASCII) instead.
	 */
	public static String escape(String content) {
		return XMLUtil.escapeXML(content, false);
	}
}
