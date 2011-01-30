/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.text.pdf.BaseFont;

/**
 * If you are using True Type fonts, you can declare the paths of the different ttf- and ttc-files
 * to this class first and then create fonts in your code using one of the getFont method
 * without having to enter a path as parameter.
 *
 * @author  Bruno Lowagie
 */

public class FontFactoryImp implements FontProvider {

/** This is a map of postscriptfontnames of True Type fonts and the path of their ttf- or ttc-file. */
    private Hashtable<String, String> trueTypeFonts = new Hashtable<String, String>();

    private static String[] TTFamilyOrder = {
        "3", "1", "1033",
        "3", "0", "1033",
        "1", "0", "0",
        "0", "3", "0"
    };

/** This is a map of fontfamilies. */
    private Hashtable<String, ArrayList<String>> fontFamilies = new Hashtable<String, ArrayList<String>>();

/** This is the default encoding to use. */
    public String defaultEncoding = BaseFont.WINANSI;

/** This is the default value of the <VAR>embedded</VAR> variable. */
    public boolean defaultEmbedding = BaseFont.NOT_EMBEDDED;

/** Creates new FontFactory */
    public FontFactoryImp() {
        trueTypeFonts.put(FontFactory.COURIER.toLowerCase(), FontFactory.COURIER);
        trueTypeFonts.put(FontFactory.COURIER_BOLD.toLowerCase(), FontFactory.COURIER_BOLD);
        trueTypeFonts.put(FontFactory.COURIER_OBLIQUE.toLowerCase(), FontFactory.COURIER_OBLIQUE);
        trueTypeFonts.put(FontFactory.COURIER_BOLDOBLIQUE.toLowerCase(), FontFactory.COURIER_BOLDOBLIQUE);
        trueTypeFonts.put(FontFactory.HELVETICA.toLowerCase(), FontFactory.HELVETICA);
        trueTypeFonts.put(FontFactory.HELVETICA_BOLD.toLowerCase(), FontFactory.HELVETICA_BOLD);
        trueTypeFonts.put(FontFactory.HELVETICA_OBLIQUE.toLowerCase(), FontFactory.HELVETICA_OBLIQUE);
        trueTypeFonts.put(FontFactory.HELVETICA_BOLDOBLIQUE.toLowerCase(), FontFactory.HELVETICA_BOLDOBLIQUE);
        trueTypeFonts.put(FontFactory.SYMBOL.toLowerCase(), FontFactory.SYMBOL);
        trueTypeFonts.put(FontFactory.TIMES_ROMAN.toLowerCase(), FontFactory.TIMES_ROMAN);
        trueTypeFonts.put(FontFactory.TIMES_BOLD.toLowerCase(), FontFactory.TIMES_BOLD);
        trueTypeFonts.put(FontFactory.TIMES_ITALIC.toLowerCase(), FontFactory.TIMES_ITALIC);
        trueTypeFonts.put(FontFactory.TIMES_BOLDITALIC.toLowerCase(), FontFactory.TIMES_BOLDITALIC);
        trueTypeFonts.put(FontFactory.ZAPFDINGBATS.toLowerCase(), FontFactory.ZAPFDINGBATS);

        ArrayList<String> tmp;
        tmp = new ArrayList<String>();
        tmp.add(FontFactory.COURIER);
        tmp.add(FontFactory.COURIER_BOLD);
        tmp.add(FontFactory.COURIER_OBLIQUE);
        tmp.add(FontFactory.COURIER_BOLDOBLIQUE);
        fontFamilies.put(FontFactory.COURIER.toLowerCase(), tmp);
        tmp = new ArrayList<String>();
        tmp.add(FontFactory.HELVETICA);
        tmp.add(FontFactory.HELVETICA_BOLD);
        tmp.add(FontFactory.HELVETICA_OBLIQUE);
        tmp.add(FontFactory.HELVETICA_BOLDOBLIQUE);
        fontFamilies.put(FontFactory.HELVETICA.toLowerCase(), tmp);
        tmp = new ArrayList<String>();
        tmp.add(FontFactory.SYMBOL);
        fontFamilies.put(FontFactory.SYMBOL.toLowerCase(), tmp);
        tmp = new ArrayList<String>();
        tmp.add(FontFactory.TIMES_ROMAN);
        tmp.add(FontFactory.TIMES_BOLD);
        tmp.add(FontFactory.TIMES_ITALIC);
        tmp.add(FontFactory.TIMES_BOLDITALIC);
        fontFamilies.put(FontFactory.TIMES.toLowerCase(), tmp);
        fontFamilies.put(FontFactory.TIMES_ROMAN.toLowerCase(), tmp);
        tmp = new ArrayList<String>();
        tmp.add(FontFactory.ZAPFDINGBATS);
        fontFamilies.put(FontFactory.ZAPFDINGBATS.toLowerCase(), tmp);
    }

