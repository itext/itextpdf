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
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Properties;
import com.lowagie.text.pdf.PdfTemplate;

/**
 * An <CODE>Image</CODE> is the representation of a graphic element (JPEG, PNG or GIF)
 * that has to be inserted into the document
 *
 * @see		Element
 * @see		Rectangle
 *
 * @author  bruno@lowagie.com
 */

public abstract class Image extends Rectangle implements Element {
    
    // static membervariables (concerning the presence of borders)
    
/** this is a kind of image alignment. */
    public static final int DEFAULT = 0;
    
/** this is a kind of image alignment. */
    public static final int RIGHT = 1;
    
/** this is a kind of image alignment. */
    public static final int LEFT = 2;
    
/** this is a kind of image alignment. */
    public static final int MIDDLE = 3;
    
/** this is a kind of image alignment. */
    public static final int TEXTWRAP = 4;
    
/** this is a kind of image alignment. */
    public static final int UNDERLYING = 8;
    
/** This represents a coordinate in the transformation matrix. */
    public static final int AX = 0;
    
/** This represents a coordinate in the transformation matrix. */
    public static final int AY = 1;
    
/** This represents a coordinate in the transformation matrix. */
    public static final int BX = 2;
    
/** This represents a coordinate in the transformation matrix. */
    public static final int BY = 3;
    
/** This represents a coordinate in the transformation matrix. */
    public static final int CX = 4;
    
/** This represents a coordinate in the transformation matrix. */
    public static final int CY = 5;
    
/** This represents a coordinate in the transformation matrix. */
    public static final int DX = 6;
    
/** This represents a coordinate in the transformation matrix. */
    public static final int DY = 7;
    
    // membervariables
    
/** The imagetype. */
    protected int type;
    
/** The URL of the image. */
    protected URL url;
    
/** The raw data of the image. */
    protected byte rawData[];
    
/** The template to be treated as an image. */
    protected PdfTemplate template;
    
/** The alignment of the Image. */
    protected int alignment;
    
/** Text that can be shown instead of the image. */
    protected String alt;
    
/** This is the absolute X-position of the image. */
    protected float absoluteX = Float.NaN;
    
/** This is the absolute Y-position of the image. */
    protected float absoluteY = Float.NaN;
    
/** This is the width of the image without rotation. */
    protected float plainWidth;
    
/** This is the width of the image without rotation. */
    protected float plainHeight;
    
/** This is the scaled width of the image taking rotation into account. */
    protected float scaledWidth;
    
/** This is the original height of the image taking rotation into account. */
    protected float scaledHeight;
    
/** This is the rotation of the image. */
    protected float rotation;
    
/** this is the colorspace of a jpeg-image. */
    protected int colorspace = -1;
    
/** this is the bits per component of the raw image. It also flags a CCITT image.*/
    protected int bpc = 1;
    
/** this is the transparency information of the raw image*/
    protected int transparency[];
    
    // serial stamping
    
    protected Long mySerialId = getSerialId();
    
    static long serialId = 0;
    
    /** Holds value of property dpiX. */
    protected int dpiX = 0;
    
    /** Holds value of property dpiY. */
    protected int dpiY = 0;
    
    protected boolean mask = false;
    
    protected Image imageMask;
    
    protected boolean invertMask = false;
    
    /** Holds value of property interpolation. */
    protected boolean interpolation;
    
    // constructors
    
/**
 * Constructs an <CODE>Image</CODE>-object, using an <VAR>url</VAR>.
 *
 * @param		url			the <CODE>URL</CODE> where the image can be found.
 */
    
    public Image(URL url) {
        super(0, 0);
        this.url = url;
        this.alignment = DEFAULT;
        rotation = 0;
    }
    
/**
 * Constructs an <CODE>Image</CODE>-object, using an <VAR>url</VAR>.
 *
 * @param		url			the <CODE>URL</CODE> where the image can be found.
 */
    
