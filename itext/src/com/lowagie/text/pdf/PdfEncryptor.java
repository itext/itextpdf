/*
/*
 * Copyright 2002 Paulo Soares
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
import java.util.HashMap;
import java.util.Iterator;

/** This class takes any PDF and returns exactly the same but
 * encrypted. All the content, links, outlines, etc, are kept.
 * It is also possible to chage the info dictionary.
 */
public class PdfEncryptor extends PdfWriter {
    
    RandomAccessFileOrArray file;
    PdfReader reader;
    int myXref[];
    
    /** Creates new PdfEncryptor.
     * @param reader the read PDF
     * @param os the output destination
     * @throws DocumentException on error
     */
    protected PdfEncryptor(PdfReader reader, OutputStream os) throws DocumentException {
        super(new PdfDocument(), os);
        this.reader = reader;
        file = reader.getSafeFile();
    }
    
    /** Entry point to encrypt a PDF document. The encryption parameters are the same as in
     * <code>PdfWriter</code>. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param reader the read PDF
     * @param os the output destination
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @param strength128Bits true for 128 bit key length. false for 40 bit key length
     * @throws DocumentException on error
     * @throws IOException on error */
    public static void encrypt(PdfReader reader, OutputStream os, byte userPassword[], byte ownerPassword[], int permissions, boolean strength128Bits) throws DocumentException, IOException {
        PdfEncryptor enc = new PdfEncryptor(reader, os);
        enc.setEncryption(userPassword, ownerPassword, permissions, strength128Bits);
        enc.go();
    }
    
    /** Entry point to encrypt a PDF document. The encryption parameters are the same as in
     * <code>PdfWriter</code>. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param reader the read PDF
     * @param os the output destination
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @param strength128Bits true for 128 bit key length. false for 40 bit key length
     * @param newInfo an optional <CODE>String</CODE> map to add or change
     * the info dictionary. Entries with <CODE>null</CODE>
     * values delete the key in the original info dictionary
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public static void encrypt(PdfReader reader, OutputStream os, byte userPassword[], byte ownerPassword[], int permissions, boolean strength128Bits, HashMap newInfo) throws DocumentException, IOException {
        PdfEncryptor enc = new PdfEncryptor(reader, os);
        enc.setEncryption(userPassword, ownerPassword, permissions, strength128Bits);
        enc.go(newInfo);
    }
    
    /** Entry point to encrypt a PDF document. The encryption parameters are the same as in
     * <code>PdfWriter</code>. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param reader the read PDF
     * @param os the output destination
     * @param strength true for 128 bit key length. false for 40 bit key length
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @throws DocumentException on error
     * @throws IOException on error */
    public static void encrypt(PdfReader reader, OutputStream os, boolean strength, String userPassword, String ownerPassword, int permissions) throws DocumentException, IOException {
        PdfEncryptor enc = new PdfEncryptor(reader, os);
        enc.setEncryption(strength, userPassword, ownerPassword, permissions);
        enc.go();
    }
    
    /** Entry point to encrypt a PDF document. The encryption parameters are the same as in
     * <code>PdfWriter</code>. The userPassword and the
     *  ownerPassword can be null or have zero length. In this case the ownerPassword
     *  is replaced by a random string. The open permissions for the document can be
     *  AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations,
     *  AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting.
     *  The permissions can be combined by ORing them.
     * @param reader the read PDF
     * @param os the output destination
     * @param strength true for 128 bit key length. false for 40 bit key length
     * @param userPassword the user password. Can be null or empty
     * @param ownerPassword the owner password. Can be null or empty
     * @param permissions the user permissions
     * @param newInfo an optional <CODE>String</CODE> map to add or change
     * the info dictionary. Entries with <CODE>null</CODE>
     * values delete the key in the original info dictionary
     * @throws DocumentException on error
     * @throws IOException on error
     */
    public static void encrypt(PdfReader reader, OutputStream os, boolean strength, String userPassword, String ownerPassword, int permissions, HashMap newInfo) throws DocumentException, IOException {
        PdfEncryptor enc = new PdfEncryptor(reader, os);
        enc.setEncryption(strength, userPassword, ownerPassword, permissions);
        enc.go(newInfo);
    }
    
    /** Does the actual document manipulation to encrypt it.
     * @throws DocumentException on error
     * @throws IOException on error
     */
    protected void go() throws DocumentException, IOException {
        go(null);
    }
    
    /** Does the actual document manipulation to encrypt it.
     * @param moreInfo an optional <CODE>String</CODE> map to add or change
     * the info dictionary. Entries with <CODE>null</CODE>
     * values delete the key in the original info dictionary
     * @throws DocumentException on error
     * @throws IOException on error
     */
    protected void go(HashMap moreInfo) throws DocumentException, IOException {
        body = new PdfBody(HEADER.length, this, true);
        os.write(HEADER);
        PdfObject xb[] = reader.xrefObj;
        myXref = new int[xb.length];
        int idx = 1;
        PRIndirectReference iInfo = (PRIndirectReference)reader.trailer.get(PdfName.INFO);
        int skip = -1;
        if (iInfo != null)
            skip = iInfo.getNumber();
        for (int k = 1; k < xb.length; ++k) {
            if (xb[k] != null && skip != k)
                myXref[k] = idx++;
        }
        file.reOpen();
        for (int k = 1; k < xb.length; ++k) {
            if (xb[k] != null && skip != k)
                addToBody(xb[k]);
        }
        file.close();
        PdfIndirectReference encryption = null;
        PdfLiteral fileID = null;
        if (crypto != null) {
            PdfIndirectObject encryptionObject = body.add(crypto.getEncryptionDictionary());
            encryptionObject.writeTo(os);
            encryption = encryptionObject.getIndirectReference();
            fileID = crypto.getFileID();
        }
        PRIndirectReference iRoot = (PRIndirectReference)reader.trailer.get(PdfName.ROOT);
        PdfIndirectReference root = new PdfIndirectReference(0, myXref[iRoot.getNumber()]);
        PdfIndirectReference info = null;
        PdfDictionary oldInfo = (PdfDictionary)reader.getPdfObject(iInfo);
        PdfDictionary newInfo = new PdfDictionary();
        if (oldInfo != null) {
            for (Iterator i = oldInfo.getKeys().iterator(); i.hasNext();) {
                PdfName key = (PdfName)i.next();
                PdfObject value = reader.getPdfObject(oldInfo.get(key));
                newInfo.put(key, value);
            }
        }
        if (moreInfo != null) {
            for (Iterator i = moreInfo.keySet().iterator(); i.hasNext();) {
                String key = (String)i.next();
                PdfName keyName = new PdfName(key);
                String value = (String)moreInfo.get(key);
                if (value == null)
                    newInfo.remove(keyName);
                else
                    newInfo.put(keyName, new PdfString(value, PdfObject.TEXT_UNICODE));
            }
        }
        if (!newInfo.getKeys().isEmpty())
            info = addToBody(newInfo).getIndirectReference();
        // write the cross-reference table of the body
        os.write(body.getCrossReferenceTable());
        PdfTrailer trailer = new PdfTrailer(body.size(),
        body.offset(),
        root,
        info,
        encryption,
        fileID);
        os.write(trailer.toPdf(this));
        os.close();
    }
    
    int getNewObjectNumber(PdfReader reader, int number, int generation) {
        return myXref[number];
    }
    
    RandomAccessFileOrArray getReaderFile(PdfReader reader) {
        return file;
    }    
}