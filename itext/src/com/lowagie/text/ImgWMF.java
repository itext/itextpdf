/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 * Very special thanks to Paulo Soares who wrote the code to retrieve the width
 * and the height of a Jpeg and added checking methods to see if a Jpeg is valid.
 */

package com.lowagie.text;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import com.lowagie.text.pdf.wmf.InputMeta;
import com.lowagie.text.pdf.wmf.MetaDo;
import com.lowagie.text.pdf.*;

/**
 * An <CODE>ImgWMF</CODE> is the representation of a windows metafile
 * that has to be inserted into the document
 *
 * @see		Element
 * @see		Image
 * @see		Gif
 * @see		Png
 *
 * @author  Paulo Soares (psoares@consiste.pt)
 */

public class ImgWMF extends Image implements Element {
    
    // Constructors
    
    /**
     * Constructs an <CODE>ImgWMF</CODE>-object, using an <VAR>url</VAR>.
     *
     * @param url the <CODE>URL</CODE> where the image can be found
     * @throws BadElementException on error
     * @throws IOException on error
     */
    
    public ImgWMF(URL url) throws BadElementException, IOException {
        super(url);
        processParameters();
    }
    
    /**
     * Constructs an <CODE>ImgWMF</CODE>-object, using a <VAR>filename</VAR>.
     *
     * @param filename a <CODE>String</CODE>-representation of the file that contains the image.
     * @throws BadElementException on error
     * @throws MalformedURLException on error
     * @throws IOException on error
     */
    
    public ImgWMF(String filename) throws BadElementException, MalformedURLException, IOException {
        this(Image.toURL(filename));
    }
    
    /**
     * Constructs an <CODE>ImgWMF</CODE>-object from memory.
     *
     * @param img the memory image
     * @throws BadElementException on error
     * @throws IOException on error
     */
    
    public ImgWMF(byte[] img) throws BadElementException, IOException {
        super((URL)null);
        rawData = img;
        processParameters();
    }
    
/**
 * This method checks if the image is a valid WMF and processes some parameters.
 */
    
    private final void processParameters() throws BadElementException, IOException {
        type = IMGTEMPLATE;
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
            InputMeta in = new InputMeta(is);
            if (in.readInt() != 0x9AC6CDD7)	{
                throw new BadElementException(errorID + " is not a valid placeable windows metafile.");
            }
            in.readWord();
            int left = in.readShort();
            int top = in.readShort();
            int right = in.readShort();
            int bottom = in.readShort();
            int inch = in.readWord();
            dpiX = 72;
            dpiY = 72;
            scaledHeight = (float)(bottom - top) / inch * 72f;
            setTop(scaledHeight);
            scaledWidth = (float)(right - left) / inch * 72f;
            setRight(scaledWidth);
        }
        finally {
            if (is != null) {
                is.close();
            }
            plainWidth = width();
            plainHeight = height();
        }
    }
    
    /** Reads the WMF into a template.
     * @param template the template to read to
     * @throws IOException on error
     * @throws DocumentException on error
     */    
    public void readWMF(PdfTemplate template) throws IOException, DocumentException {
        this.template = template;
        template.setWidth(width());
        template.setHeight(height());
        InputStream is = null;
        try {
            if (rawData == null){
                is = url.openStream();
            }
            else{
                is = new java.io.ByteArrayInputStream(rawData);
            }
            MetaDo meta = new MetaDo(is, template);
            meta.readAll();
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
