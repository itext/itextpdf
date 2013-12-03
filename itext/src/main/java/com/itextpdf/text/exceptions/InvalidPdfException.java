/*
 * $Id: InvalidPdfException.java 5914 2013-07-28 14:18:11Z blowagie $
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
package com.itextpdf.text.exceptions;

import java.io.IOException;

/**
 * Typed exception used when opening an existing PDF document.
 * Gets thrown when the document isn't a valid PDF document.
 * @since 2.1.5
 */
public class InvalidPdfException extends IOException {

	/** a serial version UID */
	private static final long serialVersionUID = -2319614911517026938L;
	private final Throwable cause;

	/**
	 * Creates an instance with a message and no cause
	 * @param	message	the reason why the document isn't a PDF document according to iText.
	 */
	public InvalidPdfException(String message) {
		this(message, null);
	}	
	
	/**
	 * Creates an exception with a message and a cause
	 * @param message	the reason why the document isn't a PDF document according to iText. 
	 * @param cause the cause of the exception, if any
	 */
	public InvalidPdfException(String message, Throwable cause){
		super(message);
		this.cause = cause;
	}
	
	/**
	 * This method is included (instead of using super(message, cause) in the constructors) to support backwards compatabilty with
	 * JDK 1.5, which did not have cause constructors for Throwable
	 * @return the cause of this exception
	 */
	public Throwable getCause() {
		return cause;
	}
}
