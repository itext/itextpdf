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

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;
/** Reads a Truetype font
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */
class TrueTypeFont extends BaseFont {

    /** The code pages possible for a True Type font.
     */    
    static final String codePages[] = {
        "1252 Latin 1",
        "1250 Latin 2: Eastern Europe",
        "1251 Cyrillic",
        "1253 Greek",
        "1254 Turkish",
        "1255 Hebrew",
        "1256 Arabic",
        "1257 Windows Baltic",
        "1258 Vietnamese",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "874 Thai",
        "932 JIS/Japan",
        "936 Chinese: Simplified chars--PRC and Singapore",
        "949 Korean Wansung",
        "950 Chinese: Traditional chars--Taiwan and Hong Kong",
        "1361 Korean Johab",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "Macintosh Character Set (US Roman)",
        "OEM Character Set",
        "Symbol Character Set",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "869 IBM Greek",
        "866 MS-DOS Russian",
        "865 MS-DOS Nordic",
        "864 Arabic",
        "863 MS-DOS Canadian French",
        "862 Hebrew",
        "861 MS-DOS Icelandic",
        "860 MS-DOS Portuguese",
        "857 IBM Turkish",
        "855 IBM Cyrillic; primarily Russian",
        "852 Latin 2",
        "775 MS-DOS Baltic",
        "737 Greek; former 437 G",
        "708 Arabic; ASMO 708",
        "850 WE/Latin 1",
        "437 US"};
    
    /** Contains the location of the several tables. The key is the name of
     * the table and the value is an <CODE>int[2]</CODE> where position 0
     * is the offset from the start of the file and position 1 is the length
     * of the table.
     */
    protected HashMap tables;
    /** The file in use.
     */
    protected RandomAccessFileOrArray rf;
    /** The file name.
     */
    protected String fileName;
    
    /** The offset from the start of the file to the table directory.
     * It is 0 for TTF and may vary for TTC depending on the chosen font.
     */    
    protected int directoryOffset;
    /** The index for the TTC font. It is an empty <CODE>String</CODE> for a
     * TTF file.
     */    
    protected String ttcIndex;
    /** The style modifier */
    protected String style = "";
    /** The content of table 'head'.
     */
    protected FontHeader head = new FontHeader();
    /** The content of table 'hhea'.
     */
    protected HorizontalHeader hhea = new HorizontalHeader();
    /** The content of table 'OS/2'.
     */
    protected WindowsMetrics os_2 = new WindowsMetrics();
    /** The width of the glyphs. This is essentially the content of table
     * 'hmtx' normalized to 1000 units.
     */
    protected int GlyphWidths[];
    /** The map containing the code information for the table 'cmap', encoding 1.0.
     * The key is the code and the value is an <CODE>int[2]</CODE> where position 0
     * is the glyph number and position 1 is the glyph width normalized to 1000
     * units.
     */
    protected HashMap cmap10;
    /** The map containing the code information for the table 'cmap', encoding 3.1
     * in Unicode.
     * <P>
     * The key is the code and the value is an <CODE>int</CODE>[2] where position 0
     * is the glyph number and position 1 is the glyph width normalized to 1000
     * units.
     */
    protected HashMap cmap31;
    /** The map containig the kerning information. It represents the content of
     * table 'kern'. The key is an <CODE>Integer</CODE> where the top 16 bits
     * are the Unicode for the first character and the lower 16 bits are the
     * Unicode for the second character. The value is the amount of kerning in
     * normalized 1000 units as an <CODE>Integer</CODE>. This value is usually negative.
     */
    protected HashMap kerning;
    /**
     * The font name.
     * This name is usually extracted from the table 'name' with
     * the 'Name ID' 6.
     */
    protected String fontName;
    
    /** The full name of the font
     */    
    protected String fullName[][];

    /** The family name of the font
     */    
    protected String familyName[][];
    /** The italic angle. It is usually extracted from the 'post' table or in it's
     * absence with the code:
     * <P>
     * <PRE>
     * -Math.atan2(hhea.caretSlopeRun, hhea.caretSlopeRise) * 180 / Math.PI
     * </PRE>
     */
    protected double italicAngle;
    /** <CODE>true</CODE> if all the glyphs have the same width.
     */
    protected boolean isFixedPitch = false;
    
