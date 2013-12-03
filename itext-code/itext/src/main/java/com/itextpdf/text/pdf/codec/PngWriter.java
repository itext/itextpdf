/*
 * $Id: PngWriter.java 6070 2013-11-18 22:54:26Z trumpetinc $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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

package com.itextpdf.text.pdf.codec;

import com.itextpdf.text.DocWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

/**
 * Writes a PNG image.
 *
 * @author  Paulo Soares
 * @since 5.0.3
 */
public class PngWriter {
    private static final byte[] PNG_SIGNTURE = {(byte)137, 80, 78, 71, 13, 10, 26, 10};

    private static final byte[] IHDR = DocWriter.getISOBytes("IHDR");
    private static final byte[] PLTE = DocWriter.getISOBytes("PLTE");
    private static final byte[] IDAT = DocWriter.getISOBytes("IDAT");
    private static final byte[] IEND = DocWriter.getISOBytes("IEND");
    private static final byte[] iCCP = DocWriter.getISOBytes("iCCP");

    private static int[] crc_table;

    private OutputStream outp;

    public PngWriter(OutputStream outp) throws IOException {
        this.outp = outp;
        outp.write(PNG_SIGNTURE);
    }

    public void writeHeader(int width, int height, int bitDepth, int colorType) throws IOException {
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        outputInt(width, ms);
        outputInt(height, ms);
        ms.write(bitDepth);
        ms.write(colorType);
        ms.write(0);
        ms.write(0);
        ms.write(0);
        writeChunk(IHDR, ms.toByteArray());
    }

    public void writeEnd() throws IOException {
        writeChunk(IEND, new byte[0]);
    }

    public void writeData(byte[] data, final int stride) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DeflaterOutputStream zip = new DeflaterOutputStream(stream);
        int k;
        for (k = 0; k < data.length-stride; k += stride) {
            zip.write(0);
            zip.write(data, k, stride);
        }
        int remaining = data.length - k;
        if (remaining > 0){
            zip.write(0);
            zip.write(data, k, remaining);
        }
        zip.close();
        writeChunk(IDAT, stream.toByteArray());
    }

    public void writePalette(byte[] data) throws IOException {
        writeChunk(PLTE, data);
    }

    public void writeIccProfile(byte[] data) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write((byte)'I');
        stream.write((byte)'C');
        stream.write((byte)'C');
        stream.write(0);
        stream.write(0);
        DeflaterOutputStream zip = new DeflaterOutputStream(stream);
        zip.write(data);
        zip.close();
        writeChunk(iCCP, stream.toByteArray());
    }

    private static void make_crc_table() {
        if (crc_table != null)
            return;
        int[] crc2 = new int[256];
        for (int n = 0; n < 256; n++) {
            int c = n;
            for (int k = 0; k < 8; k++) {
                if ((c & 1) != 0)
                    c = 0xedb88320 ^ (c >>> 1);
                else
                    c = c >>> 1;
            }
            crc2[n] = c;
        }
        crc_table = crc2;
    }

    private static int update_crc(int crc, byte[] buf, int offset, int len) {
        int c = crc;

        if (crc_table == null)
            make_crc_table();
        for (int n = 0; n < len; n++) {
            c = crc_table[(c ^ buf[n + offset]) & 0xff] ^ (c >>> 8);
        }
        return c;
    }

    private static int crc(byte[] buf, int offset, int len) {
        return update_crc(0xffffffff, buf, offset, len) ^ 0xffffffff;
    }

    private static int crc(byte[] buf) {
        return update_crc(0xffffffff, buf, 0, buf.length) ^ 0xffffffff;
    }

    public void outputInt(int n) throws IOException {
        outputInt(n, outp);
    }

    public static void outputInt(int n, OutputStream s) throws IOException {
        s.write((byte)(n >> 24));
        s.write((byte)(n >> 16));
        s.write((byte)(n >> 8));
        s.write((byte)n);
    }

    public void writeChunk(byte[] chunkType, byte[] data) throws IOException {
        outputInt(data.length);
        outp.write(chunkType, 0, 4);
        outp.write(data);
        int c = update_crc(0xffffffff, chunkType, 0, chunkType.length);
        c = update_crc(c, data, 0, data.length) ^ 0xffffffff;
        outputInt(c);
    }
}
