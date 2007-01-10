package com.lowagie.text.pdf.collection;

import java.util.Calendar;

import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfString;

public class PdfCollectionItem extends PdfDictionary {
	
	/**
	 * Constructs a Collection Item that can be added to a PdfFileSpecification.
	 */
	public PdfCollectionItem() {
		super(PdfName.COLLECTIONITEM);
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(PdfCollectionField field, PdfName schemaname, String value) {
		addItem(field, schemaname, value, false);
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(PdfCollectionField field, PdfName schemaname, String value, boolean unicode) {
		put(schemaname, field.getValue(value, unicode));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(PdfCollectionField field, PdfName schemaname, PdfString s) {
		if (field.type == PdfCollectionField.TEXT) {
			put(schemaname, s);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(PdfCollectionField field, PdfName schemaname, PdfDate d) {
		if (field.type == PdfCollectionField.DATE) {
			put(schemaname, d);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(PdfCollectionField field, PdfName schemaname, PdfNumber n) {
		if (field.type == PdfCollectionField.NUMBER) {
			put(schemaname, n);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(PdfCollectionField field, PdfName schemaname, Calendar c) {
		addItem(field, schemaname, new PdfDate(c));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(PdfCollectionField field, PdfName schemaname, int i) {
		addItem(field, schemaname, new PdfNumber(i));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(PdfCollectionField field, PdfName schemaname, float f) {
		addItem(field, schemaname, new PdfNumber(f));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void addItem(PdfCollectionField field, PdfName schemaname, double d) {
		addItem(field, schemaname, new PdfNumber(d));
	}
	
	/**
	 * Adds a prefix for the Collection item.
	 * You can only use this method after you have set the value of the item.
	 * @param prefix	a prefix
	 * @param unicode	true if you want the prefix to be in unicode	
	 */
	public void setPrefix(PdfCollectionField field, PdfName schemaname, String prefix, boolean unicode) {
		PdfObject o = get(schemaname);
		if (o == null)
			throw new IllegalArgumentException("You must set a value before adding a prefix.");
		PdfDictionary dict = new PdfDictionary(PdfName.COLLECTIONSUBITEM);
		dict.put(PdfName.D, o);
		dict.put(PdfName.P, new PdfString(prefix, unicode ? PdfObject.TEXT_UNICODE : PdfObject.TEXT_PDFDOCENCODING));
		put(schemaname, dict);
	}
	
	/**
	 * Adds a prefix for the Collection item.
	 * You can only use this method after you have set the value of the item.
	 * @param prefix	a prefix
	 * @param unicode	true if you want the prefix to be in unicode	
	 */
	public void setPrefix(PdfCollectionField field, PdfName schemaname, String prefix) {
		setPrefix(field, schemaname, prefix, false);
	}
}