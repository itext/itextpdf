/*
 * $Id: ICC_Profile.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;

public class ICC_Profile {
    protected byte[] data;
    protected int numComponents;
    private static HashMap<String, Integer> cstags = new HashMap<String, Integer>();

    protected ICC_Profile() {
    }
    
    public static ICC_Profile getInstance(byte[] data, int numComponents) {
        if (data.length < 128 || data[36] != 0x61 || data[37] != 0x63
                || data[38] != 0x73 || data[39] != 0x70)
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile"));
        try {
        	ICC_Profile icc = new ICC_Profile();
        	icc.data = data;
        	Integer cs;
        	cs = cstags.get(new String(data, 16, 4, "US-ASCII"));
        	int nc = cs == null ? 0 : cs.intValue();
        	icc.numComponents = nc;
        	// invalid ICC
        	if (nc != numComponents) {
        		throw new IllegalArgumentException("ICC profile contains " + nc + " component(s), the image data contains " + numComponents + " component(s)");
        	}
        	return icc;
    	} catch (UnsupportedEncodingException e) {
			throw new ExceptionConverter(e);
		}
    }

    public static ICC_Profile getInstance(byte[] data) {
    	try {
	        Integer cs;
			cs = cstags.get(new String(data, 16, 4, "US-ASCII"));
	        int numComponents = cs == null ? 0 : cs.intValue();
	        return getInstance(data, numComponents);
		} catch (UnsupportedEncodingException e) {
			throw new ExceptionConverter(e);
		}
    }

    public static ICC_Profile getInstance(InputStream file) {
        try {
            byte[] head = new byte[128];
            int remain = head.length;
            int ptr = 0;
            while (remain > 0) {
                int n = file.read(head, ptr, remain);
                if (n < 0)
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile"));
                remain -= n;
                ptr += n;
            }
            if (head[36] != 0x61 || head[37] != 0x63
                || head[38] != 0x73 || head[39] != 0x70)
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile"));
            remain = (head[0] & 0xff) << 24 | (head[1] & 0xff) << 16
                      | (head[2] & 0xff) <<  8 | head[3] & 0xff;
            byte[] icc = new byte[remain];
            System.arraycopy(head, 0, icc, 0, head.length);
            remain -= head.length;
            ptr = head.length;
            while (remain > 0) {
                int n = file.read(icc, ptr, remain);
                if (n < 0)
                    throw new IllegalArgumentException(MessageLocalization.getComposedMessage("invalid.icc.profile"));
                remain -= n;
                ptr += n;
            }
            return getInstance(icc);
        }
        catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }

    public static ICC_Profile GetInstance(String fname) {
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(fname);
            ICC_Profile icc = getInstance(fs);
            return icc;
        }
        catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
        finally {
            try{fs.close();}catch(Exception x){}
        }
    }

    public byte[] getData() {
        return data;
    }

    public int getNumComponents() {
        return numComponents;
    }

    static {
        cstags.put("XYZ ", Integer.valueOf(3));
        cstags.put("Lab ", Integer.valueOf(3));
        cstags.put("Luv ", Integer.valueOf(3));
        cstags.put("YCbr", Integer.valueOf(3));
        cstags.put("Yxy ", Integer.valueOf(3));
        cstags.put("RGB ", Integer.valueOf(3));
        cstags.put("GRAY", Integer.valueOf(1));
        cstags.put("HSV ", Integer.valueOf(3));
        cstags.put("HLS ", Integer.valueOf(3));
        cstags.put("CMYK", Integer.valueOf(4));
        cstags.put("CMY ", Integer.valueOf(3));
        cstags.put("2CLR", Integer.valueOf(2));
        cstags.put("3CLR", Integer.valueOf(3));
        cstags.put("4CLR", Integer.valueOf(4));
        cstags.put("5CLR", Integer.valueOf(5));
        cstags.put("6CLR", Integer.valueOf(6));
        cstags.put("7CLR", Integer.valueOf(7));
        cstags.put("8CLR", Integer.valueOf(8));
        cstags.put("9CLR", Integer.valueOf(9));
        cstags.put("ACLR", Integer.valueOf(10));
        cstags.put("BCLR", Integer.valueOf(11));
        cstags.put("CCLR", Integer.valueOf(12));
        cstags.put("DCLR", Integer.valueOf(13));
        cstags.put("ECLR", Integer.valueOf(14));
        cstags.put("FCLR", Integer.valueOf(15));
    }
}
