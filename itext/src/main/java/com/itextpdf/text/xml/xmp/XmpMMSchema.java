/*
 * $Id: XmpMMSchema.java 5934 2013-08-06 15:21:45Z eugenemark $
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

/**
 * An implementation of an XmpSchema.
 */
@Deprecated
public class XmpMMSchema extends XmpSchema {

	private static final long serialVersionUID = 1408509596611634862L;
	/** default namespace identifier*/
	public static final String DEFAULT_XPATH_ID = "xmpMM";
	/** default namespace uri*/
	public static final String DEFAULT_XPATH_URI = "http://ns.adobe.com/xap/1.0/mm/";
	

	/** A reference to the original document from which this one is derived. It is a minimal reference; missing components can be assumed to be unchanged. For example, a new version might only need to specify the instance ID and version number of the previous version, or a rendition might only need to specify the instance ID and rendition class of the original. */
	public static final String DERIVEDFROM = "xmpMM:DerivedFrom"; 
	/** The common identifier for all versions and renditions of a document. */
	public static final String DOCUMENTID = "xmpMM:DocumentID";
	/** An ordered array of high-level user actions that resulted in this resource. It is intended to give human readers a general indication of the steps taken to make the changes from the previous version to this one. The list should be at an abstract level; it is not intended to be an exhaustive keystroke or other detailed history. */
	public static final String HISTORY = "xmpMM:History";
	/** A reference to the document as it was prior to becoming managed. It is set when a managed document is introduced to an asset management system that does not currently own it. It may or may not include references to different management systems. */
	public static final String MANAGEDFROM = "xmpMM:ManagedFrom";
	/** The name of the asset management system that manages this resource. */
	public static final String MANAGER = "xmpMM:Manager";
	/** A URI identifying the managed resource to the asset management system; the presence of this property is the formal indication that this resource is managed. The form and content of this URI is private to the asset management system. */
	public static final String MANAGETO = "xmpMM:ManageTo";
	/** A URI that can be used to access information about the managed resource through a web browser. It might require a custom browser plugin. */
	public static final String MANAGEUI = "xmpMM:ManageUI";
	/** Specifies a particular variant of the asset management system. The format of this property is private to the specific asset management system. */
	public static final String MANAGERVARIANT = "xmpMM:ManagerVariant";
	/** The rendition class name for this resource.*/
	public static final String RENDITIONCLASS = "xmpMM:RenditionClass";
	/**  Can be used to provide additional rendition parameters that are too complex or verbose to encode in xmpMM: RenditionClass. */
	public static final String RENDITIONPARAMS = "xmpMM:RenditionParams";
	/** The document version identifier for this resource. */
	public static final String VERSIONID = "xmpMM:VersionID";
	/** The version history associated with this resource.*/
	public static final String VERSIONS = "xmpMM:Versions";
	
	public XmpMMSchema() {
		super("xmlns:" + DEFAULT_XPATH_ID + "=\"" + DEFAULT_XPATH_URI + "\"");
	}
}