    protected Image(Image image) {
        super(image);
        this.type = image.type;
        this.url = image.url;
        this.alignment = image.alignment;
        this.alt = image.alt;
        this.absoluteX = image.absoluteX;
        this.absoluteY = image.absoluteY;
        this.plainWidth = image.plainWidth;
        this.plainHeight = image.plainHeight;
        this.scaledWidth = image.scaledWidth;
        this.scaledHeight = image.scaledHeight;
        this.rotation = image.rotation;
        this.colorspace = image.colorspace;
        this.rawData = image.rawData;
        this.template = image.template;
        this.bpc = image.bpc;
        this.transparency = image.transparency;
        this.mySerialId = image.mySerialId;
    }
    
    // gets an instance of an Image
    
/**
 * Gets an instance of an Image.
 *
 * @param	an URL
 * @return	an object of type <CODE>Gif</CODE>, <CODE>Jpeg</CODE> or <CODE>Png</CODE>
 */
    
    public static Image getInstance(URL url) throws BadElementException, MalformedURLException, IOException {
        InputStream is = null;
        try {
            is = url.openStream();
            int c1 = is.read();
            int c2 = is.read();
            is.close();
            
            is = null;
            if (c1 == 'G' && c2 == 'I') {
                return new Gif(url);
            }
            if (c1 == 0xFF && c2 == 0xD8) {
                return new Jpeg(url);
            }
            if (c1 == Png.PNGID[0] && c2 == Png.PNGID[1]) {
                return new Png(url);
            }
            if (c1 == 0xD7 && c2 == 0xCD) {
                return new ImgWMF(url);
            }
            throw new IOException(url.toString() + " is not a recognized imageformat.");
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }
    
/**
 * Gets an instance of an Image from a java.awt.Image.
 *
 * @param image the <CODE>java.awt.Image</CODE> to convert
 * @param color if different from <CODE>null</CODE> the transparency
 * pixels are replaced by this color
 * @param forceBW if <CODE>true</CODE> the image is treated as black and white
 * @return an object of type <CODE>ImgRaw</CODE>
 * @throws BadElementException on error
 * @throws IOException on error
 */
    
    public static Image getInstance(java.awt.Image image, java.awt.Color color, boolean forceBW) throws BadElementException, IOException {
        java.awt.image.PixelGrabber pg = new java.awt.image.PixelGrabber(image, 0, 0, -1, -1, true);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new IOException("java.awt.Image Interrupted waiting for pixels!");
        }
        if ((pg.getStatus() & java.awt.image.ImageObserver.ABORT) != 0) {
            throw new IOException("java.awt.Image fetch aborted or errored");
        }
        int w = pg.getWidth();
        int h = pg.getHeight();
        int[] pixels = (int[])pg.getPixels();
        if (forceBW) {
            int byteWidth = (w / 8) + ((w & 7) != 0 ? 1 : 0);
            byte[] pixelsByte = new byte[byteWidth * h];

            int index = 0;
            int size = h * w;
            int transColor = 1;
            if (color != null) {
                transColor = (color.getRed() + color.getGreen() + color.getBlue() < 384) ? 0 : 1;
            }
            int transparency[] = null;
            int cbyte = 0x80;
            int wMarker = 0;
            int currByte = 0;
            if (color != null) {
                for (int j = 0; j < size; j++) {
                    int alpha = (pixels[j] >> 24) & 0xff;
                    if (alpha < 250) {
                        if (transColor == 1)
                            currByte |= cbyte;
                    }
                    else {
                        if ((pixels[j] & 0x888) != 0)
                            currByte |= cbyte;
                    }
                    cbyte >>= 1;
                    if (cbyte == 0 || wMarker + 1 >= w) {
                        pixelsByte[index++] = (byte)currByte;
                        cbyte = 0x80;
                        currByte = 0;
                    }
                    ++wMarker;
                    if (wMarker >= w)
                        wMarker = 0;
                }
            }
            else {
                for (int j = 0; j < size; j++) {
                    if (transparency == null) {
                        int alpha = (pixels[j] >> 24) & 0xff;
                        if (alpha == 0) {
                            transparency = new int[2];
                            transparency[0] = transparency[1] = ((pixels[j] & 0x888) != 0) ? 1 : 0;
                        }
                    }
                    if ((pixels[j] & 0x888) != 0)
                        currByte |= cbyte;
                    cbyte >>= 1;
                    if (cbyte == 0 || wMarker + 1 >= w) {
                        pixelsByte[index++] = (byte)currByte;
                        cbyte = 0x80;
                        currByte = 0;
                    }
                    ++wMarker;
                    if (wMarker >= w)
                        wMarker = 0;
                }
            }
            return Image.getInstance(w, h, 1, 1, pixelsByte, transparency);
        }
        else {
            byte[] pixelsByte = new byte[w * h * 3];

            int index = 0;
            int size = h * w;
            int red = 255;
            int green = 255;
            int blue = 255;
            if (color != null) {
                red = color.getRed();
                green = color.getGreen();
                blue = color.getBlue();
            }
            int transparency[] = null;
            if (color != null) {
                for (int j = 0; j < size; j++) {
                    int alpha = (pixels[j] >> 24) & 0xff;
                    if (alpha < 250) {
                        pixelsByte[index++] = (byte) red;
                        pixelsByte[index++] = (byte) green;
                        pixelsByte[index++] = (byte) blue;
                    }
                    else {
                        pixelsByte[index++] = (byte) ((pixels[j] >> 16) & 0xff);
                        pixelsByte[index++] = (byte) ((pixels[j] >> 8) & 0xff);
                        pixelsByte[index++] = (byte) ((pixels[j]) & 0xff);
                    }
                }
            }
            else {
                for (int j = 0; j < size; j++) {
                    if (transparency == null) {
                        int alpha = (pixels[j] >> 24) & 0xff;
                        if (alpha == 0) {
                            transparency = new int[6];
                            transparency[0] = transparency[1] = (pixels[j] >> 16) & 0xff;
                            transparency[2] = transparency[3] = (pixels[j] >> 8) & 0xff;
                            transparency[4] = transparency[5] = pixels[j] & 0xff;
                        }
                    }
                    pixelsByte[index++] = (byte) ((pixels[j] >> 16) & 0xff);
                    pixelsByte[index++] = (byte) ((pixels[j] >> 8) & 0xff);
                    pixelsByte[index++] = (byte) ((pixels[j]) & 0xff);
                }
            }
            return Image.getInstance(w, h, 3, 8, pixelsByte, transparency);
        }
    }
    
/**
 * Gets an instance of an Image from a java.awt.Image.
 *
 * @param image the <CODE>java.awt.Image</CODE> to convert
 * @param color if different from <CODE>null</CODE> the transparency
 * pixels are replaced by this color
 * @return an object of type <CODE>ImgRaw</CODE>
 * @throws BadElementException on error
 * @throws IOException on error
 */
    public static Image getInstance(java.awt.Image image, java.awt.Color color) throws BadElementException, IOException {
        return Image.getInstance(image, color, false);
    }
    
/**
 * Gets an instance of an Image.
 *
 * @param	a filename
 * @return	an object of type <CODE>Gif</CODE>, <CODE>Jpeg</CODE> or <CODE>Png</CODE>
 */
    
