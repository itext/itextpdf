/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
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
package com.itextpdf.text.io;

import com.itextpdf.text.pdf.PdfArray;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * An InputStream that allows you to read that part of a PDF
 * document that needs to be hashed.
 * @author Paulo Soares
 */
public class RangeStream extends InputStream {
    private byte b[] = new byte[1];
    private RandomAccessSource raf;
    private long range[];
    private long rangePosition = 0;

    public RangeStream(RandomAccessSource raf, long range[], PdfArray byteRange) {
        this.raf = raf; 
        if (range != null)
            this.range = range;
        else {
            this.range = new long[byteRange.size()];
            for (int k = 0; k < this.range.length; ++k) {
                this.range[k] = byteRange.getAsNumber(k).longValue();
            }
        }
    }

    /**
     * @see java.io.InputStream#read()
     */
    @Override
    public int read() throws IOException {
        int n = read(b);
        if (n != 1)
            return -1;
        return b[0] & 0xff;
    }

    /**
     * @see java.io.InputStream#read(byte[], int, int)
     */
    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || off > b.length || len < 0 ||
        off + len > b.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        if (rangePosition >= range[range.length - 2] + range[range.length - 1]) {
            return -1;
        }
        for (int k = 0; k < range.length; k += 2) {
            long start = range[k];
            long end = start + range[k + 1];
            if (rangePosition < start)
                rangePosition = start;
            if (rangePosition >= start && rangePosition < end) {
                int lenf = (int)Math.min((long)len, end - rangePosition);
                lenf = raf.get(rangePosition, b, off, lenf);
                if (lenf <= 0)
                    throw new EOFException();
                rangePosition += lenf;
                return lenf;
            }
        }
        return -1;
    }
}    
