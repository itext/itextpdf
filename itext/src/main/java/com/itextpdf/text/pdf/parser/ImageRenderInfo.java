/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfReader;

/**
 * Represents image data from a PDF
 * @since 5.0.1
 */
public class ImageRenderInfo {
	/** The graphics state that was in effect when the image was rendered */
	private final GraphicsState gs;
    /** A reference to the image XObject */
    private final PdfIndirectReference ref;
    /** A reference to an inline image */
    private final InlineImageInfo inlineImageInfo;
    /** the color space associated with the image */
    private final PdfDictionary colorSpaceDictionary;
    /** the image object to be rendered, if it has been parsed already.  Null otherwise. */
    private PdfImageObject imageObject = null;

    /**
     * Array containing marked content info for the text.
     * @since 5.0.2
     */
    private final Collection<MarkedContentInfo> markedContentInfos;


    private ImageRenderInfo(GraphicsState gs, PdfIndirectReference ref, PdfDictionary colorSpaceDictionary,Collection<MarkedContentInfo> markedContentInfo) {
        this.gs = gs;
        this.ref = ref;
        this.inlineImageInfo = null;
        this.colorSpaceDictionary = colorSpaceDictionary;
        this.markedContentInfos = new ArrayList<MarkedContentInfo>();
        if(markedContentInfo != null) {
            this.markedContentInfos.addAll(markedContentInfo);
        }
    }

    private ImageRenderInfo(GraphicsState gs, InlineImageInfo inlineImageInfo, PdfDictionary colorSpaceDictionary,Collection<MarkedContentInfo> markedContentInfo) {
        this.gs = gs;
        this.ref = null;
        this.inlineImageInfo = inlineImageInfo;
        this.colorSpaceDictionary = colorSpaceDictionary;
        this.markedContentInfos = new ArrayList<MarkedContentInfo>();
        if(markedContentInfo != null) {
            this.markedContentInfos.addAll(markedContentInfo);
        }
    }
    /**
     * Create an ImageRenderInfo object based on an XObject (this is the most common way of including an image in PDF)
     * @param gs graphic state of the XObject
     * @param ref a reference to the image XObject
     * @param colorSpaceDictionary colourspace of the image
     * @return the ImageRenderInfo representing the rendered XObject
     * @since 5.0.1
     */
    public static ImageRenderInfo createForXObject(GraphicsState gs, PdfIndirectReference ref, PdfDictionary colorSpaceDictionary){
        return new ImageRenderInfo(gs, ref, colorSpaceDictionary,null);
    }

    /**
     * Create an ImageRenderInfo object based on an XObject (this is the most common way of including an image in PDF)
     * @param gs graphic state of the XObject
     * @param ref a reference to the image XObject
     * @param colorSpaceDictionary colourspace of the image
     * @param markedContentInfo marked content information for the XObject
     * @return the ImageRenderInfo representing the rendered XObject
     * @since 5.5.11
     */
    public static ImageRenderInfo createForXObject(GraphicsState gs, PdfIndirectReference ref, PdfDictionary colorSpaceDictionary, Collection<MarkedContentInfo> markedContentInfo){
        return new ImageRenderInfo(gs, ref, colorSpaceDictionary,markedContentInfo);
    }
    
    /**
     * Create an ImageRenderInfo object based on inline image data.
     * @param gs graphic state of the XObject
     * @param InlineImageInfo  a reference to the inline image
     * @param colorSpaceDictionary colourspace of the image
     * @param markedContentInfo marked content information for the XObject
     * @return the ImageRenderInfo representing the rendered embedded image
     * @since 5.0.1
     */
    protected static ImageRenderInfo createForEmbeddedImage(GraphicsState gs, InlineImageInfo inlineImageInfo, PdfDictionary colorSpaceDictionary,Collection<MarkedContentInfo> markedContentInfo){
        ImageRenderInfo renderInfo = new ImageRenderInfo(gs, inlineImageInfo, colorSpaceDictionary,markedContentInfo);
        return renderInfo;
    }

    
    /**
     * Gets an object containing the image dictionary and bytes.
     * @return an object containing the image dictionary and byte[]
     * @since 5.0.2
     */
    public PdfImageObject getImage() throws IOException {
        prepareImageObject();
        return imageObject;
    }
    
    private void prepareImageObject() throws IOException{
        if (imageObject != null)
            return;
        
        if (ref != null){
            PRStream stream = (PRStream)PdfReader.getPdfObject(ref);
            imageObject = new PdfImageObject(stream, colorSpaceDictionary);
        } else if (inlineImageInfo != null){
            imageObject = new PdfImageObject(inlineImageInfo.getImageDictionary(), inlineImageInfo.getSamples(), colorSpaceDictionary);
        }
    }
    
    /**
     * @return a vector in User space representing the start point of the xobject
     */
    public Vector getStartPoint(){ 
        return new Vector(0, 0, 1).cross(gs.ctm); 
    }

    /**
     * @return The coordinate transformation matrix active when this image was rendered.  Coordinates are in User space.
     * @since 5.0.3
     */
    public Matrix getImageCTM(){
        return gs.ctm;
    }
    
    /**
     * @return the size of the image, in User space units
     * @since 5.0.3
     */
    public float getArea(){
        // the image space area is 1, so we multiply that by the determinant of the CTM to get the transformed area
        return gs.ctm.getDeterminant();
    }
    
    /**
     * @return an indirect reference to the image
     * @since 5.0.2
     */
    public PdfIndirectReference getRef() {
    	return ref;
    }
    
    /**
     * @return the current fill color from the graphics state at the time this render operation occured
     * @since 5.5.7
     */
    public BaseColor getCurrentFillColor(){
    	return gs.fillColor;
    }

    /**
     * Checks if the text belongs to a marked content sequence
     * with a given mcid.
     * @param mcid a marked content id
     * @return true if the text is marked with this id
     * @since 5.5.11
     */
    public boolean hasMcid(int mcid) {
        return hasMcid(mcid, false);
    }

    /**
     * Checks if the text belongs to a marked content sequence
     * with a given mcid.
     * @param mcid a marked content id
     * @param checkTheTopmostLevelOnly indicates whether to check the topmost level of marked content stack only
     * @return true if the text is marked with this id
     * @since 5.5.11
     */
    public boolean hasMcid(int mcid, boolean checkTheTopmostLevelOnly) {
        if (checkTheTopmostLevelOnly) {
            if (markedContentInfos instanceof ArrayList) {
                Integer infoMcid = getMcid();
                return (infoMcid != null) ? infoMcid == mcid : false;
            }
        } else {
            for (MarkedContentInfo info : markedContentInfos) {
                if (info.hasMcid())
                    if(info.getMcid() == mcid)
                        return true;
            }
        }
        return false;
    }


    /**
     * @return the marked content associated with the TextRenderInfo instance.
     */
    public Integer getMcid() {
        if (markedContentInfos instanceof ArrayList) {
            ArrayList<MarkedContentInfo> mci = (ArrayList<MarkedContentInfo>)markedContentInfos;
            MarkedContentInfo info = mci.size() > 0 ? mci.get(mci.size() - 1) : null;
            return (info != null && info.hasMcid()) ? info.getMcid() : null;
        }
        return null;
    }

}
