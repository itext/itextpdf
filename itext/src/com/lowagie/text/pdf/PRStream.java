/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Paulo Soares.
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
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
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

import java.io.*;
import com.lowagie.text.ExceptionConverter;
import java.util.zip.DeflaterOutputStream;
import com.lowagie.text.Document;
/**
 * a Literal
 */

class PRStream extends PdfStream {
    
    protected PRDictionary dictionary;
    protected PdfReader reader;
    protected int offset;
    protected int length;
    
    PRStream(PRDictionary dictionary, PdfReader reader, int offset)
    {
        this.dictionary = dictionary;
        this.reader = reader;
        this.offset = offset;
    }
    
    PRStream(PRDictionary dictionary, PdfReader reader, byte conts[])
    {
        this.dictionary = dictionary;
        this.reader = reader;
        this.offset = -1;
        if (Document.compress) {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                DeflaterOutputStream zip = new DeflaterOutputStream(stream);
                zip.write(conts);
                zip.close();
                bytes = stream.toByteArray();
            }
            catch(IOException ioe) {
                throw new ExceptionConverter(ioe);
            }
            dictionary.put(new PRName("Filter"), new PRName("FlateDecode"));
        }
        else
            bytes = conts;
        setLength(bytes.length);
    }
    
    void setLength(int length) {
        this.length = length;
        dictionary.put(new PRName("Length"), new PRNumber(length));
    }
    
    PRDictionary getDictionary() {
        return dictionary;
    }
    
    void setDictionary(PRDictionary dic) {
        dictionary = dic;
    }
    
    int getOffset() {
        return offset;
    }
    
    int getLength() {
        return length;
    }
    
    public byte[] toPdf(PdfWriter writer) {
        dicBytes = dictionary.toPdf(writer);
        return null;
    }

    int getStreamLength(PdfWriter writer) {
        if (dicBytes == null)
            toPdf(writer);
        return length + dicBytes.length + SIZESTREAM;
    }
    
    void writeTo(OutputStream out, PdfWriter writer) throws IOException{
        if (dicBytes == null)
            toPdf(writer);
        out.write(dicBytes);
        out.write(STARTSTREAM);
        if (length > 0) {
            PdfEncryption crypto = writer.getEncryption();
            if (offset < 0) {
                if (crypto == null)
                    out.write(bytes);
                else {
                    crypto.prepareKey();
                    byte buf[] = new byte[length];
                    System.arraycopy(bytes, 0, buf, 0, length);
                    crypto.encryptRC4(buf);
                    out.write(buf);
                }
            }
            else {
                byte buf[] = new byte[Math.min(length, 4092)];
                RandomAccessFileOrArray file = writer.getReaderFile(reader);
                file.seek(offset);
                int size = length;
                if (crypto != null)
                    crypto.prepareKey();
                while (size > 0) {
                    int r = file.read(buf, 0, Math.min(size, buf.length));
                    size -= r;
                    if (crypto != null)
                        crypto.encryptRC4(buf, 0, r);
                    out.write(buf, 0, r);
                }
            }
        }
        out.write(ENDSTREAM);
    }
}