/*
 * $Id$
 * $Name$
 *
 * Copyright 2000, 2001 by Paulo Soares.
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

/**
 * Describes a font
 */

public class PdfFontDescriptor{
    public final static int TYPE1_BUILTIN = 0;
    public final static int TYPE1_EXTERNAL = 1;
    public final static int TRUETYPE = 2;
    public final static String ENCODING_WINANSI = "WinAnsiEncoding";
    public final static String ENCODING_MACROMAN = "MacRomanEncoding";
    public final static String ENCODING_MACEXPERT = "MacExpertEncoding";
    public final static String ENCODING_BUILTIN = "";
    String Subtype;
    String Name;
    String BaseFont;
    int FirstChar;
    int LastChar;
    int Widths[] = new int[256];
    String Encoding;
    
    int Ascent;
    int CapHeight;
    int Descent;
    int Flags;
    int llx;
    int lly;
    int urx;
    int ury;
    double ItalicAngle;
    int StemV;
    int fontType;  // type1 or truetype
    boolean embedded;
    boolean subset;
    int Glyphs[] = new int[256];
    boolean CharsInUse[] = new boolean[256];
    PdfWriter writer;
    
/** Creates new PdfFontDescriptor */
    public PdfFontDescriptor() {
        for (int k = 0; k < 256; ++k)
        {
            Widths[k] = 0;
            Glyphs[k] = -1;
            CharsInUse[k] = false;
        }
    }
}
