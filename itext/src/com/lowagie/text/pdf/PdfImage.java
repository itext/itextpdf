/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
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
 *
 * REMARK:
 * LZW/GIF is covered by a software patent which is owned by Unisys Corporation.
 * Unisys refuses to license this patent for PDF-related use in software
 * even when this software is released for free and may be freely distributed.
 * HOWEVER:
 * This library doesn't compress or decompress data using the LZW
 * algorithm, nor does it create or visualize GIF-images in any way;
 * it only copies parts of an existing GIF file into a PDF file.
 *
 * More information about the GIF format can be found in the following documents:
 * * GRAPHICS INTERCHANGE FORMAT(sm) Version 89a
 *   (c)1987,1988,1989,1990 Copyright CompuServe Incorporated. Columbus, Ohio
 * * LZW and GIF explained
 *   Steve Blackstock
 * * http://mistress.informatik.unibw-muenchen.de/
 *   very special thanks to klee@informatik.unibw-muenchen.de for the algorithm
 *   to extract the LZW data from a GIF.
 */

package com.lowagie.text.pdf;

import com.lowagie.text.Image;
import com.lowagie.text.Png;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * <CODE>PdfImage</CODE> is a <CODE>PdfStream</CODE> containing an image-<CODE>Dictionary</CODE> and -stream.
 */

class PdfImage extends PdfStream {

    static final int TRANSFERSIZE = 4096;
    // membervariables
    
    /** This is the <CODE>PdfName</CODE> of the image. */
    protected PdfName name = null;
    
    // constructor
    
    /**
     * Constructs a <CODE>PdfImage</CODE>-object.
     *
     * @param image the <CODE>Image</CODE>-object
     * @param name the <CODE>PdfName</CODE> for this image
     * @throws BadPdfFormatException on error
     */
    
