package com.lowagie.bc.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class ASN1Encodable
    implements DEREncodable
{
	public byte[] getEncoded() 
		throws IOException
	{
		ByteArrayOutputStream	bOut = new ByteArrayOutputStream();
		ASN1OutputStream		aOut = new ASN1OutputStream(bOut);
		
		aOut.writeObject(this);
		
		return bOut.toByteArray();
	}
	
    public int hashCode()
    {
        return this.getDERObject().hashCode();
    }

    public boolean equals(
        Object  o)
    {
        if ((o == null) || !(o instanceof DEREncodable))
        {
            return false;
        }

        DEREncodable other = (DEREncodable)o;

        return this.getDERObject().equals(other.getDERObject());
    }

    public DERObject getDERObject()
    {
        return this.toASN1Object();
    }

    public abstract DERObject toASN1Object();
}