    public static Image getInstance(String filename) throws BadElementException, MalformedURLException, IOException {
        return getInstance(toURL(filename));
    }
    
/**
 * Gets an instance of an Image.
 *
 * @param	a byte array
 * @return	an object of type <CODE>Gif</CODE>, <CODE>Jpeg</CODE> or <CODE>Png</CODE>
 *
 * @author	Paulo Soares
 */
    
    public static Image getInstance(byte[] img) throws BadElementException, MalformedURLException, IOException {
        InputStream is = null;
        try {
            is = new java.io.ByteArrayInputStream(img);
            int c1 = is.read();
            int c2 = is.read();
            is.close();
            is = null;
            if (c1 == 'G' && c2 == 'I') {
                return new Gif(img);
            }
            if (c1 == 0xFF && c2 == 0xD8) {
                return new Jpeg(img);
            }
            if (c1 == Png.PNGID[0] && c2 == Png.PNGID[1]) {
                return new Png(img);
            }
            if (c1 == 0xD7 && c2 == 0xCD) {
                return new ImgWMF(img);
            }
            throw new IOException("Could not find a recognized imageformat.");
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }
    
/**
 * Gets an instance of an Image in raw mode.
 *
 * @param width the width of the image in pixels
 * @param height the height of the image in pixels
 * @param components 1,3 or 4 for GrayScale, RGB and CMYK
 * @param data the image data
 * @param bpc bits per component
 * @return an object of type <CODE>ImgRaw</CODE>
 * @throws BadElementException on error
 * @throws MalformedURLException on error
 * @throws IOException on error
 */
    
    public static Image getInstance(int width, int height, int components, int bpc, byte data[]) throws BadElementException, MalformedURLException, IOException {
        return new ImgRaw(width, height, components, bpc, data);
    }
    
    public static Image getInstance(PdfTemplate template) throws BadElementException, MalformedURLException, IOException {
        return new ImgTemplate(template);
    }
    
    public static Image getInstance(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data) throws BadElementException {
        return new ImgCCITT(width, height, reverseBits, typeCCITT, parameters, data);
    }

    public static Image getInstance(int width, int height, boolean reverseBits, int typeCCITT, int parameters, byte[] data, int transparency[]) throws BadElementException {
        if (transparency != null && transparency.length != 2)
            throw new BadElementException("transparency length must be equal to 2 with CCITT images");
        Image img = new ImgCCITT(width, height, reverseBits, typeCCITT, parameters, data);
        img.transparency = transparency;
        return img;
    }
/**
 * Gets an instance of an Image in raw mode.
 *
 * @param width the width of the image in pixels
 * @param height the height of the image in pixels
 * @param components 1,3 or 4 for GrayScale, RGB and CMYK
 * @param data the image data
 * @param bpc bits per component
 * @param transparency transparency information in the Mask format of the
 * image dictionary
 * @return an object of type <CODE>ImgRaw</CODE>
 * @throws BadElementException on error
 * @throws MalformedURLException on error
 * @throws IOException on error
 */
    
    public static Image getInstance(int width, int height, int components, int bpc, byte data[], int transparency[]) throws BadElementException, MalformedURLException, IOException {
        if (transparency != null && transparency.length != components * 2)
            throw new BadElementException("transparency length must be equal to (componentes * 2)");
        Image img = new ImgRaw(width, height, components, bpc, data);
        img.transparency = transparency;
        return img;
    }
    
/**
 * Returns an <CODE>Image</CODE> that has been constructed taking in account
 * the value of some <VAR>attributes</VAR>.
 *
 * @param	attributes		Some attributes
 * @return	an <CODE>Image</CODE>
 */
    
    public static Image getInstance(Properties attributes) throws BadElementException, MalformedURLException, IOException {
        String value;
        Image image = Image.getInstance(attributes.getProperty(ElementTags.URL));
        int align = 0;
        if ((value = attributes.getProperty(ElementTags.ALIGN)) != null) {
            if (ElementTags.ALIGN_LEFT.equals(value)) align |= Image.LEFT;
            else if (ElementTags.ALIGN_RIGHT.equals(value)) align |= Image.RIGHT;
            else if (ElementTags.ALIGN_MIDDLE.equals(value)) align |= Image.MIDDLE;
        }
        if ((value = attributes.getProperty(ElementTags.UNDERLYING)) != null) {
            if (new Boolean(value).booleanValue()) align |= Image.UNDERLYING;
        }
        if ((value = attributes.getProperty(ElementTags.TEXTWRAP)) != null) {
            if (new Boolean(value).booleanValue()) align |= Image.TEXTWRAP;
        }
        image.setAlignment(align);
        if ((value = attributes.getProperty(ElementTags.ALT)) != null) {
            image.setAlt(value);
        }
        String x;
        String y;
        if (((x = attributes.getProperty(ElementTags.ABSOLUTEX)) != null)
        && ((y = attributes.getProperty(ElementTags.ABSOLUTEX)) != null)) {
            image.setAbsolutePosition(Float.valueOf(x + "f").floatValue(), Float.valueOf(y + "f").floatValue());
        }
        if ((value = attributes.getProperty(ElementTags.PLAINWIDTH)) != null) {
            image.scaleAbsoluteWidth(Float.valueOf(value + "f").floatValue());
        }
        if ((value = attributes.getProperty(ElementTags.PLAINHEIGHT)) != null) {
            image.scaleAbsoluteHeight(Float.valueOf(value + "f").floatValue());
        }
        if ((value = attributes.getProperty(ElementTags.ROTATION)) != null) {
            image.setRotation(Float.valueOf(value + "f").floatValue());
        }
        return image;
    }
    
    // methods to set information
    
/**
 * Sets the alignment for the image.
 *
 * @param		aligment		the alignment
 */
    
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }
    
/**
 * Sets the alternative information for the image.
 *
 * @param		alt		the alternative information
 */
    
