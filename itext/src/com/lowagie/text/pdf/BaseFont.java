/*
 * BaseFont.java
 *
 * Created on March 10, 2001, 7:07 PM
 */

package com.lowagie.text.pdf;

import java.io.*;
import com.lowagie.text.DocumentException;
import java.util.HashMap;
/**
 *
 * @author  Administrator
 * @version 
 */
public abstract class BaseFont
{
    public final static String notdef = new String(".notdef");
    protected int widths[] = new int[256];
    protected String differences[] = new String[256];
    protected String encoding;
    protected boolean embedded;
    protected boolean fontSpecific = true;

    protected static final HashMap BuiltinFonts14 = new HashMap();
    static
    {
        BuiltinFonts14.put("Courier", PdfName.COURIER);
        BuiltinFonts14.put("Courier-Bold", PdfName.COURIER_BOLD);
        BuiltinFonts14.put("Courier-BoldOblique", PdfName.COURIER_BOLDOBLIQUE);
        BuiltinFonts14.put("Courier-Oblique", PdfName.COURIER_OBLIQUE);
        BuiltinFonts14.put("Helvetica", PdfName.HELVETICA);
        BuiltinFonts14.put("Helvetica-Bold", PdfName.HELVETICA_BOLD);
        BuiltinFonts14.put("Helvetica-BoldOblique", PdfName.HELVETICA_BOLDOBLIQUE);
        BuiltinFonts14.put("Helvetica-Oblique", PdfName.HELVETICA_OBLIQUE);
        BuiltinFonts14.put("Symbol", PdfName.SYMBOL);
        BuiltinFonts14.put("Times-Roman", PdfName.TIMES_ROMAN);
        BuiltinFonts14.put("Times-Bold", PdfName.TIMES_BOLD);
        BuiltinFonts14.put("Times-BoldItalic", PdfName.TIMES_BOLDITALIC);
        BuiltinFonts14.put("Times-Italic", PdfName.TIMES_ITALIC);
        BuiltinFonts14.put("ZapfDingbats", PdfName.ZAPFDINGBATS);
    }

    class StreamFont extends PdfStream
    {
        public StreamFont(byte contents[], int lengths[]) throws DocumentException
        {
            try {
                bytes = contents;
                dictionary.put(PdfName.LENGTH, new PdfNumber(bytes.length));
                for (int k = 0; k < lengths.length; ++k) {
                    dictionary.put(new PdfName("Length" + (k + 1)), new PdfNumber(lengths[k]));
                }
                flateCompress();
            }
            catch (Exception e) {
                throw new DocumentException(e.getMessage());
            }
        }
    }

    /** Creates new BaseFont */
    public BaseFont() {
    }

    public static BaseFont createFont(String name, String encoding, boolean embedded) throws DocumentException, IOException
    {
        if (BuiltinFonts14.containsKey(name) || name.toLowerCase().endsWith(".afm")) {
            return new Type1(name, encoding, embedded);
        }
        else if (name.toLowerCase().endsWith(".ttf")) {
            return new TrueType(name, encoding, embedded);
        }
        else if (CJKFont.isCJKFont(name, encoding))
            return new CJKFont(name, encoding, embedded);
        else
            throw new DocumentException("Font '" + name + "' with '" + encoding + "' is not recognized.");
    }
    
    protected void normalizeEncoding(String enc)
    {
        if (enc.equals("winansi") || enc.equals(""))
            encoding = "Cp1252";
        else if (enc.equals("macroman"))
            encoding = "MacRoman";
        else
            encoding = enc;
    }
    
    protected void createEncoding() throws UnsupportedEncodingException
    {
        byte b[] = new byte[256];
        for (int k = 0; k < 256; ++k)
        {
            b[k] = (byte)(k);
        }
        if (fontSpecific) {
            for (int k = 0; k < 256; ++k)
                widths[k] = getRawWidth(k, null);
        }
        else {
            String s = new String(b, encoding);
            for (int k = 0; k < 256; ++k)
            {
                char c = s.charAt(k);
                String name = GlyphList.unicodeToName((int)c);
                if (name == null)
                    name = notdef;
                differences[k] = name;
                widths[k] = getRawWidth((int)c, name);
            }
        }    
    }
    
    protected abstract int getRawWidth(int c, String name);
    
    public abstract int getKerning(char char1, char char2);
    
    public int getWidth(char char1)
    {
        return getWidth(new String(new char[]{char1}));
    }
    
    public int getWidth(String text)
    {
        int total = 0;
        try {
            byte mbytes[] = text.getBytes(encoding);
            for (int k = 0; k < mbytes.length; ++k)
                total += widths[0xff & mbytes[k]];
        }
        catch (UnsupportedEncodingException e) {
        }
        return total;
    }
    
    public float getWidthPoint(String text, int fontSize)
    {
        return (float)((double)getWidth(text) * 0.001 * fontSize);
    }
    
    byte[] convertToBytes(String text) throws UnsupportedEncodingException
    {
        return text.getBytes(encoding);
    }
    
    abstract PdfObject getFontInfo(PdfIndirectReference iobj, int index) throws DocumentException;
}
