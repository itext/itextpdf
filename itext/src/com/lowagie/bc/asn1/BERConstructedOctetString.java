/* see bouncycastle_license.txt */

package com.lowagie.bc.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class BERConstructedOctetString
    extends DEROctetString
{
    /**
     * convert a vector of octet strings into a single byte string
     */
    static private byte[] toBytes(
        Vector  octs)
    {
        ByteArrayOutputStream   bOut = new ByteArrayOutputStream();

        for (int i = 0; i != octs.size(); i++)
        {
            DEROctetString  o = (DEROctetString)octs.elementAt(i);

            try
            {
                bOut.write(o.getOctets());
            }
            catch (IOException e)
            {
                throw new RuntimeException("exception converting octets " + e.toString());
            }
        }

        return bOut.toByteArray();
    }

    private Vector  octs;

    /**
     * @param string the octets making up the octet string.
     */
    public BERConstructedOctetString(
        byte[]  string)
    {
		super(string);
    }

    public BERConstructedOctetString(
        Vector  octs)
    {
		super(toBytes(octs));

        this.octs = octs;
    }

    public BERConstructedOctetString(
        DERObject  obj)
    {
		super(obj);
    }

    public BERConstructedOctetString(
        DEREncodable  obj)
    {
        super(obj.getDERObject());
    }

    public byte[] getOctets()
    {
        return string;
    }

    /**
     * return the DER octets that make up this string.
     */
    public Enumeration getObjects()
    {
        if (octs == null)
        {
            return generateOcts().elements();
        }

        return octs.elements();
    }

    private Vector generateOcts()
    {
        int     start = 0;
        int     end = 0;
        Vector  vec = new Vector();

        while ((end + 1) < string.length)
        {
            if (string[end] == 0 && string[end + 1] == 0)
            {
                byte[]  nStr = new byte[end - start + 1];

				System.arraycopy(string, start, nStr, 0, nStr.length);

                vec.addElement(new DEROctetString(nStr));
                start = end + 1;
            }
            end++;
        }

        byte[]  nStr = new byte[string.length - start];

		System.arraycopy(string, start, nStr, 0, nStr.length);

        vec.addElement(new DEROctetString(nStr));

        return vec;
    }

    public void encode(
        DEROutputStream out)
        throws IOException
    {
        if (out instanceof ASN1OutputStream || out instanceof BEROutputStream)
        {
            out.write(CONSTRUCTED | OCTET_STRING);

            out.write(0x80);

			//
			// write out the octet array
			//
            if (octs != null)
            {
				for (int i = 0; i != octs.size(); i++)
				{
					out.writeObject(octs.elementAt(i));
				}
			}
			else
			{
        		int     start = 0;
				int     end = 0;

				while ((end + 1) < string.length)
				{
					if (string[end] == 0 && string[end + 1] == 0)
					{
						byte[]  nStr = new byte[end - start + 1];

						System.arraycopy(string, start, nStr, 0, nStr.length);

						out.writeObject(new DEROctetString(nStr));
						start = end + 1;
					}
            		end++;
        		}

				byte[]  nStr = new byte[string.length - start];

				System.arraycopy(string, start, nStr, 0, nStr.length);

        		out.writeObject(new DEROctetString(nStr));
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
