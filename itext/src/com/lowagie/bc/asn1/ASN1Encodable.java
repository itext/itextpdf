/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Abstract Syntax Notation One (ASN.1) is a formal language for abstractly describing messages to be exchanged between distributed computer systems.
 */
public abstract class ASN1Encodable
    implements DEREncodable
{
	/**
	 * Encodes the ASN1Encodable object.
	 * @return an encoded bytearray
	 * @throws IOException
	 */
	public byte[] getEncoded() 
		throws IOException
	{
		ByteArrayOutputStream	bOut = new ByteArrayOutputStream();
		ASN1OutputStream		aOut = new ASN1OutputStream(bOut);
		
		aOut.writeObject(this);
		
		return bOut.toByteArray();
	}
	
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return this.getDERObject().hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    /**
     * @see com.lowagie.bc.asn1.DEREncodable#getDERObject()
     */
    public DERObject getDERObject()
    {
        return this.toASN1Object();
    }

    /**
     * Abstract method that returns the object as an ASN1 object.
     * @return an encodable object
     */
    public abstract DERObject toASN1Object();
}
