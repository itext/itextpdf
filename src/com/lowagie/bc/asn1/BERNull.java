/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 * A BER NULL object.
 */
public class BERNull
    extends DERNull
{
    public BERNull()
    {
    }

    void encode(
        DEROutputStream  out)
        throws IOException
    {
        if (out instanceof ASN1OutputStream || out instanceof BEROutputStream)
        {
            out.write(NULL);
            out.write(0);
            out.write(0);
        }
        else
        {
            super.encode(out);
        }
    }
}