    /** The components of table 'head'.
     */
    protected class FontHeader {
        /** A variable. */
        int flags;
        /** A variable. */
        int unitsPerEm;
        /** A variable. */
        short xMin;
        /** A variable. */
        short yMin;
        /** A variable. */
        short xMax;
        /** A variable. */
        short yMax;
        /** A variable. */
        int macStyle;
    }
    
    /** The components of table 'hhea'.
     */
    protected class HorizontalHeader {
        /** A variable. */
        short Ascender;
        /** A variable. */
        short Descender;
        /** A variable. */
        short LineGap;
        /** A variable. */
        int advanceWidthMax;
        /** A variable. */
        short minLeftSideBearing;
        /** A variable. */
        short minRightSideBearing;
        /** A variable. */
        short xMaxExtent;
        /** A variable. */
        short caretSlopeRise;
        /** A variable. */
        short caretSlopeRun;
        /** A variable. */
        int numberOfHMetrics;
    }
    
    /** The components of table 'OS/2'.
     */
    protected class WindowsMetrics {
        /** A variable. */
        short xAvgCharWidth;
        /** A variable. */
        int usWeightClass;
        /** A variable. */
        int usWidthClass;
        /** A variable. */
        short fsType;
        /** A variable. */
        short ySubscriptXSize;
        /** A variable. */
        short ySubscriptYSize;
        /** A variable. */
        short ySubscriptXOffset;
        /** A variable. */
        short ySubscriptYOffset;
        /** A variable. */
        short ySuperscriptXSize;
        /** A variable. */
        short ySuperscriptYSize;
        /** A variable. */
        short ySuperscriptXOffset;
        /** A variable. */
        short ySuperscriptYOffset;
        /** A variable. */
        short yStrikeoutSize;
        /** A variable. */
        short yStrikeoutPosition;
        /** A variable. */
        short sFamilyClass;
        /** A variable. */
        byte panose[] = new byte[10];
        /** A variable. */
        byte achVendID[] = new byte[4];
        /** A variable. */
        int fsSelection;
        /** A variable. */
        int usFirstCharIndex;
        /** A variable. */
        int usLastCharIndex;
        /** A variable. */
        short sTypoAscender;
        /** A variable. */
        short sTypoDescender;
        /** A variable. */
        short sTypoLineGap;
        /** A variable. */
        int usWinAscent;
        /** A variable. */
        int usWinDescent;
        /** A variable. */
        int ulCodePageRange1;
        /** A variable. */
        int ulCodePageRange2;
        /** A variable. */
        int sCapHeight;
    }
    
    /** This constructor is present to allow extending the class.
     */
    protected TrueTypeFont() {
    }
    
