/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.IOException;
import java.util.Enumeration;

public class BERSet
    extends DERSet
{
    /**
     * create an empty sequence
     */
    public BERSet()
    {
    }

    /**
     * create a set containing one object
     */
    public BERSet(
        DEREncodable    obj)
    {
        super(obj);
    }

    /**
     * create a set containing a vector of objects.
     */
    public BERSet(
        DEREncodableVector   v)
    {
        super(v);
    }

    /*
     */
    void encode(
        DEROutputStream out)
        throws IOException
    {
        if (out instanceof ASN1OutputStream || out instanceof BEROutputStream)
        {
            out.write(SET | CONSTRUCTED);
            out.write(0x80);
            
            Enumeration e = getObjects();
            while (e.hasMoreElements())
            {
                out.writeObject(e.nextElement());
            }
        
            out.write(0x00);
            out.write(0x00);
        }
        else
        {
            super.encode(out);
        }
    }
}
