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
import java.awt.Color;

public class MetaPen extends MetaObject {

    public static final int PS_SOLID = 0;
    public static final int PS_DASH = 1;
    public static final int PS_DOT = 2;
    public static final int PS_DASHDOT = 3;
    public static final int PS_DASHDOTDOT = 4;
    public static final int PS_NULL = 5;
    public static final int PS_INSIDEFRAME = 6;

    int style = PS_SOLID;
    int penWidth = 1;
    Color color = Color.black;

    public MetaPen() {
        type = META_PEN;
    }

    public void init(InputMeta in) throws IOException {
        style = in.readWord();
        penWidth = in.readShort();
        in.readWord();
        color = in.readColor();
    }
    
    public int getStyle() {
        return style;
    }
    
    public int getPenWidth() {
        return penWidth;
    }
    
    public Color getColor() {
        return color;
    }
}
