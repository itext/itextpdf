/*
 * PdfFontDescriptor.java
 *
 * Created on December 30, 2000, 11:20 AM
 */
package com.lowagie.text.pdf;
/**
 *
 * @author  Administrator
 * @version 
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
