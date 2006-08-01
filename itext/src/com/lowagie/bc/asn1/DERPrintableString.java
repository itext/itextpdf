/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 * DER PrintableString object.
 */
public class DERPrintableString
    extends DERObject
    implements DERString
{
    String  string;

    /**
     * return a printable string from the passed in object.
     * 
     * @exception IllegalArgumentException if the object cannot be converted.
     */
    public static DERPrintableString getInstance(
        Object  obj)
    {
        if (obj == null || obj instanceof DERPrintableString)
        {
            return (DERPrintableString)obj;
        }

        if (obj instanceof ASN1OctetString)
        {
            return new DERPrintableString(((ASN1OctetString)obj).getOctets());
        }

        if (obj instanceof ASN1TaggedObject)
        {
            return getInstance(((ASN1TaggedObject)obj).getObject());
        }

        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    /**
     * return a Printable String from a tagged object.
     *
     * @param obj the tagged object holding the object we want
     * @param explicit true if the object is meant to be explicitly
     *              tagged false otherwise.
     * @exception IllegalArgumentException if the tagged object cannot
     *               be converted.
     */
    public static DERPrintableString getInstance(
        ASN1TaggedObject obj,
        boolean          explicit)
    {
        return getInstance(obj.getObject());
    }

    /**
     * basic constructor - byte encoded string.
     */
    public DERPrintableString(
        byte[]   string)
    {
        char[]  cs = new char[string.length];

        for (int i = 0; i != cs.length; i++)
        {
            cs[i] = (char)(string[i] & 0xff);
        }

        this.string = new String(cs);
    }

    /**
     * basic constructor
     */
    public DERPrintableString(
        String   string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }

    public byte[] getOctets()
    {
        char[]  cs = string.toCharArray();
        byte[]  bs = new byte[cs.length];

        for (int i = 0; i != cs.length; i++)
        {
            bs[i] = (byte)cs[i];
        }

        return bs; 
    }

    void encode(
        DEROutputStream  out)
        throws IOException
    {
        out.writeEncoded(PRINTABLE_STRING, this.getOctets());
    }

    public int hashCode()
    {
        return this.getString().hashCode();
    }

    public boolean equals(
        Object  o)
    {
        if (!(o instanceof DERPrintableString))
        {
            return false;
        }

        DERPrintableString  s = (DERPrintableString)o;

        return this.getString().equals(s.getString());
    }
}