    public void setAlt(String alt) {
        this.alt = alt;
    }
    
/**
 * Sets the absolute position of the <CODE>Image</CODE>.
 *
 * @param	absoluteX
 * @param	absoluteY
 */
    
    public void setAbsolutePosition(float absoluteX, float absoluteY) {
        this.absoluteX = absoluteX;
        this.absoluteY = absoluteY;
    }
    
/**
 * Scale the image to an absolute width and an absolute height.
 *
 * @param		newWidth	the new width
 * @param		newHeight	the new height
 *
 * @author		Paulo Soares
 */
    
    public void scaleAbsolute(float newWidth, float newHeight) {
        plainWidth = newWidth;
        plainHeight = newHeight;
        float[] matrix = matrix();
        scaledWidth = matrix[DX] - matrix[CX];
        scaledHeight = matrix[DY] - matrix[CY];
    }
    
/**
 * Scale the image to an absolute width.
 *
 * @param		newWidth	the new width
 */
    
    public void scaleAbsoluteWidth(float newWidth) {
        plainWidth = newWidth;
        float[] matrix = matrix();
        scaledWidth = matrix[DX] - matrix[CX];
        scaledHeight = matrix[DY] - matrix[CY];
    }
    
/**
 * Scale the image to an absolute height.
 *
 * @param		newHeight	the new height
 */
    
