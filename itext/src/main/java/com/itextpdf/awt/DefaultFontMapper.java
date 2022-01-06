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
package com.itextpdf.awt;

import java.awt.Font;
import java.io.File;
import java.util.HashMap;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.BaseFont;
/** Default class to map awt fonts to BaseFont.
 * @author Paulo Soares
 */

public class DefaultFontMapper implements FontMapper {

    /** A representation of BaseFont parameters.
     */
    public static class BaseFontParameters {
        /** The font name.
         */
        public String fontName;
        /** The encoding for that font.
         */
        public String encoding;
        /** The embedding for that font.
         */
        public boolean embedded;
        /** Whether the font is cached of not.
         */
        public boolean cached;
        /** The font bytes for ttf and afm.
         */
        public byte ttfAfm[];
        /** The font bytes for pfb.
         */
        public byte pfb[];

        /** Constructs default BaseFont parameters.
         * @param fontName the font name or location
         */
        public BaseFontParameters(String fontName) {
            this.fontName = fontName;
            encoding = BaseFont.CP1252;
            embedded = BaseFont.EMBEDDED;
            cached = BaseFont.CACHED;
        }
    }

    /** Maps aliases to names.
     */
    private HashMap<String, String> aliases = new HashMap<String, String>();
    /** Maps names to BaseFont parameters.
     */
    private HashMap<String, BaseFontParameters> mapper = new HashMap<String, BaseFontParameters>();
    /**
     * Returns a BaseFont which can be used to represent the given AWT Font
     *
     * @param	font		the font to be converted
     * @return	a BaseFont which has similar properties to the provided Font
     */

