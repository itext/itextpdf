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
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.Set;
import java.util.ArrayList;
import java.awt.color.ICC_Profile;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.codec.CCITTG4Encoder;
import java.lang.reflect.Constructor;
import com.lowagie.text.pdf.RandomAccessFileOrArray;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.codec.GifImage;
import com.lowagie.text.pdf.codec.PngImage;
import com.lowagie.text.pdf.codec.TiffImage;
import com.lowagie.text.pdf.codec.BmpImage;
import com.lowagie.text.pdf.PdfOCG;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfArray;

/**
 * An <CODE>Image</CODE> is the representation of a graphic element (JPEG, PNG
 * or GIF) that has to be inserted into the document
 * 
 * @see Element
 * @see Rectangle
 */

public abstract class Image extends Rectangle implements Element,
		MarkupAttributes {

	// static membervariables

	/** this is a kind of image alignment. */
	public static final int DEFAULT = 0;

	/** this is a kind of image alignment. */
	public static final int RIGHT = 2;

	/** this is a kind of image alignment. */
	public static final int LEFT = 0;

	/** this is a kind of image alignment. */
	public static final int MIDDLE = 1;

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

	/** type of image */
	public static final int ORIGINAL_NONE = 0;

	/** type of image */
	public static final int ORIGINAL_JPEG = 1;

	/** type of image */
	public static final int ORIGINAL_PNG = 2;

	/** type of image */
	public static final int ORIGINAL_GIF = 3;

	/** type of image */
	public static final int ORIGINAL_BMP = 4;

	/** type of image */
	public static final int ORIGINAL_TIFF = 5;

	/** type of image */
	public static final int ORIGINAL_WMF = 6;

	/** type of image */
    public static final int ORIGINAL_PS = 7;

	/** Image color inversion */
	protected boolean invert = false;

	/** The imagetype. */
	protected int type;

	/** The URL of the image. */
	protected URL url;

	/** The raw data of the image. */
	protected byte rawData[];

	/** The template to be treated as an image. */
	protected PdfTemplate template[] = new PdfTemplate[1];

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

	/**
	 * this is the bits per component of the raw image. It also flags a CCITT
	 * image.
	 */
	protected int bpc = 1;

	/** this is the transparency information of the raw image */
	protected int transparency[];

	// for the moment these variables are only used for Images in class Table
	// code contributed by Pelikan Stephan
	/** the indentation to the left. */
	protected float indentationLeft = 0;

	/** the indentation to the right. */
	protected float indentationRight = 0;

	// serial stamping

	protected Long mySerialId = getSerialId();

	static long serialId = 0;

	/** Holds value of property dpiX. */
	protected int dpiX = 0;

	/** Holds value of property dpiY. */
	protected int dpiY = 0;

	protected boolean mask = false;

	protected Image imageMask;

	/** Holds value of property interpolation. */
	protected boolean interpolation;

	/** if the annotation is not null the image will be clickable. */
	protected Annotation annotation = null;

	/** Contains extra markupAttributes */
	protected Properties markupAttributes;

	/** ICC Profile attached */
	protected ICC_Profile profile = null;

	/** Holds value of property deflated. */
	protected boolean deflated = false;

	private PdfDictionary additional = null;

	/** Holds value of property smask. */
	private boolean smask;

	/** Holds value of property XYRatio. */
	private float XYRatio = 0;

	/** Holds value of property originalType. */
	protected int originalType = ORIGINAL_NONE;

	/** Holds value of property originalData. */
	protected byte[] originalData;

	/** The spacing before the image. */
	protected float spacingBefore;

	/** The spacing after the image. */
	protected float spacingAfter;

	/**
	 * Holds value of property widthPercentage.
	 */
	private float widthPercentage = 100;

	protected PdfOCG layer;

    /**
     * Holds value of property initialRotation.
     */
    private float initialRotation;
    
	// constructors

	/**
	 * Constructs an <CODE>Image</CODE> -object, using an <VAR>url </VAR>.
	 * 
	 * @param url
	 *            the <CODE>URL</CODE> where the image can be found.
	 */

	public Image(URL url) {
		super(0, 0);
		this.url = url;
		this.alignment = DEFAULT;
		rotation = 0;
	}

	/**
	 * Constructs an <CODE>Image</CODE> -object, using an <VAR>url </VAR>.
	 * 
	 * @param image
	 *            another Image object.
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
		this.invert = image.invert;
		this.dpiX = image.dpiX;
		this.dpiY = image.dpiY;
		this.mask = image.mask;
		this.imageMask = image.imageMask;
		this.interpolation = image.interpolation;
		this.annotation = image.annotation;
		this.markupAttributes = image.markupAttributes;
		this.profile = image.profile;
		this.deflated = image.deflated;
		this.additional = image.additional;
		this.smask = image.smask;
		this.XYRatio = image.XYRatio;
		this.originalData = image.originalData;
		this.originalType = image.originalType;
		this.spacingAfter = image.spacingAfter;
		this.spacingBefore = image.spacingBefore;
		this.widthPercentage = image.widthPercentage;
		this.layer = image.layer;
        this.initialRotation = image.initialRotation;
	}

	/**
	 * gets an instance of an Image
	 * 
	 * @param image
	 *            an Image object
	 * @return a new Image object
	 */

	public static Image getInstance(Image image) {
		if (image == null)
			return null;
		try {
			Class cs = image.getClass();
			Constructor constructor = cs
					.getDeclaredConstructor(new Class[] { Image.class });
			return (Image) constructor.newInstance(new Object[] { image });
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

	/**
	 * Gets an instance of an Image.
	 * 
	 * @param url
	 *            an URL
	 * @return an Image
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */

	public static Image getInstance(URL url) throws BadElementException,
			MalformedURLException, IOException {
		InputStream is = null;
		try {
			is = url.openStream();
			int c1 = is.read();
			int c2 = is.read();
			int c3 = is.read();
			int c4 = is.read();
			is.close();

			is = null;
			if (c1 == 'G' && c2 == 'I' && c3 == 'F') {
				GifImage gif = new GifImage(url);
				Image img = gif.getImage(1);
				return img;
			}
			if (c1 == 0xFF && c2 == 0xD8) {
				return new Jpeg(url);
			}
			if (c1 == PngImage.PNGID[0] && c2 == PngImage.PNGID[1]
					&& c3 == PngImage.PNGID[2] && c4 == PngImage.PNGID[3]) {
				return PngImage.getImage(url);
			}
            if (c1 == '%' && c2 == '!' && c3 == 'P' && c4 == 'S') {
               return new ImgPostscript(url);
            }
			if (c1 == 0xD7 && c2 == 0xCD) {
				return new ImgWMF(url);
			}
			if (c1 == 'B' && c2 == 'M') {
				return  BmpImage.getImage(url);
			}
			if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42)
					|| (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) {
				RandomAccessFileOrArray ra = null;
				try {
					if (url.getProtocol().equals("file")) {
						String file = url.getFile();
						ra = new RandomAccessFileOrArray(file);
					} else
						ra = new RandomAccessFileOrArray(url);
					Image img = TiffImage.getTiffImage(ra, 1);
					img.url = url;
					return img;
				} finally {
					if (ra != null)
						ra.close();
				}

			}
			throw new IOException(url.toString()
					+ " is not a recognized imageformat.");
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * gets an instance of an Image
	 * 
	 * @param imgb
	 *            raw image date
	 * @return an Image object
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static Image getInstance(byte imgb[]) throws BadElementException,
			MalformedURLException, IOException {
		InputStream is = null;
		try {
			is = new java.io.ByteArrayInputStream(imgb);
			int c1 = is.read();
			int c2 = is.read();
			int c3 = is.read();
			int c4 = is.read();
			is.close();

			is = null;
			if (c1 == 'G' && c2 == 'I' && c3 == 'F') {
				GifImage gif = new GifImage(imgb);
				return gif.getImage(1);
			}
			if (c1 == 0xFF && c2 == 0xD8) {
				return new Jpeg(imgb);
			}
			if (c1 == PngImage.PNGID[0] && c2 == PngImage.PNGID[1]
					&& c3 == PngImage.PNGID[2] && c4 == PngImage.PNGID[3]) {
				return PngImage.getImage(imgb);
			}
            if (c1 == '%' && c2 == '!' && c3 == 'P' && c4 == 'S') {
               return new ImgPostscript(imgb);
            }
			if (c1 == 0xD7 && c2 == 0xCD) {
				return new ImgWMF(imgb);
			}
			if (c1 == 'B' && c2 == 'M') {
				return BmpImage.getImage(imgb);
			}
			if ((c1 == 'M' && c2 == 'M' && c3 == 0 && c4 == 42)
					|| (c1 == 'I' && c2 == 'I' && c3 == 42 && c4 == 0)) {
				RandomAccessFileOrArray ra = null;
				try {
					ra = new RandomAccessFileOrArray(imgb);
					Image img = TiffImage.getTiffImage(ra, 1);
                    if (img.getOriginalData() == null)
                        img.setOriginalData(imgb);
					return img;
				} finally {
					if (ra != null)
						ra.close();
				}

			}
			throw new IOException(
					"The byte array is not a recognized imageformat.");
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	/**
	 * Gets an instance of an Image from a java.awt.Image.
	 * 
	 * @param image
	 *            the <CODE>java.awt.Image</CODE> to convert
	 * @param color
	 *            if different from <CODE>null</CODE> the transparency pixels
	 *            are replaced by this color
	 * @param forceBW
	 *            if <CODE>true</CODE> the image is treated as black and white
	 * @return an object of type <CODE>ImgRaw</CODE>
	 * @throws BadElementException
	 *             on error
	 * @throws IOException
	 *             on error
	 */

	public static Image getInstance(java.awt.Image image, java.awt.Color color,
			boolean forceBW) throws BadElementException, IOException {
		java.awt.image.PixelGrabber pg = new java.awt.image.PixelGrabber(image,
				0, 0, -1, -1, true);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			throw new IOException(
					"java.awt.Image Interrupted waiting for pixels!");
		}
		if ((pg.getStatus() & java.awt.image.ImageObserver.ABORT) != 0) {
			throw new IOException("java.awt.Image fetch aborted or errored");
		}
		int w = pg.getWidth();
		int h = pg.getHeight();
		int[] pixels = (int[]) pg.getPixels();
		if (forceBW) {
			int byteWidth = (w / 8) + ((w & 7) != 0 ? 1 : 0);
			byte[] pixelsByte = new byte[byteWidth * h];

			int index = 0;
			int size = h * w;
			int transColor = 1;
			if (color != null) {
				transColor = (color.getRed() + color.getGreen()
						+ color.getBlue() < 384) ? 0 : 1;
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
					} else {
						if ((pixels[j] & 0x888) != 0)
							currByte |= cbyte;
					}
					cbyte >>= 1;
					if (cbyte == 0 || wMarker + 1 >= w) {
						pixelsByte[index++] = (byte) currByte;
						cbyte = 0x80;
						currByte = 0;
					}
					++wMarker;
					if (wMarker >= w)
						wMarker = 0;
				}
			} else {
				for (int j = 0; j < size; j++) {
					if (transparency == null) {
						int alpha = (pixels[j] >> 24) & 0xff;
						if (alpha == 0) {
							transparency = new int[2];
							transparency[0] = transparency[1] = ((pixels[j] & 0x888) != 0) ? 1
									: 0;
						}
					}
					if ((pixels[j] & 0x888) != 0)
						currByte |= cbyte;
					cbyte >>= 1;
					if (cbyte == 0 || wMarker + 1 >= w) {
						pixelsByte[index++] = (byte) currByte;
						cbyte = 0x80;
						currByte = 0;
					}
					++wMarker;
					if (wMarker >= w)
						wMarker = 0;
				}
			}
			return Image.getInstance(w, h, 1, 1, pixelsByte, transparency);
		} else {
			byte[] pixelsByte = new byte[w * h * 3];
			byte[] smask = null;

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
					} else {
						pixelsByte[index++] = (byte) ((pixels[j] >> 16) & 0xff);
						pixelsByte[index++] = (byte) ((pixels[j] >> 8) & 0xff);
						pixelsByte[index++] = (byte) ((pixels[j]) & 0xff);
					}
				}
			} else {
				int transparentPixel = 0;
				smask = new byte[w * h];
				boolean shades = false;
				for (int j = 0; j < size; j++) {
					byte alpha = smask[j] = (byte) ((pixels[j] >> 24) & 0xff);
					/* bugfix by Chris Nokleberg */
					if (!shades) {
						if (alpha != 0 && alpha != -1) {
							shades = true;
						} else if (transparency == null) {
							if (alpha == 0) {
								transparentPixel = pixels[j] & 0xffffff;
								transparency = new int[6];
								transparency[0] = transparency[1] = (transparentPixel >> 16) & 0xff;
								transparency[2] = transparency[3] = (transparentPixel >> 8) & 0xff;
								transparency[4] = transparency[5] = transparentPixel & 0xff;
							}
						} else if ((pixels[j] & 0xffffff) != transparentPixel) {
							shades = true;
						}
					}
					pixelsByte[index++] = (byte) ((pixels[j] >> 16) & 0xff);
					pixelsByte[index++] = (byte) ((pixels[j] >> 8) & 0xff);
					pixelsByte[index++] = (byte) ((pixels[j]) & 0xff);
				}
				if (shades)
					transparency = null;
				else
					smask = null;
			}
			Image img = Image.getInstance(w, h, 3, 8, pixelsByte, transparency);
			if (smask != null) {
				Image sm = Image.getInstance(w, h, 1, 8, smask);
				try {
					sm.makeMask();
					img.setImageMask(sm);
				} catch (DocumentException de) {
					throw new ExceptionConverter(de);
				}
			}
			return img;
		}
	}

	/**
	 * Gets an instance of an Image from a java.awt.Image.
	 * 
	 * @param image
	 *            the <CODE>java.awt.Image</CODE> to convert
	 * @param color
	 *            if different from <CODE>null</CODE> the transparency pixels
	 *            are replaced by this color
	 * @return an object of type <CODE>ImgRaw</CODE>
	 * @throws BadElementException
	 *             on error
	 * @throws IOException
	 *             on error
	 */
	public static Image getInstance(java.awt.Image image, java.awt.Color color)
			throws BadElementException, IOException {
		return Image.getInstance(image, color, false);
	}

	/**
	 * Gets an instance of an Image.
	 * 
	 * @param filename
	 *            a filename
	 * @return an object of type <CODE>Gif</CODE>,<CODE>Jpeg</CODE> or
	 *         <CODE>Png</CODE>
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */

	public static Image getInstance(String filename)
			throws BadElementException, MalformedURLException, IOException {
		return getInstance(toURL(filename));
	}

	/**
	 * Gets an instance of an Image in raw mode.
	 * 
	 * @param width
	 *            the width of the image in pixels
	 * @param height
	 *            the height of the image in pixels
	 * @param components
	 *            1,3 or 4 for GrayScale, RGB and CMYK
	 * @param data
	 *            the image data
	 * @param bpc
	 *            bits per component
	 * @return an object of type <CODE>ImgRaw</CODE>
	 * @throws BadElementException
	 *             on error
	 */

	public static Image getInstance(int width, int height, int components,
			int bpc, byte data[]) throws BadElementException {
		return Image.getInstance(width, height, components, bpc, data, null);
	}

	/**
	 * gets an instance of an Image
	 * 
	 * @param template
	 *            a PdfTemplate that has to be wrapped in an Image object
	 * @return an Image object
	 * @throws BadElementException
	 */
	public static Image getInstance(PdfTemplate template)
			throws BadElementException {
		return new ImgTemplate(template);
	}

	/**
	 * Creates an Image with CCITT G3 or G4 compression. It assumes that the
	 * data bytes are already compressed.
	 * 
	 * @param width
	 *            the exact width of the image
	 * @param height
	 *            the exact height of the image
	 * @param reverseBits
	 *            reverses the bits in <code>data</code>. Bit 0 is swapped
	 *            with bit 7 and so on
	 * @param typeCCITT
	 *            the type of compression in <code>data</code>. It can be
	 *            CCITTG4, CCITTG31D, CCITTG32D
	 * @param parameters
	 *            parameters associated with this stream. Possible values are
	 *            CCITT_BLACKIS1, CCITT_ENCODEDBYTEALIGN, CCITT_ENDOFLINE and
	 *            CCITT_ENDOFBLOCK or a combination of them
	 * @param data
	 *            the image data
	 * @return an Image object
	 * @throws BadElementException
	 *             on error
	 */
	public static Image getInstance(int width, int height, boolean reverseBits,
			int typeCCITT, int parameters, byte[] data)
			throws BadElementException {
		return Image.getInstance(width, height, reverseBits, typeCCITT,
				parameters, data, null);
	}

	/**
	 * Creates an Image with CCITT G3 or G4 compression. It assumes that the
	 * data bytes are already compressed.
	 * 
	 * @param width
	 *            the exact width of the image
	 * @param height
	 *            the exact height of the image
	 * @param reverseBits
	 *            reverses the bits in <code>data</code>. Bit 0 is swapped
	 *            with bit 7 and so on
	 * @param typeCCITT
	 *            the type of compression in <code>data</code>. It can be
	 *            CCITTG4, CCITTG31D, CCITTG32D
	 * @param parameters
	 *            parameters associated with this stream. Possible values are
	 *            CCITT_BLACKIS1, CCITT_ENCODEDBYTEALIGN, CCITT_ENDOFLINE and
	 *            CCITT_ENDOFBLOCK or a combination of them
	 * @param data
	 *            the image data
	 * @param transparency
	 *            transparency information in the Mask format of the image
	 *            dictionary
	 * @return an Image object
	 * @throws BadElementException
	 *             on error
	 */
	public static Image getInstance(int width, int height, boolean reverseBits,
			int typeCCITT, int parameters, byte[] data, int transparency[])
			throws BadElementException {
		if (transparency != null && transparency.length != 2)
			throw new BadElementException(
					"Transparency length must be equal to 2 with CCITT images");
		Image img = new ImgCCITT(width, height, reverseBits, typeCCITT,
				parameters, data);
		img.transparency = transparency;
		return img;
	}

	/**
	 * Gets an instance of an Image in raw mode.
	 * 
	 * @param width
	 *            the width of the image in pixels
	 * @param height
	 *            the height of the image in pixels
	 * @param components
	 *            1,3 or 4 for GrayScale, RGB and CMYK
	 * @param data
	 *            the image data
	 * @param bpc
	 *            bits per component
	 * @param transparency
	 *            transparency information in the Mask format of the image
	 *            dictionary
	 * @return an object of type <CODE>ImgRaw</CODE>
	 * @throws BadElementException
	 *             on error
	 */

	public static Image getInstance(int width, int height, int components,
			int bpc, byte data[], int transparency[])
			throws BadElementException {
		if (transparency != null && transparency.length != components * 2)
			throw new BadElementException(
					"Transparency length must be equal to (componentes * 2)");
		if (components == 1 && bpc == 1) {
			byte g4[] = CCITTG4Encoder.compress(data, width, height);
			return Image.getInstance(width, height, false, Image.CCITTG4,
					Image.CCITT_BLACKIS1, g4, transparency);
		}
		Image img = new ImgRaw(width, height, components, bpc, data);
		img.transparency = transparency;
		return img;
	}

	/**
	 * Returns an <CODE>Image</CODE> that has been constructed taking in
	 * account the value of some <VAR>attributes </VAR>.
	 * 
	 * @param attributes
	 *            Some attributes
	 * @return an <CODE>Image</CODE>
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */

	public static Image getInstance(Properties attributes)
			throws BadElementException, MalformedURLException, IOException {
		String value = (String) attributes.remove(ElementTags.URL);
		if (value == null)
			throw new MalformedURLException("The URL of the image is missing.");
		Image image = Image.getInstance(value);
		int align = 0;
		if ((value = (String) attributes.remove(ElementTags.ALIGN)) != null) {
			if (ElementTags.ALIGN_LEFT.equalsIgnoreCase(value))
				align |= Image.LEFT;
			else if (ElementTags.ALIGN_RIGHT.equalsIgnoreCase(value))
				align |= Image.RIGHT;
			else if (ElementTags.ALIGN_MIDDLE.equalsIgnoreCase(value))
				align |= Image.MIDDLE;
		}
		if ((value = (String) attributes.remove(ElementTags.UNDERLYING)) != null) {
			if (new Boolean(value).booleanValue())
				align |= Image.UNDERLYING;
		}
		if ((value = (String) attributes.remove(ElementTags.TEXTWRAP)) != null) {
			if (new Boolean(value).booleanValue())
				align |= Image.TEXTWRAP;
		}
		image.setAlignment(align);
		if ((value = (String) attributes.remove(ElementTags.ALT)) != null) {
			image.setAlt(value);
		}
		String x;
		String y;
		if (((x = (String) attributes.remove(ElementTags.ABSOLUTEX)) != null)
				&& ((y = (String) attributes.remove(ElementTags.ABSOLUTEY)) != null)) {
			image.setAbsolutePosition(Float.valueOf(x + "f").floatValue(),
					Float.valueOf(y + "f").floatValue());
		}
		if ((value = (String) attributes.remove(ElementTags.PLAINWIDTH)) != null) {
			image.scaleAbsoluteWidth(Float.valueOf(value + "f").floatValue());
		}
		if ((value = (String) attributes.remove(ElementTags.PLAINHEIGHT)) != null) {
			image.scaleAbsoluteHeight(Float.valueOf(value + "f").floatValue());
		}
		if ((value = (String) attributes.remove(ElementTags.ROTATION)) != null) {
			image.setRotation(Float.valueOf(value + "f").floatValue());
		}
		if (attributes.size() > 0)
			image.setMarkupAttributes(attributes);
		return image;
	}

	// methods to set information

	/**
	 * Sets the alignment for the image.
	 * 
	 * @param alignment
	 *            the alignment
	 */

	public void setAlignment(int alignment) {
		this.alignment = alignment;
	}

	/**
	 * Sets the alternative information for the image.
	 * 
	 * @param alt
	 *            the alternative information
	 */

	public void setAlt(String alt) {
		this.alt = alt;
	}

	/**
	 * Sets the absolute position of the <CODE>Image</CODE>.
	 * 
	 * @param absoluteX
	 * @param absoluteY
	 */

	public void setAbsolutePosition(float absoluteX, float absoluteY) {
		this.absoluteX = absoluteX;
		this.absoluteY = absoluteY;
	}

	/**
	 * Scale the image to an absolute width and an absolute height.
	 * 
	 * @param newWidth
	 *            the new width
	 * @param newHeight
	 *            the new height
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
	 * @param newWidth
	 *            the new width
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
	 * @param newHeight
	 *            the new height
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
	 * @param percent
	 *            the scaling percentage
	 */

	public void scalePercent(float percent) {
		scalePercent(percent, percent);
	}

	/**
	 * Scale the width and height of an image to a certain percentage.
	 * 
	 * @param percentX
	 *            the scaling percentage of the width
	 * @param percentY
	 *            the scaling percentage of the height
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
	 * @param fitWidth
	 *            the width to fit
	 * @param fitHeight
	 *            the height to fit
	 */

	public void scaleToFit(float fitWidth, float fitHeight) {
        scalePercent(100);
		float percentX = (fitWidth * 100) / scaledWidth();
		float percentY = (fitHeight * 100) / scaledHeight();
		scalePercent(percentX < percentY ? percentX : percentY);
	}

	/**
	 * Sets the rotation of the image in radians.
	 * 
	 * @param r
	 *            rotation in radians
	 */

	public void setRotation(float r) {
		double d = Math.PI; //__IDS__
		rotation = (float) ((r + initialRotation) % (2.0 * d)); //__IDS__
		if (rotation < 0) {
			rotation += 2.0 * d; //__IDS__
		}
		float[] matrix = matrix();
		scaledWidth = matrix[DX] - matrix[CX];
		scaledHeight = matrix[DY] - matrix[CY];
	}

	/**
	 * Sets the rotation of the image in degrees.
	 * 
	 * @param deg
	 *            rotation in degrees
	 */

	public void setRotationDegrees(float deg) {
		double d = Math.PI; //__IDS__
		setRotation(deg / 180 * (float) d); //__IDS__
	}

	/**
	 * Sets the annotation of this Image.
	 * 
	 * @param annotation
	 *            the annotation
	 */

	public void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}

	/**
	 * Gets the annotation.
	 * 
	 * @return the annotation that is linked to this image
	 */

	public Annotation annotation() {
		return annotation;
	}

	// methods to retrieve information

	/**
	 * Gets the bpc for the image.
	 * <P>
	 * Remark: this only makes sense for Images of the type <CODE>RawImage
	 * </CODE>.
	 * 
	 * @return a bpc value
	 */

	public int bpc() {
		return bpc;
	}

	/**
	 * Gets the raw data for the image.
	 * <P>
	 * Remark: this only makes sense for Images of the type <CODE>RawImage
	 * </CODE>.
	 * 
	 * @return the raw data
	 */

	public byte[] rawData() {
		return rawData;
	}

	/**
	 * Gets the template to be used as an image.
	 * <P>
	 * Remark: this only makes sense for Images of the type <CODE>ImgTemplate
	 * </CODE>.
	 * 
	 * @return the template
	 */

	public PdfTemplate templateData() {
		return template[0];
	}

	/**
	 * Sets data from a PdfTemplate
	 * 
	 * @param template
	 *            the template with the content
	 */
	public void setTemplateData(PdfTemplate template) {
		this.template[0] = template;
	}

	/**
	 * Checks if the <CODE>Images</CODE> has to be added at an absolute
	 * position.
	 * 
	 * @return a boolean
	 */

	public boolean hasAbsolutePosition() {
		return !Float.isNaN(absoluteY);
	}

	/**
	 * Checks if the <CODE>Images</CODE> has to be added at an absolute X
	 * position.
	 * 
	 * @return a boolean
	 */

	public boolean hasAbsoluteX() {
		return !Float.isNaN(absoluteX);
	}

	/**
	 * Returns the absolute X position.
	 * 
	 * @return a position
	 */

	public float absoluteX() {
		return absoluteX;
	}

	/**
	 * Returns the absolute Y position.
	 * 
	 * @return a position
	 */

	public float absoluteY() {
		return absoluteY;
	}

	/**
	 * Returns the type.
	 * 
	 * @return a type
	 */

	public int type() {
		return type;
	}

	/**
	 * Returns <CODE>true</CODE> if the image is a <CODE>Jpeg</CODE>
	 * -object.
	 * 
	 * @return a <CODE>boolean</CODE>
	 */

	public boolean isJpeg() {
		return type == JPEG;
	}

	/**
	 * Returns <CODE>true</CODE> if the image is a <CODE>ImgRaw</CODE>
	 * -object.
	 * 
	 * @return a <CODE>boolean</CODE>
	 */

	public boolean isImgRaw() {
		return type == IMGRAW;
	}

	/**
	 * Returns <CODE>true</CODE> if the image is an <CODE>ImgTemplate</CODE>
	 * -object.
	 * 
	 * @return a <CODE>boolean</CODE>
	 */

	public boolean isImgTemplate() {
		return type == IMGTEMPLATE;
	}

	/**
	 * Gets the <CODE>String</CODE> -representation of the reference to the
	 * image.
	 * 
	 * @return a <CODE>String</CODE>
	 */

	public URL url() {
		return url;
	}

	/**
	 * Gets the alignment for the image.
	 * 
	 * @return a value
	 */

	public int alignment() {
		return alignment;
	}

	/**
	 * Gets the alternative text for the image.
	 * 
	 * @return a <CODE>String</CODE>
	 */

	public String alt() {
		return alt;
	}

	/**
	 * Gets the scaled width of the image.
	 * 
	 * @return a value
	 */

	public float scaledWidth() {
		return scaledWidth;
	}

	/**
	 * Gets the scaled height of the image.
	 * 
	 * @return a value
	 */

	public float scaledHeight() {
		return scaledHeight;
	}

	/**
	 * Gets the colorspace for the image.
	 * <P>
	 * Remark: this only makes sense for Images of the type <CODE>Jpeg</CODE>.
	 * 
	 * @return a colorspace value
	 */

	public int colorspace() {
		return colorspace;
	}

	/**
	 * Returns the transformation matrix of the image.
	 * 
	 * @return an array [AX, AY, BX, BY, CX, CY, DX, DY]
	 */

	public float[] matrix() {
		float[] matrix = new float[8];
		float cosX = (float) Math.cos(rotation);
		float sinX = (float) Math.sin(rotation);
		matrix[AX] = plainWidth * cosX;
		matrix[AY] = plainWidth * sinX;
		matrix[BX] = (-plainHeight) * sinX;
		matrix[BY] = plainHeight * cosX;
		if (rotation < Math.PI / 2f) {
			matrix[CX] = matrix[BX];
			matrix[CY] = 0;
			matrix[DX] = matrix[AX];
			matrix[DY] = matrix[AY] + matrix[BY];
		} else if (rotation < Math.PI) {
			matrix[CX] = matrix[AX] + matrix[BX];
			matrix[CY] = matrix[BY];
			matrix[DX] = 0;
			matrix[DY] = matrix[AY];
		} else if (rotation < Math.PI * 1.5f) {
			matrix[CX] = matrix[AX];
			matrix[CY] = matrix[AY] + matrix[BY];
			matrix[DX] = matrix[BX];
			matrix[DY] = 0;
		} else {
			matrix[CX] = 0;
			matrix[CY] = matrix[AY];
			matrix[DX] = matrix[AX] + matrix[BX];
			matrix[DY] = matrix[BY];
		}
		return matrix;
	}

	/**
	 * This method is an alternative for the <CODE>InputStream.skip()</CODE>
	 * -method that doesn't seem to work properly for big values of <CODE>size
	 * </CODE>.
	 * 
	 * @param is
	 *            the <CODE>InputStream</CODE>
	 * @param size
	 *            the number of bytes to skip
	 * @throws IOException
	 */

	static public void skip(InputStream is, int size) throws IOException {
        long n;
		while (size > 0) {
            n = is.skip(size);
            if (n <= 0)
                break;
			size -= n;
		}
	}

    private static String excUri = " <>#%\"{}[]|\\\u005E\u0060";
    private static String[] excUriEsc = {"%20", "%3C", "%3E", "%23", "%25", "%22", "%7B", "%7D", "%5B", "%5D", "%7C", "%5C", "%5E", "%60"};
	/**
	 * This method makes a valid URL from a given filename.
	 * <P>
	 * This method makes the conversion of this library from the JAVA 2 platform
	 * to a JDK1.1.x-version easier.
	 * 
	 * @param filename
	 *            a given filename
	 * @return a valid URL
	 * @throws MalformedURLException
	 */

	public static URL toURL(String filename) throws MalformedURLException {
		if (filename.startsWith("file:/") || filename.startsWith("http://")
				|| filename.startsWith("https://")
				|| filename.startsWith("jar:")) {
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
        char[] t = path.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int k = 0; k < t.length; ++k) {
            char c = t[k];
            int a = excUri.indexOf(c);
            if (a >= 0)
                sb.append(excUriEsc[a]);
            else
                sb.append(c);
        }
		return new URL("file", "", sb.toString());
	}

	/**
	 * Returns the transparency.
	 * 
	 * @return the transparency values
	 */

	public int[] getTransparency() {
		return transparency;
	}

	/**
	 * Sets the transparency values
	 * 
	 * @param transparency
	 *            the transparency values
	 */
	public void setTransparency(int transparency[]) {
		this.transparency = transparency;
	}

	/**
	 * Checks if a given tag corresponds with this object.
	 * 
	 * @param tag
	 *            the given tag
	 * @return true if the tag corresponds
	 */

	public static boolean isTag(String tag) {
		return ElementTags.IMAGE.equals(tag);
	}

	/**
	 * Gets the plain width of the image.
	 * 
	 * @return a value
	 */

	public float plainWidth() {
		return plainWidth;
	}

	/**
	 * Gets the plain height of the image.
	 * 
	 * @return a value
	 */

	public float plainHeight() {
		return plainHeight;
	}

	static protected synchronized Long getSerialId() {
		++serialId;
		return new Long(serialId);
	}

	/**
	 * Returns a serial id for the Image (reuse the same image more than once)
	 * 
	 * @return a serialId
	 */
	public Long getMySerialId() {
		return mySerialId;
	}

	/**
	 * Gets the dots-per-inch in the X direction. Returns 0 if not available.
	 * 
	 * @return the dots-per-inch in the X direction
	 */
	public int getDpiX() {
		return dpiX;
	}

	/**
	 * Gets the dots-per-inch in the Y direction. Returns 0 if not available.
	 * 
	 * @return the dots-per-inch in the Y direction
	 */
	public int getDpiY() {
		return dpiY;
	}

	/**
	 * Sets the dots per inch value
	 * 
	 * @param dpiX
	 *            dpi for x coordinates
	 * @param dpiY
	 *            dpi for y coordinates
	 */
	public void setDpi(int dpiX, int dpiY) {
		this.dpiX = dpiX;
		this.dpiY = dpiY;
	}

	/**
	 * Returns <CODE>true</CODE> if this <CODE>Image</CODE> has the
	 * requisites to be a mask.
	 * 
	 * @return <CODE>true</CODE> if this <CODE>Image</CODE> can be a mask
	 */
	public boolean isMaskCandidate() {
		if (type == IMGRAW) {
			if (bpc > 0xff)
				return true;
		}
		return colorspace == 1;
	}

	/**
	 * Make this <CODE>Image</CODE> a mask.
	 * 
	 * @throws DocumentException
	 *             if this <CODE>Image</CODE> can not be a mask
	 */
	public void makeMask() throws DocumentException {
		if (!isMaskCandidate())
			throw new DocumentException("This image can not be an image mask.");
		mask = true;
	}

	/**
	 * Sets the explicit masking.
	 * 
	 * @param mask
	 *            the mask to be applied
	 * @throws DocumentException
	 *             on error
	 */
	public void setImageMask(Image mask) throws DocumentException {
		if (this.mask)
			throw new DocumentException(
					"An image mask cannot contain another image mask.");
		if (!mask.mask)
			throw new DocumentException(
					"The image mask is not a mask. Did you do makeMask()?");
		imageMask = mask;
		smask = (mask.bpc > 1 && mask.bpc <= 8);
	}

	/**
	 * Gets the explicit masking.
	 * 
	 * @return the explicit masking
	 */
	public Image getImageMask() {
		return imageMask;
	}

	/**
	 * Returns <CODE>true</CODE> if this <CODE>Image</CODE> is a mask.
	 * 
	 * @return <CODE>true</CODE> if this <CODE>Image</CODE> is a mask
	 */
	public boolean isMask() {
		return mask;
	}

	/**
	 * Inverts the meaning of the bits of a mask.
	 * 
	 * @param invert
	 *            <CODE>true</CODE> to invert the meaning of the bits of a
	 *            mask
	 */
	public void setInvertMask(boolean invert) {
		this.invert = invert;
	}

	/**
	 * Returns <CODE>true</CODE> if the bits are to be inverted in the mask.
	 * 
	 * @return <CODE>true</CODE> if the bits are to be inverted in the mask
	 */
	public boolean isInvertMask() {
		return invert;
	}

	/**
	 * Getter for the inverted value
	 * 
	 * @return true if the image is inverted
	 */
	public boolean isInverted() {
		return invert;
	}

	/**
	 * Sets inverted true or false
	 * 
	 * @param invert
	 *            true or false
	 */
	public void setInverted(boolean invert) {
		this.invert = invert;
	}

	/**
	 * Getter for property interpolation.
	 * 
	 * @return Value of property interpolation.
	 */
	public boolean isInterpolation() {
		return interpolation;
	}

	/**
	 * Sets the image interpolation. Image interpolation attempts to produce a
	 * smooth transition between adjacent sample values.
	 * 
	 * @param interpolation
	 *            New value of property interpolation.
	 */
	public void setInterpolation(boolean interpolation) {
		this.interpolation = interpolation;
	}

	/**
	 * @see com.lowagie.text.MarkupAttributes#setMarkupAttribute(java.lang.String,
	 *      java.lang.String)
	 */
	public void setMarkupAttribute(String name, String value) {
		if (markupAttributes == null) markupAttributes = new Properties();
		markupAttributes.put(name, value);
	}

	/**
	 * @see com.lowagie.text.MarkupAttributes#setMarkupAttributes(java.util.Properties)
	 */
	public void setMarkupAttributes(Properties markupAttributes) {
		this.markupAttributes = markupAttributes;
	}

	/**
	 * @see com.lowagie.text.MarkupAttributes#getMarkupAttribute(java.lang.String)
	 */
	public String getMarkupAttribute(String name) {
		return (markupAttributes == null) ? null : String
				.valueOf(markupAttributes.get(name));
	}

	/**
	 * @see com.lowagie.text.MarkupAttributes#getMarkupAttributeNames()
	 */
	public Set getMarkupAttributeNames() {
		return Chunk.getKeySet(markupAttributes);
	}

	/**
	 * @see com.lowagie.text.MarkupAttributes#getMarkupAttributes()
	 */
	public Properties getMarkupAttributes() {
		return markupAttributes;
	}

	/**
	 * Tags this image with an ICC profile.
	 * 
	 * @param profile
	 *            the profile
	 */
	public void tagICC(ICC_Profile profile) {
		this.profile = profile;
	}

	/**
	 * Checks is the image has an ICC profile.
	 * 
	 * @return the ICC profile or <CODE>null</CODE>
	 */
	public boolean hasICCProfile() {
		return (this.profile != null);
	}

	/**
	 * Gets the images ICC profile.
	 * 
	 * @return the ICC profile
	 */
	public ICC_Profile getICCProfile() {
		return profile;
	}

	/**
	 * Getter for property deflated.
	 * 
	 * @return Value of property deflated.
	 *  
	 */
	public boolean isDeflated() {
		return this.deflated;
	}

	/**
	 * Setter for property deflated.
	 * 
	 * @param deflated
	 *            New value of property deflated.
	 *  
	 */
	public void setDeflated(boolean deflated) {
		this.deflated = deflated;
	}

	/**
	 * Getter for property indexed.
	 * 
	 * @return Value of property indexed.
	 *  
	 */
	public PdfDictionary getAdditional() {
		return this.additional;
	}

	/**
	 * Sets the /Colorspace key.
	 * 
	 * @param additional
	 *            New value of property indexed.
	 */
	public void setAdditional(PdfDictionary additional) {
		this.additional = additional;
	}

	/**
	 * Getter for property smask.
	 * 
	 * @return Value of property smask.
	 *  
	 */
	public boolean isSmask() {
		return this.smask;
	}

	/**
	 * Setter for property smask.
	 * 
	 * @param smask
	 *            New value of property smask.
	 *  
	 */
	public void setSmask(boolean smask) {
		this.smask = smask;
	}

	/**
	 * Gets the X/Y pixel dimensionless aspect ratio.
	 * 
	 * @return the X/Y pixel dimensionless aspect ratio
	 */
	public float getXYRatio() {
		return this.XYRatio;
	}

	/**
	 * Sets the X/Y pixel dimensionless aspect ratio.
	 * 
	 * @param XYRatio
	 *            the X/Y pixel dimensionless aspect ratio
	 */
	public void setXYRatio(float XYRatio) {
		this.XYRatio = XYRatio;
	}

	/**
	 * Gets the left indentation.
	 * 
	 * @return the left indentation
	 */
	public float indentationLeft() {
		return indentationLeft;
	}

	/**
	 * Gets the right indentation.
	 * 
	 * @return the right indentation
	 */
	public float indentationRight() {
		return indentationRight;
	}

	/**
	 * Sets the left indentation.
	 * 
	 * @param f
	 */
	public void setIndentationLeft(float f) {
		indentationLeft = f;
	}

	/**
	 * Sets the right indentation.
	 * 
	 * @param f
	 */
	public void setIndentationRight(float f) {
		indentationRight = f;
	}

	/**
	 * Getter for property originalType.
	 * 
	 * @return Value of property originalType.
	 *  
	 */
	public int getOriginalType() {
		return this.originalType;
	}

	/**
	 * Setter for property originalType.
	 * 
	 * @param originalType
	 *            New value of property originalType.
	 *  
	 */
	public void setOriginalType(int originalType) {
		this.originalType = originalType;
	}

	/**
	 * Getter for property originalData.
	 * 
	 * @return Value of property originalData.
	 *  
	 */
	public byte[] getOriginalData() {
		return this.originalData;
	}

	/**
	 * Setter for property originalData.
	 * 
	 * @param originalData
	 *            New value of property originalData.
	 *  
	 */
	public void setOriginalData(byte[] originalData) {
		this.originalData = originalData;
	}

	/**
	 * Sets the url of the image
	 * 
	 * @param url
	 *            the url of the image
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/**
	 * Sets the spacing before this image.
	 * 
	 * @param spacing
	 *            the new spacing
	 */

	public void setSpacingBefore(float spacing) {
		this.spacingBefore = spacing;
	}

	/**
	 * Sets the spacing after this image.
	 * 
	 * @param spacing
	 *            the new spacing
	 */

	public void setSpacingAfter(float spacing) {
		this.spacingAfter = spacing;
	}

	/**
	 * Gets the spacing before this image.
	 * 
	 * @return the spacing
	 */

	public float spacingBefore() {
		return spacingBefore;
	}

	/**
	 * Gets the spacing before this image.
	 * 
	 * @return the spacing
	 */

	public float spacingAfter() {
		return spacingAfter;
	}

	/**
	 * Getter for property widthPercentage.
	 * 
	 * @return Value of property widthPercentage.
	 */
	public float getWidthPercentage() {
		return this.widthPercentage;
	}

	/**
	 * Setter for property widthPercentage.
	 * 
	 * @param widthPercentage
	 *            New value of property widthPercentage.
	 */
	public void setWidthPercentage(float widthPercentage) {
		this.widthPercentage = widthPercentage;
	}

	/**
	 * Gets the layer this image belongs to.
	 * 
	 * @return the layer this image belongs to or <code>null</code> for no
	 *         layer defined
	 */
	public PdfOCG getLayer() {
		return layer;
	}

	/**
	 * Sets the layer this image belongs to.
	 * 
	 * @param layer
	 *            the layer this image belongs to
	 */
	public void setLayer(PdfOCG layer) {
		this.layer = layer;
	}

    private PdfObject simplifyColorspace(PdfObject obj) {
        if (obj == null || !obj.isArray())
            return obj;
        PdfObject first = (PdfObject)(((PdfArray)obj).getArrayList().get(0));
        if (PdfName.CALGRAY.equals(first))
            return PdfName.DEVICEGRAY;
        else if (PdfName.CALRGB.equals(first))
            return PdfName.DEVICERGB;
        else
            return obj;
    }

    /**
     * Replaces CalRGB and CalGray colorspaces with DeviceRGB and DeviceGray.
     */    
    public void simplifyColorspace() {
        if (additional == null)
            return;
        PdfObject value = additional.get(PdfName.COLORSPACE);
        if (value == null || !value.isArray())
            return;
        PdfObject cs = simplifyColorspace(value);
        if (cs.isName())
            value = cs;
        else {
            PdfObject first = (PdfObject)(((PdfArray)value).getArrayList().get(0));
            if (PdfName.INDEXED.equals(first)) {
                ArrayList array = ((PdfArray)value).getArrayList();
                if (array.size() >= 2 && ((PdfObject)array.get(1)).isArray()) {
                     array.set(1, simplifyColorspace((PdfObject)array.get(1)));
                }
            }
        }
        additional.put(PdfName.COLORSPACE, value);
    }
    
    /**
     * Getter for property initialRotation.
     * @return Value of property initialRotation.
     */
    public float getInitialRotation() {
        return this.initialRotation;
    }
    
    /**
     * Some image formats, like TIFF may present the images rotated that have
     * to be compensated.
     * @param initialRotation New value of property initialRotation.
     */
    public void setInitialRotation(float initialRotation) {
        float old_rot = rotation - this.initialRotation;
        this.initialRotation = initialRotation;
        setRotation(old_rot);
    }
    
}