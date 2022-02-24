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
package com.itextpdf.text.pdf.events;

import java.io.IOException;
import java.util.HashMap;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfRectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

/**
 * Class that can be used to position AcroForm fields.
 */
public class FieldPositioningEvents extends PdfPageEventHelper implements PdfPCellEvent {

    /**
     * Keeps a map with fields that are to be positioned in inGenericTag.
     */
    protected HashMap<String, PdfFormField> genericChunkFields = new HashMap<String, PdfFormField>();

    /**
     * Keeps the form field that is to be positioned in a cellLayout event.
     */
    protected PdfFormField cellField = null;

    /**
     * The PdfWriter to use when a field has to added in a cell event.
     */
    protected PdfWriter fieldWriter = null;
    /**
     * The PdfFormField that is the parent of the field added in a cell event.
     */
    protected PdfFormField parent = null;

    /** Creates a new event. This constructor will be used if you need to position fields with Chunk objects. */
    public FieldPositioningEvents() {}

    /** Some extra padding that will be taken into account when defining the widget. */
    public float padding;

    /**
     * Add a PdfFormField that has to be tied to a generic Chunk.
     */
    public void addField(String text, PdfFormField field) {
    	genericChunkFields.put(text, field);
    }

    /** Creates a new event. This constructor will be used if you need to position fields with a Cell Event. */
    public FieldPositioningEvents(PdfWriter writer, PdfFormField field) {
    	this.cellField = field;
    	this.fieldWriter = writer;
    }

    /** Creates a new event. This constructor will be used if you need to position fields with a Cell Event. */
    public FieldPositioningEvents(PdfFormField parent, PdfFormField field) {
    	this.cellField = field;
    	this.parent = parent;
    }

    /** Creates a new event. This constructor will be used if you need to position fields with a Cell Event.
     * @throws DocumentException
     * @throws IOException*/
    public FieldPositioningEvents(PdfWriter writer, String text) throws IOException, DocumentException {
    	this.fieldWriter = writer;
    	TextField tf = new TextField(writer, new Rectangle(0, 0), text);
		tf.setFontSize(14);
		cellField = tf.getTextField();
	}

    /** Creates a new event. This constructor will be used if you need to position fields with a Cell Event.
     * @throws DocumentException
     * @throws IOException*/
    public FieldPositioningEvents(PdfWriter writer, PdfFormField parent, String text) throws IOException, DocumentException {
    	this.parent = parent;
    	TextField tf = new TextField(writer, new Rectangle(0, 0), text);
		tf.setFontSize(14);
		cellField = tf.getTextField();
	}

	/**
	 * @param padding The padding to set.
	 */
	public void setPadding(float padding) {
		this.padding = padding;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(PdfFormField parent) {
		this.parent = parent;
	}
	/**
	 * @see com.itextpdf.text.pdf.PdfPageEvent#onGenericTag(com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document, com.itextpdf.text.Rectangle, java.lang.String)
	 */
	@Override
    public void onGenericTag(PdfWriter writer, Document document,
			Rectangle rect, String text) {
		rect.setBottom(rect.getBottom() - 3);
		PdfFormField field = genericChunkFields.get(text);
		if (field == null) {
			TextField tf = new TextField(writer, new Rectangle(rect.getLeft(padding), rect.getBottom(padding), rect.getRight(padding), rect.getTop(padding)), text);
			tf.setFontSize(14);
			try {
				field = tf.getTextField();
			} catch (Exception e) {
				throw new ExceptionConverter(e);
			}
		}
		else {
			field.put(PdfName.RECT,  new PdfRectangle(rect.getLeft(padding), rect.getBottom(padding), rect.getRight(padding), rect.getTop(padding)));
		}
		if (parent == null)
			writer.addAnnotation(field);
		else
			parent.addKid(field);
	}

	/**
	 * @see com.itextpdf.text.pdf.PdfPCellEvent#cellLayout(com.itextpdf.text.pdf.PdfPCell, com.itextpdf.text.Rectangle, com.itextpdf.text.pdf.PdfContentByte[])
	 */
	public void cellLayout(PdfPCell cell, Rectangle rect, PdfContentByte[] canvases) {
		if (cellField == null || fieldWriter == null && parent == null) throw new IllegalArgumentException(MessageLocalization.getComposedMessage("you.have.used.the.wrong.constructor.for.this.fieldpositioningevents.class"));
		cellField.put(PdfName.RECT, new PdfRectangle(rect.getLeft(padding), rect.getBottom(padding), rect.getRight(padding), rect.getTop(padding)));
		if (parent == null)
			fieldWriter.addAnnotation(cellField);
		else
			parent.addKid(cellField);
	}
}
