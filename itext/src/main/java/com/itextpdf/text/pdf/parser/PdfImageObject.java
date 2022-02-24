/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Kevin Day, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.itextpdf.text.Version;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.pdf.FilterHandlers;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.codec.PngWriter;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.TiffWriter;

/**
 * An object that contains an image dictionary and image bytes.
 * @since 5.0.2
 */
public class PdfImageObject {

    /**
     * Different types of data that can be stored in the bytes of a {@link PdfImageObject}
     * @since 5.0.4
     */
    public static enum ImageBytesType{
        PNG("png"), // the stream contains png encoded data
        JPG("jpg"), // the stream contains jpg encoded data
        JP2("jp2"), // the stream contains jp2 encoded data
        CCITT("tif"), // the stream contains ccitt encoded data
        JBIG2("jbig2") // the stream contains JBIG2 encoded data
        ;
        
        /**
         * the recommended file extension for streams of this type
         */
        private final String fileExtension;
        
        /**
         * @param fileExtension the recommended file extension for use with data of this type (for example, if the bytes were just saved to a file, what extension should the file have)
         */
        private ImageBytesType(String fileExtension) {
            this.fileExtension = fileExtension;
        }
        
        /**
         * @return the file extension registered when this type was created
         */
        public String getFileExtension() {
            return fileExtension;
        }
    }

    /**
     * A filter that does nothing, but keeps track of the filter type that was used
     * @since 5.0.4 
     */
    private static class TrackingFilter implements FilterHandlers.FilterHandler{
        public PdfName lastFilterName = null;
        
        public byte[] decode(byte[] b, PdfName filterName, PdfObject decodeParams, PdfDictionary streamDictionary) throws IOException {
            lastFilterName = filterName;
            return b;
        }
        
    }
    
	/** The image dictionary. */
	private PdfDictionary dictionary;
	/** The decoded image bytes (after applying filters), or the raw image bytes if unable to decode */
	private byte[] imageBytes;
	private PdfDictionary colorSpaceDic;
	
    private int pngColorType = -1;
    private int pngBitDepth;
    private int width;
    private int height;
    private int bpc;
    private byte[] palette;
    private byte[] icc;
    private int stride;

    /**
     * Tracks the type of data that is actually stored in the streamBytes member
     */
    private ImageBytesType streamContentType = null;
    
    public String getFileType() {
        return streamContentType.getFileExtension();
    }
    
    /**
     * @return the type of image data that is returned by getImageBytes()
     */
    public ImageBytesType getImageBytesType(){
        return streamContentType;
    }
    
	/**
	 * Creates a PdfImage object.
	 * @param stream a PRStream
	 * @throws IOException
	 */
	public PdfImageObject(PRStream stream) throws IOException {
		this(stream, PdfReader.getStreamBytesRaw(stream), null);
	}
    
	/**
	 * Creates a PdfImage object.
	 * @param stream a PRStream
	 * @param colorSpaceDic	a color space dictionary
	 * @throws IOException
	 */
	public PdfImageObject(PRStream stream, PdfDictionary colorSpaceDic) throws IOException {
		this(stream, PdfReader.getStreamBytesRaw(stream), colorSpaceDic);
	}
	

	
	/**
	 * Creats a PdfImage object using an explicitly provided dictionary and image bytes
	 * @param dictionary the dictionary for the image
	 * @param samples the samples
	 * @param colorSpaceDic	a color space dictionary
	 * @since 5.0.3
	 */
	protected PdfImageObject(PdfDictionary dictionary, byte[] samples, PdfDictionary colorSpaceDic) throws IOException {
	    this.dictionary = dictionary;
	    this.colorSpaceDic = colorSpaceDic;
        TrackingFilter trackingFilter = new TrackingFilter();
        Map<PdfName, FilterHandlers.FilterHandler> handlers = new HashMap<PdfName, FilterHandlers.FilterHandler>(FilterHandlers.getDefaultFilterHandlers());
        handlers.put(PdfName.JBIG2DECODE, trackingFilter);
        handlers.put(PdfName.DCTDECODE, trackingFilter);
        handlers.put(PdfName.JPXDECODE, trackingFilter);

        imageBytes = PdfReader.decodeBytes(samples, dictionary, handlers);
        
        if (trackingFilter.lastFilterName != null){
	        if (PdfName.JBIG2DECODE.equals(trackingFilter.lastFilterName))
	            streamContentType = ImageBytesType.JBIG2;
	        else if (PdfName.DCTDECODE.equals(trackingFilter.lastFilterName))
	            streamContentType = ImageBytesType.JPG;
            else if (PdfName.JPXDECODE.equals(trackingFilter.lastFilterName))
                streamContentType = ImageBytesType.JP2;
        } else {
            decodeImageBytes();
        }
	}
	