    /** Creates a new TrueType font.
     * @param ttFile the location of the font on file. The file must end in '.ttf' or
     * '.ttc' but can have modifiers after the name
     * @param enc the encoding to be applied to this font
     * @param emb true if the font is to be embedded in the PDF
     * @param ttfAfm the font as a <CODE>byte</CODE> array
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
    TrueTypeFont(String ttFile, String enc, boolean emb, byte ttfAfm[]) throws DocumentException, IOException {
        String nameBase = getBaseName(ttFile);
        String ttcName = getTTCName(nameBase);
        if (nameBase.length() < ttFile.length()) {
            style = ttFile.substring(nameBase.length());
        }
        encoding = enc;
        embedded = emb;
        fileName = ttcName;
        fontType = FONT_TYPE_TT;
        ttcIndex = "";
        if (ttcName.length() < nameBase.length())
            ttcIndex = nameBase.substring(ttcName.length() + 1);
        if (fileName.toLowerCase().endsWith(".ttf") || fileName.toLowerCase().endsWith(".ttc")) {
            process(ttfAfm);
        }
        else
            throw new DocumentException(fileName + style + " is not a TTF or TTC font file.");
        try {
            " ".getBytes(enc); // check if the encoding exists
            createEncoding();
        }
        catch (UnsupportedEncodingException e) {
            throw new DocumentException(e.getMessage());
        }
    }
    
    /** Gets the name from a composed TTC file name.
     * If I have for input "myfont.ttc,2" the return will
     * be "myfont.ttc".
     * @param name the full name
     * @return the simple file name
     */    
    protected static String getTTCName(String name) {
        int idx = name.toLowerCase().indexOf(".ttc,");
        if (idx < 0)
            return name;
        else
            return name.substring(0, idx + 4);
    }
    
    
    /**
     * Reads the tables 'head', 'hhea', 'OS/2' and 'post' filling several variables.
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
    void fillTables() throws DocumentException, IOException {
        int table_location[];
        table_location = (int[])tables.get("head");
        if (table_location == null)
            throw new DocumentException("Table 'head' does not exist in " + fileName + style);
        rf.seek(table_location[0] + 16);
        head.flags = rf.readUnsignedShort();
        head.unitsPerEm = rf.readUnsignedShort();
        rf.skipBytes(16);
        head.xMin = rf.readShort();
        head.yMin = rf.readShort();
        head.xMax = rf.readShort();
        head.yMax = rf.readShort();
        head.macStyle = rf.readUnsignedShort();
        
        table_location = (int[])tables.get("hhea");
        if (table_location == null)
            throw new DocumentException("Table 'hhea' does not exist " + fileName + style);
        rf.seek(table_location[0] + 4);
        hhea.Ascender = rf.readShort();
        hhea.Descender = rf.readShort();
        hhea.LineGap = rf.readShort();
        hhea.advanceWidthMax = rf.readUnsignedShort();
        hhea.minLeftSideBearing = rf.readShort();
        hhea.minRightSideBearing = rf.readShort();
        hhea.xMaxExtent = rf.readShort();
        hhea.caretSlopeRise = rf.readShort();
        hhea.caretSlopeRun = rf.readShort();
        rf.skipBytes(12);
        hhea.numberOfHMetrics = rf.readUnsignedShort();
        
        table_location = (int[])tables.get("OS/2");
        if (table_location == null)
            throw new DocumentException("Table 'OS/2' does not exist in " + fileName + style);
        rf.seek(table_location[0]);
        int version = rf.readUnsignedShort();
        os_2.xAvgCharWidth = rf.readShort();
        os_2.usWeightClass = rf.readUnsignedShort();
        os_2.usWidthClass = rf.readUnsignedShort();
        os_2.fsType = rf.readShort();
        os_2.ySubscriptXSize = rf.readShort();
        os_2.ySubscriptYSize = rf.readShort();
        os_2.ySubscriptXOffset = rf.readShort();
        os_2.ySubscriptYOffset = rf.readShort();
        os_2.ySuperscriptXSize = rf.readShort();
        os_2.ySuperscriptYSize = rf.readShort();
        os_2.ySuperscriptXOffset = rf.readShort();
        os_2.ySuperscriptYOffset = rf.readShort();
        os_2.yStrikeoutSize = rf.readShort();
        os_2.yStrikeoutPosition = rf.readShort();
        os_2.sFamilyClass = rf.readShort();
        rf.readFully(os_2.panose);
        rf.skipBytes(16);
        rf.readFully(os_2.achVendID);
        os_2.fsSelection = rf.readUnsignedShort();
        os_2.usFirstCharIndex = rf.readUnsignedShort();
        os_2.usLastCharIndex = rf.readUnsignedShort();
        os_2.sTypoAscender = rf.readShort();
        os_2.sTypoDescender = rf.readShort();
        os_2.sTypoLineGap = rf.readShort();
        os_2.usWinAscent = rf.readUnsignedShort();
        os_2.usWinDescent = rf.readUnsignedShort();
        os_2.ulCodePageRange1 = 0;
        os_2.ulCodePageRange2 = 0;
        if (version > 0) {
            os_2.ulCodePageRange1 = rf.readInt();
            os_2.ulCodePageRange2 = rf.readInt();
        }
        if (version > 1) {
            rf.skipBytes(2);
            os_2.sCapHeight = rf.readShort();
        }
        else
            os_2.sCapHeight = (int)(0.7 * head.unitsPerEm);
        
        table_location = (int[])tables.get("post");
        if (table_location == null) {
            italicAngle = -Math.atan2(hhea.caretSlopeRun, hhea.caretSlopeRise) * 180 / Math.PI;
            return;
        }
        rf.seek(table_location[0] + 4);
        short mantissa = rf.readShort();
        int fraction = rf.readUnsignedShort();
        italicAngle = (double)mantissa + (double)fraction / 16384.0;
        rf.skipBytes(4);
        isFixedPitch = rf.readInt() != 0;
    }
    
    /**
     * Gets the Postscript font name.
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     * @return the Postscript font name
     */
    String getBaseFont() throws DocumentException, IOException {
        int table_location[];
        table_location = (int[])tables.get("name");
        if (table_location == null)
            throw new DocumentException("Table 'name' does not exist in " + fileName + style);
        rf.seek(table_location[0] + 2);
        int numRecords = rf.readUnsignedShort();
        int startOfStorage = rf.readUnsignedShort();
        String postscriptName;
        for (int k = 0; k < numRecords; ++k) {
            int platformID = rf.readUnsignedShort();
            int platformEncodingID = rf.readUnsignedShort();
            int languageID = rf.readUnsignedShort();
            int nameID = rf.readUnsignedShort();
            int length = rf.readUnsignedShort();
            int offset = rf.readUnsignedShort();
            if (nameID == 6) {
                rf.seek(table_location[0] + startOfStorage + offset);
                if (platformID == 0 || platformID == 3)
                    return readUnicodeString(length);
                else
                    return readStandardString(length);
            }
        }
        File file = new File(fileName);
        return file.getName().replace(' ', '-');
    }
    
