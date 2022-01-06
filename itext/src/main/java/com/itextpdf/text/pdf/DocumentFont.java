/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.fonts.cmaps.CMapParserEx;
import com.itextpdf.text.pdf.fonts.cmaps.CMapToUnicode;
import com.itextpdf.text.pdf.fonts.cmaps.CidLocationFromByte;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author  psoares
 */
public class DocumentFont extends BaseFont {
    // code, [glyph, width]
    private HashMap<Integer, int[]> metrics = new HashMap<Integer, int[]>();
    private String fontName;
    private PRIndirectReference refFont;
    private PdfDictionary font;
    private IntHashtable uni2byte = new IntHashtable();
    private IntHashtable byte2uni = new IntHashtable();
    private IntHashtable diffmap;
    private float ascender = 800;
    private float capHeight = 700;
    private float descender = -200;
    private float italicAngle = 0;
    private float fontWeight = 0;
    private float llx = -50;
    private float lly = -200;
    private float urx = 100;
    private float ury = 900;
    protected boolean isType0 = false;
    protected int defaultWidth = 1000;
    private IntHashtable hMetrics;
    protected String cjkEncoding;
    protected String uniMap;

    private BaseFont cjkMirror;

    private static final int stdEnc[] = {
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
    DocumentFont(PdfDictionary font) {
        this.refFont = null;
        this.font = font;
        init();
    }
    /** Creates a new instance of DocumentFont */
    DocumentFont(PRIndirectReference refFont) {
        this.refFont = refFont;
        font = (PdfDictionary)PdfReader.getPdfObject(refFont);
        init();
    }
    /** Creates a new instance of DocumentFont */
    DocumentFont(PRIndirectReference refFont, PdfDictionary drEncoding) {
        this.refFont = refFont;
        font = (PdfDictionary)PdfReader.getPdfObject(refFont);
        if (font.get(PdfName.ENCODING) == null
                && drEncoding != null) {
            for (PdfName key : drEncoding.getKeys()) {
                font.put(PdfName.ENCODING, drEncoding.get(key));
            }
        }
        init();
    }

    public PdfDictionary getFontDictionary() {
        return font;
    } 
    
    private void init() {
        encoding = "";
        fontSpecific = false;
        fontType = FONT_TYPE_DOCUMENT;
        PdfName baseFont = font.getAsName(PdfName.BASEFONT);
        fontName = baseFont != null ? PdfName.decodeName(baseFont.toString()) : "Unspecified Font Name";
        PdfName subType = font.getAsName(PdfName.SUBTYPE);
        if (PdfName.TYPE1.equals(subType) || PdfName.TRUETYPE.equals(subType))
            doType1TT();
        else if (PdfName.TYPE3.equals(subType)) {
            // In case of a Type3 font, we just show the characters as is.
            // Note that this doesn't always make sense:
            // Type 3 fonts are user defined fonts where arbitrary characters are mapped to custom glyphs
            // For instance: the character a could be mapped to an image of a dog, the character b to an image of a cat
            // When parsing a document that shows a cat and a dog, you shouldn't expect seeing a cat and a dog. Instead you'll get b and a.
            fillEncoding(null);
            fillDiffMap(font.getAsDict(PdfName.ENCODING), null);
            fillWidths();
        }
        else {
            PdfName encodingName = font.getAsName(PdfName.ENCODING);
            if (encodingName != null){
                String enc = PdfName.decodeName(encodingName.toString());
                String ffontname = CJKFont.GetCompatibleFont(enc);
                if (ffontname != null) {
                    try {
                        cjkMirror = BaseFont.createFont(ffontname, enc, false);
                    }
                    catch (Exception e) {
                        throw new ExceptionConverter(e);
                    }
                    cjkEncoding = enc;
                    uniMap = ((CJKFont)cjkMirror).getUniMap();
                }
                if (PdfName.TYPE0.equals(subType)) {
                    isType0 = true;
                    if (!enc.equals("Identity-H") && cjkMirror != null) {
                        PdfArray df = (PdfArray) PdfReader.getPdfObjectRelease(font.get(PdfName.DESCENDANTFONTS));
                        PdfDictionary cidft = (PdfDictionary) PdfReader.getPdfObjectRelease(df.getPdfObject(0));
                        PdfNumber dwo = (PdfNumber) PdfReader.getPdfObjectRelease(cidft.get(PdfName.DW));
                        if (dwo != null)
                            defaultWidth = dwo.intValue();
                        hMetrics = readWidths((PdfArray) PdfReader.getPdfObjectRelease(cidft.get(PdfName.W)));

                        PdfDictionary fontDesc = (PdfDictionary) PdfReader.getPdfObjectRelease(cidft.get(PdfName.FONTDESCRIPTOR));
                        fillFontDesc(fontDesc);
                    } else {
                        processType0(font);
                    }
                }
            }
        }
    }
    
    private void processType0(PdfDictionary font) {
        try {
            PdfObject toUniObject = PdfReader.getPdfObjectRelease(font.get(PdfName.TOUNICODE));
            PdfArray df = (PdfArray)PdfReader.getPdfObjectRelease(font.get(PdfName.DESCENDANTFONTS));
            PdfDictionary cidft = (PdfDictionary)PdfReader.getPdfObjectRelease(df.getPdfObject(0));
            PdfNumber dwo = (PdfNumber)PdfReader.getPdfObjectRelease(cidft.get(PdfName.DW));
            int dw = 1000;
            if (dwo != null)
                dw = dwo.intValue();
            IntHashtable widths = readWidths((PdfArray)PdfReader.getPdfObjectRelease(cidft.get(PdfName.W)));
            PdfDictionary fontDesc = (PdfDictionary)PdfReader.getPdfObjectRelease(cidft.get(PdfName.FONTDESCRIPTOR));
            fillFontDesc(fontDesc);
            if (toUniObject instanceof PRStream){
                fillMetrics(PdfReader.getStreamBytes((PRStream)toUniObject), widths, dw);
            } else if (new PdfName("Identity-H").equals(toUniObject)) {
                fillMetricsIdentity(widths, dw);
            }
        } catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    private IntHashtable readWidths(PdfArray ws) {
        IntHashtable hh = new IntHashtable();
        if (ws == null)
            return hh;
        for (int k = 0; k < ws.size(); ++k) {
            int c1 = ((PdfNumber)PdfReader.getPdfObjectRelease(ws.getPdfObject(k))).intValue();
            PdfObject obj = PdfReader.getPdfObjectRelease(ws.getPdfObject(++k));
            if (obj.isArray()) {
                PdfArray a2 = (PdfArray)obj;
                for (int j = 0; j < a2.size(); ++j) {
                    int c2 = ((PdfNumber)PdfReader.getPdfObjectRelease(a2.getPdfObject(j))).intValue();
                    hh.put(c1++, c2);
                }
            }
            else {
                int c2 = ((PdfNumber)obj).intValue();
                int w = ((PdfNumber)PdfReader.getPdfObjectRelease(ws.getPdfObject(++k))).intValue();
                for (; c1 <= c2; ++c1)
                    hh.put(c1, w);
            }
        }
        return hh;
    }

    private String decodeString(PdfString ps) {
        if (ps.isHexWriting())
            return PdfEncodings.convertToString(ps.getBytes(), "UnicodeBigUnmarked");
        else
            return ps.toUnicodeString();
    }

    private void fillMetricsIdentity(IntHashtable widths, int dw) {
        for (int i = 0; i < 65536; i++) {
            int w = dw;
            if (widths.containsKey(i))
                w = widths.get(i);
            metrics.put(i, new int[] {i, w});
        }
    }

    private void fillMetrics(byte[] touni, IntHashtable widths, int dw) {
        try {
            PdfContentParser ps = new PdfContentParser(new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(touni))));
            PdfObject ob = null;
            boolean notFound = true;
            int nestLevel = 0;
            int maxExc = 50;
            while ((notFound || nestLevel > 0)) {
                try {
                    ob = ps.readPRObject();
                }
                catch (Exception ex) {
                    if (--maxExc < 0)
                        break;
                    continue;
                }
                if (ob == null)
                    break;
                if (ob.type() == PdfContentParser.COMMAND_TYPE) {
                	if (ob.toString().equals("begin")) {
                		notFound = false;
                		nestLevel++;
                	}
                	else if (ob.toString().equals("end")) {
                		nestLevel--;
                	}
                	else if (ob.toString().equals("beginbfchar")) {
                        while (true) {
                            PdfObject nx = ps.readPRObject();
                            if (nx.toString().equals("endbfchar"))
                                break;
                            String cid = decodeString((PdfString)nx);
                            String uni = decodeString((PdfString)ps.readPRObject());
                            if (uni.length() == 1) {
                                int cidc = cid.charAt(0);
                                int unic = uni.charAt(uni.length() - 1);
                                int w = dw;
                                if (widths.containsKey(cidc))
                                    w = widths.get(cidc);
                                metrics.put(Integer.valueOf(unic), new int[]{cidc, w});
                            }
                        }
                    }
                    else if (ob.toString().equals("beginbfrange")) {
                        while (true) {
                            PdfObject nx = ps.readPRObject();
                            if (nx.toString().equals("endbfrange"))
                                break;
                            String cid1 = decodeString((PdfString)nx);
                            String cid2 = decodeString((PdfString)ps.readPRObject());
                            int cid1c = cid1.charAt(0);
                            int cid2c = cid2.charAt(0);
                            PdfObject ob2 = ps.readPRObject();
                            if (ob2.isString()) {
                                String uni = decodeString((PdfString)ob2);
                                if (uni.length() == 1) {
                                    int unic = uni.charAt(uni.length() - 1);
                                    for (; cid1c <= cid2c; cid1c++, unic++) {
                                        int w = dw;
                                        if (widths.containsKey(cid1c))
                                            w = widths.get(cid1c);
                                        metrics.put(Integer.valueOf(unic), new int[]{cid1c, w});
                                    }
                                }
                            }
                            else {
                                PdfArray a = (PdfArray)ob2;
                                for (int j = 0; j < a.size(); ++j, ++cid1c) {
                                    String uni = decodeString(a.getAsString(j));
                                    if (uni.length() == 1) {
                                        int unic = uni.charAt(uni.length() - 1);
                                        int w = dw;
                                        if (widths.containsKey(cid1c))
                                            w = widths.get(cid1c);
                                        metrics.put(Integer.valueOf(unic), new int[]{cid1c, w});
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    private void doType1TT() {
        CMapToUnicode toUnicode = null;
        PdfObject enc = PdfReader.getPdfObject(font.get(PdfName.ENCODING));
        if (enc == null) {
            PdfName baseFont = font.getAsName(PdfName.BASEFONT);
            if (BuiltinFonts14.containsKey(fontName)
                    && (PdfName.SYMBOL.equals(baseFont) || PdfName.ZAPFDINGBATS.equals(baseFont))) {
                fillEncoding(baseFont);
            } else
                fillEncoding(null);
            try {
                toUnicode = processToUnicode();
                if (toUnicode != null) {
                    Map<Integer, Integer> rm = toUnicode.createReverseMapping();
                    for (Map.Entry<Integer, Integer> kv : rm.entrySet()) {
                        uni2byte.put(kv.getKey().intValue(), kv.getValue().intValue());
                        byte2uni.put(kv.getValue().intValue(), kv.getKey().intValue());
                    }
                }
            }
            catch (Exception ex) {
                throw new ExceptionConverter(ex);
            }
        }
        else {
            if (enc.isName())
                fillEncoding((PdfName)enc);
            else if (enc.isDictionary()) {
                PdfDictionary encDic = (PdfDictionary)enc;
                enc = PdfReader.getPdfObject(encDic.get(PdfName.BASEENCODING));
                if (enc == null)
                    fillEncoding(null);
                else
                    fillEncoding((PdfName)enc);
                fillDiffMap(encDic, toUnicode);
            }
        }

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
            if (diffmap != null) { //widths for diffmap must override existing ones
                e = diffmap.toOrderedKeys();
                for (int k = 0; k < e.length; ++k) {
                    int n = diffmap.get(e[k]);
                    widths[n] = bf.getRawWidth(n, GlyphList.unicodeToName(e[k]));
                }
                diffmap = null;
            }
            ascender = bf.getFontDescriptor(ASCENT, 1000);
            capHeight = bf.getFontDescriptor(CAPHEIGHT, 1000);
            descender = bf.getFontDescriptor(DESCENT, 1000);
            italicAngle = bf.getFontDescriptor(ITALICANGLE, 1000);
            fontWeight = bf.getFontDescriptor(FONT_WEIGHT, 1000);
            llx = bf.getFontDescriptor(BBOXLLX, 1000);
            lly = bf.getFontDescriptor(BBOXLLY, 1000);
            urx = bf.getFontDescriptor(BBOXURX, 1000);
            ury = bf.getFontDescriptor(BBOXURY, 1000);
        }
        fillWidths();
        fillFontDesc(font.getAsDict(PdfName.FONTDESCRIPTOR));
    }

    private void fillWidths() {
        PdfArray newWidths = font.getAsArray(PdfName.WIDTHS);
        PdfNumber first = font.getAsNumber(PdfName.FIRSTCHAR);
        PdfNumber last = font.getAsNumber(PdfName.LASTCHAR);
        if (first != null && last != null && newWidths != null) {
            int f = first.intValue();
            int nSize = f + newWidths.size();
            if (widths.length < nSize) {
                int[] tmp = new int[nSize];
                System.arraycopy(widths, 0, tmp, 0, f);
                widths = tmp;
            }
            for (int k = 0; k < newWidths.size(); ++k) {
                widths[f + k] = newWidths.getAsNumber(k).intValue();
            }
        }
    }

    private void fillDiffMap(PdfDictionary encDic, CMapToUnicode toUnicode) {
        PdfArray diffs = encDic.getAsArray(PdfName.DIFFERENCES);
        if (diffs != null) {
            diffmap = new IntHashtable();
            int currentNumber = 0;
            for (int k = 0; k < diffs.size(); ++k) {
                PdfObject obj = diffs.getPdfObject(k);
                if (obj.isNumber())
                    currentNumber = ((PdfNumber)obj).intValue();
                else {
                    int c[] = GlyphList.nameToUnicode(PdfName.decodeName(((PdfName)obj).toString()));
                    if (c != null && c.length > 0) {
                        uni2byte.put(c[0], currentNumber);
                        byte2uni.put(currentNumber, c[0]);
                        diffmap.put(c[0], currentNumber);
                    }
                    else {
                        if (toUnicode == null) {
                            toUnicode = processToUnicode();
                            if (toUnicode == null) {
                                toUnicode = new CMapToUnicode();
                            }
                        }
                        final String unicode = toUnicode.lookup(new byte[]{(byte) currentNumber}, 0, 1);
                        if ((unicode != null) && (unicode.length() == 1)) {
                            this.uni2byte.put(unicode.charAt(0), currentNumber);
                            this.byte2uni.put(currentNumber, unicode.charAt(0));
                            this.diffmap.put(unicode.charAt(0), currentNumber);
                        }
                    }
                    ++currentNumber;
                }
            }
        }
    }
    
    private CMapToUnicode processToUnicode() {
        CMapToUnicode cmapRet = null;
        PdfObject toUni = PdfReader.getPdfObjectRelease(this.font.get(PdfName.TOUNICODE));
        if (toUni instanceof PRStream) {
            try {
                byte[] touni = PdfReader.getStreamBytes((PRStream)toUni);
                CidLocationFromByte lb = new CidLocationFromByte(touni);
                cmapRet = new CMapToUnicode();
                CMapParserEx.parseCid("", cmapRet, lb);
            } catch (Exception e) {
                cmapRet = null;
            }
        }
        return cmapRet;
    }

    private void fillFontDesc(PdfDictionary fontDesc) {
        if (fontDesc == null)
            return;
        PdfNumber v = fontDesc.getAsNumber(PdfName.ASCENT);
        if (v != null)
            ascender = v.floatValue();
        v = fontDesc.getAsNumber(PdfName.CAPHEIGHT);
        if (v != null)
            capHeight = v.floatValue();
        v = fontDesc.getAsNumber(PdfName.DESCENT);
        if (v != null)
            descender = v.floatValue();
        v = fontDesc.getAsNumber(PdfName.ITALICANGLE);
        if (v != null)
            italicAngle = v.floatValue();
        v = fontDesc.getAsNumber(PdfName.FONTWEIGHT);
        if (v != null) {
            fontWeight = v.floatValue();
        }
        PdfArray bbox = fontDesc.getAsArray(PdfName.FONTBBOX);
        if (bbox != null) {
            llx = bbox.getAsNumber(0).floatValue();
            lly = bbox.getAsNumber(1).floatValue();
            urx = bbox.getAsNumber(2).floatValue();
            ury = bbox.getAsNumber(3).floatValue();
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
        float maxAscent = Math.max(ury, ascender);
        float minDescent = Math.min(lly, descender);
        ascender = maxAscent * 1000 / (maxAscent - minDescent);
        descender = minDescent * 1000 / (maxAscent - minDescent);
    }

    private void fillEncoding(PdfName encoding) {
        if (encoding == null && isSymbolic()) {
            for (int k = 0; k < 256; ++k) {
                uni2byte.put(k, k);
                byte2uni.put(k, k);
            }
        } else if (PdfName.MAC_ROMAN_ENCODING.equals(encoding) || PdfName.WIN_ANSI_ENCODING.equals(encoding)
                    || PdfName.SYMBOL.equals(encoding) || PdfName.ZAPFDINGBATS.equals(encoding)) {
            byte b[] = new byte[256];
            for (int k = 0; k < 256; ++k)
                b[k] = (byte)k;
            String enc = WINANSI;
            if (PdfName.MAC_ROMAN_ENCODING.equals(encoding))
                enc = MACROMAN;
            else if (PdfName.SYMBOL.equals(encoding))
                enc = SYMBOL;
            else if (PdfName.ZAPFDINGBATS.equals(encoding))
                enc = ZAPFDINGBATS;
            String cv = PdfEncodings.convertToString(b, enc);
            char arr[] = cv.toCharArray();
            for (int k = 0; k < 256; ++k) {
                uni2byte.put(arr[k], k);
                byte2uni.put(k, arr[k]);
            }
            this.encoding = enc;
        }
        else {
            for (int k = 0; k < 256; ++k) {
                uni2byte.put(stdEnc[k], k);
                byte2uni.put(k, stdEnc[k]);
            }
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
    @Override
    public String[][] getFamilyFontName() {
        return getFullFontName();
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
    @Override
    public float getFontDescriptor(int key, float fontSize) {
        if (cjkMirror != null)
            return cjkMirror.getFontDescriptor(key, fontSize);
        switch (key) {
            case AWT_ASCENT:
            case ASCENT:
                return ascender * fontSize / 1000;
            case CAPHEIGHT:
                return capHeight * fontSize / 1000;
            case AWT_DESCENT:
            case DESCENT:
                return descender * fontSize / 1000;
            case ITALICANGLE:
                return italicAngle;
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
            case FONT_WEIGHT:
                return fontWeight * fontSize / 1000;
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
    @Override
    public String[][] getFullFontName() {
        return new String[][]{{"", "", "", fontName}};
    }

    /** Gets all the entries of the names-table. If it is a True Type font
     * each array element will have {Name ID, Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"4", "", "", "",
     * font name}.
     * @return the full name of the font
     * @since 2.0.8
     */
    @Override
    public String[][] getAllNameEntries() {
        return new String[][]{{"4", "", "", "", fontName}};
    }

    /** Gets the kerning between two Unicode chars.
     * @param char1 the first char
     * @param char2 the second char
     * @return the kerning to be applied
     *
     */
    @Override
    public int getKerning(int char1, int char2) {
        return 0;
    }

    /** Gets the postscript font name.
     * @return the postscript font name
     *
     */
    @Override
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
    @Override
    int getRawWidth(int c, String name) {
        return 0;
    }

    /** Checks if the font has any kerning pairs.
     * @return <CODE>true</CODE> if the font has any kerning pairs
     *
     */
    @Override
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
    @Override
    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws DocumentException, IOException {
    }

    /**
     * Always returns null.
     * @return	null
     * @since	2.1.3
     */
    @Override
    public PdfStream getFullFontStream() {
    	return null;
    }

    /**
     * Gets the width of a <CODE>char</CODE> in normalized 1000 units.
     * @param char1 the unicode <CODE>char</CODE> to get the width of
     * @return the width in normalized 1000 units
     */
    @Override
    public int getWidth(int char1) {
        if (isType0) {
            if(hMetrics != null && cjkMirror != null && !cjkMirror.isVertical()) {
                int c = cjkMirror.getCidCode(char1);
                int v = hMetrics.get(c);
                if (v > 0)
                    return v;
                else
                    return defaultWidth;
            } else {
                int[] ws = metrics.get(Integer.valueOf(char1));
                if (ws != null)
                    return ws[1];
                else
                    return 0;
            }
        }
        if (cjkMirror != null)
            return cjkMirror.getWidth(char1);
        return super.getWidth(char1);
    }

    @Override
    public int getWidth(String text) {
        if (isType0) {
            int total = 0;
            if(hMetrics != null && cjkMirror != null && !cjkMirror.isVertical()) {
                if (((CJKFont)cjkMirror).isIdentity()) {
                    for (int k = 0; k < text.length(); ++k) {
                        total += getWidth(text.charAt(k));
                    }
                }
                else {
                    for (int k = 0; k < text.length(); ++k) {
                        int val;
                        if (Utilities.isSurrogatePair(text, k)) {
                            val = Utilities.convertToUtf32(text, k);
                            k++;
                        }
                        else {
                            val = text.charAt(k);
                        }
                        total += getWidth(val);
                    }
                }
            } else {
                char[] chars = text.toCharArray();
                int len = chars.length;
                for (int k = 0; k < len; ++k) {
                    int[] ws = metrics.get(Integer.valueOf(chars[k]));
                    if (ws != null)
                        total += ws[1];
                }
            }
            return total;
        }
        if (cjkMirror != null)
            return cjkMirror.getWidth(text);
        return super.getWidth(text);
    }

    @Override
    public byte[] convertToBytes(String text) {
        if (cjkMirror != null)
            return cjkMirror.convertToBytes(text);
        else if (isType0) {
            char[] chars = text.toCharArray();
            int len = chars.length;
            byte[] b = new byte[len * 2];
            int bptr = 0;
            for (int k = 0; k < len; ++k) {
                int[] ws = metrics.get(Integer.valueOf(chars[k]));
                if (ws != null) {
                    int g = ws[0];
                    b[bptr++] = (byte)(g / 256);
                    b[bptr++] = (byte)g;
                }
            }
            if (bptr == b.length)
                return b;
            else {
                byte[] nb = new byte[bptr];
                System.arraycopy(b, 0, nb, 0, bptr);
                return nb;
            }
        }
        else {
            char cc[] = text.toCharArray();
            byte b[] = new byte[cc.length];
            int ptr = 0;
            for (int k = 0; k < cc.length; ++k) {
                if (uni2byte.containsKey(cc[k]))
                    b[ptr++] = (byte)uni2byte.get(cc[k]);
            }
            if (ptr == b.length)
                return b;
            else {
                byte[] b2 = new byte[ptr];
                System.arraycopy(b, 0, b2, 0, ptr);
                return b2;
            }
        }
    }

    @Override
    byte[] convertToBytes(int char1) {
        if (cjkMirror != null)
            return cjkMirror.convertToBytes(char1);
        else if (isType0) {
            int[] ws = metrics.get(Integer.valueOf(char1));
            if (ws != null) {
                int g = ws[0];
                return new byte[]{(byte)(g / 256), (byte)g};
            }
            else
                return new byte[0];
        }
        else {
            if (uni2byte.containsKey(char1))
                return new byte[]{(byte)uni2byte.get(char1)};
            else
                return new byte[0];
        }
    }

    PdfIndirectReference getIndirectReference() {
        if (refFont == null)
            throw new IllegalArgumentException("Font reuse not allowed with direct font objects.");
        return refFont;
    }

    @Override
    public boolean charExists(int c) {
        if (cjkMirror != null)
            return cjkMirror.charExists(c);
        else if (isType0) {
            return metrics.containsKey(Integer.valueOf(c));
        }
        else
            return super.charExists(c);
    }

    @Override
    public double[] getFontMatrix() {
        if (font.getAsArray(PdfName.FONTMATRIX) != null)
            return font.getAsArray(PdfName.FONTMATRIX).asDoubleArray();
        else
            return DEFAULT_FONT_MATRIX;

    }

    /**
     * Sets the font name that will appear in the pdf font dictionary.
     * It does nothing in this case as the font is already in the document.
     * @param name the new font name
     */
    @Override
    public void setPostscriptFontName(String name) {
    }

    @Override
    public boolean setKerning(int char1, int char2, int kern) {
        return false;
    }

    @Override
    public int[] getCharBBox(int c) {
        return null;
    }

    @Override
    protected int[] getRawCharBBox(int c, String name) {
        return null;
    }

    @Override
    public boolean isVertical() {
        if (cjkMirror != null)
            return cjkMirror.isVertical();
        else
            return super.isVertical();
    }

    /**
     * Exposes the unicode - > CID map that is constructed from the font's encoding
     * @return the unicode to CID map
     * @since 2.1.7
     */
    IntHashtable getUni2Byte(){
        return uni2byte;
    }

    /**
     * Exposes the CID - > unicode map that is constructed from the font's encoding
     * @return the CID to unicode map
     * @since 5.4.0
     */
    IntHashtable getByte2Uni(){
        return byte2uni;
    }

    /**
     * Gets the difference map
     * @return the difference map
     * @since 5.0.5
     */
    IntHashtable getDiffmap() {
        return diffmap;
    }

    boolean isSymbolic() {
        PdfDictionary fontDescriptor = font.getAsDict(PdfName.FONTDESCRIPTOR);
        if (fontDescriptor == null)
            return false;
        PdfNumber flags = fontDescriptor.getAsNumber(PdfName.FLAGS);
        if (flags == null)
            return false;
        return (flags.intValue() & 0x04) != 0;
    }
}
