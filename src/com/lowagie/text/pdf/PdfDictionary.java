/*
 * @(#)PdfDictionary.java			0.39 2000/11/22
 *       release rugPdf0.10:		0.03 99/03/22
 *               rugPdf0.20:		0.15 99/11/30
 *               iText0.3:			0.22 2000/02/14
 *               iText0.35:         0.22 2000/08/11
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
 *     
 * Very special thanks to Troy Harrison, Systems Consultant
 * of CNA Life Department-Information Technology
 * Troy.Harrison@cnalife.com <mailto:Troy.Harrison@cnalife.com>
 * His input concerning the changes in version rugPdf0.20 was
 * really very important.
 */

package com.lowagie.text.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * <CODE>PdfDictionary</CODE> is the Pdf dictionary object.
 * <P>
 * A dictionary is an associative table containing pairs of objects. The first element
 * of each pair is called the <I>key</I> and the second element is called the <I>value</I>.
 * Unlike dictionaries in the PostScript language, a key must be a <CODE>PdfName</CODE>.
 * A value can be any kind of <CODE>PdfObject</CODE>, including a dictionary. A dictionary is
 * generally used to collect and tie together the attributes of a complex object, with each
 * key-value pair specifying the name and value of an attribute.<BR>
 * A dictionary is represented by two left angle brackets (<<), followed by a sequence of
 * key-value pairs, followed by two right angle brackets (>>).<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.7 (page 40-41).
 * <P>
 * Since version rugPdf0.20, the entry's in a PdfDictionary are sorted by key.
 *
 * @see		PdfObject
 * @see		PdfName
 * @see		BadPdfFormatException
 *
 * @author  bruno@lowagie.com
 * @version 0.39 2000/11/22
 *
 * @since   rugPdf0.10
 */

class PdfDictionary extends PdfObject {

// static membervariables (types of dictionary's)
	
	/** This is a possible type of dictionary */
	public static final PdfName FONT = PdfName.FONT;
	
	/** This is a possible type of dictionary */
	public static final PdfName OUTLINES = PdfName.OUTLINES;
	
	/** This is a possible type of dictionary */
	public static final PdfName PAGE = PdfName.PAGE;
	
	/** This is a possible type of dictionary */
	public static final PdfName PAGES = PdfName.PAGES;
	
	/** This is a possible type of dictionary */
	public static final PdfName CATALOG = PdfName.CATALOG;
	
// membervariables

	/** This is the type of this dictionary */
	private PdfName dictionaryType = null;

	/** This is the treemap that contains all the values and keys of the dictionary */
	protected TreeMap treeMap;

// constructors

	/**
	 * Constructs an empty <CODE>PdfDictionary</CODE>-object.
	 *
	 * @since		rugPdf0.10
	 */

	PdfDictionary() {
		super(DICTIONARY);
		treeMap = new TreeMap();
	}

	/**
	 * Constructs a <CODE>PdfDictionary</CODE>-object of a certain type.
	 *
	 * @param		type	a <CODE>PdfName</CODE>
	 * @since		rugPdf0.10
	 */

	PdfDictionary(PdfName type) {
		this();
		dictionaryType = type;
		put(PdfName.TYPE, dictionaryType);
	}

// methods overriding some methods in PdfObject

	/**
     * Returns the PDF representation of this <CODE>PdfDictionary</CODE>.
	 *
	 * @return		an array of <CODE>byte</CODE>
     *
	 * @since		rugPdf0.10
     */

    byte[] toPdf() {
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			stream.write("<<\n".getBytes());

			// loop over all the object-pairs in the TreeMap
			PdfName key;
			PdfObject value;
			for (Iterator i = treeMap.keySet().iterator(); i.hasNext(); ) {
				key = (PdfName) i.next();
				value = (PdfObject) treeMap.get(key);
				stream.write(key.toPdf());
				stream.write(" ".getBytes());
				stream.write(value.toPdf());
				stream.write("\n".getBytes());
			}
			stream.write(">>".getBytes());

			return stream.toByteArray();
		}
		catch(IOException ioe) {
			throw new RuntimeException(ioe.getMessage());
		}
    }

// methods concerning the TreeMap member value

	/**
	 * Adds a <CODE>PdfObject</CODE> and its key to the <CODE>PdfDictionary</CODE>.
	 *
	 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
	 * @param		value	value of the entry (a <CODE>PdfObject</CODE>)
	 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
	 *
	 * @since		rugPdf0.10
	 */

	final PdfObject put(PdfName key, PdfObject value) {
		return (PdfObject) treeMap.put(key, value);
	}

	/**
	 * Removes a <CODE>PdfObject</CODE> and its key from the <CODE>PdfDictionary</CODE>.
	 *
	 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
	 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
	 *
	 * @since		rugPdf0.39
	 */

	final PdfObject remove(PdfName key) {
		return (PdfObject) treeMap.remove(key);
	}

	/**
	 * Gets a <CODE>PdfObject</CODE> with a certain key from the <CODE>PdfDictionary</CODE>.
	 *
	 * @param		key		key of the entry (a <CODE>PdfName</CODE>)
	 * @return		the previous </CODE>PdfObject</CODE> corresponding with the <VAR>key</VAR>
	 *
	 * @since		rugPdf0.20
	 */

	final PdfObject get(PdfName key) {
		return (PdfObject) treeMap.get(key);
	}

// methods concerning the type of Dictionary

	/**
	 * Checks if a <CODE>PdfDictionary</CODE> is of a certain type.
	 *
	 * @param		type	a type of dictionary
	 * @return		<CODE>true</CODE> of <CODE>false</CODE>
	 *
	 * @since		rugPdf0.10
	 * @deprecated
	 */

	 final boolean isDictionaryType(PdfName type) {
		 return dictionaryType.compareTo(type) == 0;
	 }

	/**
	 *  Checks if a <CODE>Dictionary</CODE> is of the type FONT.
	 *
	 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
	 *
	 * @since		rugPdf0.20
	 */

	final boolean isFont() {
		return dictionaryType.compareTo(FONT) == 0;
	}														   

	/**
	 *  Checks if a <CODE>Dictionary</CODE> is of the type PAGE.
	 *
	 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
	 *
	 * @since		rugPdf0.20
	 */

	final boolean isPage() {
		return dictionaryType.compareTo(PAGE) == 0;
	}

	/**
	 *  Checks if a <CODE>Dictionary</CODE> is of the type PAGES.
	 *
	 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
	 *
	 * @since		rugPdf0.20
	 */

	final boolean isPages() {
		return dictionaryType.compareTo(PAGES) == 0;
	}										

	/**
	 *  Checks if a <CODE>Dictionary</CODE> is of the type CATALOG.
	 *
	 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
	 *
	 * @since		rugPdf0.20
	 */

	final boolean isCatalog() {
		return dictionaryType.compareTo(CATALOG) == 0;
	}										

	/**
	 *  Checks if a <CODE>Dictionary</CODE> is of the type OUTLINES.
	 *
	 * @return		<CODE>true</CODE> if it is, <CODE>false</CODE> if it isn't.
	 *
	 * @since		iText0.39
	 */

	final boolean isOutlineTree() {
		return dictionaryType.compareTo(OUTLINES) == 0;
	}
}