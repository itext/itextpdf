/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class ASN1OctetString
    extends DERObject
{
    byte[]  string;

    /**
     * return an Octet String from a tagged object.
     *
     * @param obj the tagged object holding the object we want.
     * @param explicit true if the object is meant to be explicitly
     *              tagged false otherwise.
     * @exception IllegalArgumentException if the tagged object cannot
     *              be converted.
     */
    public static ASN1OctetString getInstance(
        ASN1TaggedObject    obj,
        boolean             explicit)
    {
        return getInstance(obj.getObject());
    }
	
    /**
     * return an Octet String from the given object.
     *
     * @param obj the object we want converted.
     * @exception IllegalArgumentException if the object cannot be converted.
     */
    public static ASN1OctetString getInstance(
        Object  obj)
    {
        if (obj == null || obj instanceof ASN1OctetString)
        {
            return (ASN1OctetString)obj;
        }

        if (obj instanceof ASN1TaggedObject)
        {
            return getInstance(((ASN1TaggedObject)obj).getObject());
        }

        if (obj instanceof ASN1Sequence)
        {
            Vector      v = new Vector();
            Enumeration e = ((ASN1Sequence)obj).getObjects();

            while (e.hasMoreElements())
            {
                v.addElement(e.nextElement());
            }

            return new BERConstructedOctetString(v);
        }

        throw new IllegalArgumentException("illegal object in getInstance: " + obj.getClass().getName());
    }

    /**
     * @param string the octets making up the octet string.
     */
    public ASN1OctetString(
        byte[]  string)
    {
        this.string = string;
    }

    public ASN1OctetString(
        DEREncodable obj)
    {
        try
        {
            ByteArrayOutputStream   bOut = new ByteArrayOutputStream();
            DEROutputStream         dOut = new DEROutputStream(bOut);

            dOut.writeObject(obj);
            dOut.close();

            this.string = bOut.toByteArray();
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Error processing object : " + e.toString());
        }
    }

    public byte[] getOctets()
    {
        return string;
    }

    public int hashCode()
    {
        byte[]  b = this.getOctets();
        int     value = 0;

        for (int i = 0; i != b.length; i++)
        {
            value ^= (b[i] & 0xff) << (i % 4);
        }

        return value;
    }

    public boolean equals(
        Object  o)
    {
        if (!(o instanceof DEROctetString))
        {
            return false;
        }

        DEROctetString  other = (DEROctetString)o;

        byte[] b1 = other.getOctets();
        byte[] b2 = this.getOctets();

        if (b1.length != b2.length)
        {
            return false;
        }

        for (int i = 0; i != b1.length; i++)
        {
            if (b1[i] != b2[i])
            {
                return false;
            }
        }

        return true;
    }

    abstract void encode(DEROutputStream out)
        throws IOException;
}