    /**
     * Constructs a <CODE>Font</CODE>-object.
     *
     * @param	fontname    the name of the font
     * @param	encoding    the encoding of the font
     * @param       embedded    true if the font is to be embedded in the PDF
     * @param	size	    the size of this font
     * @param	style	    the style of this font
     * @param	color	    the <CODE>BaseColor</CODE> of this font.
     * @return the Font constructed based on the parameters
     */
    public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
        return getFont(fontname, encoding, embedded, size, style, color, true);
    }



    /**
     * Constructs a <CODE>Font</CODE>-object.
     *
     * @param	fontname    the name of the font
     * @param	encoding    the encoding of the font
     * @param       embedded    true if the font is to be embedded in the PDF
     * @param	size	    the size of this font
     * @param	style	    the style of this font
     * @param	color	    the <CODE>BaseColor</CODE> of this font.
     * @param	cached 		true if the font comes from the cache or is added to
     * 				the cache if new, false if the font is always created new
     * @return the Font constructed based on the parameters
     */
    public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color, boolean cached) {
    	if (fontname == null) return new Font(FontFamily.UNDEFINED, size, style, color);
        String lowercasefontname = fontname.toLowerCase();
        ArrayList<String> tmp = fontFamilies.get(lowercasefontname);
        if (tmp != null) {
            // some bugs were fixed here by Daniel Marczisovszky
            int s = style == Font.UNDEFINED ? Font.NORMAL : style;
            int fs = Font.NORMAL;
            boolean found = false;
            for (String string : tmp) {
                String f = string;
                String lcf = f.toLowerCase();
                fs = Font.NORMAL;
                if (lcf.toLowerCase().indexOf("bold") != -1) fs |= Font.BOLD;
                if (lcf.toLowerCase().indexOf("italic") != -1 || lcf.toLowerCase().indexOf("oblique") != -1) fs |= Font.ITALIC;
                if ((s & Font.BOLDITALIC) == fs) {
                    fontname = f;
                    found = true;
                    break;
                }
            }
            if (style != Font.UNDEFINED && found) {
                style &= ~fs;
            }
        }
        BaseFont basefont = null;
        try {
            try {
                // the font is a type 1 font or CJK font
                basefont = BaseFont.createFont(fontname, encoding, embedded, cached, null, null, true);
            }
            catch(DocumentException de) {
            }
            if (basefont == null) {
                // the font is a true type font or an unknown font
                fontname = trueTypeFonts.get(fontname.toLowerCase());
                // the font is not registered as truetype font
                if (fontname == null) return new Font(FontFamily.UNDEFINED, size, style, color);
                // the font is registered as truetype font
                basefont = BaseFont.createFont(fontname, encoding, embedded, cached, null, null);
            }
        }
        catch(DocumentException de) {
            // this shouldn't happen
            throw new ExceptionConverter(de);
        }
        catch(IOException ioe) {
            // the font is registered as a true type font, but the path was wrong
            return new Font(FontFamily.UNDEFINED, size, style, color);
        }
        catch(NullPointerException npe) {
            // null was entered as fontname and/or encoding
            return new Font(FontFamily.UNDEFINED, size, style, color);
        }
        return new Font(basefont, size, style, color);
    }


