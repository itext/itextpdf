/*
 * PdfPRectangle.java
 *
 * Created on July 23, 2001, 7:24 PM
 */

package com.lowagie.text.pdf;

import com.lowagie.text.Rectangle;

/**
 *
 * @author  Paulo Soares
 */
public class PdfPRectangle extends Rectangle {
    
    public PdfPRectangle(float llx, float lly, float urx, float ury) {
        super(llx, lly, urx, ury);
    }
    
    public PdfPRectangle(float urx, float ury) {
        this(0, 0, urx, ury);
    }
    
    public PdfPRectangle(Rectangle rect) {
        this(rect.left(), rect.bottom(), rect.right(), rect.top());
    }
    
}
