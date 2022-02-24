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
import com.itextpdf.text.error_messages.MessageLocalization;

import java.util.HashMap;

/**
 * A class to support Type3 fonts.
 */
public class Type3Font extends BaseFont {

	private boolean[] usedSlot;
    private IntHashtable widths3 = new IntHashtable();
    private HashMap<Integer, Type3Glyph> char2glyph = new HashMap<Integer, Type3Glyph>();
    private PdfWriter writer;
    private float llx = Float.NaN, lly, urx, ury;
    private PageResources pageResources = new PageResources();
    private boolean colorized;

    /**
     * Creates a Type3 font.
     * @param writer the writer
     * @param chars an array of chars corresponding to the glyphs used (not used, present for compatibility only)
     * @param colorized if <CODE>true</CODE> the font may specify color, if <CODE>false</CODE> no color commands are allowed
     * and only images as masks can be used
     */
    public Type3Font(PdfWriter writer, char[] chars, boolean colorized) {
        this(writer, colorized);
    }

    /**
     * Creates a Type3 font. This implementation assumes that the /FontMatrix is
     * [0.001 0 0 0.001 0 0] or a 1000-unit glyph coordinate system.
     * <p>
     * An example:
     * <p>
     * <pre>
     * Document document = new Document(PageSize.A4);
     * PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("type3.pdf"));
     * document.open();
     * Type3Font t3 = new Type3Font(writer, false);
     * PdfContentByte g = t3.defineGlyph('a', 1000, 0, 0, 750, 750);
     * g.rectangle(0, 0, 750, 750);
     * g.fill();
     * g = t3.defineGlyph('b', 1000, 0, 0, 750, 750);
     * g.moveTo(0, 0);
     * g.lineTo(375, 750);
     * g.lineTo(750, 0);
     * g.fill();
     * Font f = new Font(t3, 12);
     * document.add(new Paragraph("ababab", f));
     * document.close();
     * </pre>
     * @param writer the writer
     * @param colorized if <CODE>true</CODE> the font may specify color, if <CODE>false</CODE> no color commands are allowed
     * and only images as masks can be used
     */
    public Type3Font(PdfWriter writer, boolean colorized) {
        this.writer = writer;
        this.colorized = colorized;
        fontType = FONT_TYPE_T3;
        usedSlot = new boolean[256];
    }

    /**
     * Defines a glyph. If the character was already defined it will return the same content
     * @param c the character to match this glyph.
     * @param wx the advance this character will have
     * @param llx the X lower left corner of the glyph bounding box. If the <CODE>colorize</CODE> option is
     * <CODE>true</CODE> the value is ignored
     * @param lly the Y lower left corner of the glyph bounding box. If the <CODE>colorize</CODE> option is
     * <CODE>true</CODE> the value is ignored
     * @param urx the X upper right corner of the glyph bounding box. If the <CODE>colorize</CODE> option is
     * <CODE>true</CODE> the value is ignored
     * @param ury the Y upper right corner of the glyph bounding box. If the <CODE>colorize</CODE> option is
     * <CODE>true</CODE> the value is ignored
     * @return a content where the glyph can be defined
     */
    public PdfContentByte defineGlyph(char c, float wx, float llx, float lly, float urx, float ury) {
        if (c == 0 || c > 255)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.char.1.doesn.t.belong.in.this.type3.font", c));
        usedSlot[c] = true;
        Integer ck = Integer.valueOf(c);
        Type3Glyph glyph = char2glyph.get(ck);
        if (glyph != null)
            return glyph;
        widths3.put(c, (int)wx);
        if (!colorized) {
            if (Float.isNaN(this.llx)) {
                this.llx = llx;
                this.lly = lly;
                this.urx = urx;
                this.ury = ury;
            }
            else {
                this.llx = Math.min(this.llx, llx);
                this.lly = Math.min(this.lly, lly);
                this.urx = Math.max(this.urx, urx);
                this.ury = Math.max(this.ury, ury);
            }
        }
        glyph = new Type3Glyph(writer, pageResources, wx, llx, lly, urx, ury, colorized);
        char2glyph.put(ck, glyph);
        return glyph;
    }

    @Override
    public String[][] getFamilyFontName() {
        return getFullFontName();
    }

    @Override
    public float getFontDescriptor(int key, float fontSize) {
        return 0;
    }

    @Override
    public String[][] getFullFontName() {
        return new String[][]{{"", "", "", ""}};
    }

    /**
     * @since 2.0.8
     */
    @Override
    public String[][] getAllNameEntries() {
        return new String[][]{{"4", "", "", "", ""}};
    }

    @Override
    public int getKerning(int char1, int char2) {
        return 0;
    }

    @Override
    public String getPostscriptFontName() {
        return "";
    }

    @Override
    protected int[] getRawCharBBox(int c, String name) {
        return null;
    }

    @Override
    int getRawWidth(int c, String name) {
        return 0;
    }

    @Override
    public boolean hasKernPairs() {
        return false;
    }

