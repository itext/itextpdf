/*
 * $Id$
 * $Name$
 *
 * Copyright 2000, 2001 by Paulo Soares.
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
import java.io.UnsupportedEncodingException;
import java.io.OutputStream;
import java.io.IOException;
import com.lowagie.text.DocWriter;

/**
 * Acts like a <CODE>StringBuffer</CODE> but works with <CODE>byte</CODE> arrays.
 * Floating point is converted to a format suitable to the PDF.
 * @author Paulo Soares (psoares@consiste.pt)
 */

public class ByteBuffer
{
/** The count of bytes in the buffer. */
    protected int count;
    
/** The buffer where the bytes are stored. */
    protected byte buf[];
    
/** Creates new ByteBuffer with capacity 128 */
    public ByteBuffer()
    {
        this(128);
    }
    
/**
 * Creates a byte buffer with a certain capacity.
 * @param size the initial capacity
 */
    public ByteBuffer(int size)
    {
        if (size < 1)
            size = 128;
        buf = new byte[size];
    }
    
/**
 * Appends an <CODE>int</CODE>. The size of the array will grow by one.
 * @param b the int to be appended
 * @return a reference to this <CODE>ByteBuffer</CODE> object
 */
    public ByteBuffer append_i(int b)
    {
        int newcount = count + 1;
        if (newcount > buf.length) {
            byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
            System.arraycopy(buf, 0, newbuf, 0, count);
            buf = newbuf;
        }
        buf[count] = (byte)b;
        count = newcount;
        return this;
    }
    
/**
 * Appends the subarray of the <CODE>byte</CODE> array. The buffer will grow by
 * <CODE>len</CODE> bytes.
 * @param b the array to be appended
 * @param off the offset to the start of the array
 * @param len the length of bytes to append
 * @return a reference to this <CODE>ByteBuffer</CODE> object
 */
    public ByteBuffer append(byte b[], int off, int len)
    {
        if ((off < 0) || (off > b.length) || (len < 0) ||
        ((off + len) > b.length) || ((off + len) < 0) || len == 0)
            return this;
        int newcount = count + len;
        if (newcount > buf.length) {
            byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
            System.arraycopy(buf, 0, newbuf, 0, count);
            buf = newbuf;
        }
        System.arraycopy(b, off, buf, count, len);
        count = newcount;
        return this;
    }
    
/**
 * Appends an array of bytes.
 * @param b the array to be appended
 * @return a reference to this <CODE>ByteBuffer</CODE> object
 */
    public ByteBuffer append(byte b[])
    {
        return append(b, 0, b.length);
    }
    
/**
 * Appends a <CODE>String</CODE> to the buffer. The <CODE>String</CODE> is
 * converted according to the encoding ISO-8859-1.
 * @param str the <CODE>String</CODE> to be appended
 * @return a reference to this <CODE>ByteBuffer</CODE> object
 */
    public ByteBuffer append(String str)
    {
        return append(DocWriter.getISOBytes(str));
    }
    
/**
 * Appends a <CODE>char</CODE> to the buffer. The <CODE>char</CODE> is
 * converted according to the encoding ISO-8859-1.
 * @param c the <CODE>char</CODE> to be appended
 * @return a reference to this <CODE>ByteBuffer</CODE> object
 */
    public ByteBuffer append(char c)
    {
        if (c == ' ') // common case
            return append_i(32);
        return append(DocWriter.getISOBytes(String.valueOf(c)));
    }
    
/**
 * Appends another <CODE>ByteBuffer</CODE> to this buffer.
 * @param buf the <CODE>ByteBuffer</CODE> to be appended
 * @return a reference to this <CODE>ByteBuffer</CODE> object
 */
    public ByteBuffer append(ByteBuffer buf)
    {
        return append(buf.buf, 0, buf.count);
    }
    
/**
 * Appends the string representation of an <CODE>int</CODE>.
 * @param i the <CODE>int</CODE> to be appended
 * @return a reference to this <CODE>ByteBuffer</CODE> object
 */
    public ByteBuffer append(int i)
    {
        return append(String.valueOf(i));
    }
    
/**
 * Appends a string representation of a <CODE>float</CODE> according
 * to the Pdf conventions.
 * @param i the <CODE>float</CODE> to be appended
 * @return a reference to this <CODE>ByteBuffer</CODE> object
 */
    public ByteBuffer append(float i)
    {
        return append((double)i);
    }
    
/**
 * Appends a string representation of a <CODE>double</CODE> according
 * to the Pdf conventions.
 * @param d the <CODE>double</CODE> to be appended
 * @return a reference to this <CODE>ByteBuffer</CODE> object
 */
    public ByteBuffer append(double d)
    {
        return append(formatDouble(d));
    }
    
/**
 * Outputs a <CODE>double</CODE> into a format suitable for the PDF.
 * @param d a double
 * @return the <CODE>String</CODE> representation of the <CODE>double</CODE>
 */
    public static String formatDouble(double d)
    {
        if (Math.abs(d) < 0.000015)
            return "0";
        String sign = "";
        if (d < 0) {
            sign = "-";
            d = -d;
        }
        if (d < 1.0) {
            d += 0.000005;
            long v = (long)(d * 100000);
            String total = String.valueOf(v);
            String integ = (total.length() <= 5) ? "0" : total.substring(0, total.length() - 5);
            String fract = ("00000" + total).substring(total.length());
            if (fract.equals("00000"))
                return sign + integ;
            int k;
            for (k = 4; k >= 0; --k)
                if (fract.charAt(k) != '0')
                    break;
            return sign + integ + "." + fract.substring(0, k + 1);
        }
        else if (d <= 32767) {
            d += 0.005;
            long v = (long)(d * 100);
            String total = String.valueOf(v);
            String integ = (total.length() <= 2) ? "0" : total.substring(0, total.length() - 2);
            String fract = ("00" + total).substring(total.length());
            if (fract.equals("00"))
                return sign + integ;
            int k;
            for (k = 1; k >= 0; --k)
                if (fract.charAt(k) != '0')
                    break;
            return sign + integ + "." + fract.substring(0, k + 1);
        }
        else {
            d += 0.5;
            long v = (long)d;
            return sign + String.valueOf(v);
        }
    }
    
/**
 * Sets the size to zero.
 */
    public void reset()
    {
        count = 0;
    }
    
/**
 * Creates a newly allocated byte array. Its size is the current
 * size of this output stream and the valid contents of the buffer
 * have been copied into it.
 *
 * @return  the current contents of this output stream, as a byte array.
 */
    public byte[] toByteArray()
    {
        byte newbuf[] = new byte[count];
        System.arraycopy(buf, 0, newbuf, 0, count);
        return newbuf;
    }
    
/**
 * Returns the current size of the buffer.
 *
 * @return the value of the <code>count</code> field, which is the number of valid bytes in this byte buffer.
 */
    public int size()
    {
        return count;
    }
    
/**
 * Converts the buffer's contents into a string, translating bytes into
 * characters according to the platform's default character encoding.
 *
 * @return String translated from the buffer's contents.
 */
    public String toString()
    {
        return new String(buf, 0, count);
    }
    
/**
 * Converts the buffer's contents into a string, translating bytes into
 * characters according to the specified character encoding.
 *
 * @param   enc  a character-encoding name.
 * @return String translated from the buffer's contents.
 * @throws UnsupportedEncodingException
 *         If the named encoding is not supported.
 */
    public String toString(String enc) throws UnsupportedEncodingException
    {
        return new String(buf, 0, count, enc);
    }

    /**
     * Writes the complete contents of this byte buffer output to
     * the specified output stream argument, as if by calling the output
     * stream's write method using <code>out.write(buf, 0, count)</code>.
     *
     * @param      out   the output stream to which to write the data.
     * @exception  IOException  if an I/O error occurs.
     */
    public void writeTo(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }

}