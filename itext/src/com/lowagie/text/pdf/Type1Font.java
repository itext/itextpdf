/*
 * $Id$
 * $Name$
 *
 * Copyright 2001 by Paulo Soares
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
public class Type1Font extends BaseFont
{
/** The Postscript font name.
 */
    private String FontName;
/** The full name of the font.
 */
    private String FullName;
/** The family name of the font.
 */
    private String FamilyName;
/** The weight of the font: normal, bold, etc.
 */
    private String Weight = "";
/** The italic angle of the font, usually 0.0 or negative.
 */
    private float ItalicAngle = 0.0f;
/** <CODE>true</CODE> if all the characters have the same
 *  width.
 */
    private boolean IsFixedPitch = false;
/** The character set of the font.
 */
    private String CharacterSet;
/** The llx of the FontBox.
 */
    private int llx = -50;
/** The lly of the FontBox.
 */
    private int lly = -200;
/** The lurx of the FontBox.
 */
    private int urx = 1000;
/** The ury of the FontBox.
 */
    private int ury = 900;
/** The underline position.
 */
    private int UnderlinePosition = -100;
/** The underline thickness.
 */
    private int UnderlineThickness = 50;
/** The font's encoding name. This encoding is 'StandardEncoding' or
 *  'AdobeStandardEncoding' for a font that can be totally encoded
 *  according to the characters names. For all other names the
 *  font is treated as symbolic.
 */
    private String EncodingScheme = "FontSpecific";
/** A variable.
 */
    private int CapHeight = 700;
/** A variable.
 */
    private int XHeight = 480;
/** A variable.
 */
    private int Ascender = 800;
/** A variable.
 */
    private int Descender = -200;
/** A variable.
 */
    private int StdHW;
/** A variable.
 */
    private int StdVW = 80;
    
/** Represents the section CharMetrics in the AFM file. Each
 *  element of this array contains a <CODE>Object[3]</CODE> with an
 *  Integer, Integer and String. This is the code, width and name.
 */
    private ArrayList CharMetrics = new ArrayList();
/** Represents the section KernPairs in the AFM file. The key is
 *  the name of the first character and the value is a <CODE>Object[]</CODE>
 *  with 2 elements for each kern pair. Position 0 is the name of
 *  the second character and position 1 is the kerning distance. This is
 *  repeated for all the pairs.
 */
    private HashMap KernPairs = new HashMap();
/** The file in use.
 */
    private String fileName;
/** <CODE>true</CODE> if this font is one of the 14 built in fonts.
 */
    private boolean builtinFont = false;
/** Types of records in a PFB file. 1 is ASCII and 2 is BINARY.
 *  They have to appear in the PFB file in this sequence.
 */
    private final static int pfbTypes[] = {1, 2, 1};
    
