/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie and Paulo Soares.
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
 */

package com.lowagie.text;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An <CODE>Png</CODE> is the representation of a graphic element (PNG)
 * that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 * @see		Gif
 * @see		Jpeg
 *
 * @author  bruno@lowagie.com
 */

public class Png extends Image implements Element {
    
    // public final static membervariables
    
/** Some PNG specific values. */
    public static final int[] PNGID = {137, 80, 78, 71, 13, 10, 26, 10};
    
/** A PNG marker. */
    public static final String IHDR = "IHDR";
    
/** A PNG marker. */
    public static final String PLTE = "PLTE";
    
/** A PNG marker. */
    public static final String IDAT = "IDAT";
    
/** A PNG marker. */
    public static final String IEND = "IEND";
    
/** A PNG marker. */
    public static final String tRNS = "tRNS";
    
/** A PNG marker. */
    public static final String pHYs = "pHYs";
    
    // Constructors
    
/**
 * Constructs a <CODE>Png</CODE>-object, using an <VAR>url</VAR>.
 *
 * @param		url			the <CODE>URL</CODE> where the image can be found.
 */
    
    public Png(URL url) throws BadElementException, IOException {
        super(url);
        processParameters();
    }
    
/**
 * Constructs a <CODE>Png</CODE>-object, using an <VAR>url</VAR>.
 *
 * @param		url			the <CODE>URL</CODE> where the image can be found.
 * @deprecated	use Image.getInstance(...) to create an Image
 */
    
    public Png(URL url, float width, float height) throws BadElementException, IOException {
        this(url);
        scaledWidth = width;
        scaledHeight = height;
    }
    
/**
 * Constructs a <CODE>Png</CODE>-object, using a <VAR>filename</VAR>.
 *
 * @param		filename	a <CODE>String</CODE>-representation of the file that contains the Image.
 * @deprecated	use Image.getInstance(...) to create an Image
 */
    
    public Png(String filename) throws MalformedURLException, BadElementException, IOException {
        this(Image.toURL(filename));
    }
    
/**
 * Constructs a <CODE>Png</CODE>-object, using a <VAR>filename</VAR>.
 *
 * @param		filename	a <CODE>String</CODE>-representation of the file that contains the Image.
 * @deprecated	use Image.getInstance(...) to create an Image
 */
    
    public Png(String filename, float width, float height) throws MalformedURLException, BadElementException, IOException {
        this(Image.toURL(filename), width, height);
    }
    
/**
 * Constructs a <CODE>Png</CODE>-object from memory.
 *
 * @param		img			the memory image.
 */
    
    public Png(byte[] img) throws BadElementException, IOException {
        super((URL)null);
        rawData = img;
        processParameters();
    }
    
/**
 * Constructs a <CODE>Png</CODE>-object from memory.
 *
 * @param		img			the memory image.
 */
    
    public Png(byte[] img, float width, float height) throws BadElementException, IOException {
        this(img);
        scaledWidth = width;
        scaledHeight = height;
    }
    
    // private methods
    
/**
 * Gets an <CODE>int</CODE> from an <CODE>InputStream</CODE>.
 *
 * @param		an <CODE>InputStream</CODE>
 * @return		the value of an <CODE>int</CODE>
 */
    
    public static final int getInt(InputStream is) throws IOException {
        return (is.read() << 24) + (is.read() << 16) + (is.read() << 8) + is.read();
    }
    
/**
 * Gets a <CODE>word</CODE> from an <CODE>InputStream</CODE>.
 *
 * @param		an <CODE>InputStream</CODE>
 * @return		the value of an <CODE>int</CODE>
 */
    
    public static final int getWord(InputStream is) throws IOException {
        return (is.read() << 8) + is.read();
    }
    
/**
 * Gets a <CODE>String</CODE> from an <CODE>InputStream</CODE>.
 *
 * @param		an <CODE>InputStream</CODE>
 * @return		the value of an <CODE>int</CODE>
 */
    
    public static final String getString(InputStream is) throws IOException {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            buf.append((char)is.read());
        }
        return buf.toString();
    }
    
    // private methods
    
/**
 * This method checks if the image is a valid PNG and processes some parameters.
 *
 * @author	Paulo Soares
 */
    
    private final void processParameters() throws BadElementException, IOException {
        type = PNG;
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
            for (int i = 0; i < PNGID.length; i++) {
                if (PNGID[i] != is.read())	{
                    throw new BadElementException(errorID + " is not a valid PNG-file.");
                }
            }
            while(true) {
                int len = getInt(is);
                String id = getString(is);
                if (IHDR.equals(id)) {
                    scaledWidth = getInt(is);
                    setRight(scaledWidth);
                    scaledHeight = getInt(is);
                    setTop(scaledHeight);
                    skip(is, len + 4 - 8);
                    continue;
                }
                if (pHYs.equals(id)) {
                    int dx = getInt(is);
                    int dy = getInt(is);
                    int unit = is.read();
                    if (unit == 1) {
                        dpiX = (int)((float)dx * 0.0254f + 0.5f);
                        dpiY = (int)((float)dy * 0.0254f + 0.5f);
                    }
                    skip(is, len + 4 - 9);
                    continue;
                }
                if (IDAT.equals(id) || IEND.equals(id)) {
                    break;
                }
                skip(is, len + 4);
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
