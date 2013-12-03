/*
 * $Id: LargeElement.java 5914 2013-07-28 14:18:11Z blowagie $
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
package com.itextpdf.text;

/**
 * Interface implemented by Element objects that can potentially consume
 * a lot of memory. Objects implementing the LargeElement interface can
 * be added to a Document more than once. If you have invoked setComplete(false),
 * they will be added partially and the content that was added will be
 * removed until you've invoked setComplete(true);
 * @since	iText 2.0.8
 */

public interface LargeElement extends Element {
	
	/**
	 * If you invoke setComplete(false), you indicate that the content
	 * of the object isn't complete yet; it can be added to the document
	 * partially, but more will follow. If you invoke setComplete(true),
	 * you indicate that you won't add any more data to the object.
	 * @since	iText 2.0.8
	 * @param	complete	false if you'll be adding more data after
	 * 						adding the object to the document.
	 */
	public void setComplete(boolean complete);
	
	/**
	 * Indicates if the element is complete or not.
	 * @since	iText 2.0.8
	 * @return	indicates if the element is complete according to the user.
	 */
	public boolean isComplete();
	
	/**
	 * Flushes the content that has been added.
	 */
	public void flushContent();
}