/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param   attributes  the attributes of a <CODE>Font</CODE> object.
 * @return the Font constructed based on the attributes
 */

    public Font getFont(Properties attributes) {
        String fontname = null;
        String encoding = defaultEncoding;
        boolean embedded = defaultEmbedding;
        float size = Font.UNDEFINED;
        int style = Font.NORMAL;
        BaseColor color = null;
        String value = attributes.getProperty(HtmlTags.STYLE);
        if (value != null && value.length() > 0) {
            Properties styleAttributes = HtmlUtilities.parseAttributes(value);
            if (styleAttributes.isEmpty()) {
                attributes.put(HtmlTags.STYLE, value);
            }
            else {
                fontname = styleAttributes.getProperty(HtmlTags.FONTFAMILY);
                if (fontname != null) {
                    String tmp;
                    while (fontname.indexOf(',') != -1) {
                        tmp = fontname.substring(0, fontname.indexOf(','));
                        if (isRegistered(tmp)) {
                            fontname = tmp;
                        }
                        else {
                            fontname = fontname.substring(fontname.indexOf(',') + 1);
                        }
                    }
                }
                if ((value = styleAttributes.getProperty(HtmlTags.FONTSIZE)) != null) {
                    size = HtmlUtilities.parseLength(value);
                }
                if ((value = styleAttributes.getProperty(HtmlTags.FONTWEIGHT)) != null) {
                    style |= Font.getStyleValue(value);
                }
                if ((value = styleAttributes.getProperty(HtmlTags.FONTSTYLE)) != null) {
                    style |= Font.getStyleValue(value);
                }
                if ((value = styleAttributes.getProperty(HtmlTags.COLOR)) != null) {
                    color = HtmlUtilities.decodeColor(value);
                }
                attributes.putAll(styleAttributes);
            }
        }
        if ((value = attributes.getProperty(ElementTags.ENCODING)) != null) {
            encoding = value;
        }
        if ("true".equals(attributes.getProperty(ElementTags.EMBEDDED))) {
            embedded = true;
        }
        if ((value = attributes.getProperty(ElementTags.FONT)) != null) {
            fontname = value;
        }
        if ((value = attributes.getProperty(ElementTags.SIZE)) != null) {
            size = HtmlUtilities.parseLength(value);
        }
        if ((value = attributes.getProperty(HtmlTags.STYLE)) != null) {
            style |= Font.getStyleValue(value);
        }
        if ((value = attributes.getProperty(ElementTags.STYLE)) != null) {
            style |= Font.getStyleValue(value);
        }
        String r = attributes.getProperty(ElementTags.RED);
        String g = attributes.getProperty(ElementTags.GREEN);
        String b = attributes.getProperty(ElementTags.BLUE);
        if (r != null || g != null || b != null) {
            int red = 0;
            int green = 0;
            int blue = 0;
            if (r != null) red = Integer.parseInt(r);
            if (g != null) green = Integer.parseInt(g);
            if (b != null) blue = Integer.parseInt(b);
            color = new BaseColor(red, green, blue);
        }
        else if ((value = attributes.getProperty(ElementTags.COLOR)) != null) {
            color = HtmlUtilities.decodeColor(value);
        }
        if (fontname == null) {
            return getFont(null, encoding, embedded, size, style, color);
        }
        return getFont(fontname, encoding, embedded, size, style, color);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname, String encoding, boolean embedded, float size, int style) {
        return getFont(fontname, encoding, embedded, size, style, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 * @param	size	    the size of this font
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname, String encoding, boolean embedded, float size) {
        return getFont(fontname, encoding, embedded, size, Font.UNDEFINED, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname, String encoding, boolean embedded) {
        return getFont(fontname, encoding, embedded, Font.UNDEFINED, Font.UNDEFINED, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @param	color	    the <CODE>BaseColor</CODE> of this font.
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname, String encoding, float size, int style, BaseColor color) {
        return getFont(fontname, encoding, defaultEmbedding, size, style, color);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname, String encoding, float size, int style) {
        return getFont(fontname, encoding, defaultEmbedding, size, style, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param	size	    the size of this font
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname, String encoding, float size) {
        return getFont(fontname, encoding, defaultEmbedding, size, Font.UNDEFINED, null);
    }


/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 * @param	color	    the <CODE>BaseColor</CODE> of this font.
 * @return the Font constructed based on the parameters
 * @since 2.1.0
 */

    public Font getFont(String fontname, float size, BaseColor color) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, Font.UNDEFINED, color);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname, String encoding) {
        return getFont(fontname, encoding, defaultEmbedding, Font.UNDEFINED, Font.UNDEFINED, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @param	color	    the <CODE>BaseColor</CODE> of this font.
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname, float size, int style, BaseColor color) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, style, color);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname, float size, int style) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, style, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname, float size) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, Font.UNDEFINED, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @return the Font constructed based on the parameters
 */

    public Font getFont(String fontname) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, Font.UNDEFINED, Font.UNDEFINED, null);
    }

    /**
     * Register a font by giving explicitly the font family and name.
     * @param familyName the font family
     * @param fullName the font name
     * @param path the font path
     */
    public void registerFamily(String familyName, String fullName, String path) {
        if (path != null)
            trueTypeFonts.put(fullName, path);
        ArrayList<String> tmp = fontFamilies.get(familyName);
        if (tmp == null) {
            tmp = new ArrayList<String>();
            tmp.add(fullName);
            fontFamilies.put(familyName, tmp);
        }
        else {
            int fullNameLength = fullName.length();
            boolean inserted = false;
            for (int j = 0; j < tmp.size(); ++j) {
                if (tmp.get(j).length() >= fullNameLength) {
                    tmp.add(j, fullName);
                    inserted = true;
                    break;
                }
            }
            if (!inserted)
                tmp.add(fullName);
        }
    }

