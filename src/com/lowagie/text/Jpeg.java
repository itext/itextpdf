/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
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

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An <CODE>Jpeg</CODE> is the representation of a graphic element (JPEG)
 * that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 * @see		Gif
 * @see		Png
 */

public class Jpeg extends Image implements Element {
    
    // public final static membervariables
    
    /** This is a type of marker. */
    public static final int NOT_A_MARKER = -1;
    
    /** This is a type of marker. */
    public static final int VALID_MARKER = 0;
    
    /** Acceptable Jpeg markers. */
    public static final int[] VALID_MARKERS = {0xC0, 0xC1, 0xC2};
    
    /** This is a type of marker. */
    public static final int UNSUPPORTED_MARKER = 1;
    
    /** Unsupported Jpeg markers. */
    public static final int[] UNSUPPORTED_MARKERS = {0xC3, 0xC5, 0xC6, 0xC7, 0xC8, 0xC9, 0xCA, 0xCB, 0xCD, 0xCE, 0xCF};
    
    /** This is a type of marker. */
    public static final int NOPARAM_MARKER = 2;
    
    /** Jpeg markers without additional parameters. */
    public static final int[] NOPARAM_MARKERS = {0xD0, 0xD1, 0xD2, 0xD3, 0xD4, 0xD5, 0xD6, 0xD7, 0xD8, 0x01};
    
    public static final int M_APP0 = 0xE0;
    public static final int M_APPE = 0xEE;
    
    public static final byte JFIF_ID[] = {0x4A, 0x46, 0x49, 0x46, 0x00};
    // Constructors
    
    /**
     * Constructs a <CODE>Jpeg</CODE>-object, using an <VAR>url</VAR>.
     *
     * @param		url			the <CODE>URL</CODE> where the image can be found
     */
    
    public Jpeg(URL url) throws BadElementException, IOException {
        super(url);
        processParameters();
    }
    
    /**
     * Constructs a <CODE>Jpeg</CODE>-object, using an <VAR>url</VAR>.
     *
     * @param		url			the <CODE>URL</CODE> where the image can be found.
     * @deprecated	use Image.getInstance(...) to create an Image
     */
    
    public Jpeg(URL url, float width, float height) throws BadElementException, IOException {
        this(url);
        scaledWidth = width;
        scaledHeight = height;
    }
    
    /**
     * Constructs a <CODE>Jpeg</CODE>-object, using a <VAR>filename</VAR>.
     *
     * @param		filename	a <CODE>String</CODE>-representation of the file that contains the Image.
     * @deprecated	use Image.getInstance(...) to create an Image
     */
    
    public Jpeg(String filename) throws BadElementException, MalformedURLException, IOException {
        this(Image.toURL(filename));
    }
    
    /**
     * Constructs a <CODE>Jpeg</CODE>-object, using a <VAR>filename</VAR>.
     *
     * @param		filename	a <CODE>String</CODE>-representation of the file that contains the Image.
     * @deprecated	use Image.getInstance(...) to create an Image
     */
    
    public Jpeg(String filename, float width, float height) throws BadElementException, MalformedURLException, IOException {
        this(Image.toURL(filename), width, height);
    }
    
    /**
     * Constructs a <CODE>Jpeg</CODE>-object from memory.
     *
     * @param		img		the memory image
     */
    
    public Jpeg(byte[] img) throws BadElementException, IOException {
        super((URL)null);
        rawData = img;
        processParameters();
    }
    
    /**
     * Constructs a <CODE>Jpeg</CODE>-object from memory.
     *
     * @param		img			the memory image.
     * @param		width		the width you want the image to have
     * @param		height		the height you want the image to have
     */
    
    public Jpeg(byte[] img, float width, float height) throws BadElementException, IOException {
        this(img);
        scaledWidth = width;
        scaledHeight = height;
    }
    
    // private static methods
    
    /**
     * Reads a short from the <CODE>InputStream</CODE>.
     *
     * @param	is		the <CODE>InputStream</CODE>
     * @return	an int
     */
    
    private static final int getShort(InputStream is) throws IOException {
        return (is.read() << 8) + is.read();
    }
    
