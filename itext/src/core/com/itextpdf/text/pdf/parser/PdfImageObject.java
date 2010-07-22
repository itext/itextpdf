/*
 * $Id: ContentOperator.java 4242 2010-01-02 23:22:20Z xlv $
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Kevin Day, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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

import com.itextpdf.text.Document;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

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
import java.io.ByteArrayOutputStream;

/**
 * An object that contains an image dictionary and image bytes.
 * @since 5.0.2
 */
public class PdfImageObject {

	/** The image dictionary. */
	protected PdfDictionary dictionary;
	/** The image bytes. */
	protected byte[] streamBytes;
	
    private int pngColorType = -1;
    private int pngBitDepth;
    private int width;
    private int height;
    private int bpc;
    private byte[] palette;
    private byte[] icc;
    private int stride;
    private boolean  decoded;
    public static final String TYPE_PNG = "png";
    public static final String TYPE_JPG = "jpg";
    public static final String TYPE_JP2 = "jp2";
    public static final String TYPE_TIF = "tif";

    protected String fileType;

    public String getFileType() {
        return fileType;
    }
	/**
	 * Creates a PdfImage object.
	 * @param stream a PRStream
	 * @throws IOException
	 */
	public PdfImageObject(PRStream stream) {
		this.dictionary = stream;
        try {
            streamBytes = PdfReader.getStreamBytes(stream);
            decoded = true;
        }
        catch (Exception e) {
            try {
                streamBytes = PdfReader.getStreamBytesRaw(stream);
            }
            catch (Exception e2) {}
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
	 * Returns the image bytes.
	 * @return the streamBytes
	 */
	public byte[] getStreamBytes() {
		return streamBytes;
	}
	
    private void findColorspace(PdfObject colorspace, boolean allowIndexed) throws IOException {
        if (PdfName.DEVICEGRAY.equals(colorspace)) {
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

    public byte[] getImageAsBytes() throws IOException {
        if (streamBytes == null)
            return null;
        if (!decoded) {
            PdfName filter = dictionary.getAsName(PdfName.FILTER);
            if (PdfName.DCTDECODE.equals(filter)) {
                fileType = TYPE_JPG;
                return streamBytes;
            }
            else if (PdfName.JPXDECODE.equals(filter)) {
                fileType = TYPE_JP2;
                return streamBytes;
            }
            return null;
        }
        pngColorType = -1;
        width = dictionary.getAsNumber(PdfName.WIDTH).intValue();
        height = dictionary.getAsNumber(PdfName.HEIGHT).intValue();
        bpc = dictionary.getAsNumber(PdfName.BITSPERCOMPONENT).intValue();
        pngBitDepth = bpc;
        PdfObject colorspace = dictionary.getDirectObject(PdfName.COLORSPACE);
        palette = null;
        icc = null;
        stride = 0;
        findColorspace(colorspace, true);
        ByteArrayOutputStream ms = new ByteArrayOutputStream();
        if (pngColorType < 0) {
            if (bpc != 8)
                return null;
            if (PdfName.DEVICECMYK.equals(colorspace)) {
            }
            else if (colorspace instanceof PdfArray) {
                PdfArray ca = (PdfArray)colorspace;
                PdfObject tyca = ca.getDirectObject(0);
                if (!PdfName.ICCBASED.equals(tyca))
                    return null;
                PRStream pr = (PRStream)ca.getDirectObject(1);
                int n = pr.getAsNumber(PdfName.N).intValue();
                if (n != 4) {
                    return null;
                }
                icc = PdfReader.getStreamBytes(pr);
            }
            else
                return null;
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
            wr.addField(new TiffWriter.FieldAscii(TIFFConstants.TIFFTAG_SOFTWARE, Document.getVersion()));
            ByteArrayOutputStream comp = new ByteArrayOutputStream();
            TiffWriter.compressLZW(comp, 2, streamBytes, height, 4, stride);
            byte[] buf = comp.toByteArray();
            wr.addField(new TiffWriter.FieldImage(buf));
            wr.addField(new TiffWriter.FieldLong(TIFFConstants.TIFFTAG_STRIPBYTECOUNTS, buf.length));
            if (icc != null)
                wr.addField(new TiffWriter.FieldUndefined(TIFFConstants.TIFFTAG_ICCPROFILE, icc));
            wr.writeFile(ms);
            fileType = TYPE_TIF;
            return ms.toByteArray();
        }
        PngWriter png = new PngWriter(ms);
        png.writeHeader(width, height, pngBitDepth, pngColorType);
        if (icc != null)
            png.writeIccProfile(icc);
        if (palette != null)
            png.writePalette(palette);
        png.writeData(streamBytes, stride);
        png.writeEnd();
        fileType = TYPE_PNG;
        return ms.toByteArray();
    }

    /**
     * @since 5.0.3 renamed from getAwtImage()
     */
    public BufferedImage getBufferedImage() throws IOException {
        byte[] img = getImageAsBytes();
        if (img == null)
            return null;
        return ImageIO.read(new ByteArrayInputStream(img));
    }
}
