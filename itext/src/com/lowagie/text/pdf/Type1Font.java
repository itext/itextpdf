/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 Paulo Soares
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.pdf;

import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.afm.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

/** Reads a Type1 font
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
class Type1Font extends BaseFont {
/** The PFB file if the input was made with a <CODE>byte</CODE> array. */    
    protected byte pfb[];
    
/** The Postscript font name. */
    private String FontName = null;
    
/** The full name of the font. */
    private String FullName = null;
    
/** The family name of the font. */
    private String FamilyName = null;
    
/** The weight of the font: normal, bold, etc. */
    private String Weight = "";
    
/** The italic angle of the font, usually 0.0 or negative. */
    private float ItalicAngle = 0.0f;
    
/** <CODE>true</CODE> if all the characters have the same width. */
    private boolean IsFixedPitch = false;
    
/** The character set of the font. */
    private String CharacterSet;
    
/** The llx of the FontBox. */
    private int llx = -50;
    
/** The lly of the FontBox. */
    private int lly = -200;
    
/** The lurx of the FontBox. */
    private int urx = 1000;
    
/** The ury of the FontBox. */
    private int ury = 900;
    
/** The underline position. */
    private int UnderlinePosition = -100;
    
/** The underline thickness. */
    private int UnderlineThickness = 50;
    
/**
 * The font's encoding name. This encoding is 'StandardEncoding' or
 *  'AdobeStandardEncoding' for a font that can be totally encoded
 *  according to the characters names. For all other names the
 *  font is treated as symbolic.
 */
    private String EncodingScheme = "FontSpecific";
    
/** A variable. */
    private int CapHeight = 700;
    
/** A variable. */
    private int XHeight = 480;
    
/** A variable. */
    private int Ascender = 800;
    
/** A variable. */
    private int Descender = -200;
    
/** A variable. */
    private int StdHW;
    
/** A variable. */
    private int StdVW = 80;
    
/**
 * Represents the section CharMetrics in the AFM file. Each
 * element of this array contains a <CODE>Object[3]</CODE> with an
 * Integer, Integer and String. This is the code, width and name.
 */
    private ArrayList CharMetrics = new ArrayList();
    
/**
 * Represents the section KernPairs in the AFM file. The key is
 * the name of the first character and the value is a <CODE>Object[]</CODE>
 * with 2 elements for each kern pair. Position 0 is the name of
 * the second character and position 1 is the kerning distance. This is
 * repeated for all the pairs.
 */
    private HashMap KernPairs = new HashMap();
    
/** The file in use. */
    private String fileName = null;
    
/** <CODE>true</CODE> if this font is one of the 14 built in fonts. */
    private boolean builtinFont = false;
    
