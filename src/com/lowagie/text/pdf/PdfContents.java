/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text.pdf;

import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

/**
 * <CODE>PdfContents</CODE> is a <CODE>PdfStream</CODE> containing the contents (text + graphics) of a <CODE>PdfPage</CODE>.
 */

class PdfContents extends PdfStream {
    
    static final byte SAVESTATE[] = DocWriter.getISOBytes("q\n");
    static final byte RESTORESTATE[] = DocWriter.getISOBytes("Q\n");
    static final byte ROTATE90[] = DocWriter.getISOBytes("0 1 -1 0 ");
    static final byte ROTATE180[] = DocWriter.getISOBytes("-1 0 0 -1 ");
    static final byte ROTATE270[] = DocWriter.getISOBytes("0 -1 1 0 ");
    static final byte ROTATEFINAL[] = DocWriter.getISOBytes(" cm\n");
    // constructor
    
/**
 * Constructs a <CODE>PdfContents</CODE>-object, containing text and general graphics.
 *
 * @param under the direct content that is under all others
 * @param content the graphics in a page
 * @param text the text in a page
 * @param secondContent the direct content that is over all others
 * @throws BadPdfFormatException on error
 */
    
    PdfContents(PdfContentByte under, PdfContentByte content, PdfContentByte text, PdfContentByte secondContent, Rectangle page) throws BadPdfFormatException {
        super();
        try {
            OutputStream out = null;
            streamBytes = new ByteArrayOutputStream();
            if (Document.compress)
            {
                compressed = true;
                out = new DeflaterOutputStream(streamBytes);
            }
            else
                out = streamBytes;
            int rotation = page.getRotation();
            switch (rotation) {
                case 90:
                    out.write(ROTATE90);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.top())));
                    out.write(' ');
                    out.write('0');
                    out.write(ROTATEFINAL);
                    break;
                case 180:
                    out.write(ROTATE180);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.right())));
                    out.write(' ');
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.top())));
                    out.write(ROTATEFINAL);
                    break;
                case 270:
                    out.write(ROTATE270);
                    out.write('0');
                    out.write(' ');
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.right())));
                    out.write(ROTATEFINAL);
                    break;
            }
            if (under.size() > 0) {
                out.write(SAVESTATE);
                under.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (content.size() > 0) {
                out.write(SAVESTATE);
                content.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            out.write(SAVESTATE);
            text.getInternalBuffer().writeTo(out);
            out.write(RESTORESTATE);
            if (secondContent.size() > 0) {
                secondContent.getInternalBuffer().writeTo(out);
            }
            out.close();
        }
        catch (Exception e) {
            throw new BadPdfFormatException(e.getMessage());
        }
        dictionary.put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
        if (compressed)
            dictionary.put(PdfName.FILTER, PdfName.FLATEDECODE);
    }
}