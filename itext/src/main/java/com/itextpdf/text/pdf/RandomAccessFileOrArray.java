/*
 * $Id: RandomAccessFileOrArray.java 5914 2013-07-28 14:18:11Z blowagie $
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
package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.io.IndependentRandomAccessSource;
import com.itextpdf.text.io.RandomAccessSource;
import com.itextpdf.text.io.RandomAccessSourceFactory;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
/** Intended to be layered on top of a low level RandomAccessSource object.  Provides
 * functionality useful during parsing:
 * <ul>
 * 	<li>tracks current position in the file</li>
 * 	<li>allows single byte pushback</li>
 * 	<li>allows reading of multi-byte data structures (int, long, String) for both Big and Little Endian representations</li>
 * 	<li>allows creation of independent 'views' of the underlying data source</li>
 * </ul>
 *
 * @author Paulo Soares, Kevin Day
 */
public class RandomAccessFileOrArray implements DataInput {
	
	/**
	 * The source that backs this object
	 */
	private final RandomAccessSource byteSource;
	
	/**
	 * The physical location in the underlying byte source.
	 */
    private long byteSourcePosition;
    
    /**
     * the pushed  back byte, if any
     */
    private byte back;
    /**
     * Whether there is a pushed back byte
     */
    private boolean isBack = false;

    /**
     * @deprecated use {@link RandomAccessFileOrArray#RandomAccessFileOrArray(RandomAccessSource)} instead
     * @param filename
     * @throws IOException
     */
    @Deprecated
    public RandomAccessFileOrArray(String filename) throws IOException {
    	this(new RandomAccessSourceFactory()
		.setForceRead(false)
		.setUsePlainRandomAccess(Document.plainRandomAccess)
		.createBestSource(filename));
    	
    }
    
    /**
     * Creates an independent view of the specified source.  Closing the new object will not close the source.
     * Closing the source will have adverse effect on the behavior of the new view.
     * @deprecated use {@link RandomAccessFileOrArray#createView()} instead
     * @param source the source for the new independent view
     */
    @Deprecated
    public RandomAccessFileOrArray(RandomAccessFileOrArray source) {
    	this(new IndependentRandomAccessSource(source.byteSource));
    }

    /**
     * Creates an independent view of this object (with it's own file pointer and pushback queue).  Closing the new object will not close this object.
     * Closing this object will have adverse effect on the view.
     * @return the new view
     */
    public RandomAccessFileOrArray createView(){
    	return new RandomAccessFileOrArray(new IndependentRandomAccessSource(byteSource));
    }
    
    public RandomAccessSource createSourceView() {
        return new IndependentRandomAccessSource(byteSource);
    }
    
    /**
     * Creates a RandomAccessFileOrArray that wraps the specified byte source.  The byte source will be closed when
     * this RandomAccessFileOrArray is closed.
     * @param byteSource the byte source to wrap
     */
    public RandomAccessFileOrArray(RandomAccessSource byteSource){
    	this.byteSource = byteSource;
    }
    
    /**
     * Constructs a new RandomAccessFileOrArrayObject
     * @param filename the file to open (can be a file system file or one of the following url strings: file://, http://, https://, jar:, wsjar:, vfszip:
     * @param forceRead if true, the entire file will be read into memory
     * @param plainRandomAccess if true, a regular RandomAccessFile is used to access the file contents.  If false, a memory mapped file will be used, unless the file cannot be mapped into memory, in which case regular RandomAccessFile will be used
     * @throws IOException if there is a failure opening or reading the file
     * @deprecated use {@link RandomAccessSourceFactory#createBestSource(String)} and {@link RandomAccessFileOrArray#RandomAccessFileOrArray(RandomAccessSource)} instead
     */
    @Deprecated
    public RandomAccessFileOrArray(String filename, boolean forceRead, boolean plainRandomAccess) throws IOException {
    	this(new RandomAccessSourceFactory()
    		.setForceRead(forceRead)
    		.setUsePlainRandomAccess(plainRandomAccess)
    		.createBestSource(filename));
    }

    /**
     * @param url
     * @throws IOException
     * @deprecated use {@link RandomAccessSourceFactory#createSource(URL)} and {@link RandomAccessFileOrArray#RandomAccessFileOrArray(RandomAccessSource)} instead
     */
    @Deprecated
    public RandomAccessFileOrArray(URL url) throws IOException {
    	this (new RandomAccessSourceFactory().createSource(url));
    }