    public PdfImage(Image image, String name, PdfIndirectReference maskRef) throws BadPdfFormatException {
        super();
        this.name = new PdfName(name);
        dictionary.put(PdfName.TYPE, PdfName.XOBJECT);
        dictionary.put(PdfName.SUBTYPE, PdfName.IMAGE);
        dictionary.put(PdfName.NAME, this.name);
        dictionary.put(PdfName.WIDTH, new PdfNumber(image.width()));
        dictionary.put(PdfName.HEIGHT, new PdfNumber(image.height()));
        if (maskRef != null)
            dictionary.put(PdfName.MASK, maskRef);
        if (image.isMask() && image.isInvertMask())
            dictionary.put(PdfName.DECODE, new PdfLiteral("[1 0]"));
        if (image.isInterpolation())
            dictionary.put(PdfName.INTERPOLATE, PdfBoolean.PDFTRUE);
        InputStream is = null;
        try {
            
            // Raw Image data
            if (image.isImgRaw()) {
                // will also have the CCITT parameters
                int colorspace = image.colorspace();
                int transparency[] = image.getTransparency();
                if (transparency != null && !image.isMask() && maskRef == null) {
                    String s = "[";
                    for (int k = 0; k < transparency.length; ++k)
                        s += transparency[k] + " ";
                    s += "]";
                    dictionary.put(PdfName.MASK, new PdfLiteral(s));
                }
                bytes = image.rawData();
                dictionary.put(PdfName.LENGTH, new PdfNumber(bytes.length));
                int bpc = image.bpc();
                if (bpc > 0xff) {
                    if (!image.isMask())
                        dictionary.put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                    dictionary.put(PdfName.BITSPERCOMPONENT, new PdfNumber(1));
                    dictionary.put(PdfName.FILTER, PdfName.CCITTFAXDECODE);
                    int k = bpc - Image.CCITTG3_1D;
                    PdfDictionary decodeparms = new PdfDictionary();
                    if (k != 0)
                        decodeparms.put(PdfName.K, new PdfNumber(k));
                    if ((colorspace & Image.CCITT_BLACKIS1) != 0)
                        decodeparms.put(PdfName.BLACKIS1, PdfBoolean.PDFTRUE);
                    if ((colorspace & Image.CCITT_ENCODEDBYTEALIGN) != 0)
                        decodeparms.put(PdfName.ENCODEDBYTEALIGN, PdfBoolean.PDFTRUE);
                    if ((colorspace & Image.CCITT_ENDOFLINE) != 0)
                        decodeparms.put(PdfName.ENDOFLINE, PdfBoolean.PDFTRUE);
                    if ((colorspace & Image.CCITT_ENDOFBLOCK) != 0)
                        decodeparms.put(PdfName.ENDOFBLOCK, PdfBoolean.PDFFALSE);
                    decodeparms.put(PdfName.COLUMNS, new PdfNumber(image.width()));
                    decodeparms.put(PdfName.ROWS, new PdfNumber(image.height()));
                    dictionary.put(PdfName.DECODEPARMS, decodeparms);
                }
                else {
                    if (!image.isMask()) {
                        switch(colorspace) {
                            case 1:
                                dictionary.put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                                break;
                            case 3:
                                dictionary.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                                break;
                            case 4:
                            default:
                                dictionary.put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                        }
                    }
                    dictionary.put(PdfName.BITSPERCOMPONENT, new PdfNumber(image.bpc()));
                    try {
                        flateCompress();
                    }
                    catch(PdfException pe) {
                    }
                }
                if (image.isMask())
                    dictionary.put(PdfName.IMAGEMASK, PdfBoolean.PDFTRUE);
                return;
            }
            
            // GIF, JPEG or PNG
            String errorID;
            if (image.rawData() == null){
                is = image.url().openStream();
                errorID = image.url().toString();
            }
            else{
                is = new java.io.ByteArrayInputStream(image.rawData());
                errorID = "Byte array";
            }
            streamBytes = new ByteArrayOutputStream();
            int i = 0;
            switch(image.type()) {
                case Image.PNG:
                    dictionary.put(PdfName.FILTER, PdfName.FLATEDECODE);
                    for (int j = 0; j < Png.PNGID.length; j++) {
                        if (Png.PNGID[j] != is.read()) {
                            throw new BadPdfFormatException(errorID + " is not a PNG file.");
                        }
                    }
                    int colorType = 0;
                    while (true) {
                        int len = Png.getInt(is);
                        String marker = Png.getString(is);
                        if (Png.IDAT.equals(marker)) {
                            transferBytes(is, streamBytes, len);
                            Png.getInt(is);
                        }
                        else if (Png.tRNS.equals(marker) && !image.isMask() && maskRef == null) {
                            switch (colorType) {
                                case 0:
                                    if (len >= 2) {
                                        len -= 2;
                                        int gray = Png.getWord(is);
                                        dictionary.put(PdfName.MASK, new PdfLiteral("["+gray+" "+gray+"]"));
                                    }
                                    break;
                                case 2:
                                    if (len >= 6) {
                                        len -= 6;
                                        int red = Png.getWord(is);
                                        int green = Png.getWord(is);
                                        int blue = Png.getWord(is);
                                        dictionary.put(PdfName.MASK, new PdfLiteral("["+red+" "+red+" "+green+" "+green+" "+blue+" "+blue+"]"));
                                    }
                                    break;
                                case 3:
                                    if (len > 0) {
                                        int idx = 0;
                                        while (len-- != 0) {
                                            if (is.read() == 0) {
                                                dictionary.put(PdfName.MASK, new PdfLiteral("["+idx+" "+idx+"]"));
                                                break;
                                            }
                                            ++idx;
                                        }
                                    }
                                    break;
                            }
                            Image.skip(is, len + 4);
                        }
                        else if (Png.IHDR.equals(marker)) {
                            int w = Png.getInt(is);
                            int h = Png.getInt(is);
                            
                            int bitDepth = is.read();
                            if (bitDepth == 16) {
                                throw new BadPdfFormatException(errorID + " Bit depth 16 is not suported.");
                            }
                            dictionary.put(PdfName.BITSPERCOMPONENT, new PdfNumber(bitDepth));
                            
                            colorType = is.read();
                            if (! (colorType == 0 || colorType == 2 || colorType == 3)) {
                                throw new BadPdfFormatException(errorID + " Colortype " + colorType + " is not suported.");
                            }
                            if (colorType == 0) {
                                dictionary.put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                            }
                            else if (colorType == 2) {
                                dictionary.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                            }
                            
                            int compressionMethod = is.read();
                            int filterMethod = is.read();
                            
                            int interlaceMethod = is.read();
                            if (interlaceMethod != 0) {
                                throw new BadPdfFormatException(errorID + " Interlace method " + interlaceMethod + " is not suported.");
                            }
                            
                            PdfDictionary decodeparms = new PdfDictionary();
                            decodeparms.put(PdfName.BITSPERCOMPONENT, new PdfNumber(bitDepth));
                            decodeparms.put(PdfName.PREDICTOR, new PdfNumber(15));
                            decodeparms.put(PdfName.COLUMNS, new PdfNumber(w));
                            decodeparms.put(PdfName.COLORS, new PdfNumber((colorType == 2) ? 3: 1));
                            dictionary.put(PdfName.DECODEPARMS, decodeparms);
                            
                            Png.getInt(is);
                        }
                        else if (Png.PLTE.equals(marker)) {
                            if (colorType == 3) {
                                PdfArray colorspace = new PdfArray();
                                colorspace.add(PdfName.INDEXED);
                                colorspace.add(PdfName.DEVICERGB);
                                colorspace.add(new PdfNumber(len / 3 - 1));
                                ByteBuffer colortable = new ByteBuffer();
                                while ((len--) > 0) {
                                    colortable.append_i(is.read());
                                }
                                colorspace.add(new PdfStringLiteral(colortable.toByteArray()));
                                dictionary.put(PdfName.COLORSPACE, colorspace);
                                Png.getInt(is);
                            }
                            else {
                                Image.skip(is, len + 4);
                            }
                        }
                        else if (Png.IEND.equals(marker)) {
                            break;
                        }
                        else {
                            for (int j = -4; j < len; j++) {
                                is.read();
                            }
                        }
                        
                    }
                    break;
                case Image.JPEG:
                    dictionary.put(PdfName.FILTER, PdfName.DCTDECODE);
                    switch(image.colorspace()) {
                        case 1:
                            dictionary.put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                            break;
                        case 3:
                            dictionary.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                            break;
                        default:
                            dictionary.put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                    }
                    dictionary.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                    if (image.rawData() != null){
                        bytes = image.rawData();
                        streamBytes = null;
                        dictionary.put(PdfName.LENGTH, new PdfNumber(bytes.length));
                        return;
                    }
                    transferBytes(is, streamBytes, -1);
                    break;
                case Image.GIF:
                    // HEADER + INFO + COLORTABLE
                    
                    // Byte 0-2: header
                    // checks if the file really is a GIF-file
                    if (is.read() != 'G' || is.read() != 'I' || is.read() != 'F') {
                        throw new BadPdfFormatException(errorID + " is not a GIF-file (GIF header not found).");
                    }
                    
                    dictionary.put(PdfName.FILTER, PdfName.LZWDECODE);
                    
                    PdfDictionary decodeparms = new PdfDictionary();
                    decodeparms.put(PdfName.EARLYCHANGE, new PdfNumber(0));
                    dictionary.put(PdfName.DECODEPARMS, decodeparms);
                    
                    PdfArray colorspace = new PdfArray();
                    colorspace.add(PdfName.INDEXED);
                    colorspace.add(PdfName.DEVICERGB);
                    // Byte 3-5: version
                    // Byte 6-7: logical screen width
                    // Byte 8-9: logical screen height
                    // Byte 10: Packed Fields
                    for (int j = 0; j < 8; j++) {
                        i = is.read();
                    }
                    // Byte 10: bit 1: Global Color Table Flag
                    if ((i & 0x80) == 0) {
                        throw new BadPdfFormatException(errorID + " is not a supported GIF-file (there is no global color table present).");
                    }
                    // Byte 10: bit 6-8: Size of Global Color Table
                    int nColors = 1 << ((i & 7) + 1);
                    colorspace.add(new PdfNumber(nColors - 1));
                    // Byte 11: Background color index
                    is.read();
                    // Byte 12: Pixel aspect ratio
                    is.read();
                    // Byte 13-...: Global color table
                    ByteBuffer colortable = new ByteBuffer();
                    for (int j = 0; j < nColors; j++) {
                        colortable.append_i(is.read());	// red
                        colortable.append_i(is.read());	// green
                        colortable.append_i(is.read());	// blue
                    }
                    colorspace.add(new PdfStringLiteral(colortable.toByteArray()));
                    dictionary.put(PdfName.COLORSPACE, colorspace);
                    dictionary.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                    
                    // IMAGE DESCRIPTOR
                    
                    // Byte 0: Image separator
                    // only simple gif files with image immediate following global color table are supported
                    // 0x2c is a fixed value for the image separator
                    if (is.read() != 0x2c) {
                        throw new BadPdfFormatException(errorID + " is not a supported GIF-file (the image separator '0x2c' is not found after reading the color table).");
                    }
                    // Byte 1-2: Image Left Position
                    // Byte 3-4: Image Top Position
                    // Byte 5-6: Image Width
                    // Byte 7-8: Image Height
                    // ignore position and size
                    for (int j = 0; j < 8; j++) {
                        is.read();
                    }
                    // Byte 9: Packed Fields
                    // Byte 9: bit 1: Local Color Table Flag
                    // Byte 9: bit 2: Interlace Flag
                    if ((is.read() & 0xc0) > 0) {
                        throw new BadPdfFormatException(errorID + " is not a supported GIF-file (interlaced gifs or gifs using local color table can't be inserted).");
                    }
                    
                    // Byte 10: LZW initial code
                    if (is.read() != 0x08) {
                        throw new BadPdfFormatException(errorID + " is not a supported GIF-file (initial LZW code not supported).");
                    }
                    // Read the Image Data
                    int code = 0;
                    int codelength = 9;
                    int tablelength = 257;
                    int bitsread = 0;
                    int bitstowrite = 0;
                    int bitsdone = 0;
                    int bitsleft = 23;
                    int bytesdone = 0;
                    int bytesread = 0;
                    int byteswritten = 0;
                    // read the size of the first Data Block
                    int size = is.read();
                    // Check if there is any data in the GIF
                    if (size < 1) {
                        throw new BadPdfFormatException(errorID + " is not a supported GIF-file. (no image data found).");
                    }
                    // if possible, we read the first 24 bits of data
                    size--; bytesread++; bitsread = is.read();
                    if (size > 0) {
                        size--; bytesread++; bitsread += (is.read() << 8);
                        if (size > 0) {
                            size--; bytesread++; bitsread += (is.read() << 16);
                        }
                    }
                    while (bytesread > byteswritten) {
                        tablelength++;
                        // we extract a code with length=codelength
                        code = (bitsread >> bitsdone) & ((1 << codelength) - 1);
                        // we delete the bytesdone in bitsread and append the next byte(s)
                        bytesdone = (bitsdone + codelength) / 8;
                        bitsdone = (bitsdone + codelength) % 8;
                        while (bytesdone > 0) {
                            bytesdone--;
                            bitsread = (bitsread >> 8);
                            if (size > 0) {
                                size--; bytesread++; bitsread += (is.read() << 16);
                            }
                            else {
                                size = is.read();
                                if (size > 0) {
                                    size--; bytesread++; bitsread += (is.read() << 16);
                                }
                            }
                        }
                        // we package all the bits that are done into bytes and write them to the stream
                        bitstowrite += (code << (bitsleft - codelength + 1));
                        bitsleft -= codelength;
                        while (bitsleft < 16) {
                            streamBytes.write(bitstowrite >> 16);
                            byteswritten++;
                            bitstowrite = (bitstowrite & 0xFFFF) << 8;
                            bitsleft += 8;
                        }
                        if (code == 256) {
                            codelength = 9;
                            tablelength = 257;
                        }
                        if (code == 257) {
                            break;
                        }
                        if (tablelength == (1 << codelength)) {
                            codelength++;
                        }
                    }
                    if (bytesread - byteswritten > 2) {
                        throw new BadPdfFormatException(errorID + " is not a supported GIF-file (unexpected end of data block).");
                    }
                    break;
                default:
                    throw new BadPdfFormatException(errorID + " is an unknown Image format.");
            }
            dictionary.put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
        }
        catch(IOException ioe) {
            throw new BadPdfFormatException(ioe.getMessage());
        }
        finally {
            if (is != null) {
                try{
                    is.close();
                }
                catch (Exception ee) {
                }
            }
        }
    }
    
    /**
     * Returns the <CODE>PdfName</CODE> of the image.
     *
     * @return		the name
     */
    
    public final PdfName name() {
        return name;
    }
    
    static void transferBytes(InputStream in, OutputStream out, int len) throws IOException {
        byte buffer[] = new byte[TRANSFERSIZE];
        if (len < 0)
            len = 0x7ffffff;
        int size;
        while (len != 0) {
            size = in.read(buffer, 0, Math.min(len, TRANSFERSIZE));
            if (size < 0)
                return;
            out.write(buffer, 0, size);
            len -= size;
        }
    }
}