/**
 * Types of records in a PFB file. ASCII is 1 and BINARY is 2.
 *  They have to appear in the PFB file in this sequence.
 */
    private final static int pfbTypes[] = {1, 2, 1};
    
    /** Creates a new Type1 font.
     * @param ttfAfm the AFM file if the input is made with a <CODE>byte</CODE> array
     * @param pfb the PFB file if the input is made with a <CODE>byte</CODE> array
     * @param afmFile the name of one of the 14 built-in fonts or the location of an AFM file. The file must end in '.afm'
     * @param enc the encoding to be applied to this font
     * @param emb true if the font is to be embedded in the PDF
     * @throws DocumentException the AFM file is invalid
     * @throws IOException the AFM file could not be read
     */
    Type1Font(String afmFile, String enc, boolean emb, byte ttfAfm[], byte pfb[]) throws DocumentException, IOException
    {
        if (emb && ttfAfm != null && pfb == null)
            throw new DocumentException("Two byte arrays are needed if the Type1 font is embedded.");
        if (emb && ttfAfm != null)
            this.pfb = pfb;
        encoding = enc;
        embedded = emb;
        fileName = afmFile;
        fontType = FONT_TYPE_T1;
        RandomAccessFileOrArray rf = null;
        if (BuiltinFonts14.containsKey(afmFile)) {
            embedded = false;
            builtinFont = true;
            try {
                String afm = null;
                if (afmFile.equals(COURIER))
                    afm = Courier.afm;
                else if (afmFile.equals(COURIER_BOLD))
                    afm = CourierBold.afm;
                else if (afmFile.equals(COURIER_BOLDOBLIQUE))
                    afm = CourierBoldOblique.afm;
                else if (afmFile.equals(COURIER_OBLIQUE))
                    afm = CourierOblique.afm;
                else if (afmFile.equals(HELVETICA_BOLD)) {
                    afm = HelveticaBold1.afm;
                    afm += HelveticaBold2.afm;
                }
                else if (afmFile.equals(HELVETICA_BOLDOBLIQUE)) {
                    afm = HelveticaBoldOblique1.afm;
                    afm += HelveticaBoldOblique2.afm;
                }
                else if (afmFile.equals(HELVETICA_OBLIQUE)) {
                    afm = HelveticaOblique1.afm;
                    afm += HelveticaOblique2.afm;
                }
                else if (afmFile.equals(SYMBOL))
                    afm = Symbol.afm;
                else if (afmFile.equals(TIMES_ROMAN))
                    afm = TimesRoman.afm;
                else if (afmFile.equals(TIMES_BOLD))
                    afm = TimesBold.afm;
                else if (afmFile.equals(TIMES_BOLDITALIC))
                    afm = TimesBoldItalic.afm;
                else if (afmFile.equals(TIMES_ITALIC)) {
                    afm = TimesItalic1.afm;
                    afm += TimesItalic2.afm;
                }
                else if (afmFile.equals(ZAPFDINGBATS))
                    afm = ZapfDingbats.afm;
                else {
                    afm = Helvetica1.afm;
                    afm += Helvetica2.afm;
                }
                rf = new RandomAccessFileOrArray(afm.getBytes(PdfObject.ENCODING));
                process(rf);
            }
            finally {
                if (rf != null) {
                    try {
                        rf.close();
                    }
                    catch (Exception e) {
                        // empty on purpose
                    }
                }
            }
        }
        else if (afmFile.toLowerCase().endsWith(".afm")) {
            try {
                if (ttfAfm == null)
                    rf = new RandomAccessFileOrArray(afmFile);
                else
                    rf = new RandomAccessFileOrArray(ttfAfm);
                process(rf);
            }
            finally {
                if (rf != null) {
                    try {
                        rf.close();
                    }
                    catch (Exception e) {
                        // empty on purpose
                    }
                }
            }
        }
        else
            throw new DocumentException(afmFile + " is not an AFM font file.");
        try {
            EncodingScheme = EncodingScheme.trim();
            if (EncodingScheme.equals("AdobeStandardEncoding") || EncodingScheme.equals("StandardEncoding")) {
                fontSpecific = false;
            }
            " ".getBytes(enc); // check if the encoding exists
            createEncoding();
        }
        catch (Exception e) {
            throw new DocumentException(e.getMessage());
        }
    }
    
