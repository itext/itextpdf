/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * DER UTF8String object.
 */
public class DERUTF8String
    extends DERObject
    implements DERString
{
    String  string;

    /**
     * return an UTF8 string from the passed in object.
     *
     * @exception IllegalArgumentException if the object cannot be converted.
     */
    public static DERUTF8String getInstance(
        Object  obj)
    {
        if (obj == null || obj instanceof DERUTF8String)
        {
            return (DERUTF8String)obj;
        }

        if (obj instanceof ASN1OctetString)
        {
            return new DERUTF8String(((ASN1OctetString)obj).getOctets());
        }

        if (obj instanceof ASN1TaggedObject)
        {
            return getInstance(((ASN1TaggedObject)obj).getObject());
        }

        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    /**
     * return an UTF8 String from a tagged object.
     *
     * @param obj the tagged object holding the object we want
     * @param explicit true if the object is meant to be explicitly
     *              tagged false otherwise.
     * @exception IllegalArgumentException if the tagged object cannot
     *               be converted.
     */
    public static DERUTF8String getInstance(
        ASN1TaggedObject obj,
        boolean          explicit)
    {
        return getInstance(obj.getObject());
    }

    /**
     * basic constructor - byte encoded string.
     */
    DERUTF8String(
        byte[]   string)
    {
        int i = 0;
        int length = 0;

        while (i < string.length)
        {
            length++;
            if ((string[i] & 0xe0) == 0xe0)
            {
                i += 3;
            }
            else if ((string[i] & 0xc0) == 0xc0)
            {
                i += 2;
            }
            else
            {
                i += 1;
            }
        }

        char[]  cs = new char[length];

        i = 0;
        length = 0;

        while (i < string.length)
        {
            char    ch;

            if ((string[i] & 0xe0) == 0xe0)
            {
                ch = (char)(((string[i] & 0x1f) << 12)
                      | ((string[i + 1] & 0x3f) << 6) | (string[i + 2] & 0x3f));
                i += 3;
            }
            else if ((string[i] & 0xc0) == 0xc0)
            {
                ch = (char)(((string[i] & 0x3f) << 6) | (string[i + 1] & 0x3f));
                i += 2;
            }
            else
            {
                ch = (char)(string[i] & 0xff);
                i += 1;
            }

            cs[length++] = ch;
        }

        this.string = new String(cs);
    }

    /**
     * basic constructor
     */
    public DERUTF8String(
        String   string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }

    public int hashCode()
    {
        return this.getString().hashCode();
    }

    public boolean equals(
        Object  o)
    {
        if (!(o instanceof DERUTF8String))
        {
            return false;
        }

        DERUTF8String  s = (DERUTF8String)o;

        return this.getString().equals(s.getString());
    }

    void encode(
        DEROutputStream  out)
        throws IOException
    {
        char[]                  c = string.toCharArray();
        ByteArrayOutputStream   bOut = new ByteArrayOutputStream();

        for (int i = 0; i != c.length; i++)
        {
            char    ch = c[i];

            if (ch < 0x0080)
            {
                bOut.write(ch);
            }
            else if (ch < 0x0800)
            {
                bOut.write(0xc0 | (ch >> 6));
                bOut.write(0x80 | (ch & 0x3f));
            }
            else
            {
                bOut.write(0xe0 | (ch >> 12));
                bOut.write(0x80 | ((ch >> 6) & 0x3F));
                bOut.write(0x80 | (ch & 0x3F));
            }
        }

        out.writeEncoded(UTF8_STRING, bOut.toByteArray());
    }
}
