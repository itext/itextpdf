/*
 * $Id$
 * $Name$
 *
 * Copyright 2000, 2001, 2002 by Bruno Lowagie.
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

import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An <CODE>Gif</CODE> is the representation of a graphic element (GIF)
 * that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 * @see		Jpeg
 * @see		Png
 */

public class Gif extends Image implements Element {
    
    // Constructors
    Gif(Image image) {
        super(image);
    }
    
/**
 * Constructs a <CODE>Gif</CODE>-object, using an <VAR>url</VAR>.
 *
 * @param		url			the <CODE>URL</CODE> where the image can be found.
 */
    
    public Gif(URL url) throws BadElementException, IOException {
        super(url);
        processParameters();
    }
    
/**
 * Constructs a <CODE>Gif</CODE>-object, using an <VAR>url</VAR>.
 *
 * @param		url			the <CODE>URL</CODE> where the image can be found.
 * @param		width		the width you want the image to have
 * @param		height		the height you want the image to have.
 *
 * @deprecated	use Image.getInstance(...) to create an Image
 */
    
    public Gif(URL url, float width, float height) throws BadElementException, IOException {
        this(url);
        scaledWidth = width;
        scaledHeight = height;
    }
    
/**
 * Constructs a <CODE>Gif</CODE>-object, using a <VAR>filename</VAR>.
 *
 * @param		filename	a <CODE>String</CODE>-representation of the file that contains the Image.
 * @deprecated	use Image.getInstance(...) to create an Image
 */
    
    public Gif(String filename) throws BadElementException, MalformedURLException, IOException {
        this(Image.toURL(filename));
    }
    
/**
 * Constructs a <CODE>Gif</CODE>-object, using a <VAR>filename</VAR>.
 *
 * @param		filename	a <CODE>String</CODE>-representation of the file that contains the Image.
 * @param		width		the width you want the image to have
 * @param		height		the height you want the image to have.
 * @deprecated	use Image.getInstance(...) to create an Image
 */
    
    public Gif(String filename, float width, float height)  throws BadElementException, MalformedURLException, IOException {
        this(Image.toURL(filename), width, height);
    }
    
/**
 * Constructs a <CODE>Gif</CODE>-object from memory.
 *
 * @param		img		the memory image
 */
    
    public Gif(byte[] img) throws BadElementException, IOException {
        super((URL)null);
        rawData = img;
        processParameters();
    }
    
/**
 * Constructs a <CODE>Gif</CODE>-object from memory.
 *
 * @param		img			the memory image
 * @param		width		the width you want the image to have
 * @param		height		the height you want the image to have
 */
    
    public Gif(byte[] img, float width, float height) throws BadElementException, IOException {
        this(img);
        scaledWidth = width;
        scaledHeight = height;
    }
    
    // private methods
    
/**
 * This method checks if the image is a valid GIF and processes some parameters.
 */
    
    private void processParameters() throws BadElementException, IOException {
        type = GIF;
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
            if (is.read() != 'G' || is.read() != 'I' || is.read() != 'F')	{
                throw new BadElementException(errorID + " is not a valid GIF-file.");
            }
            skip(is, 3);
            scaledWidth = is.read() + (is.read() << 8);
            setRight(scaledWidth);
            scaledHeight = is.read() + (is.read() << 8);
            setTop(scaledHeight);
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
