/*
 * Copyright (c) 2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * -Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduct the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that Software is not designed,licensed or intended for use in
 * the design, construction, operation or maintenance of any nuclear facility.
 *
 * Modified by Paulo Soares for iText.
 */
package com.lowagie.text.pdf.codec;
import com.lowagie.text.pdf.*;

public class CCITTG4Encoder {
    
    /**
     * The CCITT numerical definition of white.
     */
    private static final int WHITE = 0;
    
    /**
     * The CCITT numerical definition of black.
     */
    private static final int BLACK = 1;
    
    // --- Begin tables for CCITT compression ---
    
    private static byte[] byteTable = new byte[] {
        8, 7, 6, 6, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 4, 4,     // 0 to 15
        3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,     // 16 to 31
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,     // 32 to 47
        2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,     // 48 to 63
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,     // 64 to 79
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,     // 80 to 95
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,     // 96 to 111
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,     // 112 to 127
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,     // 128 to 143
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,     // 144 to 159
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,     // 160 to 175
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,     // 176 to 191
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,     // 192 to 207
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,     // 208 to 223
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,     // 224 to 239
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0      // 240 to 255
    };
    
    /**
     * Terminating codes for black runs.
     */
    private static int[] termCodesBlack = new int[] {
        /*     0 0x0000 */     0x0dc0000a, 0x40000003, 0xc0000002, 0x80000002,
        /*     4 0x0004 */     0x60000003, 0x30000004, 0x20000004, 0x18000005,
        /*     8 0x0008 */     0x14000006, 0x10000006, 0x08000007, 0x0a000007,
        /*    12 0x000c */     0x0e000007, 0x04000008, 0x07000008, 0x0c000009,
        /*    16 0x0010 */     0x05c0000a, 0x0600000a, 0x0200000a, 0x0ce0000b,
        /*    20 0x0014 */     0x0d00000b, 0x0d80000b, 0x06e0000b, 0x0500000b,
        /*    24 0x0018 */     0x02e0000b, 0x0300000b, 0x0ca0000c, 0x0cb0000c,
        /*    28 0x001c */     0x0cc0000c, 0x0cd0000c, 0x0680000c, 0x0690000c,
        /*    32 0x0020 */     0x06a0000c, 0x06b0000c, 0x0d20000c, 0x0d30000c,
        /*    36 0x0024 */     0x0d40000c, 0x0d50000c, 0x0d60000c, 0x0d70000c,
        /*    40 0x0028 */     0x06c0000c, 0x06d0000c, 0x0da0000c, 0x0db0000c,
        /*    44 0x002c */     0x0540000c, 0x0550000c, 0x0560000c, 0x0570000c,
        /*    48 0x0030 */     0x0640000c, 0x0650000c, 0x0520000c, 0x0530000c,
        /*    52 0x0034 */     0x0240000c, 0x0370000c, 0x0380000c, 0x0270000c,
        /*    56 0x0038 */     0x0280000c, 0x0580000c, 0x0590000c, 0x02b0000c,
        /*    60 0x003c */     0x02c0000c, 0x05a0000c, 0x0660000c, 0x0670000c
    };
    
    /**
     * Terminating codes for white runs.
     */
    private static int[] termCodesWhite = new int[] {
        /*     0 0x0000 */     0x35000008, 0x1c000006, 0x70000004, 0x80000004,
        /*     4 0x0004 */     0xb0000004, 0xc0000004, 0xe0000004, 0xf0000004,
        /*     8 0x0008 */     0x98000005, 0xa0000005, 0x38000005, 0x40000005,
        /*    12 0x000c */     0x20000006, 0x0c000006, 0xd0000006, 0xd4000006,
        /*    16 0x0010 */     0xa8000006, 0xac000006, 0x4e000007, 0x18000007,
        /*    20 0x0014 */     0x10000007, 0x2e000007, 0x06000007, 0x08000007,
        /*    24 0x0018 */     0x50000007, 0x56000007, 0x26000007, 0x48000007,
        /*    28 0x001c */     0x30000007, 0x02000008, 0x03000008, 0x1a000008,
        /*    32 0x0020 */     0x1b000008, 0x12000008, 0x13000008, 0x14000008,
        /*    36 0x0024 */     0x15000008, 0x16000008, 0x17000008, 0x28000008,
        /*    40 0x0028 */     0x29000008, 0x2a000008, 0x2b000008, 0x2c000008,
        /*    44 0x002c */     0x2d000008, 0x04000008, 0x05000008, 0x0a000008,
        /*    48 0x0030 */     0x0b000008, 0x52000008, 0x53000008, 0x54000008,
        /*    52 0x0034 */     0x55000008, 0x24000008, 0x25000008, 0x58000008,
        /*    56 0x0038 */     0x59000008, 0x5a000008, 0x5b000008, 0x4a000008,
        /*    60 0x003c */     0x4b000008, 0x32000008, 0x33000008, 0x34000008
    };
    
