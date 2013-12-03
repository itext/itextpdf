/*
 * $Id: PdfImportedPage.java 5914 2013-07-28 14:18:11Z blowagie $
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
package com.itextpdf.text.pdf;
import java.io.IOException;

import com.itextpdf.text.error_messages.MessageLocalization;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;

/** Represents an imported page.
 *
 * @author Paulo Soares
 */
public class PdfImportedPage extends com.itextpdf.text.pdf.PdfTemplate {

    PdfReaderInstance readerInstance;
    int pageNumber;
    int rotation;
    
    /**
     * True if the imported page has been copied to a writer.
     * @since iText 5.0.4
     */
    protected boolean toCopy = true;
    
    PdfImportedPage(PdfReaderInstance readerInstance, PdfWriter writer, int pageNumber) {
        this.readerInstance = readerInstance;
        this.pageNumber = pageNumber;
        this.writer = writer;
        rotation = readerInstance.getReader().getPageRotation(pageNumber);
        bBox = readerInstance.getReader().getPageSize(pageNumber);
        setMatrix(1, 0, 0, 1, -bBox.getLeft(), -bBox.getBottom());
        type = TYPE_IMPORTED;
    }

    /** Reads the content from this <CODE>PdfImportedPage</CODE>-object from a reader.
     *
     * @return self
     *
     */
    public PdfImportedPage getFromReader() {
      return this;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getRotation() {
        return rotation;
    }


    /** Always throws an error. This operation is not allowed.
     * @param image dummy
     * @param a dummy
     * @param b dummy
     * @param c dummy
     * @param d dummy
     * @param e dummy
     * @param f dummy
     * @throws DocumentException  dummy */    
    public void addImage(Image image, float a, float b, float c, float d, float e, float f) throws DocumentException {
        throwError();
    }
    
    /** Always throws an error. This operation is not allowed.
     * @param template dummy
     * @param a dummy
     * @param b dummy
     * @param c dummy
     * @param d dummy
     * @param e dummy
     * @param f  dummy */    
    public void addTemplate(PdfTemplate template, float a, float b, float c, float d, float e, float f) {
        throwError();
    }
    
    /** Always throws an error. This operation is not allowed.
     * @return  dummy */    
    public PdfContentByte getDuplicate() {
        throwError();
        return null;
    }

    /**
     * Gets the stream representing this page.
     *
     * @param	compressionLevel	the compressionLevel
     * @return the stream representing this page
     * @since	2.1.3	(replacing the method without param compressionLevel)
     */
    public PdfStream getFormXObject(int compressionLevel) throws IOException {
         return readerInstance.getFormXObject(pageNumber, compressionLevel);
    }
    
    public void setColorFill(PdfSpotColor sp, float tint) {
        throwError();
    }
    
    public void setColorStroke(PdfSpotColor sp, float tint) {
        throwError();
    }
    
    PdfObject getResources() {
        return readerInstance.getResources(pageNumber);
    }
    
    /** Always throws an error. This operation is not allowed.
     * @param bf dummy
     * @param size dummy */    
    public void setFontAndSize(BaseFont bf, float size) {
        throwError();
    }
    
    /**
     * Always throws an error. This operation is not allowed.
     * @param group New value of property group.
     * @since	2.1.6
     */ 
    public void setGroup(PdfTransparencyGroup group) {
        throwError();
	}

	void throwError() {
        throw new RuntimeException(MessageLocalization.getComposedMessage("content.can.not.be.added.to.a.pdfimportedpage"));
    }
    
    PdfReaderInstance getPdfReaderInstance() {
        return readerInstance;
    }

	/**
	 * Checks if the page has to be copied.
	 * @return true if the page has to be copied.
	 * @since iText 5.0.4
	 */
	public boolean isToCopy() {
		return toCopy;
	}

	/**
	 * Indicate that the resources of the imported page have been copied.
	 * @since iText 5.0.4
	 */
	public void setCopied() {
		toCopy = false;
	}
}
