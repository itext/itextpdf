/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.IOException;

/**
 * We insert one of these when we find a tag we don't recognise.
 */
public class DERUnknownTag
    extends DERObject
{
    int         tag;
    byte[]      data;

    /**
     * @param tag the tag value.
     * @param data the octets making up the time.
     */
    public DERUnknownTag(
        int     tag,
        byte[]  data)
    {
        this.tag = tag;
        this.data = data;
    }

    public int getTag()
    {
        return tag;
    }

    public byte[] getData()
    {
        return data;
    }

    void encode(
        DEROutputStream  out)
        throws IOException
    {
        out.writeEncoded(tag, data);
    }
    
	public boolean equals(
		Object o)
	{
        if ((o == null) || !(o instanceof DERUnknownTag))
        {
            return false;
        }
        
        DERUnknownTag other = (DERUnknownTag)o;
        
        if(tag != other.tag)
        {
			return false;
		}
		
		if(data.length != other.data.length)
		{
			return false;
		}
		
		for(int i = 0; i < data.length; i++) 
		{
			if(data[i] != other.data[i])
			{
				return false;
			}
		}
		
		return true;
	}
}