/** Creates a new Type1 font.
 * @param afmFile the name of one of the 14 built-in fonts or the location of an AFM file. The file must end in '.afm'
 * @param enc the encoding to be applied to this font
 * @param emb true if the font is to be embedded in the PDF
 * @throws DocumentException the AFM file is invalid
 * @throws IOException the AFM file could not be read
 */
    public Type1Font(String afmFile, String enc, boolean emb) throws DocumentException, IOException
    {
        encoding = enc;
        embedded = emb;
        fileName = afmFile;
        BufferedReader fin = null;
        if (BuiltinFonts14.containsKey(afmFile)) {
            embedded = false;
            builtinFont = true;
            try {
                String afm = null;
                if (afmFile.equals("Courier"))
                    afm = Courier.afm;
                else if (afmFile.equals("Courier-Bold"))
                    afm = CourierBold.afm;
                else if (afmFile.equals("Courier-BoldOblique"))
                    afm = CourierBoldOblique.afm;
                else if (afmFile.equals("Courier-Oblique"))
                    afm = CourierOblique.afm;
                else if (afmFile.equals("Helvetica-Bold")) {
                    afm = HelveticaBold1.afm;
                    afm += HelveticaBold2.afm;
                }
                else if (afmFile.equals("Helvetica-BoldOblique")) {
                    afm = HelveticaBoldOblique1.afm;
                    afm += HelveticaBoldOblique2.afm;
                }
                else if (afmFile.equals("Helvetica-Oblique")) {
                    afm = HelveticaOblique1.afm;
                    afm += HelveticaOblique2.afm;
                }
                else if (afmFile.equals("Symbol"))
                    afm = Symbol.afm;
                else if (afmFile.equals("Times-Roman"))
                    afm = TimesRoman.afm;
                else if (afmFile.equals("Times-Bold"))
                    afm = TimesBold.afm;
                else if (afmFile.equals("Times-BoldItalic"))
                    afm = TimesBoldItalic.afm;
                else if (afmFile.equals("Times-Italic")) {
                    afm = TimesItalic1.afm;
                    afm += TimesItalic2.afm;
                }
                else if (afmFile.equals("ZapfDingbats"))
                    afm = ZapfDingbats.afm;
                else {
                    afm = Helvetica1.afm;
                    afm += Helvetica2.afm;
                }
                fin = new BufferedReader(new StringReader(afm));
                process(fin);
            }
            finally {
                if (fin != null) {
                    try {
                        fin.close();
                    }
                    catch (Exception e) {
                    }
                }
            }
        }
        else if (afmFile.toLowerCase().endsWith(".afm")) {
            try {
                fin = new BufferedReader(new InputStreamReader(new FileInputStream(afmFile), PdfObject.ENCODING));
                if (fin == null)
                    throw new DocumentException(afmFile + " not found as file.");
                process(fin);
            }
            finally {
                if (fin != null) {
                    try {
                        fin.close();
                    }
                    catch (Exception e) {
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
  * @param fin AFM file with the font metrics
  * @throws DocumentException the AFM file is invalid
  * @throws IOException the AFM file could not be read
  */
    public void process(BufferedReader fin) throws DocumentException, IOException
    {
        String line;
        boolean isMetrics = false;
        while ((line = fin.readLine()) != null)
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
        while ((line = fin.readLine()) != null)
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
        while ((line = fin.readLine()) != null)
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
        while ((line = fin.readLine()) != null)
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
        fin.close();
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
        InputStream is = null;
        try {
            File file = new File(fileName.substring(0, fileName.length() - 3) + "pfb");
            int fileLength = (int)file.length();
            byte st[] = new byte[fileLength - 18];
            is = new FileInputStream(file);
            int lengths[] = new int[3];
            int bytePtr = 0;
            for (int k = 0; k < 3; ++k) {
                if (is.read() != 0x80)
                    throw new DocumentException("Start marker missing in " + file.getName());
                if (is.read() != pfbTypes[k])
                    throw new DocumentException("Incorrect segment type in " + file.getName());
                int size = is.read();
                size += is.read() << 8;
                size += is.read() << 16;
                size += is.read() << 24;
                lengths[k] = size;
                while (size != 0) {
                    int got = is.read(st, bytePtr, size);
                    if (got < 0)
                        throw new DocumentException("Premature end in " + file.getName());
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
            if (is != null) {
                try {
                    is.close();
                }
                catch (Exception e) {
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
 * @param fontDescriptor the indirect reference to a PdfDictionary containing the font descriptor
 *   or <CODE>null</CODE>
 * @return the PdfDictionary containing the font dictionary
 * @throws DocumentException if there is an error
 */
    private PdfDictionary getFontType(PdfIndirectReference fontDescriptor) throws DocumentException
    {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
        dic.put(PdfName.BASEFONT, new PdfName(FontName));
        boolean stdEncoding = encoding.equals("Cp1252") || encoding.equals("MacRoman");
        int firstChar = 0;
        if (!fontSpecific) {
            for (int k = 0; k < 256; ++k) {
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
                dif.add(new PdfNumber(firstChar));
                for (int k = firstChar; k < 256; ++k) {
                    dif.add(new PdfName(differences[k]));
                }
                enc.put(new PdfName("Differences"), dif);
                dic.put(PdfName.ENCODING, enc);
            }
        }
        if (!(builtinFont && (fontSpecific || stdEncoding))) {
            dic.put(new PdfName("FirstChar"), new PdfNumber(firstChar));
            dic.put(new PdfName("LastChar"), new PdfNumber(255));
            PdfArray wd = new PdfArray();
            for (int k = firstChar; k < 256; ++k) {
                wd.add(new PdfNumber(widths[k]));
            }
            dic.put(new PdfName("Widths"), wd);
        }
        if (!builtinFont && fontDescriptor != null)
            dic.put(new PdfName("FontDescriptor"), fontDescriptor);
        return dic;
    }
    
/** Generates the dictionary or stream required to represent the font.
 *  <CODE>index</CODE> will cycle from 0 to 2 with the next cycle beeing fed
 *  with the indirect reference from the previous cycle.
 * <P>
 *  A 0 generates the font stream.
 * <P>
 *  A 1 generates the font descriptor.
 * <P>
 *  A 2 generates the font dictionary.
 * @param iobj an indirect reference to a Pdf object. May be null
 * @param index the type of object to generate. It may be 0, 1 or 2
 * @return the object requested
 * @throws DocumentException error in generating the object
 */
    PdfObject getFontInfo(PdfIndirectReference iobj, int index) throws DocumentException
    {
        switch (index) {
            case 0:
                return getFontStream();
            case 1:
                return getFontDescriptor(iobj);
            case 2:
                return getFontType(iobj);
        }
        return null;
    }
}
