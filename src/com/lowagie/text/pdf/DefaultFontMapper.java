package com.lowagie.text.pdf;

import java.awt.Font;
import com.lowagie.text.ExceptionConverter;
import java.util.HashMap;
import java.io.File;
/** Default class to map awt fonts to BaseFont.
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
    private HashMap aliases = new HashMap();
    /** Maps names to BaseFont parameters.
     */    
    private HashMap mapper = new HashMap();
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

            if (logicalName.equals("DialogInput") || (logicalName.equals("Monospaced"))) {

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

            } else if (logicalName.equals("Serif")) {

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
        String alias = (String)aliases.get(name);
        if (alias == null)
            return (BaseFontParameters)mapper.get(name);
        BaseFontParameters p = (BaseFontParameters)mapper.get(alias);
        if (p == null)
            return (BaseFontParameters)mapper.get(name);
        else
            return p;
    }
    
    protected void insertNames(String names[][], String path) {
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
        int count = 0;
        for (int k = 0; k < files.length; ++k) {
            file = files[k];
            String name = file.getPath().toLowerCase();
            try {
                if (name.endsWith(".ttf") || name.endsWith(".otf") || name.endsWith(".afm")) {
                    String names[][] = BaseFont.getFullFontName(file.getPath(), BaseFont.CP1252, null);
                    insertNames(names, file.getPath());
                    ++count;
                }
                else if (name.endsWith(".ttc")) {
                    String ttcs[] = BaseFont.enumerateTTCNames(file.getPath());
                    for (int j = 0; j < ttcs.length; ++j) {
                        String nt = file.getPath() + "," + j;
                        String names[][] = BaseFont.getFullFontName(nt, BaseFont.CP1252, null);
                        insertNames(names, nt);
                    }
                    ++count;
                }
            }
            catch (Exception e) {
            }
        }
        return count;
    }
    
    public HashMap getMapper() {
        return mapper;
    }
    
    public HashMap getAliases() {
        return aliases;
    }
}
