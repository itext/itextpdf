/*
 * $Id: JBIG2Image.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf.codec;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgJBIG2;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * Support for JBIG2 Images.
 * This class assumes that we are always embedding into a pdf.
 * 
 * @since 2.1.5
 */
public class JBIG2Image {

	/**
	 * Gets a byte array that can be used as a /JBIG2Globals,
	 * or null if not applicable to the given jbig2.
	 * @param	ra	an random access file or array
	 * @return	a byte array
	 */
	public static byte[] getGlobalSegment(RandomAccessFileOrArray ra ) {
		try {
			JBIG2SegmentReader sr = new JBIG2SegmentReader(ra);
			sr.read();
			return sr.getGlobal(true);
		} catch (Exception e) {
	        return null;
	    }
	}
	
	/**
	 * returns an Image representing the given page.
	 * @param ra	the file or array containing the image
	 * @param page	the page number of the image
	 * @return	an Image object
	 */
	public static Image getJbig2Image(RandomAccessFileOrArray ra, int page) {
		if (page < 1)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.page.number.must.be.gt.eq.1"));
		
		try {
			JBIG2SegmentReader sr = new JBIG2SegmentReader(ra);
			sr.read();
			JBIG2SegmentReader.JBIG2Page p = sr.getPage(page);
			Image img = new ImgJBIG2(p.pageBitmapWidth, p.pageBitmapHeight, p.getData(true), sr.getGlobal(true));
			return img;
		} catch (Exception e) {
	        throw new ExceptionConverter(e);
	    }
	}

	/***
	 * Gets the number of pages in a JBIG2 image.
	 * @param ra	a random acces file array containing a JBIG2 image
	 * @return	the number of pages
	 */
	public static int getNumberOfPages(RandomAccessFileOrArray ra) {
		try {
			JBIG2SegmentReader sr = new JBIG2SegmentReader(ra);
			sr.read();
			return sr.numberOfPages();
		} catch (Exception e) {
	        throw new ExceptionConverter(e);
	    }
    }
	
	
}
