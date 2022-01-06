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
import com.itextpdf.text.Utilities;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.fonts.otf.GlyphSubstitutionTableReader;
import com.itextpdf.text.pdf.fonts.otf.Language;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Represents a True Type font with Unicode encoding. All the character
 * in the font can be used directly by using the encoding Identity-H or
 * Identity-V. This is the only way to represent some character sets such
 * as Thai.
 * @author  Paulo Soares
 */
class TrueTypeFontUnicode extends TrueTypeFont implements Comparator<int[]>{
	
	private static final List<Language> SUPPORTED_LANGUAGES_FOR_OTF = Arrays.asList(Language.BENGALI);  
	
	private Map<String, Glyph> glyphSubstitutionMap;
	private Language supportedLanguage;

    /**
     * Creates a new TrueType font addressed by Unicode characters. The font
     * will always be embedded.
     * @param ttFile the location of the font on file. The file must end in '.ttf'.
     * The modifiers after the name are ignored.
     * @param enc the encoding to be applied to this font
     * @param emb true if the font is to be embedded in the PDF
     * @param ttfAfm the font as a <CODE>byte</CODE> array
     * @throws DocumentException the font is invalid
     * @throws IOException the font file could not be read
     */
    TrueTypeFontUnicode(String ttFile, String enc, boolean emb, byte ttfAfm[], boolean forceRead) throws DocumentException, IOException {
        String nameBase = getBaseName(ttFile);
        String ttcName = getTTCName(nameBase);
        if (nameBase.length() < ttFile.length()) {
            style = ttFile.substring(nameBase.length());
        }
        encoding = enc;
        embedded = emb;
        fileName = ttcName;
        ttcIndex = "";
        if (ttcName.length() < nameBase.length())
            ttcIndex = nameBase.substring(ttcName.length() + 1);
        fontType = FONT_TYPE_TTUNI;
        if ((fileName.toLowerCase().endsWith(".ttf") || fileName.toLowerCase().endsWith(".otf") || fileName.toLowerCase().endsWith(".ttc")) && (enc.equals(IDENTITY_H) || enc.equals(IDENTITY_V)) && emb) {
            process(ttfAfm, forceRead);
            if (os_2.fsType == 2)
                throw new DocumentException(MessageLocalization.getComposedMessage("1.cannot.be.embedded.due.to.licensing.restrictions", fileName + style));
            // Sivan
            if (cmap31 == null && !fontSpecific || cmap10 == null && fontSpecific)
                directTextToByte = true;
                //throw new DocumentException(MessageLocalization.getComposedMessage("1.2.does.not.contain.an.usable.cmap", fileName, style));
            if (fontSpecific) {
                fontSpecific = false;
                String tempEncoding = encoding;
                encoding = "";
                createEncoding();
                encoding = tempEncoding;
                fontSpecific = true;
            }
        }
        else
            throw new DocumentException(MessageLocalization.getComposedMessage("1.2.is.not.a.ttf.font.file", fileName, style));
        vertical = enc.endsWith("V");
    }
    
    @Override
    void process(byte ttfAfm[], boolean preload) throws DocumentException, IOException {
    	super.process(ttfAfm, preload);
    	//readGsubTable();
    }

    /**
     * Gets the width of a <CODE>char</CODE> in normalized 1000 units.
     * @param char1 the unicode <CODE>char</CODE> to get the width of
     * @return the width in normalized 1000 units
     */
    @Override
    public int getWidth(int char1) {
        if (vertical)
            return 1000;
        if (fontSpecific) {
            if ((char1 & 0xff00) == 0 || (char1 & 0xff00) == 0xf000)
                return getRawWidth(char1 & 0xff, null);
            else
                return 0;
        }
        else {
            return getRawWidth(char1, encoding);
        }
    }