    public void scaleAbsoluteHeight(float newHeight) {
        plainHeight = newHeight;
        float[] matrix = matrix();
        scaledWidth = matrix[DX] - matrix[CX];
        scaledHeight = matrix[DY] - matrix[CY];
    }
    
/**
 * Scale the image to a certain percentage.
 *
 * @param		percent		the scaling percentage
 *
 * @author		Paulo Soares
 */
    
    public void scalePercent(float percent) {
        scalePercent(percent, percent);
    }
    
/**
 * Scale the width and height of an image to a certain percentage.
 *
 * @param		percentX	the scaling percentage of the width
 * @param		percentY	the scaling percentage of the height
 *
 * @author		Paulo Soares
 */
    
    public void scalePercent(float percentX, float percentY) {
        plainWidth = (width() * percentX) / 100f;
        plainHeight = (height() * percentY) / 100f;
        float[] matrix = matrix();
        scaledWidth = matrix[DX] - matrix[CX];
        scaledHeight = matrix[DY] - matrix[CY];
    }
    
/**
 * Scales the image so that it fits a certain width and height.
 *
 * @param		fitWidth		the width to fit
 * @param		fitHeight		the height to fit
 */
    
    public void scaleToFit(float fitWidth, float fitHeight) {
        float percentX = (fitWidth * 100) / width();
        float percentY = (fitHeight * 100) / height();
        scalePercent(percentX < percentY ? percentX : percentY);
    }
    
/**
 * Sets the rotation of the image in radians.
 *
 * @param		r		rotation in radians
 *
 * @author		Paulo Soares
 */
    
