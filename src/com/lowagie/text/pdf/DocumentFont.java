package com.lowagie.text.pdf;

import java.io.IOException;
import java.util.ArrayList;

import com.lowagie.text.DocumentException;
import com.lowagie.text.ExceptionConverter;

/**
 *
 * @author  psoares
 */
public class DocumentFont extends BaseFont {
    
    String fontName;
    PRIndirectReference refFont;
    PdfDictionary font;
    IntHashtable uni2byte = new IntHashtable();
    float Ascender = 800;
    float CapHeight = 700;
    float Descender = -200;
    float ItalicAngle = 0;
    float llx = -50;
    float lly = -200;
    float urx = 100;
    float ury = 900;
    
    BaseFont cjkMirror;
    
    String cjkNames[] = {"HeiseiMin-W3", "HeiseiKakuGo-W5", "STSong-Light", "MHei-Medium",
        "MSung-Light", "HYGoThic-Medium", "HYSMyeongJo-Medium", "MSungStd-Light", "STSongStd-Light",
        "HYSMyeongJoStd-Medium", "KozMinPro-Regular"};
        
    String cjkEncs[] = {"UniJIS-UCS2-H", "UniJIS-UCS2-H", "UniGB-UCS2-H", "UniCNS-UCS2-H",
        "UniCNS-UCS2-H", "UniKS-UCS2-H", "UniKS-UCS2-H", "UniCNS-UCS2-H", "UniGB-UCS2-H",
        "UniKS-UCS2-H", "UniJIS-UCS2-H"};
        
    static final int stdEnc[] = {
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        32,33,34,35,36,37,38,8217,40,41,42,43,44,45,46,47,
        48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,
        64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,
        80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,
        8216,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,
        112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,161,162,163,8260,165,402,167,164,39,8220,171,8249,8250,64257,64258,
        0,8211,8224,8225,183,0,182,8226,8218,8222,8221,187,8230,8240,0,191,
        0,96,180,710,732,175,728,729,168,0,730,184,0,733,731,711,
        8212,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
        0,198,0,170,0,0,0,0,321,216,338,186,0,0,0,0,
        0,230,0,0,0,305,0,0,322,248,339,223,0,0,0,0};

    /** Creates a new instance of DocumentFont */
    DocumentFont(PRIndirectReference refFont) {
        encoding = "";
        fontSpecific = false;
        this.refFont = refFont;
        fontType = FONT_TYPE_DOCUMENT;
        font = (PdfDictionary)PdfReader.getPdfObject(refFont);
        fontName = PdfName.decodeName(((PdfName)PdfReader.getPdfObject(font.get(PdfName.BASEFONT))).toString());
        PdfName subType = (PdfName)PdfReader.getPdfObject(font.get(PdfName.SUBTYPE));
        if (PdfName.TYPE1.equals(subType) || PdfName.TRUETYPE.equals(subType))
            doType1TT();
        else {
            for (int k = 0; k < cjkNames.length; ++k) {
                if (fontName.startsWith(cjkNames[k])) {
                    fontName = cjkNames[k];
                    try {
                        cjkMirror = BaseFont.createFont(fontName, cjkEncs[k], false);
                    }
                    catch (Exception e) {
                        throw new ExceptionConverter(e);
                    }
                    return;
                }
            }
        }
    }
    