	/**
	 * Returns an entry from the image dictionary.
	 * @param key a key
	 * @return the value
	 */
	public PdfObject get(PdfName key) {
		return dictionary.get(key);
	}
	
	/**
	 * Returns the image dictionary.
	 * @return the dictionary
	 */
	public PdfDictionary getDictionary() {
		return dictionary;
	}

	/**
	 * Sets state of this object according to the color space 
	 * @param colorspace the colorspace to use
	 * @param allowIndexed whether indexed color spaces will be resolved (used for recursive call)
	 * @throws IOException if there is a problem with reading from the underlying stream  
	 */
    private void findColorspace(PdfObject colorspace, boolean allowIndexed) throws IOException {
        if (colorspace == null && bpc == 1){ // handle imagemasks
            stride = (width*bpc + 7) / 8;
            pngColorType = 0;
        }
        else if (PdfName.DEVICEGRAY.equals(colorspace)) {
            stride = (width * bpc + 7) / 8;
            pngColorType = 0;
        }
        else if (PdfName.DEVICERGB.equals(colorspace)) {
            if (bpc == 8 || bpc == 16) {
                stride = (width * bpc * 3 + 7) / 8;
                pngColorType = 2;
            }
        }
        else if (colorspace instanceof PdfArray) {
            PdfArray ca = (PdfArray)colorspace;
            PdfObject tyca = ca.getDirectObject(0);
            if (PdfName.CALGRAY.equals(tyca)) {
                stride = (width * bpc + 7) / 8;
                pngColorType = 0;
            }
            else if (PdfName.CALRGB.equals(tyca)) {
                if (bpc == 8 || bpc == 16) {
                    stride = (width * bpc * 3 + 7) / 8;
                    pngColorType = 2;
                }
            }
            else if (PdfName.ICCBASED.equals(tyca)) {
                PRStream pr = (PRStream)ca.getDirectObject(1);
                int n = pr.getAsNumber(PdfName.N).intValue();
                if (n == 1) {
                    stride = (width * bpc + 7) / 8;
                    pngColorType = 0;
                    icc = PdfReader.getStreamBytes(pr);
                }
                else if (n == 3) {
                    stride = (width * bpc * 3 + 7) / 8;
                    pngColorType = 2;
                    icc = PdfReader.getStreamBytes(pr);
                }
            }
            else if (allowIndexed && PdfName.INDEXED.equals(tyca)) {
                findColorspace(ca.getDirectObject(1), false);
                if (pngColorType == 2) {
                    PdfObject id2 = ca.getDirectObject(3);
                    if (id2 instanceof PdfString) {
                        palette = ((PdfString)id2).getBytes();
                    }
                    else if (id2 instanceof PRStream) {
                        palette = PdfReader.getStreamBytes(((PRStream)id2));
                    }
                    stride = (width * bpc + 7) / 8;
                    pngColorType = 3;
                }
            }
        }
    }