    public BaseFont awtToPdf(Font font) {
        try {
            BaseFontParameters p = getBaseFontParameters(font.getFontName());
            if (p != null)
                return BaseFont.createFont(p.fontName, p.encoding, p.embedded, p.cached, p.ttfAfm, p.pfb);
            String fontKey = null;
            String logicalName = font.getName();

            if (logicalName.equalsIgnoreCase("DialogInput") || logicalName.equalsIgnoreCase("Monospaced") || logicalName.equalsIgnoreCase("Courier")) {

                if (font.isItalic()) {
                    if (font.isBold()) {
                        fontKey = BaseFont.COURIER_BOLDOBLIQUE;

                    } else {
                        fontKey = BaseFont.COURIER_OBLIQUE;
                    }

                } else {
                    if (font.isBold()) {
                        fontKey = BaseFont.COURIER_BOLD;

                    } else {
                        fontKey = BaseFont.COURIER;
                    }
                }

            } else if (logicalName.equalsIgnoreCase("Serif") || logicalName.equalsIgnoreCase("TimesRoman")) {

                if (font.isItalic()) {
                    if (font.isBold()) {
                        fontKey = BaseFont.TIMES_BOLDITALIC;

                    } else {
                        fontKey = BaseFont.TIMES_ITALIC;
                    }

                } else {
                    if (font.isBold()) {
                        fontKey = BaseFont.TIMES_BOLD;

                    } else {
                        fontKey = BaseFont.TIMES_ROMAN;
                    }
                }

            } else {  // default, this catches Dialog and SansSerif

                if (font.isItalic()) {
                    if (font.isBold()) {
                        fontKey = BaseFont.HELVETICA_BOLDOBLIQUE;

                    } else {
                        fontKey = BaseFont.HELVETICA_OBLIQUE;
                    }

                } else {
                    if (font.isBold()) {
                        fontKey = BaseFont.HELVETICA_BOLD;
                    } else {
                        fontKey = BaseFont.HELVETICA;
                    }
                }
            }
            return BaseFont.createFont(fontKey, BaseFont.CP1252, false);
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }

    /**
     * Returns an AWT Font which can be used to represent the given BaseFont
     *
     * @param	font		the font to be converted
     * @param	size		the desired point size of the resulting font
     * @return	a Font which has similar properties to the provided BaseFont
     */

    public Font pdfToAwt(BaseFont font, int size) {
        String names[][] = font.getFullFontName();
        if (names.length == 1)
            return new Font(names[0][3], 0, size);
        String name10 = null;
        String name3x = null;
        for (int k = 0; k < names.length; ++k) {
            String name[] = names[k];
            if (name[0].equals("1") && name[1].equals("0"))
                name10 = name[3];
            else if (name[2].equals("1033")) {
                name3x = name[3];
                break;
            }
        }
        String finalName = name3x;
        if (finalName == null)
            finalName = name10;
        if (finalName == null)
            finalName = names[0][3];
        return new Font(finalName, 0, size);
    }

    /** Maps a name to a BaseFont parameter.
     * @param awtName the name
     * @param parameters the BaseFont parameter
     */
    public void putName(String awtName, BaseFontParameters parameters) {
        mapper.put(awtName, parameters);
    }

    /** Maps an alias to a name.
     * @param alias the alias
     * @param awtName the name
     */
    public void putAlias(String alias, String awtName) {
        aliases.put(alias, awtName);
    }

    /** Looks for a BaseFont parameter associated with a name.
     * @param name the name
     * @return the BaseFont parameter or <CODE>null</CODE> if not found.
     */
    public BaseFontParameters getBaseFontParameters(String name) {
        String alias = aliases.get(name);
        if (alias == null)
            return mapper.get(name);
        BaseFontParameters p = mapper.get(alias);
        if (p == null)
            return mapper.get(name);
        else
            return p;
    }

    /**
     * Inserts the names in this map.
     * @param allNames the returned value of calling {@link BaseFont#getAllFontNames(String, String, byte[])}
     * @param path the full path to the font
     */
    public void insertNames(Object allNames[], String path) {
        String names[][] = (String[][])allNames[2];
        String main = null;
        for (int k = 0; k < names.length; ++k) {
            String name[] = names[k];
            if (name[2].equals("1033")) {
                main = name[3];
                break;
            }
        }
        if (main == null)
            main = names[0][3];
        BaseFontParameters p = new BaseFontParameters(path);
        mapper.put(main, p);
        for (int k = 0; k < names.length; ++k) {
            aliases.put(names[k][3], main);
        }
        aliases.put((String)allNames[0], main);
    }

    /** Inserts one font file into the map. The encoding
     * will be <CODE>BaseFont.CP1252</CODE> but can be
     * changed later.
     * @param file the file to insert
     * @return the number of files inserted
     * @since 5.0.5
     */
    public int insertFile(File file) {
        String name = file.getPath().toLowerCase();
        try {
            if (name.endsWith(".ttf") || name.endsWith(".otf") || name.endsWith(".afm")) {
                Object allNames[] = BaseFont.getAllFontNames(file.getPath(), BaseFont.CP1252, null);
                insertNames(allNames, file.getPath());
                return 1;
            } else if (name.endsWith(".ttc")) {
                String ttcs[] = BaseFont.enumerateTTCNames(file.getPath());
                for (int j = 0; j < ttcs.length; ++j) {
                    String nt = file.getPath() + "," + j;
                    Object allNames[] = BaseFont.getAllFontNames(nt, BaseFont.CP1252, null);
                    insertNames(allNames, nt);
                }
                return 1;
            }
        } catch (Exception e) {
        }
        return 0;
    }

    /** Inserts all the fonts recognized by iText in the
     * <CODE>directory</CODE> into the map. The encoding
     * will be <CODE>BaseFont.CP1252</CODE> but can be
     * changed later.
     * @param dir the directory to scan
     * @return the number of files processed
     */
    public int insertDirectory(String dir) {
        File file = new File(dir);
        if (!file.exists() || !file.isDirectory())
            return 0;
        File files[] = file.listFiles();
        if (files == null)
            return 0;
        int count = 0;
        for (int k = 0; k < files.length; ++k) {
            count += insertFile(files[k]);
        }
        return count;
    }

    public HashMap<String, BaseFontParameters> getMapper() {
        return mapper;
    }

    public HashMap<String, String> getAliases() {
        return aliases;
    }
}
