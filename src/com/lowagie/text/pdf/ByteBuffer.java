/*
 * ByteBuffer.java
 *
 * Created on March 13, 2001, 5:40 PM
 */

package com.lowagie.text.pdf;

import java.io.UnsupportedEncodingException;
/**
 *
 * @author  psoares
 * @version 
 */
public class ByteBuffer
{
    protected int count;
    
    protected byte buf[];
    
    /** Creates new ByteBuffer */
    public ByteBuffer()
    {
        this(128);
    }

    public ByteBuffer(int size)
    {
        if (size < 1)
            size = 128;
        buf = new byte[size];
    }

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

    public ByteBuffer append(byte b[])
    {
        return append(b, 0, b.length);
    }
    
    public ByteBuffer append(String str)
    {
        return append(str.getBytes());
    }
    
    public ByteBuffer append(int i)
    {
        return append(String.valueOf(i));
    }
    
    public ByteBuffer append(float i)
    {
        return append((double)i);
    }
    
    public ByteBuffer append(double d)
    {
        return append(formatDouble(d));
    }
    
    public static String formatDouble(double d)
    {
        if (Math.abs(d) < 0.000015)
            return "0";
        long v = (long)(d * 100000);
        String total = String.valueOf(v);
        String integ = (total.length() <= 5) ? "0" : total.substring(0, total.length() - 5);
        String fract = ("00000" + total).substring(total.length());
        if (fract.equals("00000"))
            return integ;
        int k;
        for (k = 4; k >= 0; --k)
            if (fract.charAt(k) != '0')
                break;
        return integ + "." + fract.substring(0, k + 1);
    }
    
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
     * @return  the value of the <code>count</code> field, which is the number
     *          of valid bytes in this byte buffer.
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


}
