package com.lowagie.text.pdf;

import java.awt.color.ICC_Profile;

import com.lowagie.text.ExceptionConverter;

/**
 * A <CODE>PdfICCBased</CODE> defines a ColorSpace
 *
 * @see		PdfStream
 */

class PdfICCBased extends PdfStream {
    
    protected int NumberOfComponents;
    
    PdfICCBased(ICC_Profile profile) {
        super();
        try {
            NumberOfComponents = profile.getNumComponents();
            PdfNumber pNumber = new PdfNumber(NumberOfComponents);
            switch (NumberOfComponents) {
                case 1:
                    put(PdfName.ALTERNATE, PdfName.DEVICEGRAY);
                    break;
                case 3:
                    put(PdfName.ALTERNATE, PdfName.DEVICERGB);
                    break;
                case 4:
                    put(PdfName.ALTERNATE, PdfName.DEVICECMYK);
                    break;
                default:
                    throw new PdfException(NumberOfComponents + " component(s) is not supported in PDF1.4");
            }
            put(PdfName.N, new PdfNumber(NumberOfComponents));
            bytes = profile.getData();
            flateCompress();
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
}
