/*
 * $Id: CJKFont.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.StreamUtil;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCache;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCidByte;
import com.itextpdf.text.pdf.fonts.cmaps.CMapCidUni;
import com.itextpdf.text.pdf.fonts.cmaps.CMapUniCid;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Creates a CJK font compatible with the fonts in the Adobe Asian font Pack.
 *
 * @author  Paulo Soares
 */

class CJKFont extends BaseFont {
    /** The encoding used in the PDF document for CJK fonts
     */
    static final String CJK_ENCODING = "UnicodeBigUnmarked";
    private static final int FIRST = 0;
    private static final int BRACKET = 1;
    private static final int SERIAL = 2;
    private static final int V1Y = 880;

    static Properties cjkFonts = new Properties();
    static Properties cjkEncodings = new Properties();
    private static final HashMap<String, HashMap<String, Object>> allFonts = new HashMap<String, HashMap<String, Object>>();
    private static boolean propertiesLoaded = false;
    
    /** The path to the font resources. */
    public static final String RESOURCE_PATH_CMAP = RESOURCE_PATH + "cmaps/";
    private static final HashMap<String,Set<String>> registryNames = new HashMap<String,Set<String>>();
    private CMapCidByte cidByte;
    private CMapUniCid uniCid;
    private CMapCidUni cidUni;
    private String uniMap;

    /** The font name */
    private String fontName;
    /** The style modifier */
    private String style = "";
    /** The CMap name associated with this font */
    private String CMap;

    private boolean cidDirect = false;

    //private char[] translationMap;
    private IntHashtable vMetrics;
    private IntHashtable hMetrics;
    private HashMap<String, Object> fontDesc;

    private static void loadProperties() {
        if (propertiesLoaded)
            return;
        synchronized (allFonts) {
            if (propertiesLoaded)
                return;
            try {
                loadRegistry();
                for (String font : registryNames.get("fonts")) {
                    allFonts.put(font, readFontProperties(font));          
                }
            }
            catch (Exception e) {
            }
            propertiesLoaded = true;
        }
    }

    private static void loadRegistry() throws IOException {
        InputStream is = StreamUtil.getResourceStream(RESOURCE_PATH_CMAP + "cjk_registry.properties");
        Properties p = new Properties();
        p.load(is);
        is.close();
        for (Object key : p.keySet()) {
            String value = p.getProperty((String)key);
            String[] sp = value.split(" ");
            Set<String> hs = new HashSet<String>();
            for (String s : sp) {
                if (s.length() > 0)
                    hs.add(s);
            }
            registryNames.put((String)key, hs);
        }
        
    }
    /** Creates a CJK font.
     * @param fontName the name of the font
     * @param enc the encoding of the font
     * @param emb always <CODE>false</CODE>. CJK font and not embedded
     * @throws DocumentException on error
     */
    CJKFont(String fontName, String enc, boolean emb) throws DocumentException {
        loadProperties();
        fontType = FONT_TYPE_CJK;
        String nameBase = getBaseName(fontName);
        if (!isCJKFont(nameBase, enc))
            throw new DocumentException(MessageLocalization.getComposedMessage("font.1.with.2.encoding.is.not.a.cjk.font", fontName, enc));
        if (nameBase.length() < fontName.length()) {
            style = fontName.substring(nameBase.length());
            fontName = nameBase;
        }
        this.fontName = fontName;
        encoding = CJK_ENCODING;
        vertical = enc.endsWith("V");
        CMap = enc;
        if (enc.equals(IDENTITY_H) || enc.equals(IDENTITY_V))
            cidDirect = true;
        loadCMaps();
    }

    String getUniMap() {
        return uniMap;
    }
    
    private void loadCMaps() throws DocumentException {
        try {
            fontDesc = allFonts.get(fontName);
            hMetrics = (IntHashtable)fontDesc.get("W");
            vMetrics = (IntHashtable)fontDesc.get("W2");
            String registry = (String)fontDesc.get("Registry");
            uniMap = "";
            for (String name : registryNames.get(registry + "_Uni")) {
                uniMap = name;
                if (name.endsWith("V") && vertical)
                    break;
                if (!name.endsWith("V") && !vertical)
                    break;
            }
            if (cidDirect) {
                cidUni = CMapCache.getCachedCMapCidUni(uniMap);
            }
            else {
                uniCid = CMapCache.getCachedCMapUniCid(uniMap);
                cidByte = CMapCache.getCachedCMapCidByte(CMap);
            }
        }
        catch (Exception ex) {
            throw new DocumentException(ex);
        }
    }
    
