package com.lowagie.text.pdf;

import com.lowagie.text.DocumentException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

public class Type1 extends BaseFont
{
    private String FontName;
    private String FullName;
    private String FamilyName;
    private String Weight = "";
    private float ItalicAngle = 0.0f;
    private boolean IsFixedPitch = false;
    private String CharacterSet;
    private int llx = -50;
    private int lly = -200;
    private int urx = 1000;
    private int ury = 900;
    private int UnderlinePosition = -100;
    private int UnderlineThickness = 50;
    private String EncodingScheme = "FontSpecific";
    private int CapHeight = 700;
    private int XHeight;
    private int Ascender = 800;
    private int Descender = -200;
    private int StdHW;
    private int StdVW = 80;

    private ArrayList CharMetrics = new ArrayList();
    private HashMap KernPairs = new HashMap();
    private String fileName;
    private boolean builtinFont = false;
    private final static int pfbTypes[] = {1, 2, 1};

    public Type1(String afmFile, String enc, boolean emb) throws DocumentException, IOException
    {
        normalizeEncoding(enc);
        embedded = emb;
        fileName = afmFile;
        InputStream is = null;
        if (BuiltinFonts14.containsKey(afmFile)) {
            embedded = false;
            builtinFont = true;
            try {
                is = getClass().getResourceAsStream("afm/" + afmFile + ".afm");
                if (is == null)
                    throw new DocumentException(afmFile + " not found as resource.");
                process(is);
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
        else if (afmFile.toLowerCase().endsWith(".afm")) {
            try {
                is = new FileInputStream(afmFile);
                if (is == null)
                    throw new DocumentException(afmFile + " not found as file.");
                process(is);
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
        else
            throw new DocumentException(afmFile + " is not an AFM font file.");
        try {
            EncodingScheme = EncodingScheme.trim();
            if (EncodingScheme.equals("AdobeStandardEncoding") || EncodingScheme.equals("StandardEncoding"))
                fontSpecific = false;
            createEncoding();
        }
        catch (Exception e) {
            throw new DocumentException(e.getMessage());
        }
    }
    
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
    
    
 /** Read the font metrics
 * @param afmFile AFM file with the font metrics
 * @throws Exception if anything goes wrong
 */    
    public void process(InputStream afmStream) throws DocumentException, IOException
    {
        BufferedReader fin = new BufferedReader(new InputStreamReader(afmStream));
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
                ItalicAngle = Float.parseFloat(tok.nextToken());
            else if (ident.equals("IsFixedPitch"))
                IsFixedPitch = tok.nextToken().equals("true");
            else if (ident.equals("CharacterSet"))
                CharacterSet = tok.nextToken("\u00ff").substring(1);
            else if (ident.equals("FontBBox"))
            {
                llx = (int)Float.parseFloat(tok.nextToken());
                lly = (int)Float.parseFloat(tok.nextToken());
                urx = (int)Float.parseFloat(tok.nextToken());
                ury = (int)Float.parseFloat(tok.nextToken());
            }
            else if (ident.equals("UnderlinePosition"))
                UnderlinePosition = (int)Float.parseFloat(tok.nextToken());
            else if (ident.equals("UnderlineThickness"))
                UnderlineThickness = (int)Float.parseFloat(tok.nextToken());
            else if (ident.equals("EncodingScheme"))
                EncodingScheme = tok.nextToken("\u00ff").substring(1);
            else if (ident.equals("CapHeight"))
                CapHeight = (int)Float.parseFloat(tok.nextToken());
            else if (ident.equals("XHeight"))
                XHeight = (int)Float.parseFloat(tok.nextToken());
            else if (ident.equals("Ascender"))
                Ascender = (int)Float.parseFloat(tok.nextToken());
            else if (ident.equals("Descender"))
                Descender = (int)Float.parseFloat(tok.nextToken());
            else if (ident.equals("StdHW"))
                StdHW = (int)Float.parseFloat(tok.nextToken());
            else if (ident.equals("StdVW"))
                StdVW = (int)Float.parseFloat(tok.nextToken());
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
                Integer width = new Integer((int)Float.parseFloat(tok.nextToken()));
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

    private PdfDictionary getFontType(PdfIndirectReference fontDescriptor) throws DocumentException
    {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, PdfName.TYPE1);
        dic.put(PdfName.BASEFONT, new PdfName(FontName));
        int firstChar = 0;
        for (int k = 0; k < 256; ++k) {
            if (!differences[k].equals(notdef)) {
                firstChar = k;
                break;
            }
        }
        if (!fontSpecific) {
            if (encoding.equals("Cp1252") || encoding.equals("MacRoman"))
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
        if (!builtinFont || (!fontSpecific && !encoding.equals("Cp1252") && !encoding.equals("MacRoman"))) {
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