    /**
     * decodes the bytes currently captured in the streamBytes and replaces it with an image representation of the bytes
     * (this will either be a png or a tiff, depending on the color depth of the image)
     * @throws IOException
     */
    private void decodeImageBytes() throws IOException{
        if (streamContentType != null)
            throw new IllegalStateException(MessageLocalization.getComposedMessage("Decoding.can't.happen.on.this.type.of.stream.(.1.)", streamContentType));
        
        pngColorType = -1;
        PdfArray decode = dictionary.getAsArray(PdfName.DECODE);
        width = dictionary.getAsNumber(PdfName.WIDTH).intValue();
        height = dictionary.getAsNumber(PdfName.HEIGHT).intValue();
        bpc = dictionary.getAsNumber(PdfName.BITSPERCOMPONENT).intValue();
        pngBitDepth = bpc;
        PdfObject colorspace = dictionary.getDirectObject(PdfName.COLORSPACE);
        if (colorspace instanceof PdfName && colorSpaceDic != null){
            PdfObject csLookup = colorSpaceDic.getDirectObject((PdfName)colorspace);
            if (csLookup != null)
                colorspace = csLookup;
        }

        palette = null;
        icc = null;
        stride = 0;
        findColorspace(colorspace, true);
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        if (pngColorType < 0) {
            if (bpc != 8)
                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.color.depth.1.is.not.supported", bpc));

            if (PdfName.DEVICECMYK.equals(colorspace)) {
            }
            else if (colorspace instanceof PdfArray) {
                PdfArray ca = (PdfArray)colorspace;
                PdfObject tyca = ca.getDirectObject(0);
                if (!PdfName.ICCBASED.equals(tyca))
                    throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.color.space.1.is.not.supported", colorspace));
                PRStream pr = (PRStream)ca.getDirectObject(1);
                int n = pr.getAsNumber(PdfName.N).intValue();
                if (n != 4) {
                    throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("N.value.1.is.not.supported", n));
                }
                icc = PdfReader.getStreamBytes(pr);
            }
            else
                throw new UnsupportedPdfException(MessageLocalization.getComposedMessage("the.color.space.1.is.not.supported", colorspace));
            stride = 4 * width;
            TiffWriter wr = new TiffWriter();
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_SAMPLESPERPIXEL, 4));
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_BITSPERSAMPLE, new int[]{8,8,8,8}));
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_PHOTOMETRIC, TIFFConstants.PHOTOMETRIC_SEPARATED));
            wr.addField(new TiffWriter.FieldLong(TIFFConstants.TIFFTAG_IMAGEWIDTH, width));
            wr.addField(new TiffWriter.FieldLong(TIFFConstants.TIFFTAG_IMAGELENGTH, height));
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_COMPRESSION, TIFFConstants.COMPRESSION_LZW));
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_PREDICTOR, TIFFConstants.PREDICTOR_HORIZONTAL_DIFFERENCING));
            wr.addField(new TiffWriter.FieldLong(TIFFConstants.TIFFTAG_ROWSPERSTRIP, height));
            wr.addField(new TiffWriter.FieldRational(TIFFConstants.TIFFTAG_XRESOLUTION, new int[]{300,1}));
            wr.addField(new TiffWriter.FieldRational(TIFFConstants.TIFFTAG_YRESOLUTION, new int[]{300,1}));
            wr.addField(new TiffWriter.FieldShort(TIFFConstants.TIFFTAG_RESOLUTIONUNIT, TIFFConstants.RESUNIT_INCH));
            wr.addField(new TiffWriter.FieldAscii(TIFFConstants.TIFFTAG_SOFTWARE, Version.getInstance().getVersion()));
            ByteArrayOutputStream comp = new ByteArrayOutputStream();
            TiffWriter.compressLZW(comp, 2, imageBytes, height, 4, stride);
            byte[] buf = comp.toByteArray();
            wr.addField(new TiffWriter.FieldImage(buf));
            wr.addField(new TiffWriter.FieldLong(TIFFConstants.TIFFTAG_STRIPBYTECOUNTS, buf.length));
            if (icc != null)
                wr.addField(new TiffWriter.FieldUndefined(TIFFConstants.TIFFTAG_ICCPROFILE, icc));
            wr.writeFile(ms);
            streamContentType = ImageBytesType.CCITT;
            imageBytes = ms.toByteArray();
            return;
        } else {
            PngWriter png = new PngWriter(ms);
            if (decode != null){
                if (pngBitDepth == 1){
                    // if the decode array is 1,0, then we need to invert the image
                    if(decode.getAsNumber(0).intValue() == 1 && decode.getAsNumber(1).intValue() == 0){
                        int len = imageBytes.length;
                        for (int t = 0; t < len; ++t) {
                            imageBytes[t] ^= 0xff;
                        }
                    } else {
                        // if the decode array is 0,1, do nothing.  It's possible that the array could be 0,0 or 1,1 - but that would be silly, so we'll just ignore that case
                    }
                } else {
                    // todo: add decode transformation for other depths
                }
            }
            png.writeHeader(width, height, pngBitDepth, pngColorType);
            if (icc != null)
                png.writeIccProfile(icc);
            if (palette != null)
                png.writePalette(palette);
            png.writeData(imageBytes, stride);
            png.writeEnd();
            streamContentType = ImageBytesType.PNG;
            imageBytes = ms.toByteArray();
        }
    }
    
    /**
     * @return the bytes of the image (the format will be as specified in {@link PdfImageObject#getImageBytesType()}
     * @throws IOException
     * @since 5.0.4
     */
    public byte[] getImageAsBytes() {
        return imageBytes; 
    }

    // AWT related methods (remove this if you port to Android / GAE)

    /**
     * @since 5.0.3 renamed from getAwtImage()
     */
    public java.awt.image.BufferedImage getBufferedImage() throws IOException {
        byte[] img = getImageAsBytes();
        if (img == null)
            return null;
        return ImageIO.read(new ByteArrayInputStream(img));
    }
}
