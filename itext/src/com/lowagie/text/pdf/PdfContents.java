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

/**
 * <CODE>PdfContents</CODE> is a <CODE>PdfStream</CODE> containing the contents (text + graphics) of a <CODE>PdfPage</CODE>.
 */

class PdfContents extends PdfStream {
    
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
    
    PdfContents(PdfContentByte under, PdfContentByte content, PdfContentByte text, PdfContentByte secondContent) throws BadPdfFormatException {
        super(new PdfDictionary(), " ");
        ByteBuffer buf = new ByteBuffer();
        if (under.size() > 0) {
            buf.append("q\n");
            buf.append(under.getInternalBuffer());
            buf.append("Q\n");
        }
        if (content.size() > 0) {
            buf.append("q\n");
            buf.append(content.getInternalBuffer());
            buf.append("Q\n");
        }
        buf.append("q\n");
        buf.append(text.getInternalBuffer());
        buf.append("Q\n");
        if (secondContent.size() > 0) {
            buf.append(secondContent.getInternalBuffer());
        }
        bytes = buf.toByteArray();
        dictionary.put(PdfName.LENGTH, new PdfNumber(bytes.length));
        try {
            flateCompress();
        }
        catch(PdfException pe) {
        }
    }
}