    public void setRotation(float r) {
        double d=Math.PI;                  //__IDS__	
        rotation = (float)(r % (2.0 * d)); //__IDS__
        if (rotation < 0) {
            rotation += 2.0 * d;           //__IDS__	
        }
        float[] matrix = matrix();
        scaledWidth = matrix[DX] - matrix[CX];
        scaledHeight = matrix[DY] - matrix[CY];
    }
    
/**
 * Sets the rotation of the image in degrees.
 *
 * @param		r		rotation in degrees
 *
 * @author		Paulo Soares
 */
    
    public void setRotationDegrees(float deg) {
        double d=Math.PI;                  //__IDS__
        setRotation(deg / 180 * (float)d); //__IDS__
    }
    
    // methods to retrieve information
    
/** Gets the bpc for the image.
 * <P>
 * Remark: this only makes sense for Images of the type <CODE>RawImage</CODE>.
 *
 * @return a bpc value
 */
    
    public int bpc() {
        return bpc;
    }
    
/**
 * Gets the raw data for the image.
 * <P>
 * Remark: this only makes sense for Images of the type <CODE>RawImage</CODE>.
 *
 * @return		the raw data
 *
 * @author		Paulo Soares
 */
    
    public byte[] rawData() {
        return rawData;
    }
    
/**
 * Gets the template to be used as an image.
 * <P>
 * Remark: this only makes sense for Images of the type <CODE>ImgTemplate</CODE>.
 *
 * @return		the template
 *
 * @author		Paulo Soares
 */
    
    public PdfTemplate templateData() {
        return template;
    }
    
    public void setTemplateData(PdfTemplate template) {
        this.template = template;
    }
    
/**
 * Checks if the <CODE>Images</CODE> has to be added at an absolute position.
 *
 * @return		a boolean
 */
    
    public boolean hasAbsolutePosition() {
        return !Float.isNaN(absoluteX) && !Float.isNaN(absoluteY);
    }
    
/**
 * Returns the absolute X position.
 *
 * @return		a position
 */
    
    public float absoluteX() {
        return absoluteX;
    }
    
/**
 * Returns the absolute Y position.
 *
 * @return		a position
 */
    
    public float absoluteY() {
        return absoluteY;
    }
    
/**
 * Returns the type.
 *
 * @return		a type
 */
    
    public int type() {
        return type;
    }
    
/**
 * Returns <CODE>true</CODE> if the image is a <CODE>Gif</CODE>-object.
 *
 * @return		a <CODE>boolean</CODE>
 */
    
    public boolean isGif() {
        return type == GIF;
    }
    
/**
 * Returns <CODE>true</CODE> if the image is a <CODE>Jpeg</CODE>-object.
 *
 * @return		a <CODE>boolean</CODE>
 */
    
    public boolean isJpeg() {
        return type == JPEG;
    }
    
/**
 * Returns <CODE>true</CODE> if the image is a <CODE>Png</CODE>-object.
 *
 * @return		a <CODE>boolean</CODE>
 */
    
    public boolean isPng() {
        return type == PNG;
    }
    
/**
 * Returns <CODE>true</CODE> if the image is a <CODE>ImgRaw</CODE>-object.
 *
 * @return		a <CODE>boolean</CODE>
 *
 * @author		Paulo Soares
 */
    
