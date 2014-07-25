/*
 * $Id: XmpBasicSchema.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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


/**
 * An implementation of an XmpSchema.
 */
@Deprecated
public class XmpBasicSchema extends XmpSchema {

	private static final long serialVersionUID = -2416613941622479298L;
	/** default namespace identifier*/
	public static final String DEFAULT_XPATH_ID = "xmp";
	/** default namespace uri*/
	public static final String DEFAULT_XPATH_URI = "http://ns.adobe.com/xap/1.0/";
	
	/** An unordered array specifying properties that were edited outside the authoring application. Each item should contain a single namespace and XPath separated by one ASCII space (U+0020). */
	public static final String ADVISORY = "xmp:Advisory";
	/** The base URL for relative URLs in the document content. If this document contains Internet links, and those links are relative, they are relative to this base URL. This property provides a standard way for embedded relative URLs to be interpreted by tools. Web authoring tools should set the value based on their notion of where URLs will be interpreted. */
	public static final String BASEURL = "xmp:BaseURL";
	/** The date and time the resource was originally created. */
	public static final String CREATEDATE = "xmp:CreateDate";
	/** The name of the first known tool used to create the resource. If history is present in the metadata, this value should be equivalent to that of xmpMM:History's softwareAgent property. */
	public static final String CREATORTOOL = "xmp:CreatorTool";
	/** An unordered array of text strings that unambiguously identify the resource within a given context. */
	public static final String IDENTIFIER = "xmp:Identifier";
	/** The date and time that any metadata for this resource was last changed. */
	public static final String METADATADATE = "xmp:MetadataDate";
	/** The date and time the resource was last modified. */
	public static final String MODIFYDATE = "xmp:ModifyDate";
	/** A short informal name for the resource. */
	public static final String NICKNAME = "xmp:Nickname";
	/** An alternative array of thumbnail images for a file, which can differ in characteristics such as size or image encoding. */
	public static final String THUMBNAILS = "xmp:Thumbnails";

	
	public XmpBasicSchema() {
		super("xmlns:" + DEFAULT_XPATH_ID + "=\"" + DEFAULT_XPATH_URI + "\"");
	}
	
	/**
	 * Adds the creatortool.
	 * @param creator
	 */
	public void addCreatorTool(String creator) {
		setProperty(CREATORTOOL, creator);
	}
	
	/**
	 * Adds the creation date.
	 * @param date
	 */
	public void addCreateDate(String date) {
		setProperty(CREATEDATE, date);
	}
	
	/**
	 * Adds the modification date.
	 * @param date
	 */
	public void addModDate(String date) {
		setProperty(MODIFYDATE, date);
	}

	/**
	 * Adds the meta data date.
	 * @param date
	 */
	public void addMetaDataDate(String date) {
		setProperty(METADATADATE, date);
	}

	/** Adds the identifier.
	 * @param id
	 */
	public void addIdentifiers(String[] id) {
		XmpArray array = new XmpArray(XmpArray.UNORDERED);
		for (int i = 0; i < id.length; i++) {
			array.add(id[i]);
		}
		setProperty(IDENTIFIER, array);
	}

	/** Adds the nickname.
	 * @param name
	 */
	public void addNickname(String name) {
		setProperty(NICKNAME, name);
	}
}
