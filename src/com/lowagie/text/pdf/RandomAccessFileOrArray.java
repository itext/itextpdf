/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 Paulo Soares
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

import java.io.DataInputStream;
import java.io.DataInput;
import java.io.IOException;
import java.io.EOFException;
import java.io.RandomAccessFile;
import java.io.File;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
/** An implementation of a RandomAccessFile for input only
 * that accepts a file or a byte array as data source.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
public class RandomAccessFileOrArray implements DataInput {

    RandomAccessFile rf;
    String filename;
    byte arrayIn[];
    int arrayInPtr;

    public RandomAccessFileOrArray(String filename) throws IOException {
        File file = new File(filename);
        if (!file.canRead()) {
            InputStream is = BaseFont.getResourceStream(filename);
            if (is == null)
                throw new IOException(filename + " not found as file or resource.");
            try {
                byte b[] = new byte[4096];
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                for (;;) {
                    int read = is.read(b);
                    if (read < 1)
                        break;
                    out.write(b, 0, read);
                }
                this.arrayIn = out.toByteArray();
                return;
            }
            finally {
                try {
                    is.close();
                }
                catch (IOException ioe) {
                    // empty on purpose
                }
            }
        }
        this.filename = filename;
        rf = new RandomAccessFile(filename, "r");
    }
    
    public RandomAccessFileOrArray(byte arrayIn[]) {
        this.arrayIn = arrayIn;
    }

    public RandomAccessFileOrArray(RandomAccessFileOrArray file) {
        filename = file.filename;
        arrayIn = file.arrayIn;
    }
    
    public int read() throws IOException {
        if (arrayIn == null)
            return rf.read();
        else {
            if (arrayInPtr >= arrayIn.length)
                return -1;
            return arrayIn[arrayInPtr++] & 0xff;
        }
    }

    public int read(byte[] b, int off, int len) throws IOException {
        if (arrayIn == null)
            return rf.read(b, off, len);
        else {
            if (len == 0)
                return 0;
            if (arrayInPtr >= arrayIn.length)
                return -1;
            if (arrayInPtr + len > arrayIn.length)
                len = arrayIn.length - arrayInPtr;
            System.arraycopy(arrayIn, arrayInPtr, b, off, len);
            arrayInPtr += len;
            return len;
        }
    }

    public int read(byte b[]) throws IOException {
        return read(b, 0, b.length);
    }
    
    public void readFully(byte b[]) throws IOException {
        readFully(b, 0, b.length);
    }
    
    public void readFully(byte b[], int off, int len) throws IOException {
        int n = 0;
        do {
            int count = read(b, off + n, len - n);
            if (count < 0)
                throw new EOFException();
            n += count;
        } while (n < len);
    }
    
    public int skipBytes(int n) throws IOException {
        int pos;
        int len;
        int newpos;
        
        if (n <= 0) {
            return 0;
        }
        pos = getFilePointer();
        len = length();
        newpos = pos + n;
        if (newpos > len) {
            newpos = len;
        }
        seek(newpos);
        
    /* return the actual number of bytes skipped */
        return newpos - pos;
    }
    
    public void reOpen() throws IOException {
        if (filename != null) {
            close();
            rf = new RandomAccessFile(filename, "r");
        }
        else {
            arrayInPtr = 0;
        }
    }
    
    protected void insureOpen() throws IOException {
        if (filename != null && rf == null) {
            reOpen();
        }
    }

    public void close() throws IOException {
        if (rf != null) {
            rf.close();
            rf = null;
        }
    }
    
    public int length() throws IOException {
        if (arrayIn == null)
            return (int)rf.length();
        else
            return arrayIn.length;
    }
    
    public void seek(int pos) throws IOException {
        if (arrayIn == null) {
            insureOpen();
            rf.seek(pos);
        }
        else
            arrayInPtr = pos;
    }
    
    public int getFilePointer() throws IOException {
        if (arrayIn == null)
            return (int)rf.getFilePointer();
        else
            return arrayInPtr;
    }
    
    public boolean readBoolean() throws IOException {
        int ch = this.read();
        if (ch < 0)
            throw new EOFException();
        return (ch != 0);
    }
    
    public byte readByte() throws IOException {
        int ch = this.read();
        if (ch < 0)
            throw new EOFException();
        return (byte)(ch);
    }
    
    public int readUnsignedByte() throws IOException {
        int ch = this.read();
        if (ch < 0)
            throw new EOFException();
        return ch;
    }
    
    public short readShort() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (short)((ch1 << 8) + ch2);
    }
    
    public int readUnsignedShort() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch1 << 8) + ch2;
    }
    
    public char readChar() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (char)((ch1 << 8) + ch2);
    }
    
    public int readInt() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4);
    }
    
    public long readLong() throws IOException {
        return ((long)(readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
    }
    
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }
    
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }
    
    public String readLine() throws IOException {
        StringBuffer input = new StringBuffer();
        int c = -1;
        boolean eol = false;
        
        while (!eol) {
            switch (c = read()) {
                case -1:
                case '\n':
                    eol = true;
                    break;
                case '\r':
                    eol = true;
                    int cur = getFilePointer();
                    if ((read()) != '\n') {
                        seek(cur);
                    }
                    break;
                default:
                    input.append((char)c);
                    break;
            }
        }
        
        if ((c == -1) && (input.length() == 0)) {
            return null;
        }
        return input.toString();
    }
    
    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }
}