    /** Extracts the names of the font in all the languages available.
     * @param id the name id to retrieve
     * @throws DocumentException on error
     * @throws IOException on error
     */    
    String[][] getNames(int id) throws DocumentException, IOException {
        int table_location[];
        table_location = (int[])tables.get("name");
        if (table_location == null)
            throw new DocumentException("Table 'name' does not exist in " + fileName + style);
        rf.seek(table_location[0] + 2);
        int numRecords = rf.readUnsignedShort();
        int startOfStorage = rf.readUnsignedShort();
        String postscriptName;
        ArrayList names = new ArrayList();
        for (int k = 0; k < numRecords; ++k) {
            int platformID = rf.readUnsignedShort();
            int platformEncodingID = rf.readUnsignedShort();
            int languageID = rf.readUnsignedShort();
            int nameID = rf.readUnsignedShort();
            int length = rf.readUnsignedShort();
            int offset = rf.readUnsignedShort();
            if (nameID == id) {
                int pos = rf.getFilePointer();
                rf.seek(table_location[0] + startOfStorage + offset);
                String name;
                if (platformID == 0 || platformID == 3 || (platformID == 2 && platformEncodingID == 1)){
                    name = readUnicodeString(length);
                }
                else {
                    name = readStandardString(length);
                }
                names.add(new String[]{String.valueOf(platformID),
                    String.valueOf(platformEncodingID), String.valueOf(languageID), name});
                rf.seek(pos);
            }
        }
        String thisName[][] = new String[names.size()][];
        for (int k = 0; k < names.size(); ++k)
            thisName[k] = (String[])names.get(k);
        return thisName;
    }

