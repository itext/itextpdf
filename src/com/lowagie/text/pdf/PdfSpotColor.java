/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text.pdf;

import java.awt.Color;
/**
 * A <CODE>PdfSpotColor</CODE> defines a ColorSpace
 *
 * @see		PdfDictionary
 */

public class PdfSpotColor extends PdfArray {
    
/*	The tint value */
    protected float tint;
    
/*	The color name */
    public PdfName name;
    
/* The function dictionary */
    public PdfDictionary func;
    
/* The alternative color space */
    public Color altcs;
    // constructors
    
    /**
     * Constructs a new <CODE>PdfSpotColor</CODE>.
     *
     * @param		name		a String value
     * @param		tint		a tint value between 0 and 1
     * @param		altcs		a altnative colorspace value
     */
    
    public PdfSpotColor(String name, float tint, Color altcs) {
        super(PdfName.SEPARATION);
        int range = 0;
        PdfLiteral c0;
        PdfArray c1 = new PdfArray();
        try {
            this.name = new PdfName(name);
            add(this.name);
        } catch (BadPdfFormatException bfe) {}
        if (altcs instanceof ExtendedColor) {
            int type = ((ExtendedColor)altcs).type;
            switch (type) {
                case ExtendedColor.TYPE_GRAY:
                    add(PdfName.DEVICEGRAY);
                    range = 2;
                    c0 = new PdfLiteral("[0]");
                    c1.add(new PdfNumber(((GrayColor)altcs).getGray()));
                    break;
                case ExtendedColor.TYPE_CMYK:
                    add(PdfName.DEVICECMYK);
                    range = 8;
                    c0 = new PdfLiteral("[0 0 0 0]");
                    CMYKColor cmyk = (CMYKColor)altcs;
                    c1.add(new PdfNumber(cmyk.getCyan()));
                    c1.add(new PdfNumber(cmyk.getMagenta()));
                    c1.add(new PdfNumber(cmyk.getYellow()));
                    c1.add(new PdfNumber(cmyk.getBlack()));
                    break;
                default:
                    throw new RuntimeException("Only RGB, Gray and CMYK are supported as alternative color spaces.");
            }
        }
        else {
            add(PdfName.DEVICERGB);
            range = 6;
            c0 = new PdfLiteral("[0 0 0]");
            c1.add(new PdfNumber((float)altcs.getRed() / 255));
            c1.add(new PdfNumber((float)altcs.getGreen() / 255));
            c1.add(new PdfNumber((float)altcs.getBlue() / 255));
        }
        func = new PdfDictionary();
        add(func);
        func.put(PdfName.FUNCTIONTYPE, new PdfNumber((int) 2));
        try {
            PdfName domain = new PdfName("Domain");
            PdfArray domainval = new PdfArray(new PdfNumber(0));
            domainval.add(new PdfNumber(1));
            func.put(domain, domainval);
            PdfName c0n = new PdfName("C0");
            func.put(c0n, c0);
            PdfName c1n = new PdfName("C1");
            func.put(c1n, c1);
            PdfName n = new PdfName("N");
            PdfNumber nval = new PdfNumber(1.0);
            func.put(n, nval);
        } catch (BadPdfFormatException bfe) {
        }
        this.tint = tint;
        this.altcs = altcs;
    }
    
    public float getTint() {
        return tint;
    }
    
    public Color getAlternativeCS() {
        return altcs;
    }
/*
     public static void main (String[] argv) {
        PdfColor altcs1 = new PdfColor(13, 39,49,12);
        PdfSpotColor spc = new PdfSpotColor("Pantone 34 :w 932", 0.4f, altcs1);
        System.out.println("CMYK SpotColor\n" + new String(spc.toPdf(null)));
        PdfColor altcs2 = new PdfColor(13, 39,49);
        spc = new PdfSpotColor("Pantone 34 :w 932", 0.4f, altcs2);
        System.out.println("RGB SpotColor\n" + new String(spc.toPdf(null)));
        PdfColor altcs3 = new PdfColor(13);
        spc = new PdfSpotColor("Pantone 34 :w 932", 0.4f, altcs3);
        System.out.println("GrayScale SpotColor\n" + new String(spc.toPdf(null)));
     }
 */
}