    /**
     * Gets the width of a <CODE>String</CODE> in normalized 1000 units.
     * @param text the <CODE>String</CODE> to get the width of
     * @return the width in normalized 1000 units
     */
    @Override
    public int getWidth(String text) {
        if (vertical)
            return text.length() * 1000;
        int total = 0;
        if (fontSpecific) {
            char cc[] = text.toCharArray();
            int len = cc.length;
            for (int k = 0; k < len; ++k) {
                char c = cc[k];
                if ((c & 0xff00) == 0 || (c & 0xff00) == 0xf000)
                    total += getRawWidth(c & 0xff, null);
            }
        }
        else {
            int len = text.length();
            for (int k = 0; k < len; ++k) {
                if (Utilities.isSurrogatePair(text, k)) {
                    total += getRawWidth(Utilities.convertToUtf32(text, k), encoding);
                    ++k;
                }
                else
                    total += getRawWidth(text.charAt(k), encoding);
            }
        }
        return total;
    }

    /** Creates a ToUnicode CMap to allow copy and paste from Acrobat.
     * @param metrics metrics[0] contains the glyph index and metrics[2]
     * contains the Unicode code
     * @return the stream representing this CMap or <CODE>null</CODE>
     */
    public PdfStream getToUnicode(Object metrics[]) {
        if (metrics.length == 0)
            return null;
        StringBuffer buf = new StringBuffer(
        "/CIDInit /ProcSet findresource begin\n" +
        "12 dict begin\n" +
        "begincmap\n" +
        "/CIDSystemInfo\n" +
        "<< /Registry (TTX+0)\n" +
        "/Ordering (T42UV)\n" +
        "/Supplement 0\n" +
        ">> def\n" +
        "/CMapName /TTX+0 def\n" +
        "/CMapType 2 def\n" +
        "1 begincodespacerange\n" +
        "<0000><FFFF>\n" +
        "endcodespacerange\n");
        int size = 0;
        for (int k = 0; k < metrics.length; ++k) {
            if (size == 0) {
                if (k != 0) {
                    buf.append("endbfrange\n");
                }
                size = Math.min(100, metrics.length - k);
                buf.append(size).append(" beginbfrange\n");
            }
            --size;
            int metric[] = (int[])metrics[k];
            String fromTo = toHex(metric[0]);
            buf.append(fromTo).append(fromTo).append(toHex(metric[2])).append('\n');
        }
        buf.append(
        "endbfrange\n" +
        "endcmap\n" +
        "CMapName currentdict /CMap defineresource pop\n" +
        "end end\n");
        String s = buf.toString();
        PdfStream stream = new PdfStream(PdfEncodings.convertToBytes(s, null));
        stream.flateCompress(compressionLevel);
        return stream;
    }

    private static String toHex4(int n) {
        String s = "0000" + Integer.toHexString(n);
        return s.substring(s.length() - 4);
    }

    /** Gets an hex string in the format "&lt;HHHH&gt;".
     * @param n the number
     * @return the hex string
     */
    static String toHex(int n) {
        if (n < 0x10000)
            return "<" + toHex4(n) + ">";
        n -= 0x10000;
        int high = n / 0x400 + 0xd800;
        int low = n % 0x400 + 0xdc00;
        return "[<" + toHex4(high) + toHex4(low) + ">]";
    }