    /**
     * Returns a font compatible with a CJK encoding or null if not found.
     * @param enc
     * @return 
     */
    public static String GetCompatibleFont(String enc) {
        loadProperties();
        String registry = null;
        for (Entry<String,Set<String>> e : registryNames.entrySet()) {
            if (e.getValue().contains(enc)) {
                registry = e.getKey();
                break;
            }
        }
        if (registry == null)
            return null;
        for (Entry<String, HashMap<String, Object>> e : allFonts.entrySet()) {
            if (registry.equals(e.getValue().get("Registry")))
                return e.getKey();
        }
        return null;
    }
    
    /** Checks if its a valid CJK font.
     * @param fontName the font name
     * @param enc the encoding
     * @return <CODE>true</CODE> if it is CJK font
     */
    public static boolean isCJKFont(String fontName, String enc) {
        loadProperties();
        if (!registryNames.containsKey("fonts"))
            return false;
        if (!registryNames.get("fonts").contains(fontName))
            return false;
        if (enc.equals(IDENTITY_H) || enc.equals(IDENTITY_V))
            return true;
        String registry = (String)allFonts.get(fontName).get("Registry");
        Set<String> encodings = registryNames.get(registry);
        return encodings != null && encodings.contains(enc);
    }

    /**
     * Gets the width of a <CODE>char</CODE> in normalized 1000 units.
     * @param char1 the unicode <CODE>char</CODE> to get the width of
     * @return the width in normalized 1000 units
     */
    @Override
    public int getWidth(int char1) {
        int c = char1;
        if (!cidDirect)
            c = uniCid.lookup(char1);
        int v;
        if (vertical)
            v = vMetrics.get(c);
        else
            v = hMetrics.get(c);
        if (v > 0)
            return v;
        else
            return 1000;
    }

