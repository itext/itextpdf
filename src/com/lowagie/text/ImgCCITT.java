/*
 * $Id$
 * $Name$
 *
 * Copyright 2000, 2001, 2002 by Paulo Soares.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text;

import java.net.URL;

/**
 * CCITT Image data that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 *
 * @author  Paulo Soares
 */

public class ImgCCITT extends Image implements Element {
    /** A table to do fast bit reversal.
     */    
    static final byte bitReversal[] = {
        (byte)0x00, (byte)0x80, (byte)0x40, (byte)0xc0, (byte)0x20, (byte)0xa0, (byte)0x60, (byte)0xe0,
        (byte)0x10, (byte)0x90, (byte)0x50, (byte)0xd0, (byte)0x30, (byte)0xb0, (byte)0x70, (byte)0xf0,
        (byte)0x08, (byte)0x88, (byte)0x48, (byte)0xc8, (byte)0x28, (byte)0xa8, (byte)0x68, (byte)0xe8,
        (byte)0x18, (byte)0x98, (byte)0x58, (byte)0xd8, (byte)0x38, (byte)0xb8, (byte)0x78, (byte)0xf8,
        (byte)0x04, (byte)0x84, (byte)0x44, (byte)0xc4, (byte)0x24, (byte)0xa4, (byte)0x64, (byte)0xe4,
        (byte)0x14, (byte)0x94, (byte)0x54, (byte)0xd4, (byte)0x34, (byte)0xb4, (byte)0x74, (byte)0xf4,
        (byte)0x0c, (byte)0x8c, (byte)0x4c, (byte)0xcc, (byte)0x2c, (byte)0xac, (byte)0x6c, (byte)0xec,
        (byte)0x1c, (byte)0x9c, (byte)0x5c, (byte)0xdc, (byte)0x3c, (byte)0xbc, (byte)0x7c, (byte)0xfc,
        (byte)0x02, (byte)0x82, (byte)0x42, (byte)0xc2, (byte)0x22, (byte)0xa2, (byte)0x62, (byte)0xe2,
        (byte)0x12, (byte)0x92, (byte)0x52, (byte)0xd2, (byte)0x32, (byte)0xb2, (byte)0x72, (byte)0xf2,
        (byte)0x0a, (byte)0x8a, (byte)0x4a, (byte)0xca, (byte)0x2a, (byte)0xaa, (byte)0x6a, (byte)0xea,
        (byte)0x1a, (byte)0x9a, (byte)0x5a, (byte)0xda, (byte)0x3a, (byte)0xba, (byte)0x7a, (byte)0xfa,
        (byte)0x06, (byte)0x86, (byte)0x46, (byte)0xc6, (byte)0x26, (byte)0xa6, (byte)0x66, (byte)0xe6,
        (byte)0x16, (byte)0x96, (byte)0x56, (byte)0xd6, (byte)0x36, (byte)0xb6, (byte)0x76, (byte)0xf6,
        (byte)0x0e, (byte)0x8e, (byte)0x4e, (byte)0xce, (byte)0x2e, (byte)0xae, (byte)0x6e, (byte)0xee,
        (byte)0x1e, (byte)0x9e, (byte)0x5e, (byte)0xde, (byte)0x3e, (byte)0xbe, (byte)0x7e, (byte)0xfe,
        (byte)0x01, (byte)0x81, (byte)0x41, (byte)0xc1, (byte)0x21, (byte)0xa1, (byte)0x61, (byte)0xe1,
        (byte)0x11, (byte)0x91, (byte)0x51, (byte)0xd1, (byte)0x31, (byte)0xb1, (byte)0x71, (byte)0xf1,
        (byte)0x09, (byte)0x89, (byte)0x49, (byte)0xc9, (byte)0x29, (byte)0xa9, (byte)0x69, (byte)0xe9,
        (byte)0x19, (byte)0x99, (byte)0x59, (byte)0xd9, (byte)0x39, (byte)0xb9, (byte)0x79, (byte)0xf9,
        (byte)0x05, (byte)0x85, (byte)0x45, (byte)0xc5, (byte)0x25, (byte)0xa5, (byte)0x65, (byte)0xe5,
        (byte)0x15, (byte)0x95, (byte)0x55, (byte)0xd5, (byte)0x35, (byte)0xb5, (byte)0x75, (byte)0xf5,
        (byte)0x0d, (byte)0x8d, (byte)0x4d, (byte)0xcd, (byte)0x2d, (byte)0xad, (byte)0x6d, (byte)0xed,
        (byte)0x1d, (byte)0x9d, (byte)0x5d, (byte)0xdd, (byte)0x3d, (byte)0xbd, (byte)0x7d, (byte)0xfd,
        (byte)0x03, (byte)0x83, (byte)0x43, (byte)0xc3, (byte)0x23, (byte)0xa3, (byte)0x63, (byte)0xe3,
        (byte)0x13, (byte)0x93, (byte)0x53, (byte)0xd3, (byte)0x33, (byte)0xb3, (byte)0x73, (byte)0xf3,
        (byte)0x0b, (byte)0x8b, (byte)0x4b, (byte)0xcb, (byte)0x2b, (byte)0xab, (byte)0x6b, (byte)0xeb,
        (byte)0x1b, (byte)0x9b, (byte)0x5b, (byte)0xdb, (byte)0x3b, (byte)0xbb, (byte)0x7b, (byte)0xfb,
        (byte)0x07, (byte)0x87, (byte)0x47, (byte)0xc7, (byte)0x27, (byte)0xa7, (byte)0x67, (byte)0xe7,
        (byte)0x17, (byte)0x97, (byte)0x57, (byte)0xd7, (byte)0x37, (byte)0xb7, (byte)0x77, (byte)0xf7,
        (byte)0x0f, (byte)0x8f, (byte)0x4f, (byte)0xcf, (byte)0x2f, (byte)0xaf, (byte)0x6f, (byte)0xef,
        (byte)0x1f, (byte)0x9f, (byte)0x5f, (byte)0xdf, (byte)0x3f, (byte)0xbf, (byte)0x7f, (byte)0xff
    };
        
