/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;
import com.lowagie.text.Document;
import com.lowagie.text.DocWriter;
import com.lowagie.text.ExceptionConverter;

/**
 * <CODE>PdfStream</CODE> is the Pdf stream object.
 * <P>
 * A stream, like a string, is a sequence of characters. However, an application can
 * read a small portion of a stream at a time, while a string must be read in its entirety.
 * For this reason, objects with potentially large amounts of data, such as images and
 * page descriptions, are represented as streams.<BR>
 * A stream consists of a dictionary that describes a sequence of characters, followed by
 * the keyword <B>stream</B>, followed by zero or more lines of characters, followed by
 * the keyword <B>endstream</B>.<BR>
 * All streams must be <CODE>PdfIndirectObject</CODE>s. The stream dictionary must be a direct
 * object. The keyword <B>stream</B> that follows the stream dictionary should be followed by
 * a carriage return and linefeed or just a linefeed.<BR>
 * Remark: In this version only the FLATEDECODE-filter is supported.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.8 (page 41-53).<BR>
 *
 * @see		PdfObject
 * @see		PdfDictionary
 */

public class PdfStream extends PdfDictionary {
    
    // membervariables
    
/** is the stream compressed? */
    protected boolean compressed = false;
    
    protected ByteArrayOutputStream streamBytes = null;
    protected InputStream inputStream;
    protected PdfIndirectReference ref;
    protected int inputStreamLength = -1;
    protected PdfWriter writer;
        
    static final byte STARTSTREAM[] = DocWriter.getISOBytes("stream\n");
    static final byte ENDSTREAM[] = DocWriter.getISOBytes("\nendstream");
    static final int SIZESTREAM = STARTSTREAM.length + ENDSTREAM.length;

    // constructors
    
/**
 * Constructs a <CODE>PdfStream</CODE>-object.
 *
 * @param		bytes			content of the new <CODE>PdfObject</CODE> as an array of <CODE>byte</CODE>.
 */
 
    public PdfStream(byte[] bytes) {
        super();
        type = STREAM;
        this.bytes = bytes;
        put(PdfName.LENGTH, new PdfNumber(bytes.length));
    }
  
    /**
     * Creates an efficient stream. No temporary array is ever created. The <CODE>InputStream</CODE>
     * is totally consumed but is not closed. The general usage is:
     * <p>
     * <pre>
     * InputStream in = ...;
     * PdfStream stream = new PdfStream(in, writer);
     * stream.flateCompress();
     * writer.addToBody(stream);
     * stream.writeLength();
     * in.close();
     * </pre>
     * @param inputStream the data to write to this stream
     * @param writer the <CODE>PdfWriter</CODE> for this stream
     */    
    public PdfStream(InputStream inputStream, PdfWriter writer) {
        super();
        type = STREAM;
        this.inputStream = inputStream;
        this.writer = writer;
        ref = writer.getPdfIndirectReference();
        put(PdfName.LENGTH, ref);
    }
  
/**
 * Constructs a <CODE>PdfStream</CODE>-object.
 */
    
    protected PdfStream() {
        super();
        type = STREAM;
    }
    
    /**
     * Writes the stream length to the <CODE>PdfWriter</CODE>.
     * <p>
     * This method must be called and can only be called if the contructor {@link #PdfStream(InputStream,PdfWriter)}
     * is used to create the stream.
     * @throws IOException on error
     * @see PdfStream(InputStream,PdfWriter)
     */
    public void writeLength() throws IOException {
        if (inputStream == null)
            throw new UnsupportedOperationException("writeLength() can only be called in a contructed PdfStream(InputStream,PdfWriter).");
        if (inputStreamLength == -1)
            throw new IOException("writeLength() can only be called after output of the stream body.");
        writer.addToBody(new PdfNumber(inputStreamLength), ref);
    }
    
    /**
     * Compresses the stream.
     */
    
    public void flateCompress() {
        if (!Document.compress)
            return;
        // check if the flateCompress-method has already been
        if (compressed) {
            return;
        }
        if (inputStream != null) {
            compressed = true;
            return;
        }
        // check if a filter already exists
        PdfObject filter = get(PdfName.FILTER);
        if (filter != null) {
            if (filter.isName() && ((PdfName) filter).compareTo(PdfName.FLATEDECODE) == 0) {
                return;
            }
            else if (filter.isArray() && ((PdfArray) filter).contains(PdfName.FLATEDECODE)) {
                return;
            }
            else {
                throw new RuntimeException("Stream could not be compressed: filter is not a name or array.");
            }
        }
        try {
            // compress
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            DeflaterOutputStream zip = new DeflaterOutputStream(stream);
            if (streamBytes != null)
                streamBytes.writeTo(zip);
            else
                zip.write(bytes);
            zip.close();
            // update the object
            streamBytes = stream;
            bytes = null;
            put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
            if (filter == null) {
                put(PdfName.FILTER, PdfName.FLATEDECODE);
            }
            else {
                PdfArray filters = new PdfArray(filter);
                filters.add(PdfName.FLATEDECODE);
                put(PdfName.FILTER, filters);
            }
            compressed = true;
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

//    public int getStreamLength(PdfWriter writer) {
//        if (dicBytes == null)
//            toPdf(writer);
//        if (streamBytes != null)
//            return streamBytes.size() + dicBytes.length + SIZESTREAM;
//        else
//            return bytes.length + dicBytes.length + SIZESTREAM;
//    }
    
    protected void superToPdf(PdfWriter writer, OutputStream os) throws IOException {
        super.toPdf(writer, os);
    }
    
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        if (inputStream != null && compressed)
            put(PdfName.FILTER, PdfName.FLATEDECODE);
        superToPdf(writer, os);
        os.write(STARTSTREAM);
        PdfEncryption crypto = null;
        if (writer != null)
            crypto = writer.getEncryption();
        if (crypto != null)
            crypto.prepareKey();
        if (inputStream != null) {
            DeflaterOutputStream def = null;
            PdfEncryptionStream encs = null;
            OutputStreamCounter osc = new OutputStreamCounter(os);
            OutputStream fout = osc;
            if (crypto != null)
                fout = encs = new PdfEncryptionStream(fout, crypto);
            if (compressed)    
                fout = def = new DeflaterOutputStream(fout, new Deflater(Deflater.BEST_COMPRESSION), 0x8000);
            
            byte buf[] = new byte[0x10000];
            while (true) {
                int n = inputStream.read(buf);
                if (n <= 0)
                    break;
                fout.write(buf, 0, n);
            }
            if (def != null)
                def.finish();
            inputStreamLength = osc.getCounter();
        }
        else {
            if (crypto == null) {
                if (streamBytes != null)
                    streamBytes.writeTo(os);
                else
                    os.write(bytes);
            }
            else {
                byte b[];
                if (streamBytes != null) {
                    b = streamBytes.toByteArray();
                    crypto.encryptRC4(b);
                }
                else {
                    b = new byte[bytes.length];
                    crypto.encryptRC4(bytes, b);
                }
                os.write(b);
            }
        }
        os.write(ENDSTREAM);
    }
}