    @Override
    public int getWidth(String text) {
        int total = 0;
        if (cidDirect) {
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
        return total;
    }

    @Override
    int getRawWidth(int c, String name) {
        return 0;
    }

    @Override
    public int getKerning(int char1, int char2) {
        return 0;
    }

    private PdfDictionary getFontDescriptor() {
        PdfDictionary dic = new PdfDictionary(PdfName.FONTDESCRIPTOR);
        dic.put(PdfName.ASCENT, new PdfLiteral((String)fontDesc.get("Ascent")));
        dic.put(PdfName.CAPHEIGHT, new PdfLiteral((String)fontDesc.get("CapHeight")));
        dic.put(PdfName.DESCENT, new PdfLiteral((String)fontDesc.get("Descent")));
        dic.put(PdfName.FLAGS, new PdfLiteral((String)fontDesc.get("Flags")));
        dic.put(PdfName.FONTBBOX, new PdfLiteral((String)fontDesc.get("FontBBox")));
        dic.put(PdfName.FONTNAME, new PdfName(fontName + style));
        dic.put(PdfName.ITALICANGLE, new PdfLiteral((String)fontDesc.get("ItalicAngle")));
        dic.put(PdfName.STEMV, new PdfLiteral((String)fontDesc.get("StemV")));
        PdfDictionary pdic = new PdfDictionary();
        pdic.put(PdfName.PANOSE, new PdfString((String)fontDesc.get("Panose"), null));
        dic.put(PdfName.STYLE, pdic);
        return dic;
    }

    private PdfDictionary getCIDFont(PdfIndirectReference fontDescriptor, IntHashtable cjkTag) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, PdfName.CIDFONTTYPE0);
        dic.put(PdfName.BASEFONT, new PdfName(fontName + style));
        dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor);
        int keys[] = cjkTag.toOrderedKeys();
        String w = convertToHCIDMetrics(keys, hMetrics);
        if (w != null)
            dic.put(PdfName.W, new PdfLiteral(w));
        if (vertical) {
            w = convertToVCIDMetrics(keys, vMetrics, hMetrics);
            if (w != null)
                dic.put(PdfName.W2, new PdfLiteral(w));
        }
        else
            dic.put(PdfName.DW, new PdfNumber(1000));
        PdfDictionary cdic = new PdfDictionary();
        if (cidDirect) {
            cdic.put(PdfName.REGISTRY, new PdfString(cidUni.getRegistry(), null));
            cdic.put(PdfName.ORDERING, new PdfString(cidUni.getOrdering(), null));
            cdic.put(PdfName.SUPPLEMENT, new PdfNumber(cidUni.getSupplement()));
        }
        else {
            cdic.put(PdfName.REGISTRY, new PdfString(cidByte.getRegistry(), null));
            cdic.put(PdfName.ORDERING, new PdfString(cidByte.getOrdering(), null));
            cdic.put(PdfName.SUPPLEMENT, new PdfNumber(cidByte.getSupplement()));
        }
        dic.put(PdfName.CIDSYSTEMINFO, cdic);
        return dic;
    }

    private PdfDictionary getFontBaseType(PdfIndirectReference CIDFont) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        dic.put(PdfName.SUBTYPE, PdfName.TYPE0);
        String name = fontName;
        if (style.length() > 0)
            name += "-" + style.substring(1);
        name += "-" + CMap;
        dic.put(PdfName.BASEFONT, new PdfName(name));
        dic.put(PdfName.ENCODING, new PdfName(CMap));
        dic.put(PdfName.DESCENDANTFONTS, new PdfArray(CIDFont));
        return dic;
    }

    @Override
    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object params[]) throws DocumentException, IOException {
        IntHashtable cjkTag = (IntHashtable)params[0];
        PdfIndirectReference ind_font = null;
        PdfObject pobj = null;
        PdfIndirectObject obj = null;
        pobj = getFontDescriptor();
        if (pobj != null){
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        }
        pobj = getCIDFont(ind_font, cjkTag);
        if (pobj != null){
            obj = writer.addToBody(pobj);
            ind_font = obj.getIndirectReference();
        }
        pobj = getFontBaseType(ind_font);
        writer.addToBody(pobj, ref);
    }

    /**
     * You can't get the FontStream of a CJK font (CJK fonts are never embedded),
     * so this method always returns null.
   	 * @return	null
     * @since	2.1.3
     */
    @Override
    public PdfStream getFullFontStream() {
    	return null;
    }

    private float getDescNumber(String name) {
        return Integer.parseInt((String)fontDesc.get(name));
    }

    private float getBBox(int idx) {
        String s = (String)fontDesc.get("FontBBox");
        StringTokenizer tk = new StringTokenizer(s, " []\r\n\t\f");
        String ret = tk.nextToken();
        for (int k = 0; k < idx; ++k)
            ret = tk.nextToken();
        return Integer.parseInt(ret);
    }

    /** Gets the font parameter identified by <CODE>key</CODE>. Valid values
     * for <CODE>key</CODE> are <CODE>ASCENT</CODE>, <CODE>CAPHEIGHT</CODE>, <CODE>DESCENT</CODE>
     * and <CODE>ITALICANGLE</CODE>.
     * @param key the parameter to be extracted
     * @param fontSize the font size in points
     * @return the parameter in points
     */
    @Override
    public float getFontDescriptor(int key, float fontSize) {
        switch (key) {
            case AWT_ASCENT:
            case ASCENT:
                return getDescNumber("Ascent") * fontSize / 1000;
            case CAPHEIGHT:
                return getDescNumber("CapHeight") * fontSize / 1000;
            case AWT_DESCENT:
            case DESCENT:
                return getDescNumber("Descent") * fontSize / 1000;
            case ITALICANGLE:
                return getDescNumber("ItalicAngle");
            case BBOXLLX:
                return fontSize * getBBox(0) / 1000;
            case BBOXLLY:
                return fontSize * getBBox(1) / 1000;
            case BBOXURX:
                return fontSize * getBBox(2) / 1000;
            case BBOXURY:
                return fontSize * getBBox(3) / 1000;
            case AWT_LEADING:
                return 0;
            case AWT_MAXADVANCE:
                return fontSize * (getBBox(2) - getBBox(0)) / 1000;
        }
        return 0;
    }

    @Override
    public String getPostscriptFontName() {
        return fontName;
    }

    /** Gets the full name of the font. If it is a True Type font
     * each array element will have {Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"", "", "",
     * font name}.
     * @return the full name of the font
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
     */
    @Override
    public String[][] getAllNameEntries() {
        return new String[][]{{"4", "", "", "", fontName}};
    }

    /** Gets the family name of the font. If it is a True Type font
     * each array element will have {Platform ID, Platform Encoding ID,
     * Language ID, font name}. The interpretation of this values can be
     * found in the Open Type specification, chapter 2, in the 'name' table.<br>
     * For the other fonts the array has a single element with {"", "", "",
     * font name}.
     * @return the family name of the font
     */
    @Override
    public String[][] getFamilyFontName() {
        return getFullFontName();
    }

