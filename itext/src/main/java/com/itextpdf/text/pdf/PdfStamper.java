/*
 * $Id: PdfStamper.java 6318 2014-03-12 10:23:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.collection.PdfCollection;
import com.itextpdf.text.pdf.interfaces.PdfEncryptionSettings;
import com.itextpdf.text.pdf.interfaces.PdfViewerPreferences;
import com.itextpdf.text.pdf.security.LtvVerification;
import com.itextpdf.text.xml.xmp.XmpWriter;

/** Applies extra content to the pages of a PDF document.
 * This extra content can be all the objects allowed in PdfContentByte
 * including pages from other Pdfs. The original PDF will keep
 * all the interactive elements including bookmarks, links and form fields.
 * <p>
 * It is also possible to change the field values and to
 * flatten them. New fields can be added but not flattened.
 * @author Paulo Soares
 */
public class PdfStamper
	implements PdfViewerPreferences, PdfEncryptionSettings {
    /**
     * The writer
     */
    protected PdfStamperImp stamper;
    private Map<String, String> moreInfo;
    protected boolean hasSignature;
    protected PdfSignatureAppearance sigApp;
    protected XmlSignatureAppearance sigXmlApp;
    private LtvVerification verification;

    /** Starts the process of adding extra content to an existing PDF
     * document.
     * <p>
     * The reader will be closed when this PdfStamper is closed
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public PdfStamper(final PdfReader reader, final OutputStream os) throws DocumentException, IOException {
        stamper = new PdfStamperImp(reader, os, '\0', false);
    }

    /**
     * Starts the process of adding extra content to an existing PDF
     * document.
     * <p>
     * The reader will be closed when this PdfStamper is closed
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public PdfStamper(final PdfReader reader, final OutputStream os, final char pdfVersion) throws DocumentException, IOException {
        stamper = new PdfStamperImp(reader, os, pdfVersion, false);
    }

    /**
     * Starts the process of adding extra content to an existing PDF
     * document, possibly as a new revision.
     * <p>
     * The reader will be closed when this PdfStamper is closed
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @param append if <CODE>true</CODE> appends the document changes as a new revision. This is
     * only useful for multiple signatures as nothing is gained in speed or memory
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public PdfStamper(final PdfReader reader, final OutputStream os, final char pdfVersion, final boolean append) throws DocumentException, IOException {
        stamper = new PdfStamperImp(reader, os, pdfVersion, append);
    }

    /** Gets the optional <CODE>String</CODE> map to add or change values in
     * the info dictionary.
     * @return the map or <CODE>null</CODE>
     *
     */
    public Map<String, String> getMoreInfo() {
        return this.moreInfo;
    }

    /** An optional <CODE>String</CODE> map to add or change values in
     * the info dictionary. Entries with <CODE>null</CODE>
     * values delete the key in the original info dictionary
     * @param moreInfo additional entries to the info dictionary
     *
     */
    public void setMoreInfo(final Map<String, String> moreInfo) {
        this.moreInfo = moreInfo;
    }

    /**
     * Replaces a page from this document with a page from other document. Only the content
     * is replaced not the fields and annotations. This method must be called before
     * getOverContent() or getUndercontent() are called for the same page.
     * @param r the <CODE>PdfReader</CODE> from where the new page will be imported
     * @param pageImported the page number of the imported page
     * @param pageReplaced the page to replace in this document
     * @since iText 2.1.1
     */
    public void replacePage(final PdfReader r, final int pageImported, final int pageReplaced) {
        stamper.replacePage(r, pageImported, pageReplaced);
    }

    /**
     * Inserts a blank page. All the pages above and including <CODE>pageNumber</CODE> will
     * be shifted up. If <CODE>pageNumber</CODE> is bigger than the total number of pages
     * the new page will be the last one.
     * @param pageNumber the page number position where the new page will be inserted
     * @param mediabox the size of the new page
     */
    public void insertPage(final int pageNumber, final Rectangle mediabox) {
        stamper.insertPage(pageNumber, mediabox);
    }

    /**
     * Gets the signing instance. The appearances and other parameters can the be set.
     * @return the signing instance
     */
    public PdfSignatureAppearance getSignatureAppearance() {
        return sigApp;
    }

    /**
     * Gets the xml signing instance. The appearances and other parameters can the be set.
     * @return the xml signing instance
     */
    public XmlSignatureAppearance getXmlSignatureAppearance() {
        return sigXmlApp;
    }

    /**
     * Closes the document. No more content can be written after the
     * document is closed.
     * <p>
     * If closing a signed document with an external signature the closing must be done
     * in the <CODE>PdfSignatureAppearance</CODE> instance.
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public void close() throws DocumentException, IOException {
        if (stamper.closed)
            return;
        if (!hasSignature) {
            mergeVerification();
            stamper.close(moreInfo);
        }
        else {
            throw new DocumentException("Signature defined. Must be closed in PdfSignatureAppearance.");
        }
    }

    /** Gets a <CODE>PdfContentByte</CODE> to write under the page of
     * the original document.
     * @param pageNum the page number where the extra content is written
     * @return a <CODE>PdfContentByte</CODE> to write under the page of
     * the original document
     */
    public PdfContentByte getUnderContent(final int pageNum) {
        return stamper.getUnderContent(pageNum);
    }

    /** Gets a <CODE>PdfContentByte</CODE> to write over the page of
     * the original document.
     * @param pageNum the page number where the extra content is written
     * @return a <CODE>PdfContentByte</CODE> to write over the page of
     * the original document
     */
    public PdfContentByte getOverContent(final int pageNum) {
        return stamper.getOverContent(pageNum);
    }

    /** Checks if the content is automatically adjusted to compensate
     * the original page rotation.
     * @return the auto-rotation status
     */
    public boolean isRotateContents() {
        return stamper.isRotateContents();
    }

    /** Flags the content to be automatically adjusted to compensate
     * the original page rotation. The default is <CODE>true</CODE>.
     * @param rotateContents <CODE>true</CODE> to set auto-rotation, <CODE>false</CODE>
     * otherwise
     */
    public void setRotateContents(final boolean rotateContents) {
        stamper.setRotateContents(rotateContents);
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
     * @throws DocumentException if anything was already written to the output
     */
    public void setEncryption(final byte userPassword[], final byte ownerPassword[], final int permissions, final boolean strength128Bits) throws DocumentException {
        if (stamper.isAppend())
            throw new DocumentException(MessageLocalization.getComposedMessage("append.mode.does.not.support.changing.the.encryption.status"));
        if (stamper.isContentWritten())
            throw new DocumentException(MessageLocalization.getComposedMessage("content.was.already.written.to.the.output"));
        stamper.setEncryption(userPassword, ownerPassword, permissions, strength128Bits ? PdfWriter.STANDARD_ENCRYPTION_128 : PdfWriter.STANDARD_ENCRYPTION_40);
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
     * @param encryptionType the type of encryption. It can be one of STANDARD_ENCRYPTION_40, STANDARD_ENCRYPTION_128 or ENCRYPTION_AES128.
     * Optionally DO_NOT_ENCRYPT_METADATA can be ored to output the metadata in cleartext
     * @throws DocumentException if the document is already open
     */
    public void setEncryption(final byte userPassword[], final byte ownerPassword[], final int permissions, final int encryptionType) throws DocumentException {
        if (stamper.isAppend())
            throw new DocumentException(MessageLocalization.getComposedMessage("append.mode.does.not.support.changing.the.encryption.status"));
        if (stamper.isContentWritten())
            throw new DocumentException(MessageLocalization.getComposedMessage("content.was.already.written.to.the.output"));
        stamper.setEncryption(userPassword, ownerPassword, permissions, encryptionType);
    }

    /**
     * Sets the encryption options for this document. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param strength <code>true</code> for 128 bit key length, <code>false</code> for 40 bit key length
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @throws DocumentException if anything was already written to the output
     */
    public void setEncryption(final boolean strength, final String userPassword, final String ownerPassword, final int permissions) throws DocumentException {
        setEncryption(DocWriter.getISOBytes(userPassword), DocWriter.getISOBytes(ownerPassword), permissions, strength);
    }

    /**
     * Sets the encryption options for this document. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param encryptionType the type of encryption. It can be one of STANDARD_ENCRYPTION_40, STANDARD_ENCRYPTION_128 or ENCRYPTION_AES128.
     * Optionally DO_NOT_ENCRYPT_METADATA can be ored to output the metadata in cleartext
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @throws DocumentException if anything was already written to the output
     */
    public void setEncryption(final int encryptionType, final String userPassword, final String ownerPassword, final int permissions) throws DocumentException {
        setEncryption(DocWriter.getISOBytes(userPassword), DocWriter.getISOBytes(ownerPassword), permissions, encryptionType);
    }

    /**
     * Sets the certificate encryption options for this document. An array of one or more public certificates
     * must be provided together with an array of the same size for the permissions for each certificate.
     *  The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * Optionally DO_NOT_ENCRYPT_METADATA can be ored to output the metadata in cleartext
     * @param certs the public certificates to be used for the encryption
     * @param permissions the user permissions for each of the certificates
     * @param encryptionType the type of encryption. It can be one of STANDARD_ENCRYPTION_40, STANDARD_ENCRYPTION_128 or ENCRYPTION_AES128.
     * @throws DocumentException if the encryption was set too late
     */
     public void setEncryption(final Certificate[] certs, final int[] permissions, final int encryptionType) throws DocumentException {
        if (stamper.isAppend())
            throw new DocumentException(MessageLocalization.getComposedMessage("append.mode.does.not.support.changing.the.encryption.status"));
        if (stamper.isContentWritten())
            throw new DocumentException(MessageLocalization.getComposedMessage("content.was.already.written.to.the.output"));
        stamper.setEncryption(certs, permissions, encryptionType);
     }

    /** Gets a page from other PDF document. Note that calling this method more than
     * once with the same parameters will retrieve the same object.
     * @param reader the PDF document where the page is
     * @param pageNumber the page number. The first page is 1
     * @return the template representing the imported page
     */
    public PdfImportedPage getImportedPage(final PdfReader reader, final int pageNumber) {
        return stamper.getImportedPage(reader, pageNumber);
    }

    /** Gets the underlying PdfWriter.
     * @return the underlying PdfWriter
     */
    public PdfWriter getWriter() {
        return stamper;
    }

    /** Gets the underlying PdfReader.
     * @return the underlying PdfReader
     */
    public PdfReader getReader() {
        return stamper.reader;
    }

    /** Gets the <CODE>AcroFields</CODE> object that allows to get and set field values
     * and to merge FDF forms.
     * @return the <CODE>AcroFields</CODE> object
     */
    public AcroFields getAcroFields() {
        return stamper.getAcroFields();
    }

    /** Determines if the fields are flattened on close. The fields added with
     * {@link #addAnnotation(PdfAnnotation,int)} will never be flattened.
     * @param flat <CODE>true</CODE> to flatten the fields, <CODE>false</CODE>
     * to keep the fields
     */
    public void setFormFlattening(final boolean flat) {
        stamper.setFormFlattening(flat);
    }

    /** Determines if the FreeText annotations are flattened on close.
     * @param flat <CODE>true</CODE> to flatten the FreeText annotations, <CODE>false</CODE>
     * (the default) to keep the FreeText annotations as active content.
     */
    public void setFreeTextFlattening(final boolean flat) {
    	stamper.setFreeTextFlattening(flat);
	}

    /**
     * Adds an annotation of form field in a specific page. This page number
     * can be overridden with {@link PdfAnnotation#setPlaceInPage(int)}.
     * @param annot the annotation
     * @param page the page
     */
    public void addAnnotation(final PdfAnnotation annot, final int page) {
        stamper.addAnnotation(annot, page);
    }

    /**
     * Adds an empty signature.
     * @param name	the name of the signature
     * @param page	the page number
     * @param llx	lower left x coordinate of the signature's position
     * @param lly	lower left y coordinate of the signature's position
     * @param urx	upper right x coordinate of the signature's position
     * @param ury	upper right y coordinate of the signature's position
     * @return	a signature form field
     * @since	2.1.4
     */
    public PdfFormField addSignature(final String name, final int page, final float llx, final float lly, final float urx, final float ury) {
        PdfAcroForm acroForm = stamper.getAcroForm();
        PdfFormField signature = PdfFormField.createSignature(stamper);
        acroForm.setSignatureParams(signature, name, llx, lly, urx, ury);
        acroForm.drawSignatureAppearences(signature, llx, lly, urx, ury);
        addAnnotation(signature, page);
        return signature;
    }

    /**
     * Adds the comments present in an FDF file.
     * @param fdf the FDF file
     * @throws IOException on error
     */
    public void addComments(final FdfReader fdf) throws IOException {
        stamper.addComments(fdf);
    }

    /**
     * Sets the bookmarks. The list structure is defined in
     * {@link SimpleBookmark}.
     * @param outlines the bookmarks or <CODE>null</CODE> to remove any
     */
    public void setOutlines(final List<HashMap<String, Object>> outlines) {
        stamper.setOutlines(outlines);
    }

    /**
     * Sets the thumbnail image for a page.
     * @param image the image
     * @param page the page
     * @throws PdfException on error
     * @throws DocumentException on error
     */
    public void setThumbnail(final Image image, final int page) throws PdfException, DocumentException {
        stamper.setThumbnail(image, page);
    }

    /**
     * Adds <CODE>name</CODE> to the list of fields that will be flattened on close,
     * all the other fields will remain. If this method is never called or is called
     * with invalid field names, all the fields will be flattened.
     * <p>
     * Calling <CODE>setFormFlattening(true)</CODE> is needed to have any kind of
     * flattening.
     * @param name the field name
     * @return <CODE>true</CODE> if the field exists, <CODE>false</CODE> otherwise
     */
    public boolean partialFormFlattening(final String name) {
        return stamper.partialFormFlattening(name);
    }

    /** Adds a JavaScript action at the document level. When the document
     * opens all this JavaScript runs. The existing JavaScript will be replaced.
     * @param js the JavaScript code
     */
    public void addJavaScript(final String js) {
        stamper.addJavaScript(js, !PdfEncodings.isPdfDocEncoding(js));
    }

    /** Adds a JavaScript action at the document level. When the document
     * opens all this JavaScript runs. The existing JavaScript will be replaced.
     * @param name the name for the JavaScript snippet in the name tree
     * @param js the JavaScript code
     */
    public void addJavaScript(final String name, final String js) {
        stamper.addJavaScript(name, PdfAction.javaScript(js, stamper, !PdfEncodings.isPdfDocEncoding(js)));
    }

    /** Adds a file attachment at the document level. Existing attachments will be kept.
     * @param description the file description
     * @param fileStore an array with the file. If it's <CODE>null</CODE>
     * the file will be read from the disk
     * @param file the path to the file. It will only be used if
     * <CODE>fileStore</CODE> is not <CODE>null</CODE>
     * @param fileDisplay the actual file name stored in the pdf
     * @throws IOException on error
     */
    public void addFileAttachment(final String description, final byte fileStore[], final String file, final String fileDisplay) throws IOException {
        addFileAttachment(description, PdfFileSpecification.fileEmbedded(stamper, file, fileDisplay, fileStore));
    }

    /** Adds a file attachment at the document level. Existing attachments will be kept.
     * @param description the file description
     * @param fs the file specification
     * @throws IOException
     */
    public void addFileAttachment(final String description, final PdfFileSpecification fs) throws IOException {
        stamper.addFileAttachment(description, fs);
    }

    /**
     * This is the most simple way to change a PDF into a
     * portable collection. Choose one of the following names:
     * <ul>
     * <li>PdfName.D (detailed view)
     * <li>PdfName.T (tiled view)
     * <li>PdfName.H (hidden)
     * </ul>
     * Pass this name as a parameter and your PDF will be
     * a portable collection with all the embedded and
     * attached files as entries.
     * @param initialView can be PdfName.D, PdfName.T or PdfName.H
     */
    public void makePackage( final PdfName initialView ) {
    	PdfCollection collection = new PdfCollection(0);
    	collection.put(PdfName.VIEW, initialView);
    	stamper.makePackage( collection );
    }

    /**
     * Adds or replaces the Collection Dictionary in the Catalog.
     * @param	collection	the new collection dictionary.
     */
    public void makePackage(final PdfCollection collection) {
    	stamper.makePackage(collection);
    }

    /**
     * Sets the viewer preferences.
     * @param preferences the viewer preferences
     * @see PdfViewerPreferences#setViewerPreferences(int)
     */
    public void setViewerPreferences(final int preferences) {
        stamper.setViewerPreferences(preferences);
    }

    /** Adds a viewer preference
     * @param key a key for a viewer preference
     * @param value the value for the viewer preference
     * @see PdfViewerPreferences#addViewerPreference
     */

    public void addViewerPreference(final PdfName key, final PdfObject value) {
    	stamper.addViewerPreference(key, value);
    }

    /**
     * Sets the XMP metadata.
     * @param xmp
     * @see PdfWriter#setXmpMetadata(byte[])
     */
    public void setXmpMetadata(final byte[] xmp) {
        stamper.setXmpMetadata(xmp);
    }

    public void createXmpMetadata() {
        stamper.createXmpMetadata();
    }

    public XmpWriter getXmpWriter() {
        return stamper.getXmpWriter();
    }

    /**
     * Gets the 1.5 compression status.
     * @return <code>true</code> if the 1.5 compression is on
     */
    public boolean isFullCompression() {
        return stamper.isFullCompression();
    }

    /**
     * Sets the document's compression to the new 1.5 mode with object streams and xref
     * streams. It can be set at any time but once set it can't be unset.
     */
    public void setFullCompression() throws DocumentException {
        if (stamper.isAppend())
            return;
        stamper.fullCompression = true;
        stamper.setAtLeastPdfVersion(PdfWriter.VERSION_1_5);
    }

    /**
     * Sets the open and close page additional action.
     * @param actionType the action type. It can be <CODE>PdfWriter.PAGE_OPEN</CODE>
     * or <CODE>PdfWriter.PAGE_CLOSE</CODE>
     * @param action the action to perform
     * @param page the page where the action will be applied. The first page is 1
     * @throws PdfException if the action type is invalid
     */
    public void setPageAction(final PdfName actionType, final PdfAction action, final int page) throws PdfException {
        stamper.setPageAction(actionType, action, page);
    }

    /**
     * Sets the display duration for the page (for presentations)
     * @param seconds   the number of seconds to display the page. A negative value removes the entry
     * @param page the page where the duration will be applied. The first page is 1
     */
    public void setDuration(final int seconds, final int page) {
        stamper.setDuration(seconds, page);
    }

    /**
     * Sets the transition for the page
     * @param transition   the transition object. A <code>null</code> removes the transition
     * @param page the page where the transition will be applied. The first page is 1
     */
    public void setTransition(final PdfTransition transition, final int page) {
        stamper.setTransition(transition, page);
    }

    /**
     * Applies a digital signature to a document, possibly as a new revision, making
     * possible multiple signatures. The returned PdfStamper
     * can be used normally as the signature is only applied when closing.
     * <p>
     * A possible use for adding a signature without invalidating an existing one is:
     * <p>
     * <pre>
     * KeyStore ks = KeyStore.getInstance("pkcs12");
     * ks.load(new FileInputStream("my_private_key.pfx"), "my_password".toCharArray());
     * String alias = (String)ks.aliases().nextElement();
     * PrivateKey key = (PrivateKey)ks.getKey(alias, "my_password".toCharArray());
     * Certificate[] chain = ks.getCertificateChain(alias);
     * PdfReader reader = new PdfReader("original.pdf");
     * FileOutputStream fout = new FileOutputStream("signed.pdf");
     * PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0', new
     * File("/temp"), true);
     * PdfSignatureAppearance sap = stp.getSignatureAppearance();
     * sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
     * sap.setReason("I'm the author");
     * sap.setLocation("Lisbon");
     * // comment next line to have an invisible signature
     * sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
     * stp.close();
     * </pre>
     * @param reader the original document
     * @param os the output stream or <CODE>null</CODE> to keep the document in the temporary file
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @param tempFile location of the temporary file. If it's a directory a temporary file will be created there.
     *     If it's a file it will be used directly. The file will be deleted on exit unless <CODE>os</CODE> is null.
     *     In that case the document can be retrieved directly from the temporary file. If it's <CODE>null</CODE>
     *     no temporary file will be created and memory will be used
     * @param append if <CODE>true</CODE> the signature and all the other content will be added as a
     * new revision thus not invalidating existing signatures
     * @return a <CODE>PdfStamper</CODE>
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public static PdfStamper createSignature(final PdfReader reader, final OutputStream os, final char pdfVersion, File tempFile, final boolean append) throws DocumentException, IOException {
        PdfStamper stp;
        if (tempFile == null) {
            ByteBuffer bout = new ByteBuffer();
            stp = new PdfStamper(reader, bout, pdfVersion, append);
            stp.sigApp = new PdfSignatureAppearance(stp.stamper);
            stp.sigApp.setSigout(bout);
        }
        else {
            if (tempFile.isDirectory())
                tempFile = File.createTempFile("pdf", null, tempFile);
            FileOutputStream fout = new FileOutputStream(tempFile);
            stp = new PdfStamper(reader, fout, pdfVersion, append);
            stp.sigApp = new PdfSignatureAppearance(stp.stamper);
            stp.sigApp.setTempFile(tempFile);
        }
        stp.sigApp.setOriginalout(os);
        stp.sigApp.setStamper(stp);
        stp.hasSignature = true;
        PdfDictionary catalog = reader.getCatalog();
        PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM), catalog);
        if (acroForm != null) {
            acroForm.remove(PdfName.NEEDAPPEARANCES);
            stp.stamper.markUsed(acroForm);
        }
        return stp;
    }

    /**
     * Applies a digital signature to a document. The returned PdfStamper
     * can be used normally as the signature is only applied when closing.
     * <p>
     * Note that the pdf is created in memory.
     * <p>
     * A possible use is:
     * <p>
     * <pre>
     * KeyStore ks = KeyStore.getInstance("pkcs12");
     * ks.load(new FileInputStream("my_private_key.pfx"), "my_password".toCharArray());
     * String alias = (String)ks.aliases().nextElement();
     * PrivateKey key = (PrivateKey)ks.getKey(alias, "my_password".toCharArray());
     * Certificate[] chain = ks.getCertificateChain(alias);
     * PdfReader reader = new PdfReader("original.pdf");
     * FileOutputStream fout = new FileOutputStream("signed.pdf");
     * PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0');
     * PdfSignatureAppearance sap = stp.getSignatureAppearance();
     * sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
     * sap.setReason("I'm the author");
     * sap.setLocation("Lisbon");
     * // comment next line to have an invisible signature
     * sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
     * stp.close();
     * </pre>
     * @param reader the original document
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @throws DocumentException on error
     * @throws IOException on error
     * @return a <CODE>PdfStamper</CODE>
     */
    public static PdfStamper createSignature(final PdfReader reader, final OutputStream os, final char pdfVersion) throws DocumentException, IOException {
        return createSignature(reader, os, pdfVersion, null, false);
    }

    /**
     * Applies a digital signature to a document. The returned PdfStamper
     * can be used normally as the signature is only applied when closing.
     * <p>
     * A possible use is:
     * <p>
     * <pre>
     * KeyStore ks = KeyStore.getInstance("pkcs12");
     * ks.load(new FileInputStream("my_private_key.pfx"), "my_password".toCharArray());
     * String alias = (String)ks.aliases().nextElement();
     * PrivateKey key = (PrivateKey)ks.getKey(alias, "my_password".toCharArray());
     * Certificate[] chain = ks.getCertificateChain(alias);
     * PdfReader reader = new PdfReader("original.pdf");
     * FileOutputStream fout = new FileOutputStream("signed.pdf");
     * PdfStamper stp = PdfStamper.createSignature(reader, fout, '\0', new File("/temp"));
     * PdfSignatureAppearance sap = stp.getSignatureAppearance();
     * sap.setCrypto(key, chain, null, PdfSignatureAppearance.WINCER_SIGNED);
     * sap.setReason("I'm the author");
     * sap.setLocation("Lisbon");
     * // comment next line to have an invisible signature
     * sap.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
     * stp.close();
     * </pre>
     * @param reader the original document
     * @param os the output stream or <CODE>null</CODE> to keep the document in the temporary file
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @param tempFile location of the temporary file. If it's a directory a temporary file will be created there.
     *     If it's a file it will be used directly. The file will be deleted on exit unless <CODE>os</CODE> is null.
     *     In that case the document can be retrieved directly from the temporary file. If it's <CODE>null</CODE>
     *     no temporary file will be created and memory will be used
     * @return a <CODE>PdfStamper</CODE>
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public static PdfStamper createSignature(final PdfReader reader, final OutputStream os, final char pdfVersion, final File tempFile) throws DocumentException, IOException {
        return createSignature(reader, os, pdfVersion, tempFile, false);
    }

    public static PdfStamper createXmlSignature(final PdfReader reader, final OutputStream os) throws IOException, DocumentException {
        PdfStamper stp = new PdfStamper(reader, os);
        stp.sigXmlApp = new XmlSignatureAppearance(stp.stamper);
        //stp.sigApp.setSigout(bout);
        //stp.sigApp.setOriginalout(os);
        stp.sigXmlApp.setStamper(stp);

        return stp;
    }

    /**
     * Gets the PdfLayer objects in an existing document as a Map
     * with the names/titles of the layers as keys.
     * @return	a Map with all the PdfLayers in the document (and the name/title of the layer as key)
     * @since	2.1.2
     */
    public Map<String, PdfLayer> getPdfLayers() {
    	return stamper.getPdfLayers();
    }
    
    public void markUsed(PdfObject obj) {
        stamper.markUsed(obj);
    }
    
    public LtvVerification getLtvVerification() {
        if (verification == null)
            verification = new LtvVerification(this);
        return verification;
    }
    
    void mergeVerification() throws IOException {
        if (verification == null)
            return;
        verification.merge();
    }

    protected PdfStamper() {

    }
}