    public boolean isImgRaw() {
        return type == IMGRAW;
    }
 /**
 * Returns <CODE>true</CODE> if the image is an <CODE>ImgTemplate</CODE>-object.
 *
 * @return		a <CODE>boolean</CODE>
 *
 * @author		Paulo Soares
 */
    
    public boolean isImgTemplate() {
        return type == IMGTEMPLATE;
    }
    
/**
 * Gets the <CODE>String</CODE>-representation of the reference to the image.
 *
 * @return		a <CODE>String</CODE>
 */
    
    public URL url() {
        return url;
    }
    
/**
 * Gets the alignment for the image.
 *
 * @return		a value
 */
    
    public int alignment() {
        return alignment;
    }
    
/**
 * Gets the alternative text for the image.
 *
 * @return		a <CODE>String</CODE>
 */
    
    public String alt() {
        return alt;
    }
    
/**
 * Gets the scaled width of the image.
 *
 * @return		a value
 */
    
    public float scaledWidth() {
        return scaledWidth;
    }
    
/**
 * Gets the scaled height of the image.
 *
 * @return		a value
 */
    
    public float scaledHeight() {
        return scaledHeight;
    }
    
/**
 * Gets the colorspace for the image.
 * <P>
 * Remark: this only makes sense for Images of the type <CODE>Jpeg</CODE>.
 *
 * @return		a colorspace value
 *
 * @author		Paulo Soares
 */
    
    public int colorspace() {
        return colorspace;
    }
    
/**
 * Returns the transformation matrix of the image.
 *
 * @return		an array [AX, AY, BX, BY, CX, CY, DX, DY]
 *
 * @author		Paulo Soares
 */
    
    public float[] matrix() {
        float[] matrix = new float[8];
        float cosX = (float)Math.cos(rotation);
        float sinX = (float)Math.sin(rotation);
        matrix[AX] = plainWidth * cosX;
        matrix[AY] = plainWidth * sinX;
        matrix[BX] = (- plainHeight) * sinX;
        matrix[BY] = plainHeight * cosX;
        if (rotation < Math.PI / 2f) {
            matrix[CX] = matrix[BX];
            matrix[CY] = 0;
            matrix[DX] = matrix[AX];
            matrix[DY] = matrix[AY] + matrix[BY];
        }
        else if (rotation < Math.PI) {
            matrix[CX] = matrix[AX] + matrix[BX];
            matrix[CY] = matrix[BY];
            matrix[DX] = 0;
            matrix[DY] = matrix[AY];
        }
        else if (rotation < Math.PI * 1.5f) {
            matrix[CX] = matrix[AX];
            matrix[CY] = matrix[AY] + matrix[BY];
            matrix[DX] = matrix[BX];
            matrix[DY] = 0;
        }
        else {
            matrix[CX] = 0;
            matrix[CY] = matrix[AY];
            matrix[DX] = matrix[AX] + matrix[BX];
            matrix[DY] = matrix[BY];
        }
        return matrix;
    }
    
/**
 * This method is an alternative for the <CODE>InputStream.skip()</CODE>-method
 * that doesn't seem to work properly for big values of <CODE>size</CODE>.
 *
 * @param	is		the <CODE>InputStream</CODE>
 * @param	size	the number of bytes to skip
 *
 * @author		Paulo Soares
 */
    
    static public void skip(InputStream is, int size) throws IOException {
        while (size > 0) {
            size -= is.skip(size);
        }
    }
    
/**
 * This method makes a valid URL from a given filename.
 * <P>
 * This method makes the conversion of this library from the JAVA 2 platform
 * to a JDK1.1.x-version easier.
 *
 * @param        filename        a given filename
 * @return        a valid URL
 *
 * @author                Paulo Soares
 */
    
