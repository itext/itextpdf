/*
 * Copyright 2003 by Paulo Soares.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */
package com.lowagie.text.pdf;

import java.io.OutputStream;
import java.io.IOException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.DocWriter;
import java.util.HashMap;

/** Applies extra content to the pages of a PDF document.
 * This extra content can be all the objects allowed in PdfContentByte
 * including pages from other Pdfs. The original PDF will keep
 * all the interactive elements including bookmarks, links and form fields.
 * <p>
 * It is also possible to change the field values and to
 * flatten them.
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class PdfStamper {
    protected PdfStamperImp stamper;
    private HashMap moreInfo;
    
    /** Starts the process of adding extra content to an existing PDF
     * document.
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @throws DocumentException on error
     * @throws IOException on error
     */    
    public PdfStamper(PdfReader reader, OutputStream os) throws DocumentException, IOException {
        stamper = new PdfStamperImp(reader, os, '\0');
    }
    
    /** Starts the process of adding extra content to an existing PDF
     * document.
     * @param reader the original document. It cannot be reused
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @throws DocumentException on error
     * @throws IOException on error
     */    
    public PdfStamper(PdfReader reader, OutputStream os, char pdfVersion) throws DocumentException, IOException {
        stamper = new PdfStamperImp(reader, os, pdfVersion);
    }
    
    /** Gets the optional <CODE>String</CODE> map to add or change values in
     * the info dictionary.
     * @return the map or CODE>null</CODE>
     *
     */
    public HashMap getMoreInfo() {
        return this.moreInfo;
    }
    
    /** An optional <CODE>String</CODE> map to add or change values in
     * the info dictionary. Entries with <CODE>null</CODE>
     * values delete the key in the original info dictionary
     * @param moreInfo additional entries to the info dictionary
     *
     */
    public void setMoreInfo(HashMap moreInfo) {
        this.moreInfo = moreInfo;
    }
    
    /** Closes the document. No more content can be written after the
     * document is closed.
     * @throws DocumentException on error
     * @throws IOException on error
     */    
    public void close() throws DocumentException, IOException {
        stamper.close(moreInfo);
    }
    
    /** Gets a <CODE>PdfContentByte</CODE> to write under the page of
     * the original document.
     * @param pageNum the page number where the extra content is written
     * @return a <CODE>PdfContentByte</CODE> to write under the page of
     * the original document
     */    
    public PdfContentByte getUnderContent(int pageNum) {
        return stamper.getUnderContent(pageNum);
    }

    /** Gets a <CODE>PdfContentByte</CODE> to write over the page of
     * the original document.
     * @param pageNum the page number where the extra content is written
     * @return a <CODE>PdfContentByte</CODE> to write over the page of
     * the original document
     */    
    public PdfContentByte getOverContent(int pageNum) {
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
    public void setRotateContents(boolean rotateContents) {
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
     * @param strength128Bits true for 128 bit key length. false for 40 bit key length
     * @throws DocumentException if anything was already written to the output
     */
    public void setEncryption(byte userPassword[], byte ownerPassword[], int permissions, boolean strength128Bits) throws DocumentException {
        if (stamper.isContentWritten())
            throw new DocumentException("Content was already written to the output.");
        stamper.setEncryption(userPassword, ownerPassword, permissions, strength128Bits);
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
     * @throws DocumentException if anything was already written to the output
     */
    public void setEncryption(boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException {
        setEncryption(DocWriter.getISOBytes(userPassword), DocWriter.getISOBytes(ownerPassword), permissions, strength);
    }

    /** Gets a page from other PDF document. Note that calling this method more than
     * once with the same parameters will retrieve the same object.
     * @param reader the PDF document where the page is
     * @param pageNumber the page number. The first page is 1
     * @return the template representing the imported page
     */
    public PdfImportedPage getImportedPage(PdfReader reader, int pageNumber) {
        return stamper.getImportedPage(reader, pageNumber);
    }
    
    /** Gets the underlying PdfWriter.
     * @return the underlying PdfWriter
     */    
    public PdfWriter getWriter() {
        return stamper;
    }
    
    /** Gets the <CODE>AcroFields</CODE> object that allows to get and set field values
     * and to merge FDF forms.
     * @return the <CODE>AcroFields</CODE> object
     */    
    public AcroFields getAcroFields() {
        return stamper.getAcroFields();
    }
    
    /** Determines if the fields are flattened on close.
     * @param flat <CODE>true</CODE> to flatten the fields, <CODE>false</CODE>
     * to keep the fields
     */    
    public void setFormFlattening(boolean flat) {
        stamper.setFormFlattening(flat);
    }

    /** Adds an annotation in a specific page.
     * @param annot the annotation
     * @param page the page
     */    
    public void addAnnotation(PdfAnnotation annot, int page) {
        stamper.addAnnotation(annot, page);
    }
    
}