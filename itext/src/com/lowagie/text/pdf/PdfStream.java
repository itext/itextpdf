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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DeflaterOutputStream;
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

class PdfStream extends PdfObject {
    
    // membervariables
    
/** the stream dictionary */
    protected PdfDictionary dictionary;
    
/** is the stream compressed? */
    protected boolean compressed = false;
    
    protected ByteArrayOutputStream streamBytes = null;
    
    protected byte dicBytes[] = null;
    
    static final byte STARTSTREAM[] = DocWriter.getISOBytes("\nstream\n");
    static final byte ENDSTREAM[] = DocWriter.getISOBytes("\nendstream");
    static final int SIZESTREAM = STARTSTREAM.length + ENDSTREAM.length;

    // constructors
    
/**
 * Constructs a <CODE>PdfStream</CODE>-object.
 *
 * @param		dictionary		the stream dictionary
 * @param		str				the stream
 */
    
    PdfStream(PdfDictionary dictionary, String stream) {
        super(STREAM);
        this.dictionary = dictionary;
        
        // The length of a line in a PDF document is limited to 256 characters
        StringBuffer streamContent = new StringBuffer();
        int numberProcessed = 0;
        int numberToProcess = stream.length();
        int positionOfNewLine;
        int lengthOfTheLine;
        while (numberProcessed < numberToProcess) {
            positionOfNewLine = stream.indexOf('\n', numberProcessed) + 1;
            lengthOfTheLine = positionOfNewLine - numberProcessed;
            if (lengthOfTheLine < 250 && lengthOfTheLine > 0) {
                streamContent.append(stream.substring(numberProcessed, positionOfNewLine));
                numberProcessed = positionOfNewLine;
            }
            else {
                try {
                    streamContent.append(stream.substring(numberProcessed, numberProcessed + 250) + "\n");
                }
                catch(IndexOutOfBoundsException ioobe) {
                    streamContent.append(stream.substring(numberProcessed) + "\n");
                }
                numberProcessed += 250;
            }
        }
        
        setContent(streamContent.toString());
        dictionary.put(PdfName.LENGTH, new PdfNumber(bytes.length));
    }
    
/**
 * Constructs a <CODE>PdfStream</CODE>-object.
 *
 * @param		bytes			content of the new <CODE>PdfObject</CODE> as an array of <CODE>byte</CODE>.
 */
 
    PdfStream(byte[] bytes) {
        super(STREAM);
        dictionary = new PdfDictionary();
        this.bytes = bytes;
        dictionary.put(PdfName.LENGTH, new PdfNumber(bytes.length));
    }
  
/**
 * Constructs a <CODE>PdfStream</CODE>-object.
 *
 * @param		str				the stream
 */
    
    PdfStream(String stream) {
        this(new PdfDictionary(), stream);
    }
    
/**
 * Constructs a <CODE>PdfStream</CODE>-object.
 */
    
    protected PdfStream() {
        super(STREAM);
        dictionary = new PdfDictionary();
    }
    
    // methods overriding some methods of PdfObject
    
/**
 * Returns the PDF representation of this <CODE>PdfObject</CODE> as an array of <CODE>bytes</CODE>s.
 *
 * @return		an array of <CODE>byte</CODE>s
 */
    
    public byte[] toPdf(PdfWriter writer) {
        dicBytes = dictionary.toPdf(writer);
        return null;
    }
    
    // methods
    
/**
 * Compresses the stream.
 *
 * @return		<CODE>void<CODE>
 * @throws		<CODE>PdfException<CODE> if a filter is allready defined
 */
    
    synchronized final void flateCompress() throws PdfException {
        if (!Document.compress)
            return;
        // check if the flateCompress-method has allready been
        if (compressed) {
            return;
        }
        // check if a filter allready exists
        PdfObject filter = dictionary.get(PdfName.FILTER);
        if (filter != null) {
            if (filter.isName() && ((PdfName) filter).compareTo(PdfName.FLATEDECODE) == 0) {
                return;
            }
            else if (filter.isArray() && ((PdfArray) filter).contains(PdfName.FLATEDECODE)) {
                return;
            }
            else {
                throw new PdfException("Stream could not be compressed: filter is not a name or array.");
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
            dictionary.put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
            if (filter == null) {
                dictionary.put(PdfName.FILTER, PdfName.FLATEDECODE);
            }
            else {
                PdfArray filters = new PdfArray(filter);
                filters.add(PdfName.FLATEDECODE);
                dictionary.put(PdfName.FILTER, filters);
            }
            compressed = true;
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    int getStreamLength(PdfWriter writer) {
        if (dicBytes == null)
            toPdf(writer);
        if (streamBytes != null)
            return streamBytes.size() + dicBytes.length + SIZESTREAM;
        else
            return bytes.length + dicBytes.length + SIZESTREAM;
    }
    
    void writeTo(OutputStream out, PdfWriter writer) throws IOException{
        if (dicBytes == null)
            toPdf(writer);
        out.write(dicBytes);
        out.write(STARTSTREAM);
        PdfEncryption crypto = writer.getEncryption();
        if (crypto == null) {
            if (streamBytes != null)
                streamBytes.writeTo(out);
            else
                out.write(bytes);
        }
        else {
            crypto.prepareKey();
            byte b[];
            if (streamBytes != null) {
                b = streamBytes.toByteArray();
                crypto.encryptRC4(b);
            }
            else {
                b = new byte[bytes.length];
                crypto.encryptRC4(bytes, b);
            }
            out.write(b);
        }
        out.write(ENDSTREAM);
    }
}