    /** Reverses the bits in the array.
     * @param data data to reverse bits
     */    
    public static void ReverseBits(byte data[]) {
        for (int k = 0; k < data.length; ++k)
            data[k] = bitReversal[(int)data[k] & 0xff];
    }

    ImgCCITT(Image image) {
        super(image);
    }

    /** Creates an Image with CCITT compression.
     *
     * @param width the exact width of the image
     * @param height the exact height of the image
     * @param reverseBits reverses the bits in <code>data<\code>.
     *  Bit 0 is swapped with bit 7 and so on
     * @param typeCCITT the type of compression in <code>data<\code>. It can be
     * CCITTG4, CCITTG31D, CCITTG32D
     * @param parameters parameters associated with this stream. Possible values are
     * CCITT_BLACKIS1, CCITT_ENCODEDBYTEALIGN, CCITT_ENDOFLINE and CCITT_ENDOFBLOCK or a
     * combination of them
     * @param data the image data
     * @throws BadElementException on error
     */

    public ImgCCITT(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data) throws BadElementException{
        super((URL)null);
        if (typeCCITT != CCITTG4 && typeCCITT != CCITTG3_1D && typeCCITT != CCITTG3_2D)
            throw new BadElementException("The CCITT compression type must be CCITTG4, CCITTG3_1D or CCITTG3_2D");
        if (reverseBits)
            ReverseBits(data);
        type = IMGRAW;
        scaledHeight = height;
        setTop(scaledHeight);
        scaledWidth = width;
        setRight(scaledWidth);
        colorspace = parameters;
        bpc = typeCCITT;
        rawData = data;
        plainWidth = width();
        plainHeight = height();
    }
}