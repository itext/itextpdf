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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import com.lowagie.text.DocWriter;

/**
 * <CODE>PdfIndirectObject</CODE> is the Pdf indirect object.
 * <P>
 * An <I>indirect object</I> is an object that has been labeled so that it can be referenced by
 * other objects. Any type of <CODE>PdfObject</CODE> may be labeled as an indirect object.<BR>
 * An indirect object consists of an object identifier, a direct object, and the <B>endobj</B>
 * keyword. The <I>object identifier</I> consists of an integer <I>object number</I>, an integer
 * <I>generation number</I>, and the <B>obj</B> keyword.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.10 (page 53).
 *
 * @see		PdfObject
 * @see		PdfIndirectReference
 */

class PdfIndirectObject {
    
    // membervariables
    
/** The object number */
    protected int number;
    
/** the generation number */
    protected int generation = 0;
    
/** The object type */
    protected int type;
    
/** The object ready to stream out */
    protected ByteArrayOutputStream bytes;
    
    static final byte STARTOBJ[] = DocWriter.getISOBytes(" obj\n");
    static final byte ENDOBJ[] = DocWriter.getISOBytes("\nendobj\n");
    static final int SIZEOBJ = STARTOBJ.length + ENDOBJ.length;
    boolean isStream = false;
    PdfStream stream;
    PdfWriter writer;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfIndirectObject</CODE>.
 *
 * @param		number			the object number
 * @param		object			the direct object
 */
    
    PdfIndirectObject(int number, PdfObject object, PdfWriter writer) {
        this(number, 0, object, writer);
    }
    
/**
 * Constructs a <CODE>PdfIndirectObject</CODE>.
 *
 * @param		number			the object number
 * @param		generation		the generation number
 * @param		object			the direct object
 */
    
    PdfIndirectObject(int number, int generation, PdfObject object, PdfWriter writer) {
        this.writer = writer;
        this.number = number;
        this.generation = generation;
        type = object.type();
        isStream = (object.type() == object.STREAM);
        PdfEncryption crypto = writer.getEncryption();
        if (crypto != null) {
            crypto.setHashKey(number, generation);
        }
        try {
            bytes = new ByteArrayOutputStream();
            bytes.write(DocWriter.getISOBytes(String.valueOf(number)));
            bytes.write(32);
            bytes.write(DocWriter.getISOBytes(String.valueOf(generation)));
            if (!isStream) {
                bytes.write(STARTOBJ);
                bytes.write(object.toPdf(writer));
                bytes.write(ENDOBJ);
            }
            else
                stream = (PdfStream)object;
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe.getMessage());
        }
    }
    
    // methods
    
/**
 * Return the length of this <CODE>PdfIndirectObject</CODE>.
 *
 * @return		the length of the PDF-representation of this indirect object.
 */
    
    public final int length() {
        if (isStream)
            return bytes.size() + SIZEOBJ + stream.getStreamLength(writer);
        else
            return bytes.size();
    }
    
    
/**
 * Returns a <CODE>PdfIndirectReference</CODE> to this <CODE>PdfIndirectObject</CODE>.
 *
 * @return		a <CODE>PdfIndirectReference</CODE>
 */
    
    final PdfIndirectReference getIndirectReference() {
        return new PdfIndirectReference(type, number, generation);
    }
    
/**
 * Writes eficiently to a stream
 *
 * @param out the stream to write to
 * @throws IOException on write error
 */
    final void writeTo(OutputStream out) throws IOException
    {
        bytes.writeTo(out);
        if (isStream) {
            out.write(STARTOBJ);
            stream.writeTo(out, writer);
            out.write(ENDOBJ);
        }
    }
}
