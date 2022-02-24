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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Utilities;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

import java.text.MessageFormat;
import java.util.ArrayList;

/** Selects the appropriate fonts that contain the glyphs needed to
 * render text correctly. The fonts are checked in order until the
 * character is found.
 * <p>
 * The built in fonts "Symbol" and "ZapfDingbats", if used, have a special encoding
 * to allow the characters to be referred by Unicode.
 * @author Paulo Soares
 */
public class FontSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfSmartCopy.class);

    protected ArrayList<Font> fonts = new ArrayList<Font>();
    protected ArrayList<Font> unsupportedFonts = new ArrayList<Font>();
    protected Font currentFont = null;

    /**
     * Adds a <CODE>Font</CODE> to be searched for valid characters.
     * @param font the <CODE>Font</CODE>
     */
    public void addFont(Font font) {
        if (!isSupported(font)) {
            unsupportedFonts.add(font);
            return;
        }
        if (font.getBaseFont() != null) {
            fonts.add(font);
            return;
        }
        BaseFont bf = font.getCalculatedBaseFont(true);
        Font f2 = new Font(bf, font.getSize(), font.getCalculatedStyle(), font.getColor());
        fonts.add(f2);
    }

    /**
     * Process the text so that it will render with a combination of fonts
     * if needed.
     * @param text the text
     * @return a <CODE>Phrase</CODE> with one or more chunks
     */
    public Phrase process(String text) {
        if (getSize() == 0)
            throw new IndexOutOfBoundsException(MessageLocalization.getComposedMessage("no.font.is.defined"));
        char cc[] = text.toCharArray();
        int len = cc.length;
        StringBuffer sb = new StringBuffer();
        Phrase ret = new Phrase();
        currentFont = null;
        for (int k = 0; k < len; ++k) {
            Chunk newChunk = processChar(cc, k, sb);
            if (newChunk != null) {
                ret.add(newChunk);
            }
        }
        if (sb.length() > 0) {
            Chunk ck = new Chunk(sb.toString(), currentFont != null ? currentFont : getFont(0));
            ret.add(ck);
        }
        return ret;
    }

    protected Chunk processChar(char[] cc, int k, StringBuffer sb) {
        Chunk newChunk = null;
        char c = cc[k];
        if (c == '\n' || c == '\r') {
            sb.append(c);
        } else {
            Font font = null;
            if (Utilities.isSurrogatePair(cc, k)) {
                int u = Utilities.convertToUtf32(cc, k);
                for (int f = 0; f < getSize(); ++f) {
                    font = getFont(f);
                    if (font.getBaseFont().charExists(u) || Character.getType(u) == Character.FORMAT) {
                        if (currentFont != font) {
                            if (sb.length() > 0 && currentFont != null) {
                                newChunk = new Chunk(sb.toString(), currentFont);
                                sb.setLength(0);
                            }
                            currentFont = font;
                        }
                        sb.append(c);
                        sb.append(cc[++k]);
                        break;
                    }
                }
            } else {
                for (int f = 0; f < getSize(); ++f) {
                    font = getFont(f);
                    if (font.getBaseFont().charExists(c) || Character.getType(c) == Character.FORMAT) {
                        if (currentFont != font) {
                            if (sb.length() > 0 && currentFont != null) {
                                newChunk = new Chunk(sb.toString(), currentFont);
                                sb.setLength(0);
                            }
                            currentFont = font;
                        }
                        sb.append(c);
                        break;
                    }
                }
            }
        }
        return newChunk;
    }

    protected int getSize() {
        return fonts.size() + unsupportedFonts.size();
    }

    protected Font getFont(int i) {
        return i < fonts.size()
                ? fonts.get(i)
                : unsupportedFonts.get(i - fonts.size());
    }

    private boolean isSupported(Font font) {
        BaseFont bf = font.getBaseFont();
        if (bf instanceof TrueTypeFont && BaseFont.WINANSI.equals(bf.getEncoding()) && !((TrueTypeFont) bf).isWinAnsiSupported()) {
            LOGGER.warn(MessageFormat.format("cmap(1, 0) not found for TrueType Font {0}, it is required for WinAnsi encoding.", font));
            return false;
        }
        return true;
    }
}