//    static char[] readCMap(String name) {
//        try {
//            name = name + ".cmap";
//            InputStream is = getResourceStream(RESOURCE_PATH + name);
//            char c[] = new char[0x10000];
//            for (int k = 0; k < 0x10000; ++k)
//                c[k] = (char)((is.read() << 8) + is.read());
//            is.close();
//            return c;
//        }
//        catch (Exception e) {
//            // empty on purpose
//        }
//        return null;
//    }

    static IntHashtable createMetric(String s) {
        IntHashtable h = new IntHashtable();
        StringTokenizer tk = new StringTokenizer(s);
        while (tk.hasMoreTokens()) {
            int n1 = Integer.parseInt(tk.nextToken());
            h.put(n1, Integer.parseInt(tk.nextToken()));
        }
        return h;
    }

    static String convertToHCIDMetrics(int keys[], IntHashtable h) {
        if (keys.length == 0)
            return null;
        int lastCid = 0;
        int lastValue = 0;
        int start;
        for (start = 0; start < keys.length; ++start) {
            lastCid = keys[start];
            lastValue = h.get(lastCid);
            if (lastValue != 0) {
                ++start;
                break;
            }
        }
        if (lastValue == 0)
            return null;
        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(lastCid);
        int state = FIRST;
        for (int k = start; k < keys.length; ++k) {
            int cid = keys[k];
            int value = h.get(cid);
            if (value == 0)
                continue;
            switch (state) {
                case FIRST: {
                    if (cid == lastCid + 1 && value == lastValue) {
                        state = SERIAL;
                    }
                    else if (cid == lastCid + 1) {
                        state = BRACKET;
                        buf.append('[').append(lastValue);
                    }
                    else {
                        buf.append('[').append(lastValue).append(']').append(cid);
                    }
                    break;
                }
                case BRACKET: {
                    if (cid == lastCid + 1 && value == lastValue) {
                        state = SERIAL;
                        buf.append(']').append(lastCid);
                    }
                    else if (cid == lastCid + 1) {
                        buf.append(' ').append(lastValue);
                    }
                    else {
                        state = FIRST;
                        buf.append(' ').append(lastValue).append(']').append(cid);
                    }
                    break;
                }
                case SERIAL: {
                    if (cid != lastCid + 1 || value != lastValue) {
                        buf.append(' ').append(lastCid).append(' ').append(lastValue).append(' ').append(cid);
                        state = FIRST;
                    }
                    break;
                }
            }
            lastValue = value;
            lastCid = cid;
        }
        switch (state) {
            case FIRST: {
                buf.append('[').append(lastValue).append("]]");
                break;
            }
            case BRACKET: {
                buf.append(' ').append(lastValue).append("]]");
                break;
            }
            case SERIAL: {
                buf.append(' ').append(lastCid).append(' ').append(lastValue).append(']');
                break;
            }
        }
        return buf.toString();
    }

    static String convertToVCIDMetrics(int keys[], IntHashtable v, IntHashtable h) {
        if (keys.length == 0)
            return null;
        int lastCid = 0;
        int lastValue = 0;
        int lastHValue = 0;
        int start;
        for (start = 0; start < keys.length; ++start) {
            lastCid = keys[start];
            lastValue = v.get(lastCid);
            if (lastValue != 0) {
                ++start;
                break;
            }
            else
                lastHValue = h.get(lastCid);
        }
        if (lastValue == 0)
            return null;
        if (lastHValue == 0)
            lastHValue = 1000;
        StringBuilder buf = new StringBuilder();
        buf.append('[');
        buf.append(lastCid);
        int state = FIRST;
        for (int k = start; k < keys.length; ++k) {
            int cid = keys[k];
            int value = v.get(cid);
            if (value == 0)
                continue;
            int hValue = h.get(lastCid);
            if (hValue == 0)
                hValue = 1000;
            switch (state) {
                case FIRST: {
                    if (cid == lastCid + 1 && value == lastValue && hValue == lastHValue) {
                        state = SERIAL;
                    }
                    else {
                        buf.append(' ').append(lastCid).append(' ').append(-lastValue).append(' ').append(lastHValue / 2).append(' ').append(V1Y).append(' ').append(cid);
                    }
                    break;
                }
                case SERIAL: {
                    if (cid != lastCid + 1 || value != lastValue || hValue != lastHValue) {
                        buf.append(' ').append(lastCid).append(' ').append(-lastValue).append(' ').append(lastHValue / 2).append(' ').append(V1Y).append(' ').append(cid);
                        state = FIRST;
                    }
                    break;
                }
            }
            lastValue = value;
            lastCid = cid;
            lastHValue = hValue;
        }
        buf.append(' ').append(lastCid).append(' ').append(-lastValue).append(' ').append(lastHValue / 2).append(' ').append(V1Y).append(" ]");
        return buf.toString();
    }

    private static HashMap<String, Object> readFontProperties(String name) throws IOException {
        name += ".properties";
        InputStream is = StreamUtil.getResourceStream(RESOURCE_PATH_CMAP + name);
        Properties p = new Properties();
        p.load(is);
        is.close();
        IntHashtable W = createMetric(p.getProperty("W"));
        p.remove("W");
        IntHashtable W2 = createMetric(p.getProperty("W2"));
        p.remove("W2");
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (Enumeration<Object> e = p.keys(); e.hasMoreElements();) {
            Object obj = e.nextElement();
            map.put((String)obj, p.getProperty((String)obj));
        }
        map.put("W", W);
        map.put("W2", W2);
        return map;
    }

    @Override
    public int getUnicodeEquivalent(int c) {
        if (cidDirect) {
            if (c == CID_NEWLINE)
                return '\n';
            return cidUni.lookup(c);
        }
        return c;
    }

    @Override
    public int getCidCode(int c) {
        if (cidDirect)
            return c;
        return uniCid.lookup(c);
    }

    /** Checks if the font has any kerning pairs.
     * @return always <CODE>false</CODE>
     */
    @Override
    public boolean hasKernPairs() {
        return false;
    }

    /**
     * Checks if a character exists in this font.
     * @param c the character to check
     * @return <CODE>true</CODE> if the character has a glyph,
     * <CODE>false</CODE> otherwise
     */
    @Override
    public boolean charExists(int c) {
        if (cidDirect)
            return true;
        return cidByte.lookup(uniCid.lookup(c)).length > 0;
    }

    /**
     * Sets the character advance.
     * @param c the character
     * @param advance the character advance normalized to 1000 units
     * @return <CODE>true</CODE> if the advance was set,
     * <CODE>false</CODE> otherwise. Will always return <CODE>false</CODE>
     */
    @Override
    public boolean setCharAdvance(int c, int advance) {
        return false;
    }

    /**
     * Sets the font name that will appear in the pdf font dictionary.
     * Use with care as it can easily make a font unreadable if not embedded.
     * @param name the new font name
     */
    @Override
    public void setPostscriptFontName(String name) {
        fontName = name;
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
    
    /**
     * Converts a <CODE>String</CODE> to a </CODE>byte</CODE> array according
     * to the font's encoding.
     * @param text the <CODE>String</CODE> to be converted
     * @return an array of <CODE>byte</CODE> representing the conversion according to the font's encoding
     */
    @Override
    public byte[] convertToBytes(String text) {
        if (cidDirect)
            return super.convertToBytes(text);
        try {
            if (text.length() == 1)
                return convertToBytes((int)text.charAt(0));
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            for (int k = 0; k < text.length(); ++k) {
                int val;
                if (Utilities.isSurrogatePair(text, k)) {
                    val = Utilities.convertToUtf32(text, k);
                    k++;
                }
                else {
                    val = text.charAt(k);
                }
                bout.write(convertToBytes(val));
            }
            return bout.toByteArray();
        }
        catch (Exception ex) {
            throw new ExceptionConverter(ex);
        }
    }
    
    /**
     * Converts a <CODE>char</CODE> to a </CODE>byte</CODE> array according
     * to the font's encoding.
     * @param char1 the <CODE>char</CODE> to be converted
     * @return an array of <CODE>byte</CODE> representing the conversion according to the font's encoding
     */
    @Override
    byte[] convertToBytes(int char1) {
        if (cidDirect)
            return super.convertToBytes(char1);
        return cidByte.lookup(uniCid.lookup(char1));
    }
    
    public boolean isIdentity() {
        return cidDirect;
    }
}
