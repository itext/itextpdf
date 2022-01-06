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

import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

public class PdfTargetDictionary extends PdfDictionary {
	
	/**
	 * Creates dictionary referring to a target document that is the parent of the current document.
	 * @param nested	null if this is the actual target, another target if this is only an intermediate target.
	 */
	public PdfTargetDictionary(PdfTargetDictionary nested) {
		super();
		put(PdfName.R, PdfName.P);
		if (nested != null)
			setAdditionalPath(nested);
	}
	
	/**
	 * Creates a dictionary referring to a target document.
	 * @param child	if false, this refers to the parent document; if true, this refers to a child document, and you'll have to specify where to find the child using the other methods of this class
	 */
	public PdfTargetDictionary(boolean child) {
		super();
		if (child) {
			put(PdfName.R, PdfName.C);
		}
		else {
			put(PdfName.R, PdfName.P);
		}
	}
	
	/**
	 * If this dictionary refers to a child that is a document level attachment,
	 * you need to specify the name that was used to attach the document.
	 * @param	target	the name in the EmbeddedFiles name tree
	 */
	public void setEmbeddedFileName(String target) {
		put(PdfName.N, new PdfString(target, null));
	}
	
	/**
	 * If this dictionary refers to a child that is a file attachment added to a page,
	 * you need to specify the name of the page (or use setFileAttachmentPage to specify the page number).
	 * Once you have specified the page, you still need to specify the attachment using another method.
	 * @param name	the named destination referring to the page with the file attachment.
	 */
	public void setFileAttachmentPagename(String name) {
		put(PdfName.P, new PdfString(name, null));
	}
	
	/**
	 * If this dictionary refers to a child that is a file attachment added to a page,
	 * you need to specify the page number (or use setFileAttachmentPagename to specify a named destination).
	 * Once you have specified the page, you still need to specify the attachment using another method.
	 * @param page	the page number of the page with the file attachment.
	 */
	public void setFileAttachmentPage(int page) {
		put(PdfName.P, new PdfNumber(page));
	}
	
	/**
	 * If this dictionary refers to a child that is a file attachment added to a page,
	 * you need to specify the page with setFileAttachmentPage or setFileAttachmentPageName,
	 * and then specify the name of the attachment added to this page (or use setFileAttachmentIndex).
	 * @param name		the name of the attachment
	 */
	public void setFileAttachmentName(String name) {
		put(PdfName.A, new PdfString(name, PdfObject.TEXT_UNICODE));
	}
	
	/**
	 * If this dictionary refers to a child that is a file attachment added to a page,
	 * you need to specify the page with setFileAttachmentPage or setFileAttachmentPageName,
	 * and then specify the index of the attachment added to this page (or use setFileAttachmentName).
	 * @param annotation		the number of the attachment
	 */
	public void setFileAttachmentIndex(int annotation) {
		put(PdfName.A, new PdfNumber(annotation));
	}
	
	/**
	 * If this dictionary refers to an intermediate target, you can
	 * add the next target in the sequence.
	 * @param nested	the next target in the sequence
	 */
	public void setAdditionalPath(PdfTargetDictionary nested) {
		put(PdfName.T, nested);
	}
}
