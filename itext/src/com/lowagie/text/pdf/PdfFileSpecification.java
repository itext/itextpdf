/*
 * Copyright 2003 Paulo Soares
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
/** Specifies a file or an URL. The file can be extern or embedded.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class PdfFileSpecification extends PdfDictionary {
    protected PdfWriter writer;
    protected PdfIndirectReference ref;
    
    /** Creates a new instance of PdfFileSpecification. The static methods are preferred. */
    public PdfFileSpecification() {
        super(PdfName.FILESPEC);
    }
    
    /**
     * Creates a file specification of type URL.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param url the URL
     * @return the file specification
     */    
    public static PdfFileSpecification url(PdfWriter writer, String url) {
        PdfFileSpecification fs = new PdfFileSpecification();
        fs.writer = writer;
        fs.put(PdfName.FS, PdfName.URL);
        fs.put(PdfName.F, new PdfString(url));
        return fs;
    }

    /**
     * Creates a file specification with the file embedded. The file may
     * come from the file system or from a byte array. The data is flate compressed.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param filePath the file path
     * @param fileDisplay the file information that is presented to the user
     * @param fileStore the byte array with the file. If it is not <CODE>null</CODE>
     * it takes precedence over <CODE>filePath</CODE>
     * @throws IOException on error
     * @return the file specification
     */    
    public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte fileStore[]) throws IOException {
        return fileEmbedded(writer, filePath, fileDisplay, fileStore, true);
    }
    
    
    /**
     * Creates a file specification with the file embedded. The file may
     * come from the file system or from a byte array.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param filePath the file path
     * @param fileDisplay the file information that is presented to the user
     * @param fileStore the byte array with the file. If it is not <CODE>null</CODE>
     * it takes precedence over <CODE>filePath</CODE>
     * @param compress sets the compression on the data. Multimedia content will benefit little
     * from compression
     * @throws IOException on error
     * @return the file specification
     */    
    public static PdfFileSpecification fileEmbedded(PdfWriter writer, String filePath, String fileDisplay, byte fileStore[], boolean compress) throws IOException {
        PdfFileSpecification fs = new PdfFileSpecification();
        fs.writer = writer;
        fs.put(PdfName.F, new PdfString(fileDisplay));
        PdfStream stream;
        InputStream in = null;
        PdfIndirectReference ref;
        PdfIndirectReference refFileLength;
        try {
            refFileLength = writer.getPdfIndirectReference();
            if (fileStore == null) {
                File file = new File(filePath);
                if (file.canRead()) {
                    in = new FileInputStream(filePath);
                }
                else {
                    if (filePath.startsWith("file:/") || filePath.startsWith("http://") || filePath.startsWith("https://") || filePath.startsWith("jar:")) {
                        in = new URL(filePath).openStream();
                    }
                    else {
                        in = BaseFont.getResourceStream(filePath);
                        if (in == null)
                            throw new IOException(filePath + " not found as file or resource.");
                    }
                }
                stream = new PdfStream(in, writer);
            }
            else
                stream = new PdfStream(fileStore);
            stream.put(PdfName.TYPE, PdfName.EMBEDDEDFILE);
            if (compress)
                stream.flateCompress();
            stream.put(PdfName.PARAMS, refFileLength);
            ref = writer.addToBody(stream).getIndirectReference();
            if (fileStore == null) {
                stream.writeLength();
            }
            PdfDictionary params = new PdfDictionary();
            params.put(PdfName.SIZE, new PdfNumber(stream.getRawLength()));
            writer.addToBody(params, refFileLength);
        }
        finally {
            if (in != null)
                try{in.close();}catch(Exception e){}
        }
        PdfDictionary f = new PdfDictionary();
        f.put(PdfName.F, ref);
        fs.put(PdfName.EF, f);
        return fs;
    }
    
    /**
     * Creates a file specification for an external file.
     * @param writer the <CODE>PdfWriter</CODE>
     * @param filePath the file path
     * @return the file specification
     */
    public static PdfFileSpecification fileExtern(PdfWriter writer, String filePath) {
        PdfFileSpecification fs = new PdfFileSpecification();
        fs.writer = writer;
        fs.put(PdfName.F, new PdfString(filePath));
        return fs;
    }
    
    /**
     * Gets the indirect reference to this file specification.
     * Multiple invocations will retrieve the same value.
     * @throws IOException on error
     * @return the indirect reference
     */    
    public PdfIndirectReference getReference() throws IOException {
        if (ref != null)
            return ref;
        ref = writer.addToBody(this).getIndirectReference();
        return ref;
    }
    
    /**
     * Sets the file name (the key /F) string as an hex representation
     * to support multi byte file names. The name must have the slash and
     * backslash escaped according to the file specification rules
     * @param fileName the file name as a byte array
     */    
    public void setMultiByteFileName(byte fileName[]) {
        put(PdfName.F, new PdfString(fileName).setHexWriting(true));
    }
}
