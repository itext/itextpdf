/*
 * Copyright 2003, 2004 by Paulo Soares.
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

import java.security.cert.X509Certificate;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.CertPath;
import java.security.cert.CRL;
import java.security.PrivateKey;
import java.security.KeyStore;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.io.RandomAccessFile;
import java.io.File;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.DocWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;

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
    private boolean hasSignature;
    private ByteBuffer sigout;
    private OutputStream originalout;
    private PrivateKey privKey;
    private Certificate[] certChain;
    private CRL[] crlList;
    private PdfName filter;
    private String reason;
    private String location;
    private String provider;
    private File tempFile;

    /**
     * The self signed filter.
     */
    public static final PdfName SELF_SIGNED = PdfName.ADOBE_PPKLITE;
    /**
     * The VeriSign filter.
     */
    public static final PdfName VERISIGN_SIGNED = PdfName.VERISIGN_PPKVS;
    /**
     * The Windows Certificate Security.
     */
    public static final PdfName WINCER_SIGNED = PdfName.ADOBE_PPKMS;

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

    /**
     * Starts the process of adding extra content to an existing PDF
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
     * @return the map or <CODE>null</CODE>
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


    /**
     * Sets the Cryptographic Service Provider that will sign the document.
     *
     * @param provider the name of the provider, for example <code>SUN<code>, or
     * <code>null</code> to use the default provider.
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * Returns the Cryptographic Service Provider that will sign the document.
     *
     * @returns provider the name of the provider, for example <code>SUN<code>,
     * or <code>null</code> to use the default provider.
     */
    public String getProvider() {
        return this.provider;
    }

    /** Closes the document. No more content can be written after the
     * document is closed.
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public void close() throws DocumentException, IOException {
        if (!hasSignature) {
            stamper.close(moreInfo);
            return;
        }
        try {
            AcroFields af = getAcroFields();
            String name = "Signature";
            int step = 0;
            boolean found = false;
            while (!found) {
                ++step;
                String n1 = name + step;
                if (af.getFieldItem(n1) != null)
                    continue;
                n1 += ".";
                found = true;
                for (Iterator it = af.getFields().keySet().iterator(); it.hasNext();) {
                    String fn = (String)it.next();
                    if (fn.startsWith(n1)) {
                        found = false;
                        break;
                    }
                }
            }
            name += step;
            PdfReader reader = getReader();
            PdfDictionary catalog = reader.getCatalog();
            PdfDictionary acroForm = (PdfDictionary)PdfReader.getPdfObject(catalog.get(PdfName.ACROFORM));
            if (acroForm == null) {
                acroForm = new PdfDictionary();
                catalog.put(PdfName.ACROFORM, acroForm);
                acroForm.put(PdfName.SIGFLAGS, new PdfNumber(3));
            }
            PdfArray fields = (PdfArray)PdfReader.getPdfObject(acroForm.get(PdfName.FIELDS));
            if (fields == null) {
                fields = new PdfArray();
                acroForm.put(PdfName.FIELDS, fields);
            }
            PdfWriter writer = getWriter();
            PdfIndirectReference refWidget = writer.getPdfIndirectReference();
            PdfIndirectReference refSig = writer.getPdfIndirectReference();
            fields.add(refWidget);
            PdfDictionary widget = new PdfDictionary(PdfName.ANNOT);
            widget.put(PdfName.SUBTYPE, PdfName.WIDGET);
            widget.put(PdfName.F, new PdfNumber(132));
            widget.put(PdfName.FT, PdfName.SIG);
            widget.put(PdfName.RECT, new PdfArray(new int[]{0, 0, 0, 0}));
            widget.put(PdfName.T, new PdfString(name));
            widget.put(PdfName.V, refSig);
            widget.put(PdfName.P, reader.getPageOrigRef(1));
            PdfDictionary page = reader.getPageN(1);
            PdfArray annots = (PdfArray)PdfReader.getPdfObject(page.get(PdfName.ANNOTS));
            if (annots == null) {
                annots = new PdfArray();
                page.put(PdfName.ANNOTS, annots);
            }
            annots.add(refWidget);
            writer.addToBody(widget, refWidget);
            PdfSigGenericPKCS sig = null;
            if (PdfName.ADOBE_PPKLITE.equals(filter))
                sig = new PdfSigGenericPKCS.PPKLite(getProvider());
            else if (PdfName.ADOBE_PPKMS.equals(filter))
                sig = new PdfSigGenericPKCS.PPKMS(getProvider());
            else if (PdfName.VERISIGN_PPKVS.equals(filter))
                sig = new PdfSigGenericPKCS.VeriSign(getProvider());
            else
                throw new IllegalArgumentException("Unknown filter: " + filter.toString());
            if (reason != null)
                sig.setReason(reason);
            if (location != null)
                sig.setLocation(location);
            sig.setSignInfo(privKey, certChain, crlList);
            int position = writer.getOs().getCounter();
            writer.addToBody(sig, refSig, false);
            stamper.close(moreInfo);
            if (tempFile == null) {
                byte bout[] = sigout.getBuffer();
                int boutLen = sigout.size();
                int br = indexArray(bout, position, "/ByteRange");
                int cr = indexArray(bout, position, "/Contents");
                int ecr = indexArray(bout, cr, ">");
                ByteBuffer bf = new ByteBuffer();
                int range[] = {0, cr + 9, ecr + 1, boutLen - ecr - 1};
                bf.append("/ByteRange[0 ").append(range[1]).append(' ').append(range[2]).append(' ').append(range[3]).append(']');
                System.arraycopy(bf.getBuffer(), 0, bout, br, bf.size());
                for (int k = 0; k < range.length; ++k) {
                    int start = range[k];
                    int length = range[++k];
                    sig.getSigner().update(bout, start, length);
                }
                byte ar2[] = sig.getSignerContents();
                PdfString cout = new PdfString(ar2).setWritingMode(true);
                bf.reset();
                cout.toPdf(null, bf);
                System.arraycopy(bf.getBuffer(), 0, bout, range[1], bf.size());
                originalout.write(bout, 0, boutLen);
            }
            else {
                RandomAccessFile raf = null;
                try {
                    raf = new RandomAccessFile(tempFile, "rw");
                    int boutLen = (int)raf.length();
                    int br = indexFile(raf, position, "/ByteRange");
                    int cr = indexFile(raf, position, "/Contents");
                    int ecr = indexFile(raf, cr, ">");
                    ByteBuffer bf = new ByteBuffer();
                    int range[] = {0, cr + 9, ecr + 1, boutLen - ecr - 1};
                    bf.append("/ByteRange[0 ").append(range[1]).append(' ').append(range[2]).append(' ').append(range[3]).append(']');
                    raf.seek(br);
                    raf.write(bf.getBuffer(), 0, bf.size());
                    byte ar2[] = new byte[8192];
                    for (int k = 0; k < range.length; ++k) {
                        int start = range[k];
                        int length = range[++k];
                        raf.seek(start);
                        while (length > 0) {
                            int r = raf.read(ar2, 0, Math.min(ar2.length, length));
                            if (r < 0)
                                throw new EOFException("Unexpected EOF");
                            sig.getSigner().update(ar2, 0, r);
                            length -= r;
                        }
                    }
                    byte ar3[] = sig.getSignerContents();
                    PdfString cout = new PdfString(ar3).setWritingMode(true);
                    bf.reset();
                    cout.toPdf(null, bf);
                    raf.seek(0);
                    int length = range[1];
                    while (length > 0) {
                        int r = raf.read(ar2, 0, Math.min(ar2.length, length));
                        if (r < 0)
                            throw new EOFException("Unexpected EOF");
                        originalout.write(ar2, 0, r);
                        length -= r;
                    }
                    originalout.write(bf.getBuffer(), 0, bf.size());
                    raf.seek(range[1] + bf.size());
                    length = boutLen - (range[1] + bf.size());
                    while (length > 0) {
                        int r = raf.read(ar2, 0, Math.min(ar2.length, length));
                        if (r < 0)
                            throw new EOFException("Unexpected EOF");
                        originalout.write(ar2, 0, r);
                        length -= r;
                    }
                }
                finally {
                    try{raf.close();}catch(Exception ee){}
                    try{tempFile.delete();}catch(Exception ee){}
                }
            }
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        finally {
            try{originalout.close();}catch(Exception e){}
        }
    }

    private static int indexArray(byte bout[], int position, String search) {
        byte ss[] = PdfEncodings.convertToBytes(search, null);
        while (true) {
            int k;
            for (k = 0; k < ss.length; ++k) {
                if (ss[k] != bout[position + k])
                    break;
            }
            if (k == ss.length)
                return position;
            ++position;
        }
    }

    private static int indexFile(RandomAccessFile raf, int position, String search) throws IOException {
        byte ss[] = PdfEncodings.convertToBytes(search, null);
        while (true) {
            raf.seek(position);
            int k;
            for (k = 0; k < ss.length; ++k) {
                int b = raf.read();
                if (b < 0)
                    throw new EOFException("Unexpected EOF");
                if (ss[k] != (byte)b)
                    break;
            }
            if (k == ss.length)
                return position;
            ++position;
        }
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

    /**
     * Sets the bookmarks. The list structure is defined in
     * <CODE>SimpleBookmark#</CODE>.
     * @param outlines the bookmarks or <CODE>null</CODE> to remove any
     * @throws IOException on error
     */
    public void setOutlines(List outlines) throws IOException {
        stamper.setOutlines(outlines);
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
    public boolean partialFormFlattening(String name) {
        return stamper.partialFormFlattening(name);
    }

    /** Adds a JavaScript action at the document level. When the document
     * opens all this JavaScript runs.
     * @param js the JavaScript code
     */
    public void addJavaScript(String js) {
        stamper.addJavaScript(js, !PdfEncodings.isPdfDocEncoding(js));
    }

    /**
     * Sets the viewer preferences.
     * @param preferences the viewer preferences
     * @see PdfWriter#setViewerPreferences(int)
     */
    public void setViewerPreferences(int preferences) {
        stamper.setViewerPreferences(preferences);
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
    public void setFullCompression() {
        stamper.setFullCompression();
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
     * PdfStamper stp = PdfStamper.createInvisibleSignature(reader, fout, '\0', key, chain, null, PdfStamper.WINCER_SIGNED, "I'm the author", "Lisbon");
     * stp.close();
     * </pre>
     * @param reader the original document
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @param privKey the certificate private key
     * @param certChain the certificate chain
     * @param crlList the certificate revocation list
     * @param filter the signature type to use. It can be SELF_SIGNED, VERISIGN_SIGNED
     * or WINCER_SIGNED
     * @param reason the signing reason or <CODE>null</CODE>
     * @param location the signing location or <CODE>null</CODE>
     * @param tempFile location of the temporary file. If it's a directory a temporary file will be created there.
     *     If it's a file it will be used directly. The file will be deleted on exit. If it's <CODE>null</CODE>
     *     no temporary file will be created and memory will be used
     * @throws DocumentException on error
     * @throws IOException on error
     * @return a <CODE>PdfStamper</CODE>
     */
    public static PdfStamper createInvisibleSignature(PdfReader reader, OutputStream os, char pdfVersion, PrivateKey privKey, Certificate[] certChain, CRL[] crlList, PdfName filter, String reason, String location, File tempFile) throws DocumentException, IOException {
        if (privKey == null)
            throw new NullPointerException("The private key cannot be null.");
        if (certChain == null)
            throw new NullPointerException("The certificate chain cannot be null.");
        if (certChain.length == 0)
            throw new IllegalArgumentException("The certificate chain cannot be empty.");
        PdfStamper stp;
        if (tempFile == null) {
            ByteBuffer bout = new ByteBuffer();
            stp = new PdfStamper(reader, bout, pdfVersion);
            stp.sigout = bout;
        }
        else {
            if (tempFile.isDirectory())
                tempFile = File.createTempFile("pdf", null, tempFile);
            FileOutputStream fout = new FileOutputStream(tempFile);
            stp = new PdfStamper(reader, fout, pdfVersion);
            stp.tempFile = tempFile;
        }
        stp.originalout = os;
        stp.hasSignature = true;
        stp.privKey = privKey;
        stp.certChain = certChain;
        stp.crlList = crlList;
        stp.filter = filter;
        stp.reason = reason;
        stp.location = location;
        return stp;
    }

    /**
     * Applies a digital signature to a document. The returned PdfStamper
     * can be used normally as the signature is only applied when closing.
     * <p>
     * Note that the pdf in created in memory.
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
     * PdfStamper stp = PdfStamper.createInvisibleSignature(reader, fout, '\0', key, chain, null, PdfStamper.WINCER_SIGNED, "I'm the author", "Lisbon");
     * stp.close();
     * </pre>
     * @param reader the original document
     * @param os the output stream
     * @param pdfVersion the new pdf version or '\0' to keep the same version as the original
     * document
     * @param privKey the certificate private key
     * @param certChain the certificate chain
     * @param crlList the certificate revocation list
     * @param filter the signature type to use. It can be SELF_SIGNED, VERISIGN_SIGNED
     * or WINCER_SIGNED
     * @param reason the signing reason or <CODE>null</CODE>
     * @param location the signing location or <CODE>null</CODE>
     * @throws DocumentException on error
     * @throws IOException on error
     * @return a <CODE>PdfStamper</CODE>
     */
    public static PdfStamper createInvisibleSignature(PdfReader reader, OutputStream os, char pdfVersion, PrivateKey privKey, Certificate[] certChain, CRL[] crlList, PdfName filter, String reason, String location) throws DocumentException, IOException 
    {
        return createInvisibleSignature(reader,  os, pdfVersion, privKey, certChain, crlList, filter, reason, location, null);
    }
}