/*
 * $Id$
 * $Name$
 *
 * Copyright 2001 by Paulo Soares.
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

package com.lowagie.text.pdf.wmf;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.awt.Color;
import com.lowagie.text.pdf.*;
import com.lowagie.text.DocumentException;

public class MetaFont extends MetaObject {
    static final String fontNames[] = {
        "Courier", "Courier-Bold", "Courier-Oblique", "Courier-BoldOblique",
        "Helvetica", "Helvetica-Bold", "Helvetica-Oblique", "Helvetica-BoldOblique",
        "Times-Roman", "Times-Bold", "Times-Italic", "Times-BoldItalic",
        "Symbol", "ZapfDingbats"};

    static final int MARKER_BOLD = 1;
    static final int MARKER_ITALIC = 2;
    static final int MARKER_COURIER = 0;
    static final int MARKER_HELVETICA = 4;
    static final int MARKER_TIMES = 8;
    static final int MARKER_SYMBOL = 12;

    static final int DEFAULT_PITCH = 0;
    static final int FIXED_PITCH = 1;
    static final int VARIABLE_PITCH = 2;
    static final int FF_DONTCARE = 0;
    static final int FF_ROMAN = 1;
    static final int FF_SWISS = 2;
    static final int FF_MODERN = 3;
    static final int FF_SCRIPT = 4;
    static final int FF_DECORATIVE = 5;
    static final int BOLDTHRESHOLD = 600;    
    static final int nameSize = 32;
    static final int ETO_OPAQUE = 2;
    static final int ETO_CLIPPED = 4;

    int height;
    float angle;
    int bold;
    int italic;
    boolean underline;
    boolean strikeout;
    int charset;
    int pitchAndFamily;
    String faceName;
    BaseFont font = null;

    public MetaFont() {
        type = META_FONT;
    }

    public void init(InputMeta in) throws IOException {
        height = Math.abs(in.readShort());
        in.skip(2);
        angle = (float)(in.readShort() / 1800.0 * Math.PI);
        in.skip(2);
        bold = (in.readShort() >= BOLDTHRESHOLD ? MARKER_BOLD : 0);
        italic = (in.readByte() != 0 ? MARKER_ITALIC : 0);
        underline = (in.readByte() != 0);
        strikeout = (in.readByte() != 0);
        charset = in.readByte();
        in.skip(3);
        pitchAndFamily = in.readByte();
        byte name[] = new byte[nameSize];
        int k;
        for (k = 0; k < nameSize; ++k) {
            int c = in.readByte();
            if (c == 0) {
                break;
            }
            name[k] = (byte)c;
        }
        try {
            faceName = new String(name, 0, k, "Cp1252");
        }
        catch (UnsupportedEncodingException e) {
            faceName = new String(name, 0, k);
        }
        faceName = faceName.toLowerCase();
    }
    
    public BaseFont getFont() {
        if (font != null)
            return font;
        String fontName;
        if (faceName.indexOf("courier") != -1 || faceName.indexOf("terminal") != -1
            || faceName.indexOf("fixedsys") != -1) {
            fontName = fontNames[MARKER_COURIER + italic + bold];
        }
        else if (faceName.indexOf("ms sans serif") != -1 || faceName.indexOf("arial") != -1
            || faceName.indexOf("system") != -1) {
            fontName = fontNames[MARKER_HELVETICA + italic + bold];
        }
        else if (faceName.indexOf("arial black") != -1) {
            fontName = fontNames[MARKER_HELVETICA + italic + MARKER_BOLD];
        }
        else if (faceName.indexOf("times") != -1 || faceName.indexOf("ms serif") != -1
            || faceName.indexOf("roman") != -1) {
            fontName = fontNames[MARKER_TIMES + italic + bold];
        }
        else if (faceName.indexOf("symbol") != -1) {
            fontName = fontNames[MARKER_SYMBOL];
        }
        else {
            int pitch = pitchAndFamily & 3;
            int family = (pitchAndFamily >> 4) & 7;
            switch (family) {
                case FF_MODERN:
                    fontName = fontNames[MARKER_COURIER + italic + bold];
                    break;
                case FF_ROMAN:
                    fontName = fontNames[MARKER_TIMES + italic + bold];
                    break;
                case FF_SWISS:
                case FF_SCRIPT:
                case FF_DECORATIVE:
                    fontName = fontNames[MARKER_HELVETICA + italic + bold];
                    break;
                default:
                {
                    switch (pitch) {
                        case FIXED_PITCH:
                            fontName = fontNames[MARKER_COURIER + italic + bold];
                            break;
                        default:
                            fontName = fontNames[MARKER_HELVETICA + italic + bold];
                            break;
                    }
                }
            }
        }
        try {
            font = BaseFont.createFont(fontName, "Cp1252", false);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        
        return font;
    }
    
    public float getAngle() {
        return angle;
    }
    
    public boolean isUnderline() {
        return underline;
    }
    
    public boolean isStrikeout() {
        return strikeout;
    }
    
    public float getFontSize(MetaState state) {
        return Math.abs(state.transformY(height) - state.transformY(0)) * 0.86f;
    }
}