    /** Reads the font data.
     * @param ttfAfm the font as a <CODE>byte</CODE> array, possibly <CODE>null</CODE>
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
    void process(byte ttfAfm[]) throws DocumentException, IOException {
        tables = new HashMap();
        
        try {
            if (ttfAfm == null)
                rf = new RandomAccessFileOrArray(fileName);
            else
                rf = new RandomAccessFileOrArray(ttfAfm);
            if (ttcIndex.length() > 0) {
                int dirIdx = Integer.parseInt(ttcIndex);
                if (dirIdx < 0)
                    throw new DocumentException("The font index for " + fileName + " must be positive.");
                String mainTag = readStandardString(4);
                if (!mainTag.equals("ttcf"))
                    throw new DocumentException(fileName + " is not a valid TTC file.");
                rf.skipBytes(4);
                int dirCount = rf.readInt();
                if (dirIdx >= dirCount)
                    throw new DocumentException("The font index for " + fileName + " must be between 0 and " + (dirCount - 1) + ". It was " + dirIdx + ".");
                rf.skipBytes(dirIdx * 4);
                directoryOffset = rf.readInt();
            }
            rf.seek(directoryOffset);
            if (rf.readInt() != 0x00010000)
                throw new DocumentException(fileName + " is not a valid TTF file.");
            int num_tables = rf.readUnsignedShort();
            rf.skipBytes(6);
            for (int k = 0; k < num_tables; ++k) {
                String tag = readStandardString(4);
                rf.skipBytes(4);
                int table_location[] = new int[2];
                table_location[0] = rf.readInt();
                table_location[1] = rf.readInt();
                tables.put(tag, table_location);
            }
            fontName = getBaseFont();
            fullName = getNames(4); //full name
            familyName = getNames(1); //family name
            fillTables();
            readGlyphWidths();
            readCMaps();
            readKerning();
        }
        finally {
            if (rf != null) {
                rf.close();
                if (!embedded)
                    rf = null;
            }
        }
    }
    
    /** Reads a <CODE>String</CODE> from the font file as bytes using the Cp1252
     *  encoding.
     * @param length the length of bytes to read
     * @return the <CODE>String</CODE> read
     * @throws IOException the font file could not be read
     */
    protected String readStandardString(int length) throws IOException {
        byte buf[] = new byte[length];
        rf.readFully(buf);
        try {
            return new String(buf, WINANSI);
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    /** Reads a Unicode <CODE>String</CODE> from the font file. Each character is
     *  represented by two bytes.
     * @param length the length of bytes to read. The <CODE>String</CODE> will have <CODE>length</CODE>/2
     * characters
     * @return the <CODE>String</CODE> read
     * @throws IOException the font file could not be read
     */
    protected String readUnicodeString(int length) throws IOException {
        StringBuffer buf = new StringBuffer();
        length /= 2;
        for (int k = 0; k < length; ++k) {
            buf.append(rf.readChar());
        }
        return buf.toString();
    }
    
    /** Reads the glyphs widths. The widths are extracted from the table 'hmtx'.
     *  The glyphs are normalized to 1000 units.
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
    protected void readGlyphWidths() throws DocumentException, IOException {
        int table_location[];
        table_location = (int[])tables.get("hmtx");
        if (table_location == null)
            throw new DocumentException("Table 'hmtx' does not exist in " + fileName + style);
        rf.seek(table_location[0]);
        GlyphWidths = new int[hhea.numberOfHMetrics];
        for (int k = 0; k < hhea.numberOfHMetrics; ++k) {
            GlyphWidths[k] = (rf.readUnsignedShort() * 1000) / head.unitsPerEm;
            rf.readUnsignedShort();
        }
    }
    
    /** Gets a glyph width.
     * @param glyph the glyph to get the width of
     * @return the width of the glyph in normalized 1000 units
     */
    public int getGlyphWidth(int glyph) {
        if (glyph >= GlyphWidths.length)
            glyph = GlyphWidths.length - 1;
        return GlyphWidths[glyph];
    }
    
    /** Reads the several maps from the table 'cmap'. The maps of interest are 1.0 for symbolic
     *  fonts and 3.1 for all others. A symbolic font is defined as having the map 3.0.
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
    void readCMaps() throws DocumentException, IOException {
        int table_location[];
        table_location = (int[])tables.get("cmap");
        if (table_location == null)
            throw new DocumentException("Table 'cmap' does not exist in " + fileName + style);
        rf.seek(table_location[0]);
        rf.skipBytes(2);
        int num_tables = rf.readUnsignedShort();
        fontSpecific = false;
        int map10 = 0;
        int map31 = 0;
        for (int k = 0; k < num_tables; ++k) {
            int platId = rf.readUnsignedShort();
            int platSpecId = rf.readUnsignedShort();
            int offset = rf.readInt();
            if (platId == 3 && platSpecId == 0) {
                fontSpecific = true;
            }
            else if (platId == 3 && platSpecId == 1) {
                map31 = offset;
            }
            if (platId == 1 && platSpecId == 0) {
                map10 = offset;
            }
        }
        if (map10 > 0) {
            rf.seek(table_location[0] + map10);
            int format = rf.readUnsignedShort();
            switch (format) {
                case 0:
                    cmap10 = readFormat0();
                    break;
                case 4:
                    cmap10 = readFormat4();
                    break;
                case 6:
                    cmap10 = readFormat6();
                    break;
            }
        }
        if (map31 > 0) {
            rf.seek(table_location[0] + map31);
            int format = rf.readUnsignedShort();
            if (format == 4) {
                cmap31 = readFormat4();
            }
        }
    }
    
    /** The information in the maps of the table 'cmap' is coded in several formats.
     *  Format 0 is the Apple standard character to glyph index mapping table.
     * @return a <CODE>HashMap</CODE> representing this map
     * @throws IOException the font file could not be read
     */
    HashMap readFormat0() throws IOException {
        HashMap h = new HashMap();
        rf.skipBytes(4);
        for (int k = 0; k < 256; ++k) {
            int r[] = new int[2];
            r[0] = rf.readUnsignedByte();
            r[1] = getGlyphWidth(r[0]);
            h.put(new Integer(k), r);
        }
        return h;
    }
    
    /** The information in the maps of the table 'cmap' is coded in several formats.
     *  Format 4 is the Microsoft standard character to glyph index mapping table.
     * @return a <CODE>HashMap</CODE> representing this map
     * @throws IOException the font file could not be read
     */
    HashMap readFormat4() throws IOException {
        HashMap h = new HashMap();
        int table_lenght = rf.readUnsignedShort();
        rf.skipBytes(2);
        int segCount = rf.readUnsignedShort() / 2;
        rf.skipBytes(6);
        int endCount[] = new int[segCount];
        for (int k = 0; k < segCount; ++k) {
            endCount[k] = rf.readUnsignedShort();
        }
        rf.skipBytes(2);
        int startCount[] = new int[segCount];
        for (int k = 0; k < segCount; ++k) {
            startCount[k] = rf.readUnsignedShort();
        }
        int idDelta[] = new int[segCount];
        for (int k = 0; k < segCount; ++k) {
            idDelta[k] = rf.readUnsignedShort();
        }
        int idRO[] = new int[segCount];
        for (int k = 0; k < segCount; ++k) {
            idRO[k] = rf.readUnsignedShort();
        }
        int glyphId[] = new int[table_lenght / 2 - 8 - segCount * 4];
        for (int k = 0; k < glyphId.length; ++k) {
            glyphId[k] = rf.readUnsignedShort();
        }
        for (int k = 0; k < segCount; ++k) {
            int glyph;
            for (int j = startCount[k]; j <= endCount[k] && j != 0xFFFF; ++j) {
                if (idRO[k] == 0) {
                    glyph = (j + idDelta[k]) & 0xFFFF;
                }
                else {
                    int idx = k + idRO[k] / 2 - segCount + j - startCount[k];
                    glyph = (glyphId[idx] + idDelta[k]) & 0xFFFF;
                }
                int r[] = new int[2];
                r[0] = glyph;
                r[1] = getGlyphWidth(r[0]);
                h.put(new Integer(j), r);
            }
        }
        return h;
    }
    
    /** The information in the maps of the table 'cmap' is coded in several formats.
     *  Format 6 is a trimmed table mapping. It is similar to format 0 but can have
     *  less than 256 entries.
     * @return a <CODE>HashMap</CODE> representing this map
     * @throws IOException the font file could not be read
     */
    HashMap readFormat6() throws IOException {
        HashMap h = new HashMap();
        rf.skipBytes(4);
        int start_code = rf.readUnsignedShort();
        int code_count = rf.readUnsignedShort();
        for (int k = 0; k < code_count; ++k) {
            int r[] = new int[2];
            r[0] = rf.readUnsignedShort();
            r[1] = getGlyphWidth(r[0]);
            h.put(new Integer(k + start_code), r);
        }
        return h;
    }
    
    /** Reads the kerning information from the 'kern' table.
     * @throws IOException the font file could not be read
     */
    void readKerning() throws IOException {
        int table_location[];
        table_location = (int[])tables.get("kern");
        if (table_location == null)
            return;
        rf.seek(table_location[0] + 2);
        int nTables = rf.readUnsignedShort();
        kerning = new HashMap();
        int checkpoint = table_location[0] + 4;
        int length = 0;
        for (int k = 0; k < nTables; ++k) {
            checkpoint += length;
            rf.seek(checkpoint);
            rf.skipBytes(2);
            length = rf.readUnsignedShort();
            int coverage = rf.readUnsignedShort();
            if ((coverage & 0xfff7) == 0x0001) {
                int nPairs = rf.readUnsignedShort();
                rf.skipBytes(6);
                for (int j = 0; j < nPairs; ++j) {
                    Integer pair = new Integer(rf.readInt());
                    Integer value = new Integer(((int)rf.readShort() * 1000) / head.unitsPerEm);
                    kerning.put(pair, value);
                }
            }
        }
    }
    
    /** Gets the kerning between two Unicode chars.
     * @param char1 the first char
     * @param char2 the second char
     * @return the kerning to be applied
     */
    public int getKerning(char char1, char char2) {
        Integer v = (Integer)kerning.get(new Integer((((int)char1) << 16) + ((int)char2)));
        if (v == null)
            return 0;
        else
            return v.intValue();
    }
    
    /** Gets the width from the font according to the unicode char <CODE>c</CODE>.
     * If the <CODE>name</CODE> is null it's a symbolic font.
     * @param c the unicode char
     * @param name the glyph name
     * @return the width of the char
     */
    protected int getRawWidth(int c, String name) {
        HashMap map = null;
        if (name == null)
            map = cmap10;
        else
            map = cmap31;
        if (map == null)
            return 0;
        int metric[] = (int[])map.get(new Integer(c));
        if (metric == null)
            return 0;
        return metric[1];
    }
    
    /** Generates the font descriptor for this font.
     * @return the PdfDictionary containing the font descriptor or <CODE>null</CODE>
     * @param subsetPrefix the subset prefix
     * @param fontStream the indirect reference to a PdfStream containing the font or <CODE>null</CODE>
     * @throws DocumentException if there is an error
     */
    protected PdfDictionary getFontDescriptor(PdfIndirectReference fontStream, String subsetPrefix) throws DocumentException {
        PdfDictionary dic = new PdfDictionary(new PdfName("FontDescriptor"));
        dic.put(new PdfName("Ascent"), new PdfNumber((int)os_2.sTypoAscender * 1000 / head.unitsPerEm));
        dic.put(new PdfName("CapHeight"), new PdfNumber((int)os_2.sCapHeight * 1000 / head.unitsPerEm));
        dic.put(new PdfName("Descent"), new PdfNumber((int)os_2.sTypoDescender * 1000 / head.unitsPerEm));
        dic.put(new PdfName("FontBBox"), new PdfRectangle(
        (int)head.xMin * 1000 / head.unitsPerEm,
        (int)head.yMin * 1000 / head.unitsPerEm,
        (int)head.xMax * 1000 / head.unitsPerEm,
        (int)head.yMax * 1000 / head.unitsPerEm));
        dic.put(new PdfName("FontName"), new PdfName(subsetPrefix + fontName + style));
        dic.put(new PdfName("ItalicAngle"), new PdfNumber(italicAngle));
        dic.put(new PdfName("StemV"), new PdfNumber(80));
        if (fontStream != null)
            dic.put(new PdfName("FontFile2"), fontStream);
        int flags = 0;
        if (isFixedPitch)
            flags |= 1;
        flags |= fontSpecific ? 4 : 32;
        if ((head.macStyle & 2) != 0)
            flags |= 64;
        if ((head.macStyle & 1) != 0)
            flags |= 262144;
        dic.put(new PdfName("Flags"), new PdfNumber(flags));
        
        return dic;
    }
    
    /** Generates the font dictionary for this font.
     * @return the PdfDictionary containing the font dictionary
     * @param subsetPrefix the subset prefx
     * @param firstChar the first valid character
     * @param lastChar the last valid character
     * @param shortTag a 256 bytes long <CODE>byte</CODE> array where each unused byte is represented by 0
     * @param fontDescriptor the indirect reference to a PdfDictionary containing the font descriptor or <CODE>null</CODE>
     * @throws DocumentException if there is an error
     */
    protected PdfDictionary getFontBaseType(PdfIndirectReference fontDescriptor, String subsetPrefix, int firstChar, int lastChar, byte shortTag[]) throws DocumentException {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, new PdfName("TrueType"));
        dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + fontName + style));
        if (!fontSpecific) {
            for (int k = firstChar; k <= lastChar; ++k) {
                if (!differences[k].equals(notdef)) {
                    firstChar = k;
                    break;
                }
            }
        if (encoding.equals("Cp1252") || encoding.equals("MacRoman"))
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
        if (fontDescriptor != null)
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
        if (!subset) {
            firstChar = 0;
            lastChar = shortTag.length - 1;
            for (int k = 0; k < shortTag.length; ++k)
                shortTag[k] = 1;
        }
        PdfIndirectReference ind_font = null;
        PdfObject pobj = null;
        PdfIndirectObject obj = null;
        String subsetPrefix = "";
        if (embedded) {
            subsetPrefix = createSubsetPrefix();
            HashMap glyphs = new HashMap();
            for (int k = firstChar; k <= lastChar; ++k) {
                if (shortTag[k] != 0) {
                    int metrics[];
                    if (fontSpecific)
                        metrics = getMetricsTT(k);
                    else
                        metrics = getMetricsTT(unicodeDifferences[k]);
                    if (metrics != null)
                        glyphs.put(new Integer(metrics[0]), null);
                }
            }
            TrueTypeFontSubSet sb = new TrueTypeFontSubSet(fileName, rf, glyphs, directoryOffset, true);
            byte b[] = sb.process();
            int lengths[] = new int[]{b.length};
            pobj = new StreamFont(b, lengths);
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        }
        pobj = getFontDescriptor(ind_font, subsetPrefix);
        if (pobj != null){
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        }
        pobj = getFontBaseType(ind_font, subsetPrefix, firstChar, lastChar, shortTag);
        writer.addToBody(pobj, ref);
    }
    
