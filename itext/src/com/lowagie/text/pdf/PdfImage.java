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
        put(PdfName.TYPE, PdfName.XOBJECT);
        put(PdfName.SUBTYPE, PdfName.IMAGE);
        put(PdfName.WIDTH, new PdfNumber(image.width()));
        put(PdfName.HEIGHT, new PdfNumber(image.height()));
        if (image.isMask() && (image.bpc() == 1 || image.bpc() > 0xff))
            put(PdfName.IMAGEMASK, PdfBoolean.PDFTRUE);
        if (maskRef != null) {
            if (image.isSmask())
                put(PdfName.SMASK, maskRef);
            else
                put(PdfName.MASK, maskRef);
        }
        if (image.isMask() && image.isInvertMask())
            put(PdfName.DECODE, new PdfLiteral("[1 0]"));
        if (image.isInterpolation())
            put(PdfName.INTERPOLATE, PdfBoolean.PDFTRUE);
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
                    put(PdfName.MASK, new PdfLiteral(s));
                }
                bytes = image.rawData();
                put(PdfName.LENGTH, new PdfNumber(bytes.length));
                int bpc = image.bpc();
                if (bpc > 0xff) {
                    if (!image.isMask())
                        put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(1));
                    put(PdfName.FILTER, PdfName.CCITTFAXDECODE);
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
                    put(PdfName.DECODEPARMS, decodeparms);
                }
                else {
                    switch(colorspace) {
                        case 1:
                            put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                            if (image.isInverted())
                                put(PdfName.DECODE, new PdfLiteral("[1 0]"));
                            break;
                        case 3:
                            put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                            if (image.isInverted())
                                put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0]"));
                            break;
                        case 4:
                        default:
                            put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                            if (image.isInverted())
                                put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0 1 0]"));
                    }
                    PdfDictionary additional = image.getAdditional();
                    if (additional != null)
                        putAll(additional);
                    if (image.isMask() && (image.bpc() == 1 || image.bpc() > 8))
                        remove(PdfName.COLORSPACE);
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(image.bpc()));
                    if (image.isDeflated())
                        put(PdfName.FILTER, PdfName.FLATEDECODE);
                    else {
                        flateCompress();
                    }
                }
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
            int i = 0;
            switch(image.type()) {
                case Image.JPEG:
                    put(PdfName.FILTER, PdfName.DCTDECODE);
                    switch(image.colorspace()) {
                        case 1:
                            put(PdfName.COLORSPACE, PdfName.DEVICEGRAY);
                            break;
                        case 3:
                            put(PdfName.COLORSPACE, PdfName.DEVICERGB);
                            break;
                        default:
                            put(PdfName.COLORSPACE, PdfName.DEVICECMYK);
                            if (image.isInverted()) {
                                put(PdfName.DECODE, new PdfLiteral("[1 0 1 0 1 0 1 0]"));
                            }
                    }
                    put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
                    if (image.rawData() != null){
                        bytes = image.rawData();
                        put(PdfName.LENGTH, new PdfNumber(bytes.length));
                        return;
                    }
                    streamBytes = new ByteArrayOutputStream();
                    transferBytes(is, streamBytes, -1);
                    break;
                default:
                    throw new BadPdfFormatException(errorID + " is an unknown Image format.");
            }
            put(PdfName.LENGTH, new PdfNumber(streamBytes.size()));
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
                    // empty on purpose
                }
            }
        }
    }
    
    /**
     * Returns the <CODE>PdfName</CODE> of the image.
     *
     * @return		the name
     */
    
    public PdfName name() {
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
    
    protected void importAll(PdfImage dup) {
        name = dup.name;
        compressed = dup.compressed;
        streamBytes = dup.streamBytes;
        bytes = dup.bytes;
        hashMap = dup.hashMap;
    }
}
