/*
 * @(#)DocumentException.java		0.23 2000/02/02
 *       release iText0.3:			0.23 2000/02/14
 *       release iText0.35:         0.23 2000/08/11
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *  	  
 */

package com.lowagie.text;

/**
 * Signals that an error has occurred in a <CODE>Document</CODE>.
 *
 * @see		BadElementException
 * @see		Document
 * @see		DocWriter
 * @see		DocListener
 *
 * @author  bruno@lowagie.com
 * @version 0.23, 2000/02/02
 *
 * @since   iText0.30
 */

public class DocumentException extends Exception {

// constructors

    /**
     * Constructs a <CODE>DocumentException</CODE> whithout a message. 
     *
     * @since   iText0.30
     */

    public DocumentException() {
		super();
    }

    /**
     * Constructs a <code>DocumentException</code> with a message. 
     *
     * @param		message			a message describing the exception
	 *
     * @since		iText0.30
     */

    public DocumentException(String message) {
		super(message);
    }
}