    /** Generates the CIDFontTyte2 dictionary.
     * @param fontDescriptor the indirect reference to the font descriptor
     * @param subsetPrefix the subset prefix
     * @param metrics the horizontal width metrics
     * @return a stream
     */
    public PdfDictionary getCIDFontType2(PdfIndirectReference fontDescriptor, String subsetPrefix, Object metrics[]) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);
        // sivan; cff
        if (cff) {
			dic.put(PdfName.SUBTYPE, PdfName.CIDFONTTYPE0);
            dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix+fontName+"-"+encoding));
        }
		else {
			dic.put(PdfName.SUBTYPE, PdfName.CIDFONTTYPE2);
            dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + fontName));
        }
        dic.put(PdfName.FONTDESCRIPTOR, fontDescriptor);
        if (!cff)
          dic.put(PdfName.CIDTOGIDMAP,PdfName.IDENTITY);
        PdfDictionary cdic = new PdfDictionary();
        cdic.put(PdfName.REGISTRY, new PdfString("Adobe"));
        cdic.put(PdfName.ORDERING, new PdfString("Identity"));
        cdic.put(PdfName.SUPPLEMENT, new PdfNumber(0));
        dic.put(PdfName.CIDSYSTEMINFO, cdic);
        if (!vertical) {
            dic.put(PdfName.DW, new PdfNumber(1000));
            StringBuffer buf = new StringBuffer("[");
            int lastNumber = -10;
            boolean firstTime = true;
            for (int k = 0; k < metrics.length; ++k) {
                int metric[] = (int[])metrics[k];
                if (metric[1] == 1000)
                    continue;
                int m = metric[0];
                if (m == lastNumber + 1) {
                    buf.append(' ').append(metric[1]);
                }
                else {
                    if (!firstTime) {
                        buf.append(']');
                    }
                    firstTime = false;
                    buf.append(m).append('[').append(metric[1]);
                }
                lastNumber = m;
            }
            if (buf.length() > 1) {
                buf.append("]]");
                dic.put(PdfName.W, new PdfLiteral(buf.toString()));
            }
        }
        return dic;
    }

    /** Generates the font dictionary.
     * @param descendant the descendant dictionary
     * @param subsetPrefix the subset prefix
     * @param toUnicode the ToUnicode stream
     * @return the stream
     */
    public PdfDictionary getFontBaseType(PdfIndirectReference descendant, String subsetPrefix, PdfIndirectReference toUnicode) {
        PdfDictionary dic = new PdfDictionary(PdfName.FONT);

        dic.put(PdfName.SUBTYPE, PdfName.TYPE0);
        // The PDF Reference manual advises to add -encoding to CID font names
		if (cff)
		  dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix+fontName+"-"+encoding));
		  //dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix+fontName));
		else
		  dic.put(PdfName.BASEFONT, new PdfName(subsetPrefix + fontName));
		  //dic.put(PdfName.BASEFONT, new PdfName(fontName));
        dic.put(PdfName.ENCODING, new PdfName(encoding));
        dic.put(PdfName.DESCENDANTFONTS, new PdfArray(descendant));
        if (toUnicode != null)
            dic.put(PdfName.TOUNICODE, toUnicode);
        return dic;
    }

    public int GetCharFromGlyphId(int gid) {
        if (glyphIdToChar == null) {
            int[] g2 = new int[maxGlyphId];
            HashMap<Integer, int[]> map = null;
            if (cmapExt != null) {
                map = cmapExt;
            }
            else if (cmap31 != null) {
                map = cmap31;
            }
            if (map != null) {
                for (Map.Entry<Integer, int[]> entry : map.entrySet()) {
                    g2[entry.getValue()[0]] = entry.getKey().intValue();
                }
            }
            glyphIdToChar = g2;
        }
        return glyphIdToChar[gid];
    }
    
    /** The method used to sort the metrics array.
     * @param o1 the first element
     * @param o2 the second element
     * @return the comparison
     */
    public int compare(int[] o1, int[] o2) {
        int m1 = o1[0];
        int m2 = o2[0];
        if (m1 < m2)
            return -1;
        if (m1 == m2)
            return 0;
        return 1;
    }

    private static final byte[] rotbits = {(byte)0x80,(byte)0x40,(byte)0x20,(byte)0x10,(byte)0x08,(byte)0x04,(byte)0x02,(byte)0x01};

    /** Outputs to the writer the font dictionaries and streams.
     * @param writer the writer for this document
     * @param ref the font indirect reference
     * @param params several parameters that depend on the font type
     * @throws IOException on error
     * @throws DocumentException error in generating the object
     */
    @Override
    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object params[]) throws DocumentException, IOException {
        writer.getTtfUnicodeWriter().writeFont(this, ref, params, rotbits);
    }

    /**
     * Returns a PdfStream object with the full font program.
     * @return	a PdfStream with the font program
     * @since	2.1.3
     */
    @Override
    public PdfStream getFullFontStream() throws IOException, DocumentException {
    	if (cff) {
			return new StreamFont(readCffFont(), "CIDFontType0C", compressionLevel);
        }
    	return super.getFullFontStream();
    }

    /** A forbidden operation. Will throw a null pointer exception.
     * @param text the text
     * @return always <CODE>null</CODE>
     */
    @Override
    public byte[] convertToBytes(String text) {
        return null;
    }

    @Override
    byte[] convertToBytes(int char1) {
        return null;
    }

    /** Gets the glyph index and metrics for a character.
     * @param c the character
     * @return an <CODE>int</CODE> array with {glyph index, width}
     */
    @Override
    public int[] getMetricsTT(int c) {
        if (cmapExt != null)
            return cmapExt.get(Integer.valueOf(c));
        HashMap<Integer, int[]> map = null;
        if (fontSpecific)
            map = cmap10;
        else
            map = cmap31;
        if (map == null)
            return null;
        if (fontSpecific) {
            if ((c & 0xffffff00) == 0 || (c & 0xffffff00) == 0xf000)
                return map.get(Integer.valueOf(c & 0xff));
            else
                return null;
        }
        else {
            int[] result = map.get(Integer.valueOf(c));
            if (result == null) {
                Character ch = ArabicLigaturizer.getReverseMapping((char) c);
                if (ch != null)
                    result = map.get(Integer.valueOf(ch));
            }
            return result;
        }
    }

    /**
     * Checks if a character exists in this font.
     * @param c the character to check
     * @return <CODE>true</CODE> if the character has a glyph,
     * <CODE>false</CODE> otherwise
     */
    @Override
    public boolean charExists(int c) {
        return getMetricsTT(c) != null;
    }

    /**
     * Sets the character advance.
     * @param c the character
     * @param advance the character advance normalized to 1000 units
     * @return <CODE>true</CODE> if the advance was set,
     * <CODE>false</CODE> otherwise
     */
    @Override
    public boolean setCharAdvance(int c, int advance) {
        int[] m = getMetricsTT(c);
        if (m == null)
            return false;
        m[1] = advance;
        return true;
    }

    @Override
    public int[] getCharBBox(int c) {
        if (bboxes == null)
            return null;
        int[] m = getMetricsTT(c);
        if (m == null)
            return null;
        return bboxes[m[0]];
    }
    
    protected Map<String, Glyph> getGlyphSubstitutionMap() {
        return glyphSubstitutionMap;
    }
    
    Language getSupportedLanguage() {
    	return supportedLanguage;
    }
    
    private void readGsubTable() throws IOException { 
        if (tables.get("GSUB") != null) {
            
            Map<Integer, Character> glyphToCharacterMap = new HashMap<Integer, Character>(cmap31.size());

            for (Integer charCode : cmap31.keySet()) {
                char c = (char) charCode.intValue();
                int glyphCode = cmap31.get(charCode)[0];
                glyphToCharacterMap.put(glyphCode, c);
            }
        
            GlyphSubstitutionTableReader gsubReader = new GlyphSubstitutionTableReader(
            		rf, tables.get("GSUB")[0], glyphToCharacterMap, glyphWidthsByIndex);
            
            try {
            	gsubReader.read();
            	supportedLanguage = gsubReader.getSupportedLanguage();
            	
            	if (SUPPORTED_LANGUAGES_FOR_OTF.contains(supportedLanguage)) {
            		glyphSubstitutionMap = gsubReader.getGlyphSubstitutionMap();
                    /*if (false) {
                    	StringBuilder  sb = new StringBuilder(50);
                        
                        for (int glyphCode : glyphToCharacterMap.keySet()) {
                        	sb.append(glyphCode).append("=>").append(glyphToCharacterMap.get(glyphCode)).append("\n");
                        }
                        System.out.println("GlyphToCharacterMap:\n" + sb.toString());
                    }
                    if (false) {
                        StringBuilder sb = new StringBuilder(50);
                        int count = 1;
                        
                        for (String chars : glyphSubstitutionMap.keySet()) {
                            int glyphId = glyphSubstitutionMap.get(chars).code;
                            sb.append(count++).append(".>");
                            sb.append(chars).append(" => ").append(glyphId).append("\n");
                        }
                        System.out.println("GlyphSubstitutionMap:\n" + sb.toString());
                    }*/
            	}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