    /**
     * Returns a type of marker.
     *
     * @param	an int
     * @return	a type: <VAR>VALID_MARKER</CODE>, <VAR>UNSUPPORTED_MARKER</VAR> or <VAR>NOPARAM_MARKER</VAR>
     */
    
    private static final int marker(int marker) {
        for (int i = 0; i < VALID_MARKERS.length; i++) {
            if (marker == VALID_MARKERS[i]) {
                return VALID_MARKER;
            }
        }
        for (int i = 0; i < NOPARAM_MARKERS.length; i++) {
            if (marker == NOPARAM_MARKERS[i]) {
                return NOPARAM_MARKER;
            }
        }
        for (int i = 0; i < UNSUPPORTED_MARKERS.length; i++) {
            if (marker == UNSUPPORTED_MARKERS[i]) {
                return UNSUPPORTED_MARKER;
            }
        }
        return NOT_A_MARKER;
    }
    
    // private methods
    
    /**
     * This method checks if the image is a valid JPEG and processes some parameters.
     */
    
    private final void processParameters() throws BadElementException, IOException {
        type = JPEG;
        InputStream is = null;
        try {
            String errorID;
            if (rawData == null){
                is = url.openStream();
                errorID = url.toString();
            }
            else{
                is = new java.io.ByteArrayInputStream(rawData);
                errorID = "Byte array";
            }
            if (is.read() != 0xFF || is.read() != 0xD8)	{
                throw new BadElementException(errorID + " is not a valid JPEG-file.");
            }
            boolean firstPass = true;
            int len;
            while (true) {
                if (is.read() == 0xFF) {
                    int marker = is.read();
                    if (firstPass && marker == M_APP0) {
                        firstPass = false;
                        len = getShort(is);
                        if (len < 16) {
                            skip(is, len - 2);
                            continue;
                        }
                        byte bcomp[] = new byte[JFIF_ID.length];
                        int r = is.read(bcomp);
                        if (r != bcomp.length)
                            throw new BadElementException(errorID + " corrupted JFIF marker.");
                        boolean found = true;
                        for (int k = 0; k < bcomp.length; ++k) {
                            if (bcomp[k] != JFIF_ID[k]) {
                                found = false;
                                break;
                            }
                        }
                        if (!found) {
                            skip(is, len - 2 - bcomp.length);
                            continue;
                        }
                        skip(is, 2);
                        int units = is.read();
                        int dx = getShort(is);
                        int dy = getShort(is);
                        if (units == 1) {
                            dpiX = dx;
                            dpiY = dy;
                        }
                        else if (units == 2) {
                            dpiX = (int)((float)dx * 2.54f + 0.5f);
                            dpiY = (int)((float)dy * 2.54f + 0.5f);
                        }
                        skip(is, len - 2 - bcomp.length - 7);
                        continue;
                    }
                    if (marker == M_APPE) {
                        len = getShort(is);
                        byte[] byteappe = new byte[len];
                        for (int k = 0; k < len; ++k) {
                            byteappe[k] = (byte)is.read();
                        }
                        if (byteappe.length > 12) {
                            String appe = new String(byteappe, 0, 5, "ISO-8859-1");
                            if (appe.equals("Adobe")) {
                                invert = true;
                            }
                        }
                    }
                    firstPass = false;
                    int markertype = marker(marker);
                    if (markertype == VALID_MARKER) {
                        skip(is, 2);
                        if (is.read() != 0x08) {
                            throw new BadElementException(errorID + " must have 8 bits per component.");
                        }
                        scaledHeight = getShort(is);
                        setTop(scaledHeight);
                        scaledWidth = getShort(is);
                        setRight(scaledWidth);
                        colorspace = is.read();
                        break;
                    }
                    else if (markertype == UNSUPPORTED_MARKER) {
                        throw new BadElementException(errorID + ": unsupported JPEG marker: " + marker);
                    }
                    else if (markertype != NOPARAM_MARKER) {
                        skip(is, getShort(is) - 2);
                    }
                }
            }
        }
        finally {
            if (is != null) {
                is.close();
            }
            plainWidth = width();
            plainHeight = height();
        }
    }
}
