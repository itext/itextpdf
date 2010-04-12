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

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

/**
 * An object that contains an image dictionary and image bytes.
 * @since 5.0.2
 */
public class PdfImageObject {

	/** The image dictionary. */
	protected PdfDictionary dictionary;
	/** The image bytes. */
	protected byte[] streamBytes;
	
	/**
	 * Creates a PdfImage object.
	 * @param stream a PRStream
	 * @throws IOException
	 */
	public PdfImageObject(PRStream stream) {
		this.dictionary = stream;
		try {
			if (PdfName.FLATEDECODE.equals(dictionary.getAsName(PdfName.FILTER)))
				streamBytes = PdfReader.getStreamBytes(stream);
			// else if other filter (not supported yet)
			else
				streamBytes = PdfReader.getStreamBytesRaw(stream);
		}
		catch(IOException ioe) {
			streamBytes = null;
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
	
	public BufferedImage getAwtImage() throws IOException {
		PdfName filter = dictionary.getAsName(PdfName.FILTER);
		if (PdfName.DCTDECODE.equals(filter)) {
			return ImageIO.read(new ByteArrayInputStream(streamBytes));
		}
		if (!PdfName.FLATEDECODE.equals(filter)) {
			return null;
		}
		BufferedImage bi = null;
		DataBuffer db = new DataBufferByte(streamBytes, streamBytes.length);
		int width = dictionary.getAsNumber(PdfName.WIDTH).intValue();
		int height = dictionary.getAsNumber(PdfName.HEIGHT).intValue();
		WritableRaster raster;
		int bpc = dictionary.getAsNumber(PdfName.BITSPERCOMPONENT).intValue();
		switch(bpc) {
		case 1:
			raster = Raster.createPackedRaster( db, width, height, 1, null );
			bi = new BufferedImage( width, height, BufferedImage.TYPE_BYTE_BINARY );
			bi.setData( raster );
			break;
		default:
			PdfObject colorspace = dictionary.getDirectObject(PdfName.COLORSPACE);
			if (PdfName.DEVICERGB.equals(colorspace)) {
				if (width * height == streamBytes.length) {
					bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED );
					raster = Raster.createPackedRaster(db, width, height, bpc, null);
					bi.setData(raster);
				}
				else {
					bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
					raster = Raster.createInterleavedRaster(db, width, height,
							width * 3, 3, new int[]{0, 1, 2}, null );
					bi.setData(raster);
				}
			}
			else if (colorspace instanceof PdfArray) {
				PdfArray colorspacearray = (PdfArray) colorspace;
				if (PdfName.INDEXED.equals(colorspacearray.getAsName(0))) {
					int hival = colorspacearray.getAsNumber(2).intValue();
					byte[] index = colorspacearray.getDirectObject(3).getBytes();
					raster = Raster.createPackedRaster( db, width, height, bpc, null );
					ColorModel cm = new IndexColorModel(bpc, hival + 1, index, 0, false);
					bi = new BufferedImage(cm, raster, false, null);
				}
				else {
					bi = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );
					raster = Raster.createInterleavedRaster(db, width, height,
							width * 3, 3, new int[]{0, 1, 2}, null );
					bi.setData(raster);
				}
			} 
		}
		return bi;
	}
}