    /**
     * Make-up codes for black runs.
     */
    private static int[] makeupCodesBlack = new int[] {
        /*     0 0x0000 */     0x00000000, 0x03c0000a, 0x0c80000c, 0x0c90000c,
        /*     4 0x0004 */     0x05b0000c, 0x0330000c, 0x0340000c, 0x0350000c,
        /*     8 0x0008 */     0x0360000d, 0x0368000d, 0x0250000d, 0x0258000d,
        /*    12 0x000c */     0x0260000d, 0x0268000d, 0x0390000d, 0x0398000d,
        /*    16 0x0010 */     0x03a0000d, 0x03a8000d, 0x03b0000d, 0x03b8000d,
        /*    20 0x0014 */     0x0290000d, 0x0298000d, 0x02a0000d, 0x02a8000d,
        /*    24 0x0018 */     0x02d0000d, 0x02d8000d, 0x0320000d, 0x0328000d,
        /*    28 0x001c */     0x0100000b, 0x0180000b, 0x01a0000b, 0x0120000c,
        /*    32 0x0020 */     0x0130000c, 0x0140000c, 0x0150000c, 0x0160000c,
        /*    36 0x0024 */     0x0170000c, 0x01c0000c, 0x01d0000c, 0x01e0000c,
        /*    40 0x0028 */     0x01f0000c, 0x00000000, 0x00000000, 0x00000000,
        /*    44 0x002c */     0x00000000, 0x00000000, 0x00000000, 0x00000000,
        /*    48 0x0030 */     0x00000000, 0x00000000, 0x00000000, 0x00000000,
        /*    52 0x0034 */     0x00000000, 0x00000000, 0x00000000, 0x00000000,
        /*    56 0x0038 */     0x00000000, 0x00000000, 0x00000000, 0x00000000
    };
    
    /**
     * Make-up codes for white runs.
     */
    private static int[] makeupCodesWhite = new int[] {
        /*     0 0x0000 */     0x00000000, 0xd8000005, 0x90000005, 0x5c000006,
        /*     4 0x0004 */     0x6e000007, 0x36000008, 0x37000008, 0x64000008,
        /*     8 0x0008 */     0x65000008, 0x68000008, 0x67000008, 0x66000009,
        /*    12 0x000c */     0x66800009, 0x69000009, 0x69800009, 0x6a000009,
        /*    16 0x0010 */     0x6a800009, 0x6b000009, 0x6b800009, 0x6c000009,
        /*    20 0x0014 */     0x6c800009, 0x6d000009, 0x6d800009, 0x4c000009,
        /*    24 0x0018 */     0x4c800009, 0x4d000009, 0x60000006, 0x4d800009,
        /*    28 0x001c */     0x0100000b, 0x0180000b, 0x01a0000b, 0x0120000c,
        /*    32 0x0020 */     0x0130000c, 0x0140000c, 0x0150000c, 0x0160000c,
        /*    36 0x0024 */     0x0170000c, 0x01c0000c, 0x01d0000c, 0x01e0000c,
        /*    40 0x0028 */     0x01f0000c, 0x00000000, 0x00000000, 0x00000000,
        /*    44 0x002c */     0x00000000, 0x00000000, 0x00000000, 0x00000000,
        /*    48 0x0030 */     0x00000000, 0x00000000, 0x00000000, 0x00000000,
        /*    52 0x0034 */     0x00000000, 0x00000000, 0x00000000, 0x00000000,
        /*    56 0x0038 */     0x00000000, 0x00000000, 0x00000000, 0x00000000
    };
    
    /**
     * Pass mode table.
     */
    private static int[] passMode = new int[] {
        0x10000004            // 0001
    };
    
    /**
     * Vertical mode table.
     */
    private static int[] vertMode = new int[] {
        0x06000007,            // 0000011
        0x0c000006,            // 000011
        0x60000003,            // 011
        0x80000001,            // 1
        0x40000003,            // 010
        0x08000006,            // 000010
        0x04000007            // 0000010
    };
    
    /**
     * Horizontal mode table.
     */
    private static int[] horzMode = new int[] {
        0x20000003            // 001
    };
    
    /**
     * Black and white terminating code table.
     */
    private static int[][] termCodes =
    new int[][] {termCodesWhite, termCodesBlack};
    
