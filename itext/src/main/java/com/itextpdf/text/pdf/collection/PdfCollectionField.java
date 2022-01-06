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
package com.itextpdf.text.pdf.collection;

import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * @author blowagie
 *
 */
public class PdfCollectionField extends PdfDictionary {
	/** A possible type of collection field. */
	public static final int TEXT = 0;
	/** A possible type of collection field. */
	public static final int DATE = 1;
	/** A possible type of collection field. */
	public static final int NUMBER = 2;
	/** A possible type of collection field. */
	public static final int FILENAME = 3;
	/** A possible type of collection field. */
	public static final int DESC = 4;
	/** A possible type of collection field. */
	public static final int MODDATE = 5;
	/** A possible type of collection field. */
	public static final int CREATIONDATE = 6;
	/** A possible type of collection field. */
	public static final int SIZE = 7;
	
	/**
	 * The type of the PDF collection field.
	 * @since 2.1.2 (was called <code>type</code> previously)
	 */
	protected int fieldType;

	/**
	 * Creates a PdfCollectionField.
	 * @param name		the field name
	 * @param type		the field type
	 */
	public PdfCollectionField(String name, int type) {
		super(PdfName.COLLECTIONFIELD);
		put(PdfName.N, new PdfString(name, PdfObject.TEXT_UNICODE));
		this.fieldType = type;
		switch(type) {
		default:
			put(PdfName.SUBTYPE, PdfName.S);
			break;
		case DATE:
			put(PdfName.SUBTYPE, PdfName.D);
			break;
		case NUMBER:
			put(PdfName.SUBTYPE, PdfName.N);
			break;
		case FILENAME:
			put(PdfName.SUBTYPE, PdfName.F);
			break;
		case DESC:
			put(PdfName.SUBTYPE, PdfName.DESC);
			break;
		case MODDATE:
			put(PdfName.SUBTYPE, PdfName.MODDATE);
			break;
		case CREATIONDATE:
			put(PdfName.SUBTYPE, PdfName.CREATIONDATE);
			break;
		case SIZE:
			put(PdfName.SUBTYPE, PdfName.SIZE);
			break;
		}
	}
	
	/**
	 * The relative order of the field name. Fields are sorted in ascending order.
	 * @param i	a number indicating the order of the field
	 */
	public void setOrder(int i) {
		put(PdfName.O, new PdfNumber(i));
	}
	
	/**
	 * Sets the initial visibility of the field.
	 * @param visible	the default is true (visible)
	 */
	public void setVisible(boolean visible) {
		put(PdfName.V, new PdfBoolean(visible));
	}
	
	/**
	 * Indication if the field value should be editable in the viewer.
	 * @param editable	the default is false (not editable)
	 */
	public void setEditable(boolean editable) {
		put(PdfName.E, new PdfBoolean(editable));
	}

	/**
	 * Checks if the type of the field is suitable for a Collection Item.
	 */
	public boolean isCollectionItem() {
		switch(fieldType) {
		case TEXT:
		case DATE:
		case NUMBER:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * Returns a PdfObject that can be used as the value of a Collection Item.
	 * @param v	value	the value that has to be changed into a PdfObject (PdfString, PdfDate or PdfNumber)	
	 */
	public PdfObject getValue(String v) {
		switch(fieldType) {
		case TEXT:
			return new PdfString(v, PdfObject.TEXT_UNICODE);
		case DATE:
			return new PdfDate(PdfDate.decode(v));
		case NUMBER:
			return new PdfNumber(v);
		}
		throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.is.not.an.acceptable.value.for.the.field.2", v, get(PdfName.N).toString()));
	}
}
