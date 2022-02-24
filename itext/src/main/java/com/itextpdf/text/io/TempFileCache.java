/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Yulian Gaponenko, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General License for more
 * details. You should have received a copy of the GNU Affero General License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General License.
 *
 * In accordance with Section 7(b) of the GNU Affero General License, a covered
 * work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.text.io;

import com.itextpdf.text.pdf.PdfObject;

import java.io.*;

public class TempFileCache {

    public class ObjectPosition {
        ObjectPosition(long offset, int length) {
            this.offset = offset;
            this.length = length;
        }

        long offset;
        int length;
    }

    private String filename;
    private RandomAccessFile cache;
    private ByteArrayOutputStream baos;

    private byte[] buf;


    public TempFileCache(String filename) throws IOException {
        this.filename = filename;
        File f = new File(filename);
        File parent = f.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }

        cache = new RandomAccessFile(filename, "rw");

        baos = new ByteArrayOutputStream();
    }

    public ObjectPosition put(PdfObject obj) throws IOException {
        baos.reset();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        long offset, size;
        offset = cache.length();

        oos.writeObject(obj);
        cache.seek(offset);
        cache.write(baos.toByteArray());

        size = cache.length() - offset;

        return new ObjectPosition(offset, (int)size);
    }

    public PdfObject get(ObjectPosition pos) throws IOException, ClassNotFoundException {
        PdfObject obj = null;
        if (pos != null) {
            cache.seek(pos.offset);
            cache.read(getBuffer(pos.length), 0, pos.length);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(getBuffer(pos.length)));
            try {
                obj = (PdfObject) ois.readObject();
            } finally {
                ois.close();
            }
        }

        return obj;
    }

    private byte[] getBuffer(int size) {
        if (buf == null || buf.length < size) {
            buf = new byte[size];
        }

        return buf;
    }

    public void close() throws IOException {
        cache.close();
        cache = null;

        new File(filename).delete();
    }


}