    public void doType1TT() {
        PdfObject enc = PdfReader.getPdfObject(font.get(PdfName.ENCODING));
        if (enc == null)
            fillEncoding(null);
        else {
            if (enc.type() == PdfObject.NAME)
                fillEncoding((PdfName)enc);
            else {
                PdfDictionary encDic = (PdfDictionary)enc;
                enc = PdfReader.getPdfObject(encDic.get(PdfName.BASEENCODING));
                if (enc == null)
                    fillEncoding(null);
                else
                    fillEncoding((PdfName)enc);
                PdfArray diffs = (PdfArray)PdfReader.getPdfObject(encDic.get(PdfName.DIFFERENCES));
                if (diffs != null) {
                    ArrayList dif = diffs.getArrayList();
                    int currentNumber = 0;
                    for (int k = 0; k < dif.size(); ++k) {
                        PdfObject obj = (PdfObject)dif.get(k);
                        if (obj.type() == PdfObject.NUMBER)
                            currentNumber = ((PdfNumber)obj).intValue();
                        else {
                            int c[] = GlyphList.nameToUnicode(PdfName.decodeName(((PdfName)obj).toString()));
                            if (c != null && c.length > 0)
                                uni2byte.put(c[0], currentNumber);
                            ++currentNumber;
                        }
                    }
                }
            }
        }
        PdfArray newWidths = (PdfArray)PdfReader.getPdfObject(font.get(PdfName.WIDTHS));
        PdfNumber first = (PdfNumber)PdfReader.getPdfObject(font.get(PdfName.FIRSTCHAR));
        PdfNumber last = (PdfNumber)PdfReader.getPdfObject(font.get(PdfName.LASTCHAR));
        if (BuiltinFonts14.containsKey(fontName)) {
            BaseFont bf;
            try {
                bf = BaseFont.createFont(fontName, WINANSI, false);
            }
            catch (Exception e) {
                throw new ExceptionConverter(e);
            }
            int e[] = uni2byte.toOrderedKeys();
            for (int k = 0; k < e.length; ++k) {
                int n = uni2byte.get(e[k]);
                widths[n] = bf.getRawWidth(n, GlyphList.unicodeToName(e[k]));
            }
            Ascender = bf.getFontDescriptor(ASCENT, 1000);
            CapHeight = bf.getFontDescriptor(CAPHEIGHT, 1000);
            Descender = bf.getFontDescriptor(DESCENT, 1000);
            ItalicAngle = bf.getFontDescriptor(ITALICANGLE, 1000);
            llx = bf.getFontDescriptor(BBOXLLX, 1000);
            lly = bf.getFontDescriptor(BBOXLLY, 1000);
            urx = bf.getFontDescriptor(BBOXURX, 1000);
            ury = bf.getFontDescriptor(BBOXURY, 1000);
        }
        if (first != null && last != null && newWidths != null) {
            int f = ((PdfNumber)first).intValue();
            ArrayList ar = ((PdfArray)newWidths).getArrayList();
            for (int k = 0; k < ar.size(); ++k) {
                widths[f + k] = ((PdfNumber)ar.get(k)).intValue();
            }
        }
        fillFontDesc();
    }
    
    void fillFontDesc() {
        PdfDictionary fontDesc = (PdfDictionary)PdfReader.getPdfObject(font.get(PdfName.FONTDESCRIPTOR));
        if (fontDesc == null)
            return;
        PdfNumber v = (PdfNumber)PdfReader.getPdfObject(fontDesc.get(PdfName.ASCENT));
        if (v != null)
            Ascender = v.floatValue();
        v = (PdfNumber)PdfReader.getPdfObject(fontDesc.get(PdfName.CAPHEIGHT));
        if (v != null)
            CapHeight = v.floatValue();
        v = (PdfNumber)PdfReader.getPdfObject(fontDesc.get(PdfName.DESCENT));
        if (v != null)
            Descender = v.floatValue();
        v = (PdfNumber)PdfReader.getPdfObject(fontDesc.get(PdfName.ITALICANGLE));
        if (v != null)
            ItalicAngle = v.floatValue();
        PdfArray bbox = (PdfArray)PdfReader.getPdfObject(fontDesc.get(PdfName.FONTBBOX));
        if (bbox != null) {
            ArrayList ar = bbox.getArrayList();
            llx = ((PdfNumber)ar.get(0)).floatValue();
            lly = ((PdfNumber)ar.get(1)).floatValue();
            urx = ((PdfNumber)ar.get(2)).floatValue();
            ury = ((PdfNumber)ar.get(3)).floatValue();
            if (llx > urx) {
                float t = llx;
                llx = urx;
                urx = t;
            }
            if (lly > ury) {
                float t = lly;
                lly = ury;
                ury = t;
            }
        }
    }
    
