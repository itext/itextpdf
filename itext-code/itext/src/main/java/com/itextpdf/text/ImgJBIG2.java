/*
 * $Id: ImgJBIG2.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text;

import java.net.URL;
import java.security.MessageDigest;

/**
 * Support for JBIG2 images.
 * @since 2.1.5
 */
public class ImgJBIG2 extends Image {
	
	/** JBIG2 globals */
	private  byte[] global;
	/** A unique hash */
	private  byte[] globalHash;
	
	/**
	 * Copy contstructor.
	 * @param	image another Image
	 */
	ImgJBIG2(Image image) {
		super(image);
	}

	/**
	 * Empty constructor.
	 */
	public ImgJBIG2() {
		super((Image) null);
	}

	/**
	 * Actual constructor for ImgJBIG2 images.
	 * @param	width	the width of the image
	 * @param	height	the height of the image
	 * @param	data	the raw image data
	 * @param	globals	JBIG2 globals
	 */
	public ImgJBIG2(int width, int height, byte[] data, byte[] globals) {
		super((URL) null);
        type = JBIG2;
        originalType = ORIGINAL_JBIG2;
		scaledHeight = height;
		setTop(scaledHeight);
		scaledWidth = width;
		setRight(scaledWidth);
		bpc = 1;
		colorspace = 1;
		rawData = data;
		plainWidth = getWidth();
		plainHeight = getHeight();
		if ( globals != null ) {
			this.global = globals;
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("MD5");
				md.update(this.global);
				this.globalHash = md.digest();
			} catch (Exception e) {
				//ignore
			}
			
		}
	}
	
	/**
	 * Getter for the JBIG2 global data.
	 * @return 	an array of bytes
	 */
	public byte[] getGlobalBytes() {
		return this.global;
	}
	
	/**
	 * Getter for the unique hash.
	 * @return	an array of bytes
	 */
	public byte[] getGlobalHash() {
		return this.globalHash;
	}

}