    /** Gets the font parameter identified by <CODE>key</CODE>. Valid values
     * for <CODE>key</CODE> are <CODE>ASCENT</CODE>, <CODE>CAPHEIGHT</CODE>, <CODE>DESCENT</CODE>
     * and <CODE>ITALICANGLE</CODE>.
     * @param key the parameter to be extracted
     * @param fontSize the font size in points
     * @return the parameter in points
     */    
    public float getFontDescriptor(int key, float fontSize) {
        switch (key) {
            case ASCENT:
                return (float)os_2.sTypoAscender * fontSize / (float)head.unitsPerEm;
            case CAPHEIGHT:
                return (float)os_2.sCapHeight * fontSize / (float)head.unitsPerEm;
            case DESCENT:
                return (float)os_2.sTypoDescender * fontSize / (float)head.unitsPerEm;
            case ITALICANGLE:
                return (float)italicAngle;
            case BBOXLLX:
                return fontSize * (int)head.xMin / head.unitsPerEm;
            case BBOXLLY:
                return fontSize * (int)head.yMin / head.unitsPerEm;
            case BBOXURX:
                return fontSize * (int)head.xMax / head.unitsPerEm;
            case BBOXURY:
                return fontSize * (int)head.yMax / head.unitsPerEm;
            case AWT_ASCENT:
                return fontSize * (int)hhea.Ascender / head.unitsPerEm;
            case AWT_DESCENT:
                return fontSize * (int)hhea.Descender / head.unitsPerEm;
            case AWT_LEADING:
                return fontSize * (int)hhea.LineGap / head.unitsPerEm;
            case AWT_MAXADVANCE:
                return fontSize * (int)hhea.advanceWidthMax / head.unitsPerEm;
        }
        return 0;
    }
    