    public static URL toURL(String filename) throws MalformedURLException {
        if (filename.startsWith("file:/") || filename.startsWith("http://")) {
            return new URL(filename);
        }
        File f = new File(filename);
        String path = f.getAbsolutePath();
        if (File.separatorChar != '/') {
            path = path.replace(File.separatorChar, '/');
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (!path.endsWith("/") && f.isDirectory()) {
            path = path + "/";
        }
        return new URL("file", "", path);
    }
    
/**
 * Returns the transparency.
 *
 * @author                Paulo Soares
 */
    
    public int[] getTransparency()
    {
        return transparency;
    }
    
/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.IMAGE.equals(tag);
    }
    
/**
 * Gets the plain width of the image.
 *
 * @return              a value
 */
    
    public float plainWidth() {
        return plainWidth;
    }
    
 /**
  * Gets the plain height of the image.
  *
  * @return              a value
  */
    
    public float plainHeight() {
        return plainHeight;
    }
    
    static protected synchronized Long getSerialId() {
        ++serialId;
        return new Long(serialId);
    }
    
    public Long getMySerialId() {
        return mySerialId;
    }
    
    /** Gets the dots-per-inch in the X direction. Returns 0 if not available.
     * @return the dots-per-inch in the X direction
     */
    public int getDpiX() {
        return dpiX;
    }
    
    /** Gets the dots-per-inch in the Y direction. Returns 0 if not available.
     * @return the dots-per-inch in the Y direction
     */
    public int getDpiY() {
        return dpiY;
    }
    
    /** Returns <CODE>true</CODE> if this <CODE>Image</CODE> has the
     * requisites to be a mask.
     * @return <CODE>true</CODE> if this <CODE>Image</CODE> can ba a mask
     */    
    public boolean isMaskCandidate() {
        if (type == IMGRAW) {
            if (bpc > 0xff)
                return true;
            return bpc == 1 && colorspace == 1;
        }
        return type == PNG && bpc == 1 && colorspace == 1;
    }
    
    /** Make this <CODE>Image</CODE> a mask.
     * @throws DocumentException if this <CODE>Image</CODE> can not be a mask
     */    
    public void makeMask() throws DocumentException {
        if (!isMaskCandidate())
            throw new DocumentException("This image can not be an image mask.");
        mask = true;
    }
    
    /** Sets the explicit masking.
     * @param mask the mask to be applied
     * @throws DocumentException on error
     */    
    public void setImageMask(Image mask) throws DocumentException {
        if (this.mask)
            throw new DocumentException("An image mask can not contain another image mask.");
        if (!mask.mask)
            throw new DocumentException("The image mask is not a mask. Did you do makeMask()?");
        imageMask = mask;
    }
    
    /** Gets the explicit masking.
     * @return the explicit masking
     */    
    public Image getImageMask() {
        return imageMask;
    }
    
    /** Returns <CODE>true</CODE> if this <CODE>Image</CODE> is a mask.
     * @return <CODE>true</CODE> if this <CODE>Image</CODE> is a mask
     */    
    public boolean isMask() {
        return mask;
    }
    
    /** Inverts the meaning of the bits of a mask.
     * @param invertMask <CODE>true</CODE> to invert the meaning of the bits of a mask
     */    
    public void setInvertMask(boolean invertMask) {
        this.invertMask = invertMask;
    }
    
    /** Returns <CODE>true</CODE> if the bits are to be inverted
     * in the mask.
     * @return <CODE>true</CODE> if the bits are to be inverted in the mask
     */    
    public boolean isInvertMask() {
        return invertMask;
    }
    
    /** Getter for property interpolation.
     * @return Value of property interpolation.
     */
    public boolean isInterpolation() {
        return interpolation;
    }
    
    /** Sets the image interpolation. Image interpolation attempts to
     * produce a smooth transition between adjacent sample values.
     * @param interpolation New value of property interpolation.
     */
    public void setInterpolation(boolean interpolation) {
        this.interpolation = interpolation;
    }
    
}
