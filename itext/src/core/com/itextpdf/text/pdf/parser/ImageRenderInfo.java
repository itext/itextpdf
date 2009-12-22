/*
 * Created on Dec 21, 2009
 * (c) 2009 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfObject;

/**
 * @author kevin
 */
public class ImageRenderInfo {
    private final PdfObject xobject;
    private final Matrix ctm;
    
    public ImageRenderInfo(PdfObject xobject, Matrix ctm) {
        this.xobject = xobject;
        this.ctm = ctm;
    }

    /**
     * @return the XObject itself
     */
    public PdfObject getXObject(){
        return xobject;
    }
    
    /**
     * @return a vector in User space representing the start point of the xobject
     */
    public Vector getStartPoint(){ 
        return new Vector(0, 0, 1).cross(ctm); 
    }
    

}
