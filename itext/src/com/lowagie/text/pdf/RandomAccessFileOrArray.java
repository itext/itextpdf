package com.lowagie.text.pdf;

import java.io.DataInputStream;
import java.io.DataInput;
import java.io.IOException;
import java.io.EOFException;
import java.io.RandomAccessFile;
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
    
    public final void readFully(byte b[]) throws IOException {
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
    
    void reOpen() throws IOException {
        if (filename != null) {
            close();
            rf = new RandomAccessFile(filename, "r");
        }
        else {
            arrayInPtr = 0;
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
        if (arrayIn == null)
            rf.seek(pos);
        else
            arrayInPtr = pos;
    }
    
    public int getFilePointer() throws IOException {
        if (arrayIn == null)
            return (int)rf.getFilePointer();
        else
            return arrayInPtr;
    }
    
    public final boolean readBoolean() throws IOException {
        int ch = this.read();
        if (ch < 0)
            throw new EOFException();
        return (ch != 0);
    }
    
    public final byte readByte() throws IOException {
        int ch = this.read();
        if (ch < 0)
            throw new EOFException();
        return (byte)(ch);
    }
    
    public final int readUnsignedByte() throws IOException {
        int ch = this.read();
        if (ch < 0)
            throw new EOFException();
        return ch;
    }
    
    public final short readShort() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (short)((ch1 << 8) + ch2);
    }
    
    public final int readUnsignedShort() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch1 << 8) + ch2;
    }
    
    public final char readChar() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (char)((ch1 << 8) + ch2);
    }
    
    public final int readInt() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4);
    }
    
    public final long readLong() throws IOException {
        return ((long)(readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
    }
    
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }
    
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }
    
    public final String readLine() throws IOException {
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
    
    public final String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }
}
