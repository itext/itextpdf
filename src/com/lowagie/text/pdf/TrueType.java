package com.lowagie.text.pdf;

import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.Iterator;
import com.lowagie.text.DocumentException;
/**
 * Insert the type's description here.
 * Creation date: (12/16/00 18:17:28)
 * @author: Administrator
 */
public class TrueType extends BaseFont
{
	protected HashMap tables;
	protected RandomAccessFile rf;
	protected String fileName;
	protected FontHeader head = new FontHeader();
	protected HorizontalHeader hhea = new HorizontalHeader();
	protected WindowsMetrics os_2 = new WindowsMetrics();
    protected int GlyphWidths[];
    protected HashMap cmap10;
    protected HashMap cmap31;
    protected HashMap kerning;
    protected String fontName;
    protected double italicAngle;
    protected boolean isFixedPitch = false;

	protected class FontHeader
	{
		int flags;
		int unitsPerEm;
		short xMin;
		short yMin;
		short xMax;
		short yMax;
		int macStyle;
	}
	
	protected class HorizontalHeader
	{
		short Ascender;
		short Descender;
		short LineGap;
		int advanceWidthMax;
		short minLeftSideBearing;
		short minRightSideBearing;
		short xMaxExtent;
		short caretSlopeRise;
		short caretSlopeRun;
		int numberOfHMetrics;
	}

	protected class WindowsMetrics
	{
		short xAvgCharWidth;
		int usWeightClass;
		int usWidthClass;
		short fsType;
		short ySubscriptXSize;
		short ySubscriptYSize;
		short ySubscriptXOffset;
		short ySubscriptYOffset;
		short ySuperscriptXSize;
		short ySuperscriptYSize;
		short ySuperscriptXOffset;
		short ySuperscriptYOffset;
		short yStrikeoutSize;
		short yStrikeoutPosition;
		short sFamilyClass;
		byte panose[] = new byte[10];
		byte achVendID[] = new byte[4];
		int fsSelection;
		int usFirstCharIndex;
		int usLastCharIndex;
		short sTypoAscender;
		short sTypoDescender;
		short sTypoLineGap;
		int usWinAscent;
		int usWinDescent;
	}

    protected TrueType()
    {
    }
    