    @Override
    public boolean setKerning(int char1, int char2, int kern) {
        return false;
    }

    @Override
    public void setPostscriptFontName(String name) {
    }

    @Override
    void writeFont(PdfWriter writer, PdfIndirectReference ref, Object[] params) throws com.itextpdf.text.DocumentException, java.io.IOException {
        if (this.writer != writer)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("type3.font.used.with.the.wrong.pdfwriter"));

        // Get first & lastchar ...
        int firstChar = 0;
        while( firstChar < usedSlot.length && !usedSlot[firstChar] ) firstChar++;

        if ( firstChar == usedSlot.length ) {
        	throw new DocumentException(MessageLocalization.getComposedMessage("no.glyphs.defined.for.type3.font"));
        }
        int lastChar = usedSlot.length - 1;
        while( lastChar >= firstChar && !usedSlot[lastChar] ) lastChar--;

        int[] widths = new int[lastChar - firstChar + 1];
        int[] invOrd = new int[lastChar - firstChar + 1];

        int invOrdIndx = 0, w = 0;
        for( int u = firstChar; u<=lastChar; u++, w++ ) {
            if ( usedSlot[u] ) {
                invOrd[invOrdIndx++] = u;
                widths[w] = widths3.get(u);
            }
        }
        PdfArray diffs = new PdfArray();
        PdfDictionary charprocs = new PdfDictionary();
        int last = -1;
        for (int k = 0; k < invOrdIndx; ++k) {
            int c = invOrd[k];
            if (c > last) {
                last = c;
                diffs.add(new PdfNumber(last));
            }
            ++last;
            int c2 = invOrd[k];
            String s = GlyphList.unicodeToName(c2);
            if (s == null)
                s = "a" + c2;
            PdfName n = new PdfName(s);
            diffs.add(n);
            Type3Glyph glyph = char2glyph.get(Integer.valueOf(c2));
            PdfStream stream = new PdfStream(glyph.toPdf(null));
            stream.flateCompress(compressionLevel);
            PdfIndirectReference refp = writer.addToBody(stream).getIndirectReference();
            charprocs.put(n, refp);
        }
        PdfDictionary font = new PdfDictionary(PdfName.FONT);
        font.put(PdfName.SUBTYPE, PdfName.TYPE3);
        if (colorized)
            font.put(PdfName.FONTBBOX, new PdfRectangle(0, 0, 0, 0));
        else
            font.put(PdfName.FONTBBOX, new PdfRectangle(llx, lly, urx, ury));
        font.put(PdfName.FONTMATRIX, new PdfArray(new float[]{0.001f, 0, 0, 0.001f, 0, 0}));
        font.put(PdfName.CHARPROCS, writer.addToBody(charprocs).getIndirectReference());
        PdfDictionary encoding = new PdfDictionary();
        encoding.put(PdfName.DIFFERENCES, diffs);
        font.put(PdfName.ENCODING, writer.addToBody(encoding).getIndirectReference());
        font.put(PdfName.FIRSTCHAR, new PdfNumber(firstChar));
        font.put(PdfName.LASTCHAR, new PdfNumber(lastChar));
        font.put(PdfName.WIDTHS, writer.addToBody(new PdfArray(widths)).getIndirectReference());
        if (pageResources.hasResources())
            font.put(PdfName.RESOURCES, writer.addToBody(pageResources.getResources()).getIndirectReference());
        writer.addToBody(font, ref);
    }

    /**
     * Always returns null, because you can't get the FontStream of a Type3 font.
   	 * @return	null
     * @since	2.1.3
     */
    @Override
    public PdfStream getFullFontStream() {
    	return null;
    }


    @Override
    public byte[] convertToBytes(String text) {
        char[] cc = text.toCharArray();
        byte[] b = new byte[cc.length];
        int p = 0;
        for (int k = 0; k < cc.length; ++k) {
            char c = cc[k];
            if (charExists(c))
                b[p++] = (byte)c;
        }
        if (b.length == p)
            return b;
        byte[] b2 = new byte[p];
        System.arraycopy(b, 0, b2, 0, p);
        return b2;
    }

    @Override
    byte[] convertToBytes(int char1) {
        if (charExists(char1))
            return new byte[]{(byte)char1};
        else return new byte[0];
    }

    @Override
    public int getWidth(int char1) {
        if (!widths3.containsKey(char1))
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.char.1.is.not.defined.in.a.type3.font", char1));
        return widths3.get(char1);
    }

    @Override
    public int getWidth(String text) {
        char[] c = text.toCharArray();
        int total = 0;
        for (int k = 0; k < c.length; ++k)
            total += getWidth(c[k]);
        return total;
    }

    @Override
    public int[] getCharBBox(int c) {
        return null;
    }

    @Override
    public boolean charExists(int c) {
        if (c > 0 && c < 256) {
            return usedSlot[c];
        } else {
            return false;
        }
    }

    @Override
    public boolean setCharAdvance(int c, int advance) {
        return false;
    }

}
