/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 * A NULL object.
 */
public class DERNull
    extends ASN1Null
{
    byte[]  zeroBytes = new byte[0];

    public DERNull()
    {
    }

    void encode(
        DEROutputStream  out)
        throws IOException
    {
        out.writeEncoded(NULL, zeroBytes);
    }
    
	public boolean equals(
		Object o)
	{
        if ((o == null) || !(o instanceof DERNull))
        {
            return false;
        }
        
		return true;
	}
}
