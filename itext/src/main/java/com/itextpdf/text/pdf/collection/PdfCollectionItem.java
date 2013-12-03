/*
 * $Id: PdfCollectionItem.java 5914 2013-07-28 14:18:11Z blowagie $
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
package com.itextpdf.text.pdf.collection;

import java.util.Calendar;
import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

public class PdfCollectionItem extends PdfDictionary {
	
	/** The PdfCollectionSchema with the names and types of the items. */
	PdfCollectionSchema schema;
	
	/**
	 * Constructs a Collection Item that can be added to a PdfFileSpecification.
	 */
	public PdfCollectionItem(PdfCollectionSchema schema) {
		super(PdfName.COLLECTIONITEM);
		this.schema = schema;
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(String key, String value) {
		PdfName fieldname = new PdfName(key);
		PdfCollectionField field = (PdfCollectionField)schema.get(fieldname);
		put(fieldname, field.getValue(value));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(String key, PdfString value) {
		PdfName fieldname = new PdfName(key);
		PdfCollectionField field = (PdfCollectionField)schema.get(fieldname);
		if (field.fieldType == PdfCollectionField.TEXT) {
			put(fieldname, value);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param d
	 */
	public void addItem(String key, PdfDate d) {
		PdfName fieldname = new PdfName(key);
		PdfCollectionField field = (PdfCollectionField)schema.get(fieldname);
		if (field.fieldType == PdfCollectionField.DATE) {
			put(fieldname, d);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param n
	 */
	public void addItem(String key, PdfNumber n) {
		PdfName fieldname = new PdfName(key);
		PdfCollectionField field = (PdfCollectionField)schema.get(fieldname);
		if (field.fieldType == PdfCollectionField.NUMBER) {
			put(fieldname, n);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param c
	 */
	public void addItem(String key, Calendar c) {
		addItem(key, new PdfDate(c));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param i
	 */
	public void addItem(String key, int i) {
		addItem(key, new PdfNumber(i));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param f
	 */
	public void addItem(String key, float f) {
		addItem(key, new PdfNumber(f));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param d
	 */
	public void addItem(String key, double d) {
		addItem(key, new PdfNumber(d));
	}
	
	/**
	 * Adds a prefix for the Collection item.
	 * You can only use this method after you have set the value of the item.
	 * @param prefix	a prefix
	 */
	public void setPrefix(String key, String prefix) {
		PdfName fieldname = new PdfName(key);
		PdfObject o = get(fieldname);
		if (o == null)
			throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.must.set.a.value.before.adding.a.prefix"));
		PdfDictionary dict = new PdfDictionary(PdfName.COLLECTIONSUBITEM);
		dict.put(PdfName.D, o);
		dict.put(PdfName.P, new PdfString(prefix, PdfObject.TEXT_UNICODE));
		put(fieldname, dict);
	}
}
