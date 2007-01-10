package com.lowagie.text.pdf.collection;

import java.util.Calendar;

import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfString;

public class PdfCollectionItem extends PdfDictionary {

	protected PdfCollectionField field;
	
	/**
	 * Constructs a Collection Item that can be added to a PdfFileSpecification.
	 */
	public PdfCollectionItem(PdfCollectionField field) {
		super(PdfName.COLLECTIONITEM);
		if (!field.isCollectionItem())
			throw new IllegalArgumentException("The type of the field isn't compatible with a Collection Item.");
		this.field = field;
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void setValue(String value) {
		setValue(value, false);
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void setValue(String value, boolean unicode) {
		put(field.getName(), field.getValue(value, unicode));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void setValue(PdfString s) {
		if (field.type == PdfCollectionField.TEXT) {
			put(field.getName(), s);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void setValue(PdfDate d) {
		if (field.type == PdfCollectionField.DATE) {
			put(field.getName(), d);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void setValue(PdfNumber n) {
		if (field.type == PdfCollectionField.NUMBER) {
			put(field.getName(), n);
		}
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void setValue(Calendar c) {
		setValue(new PdfDate(c));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void setValue(int i) {
		setValue(new PdfNumber(i));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void setValue(float f) {
		setValue(new PdfNumber(f));
	}
	
	/**
	 * Sets the value of the collection item.
	 * @param value
	 */
	public void setValue(double d) {
		setValue(new PdfNumber(d));
	}
	
	/**
	 * Adds a prefix for the Collection item.
	 * You can only use this method after you have set the value of the item.
	 * @param prefix	a prefix
	 * @param unicode	true if you want the prefix to be in unicode	
	 */
	public void setPrefix(String prefix, boolean unicode) {
		PdfObject o = get(field.getName());
		if (o == null)
			throw new IllegalArgumentException("You must set a value before adding a prefix.");
		PdfDictionary dict = new PdfDictionary(PdfName.COLLECTIONSUBITEM);
		dict.put(PdfName.D, o);
		dict.put(PdfName.P, new PdfString(prefix, unicode ? PdfObject.TEXT_UNICODE : PdfObject.TEXT_PDFDOCENCODING));
		put(field.getName(), dict);
	}
}