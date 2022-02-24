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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import com.itextpdf.text.pdf.qrcode.WriterException;
import com.itextpdf.text.pdf.qrcode.ByteMatrix;
import com.itextpdf.text.pdf.qrcode.QRCodeWriter;
import com.itextpdf.text.pdf.codec.CCITTG4Encoder;
import java.util.Map;

/**
 * A QRCode implementation based on the zxing code.
 * @author Paulo Soares
 * @since 5.0.2
 */
public class BarcodeQRCode {
    ByteMatrix bm;

    /**
     * Creates the QR barcode. The barcode is always created with the smallest possible size and is then stretched
     * to the width and height given. Set the width and height to 1 to get an unscaled barcode.
     * @param content the text to be encoded
     * @param width the barcode width
     * @param height the barcode height
     * @param hints modifiers to change the way the barcode is create. They can be EncodeHintType.ERROR_CORRECTION
     * and EncodeHintType.CHARACTER_SET. For EncodeHintType.ERROR_CORRECTION the values can be ErrorCorrectionLevel.L, M, Q, H.
     * For EncodeHintType.CHARACTER_SET the values are strings and can be Cp437, Shift_JIS and ISO-8859-1 to ISO-8859-16.
     * You can also use UTF-8, but correct behaviour is not guaranteed as Unicode is not supported in QRCodes.
     * The default value is ISO-8859-1.
     * @throws WriterException
     */
    public BarcodeQRCode(String content, int width, int height, Map<EncodeHintType,Object> hints) {
        try {
            QRCodeWriter qc = new QRCodeWriter();
            bm = qc.encode(content, width, height, hints);
        }
        catch (WriterException ex) {
            throw new ExceptionConverter(ex);
        }
    }

    private byte[] getBitMatrix() {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int stride = (width + 7) / 8;
        byte[] b = new byte[stride * height];
        byte[][] mt = bm.getArray();
        for (int y = 0; y < height; ++y) {
            byte[] line = mt[y];
            for (int x = 0; x < width; ++x) {
                if (line[x] != 0) {
                    int offset = stride * y + x / 8;
                    b[offset] |= (byte)(0x80 >> (x % 8));
                }
            }
        }
        return b;
    }

    /** Gets an <CODE>Image</CODE> with the barcode.
     * @return the barcode <CODE>Image</CODE>
     * @throws BadElementException on error
     */
    public Image getImage() throws BadElementException {
        byte[] b = getBitMatrix();
        byte g4[] = CCITTG4Encoder.compress(b, bm.getWidth(), bm.getHeight());
        return Image.getInstance(bm.getWidth(), bm.getHeight(), false, Image.CCITTG4, Image.CCITT_BLACKIS1, g4, null);
    }

    // AWT related methods (remove this if you port to Android / GAE)

    /** Creates a <CODE>java.awt.Image</CODE>.
     * @param foreground the color of the bars
     * @param background the color of the background
     * @return the image
     */
    public java.awt.Image createAwtImage(java.awt.Color foreground, java.awt.Color background) {
        int f = foreground.getRGB();
        int g = background.getRGB();
        java.awt.Canvas canvas = new java.awt.Canvas();

        int width = bm.getWidth();
        int height = bm.getHeight();
        int pix[] = new int[width * height];
        byte[][] mt = bm.getArray();
        for (int y = 0; y < height; ++y) {
            byte[] line = mt[y];
            for (int x = 0; x < width; ++x) {
                pix[y * width + x] = line[x] == 0 ? f : g;
            }
        }

        java.awt.Image img = canvas.createImage(new java.awt.image.MemoryImageSource(width, height, pix, 0, width));
        return img;
    }

    public void placeBarcode(PdfContentByte cb, BaseColor foreground, float moduleSide) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        byte[][] mt = bm.getArray();

        cb.setColorFill(foreground);

        for (int y = 0; y < height; ++y) {
            byte[] line = mt[y];
            for (int x = 0; x < width; ++x) {
                if (line[x] == 0) {
                    cb.rectangle(x * moduleSide, (height - y - 1) * moduleSide, moduleSide, moduleSide);
                }
            }
        }
        cb.fill();
    }

    /** Gets the size of the barcode grid. */
    public Rectangle getBarcodeSize() {
        return new Rectangle(0, 0, bm.getWidth(), bm.getHeight());
    }
}