    /**
     * @param is
     * @throws IOException
     * @deprecated use {@link RandomAccessSourceFactory#createSource(InputStream)} and {@link RandomAccessFileOrArray#RandomAccessFileOrArray(RandomAccessSource)} instead
     */
    @Deprecated
    public RandomAccessFileOrArray(InputStream is) throws IOException {
    	this (new RandomAccessSourceFactory().createSource(is));
    }
    

    /**
     * @param arrayIn byte[]
     * @throws IOException
     * @deprecated use {@link RandomAccessSourceFactory#createSource(byte[])} and {@link RandomAccessFileOrArray#RandomAccessFileOrArray(RandomAccessSource)} instead
     */
   @Deprecated
    public RandomAccessFileOrArray(byte arrayIn[]) {
    	this (new RandomAccessSourceFactory().createSource(arrayIn));
    }
    
    @Deprecated
    //TODO: I'm only putting this in here for backwards compatability with PdfReader(RAFOA, byte[]).  Once we get rid of the
    //PdfReader constructor, we can get rid of this method as well
    protected RandomAccessSource getByteSource(){
    	return byteSource;
    }
    
    /**
     * Pushes a byte back.  The next get() will return this byte instead of the value from the underlying data source
     * @param b the byte to push
     */
    public void pushBack(byte b) {
        back = b;
        isBack = true;
    }
    
    /**
     * Reads a single byte
     * @return the byte, or -1 if EOF is reached
     * @throws IOException
     */
    public int read() throws IOException {
        if(isBack) {
            isBack = false;
            return back & 0xff;
        }
        
        return byteSource.get(byteSourcePosition++);
    }
    
