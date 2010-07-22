/*
 * $Id: ContentOperator.java 4242 2010-01-02 23:22:20Z xlv $
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
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

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfReader;

/**
 * Represents image data from a PDF
 * @since 5.0.1
 */
public class ImageRenderInfo {
    /** The coordinate transformation matrix that was in effect when the image was rendered */
    private final Matrix ctm;
    /** A reference to the image XObject */
    private final PdfIndirectReference ref;
    
    private ImageRenderInfo(Matrix ctm, PdfIndirectReference ref) {
        this.ctm = ctm;
        this.ref = ref;
    }
    
    /**
     * Create an ImageRenderInfo object based on an XObject (this is the most common way of including an image in PDF)
     * @param ctm the coordinate transformation matrix at the time the image is rendered
     * @param ref a reference to the image XObject
     * @return the ImageRenderInfo representing the rendered XObject
     * @since 5.0.1
     */
    public static ImageRenderInfo createForXObject(Matrix ctm, PdfIndirectReference ref){
        return new ImageRenderInfo(ctm, ref);
    }
    
    /**
     * Create an ImageRenderInfo object based on embedded image data.  This is nowhere near completely thought through
     * and really just acts as a placeholder.
     * @param ctm the coordinate transformation matrix at the time the image is rendered
     * @param imageDictionary a dictionary containing parameters of the embedded image (note that the key/value pairs of this dictionary can have abbreviations in them)
     * @param streamBytes the bytes of the image data
     * @return the ImageRenderInfo representing the rendered embedded image
     * @since 5.0.1
     */
    protected static ImageRenderInfo createdForEmbeddedImage(Matrix ctm, PdfDictionary imageDictionary, byte[] streamBytes){
        return new ImageRenderInfo(ctm, null);
    }
    
    /**
     * Gets an object containing the image dictionary and bytes.
     * @return an object containing the image dictionary and byte[]
     * @since 5.0.2
     */
    public PdfImageObject getImage() {
		PRStream stream = (PRStream)PdfReader.getPdfObject(ref);
		return new PdfImageObject(stream);
    }
    
    /**
     * @return a vector in User space representing the start point of the xobject
     */
    public Vector getStartPoint(){ 
        return new Vector(0, 0, 1).cross(ctm); 
    }

    /**
     * @return the size of the image, in User space units
     * @since 5.0.3
     */
    public float getArea(){
        // the image space area is 1, so we multiply that by the determinant of the CTM to get the transformed area
        return ctm.getDeterminant();
    }
    
    /**
     * @return an indirect reference to the image
     * @since 5.0.2
     */
    public PdfIndirectReference getRef() {
    	return ref;
    }
}
