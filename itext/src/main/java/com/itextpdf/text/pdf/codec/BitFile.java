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
 * Came from GIFEncoder initially.
 * Modified - to allow for output compressed data without the block counts
 * which breakup the compressed data stream for GIF.
 * @since 5.0.2
 */
public class BitFile
{
    OutputStream output_;
    byte buffer_[];
    int	index_;
    int bitsLeft_;	// bits left at current index that are avail.

    /** note this also indicates gif format BITFile. **/
    boolean blocks_ = false;

    /**
	 * @param output destination for output data
	 * @param blocks GIF LZW requires block counts for output data
	 **/
    public BitFile(OutputStream output, boolean blocks)
    {
        output_	= output;
        blocks_ = blocks;
        buffer_	= new byte[256];
        index_ = 0;
        bitsLeft_ =	8;
    }

    public void	flush()	throws IOException
    {
        int	numBytes = index_ +	(bitsLeft_ == 8	? 0	: 1);
        if (numBytes > 0)
            {
                if (blocks_)
                    output_.write(numBytes);
                output_.write(buffer_, 0, numBytes);
                buffer_[0] = 0;
                index_ = 0;
                bitsLeft_ =	8;
            }
    }

    public void	writeBits(int bits,	int	numbits) throws	IOException
    {
        int	bitsWritten	= 0;
        int	numBytes = 255;		// gif block count
        do
            {
                // This handles the GIF block count stuff
                if ((index_	== 254 && bitsLeft_	== 0) || index_	> 254)
                    {
                        if (blocks_)
                            output_.write(numBytes);

                        output_.write(buffer_, 0, numBytes);

                        buffer_[0] = 0;
                        index_ = 0;
                        bitsLeft_ =	8;
                    }

                if (numbits	<= bitsLeft_) // bits contents fit in current index byte
                    {
                        if (blocks_) // GIF
                            {
                                buffer_[index_]	|= (bits & ((1 << numbits) - 1)) <<	(8 - bitsLeft_);
                                bitsWritten	+= numbits;
                                bitsLeft_ -= numbits;
                                numbits	= 0;
                            }
                        else
                            {
                                buffer_[index_]	|= (bits & ((1 << numbits) - 1)) <<	(bitsLeft_ - numbits);
                                bitsWritten	+= numbits;
                                bitsLeft_ -= numbits;
                                numbits	= 0;

                            }
                    }
                else	// bits overflow from current byte to next.
                    {
                        if (blocks_)	// GIF
                            {
                                // if bits  > space left in current byte then the lowest order bits
                                // of code are taken and put in current byte and rest put in next.
                                buffer_[index_]	|= (bits & ((1 << bitsLeft_) - 1)) << (8 - bitsLeft_);
                                bitsWritten	+= bitsLeft_;
                                bits >>= bitsLeft_;
                                numbits	-= bitsLeft_;
                                buffer_[++index_] =	0;
                                bitsLeft_ =	8;
                            }
                        else
                            {
                                // if bits  > space left in current byte then the highest order bits
                                // of code are taken and put in current byte and rest put in next.
                                // at highest order bit location !! 
                                int topbits = (bits >>> (numbits - bitsLeft_)) & ((1 << bitsLeft_) - 1);
                                buffer_[index_]	|= topbits;
                                numbits -= bitsLeft_;	// ok this many bits gone off the top
                                bitsWritten	+= bitsLeft_;
                                buffer_[++index_] =	0;	// next index
                                bitsLeft_ =	8;
                            }
                    }

            } while	(numbits !=	0);
    }
}
