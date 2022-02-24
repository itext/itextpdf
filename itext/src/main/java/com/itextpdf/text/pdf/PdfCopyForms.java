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
package com.itextpdf.text.pdf;

import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;

/**
 * Allows you to add one (or more) existing PDF document(s) to
 * create a new PDF and add the form of another PDF document to
 * this new PDF.
 * @since 2.1.5
 * @deprecated since 5.5.2
 */
public class PdfCopyForms
	implements PdfViewerPreferences, PdfEncryptionSettings {

	/** The class with the actual implementations. */
    private PdfCopyFormsImp fc;

    /**
     * Creates a new instance.
     * @param os the output stream
     * @throws DocumentException on error
     */
    public PdfCopyForms(OutputStream os) throws DocumentException {
        fc = new PdfCopyFormsImp(os);
    }

    /**
     * Concatenates a PDF document.
     * @param reader the PDF document
     * @throws DocumentException on error
     */
    public void addDocument(PdfReader reader) throws DocumentException, IOException {
        fc.addDocument(reader);
    }

    /**
     * Concatenates a PDF document selecting the pages to keep. The pages are described as a
     * <CODE>List</CODE> of <CODE>Integer</CODE>. The page ordering can be changed but
     * no page repetitions are allowed.
     * @param reader the PDF document
     * @param pagesToKeep the pages to keep
     * @throws DocumentException on error
     */
    public void addDocument(PdfReader reader, List<Integer> pagesToKeep) throws DocumentException, IOException {
        fc.addDocument(reader, pagesToKeep);
    }

    /**
     * Concatenates a PDF document selecting the pages to keep. The pages are described as
     * ranges. The page ordering can be changed but
     * no page repetitions are allowed.
     * @param reader the PDF document
     * @param ranges the comma separated ranges as described in {@link SequenceList}
     * @throws DocumentException on error
     */
    public void addDocument(PdfReader reader, String ranges) throws DocumentException, IOException {
        fc.addDocument(reader, SequenceList.expand(ranges, reader.getNumberOfPages()));
    }

    /**
     *Copies the form fields of this PDFDocument onto the PDF-Document which was added
     * @param reader the PDF document
     * @throws DocumentException on error
     */
    public void copyDocumentFields(PdfReader reader) throws DocumentException{
        fc.copyDocumentFields(reader);
    }

    /** Sets the encryption options for this document. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @param strength128Bits <code>true</code> for 128 bit key length, <code>false</code> for 40 bit key length
     * @throws DocumentException if the document is already open
     */
    public void setEncryption(byte userPassword[], byte ownerPassword[], int permissions, boolean strength128Bits) throws DocumentException {
    	fc.setEncryption(userPassword, ownerPassword, permissions, strength128Bits ? PdfWriter.STANDARD_ENCRYPTION_128 : PdfWriter.STANDARD_ENCRYPTION_40);
    }

    /**
     * Sets the encryption options for this document. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param strength true for 128 bit key length. false for 40 bit key length
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @throws DocumentException if the document is already open
     */
    public void setEncryption(boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException {
        setEncryption(DocWriter.getISOBytes(userPassword), DocWriter.getISOBytes(ownerPassword), permissions, strength);
    }

    /**
     * Closes the output document.
     */
    public void close() {
        fc.close();
    }

    /**
     * Opens the document. This is usually not needed as addDocument() will do it
     * automatically.
     */
    public void open() {
        fc.openDoc();
    }

    /**
     * Adds JavaScript to the global document
     * @param js the JavaScript
     */
    public void addJavaScript(String js) {
        fc.addJavaScript(js, !PdfEncodings.isPdfDocEncoding(js));
    }

    /**
     * Sets the bookmarks. The list structure is defined in
     * <CODE>SimpleBookmark#</CODE>.
     * @param outlines the bookmarks or <CODE>null</CODE> to remove any
     */
    public void setOutlines(List<HashMap<String, Object>> outlines) {
        fc.setOutlines(outlines);
    }

    /** Gets the underlying PdfWriter.
     * @return the underlying PdfWriter
     */
    public PdfWriter getWriter() {
        return fc;
    }

    /**
     * Gets the 1.5 compression status.
     * @return <code>true</code> if the 1.5 compression is on
     */
    public boolean isFullCompression() {
        return fc.isFullCompression();
    }

    /**
     * Sets the document's compression to the new 1.5 mode with object streams and xref
     * streams. It can be set at any time but once set it can't be unset.
     * <p>
     * If set before opening the document it will also set the pdf version to 1.5.
     */
    public void setFullCompression() throws DocumentException {
        fc.setFullCompression();
    }

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings#setEncryption(byte[], byte[], int, int)
	 */
	public void setEncryption(byte[] userPassword, byte[] ownerPassword, int permissions, int encryptionType) throws DocumentException {
		fc.setEncryption(userPassword, ownerPassword, permissions, encryptionType);
	}

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfViewerPreferences#addViewerPreference(com.itextpdf.text.pdf.PdfName, com.itextpdf.text.pdf.PdfObject)
	 */
	public void addViewerPreference(PdfName key, PdfObject value) {
		fc.addViewerPreference(key, value);
	}

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfViewerPreferences#setViewerPreferences(int)
	 */
	public void setViewerPreferences(int preferences) {
		fc.setViewerPreferences(preferences);
	}

	/**
	 * @see com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings#setEncryption(java.security.cert.Certificate[], int[], int)
	 */
	public void setEncryption(Certificate[] certs, int[] permissions, int encryptionType) throws DocumentException {
		fc.setEncryption(certs, permissions, encryptionType);
	}
}
