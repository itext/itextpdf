/*
 * Copyright 2003-2012 by Paulo Soares.
 *
 * This code was originally released in 2001 by SUN (see class
 * com.sun.media.imageio.plugins.tiff.TIFFDirectory.java)
 * using the BSD license in a specific wording. In a mail dating from
 * January 23, 2008, Brian Burkhalter (@sun.com) gave us permission
 * to use the code under the following version of the BSD license:
 *
 * Copyright (c) 2005 Sun Microsystems, Inc. All  Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 
 * 
 * - Redistribution of source code must retain the above copyright 
 *   notice, this  list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in 
 *   the documentation and/or other materials provided with the
 *   distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of 
 * contributors may be used to endorse or promote products derived 
 * from this software without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any 
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND 
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS LICENSORS SHALL 
 * NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF 
 * USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR 
 * ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL,
 * CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND
 * REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR
 * INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES. 
 * 
 * You acknowledge that this software is not designed or intended for 
 * use in the design, construction, operation or maintenance of any 
 * nuclear facility. 
 *
 * $Revision$
 * $Date$
 * $State: Exp $
 */

package com.itextpdf.text.pdf.codec;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Modified from original LZWCompressor to change interface to passing a
 * buffer of data to be compressed.
 * @since 5.0.2
 */
public class LZWCompressor
{
    /** base underlying code size of data being compressed 8 for TIFF, 1 to 8 for GIF **/
    int codeSize_;

    /** reserved clear code based on code size **/
    int clearCode_;

    /** reserved end of data code based on code size **/
    int endOfInfo_;

    /** current number bits output for each code **/
    int numBits_;

    /** limit at which current number of bits code size has to be increased **/
    int limit_;

    /** the prefix code which represents the predecessor string to current input point **/
    short prefix_;

    /** output destination for bit codes **/
    BitFile bf_;

    /** general purpose LZW string table **/
    LZWStringTable lzss_;

    /** modify the limits of the code values in LZW encoding due to TIFF bug / feature **/
    boolean tiffFudge_;

    /**
	 * @param out destination for compressed data
	 * @param codeSize the initial code size for the LZW compressor
	 * @param TIFF flag indicating that TIFF lzw fudge needs to be applied
	 * @exception IOException if underlying output stream error
	 **/
    public LZWCompressor(OutputStream out, int codeSize, boolean TIFF) throws IOException
    {
        bf_ = new BitFile(out, !TIFF);	// set flag for GIF as NOT tiff
        codeSize_  = codeSize;
        tiffFudge_ = TIFF;
        clearCode_ = 1 << codeSize_;
        endOfInfo_ = clearCode_ + 1;
        numBits_   = codeSize_ + 1;

        limit_ = (1 << numBits_) - 1;
        if (tiffFudge_)
            --limit_;

        prefix_ = (short)0xFFFF;
        lzss_ = new LZWStringTable();
        lzss_.ClearTable(codeSize_);
        bf_.writeBits(clearCode_, numBits_);
    }

    /**
	 * @param buf data to be compressed to output stream
	 * @exception IOException if underlying output stream error
	 **/
    public void compress(byte[] buf, int offset, int length)
        throws IOException
    {
        int idx;
        byte c;
        short index;

        int maxOffset = offset + length;
        for (idx = offset; idx < maxOffset; ++idx)
            {
                c = buf[idx];
                if ((index = lzss_.FindCharString(prefix_, c)) != -1)
                    prefix_ = index;
                else
                    {
                        bf_.writeBits(prefix_, numBits_);
                        if (lzss_.AddCharString(prefix_, c) > limit_)
                            {
                                if (numBits_ == 12)
                                    {
                                        bf_.writeBits(clearCode_, numBits_);
                                        lzss_.ClearTable(codeSize_);
                                        numBits_ = codeSize_ + 1;
                                    }
                                else
                                    ++numBits_;

                                limit_ = (1 << numBits_) - 1;
                                if (tiffFudge_)
                                    --limit_;
                            }
                        prefix_ = (short)((short)c & 0xFF);
                    }
            }
    }

    /**
	 * Indicate to compressor that no more data to go so write out
	 * any remaining buffered data.
	 *
	 * @exception IOException if underlying output stream error
	 **/
    public void flush() throws IOException
    {
        if (prefix_ != -1)
            bf_.writeBits(prefix_, numBits_);
		
        bf_.writeBits(endOfInfo_, numBits_);
        bf_.flush();
    }
}