    public int read(byte[] b, int off, int len) throws IOException {
        if (len == 0)
            return 0;
        int count = 0;
        if (isBack && len > 0) {
            isBack = false;
            b[off++] = back;
            --len;
            count++;
        }
        if (len > 0){
        	int byteSourceCount = byteSource.get(byteSourcePosition, b, off, len);
            if (byteSourceCount > 0) {
                count += byteSourceCount;
                byteSourcePosition += byteSourceCount;
            }
        }
        if (count == 0)
            return -1;
        return count;
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
    
    public long skip(long n) throws IOException {
        if (n <= 0) {
            return 0;
        }
        int adj = 0;
        if (isBack) {
            isBack = false;
            if (n == 1) {
                return 1;
            }
            else {
                --n;
                adj = 1;
            }
        }
        long pos;
        long len;
        long newpos;
        
        pos = getFilePointer();
        len = length();
        newpos = pos + n;
        if (newpos > len) {
            newpos = len;
        }
        seek(newpos);
        
        /* return the actual number of bytes skipped */
        return newpos - pos + adj;
    }
    
    public int skipBytes(int n) throws IOException {
        return (int)skip(n);
    }
    
    @Deprecated
    //TODO: remove all references to this call, then remove this method
    public void reOpen() throws IOException {
        seek(0);
    }

    
    public void close() throws IOException {
        isBack = false;
        
        byteSource.close();
    }
    
    public long length() throws IOException {
    	return byteSource.length();
    }
    
    public void seek(long pos) throws IOException {
    	byteSourcePosition = pos;
    	isBack = false;
    }
    
    //TODO: consider changing method name to getPosition or something like that - might not be worth making a breaking change, though
    public long getFilePointer() throws IOException {
    	return byteSourcePosition - (isBack ? 1 : 0);
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
    
    /**
     * Reads a signed 16-bit number from this stream in little-endian order.
     * The method reads two
     * bytes from this stream, starting at the current stream pointer.
     * If the two bytes read, in order, are
     * <code>b1</code> and <code>b2</code>, where each of the two values is
     * between <code>0</code> and <code>255</code>, inclusive, then the
     * result is equal to:
     * <blockquote><pre>
     *     (short)((b2 &lt;&lt; 8) | b1)
     * </pre></blockquote>
     * <p>
     * This method blocks until the two bytes are read, the end of the
     * stream is detected, or an exception is thrown.
     *
     * @return     the next two bytes of this stream, interpreted as a signed
     *             16-bit number.
     * @exception  EOFException  if this stream reaches the end before reading
     *               two bytes.
     * @exception  IOException   if an I/O error occurs.
     */
    public final short readShortLE() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (short)((ch2 << 8) + (ch1 << 0));
    }
    
    public int readUnsignedShort() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch1 << 8) + ch2;
    }
    
    /**
     * Reads an unsigned 16-bit number from this stream in little-endian order.
     * This method reads
     * two bytes from the stream, starting at the current stream pointer.
     * If the bytes read, in order, are
     * <code>b1</code> and <code>b2</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1, b2&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (b2 &lt;&lt; 8) | b1
     * </pre></blockquote>
     * <p>
     * This method blocks until the two bytes are read, the end of the
     * stream is detected, or an exception is thrown.
     *
     * @return     the next two bytes of this stream, interpreted as an
     *             unsigned 16-bit integer.
     * @exception  EOFException  if this stream reaches the end before reading
     *               two bytes.
     * @exception  IOException   if an I/O error occurs.
     */
    public final int readUnsignedShortLE() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch2 << 8) + (ch1 << 0);
    }
    
    public char readChar() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (char)((ch1 << 8) + ch2);
    }
    
    /**
     * Reads a Unicode character from this stream in little-endian order.
     * This method reads two
     * bytes from the stream, starting at the current stream pointer.
     * If the bytes read, in order, are
     * <code>b1</code> and <code>b2</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1,&nbsp;b2&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (char)((b2 &lt;&lt; 8) | b1)
     * </pre></blockquote>
     * <p>
     * This method blocks until the two bytes are read, the end of the
     * stream is detected, or an exception is thrown.
     *
     * @return     the next two bytes of this stream as a Unicode character.
     * @exception  EOFException  if this stream reaches the end before reading
     *               two bytes.
     * @exception  IOException   if an I/O error occurs.
     */
    public final char readCharLE() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (char)((ch2 << 8) + (ch1 << 0));
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
    
    /**
     * Reads a signed 32-bit integer from this stream in little-endian order.
     * This method reads 4
     * bytes from the stream, starting at the current stream pointer.
     * If the bytes read, in order, are <code>b1</code>,
     * <code>b2</code>, <code>b3</code>, and <code>b4</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1, b2, b3, b4&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (b4 &lt;&lt; 24) | (b3 &lt;&lt; 16) + (b2 &lt;&lt; 8) + b1
     * </pre></blockquote>
     * <p>
     * This method blocks until the four bytes are read, the end of the
     * stream is detected, or an exception is thrown.
     *
     * @return     the next four bytes of this stream, interpreted as an
     *             <code>int</code>.
     * @exception  EOFException  if this stream reaches the end before reading
     *               four bytes.
     * @exception  IOException   if an I/O error occurs.
     */
    public final int readIntLE() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }
    
    /**
     * Reads an unsigned 32-bit integer from this stream. This method reads 4
     * bytes from the stream, starting at the current stream pointer.
     * If the bytes read, in order, are <code>b1</code>,
     * <code>b2</code>, <code>b3</code>, and <code>b4</code>, where
     * <code>0&nbsp;&lt;=&nbsp;b1, b2, b3, b4&nbsp;&lt;=&nbsp;255</code>,
     * then the result is equal to:
     * <blockquote><pre>
     *     (b1 &lt;&lt; 24) | (b2 &lt;&lt; 16) + (b3 &lt;&lt; 8) + b4
     * </pre></blockquote>
     * <p>
     * This method blocks until the four bytes are read, the end of the
     * stream is detected, or an exception is thrown.
     *
     * @return     the next four bytes of this stream, interpreted as a
     *             <code>long</code>.
     * @exception  EOFException  if this stream reaches the end before reading
     *               four bytes.
     * @exception  IOException   if an I/O error occurs.
     */
    public final long readUnsignedInt() throws IOException {
        long ch1 = this.read();
        long ch2 = this.read();
        long ch3 = this.read();
        long ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
    
    public final long readUnsignedIntLE() throws IOException {
        long ch1 = this.read();
        long ch2 = this.read();
        long ch3 = this.read();
        long ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch4 << 24) + (ch3 << 16) + (ch2 << 8) + (ch1 << 0));
    }
    
    public long readLong() throws IOException {
        return ((long)(readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
    }
    
    public final long readLongLE() throws IOException {
        int i1 = readIntLE();
        int i2 = readIntLE();
        return ((long)i2 << 32) + (i1 & 0xFFFFFFFFL);
    }
    
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }
    
    public final float readFloatLE() throws IOException {
        return Float.intBitsToFloat(readIntLE());
    }
    
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }
    
    public final double readDoubleLE() throws IOException {
        return Double.longBitsToDouble(readLongLE());
    }
    
    public String readLine() throws IOException {
        StringBuilder input = new StringBuilder();
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
                    long cur = getFilePointer();
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
    
    /** Reads a <CODE>String</CODE> from the font file as bytes using the given
     *  encoding.
     * @param length the length of bytes to read
     * @param encoding the given encoding
     * @return the <CODE>String</CODE> read
     * @throws IOException the font file could not be read
     */
    public String readString(int length, String encoding) throws IOException {
        byte buf[] = new byte[length];
        readFully(buf);
        try {
            return new String(buf, encoding);
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
}