/**
 * Register a ttf- or a ttc-file.
 *
 * @param   path    the path to a ttf- or ttc-file
 */

    public void register(String path) {
        register(path, null);
    }

/**
 * Register a font file and use an alias for the font contained in it.
 *
 * @param   path    the path to a font file
 * @param   alias   the alias you want to use for the font
 */

    public void register(String path, String alias) {
        try {
            if (path.toLowerCase().endsWith(".ttf") || path.toLowerCase().endsWith(".otf") || path.toLowerCase().indexOf(".ttc,") > 0) {
                Object allNames[] = BaseFont.getAllFontNames(path, BaseFont.WINANSI, null);
                trueTypeFonts.put(((String)allNames[0]).toLowerCase(), path);
                if (alias != null) {
                    trueTypeFonts.put(alias.toLowerCase(), path);
                }
                // register all the font names with all the locales
                String[][] names = (String[][])allNames[2]; //full name
                for (String[] name : names) {
                    trueTypeFonts.put(name[3].toLowerCase(), path);
                }
                String fullName = null;
                String familyName = null;
                names = (String[][])allNames[1]; //family name
                for (int k = 0; k < TTFamilyOrder.length; k += 3) {
                    for (String[] name : names) {
                        if (TTFamilyOrder[k].equals(name[0]) && TTFamilyOrder[k + 1].equals(name[1]) && TTFamilyOrder[k + 2].equals(name[2])) {
                            familyName = name[3].toLowerCase();
                            k = TTFamilyOrder.length;
                            break;
                        }
                    }
                }
                if (familyName != null) {
                    String lastName = "";
                    names = (String[][])allNames[2]; //full name
                    for (String[] name : names) {
                        for (int k = 0; k < TTFamilyOrder.length; k += 3) {
                            if (TTFamilyOrder[k].equals(name[0]) && TTFamilyOrder[k + 1].equals(name[1]) && TTFamilyOrder[k + 2].equals(name[2])) {
                                fullName = name[3];
                                if (fullName.equals(lastName))
                                    continue;
                                lastName = fullName;
                                registerFamily(familyName, fullName, null);
                                break;
                            }
                        }
                    }
                }
            }
            else if (path.toLowerCase().endsWith(".ttc")) {
                if (alias != null)
                    System.err.println("class FontFactory: You can't define an alias for a true type collection.");
                String[] names = BaseFont.enumerateTTCNames(path);
                for (int i = 0; i < names.length; i++) {
                    register(path + "," + i);
                }
            }
            else if (path.toLowerCase().endsWith(".afm") || path.toLowerCase().endsWith(".pfm")) {
                BaseFont bf = BaseFont.createFont(path, BaseFont.CP1252, false);
                String fullName = bf.getFullFontName()[0][3].toLowerCase();
                String familyName = bf.getFamilyFontName()[0][3].toLowerCase();
                String psName = bf.getPostscriptFontName().toLowerCase();
                registerFamily(familyName, fullName, null);
                trueTypeFonts.put(psName, path);
                trueTypeFonts.put(fullName, path);
            }
        }
        catch(DocumentException de) {
            // this shouldn't happen
            throw new ExceptionConverter(de);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    /** Register all the fonts in a directory.
     * @param dir the directory
     * @return the number of fonts registered
     */
    public int registerDirectory(String dir) {
        return registerDirectory(dir, false);
    }

    /**
     * Register all the fonts in a directory and possibly its subdirectories.
     * @param dir the directory
     * @param scanSubdirectories recursively scan subdirectories if <code>true</true>
     * @return the number of fonts registered
     * @since 2.1.2
     */
    public int registerDirectory(String dir, boolean scanSubdirectories) {
        int count = 0;
        try {
            File file = new File(dir);
            if (!file.exists() || !file.isDirectory())
                return 0;
            String files[] = file.list();
            if (files == null)
                return 0;
            for (int k = 0; k < files.length; ++k) {
                try {
                    file = new File(dir, files[k]);
                    if (file.isDirectory()) {
                        if (scanSubdirectories) {
                            count += registerDirectory(file.getAbsolutePath(), true);
                        }
                    } else {
                        String name = file.getPath();
                        String suffix = name.length() < 4 ? null : name.substring(name.length() - 4).toLowerCase();
                        if (".afm".equals(suffix) || ".pfm".equals(suffix)) {
                            /* Only register Type 1 fonts with matching .pfb files */
                            File pfb = new File(name.substring(0, name.length() - 4) + ".pfb");
                            if (pfb.exists()) {
                                register(name, null);
                                ++count;
                            }
                        } else if (".ttf".equals(suffix) || ".otf".equals(suffix) || ".ttc".equals(suffix)) {
                            register(name, null);
                            ++count;
                        }
                    }
                }
                catch (Exception e) {
                    //empty on purpose
                }
            }
        }
        catch (Exception e) {
            //empty on purpose
        }
        return count;
    }

    /** Register fonts in some probable directories. It usually works in Windows,
     * Linux and Solaris.
     * @return the number of fonts registered
     */
    public int registerDirectories() {
        int count = 0;
        String windir = System.getenv("windir");
        String fileseparator = System.getProperty("file.separator");
        if (windir != null && fileseparator != null) {
        	count += registerDirectory(windir + fileseparator + "fonts");
        }
        count += registerDirectory("/usr/share/X11/fonts", true);
        count += registerDirectory("/usr/X/lib/X11/fonts", true);
        count += registerDirectory("/usr/openwin/lib/X11/fonts", true);
        count += registerDirectory("/usr/share/fonts", true);
        count += registerDirectory("/usr/X11R6/lib/X11/fonts", true);
        count += registerDirectory("/Library/Fonts");
        count += registerDirectory("/System/Library/Fonts");
        return count;
    }

/**
 * Gets a set of registered fontnames.
 * @return a set of registered fonts
 */

    public Set<String> getRegisteredFonts() {
        return trueTypeFonts.keySet();
    }

/**
 * Gets a set of registered fontnames.
 * @return a set of registered font families
 */

    public Set<String> getRegisteredFamilies() {
        return fontFamilies.keySet();
    }

/**
 * Checks if a certain font is registered.
 *
 * @param   fontname    the name of the font that has to be checked.
 * @return  true if the font is found
 */
    public boolean isRegistered(String fontname) {
        return trueTypeFonts.containsKey(fontname.toLowerCase());
    }
}
