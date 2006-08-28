/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 * A NULL object.
 */
public abstract class ASN1Null
    extends DERObject
{
    public ASN1Null()
    {
    }

    public int hashCode()
    {
        return 0;
    }

	public boolean equals(
		Object o)
	{
        if ((o == null) || !(o instanceof ASN1Null))
        {
            return false;
        }
        
		return true;
	}

    abstract void encode(DEROutputStream out)
        throws IOException;
}