    /** Gets the glyph index and metrics for a character.
     * @param c the character
     * @return an <CODE>int</CODE> array with {glyph index, width}
     */    
    public int[] getMetricsTT(int c) {
        if (!fontSpecific && cmap31 != null) 
            return (int[])cmap31.get(new Integer(c));
        if (fontSpecific && cmap10 != null) 
            return (int[])cmap10.get(new Integer(c));
        return null;
    }

    /** Gets the postscript font name.
     * @return the postscript font name
     */
    public String getPostscriptFontName() {
        return fontName;
    }

    /** Gets the code pages supported by the font.
     * @return the code pages supported by the font
     */
    public String[] getCodePagesSupported() {
        long cp = (((long)os_2.ulCodePageRange2) << 32) + ((long)os_2.ulCodePageRange1 & 0xffffffffL);
        int count = 0;
        long bit = 1;
        for (int k = 0; k < 64; ++k) {
            if ((cp & bit) != 0 && codePages[k] != null)
                ++count;
            bit <<= 1;
        }
        String ret[] = new String[count];
        count = 0;
        bit = 1;
        for (int k = 0; k < 64; ++k) {
            if ((cp & bit) != 0 && codePages[k] != null)
                ret[count++] = codePages[k];
            bit <<= 1;
        }
        return ret;
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
        return fullName;
    }
    
    /** Gets the family name of the font. If it is a True Type font
     * each array element will have {Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"", "", "",
     * font name}.
     * @return the family name of the font
     */
    public String[][] getFamilyFontName() {
        return familyName;
    }
    
}