    public TrueType(String ttFile, String enc, boolean emb) throws DocumentException, IOException
    {
        normalizeEncoding(enc);
        embedded = emb;
        fileName = ttFile;
        if (fileName.toLowerCase().endsWith(".ttf")) {
            process(fileName);
        }
        else
            throw new DocumentException(fileName + " is not a TTF font file.");
        try {
            createEncoding();
        }
        catch (UnsupportedEncodingException e) {
            throw new DocumentException(e.getMessage());
        }
    }

    
    void fillTables() throws DocumentException, IOException
    {
    	int table_location[];
    	table_location = (int[])tables.get("head");
    	if (table_location == null)
    		throw new DocumentException("Table 'head' does not exist in " + fileName);
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
    		throw new DocumentException("Table 'hhea' does not exist " + fileName);
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
    		throw new DocumentException("Table 'OS/2' does not exist in " + fileName);
    	rf.seek(table_location[0] + 2);
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
    
    String getBaseFont() throws DocumentException, IOException
    {
    	int table_location[];
    	table_location = (int[])tables.get("name");
    	if (table_location == null)
    		throw new DocumentException("Table 'name' does not exist in " + fileName);
    	rf.seek(table_location[0] + 2);
    	int numRecords = rf.readUnsignedShort();
    	int startOfStorage = rf.readUnsignedShort();
    	String postscriptName;
    	for (int k = 0; k < numRecords; ++k)
    	{
    		int platformID = rf.readUnsignedShort();
    		int platformEncodingID = rf.readUnsignedShort();
    		int languageID = rf.readUnsignedShort();
    		int nameID = rf.readUnsignedShort();
    		int length = rf.readUnsignedShort();
    		int offset = rf.readUnsignedShort();
    		if (nameID == 6)
    		{
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

    void process(String file) throws DocumentException, IOException
    {
    	tables = new HashMap();
    	
    	try
    	{
    		fileName = file;
    		rf = new RandomAccessFile(fileName, "r");
    		rf.seek(4);
    		int num_tables = rf.readUnsignedShort();
    		rf.seek(12);
    		for (int k = 0; k < num_tables; ++k)
    		{
    			String tag = readStandardString(4);
    			rf.skipBytes(4);
    			int table_location[] = new int[2];
    			table_location[0] = rf.readInt();
    			table_location[1] = rf.readInt();
    			tables.put(tag, table_location);
    		}
    		fontName = getBaseFont();
            fillTables();
            readGlyphWidths();
            readCMaps();
            readKerning();
    	}
    	finally
    	{
    		if (rf != null)
    			rf.close();
    	}
    }
    String readStandardString(int length) throws IOException
    {
    	byte buf[] = new byte[length];
    	rf.readFully(buf);
    	return new String(buf);
    }
    String readUnicodeString(int length) throws IOException
    {
    	StringBuffer buf = new StringBuffer();
    	length /= 2;
    	for (int k = 0; k < length; ++k)
    	{
    		buf.append(rf.readChar());
    	}
    	return buf.toString();
    }

/*    public String toString()
    {
    	StringBuffer ret = new StringBuffer();
    	try
    	{
    		ret.append("Table Locations\n");
    		for (Iterator i = tables.keySet().iterator(); i.hasNext();)
    		{
    			String k = (String)i.next();
    			ret.append("    '").append(k).append("'  off = 0x");
    			int v[] = (int[])tables.get(k);
    			k = Integer.toHexString(v[0]);
    			k = "0000000" + k;
    			k = k.substring(k.length() - 8, k.length());
    			ret.append(k).append(", len = ");
    			k = Integer.toString(v[1]);
    			k = "       " + k;
    			k = k.substring(k.length() - 8, k.length());
    			ret.append(k).append("\n");
    		}
    		ret.append("\nKeys\n");
    		for (Iterator i = results.keySet().iterator(); i.hasNext();)
    		{
    			String k = (String)i.next();
    			String s = (String)results.get(k);
    			ret.append("    ").append(k).append(" - ").append(s).append("\n");
    		}
    		ret.append("\nCMaps\n");
            if (cmap10 != null)
            {
                ret.append("    map10\n");
                TreeMap map = new TreeMap();
                for (Iterator i = cmap10.keySet().iterator(); i.hasNext();)
                {
                    map.put(i.next(), null);
                }
                for (Iterator i = map.keySet().iterator(); i.hasNext();)
                {
                    Integer g = (Integer)i.next();
                    int r[] = (int[])cmap10.get(g);
                    ret.append("    ").append(g).append(",").append(r[0]).append("\n");
                }
            }
            if (cmap31 != null)
            {
                ret.append("    map31\n");
                TreeMap map = new TreeMap();
                for (Iterator i = cmap31.keySet().iterator(); i.hasNext();)
                {
                    map.put(i.next(), null);
                }
                for (Iterator i = map.keySet().iterator(); i.hasNext();)
                {
                    Integer g = (Integer)i.next();
                    int r[] = (int[])cmap31.get(g);
                    String k = Integer.toHexString(g.intValue());
                    k = "0000" + k;
                    k = k.substring(k.length() - 4, k.length());
                    ret.append("    ").append(k).append(",").append(r[0]).append("\n");
                }
            }
    
    	}
    	catch (Exception e)
    	{
    	}
    	return ret.toString();
    }
*/
    public void readGlyphWidths() throws DocumentException, IOException
    {
    	int table_location[];
    	table_location = (int[])tables.get("hmtx");
    	if (table_location == null)
    		throw new DocumentException("Table 'hmtx' does not exist in " + fileName);
    	rf.seek(table_location[0]);
        GlyphWidths = new int[hhea.numberOfHMetrics];
        for (int k = 0; k < hhea.numberOfHMetrics; ++k)
        {
            GlyphWidths[k] = (rf.readUnsignedShort() * 1000) / head.unitsPerEm;
            rf.readUnsignedShort();
        }
    }

    public int getGlyphWidth(int glyph)
    {
        if (glyph >= GlyphWidths.length)
            glyph = GlyphWidths.length - 1;
        return GlyphWidths[glyph];
    }
    
    void readCMaps() throws DocumentException, IOException
    {
    	int table_location[];
    	table_location = (int[])tables.get("cmap");
    	if (table_location == null)
    		throw new DocumentException("Table 'cmap' does not exist in " + fileName);
    	rf.seek(table_location[0]);
        rf.skipBytes(2);
        int num_tables = rf.readUnsignedShort();
        fontSpecific = false;
        int map10 = 0;
        int map31 = 0;
        for (int k = 0; k < num_tables; ++k)
        {
            int platId = rf.readUnsignedShort();
            int platSpecId = rf.readUnsignedShort();
            int offset = rf.readInt();
            if (platId == 3 && platSpecId == 0)
            {
                fontSpecific = true;
            }
            else if (platId == 3 && platSpecId == 1)
            {
                map31 = offset;
            }
            if (platId == 1 && platSpecId == 0)
            {
                map10 = offset;
            }
        }
        if (map10 > 0)
        {
            rf.seek(table_location[0] + map10);
            int format = rf.readUnsignedShort();
            switch (format)
            {
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
        if (map31 > 0)
        {
            rf.seek(table_location[0] + map31);
            int format = rf.readUnsignedShort();
            if (format == 4)
            {
                cmap31 = readFormat4();
            }
        }
    }
    
    HashMap readFormat0() throws IOException
    {
        HashMap h = new HashMap();
        rf.skipBytes(4);
        for (int k = 0; k < 256; ++k)
        {
            int r[] = new int[2];
            r[0] = rf.readUnsignedByte();
            r[1] = getGlyphWidth(r[0]);
            h.put(new Integer(k), r);
        }
        return h;
    }

    HashMap readFormat4() throws IOException
    {
        HashMap h = new HashMap();
        int table_lenght = rf.readUnsignedShort();
        rf.skipBytes(2);
        int segCount = rf.readUnsignedShort() / 2;
        rf.skipBytes(6);
        int endCount[] = new int[segCount];
        for (int k = 0; k < segCount; ++k)
        {
            endCount[k] = rf.readUnsignedShort();
        }
        rf.skipBytes(2);
        int startCount[] = new int[segCount];
        for (int k = 0; k < segCount; ++k)
        {
            startCount[k] = rf.readUnsignedShort();
        }
        int idDelta[] = new int[segCount];
        for (int k = 0; k < segCount; ++k)
        {
            idDelta[k] = rf.readUnsignedShort();
        }
        int idRO[] = new int[segCount];
        for (int k = 0; k < segCount; ++k)
        {
            idRO[k] = rf.readUnsignedShort();
        }
        int glyphId[] = new int[table_lenght / 2 - 8 - segCount * 4];
        for (int k = 0; k < glyphId.length; ++k)
        {
            glyphId[k] = rf.readUnsignedShort();
        }
        for (int k = 0; k < segCount; ++k)
        {
            int glyph;
            for (int j = startCount[k]; j <= endCount[k] && j != 0xFFFF; ++j)
            {
                if (idRO[k] == 0)
                {
                    glyph = (j + idDelta[k]) & 0xFFFF;
                }
                else
                {
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

    HashMap readFormat6() throws IOException
    {
        HashMap h = new HashMap();
        rf.skipBytes(4);
        int start_code = rf.readUnsignedShort();
        int code_count = rf.readUnsignedShort();
        for (int k = 0; k < code_count; ++k)
        {
            int r[] = new int[2];
            r[0] = rf.readUnsignedShort();
            r[1] = getGlyphWidth(r[0]);
            h.put(new Integer(k + start_code), r);
        }
        return h;
    }
    
    void readKerning() throws IOException
    {
    	int table_location[];
    	table_location = (int[])tables.get("kern");
    	if (table_location == null)
    		return;
    	rf.seek(table_location[0] + 2);
        int nTables = rf.readUnsignedShort();
        kerning = new HashMap();
        int checkpoint = table_location[0] + 4;
        int length = 0;
        for (int k = 0; k < nTables; ++k)
        {
            checkpoint += length;
            rf.seek(checkpoint);
            rf.skipBytes(2);
            length = rf.readUnsignedShort();
            int coverage = rf.readUnsignedShort();
            if ((coverage & 0xfff7) == 0x0001)
            {
                int nPairs = rf.readUnsignedShort();
                rf.skipBytes(6);
                for (int j = 0; j < nPairs; ++j)
                {
                    Integer pair = new Integer(rf.readInt());
                    Integer value = new Integer(((int)rf.readShort() * 1000) / head.unitsPerEm);
                    kerning.put(pair, value);
                }
            }
        }
    }

    public int getKerning(char char1, char char2)
    {
        Integer v = (Integer)kerning.get(new Integer((((int)char1) << 16) + ((int)char2)));
        if (v == null)
            return 0;
        else
            return v.intValue();
    }
    
    protected int getRawWidth(int c, String name)
    {
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

    private PdfStream getFontStream() throws DocumentException
    {
        if (!embedded)
            return null;
        InputStream is = null;
        try {
            File file = new File(fileName);
            int fileLength = (int)file.length();
            byte st[] = new byte[fileLength];
            is = new FileInputStream(file);
            int lengths[] = new int[]{fileLength};
            int bytePtr = 0;
            int size = fileLength;
            while (size != 0) {
                int got = is.read(st, bytePtr, size);
                if (got < 0)
                    throw new DocumentException("Premature end in " + file.getName());
                bytePtr += got;
                size -= got;
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
        PdfDictionary dic = new PdfDictionary(new PdfName("FontDescriptor"));
        dic.put(new PdfName("Ascent"), new PdfNumber((int)hhea.Ascender * 1000 / head.unitsPerEm));
        dic.put(new PdfName("CapHeight"), new PdfNumber(800));
        dic.put(new PdfName("Descent"), new PdfNumber((int)hhea.Descender * 1000 / head.unitsPerEm));
        dic.put(new PdfName("FontBBox"), new PdfRectangle(
            (int)head.xMin * 1000 / head.unitsPerEm,
            (int)head.yMin * 1000 / head.unitsPerEm,
            (int)head.xMax * 1000 / head.unitsPerEm,
            (int)head.yMax * 1000 / head.unitsPerEm));
        dic.put(new PdfName("FontName"), new PdfName(fontName));
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

    private PdfDictionary getFontType(PdfIndirectReference fontDescriptor) throws DocumentException
    {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, new PdfName("TrueType"));
        dic.put(PdfName.BASEFONT, new PdfName(fontName));
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
        dic.put(new PdfName("FirstChar"), new PdfNumber(firstChar));
        dic.put(new PdfName("LastChar"), new PdfNumber(255));
        PdfArray wd = new PdfArray();
        for (int k = firstChar; k < 256; ++k) {
            wd.add(new PdfNumber(widths[k]));
        }
        dic.put(new PdfName("Widths"), wd);
        if (fontDescriptor != null)
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