    void fillEncoding(PdfName encoding) {
        if (PdfName.MAC_ROMAN_ENCODING.equals(encoding) || PdfName.WIN_ANSI_ENCODING.equals(encoding)) {
            byte b[] = new byte[256];
            for (int k = 0; k < 256; ++k)
                b[k] = (byte)k;
            String enc = WINANSI;
            if (PdfName.MAC_ROMAN_ENCODING.equals(encoding))
                enc = MACROMAN;
            String cv = PdfEncodings.convertToString(b, enc);
            char arr[] = cv.toCharArray();
            for (int k = 0; k < 256; ++k)
                uni2byte.put(arr[k], k);
        }
        else {
            for (int k = 0; k < 256; ++k)
                uni2byte.put(stdEnc[k], k);
        }
    }
    
    /** Gets the family name of the font. If it is a True Type font
     * each array element will have {Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"", "", "",
     * font name}.
     * @return the family name of the font
     *
     */
    public String[][] getFamilyFontName() {
        return null;
    }
    
    /** Gets the font parameter identified by <CODE>key</CODE>. Valid values
     * for <CODE>key</CODE> are <CODE>ASCENT</CODE>, <CODE>CAPHEIGHT</CODE>, <CODE>DESCENT</CODE>,
     * <CODE>ITALICANGLE</CODE>, <CODE>BBOXLLX</CODE>, <CODE>BBOXLLY</CODE>, <CODE>BBOXURX</CODE>
     * and <CODE>BBOXURY</CODE>.
     * @param key the parameter to be extracted
     * @param fontSize the font size in points
     * @return the parameter in points
     *
     */
    public float getFontDescriptor(int key, float fontSize) {
        if (cjkMirror != null)
            return cjkMirror.getFontDescriptor(key, fontSize);
        switch (key) {
            case AWT_ASCENT:
            case ASCENT:
                return Ascender * fontSize / 1000;
            case CAPHEIGHT:
                return CapHeight * fontSize / 1000;
            case AWT_DESCENT:
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
            case AWT_LEADING:
                return 0;
            case AWT_MAXADVANCE:
                return (urx - llx) * fontSize / 1000;
        }
        return 0;
    }
    
    /** Gets the full name of the font. If it is a True Type font
     * each array element will have {Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"", "", "",
     * font name}.
     * @return the full name of the font
     *
     */
    public String[][] getFullFontName() {
        return null;
    }
    
    /** Gets the kerning between two Unicode chars.
     * @param char1 the first char
     * @param char2 the second char
     * @return the kerning to be applied
     *
     */
    public int getKerning(char char1, char char2) {
        return 0;
    }
    
    /** Gets the postscript font name.
     * @return the postscript font name
     *
     */
    public String getPostscriptFontName() {
        return fontName;
    }
    
    /** Gets the width from the font according to the Unicode char <CODE>c</CODE>
     * or the <CODE>name</CODE>. If the <CODE>name</CODE> is null it's a symbolic font.
     * @param c the unicode char
     * @param name the glyph name
     * @return the width of the char
     *
     */
    int getRawWidth(int c, String name) {
        return 0;
    }
    
    /** Checks if the font has any kerning pairs.
     * @return <CODE>true</CODE> if the font has any kerning pairs
     *
     */
    public boolean hasKernPairs() {
        return false;
    }
    
    /** Outputs to the writer the font dictionaries and streams.
     * @param writer the writer for this document
     * @param ref the font indirect reference
     * @param params several parameters that depend on the font type
     * @throws IOException on error
     * @throws DocumentException error in generating the object
     *
     */
    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
    }

    public int getWidth(String text) {
        if (cjkMirror != null)
            return cjkMirror.getWidth(text);
        else
            return super.getWidth(text);
    }
    
    byte[] convertToBytes(String text) {
        if (cjkMirror != null)
            return PdfEncodings.convertToBytes(text, CJKFont.CJK_ENCODING);
        else {
            char cc[] = text.toCharArray();
            byte b[] = new byte[cc.length];
            for (int k = 0; k < cc.length; ++k)
                b[k] = (byte)uni2byte.get(cc[k]);
            return b;
        }
    }
    
    PdfIndirectReference getIndirectReference() {
        return refFont;
    }
}
