/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;

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
            Deflater deflater = null;
            streamBytes = new ByteArrayOutputStream();
            if (Document.compress)
            {
                compressed = true;
                if (text != null)
                    compressionLevel = text.getPdfWriter().getCompressionLevel();
                else if (content != null)
                    compressionLevel = content.getPdfWriter().getCompressionLevel();
                deflater = new Deflater(compressionLevel);
                out = new DeflaterOutputStream(streamBytes, deflater);
            }
            else
                out = streamBytes;
            int rotation = page.getRotation();
            switch (rotation) {
                case 90:
                    out.write(ROTATE90);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getTop())));
                    out.write(' ');
                    out.write('0');
                    out.write(ROTATEFINAL);
                    break;
                case 180:
                    out.write(ROTATE180);
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getRight())));
                    out.write(' ');
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getTop())));
                    out.write(ROTATEFINAL);
                    break;
                case 270:
                    out.write(ROTATE270);
                    out.write('0');
                    out.write(' ');
                    out.write(DocWriter.getISOBytes(ByteBuffer.formatDouble(page.getRight())));
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
            if (text != null) {
                out.write(SAVESTATE);
                text.getInternalBuffer().writeTo(out);
                out.write(RESTORESTATE);
            }
            if (secondContent.size() > 0) {
                secondContent.getInternalBuffer().writeTo(out);
            }
            out.close();
            if (deflater != null) {
                deflater.end();
            }
        }
        catch (Exception e) {
            throw new BadPdfFormatException(e.getMessage());
        }
        put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
        if (compressed)
            put(PdfName.FILTER, PdfName.FLATEDECODE);
    }
}
