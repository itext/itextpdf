/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Base class for an application specific object
 */
public class DERApplicationSpecific 
	extends DERObject
{
	private int		tag;
	private byte[]	octets;
	
	public DERApplicationSpecific(
		int		tag,
		byte[]	octets)
	{
		this.tag = tag;
		this.octets = octets;
	}
	
	public DERApplicationSpecific(
		int 							tag, 
		DEREncodable 		object) 
		throws IOException 
	{
		this.tag = tag | DERTags.CONSTRUCTED;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DEROutputStream dos = new DEROutputStream(baos);
		
		dos.writeObject(object);
		
		this.octets = baos.toByteArray();
	}
	
	public boolean isConstructed()
	{
		return (tag & DERTags.CONSTRUCTED) != 0;
	}
	
	public byte[] getContents()
	{
		return octets;
	}
	
	public int getApplicationTag() 
	{
		return tag & 0x1F;
	}
 	
	public DERObject getObject() 
		throws IOException 
	{
		return new ASN1InputStream(new ByteArrayInputStream(getContents())).readObject();
	}
	
    /* (non-Javadoc)
     * @see org.bouncycastle.asn1.DERObject#encode(org.bouncycastle.asn1.DEROutputStream)
     */
    void encode(DEROutputStream out) throws IOException
    {
        out.writeEncoded(DERTags.APPLICATION | tag, octets);
    }
}