/** Gets the width from the font according to the <CODE>name</CODE> or,
 * if the <CODE>name</CODE> is null, meaning it is a symbolic font,
 * the char <CODE>c</CODE>.
 * @param c the char if the font is symbolic
 * @param name the glyph name
 * @return the width of the char
 */
    protected int getRawWidth(int c, String name)
    {
        try {
            if (name == null) { // font specific
                for (int k = 0; k < CharMetrics.size(); ++k) {
                    Object metrics[] = (Object[])CharMetrics.get(k);
                    if (((Integer)(metrics[0])).intValue() == c)
                        return ((Integer)(metrics[1])).intValue();
                }
            }
            else {
                if (name.equals(".notdef"))
                    return 0;
                for (int k = 0; k < CharMetrics.size(); ++k) {
                    Object metrics[] = (Object[])CharMetrics.get(k);
                    if (name.equals(metrics[2]))
                        return ((Integer)(metrics[1])).intValue();
                }
            }
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        return 0;
    }
    
/** Gets the kerning between two Unicode characters. The characters
 * are converted to names and this names are used to find the kerning
 * pairs in the <CODE>HashMap</CODE> <CODE>KernPairs</CODE>.
 * @param char1 the first char
 * @param char2 the second char
 * @return the kerning to be applied
 */
    public int getKerning(char char1, char char2)
    {
        String first = GlyphList.unicodeToName((int)char1);
        if (first == null)
            return 0;
        String second = GlyphList.unicodeToName((int)char2);
        if (second == null)
            return 0;
        Object obj[] = (Object[])KernPairs.get(first);
        if (obj == null)
            return 0;
        for (int k = 0; k < obj.length; k += 2) {
            if (second.equals(obj[k]))
                return ((Integer)obj[k + 1]).intValue();
        }
        return 0;
    }
    
    
    /** Reads the font metrics
     * @param rf the AFM file
     * @throws DocumentException the AFM file is invalid
     * @throws IOException the AFM file could not be read
     */
    public void process(RandomAccessFileOrArray rf) throws DocumentException, IOException
    {
        String line;
        boolean isMetrics = false;
        while ((line = rf.readLine()) != null)
        {
            StringTokenizer tok = new StringTokenizer(line);
            if (!tok.hasMoreTokens())
                continue;
            String ident = tok.nextToken();
            if (ident.equals("FontName"))
                FontName = tok.nextToken("\u00ff").substring(1);
            else if (ident.equals("FullName"))
                FullName = tok.nextToken("\u00ff").substring(1);
            else if (ident.equals("FamilyName"))
                FamilyName = tok.nextToken("\u00ff").substring(1);
            else if (ident.equals("Weight"))
                Weight = tok.nextToken("\u00ff").substring(1);
            else if (ident.equals("ItalicAngle"))
                ItalicAngle = Float.valueOf(tok.nextToken()).floatValue();
            else if (ident.equals("IsFixedPitch"))
                IsFixedPitch = tok.nextToken().equals("true");
            else if (ident.equals("CharacterSet"))
                CharacterSet = tok.nextToken("\u00ff").substring(1);
            else if (ident.equals("FontBBox"))
            {
                llx = (int)Float.valueOf(tok.nextToken()).floatValue();
                lly = (int)Float.valueOf(tok.nextToken()).floatValue();
                urx = (int)Float.valueOf(tok.nextToken()).floatValue();
                ury = (int)Float.valueOf(tok.nextToken()).floatValue();
            }
            else if (ident.equals("UnderlinePosition"))
                UnderlinePosition = (int)Float.valueOf(tok.nextToken()).floatValue();
            else if (ident.equals("UnderlineThickness"))
                UnderlineThickness = (int)Float.valueOf(tok.nextToken()).floatValue();
            else if (ident.equals("EncodingScheme"))
                EncodingScheme = tok.nextToken("\u00ff").substring(1);
            else if (ident.equals("CapHeight"))
                CapHeight = (int)Float.valueOf(tok.nextToken()).floatValue();
            else if (ident.equals("XHeight"))
                XHeight = (int)Float.valueOf(tok.nextToken()).floatValue();
            else if (ident.equals("Ascender"))
                Ascender = (int)Float.valueOf(tok.nextToken()).floatValue();
            else if (ident.equals("Descender"))
                Descender = (int)Float.valueOf(tok.nextToken()).floatValue();
            else if (ident.equals("StdHW"))
                StdHW = (int)Float.valueOf(tok.nextToken()).floatValue();
            else if (ident.equals("StdVW"))
                StdVW = (int)Float.valueOf(tok.nextToken()).floatValue();
            else if (ident.equals("StartCharMetrics"))
            {
                isMetrics = true;
                break;
            }
        }
        if (!isMetrics)
            throw new DocumentException("Missing StartCharMetrics in " + fileName);
        while ((line = rf.readLine()) != null)
        {
            StringTokenizer tok = new StringTokenizer(line);
            if (!tok.hasMoreTokens())
                continue;
            String ident = tok.nextToken();
            if (ident.equals("EndCharMetrics"))
            {
                isMetrics = false;
                break;
            }
            Integer C = new Integer(-1);
            Integer WX = new Integer(250);
            String N = "";
            tok = new StringTokenizer(line, ";");
            while (tok.hasMoreTokens())
            {
                StringTokenizer tokc = new StringTokenizer(tok.nextToken());
                if (!tokc.hasMoreTokens())
                    continue;
                ident = tokc.nextToken();
                if (ident.equals("C"))
                    C = Integer.valueOf(tokc.nextToken());
                else if (ident.equals("WX"))
                    WX = Integer.valueOf(tokc.nextToken());
                else if (ident.equals("N"))
                    N = tokc.nextToken();
            }
            CharMetrics.add(new Object[]{C, WX, N});
        }
        if (isMetrics)
            throw new DocumentException("Missing EndCharMetrics in " + fileName);
        while ((line = rf.readLine()) != null)
        {
            StringTokenizer tok = new StringTokenizer(line);
            if (!tok.hasMoreTokens())
                continue;
            String ident = tok.nextToken();
            if (ident.equals("EndFontMetrics"))
                return;
            if (ident.equals("StartKernPairs"))
            {
                isMetrics = true;
                break;
            }
        }
        if (!isMetrics)
            throw new DocumentException("Missing EndFontMetrics in " + fileName);
        while ((line = rf.readLine()) != null)
        {
            StringTokenizer tok = new StringTokenizer(line);
            if (!tok.hasMoreTokens())
                continue;
            String ident = tok.nextToken();
            if (ident.equals("KPX"))
            {
                String first = tok.nextToken();
                String second = tok.nextToken();
                Integer width = new Integer((int)Float.valueOf(tok.nextToken()).floatValue());
                Object relates[] = (Object[])KernPairs.get(first);
                if (relates == null)
                    KernPairs.put(first, new Object[]{second, width});
                else
                {
                    int n = relates.length;
                    Object relates2[] = new Object[n + 2];
                    System.arraycopy(relates, 0, relates2, 0, n);
                    relates2[n] = second;
                    relates2[n + 1] = width;
                    KernPairs.put(first, relates2);
                }
            }
            else if (ident.equals("EndKernPairs"))
            {
                isMetrics = false;
                break;
            }
        }
        if (isMetrics)
            throw new DocumentException("Missing EndKernPairs in " + fileName);
        rf.close();
    }
    
/** If the embedded flag is <CODE>false</CODE> or if the font is
 *  one of the 14 built in types, it returns <CODE>null</CODE>,
 * otherwise the font is read and output in a PdfStream object.
 * @return the PdfStream containing the font or <CODE>null</CODE>
 * @throws DocumentException if there is an error reading the font
 */
    private PdfStream getFontStream() throws DocumentException
    {
        if (builtinFont || !embedded)
            return null;
        RandomAccessFileOrArray rf = null;
        try {
            String filePfb = fileName.substring(0, fileName.length() - 3) + "pfb";
            if (pfb == null)
                rf = new RandomAccessFileOrArray(filePfb);
            else
                rf = new RandomAccessFileOrArray(pfb);
            int fileLength = rf.length();
            byte st[] = new byte[fileLength - 18];
            int lengths[] = new int[3];
            int bytePtr = 0;
            for (int k = 0; k < 3; ++k) {
                if (rf.read() != 0x80)
                    throw new DocumentException("Start marker missing in " + filePfb);
                if (rf.read() != pfbTypes[k])
                    throw new DocumentException("Incorrect segment type in " + filePfb);
                int size = rf.read();
                size += rf.read() << 8;
                size += rf.read() << 16;
                size += rf.read() << 24;
                lengths[k] = size;
                while (size != 0) {
                    int got = rf.read(st, bytePtr, size);
                    if (got < 0)
                        throw new DocumentException("Premature end in " + filePfb);
                    bytePtr += got;
                    size -= got;
                }
            }
            return new StreamFont(st, lengths);
        }
        catch (Exception e) {
            throw new DocumentException(e.getMessage());
        }
        finally {
            if (rf != null) {
                try {
                    rf.close();
                }
                catch (Exception e) {
                    // empty on purpose
                }
            }
        }
    }
    
/** Generates the font descriptor for this font or <CODE>null</CODE> if it is
 * one of the 14 built in fonts.
 * @param fontStream the indirect reference to a PdfStream containing the font or <CODE>null</CODE>
 * @return the PdfDictionary containing the font descriptor or <CODE>null</CODE>
 * @throws DocumentException if there is an error
 */
    private PdfDictionary getFontDescriptor(PdfIndirectReference fontStream) throws DocumentException
    {
        if (builtinFont)
            return null;
        PdfDictionary dic = new PdfDictionary(new PdfName("FontDescriptor"));
        dic.put(new PdfName("Ascent"), new PdfNumber(Ascender));
        dic.put(new PdfName("CapHeight"), new PdfNumber(CapHeight));
        dic.put(new PdfName("Descent"), new PdfNumber(Descender));
        dic.put(new PdfName("FontBBox"), new PdfRectangle(llx, lly, urx, ury));
        dic.put(new PdfName("FontName"), new PdfName(FontName));
        dic.put(new PdfName("ItalicAngle"), new PdfNumber(ItalicAngle));
        dic.put(new PdfName("StemV"), new PdfNumber(StdVW));
        if (fontStream != null)
            dic.put(new PdfName("FontFile"), fontStream);
        int flags = 0;
        if (IsFixedPitch)
            flags |= 1;
        flags |= fontSpecific ? 4 : 32;
        if (ItalicAngle < 0)
            flags |= 64;
        if (FontName.indexOf("Caps") >= 0 || FontName.endsWith("SC"))
            flags |= 131072;
        if (Weight.equals("Bold"))
            flags |= 262144;
        dic.put(new PdfName("Flags"), new PdfNumber(flags));
        
        return dic;
    }
    
    /** Generates the font dictionary for this font.
     * @return the PdfDictionary containing the font dictionary
     * @param firstChar the first valid character
     * @param lastChar the last valid character
     * @param shortTag a 256 bytes long <CODE>byte</CODE> array where each unused byte is represented by 0
     * @param fontDescriptor the indirect reference to a PdfDictionary containing the font descriptor or <CODE>null</CODE>
     * @throws DocumentException if there is an error
     */
    private PdfDictionary getFontBaseType(PdfIndirectReference fontDescriptor, int firstChar, int lastChar, byte shortTag[]) throws DocumentException
    {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
        dic.put(PdfName.BASEFONT, new PdfName(FontName));
        boolean stdEncoding = encoding.equals("Cp1252") || encoding.equals("MacRoman");
        if (!fontSpecific) {
            for (int k = firstChar; k <= lastChar; ++k) {
                if (!differences[k].equals(notdef)) {
                    firstChar = k;
                    break;
                }
            }
            if (stdEncoding)
                dic.put(PdfName.ENCODING, encoding.equals("Cp1252") ? PdfName.WIN_ANSI_ENCODING : PdfName.MAC_ROMAN_ENCODING);
            else {
                PdfDictionary enc = new PdfDictionary(new PdfName("Encoding"));
                PdfArray dif = new PdfArray();
                boolean gap = true;                
                for (int k = firstChar; k <= lastChar; ++k) {
                    if (shortTag[k] != 0) {
                        if (gap) {
                            dif.add(new PdfNumber(k));
                            gap = false;
                        }
                        dif.add(new PdfName(differences[k]));
                    }
                    else
                        gap = true;
                }
                enc.put(new PdfName("Differences"), dif);
                dic.put(PdfName.ENCODING, enc);
            }
        }
        if (forceWidthsOutput || !(builtinFont && (fontSpecific || stdEncoding))) {
            dic.put(new PdfName("FirstChar"), new PdfNumber(firstChar));
            dic.put(new PdfName("LastChar"), new PdfNumber(lastChar));
            PdfArray wd = new PdfArray();
            for (int k = firstChar; k <= lastChar; ++k) {
                if (shortTag[k] == 0)
                    wd.add(new PdfNumber(0));
                else
                    wd.add(new PdfNumber(widths[k]));
            }
            dic.put(new PdfName("Widths"), wd);
        }
        if (!builtinFont && fontDescriptor != null)
            dic.put(new PdfName("FontDescriptor"), fontDescriptor);
        return dic;
    }
    
    /** Outputs to the writer the font dictionaries and streams.
     * @param writer the writer for this document
     * @param ref the font indirect reference
     * @param params several parameters that depend on the font type
     * @throws IOException on error
     * @throws DocumentException error in generating the object
     */
    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object params[]) throws DocumentException, IOException {
        int firstChar = ((Integer)params[0]).intValue();
        int lastChar = ((Integer)params[1]).intValue();
        byte shortTag[] = (byte[])params[2];
        PdfIndirectReference ind_font = null;
        PdfObject pobj = null;
        PdfIndirectObject obj = null;
        pobj = getFontStream();
        if (pobj != null){
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        }
        pobj = getFontDescriptor(ind_font);
        if (pobj != null){
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        }
        pobj = getFontBaseType(ind_font, firstChar, lastChar, shortTag);
        writer.addToBody(pobj, ref);
    }
    
    /** Gets the font parameter identified by <CODE>key</CODE>. Valid values
     * for <CODE>key</CODE> are <CODE>ASCENT</CODE>, <CODE>CAPHEIGHT</CODE>, <CODE>DESCENT</CODE>,
     * <CODE>ITALICANGLE</CODE>, <CODE>BBOXLLX</CODE>, <CODE>BBOXLLY</CODE>, <CODE>BBOXURX</CODE>
     * and <CODE>BBOXURY</CODE>.
     * @param key the parameter to be extracted
     * @param fontSize the font size in points
     * @return the parameter in points
     */    
    public float getFontDescriptor(int key, float fontSize) {
        switch (key) {
            case ASCENT:
                return Ascender * fontSize / 1000;
            case CAPHEIGHT:
                return CapHeight * fontSize / 1000;
            case DESCENT:
                return Descender * fontSize / 1000;
            case ITALICANGLE:
                return ItalicAngle;
            case BBOXLLX:
                return llx * fontSize / 1000;
            case BBOXLLY:
                return lly * fontSize / 1000;
            case BBOXURX:
                return urx * fontSize / 1000;
            case BBOXURY:
                return ury * fontSize / 1000;
        }
        return 0;
    }
    
    /** Gets the postscript font name.
     * @return the postscript font name
     */
    public String getPostscriptFontName() {
        return FontName;
    }
    
    /** Gets the full name of the font. If it is a True Type font
     * each array element will have {Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"", "", "",
     * font name}.
     * @return the full name of the font
     */
    public String[][] getFullFontName() {
        return new String[][]{{"", "", "", FullName}};
    }
    
}