    /**
     * Black and white make-up code table.
     */
    private static int[][] makeupCodes =
        new int[][] {makeupCodesWhite, makeupCodesBlack};
    
    /**
     * Black and white pass mode table.
     */
    private static int[][] pass = new int[][] {passMode, passMode};
    
    /**
     * Black and white vertical mode table.
     */
    private static int[][] vert = new int[][] {vertMode, vertMode};
    
    /**
     * Black and white horizontal mode table.
     */
    private static int[][] horz = new int[][] {horzMode, horzMode};
    
    // --- End tables for CCITT compression ---
    
    /**
     * Output bit buffer.
     */
    private int bits;
    
    /**
     * Number of bits in the output bit buffer.
     */
    private int ndex;
    private ByteBuffer outBuf = new ByteBuffer(1024);
    private int width;
    private int lineStride;
    byte[] refData = null;
    
    /**
     * Constructs a <code>TIFFFaxEncoder</code> for CCITT bilevel encoding.
     */
    public CCITTG4Encoder(int width) {
        this.width = width;
        lineStride = (width + 7) / 8;
        initBitBuf();
    }
    
    public static byte[] compress(byte[] data, int width, int height) {
        CCITTG4Encoder g4 = new CCITTG4Encoder(width);
        int yPos = 0;
        while (height-- != 0) {
            g4.encodeT6Line(data, yPos);
            yPos += g4.lineStride;
        }
        return g4.close();
    }
    
    public void encodeT6Lines(byte data[], int lineAddr, int height) {
        int yPos = lineAddr;
        while (height-- != 0) {
            encodeT6Line(data, yPos);
            yPos += lineStride;
        }
    }
    /**
     * Return min of <code>maxOffset</code> or offset of first pixel
     * different from pixel at <code>bitOffset</code>.
     */
    private int nextState(byte[] data,
        int    base,
        int    bitOffset,
        int    maxOffset) {
        if(data == null) {
            return maxOffset;
        }
        
        int next  = base + (bitOffset>>>3);
        int end   = base + (maxOffset>>>3);
        if(end == data.length) { // Prevents out of bounds exception below
            end--;
        }
        if (next == data.length) // and so does this
            --next;
        int extra = bitOffset & 0x7;
        
        int  testbyte;
        if((data[next] & (0x80 >>> extra)) != 0) {    // look for "0"
            testbyte = ~(data[next]) & (0xff >>> extra);
            while (next < end) {
                if (testbyte != 0) {
                    break;
                }
                testbyte = ~(data[++next]) & 0xff;
            }
        } else {                // look for "1"
            if ((testbyte = (data[next] & (0xff >>> extra))) != 0) {
                bitOffset = (next-base)*8 + byteTable[testbyte];
                return ((bitOffset < maxOffset) ? bitOffset : maxOffset);
            }
            while (next < end) {
                if ((testbyte = data[++next]&0xff) != 0) {
                    // "1" is in current byte
                    bitOffset = (next-base)*8 + byteTable[testbyte];
                    return ((bitOffset < maxOffset) ? bitOffset : maxOffset);
                }
            }
        }
        bitOffset = (next-base)*8 + byteTable[testbyte];
        return ((bitOffset < maxOffset) ? bitOffset : maxOffset);
    }
    
    /**
     * Initialize bit buffer machinery.
     */
    private void initBitBuf() {
        ndex = 0;
        bits = 0x00000000;
    }
    
    /**
     * Get code for run and add to compressed bitstream.
     */
    private void add1DBits(
        int    count, // #pixels in run
        int    color) // color of run
    {
        int                 sixtyfours;
        int        mask;
        
        sixtyfours = count >>> 6;    // count / 64;
        count = count & 0x3f;       // count % 64
        if (sixtyfours != 0) {
            for ( ; sixtyfours > 40; sixtyfours -= 40) {
                mask = makeupCodes[color][40];
                bits |= (mask & 0xfff80000) >>> ndex;
                ndex += (int)(mask & 0x0000ffff);
                while (ndex > 7) {
                    outBuf.append((byte)(bits >>> 24));
                    bits <<= 8;
                    ndex -= 8;
                }
            }
            
            mask = makeupCodes[color][sixtyfours];
            bits |= (mask & 0xfff80000) >>> ndex;
            ndex += (int)(mask & 0x0000ffff);
            while (ndex > 7) {
                outBuf.append((byte)(bits >>> 24));
                bits <<= 8;
                ndex -= 8;
            }
        }
        
        mask = termCodes[color][count];
        bits |= (mask & 0xfff80000) >>> ndex;
        ndex += (int)(mask & 0x0000ffff);
        while (ndex > 7) {
            outBuf.append((byte)(bits >>> 24));
            bits <<= 8;
            ndex -= 8;
        }        
    }
    
    /**
     * Place entry from mode table into compressed bitstream.
     */
    private void add2DBits(
        int[][] mode,  // 2-D mode to be encoded
        int     entry) // mode entry (0 unless vertical)
    {
        int        mask;
        int                 color = 0;
        
        mask = mode[color][entry];
        bits |= (mask & 0xfff80000) >>> ndex;
        ndex += (int)(mask & 0x0000ffff);
        while (ndex > 7) {
            outBuf.append((byte)(bits >>> 24));
            bits <<= 8;
            ndex -= 8;
        }
    }
    
    /**
     * Add an End-of-Line (EOL == 0x001) to the compressed bitstream
     * with optional byte alignment.
     */
    private void addEOL(boolean is1DMode,// 1D encoding
    boolean addFill, // byte aligned EOLs
    boolean add1    // add1 ? EOL+1 : EOL+0
    )
    {
        
        //
        // Add zero-valued fill bits such that the EOL is aligned as
        //
        //     xxxx 0000 0000 0001
        //
        if(addFill) {
            //
            // Simply increment the bit count. No need to feed bits into
            // the output buffer at this point as there are at most 7 bits
            // in the bit buffer, at most 7 are added here, and at most
            // 13 below making the total 7+7+13 = 27 before the bit feed
            // at the end of this routine.
            //
            ndex += ((ndex <= 4) ? 4 - ndex : 12 - ndex);
        }
        
        //
        // Write EOL into buffer
        //
        if(is1DMode) {
            bits |= 0x00100000 >>> ndex;
            ndex += 12;
        } else {
            bits |= (add1 ? 0x00180000 : 0x00100000) >>> ndex;
            ndex += 13;
        }
        
        while (ndex > 7) {
            outBuf.append((byte)(bits >>> 24));
            bits <<= 8;
            ndex -= 8;
        }
    }
    
    /**
     * Add an End-of-Facsimile-Block (EOFB == 0x001001) to the compressed
     * bitstream.
     */
    private void addEOFB()
    {
        //
        // eofb code
        //
        bits |= 0x00100100 >>> ndex;
        
        //
        // eofb code length
        //
        ndex += 24;
        
        //
        // flush all pending bits
        //
        while(ndex > 0) {
            outBuf.append((byte)(bits >>> 24));
            bits <<= 8;
            ndex -= 8;
        }
    }

    public void encodeT6Line(byte data[], int lineAddr) {
        int a0   = 0;
        int last = a0 + width;

        int testbit = 
        ((data[lineAddr + (a0>>>3)]&0xff) >>>
        (7-(a0 & 0x7))) & 0x1;
        int a1 = testbit != 0 ?
        a0 : nextState(data, lineAddr, a0, last);

        testbit = refData == null ?
        0: ((refData[(a0>>>3)]&0xff) >>>
        (7-(a0 & 0x7))) & 0x1;
        int b1 = testbit != 0 ?
        a0 : nextState(refData, 0, a0, last);

        //
        // The current color is set to WHITE at line start
        //
        int color = WHITE;

        while(true) {
            int b2 = nextState(refData, 0, b1, last);
            if(b2 < a1) {          // pass mode
                add2DBits(pass, 0);
                a0 = b2;
            } else {
                int tmp = b1 - a1 + 3;
                if((tmp <= 6) && (tmp >= 0)) { // vertical mode
                    add2DBits(vert, tmp);
                    a0 = a1;
                } else {            // horizontal mode
                    int a2 = nextState(data, lineAddr, a1, last);
                    add2DBits(horz, 0);
                    add1DBits(a1-a0, color);
                    add1DBits(a2-a1, color^1);
                    a0 = a2;
                }
            }
            if(a0 >= last) {
                break;
            }
            color = ((data[lineAddr + (a0>>>3)]&0xff) >>>
            (7-(a0 & 0x7))) & 0x1;
            a1 = nextState(data, lineAddr, a0, last);
            b1 = nextState(refData, 0, a0, last);
            testbit = refData == null ?
            0: ((refData[(b1>>>3)]&0xff) >>>
            (7-(b1 & 0x7))) & 0x1;
            if(testbit == color) {
                b1 = nextState(refData, 0, b1, last);
            }
        }

        if (refData == null)
            refData = new byte[lineStride + 1];
        System.arraycopy(data, lineAddr, refData, 0, lineStride);
    }
    
    public byte[] close() {
        addEOFB();
        return outBuf.toByteArray();
    }
}