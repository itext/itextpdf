/*
 * $Id$
 * $Name$
 *
 * Copyright 2002 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text;

import java.awt.Color;
import java.io.IOException;
import java.util.Hashtable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Enumeration;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.markup.MarkupTags;
import com.lowagie.text.markup.MarkupParser;

/**
 * If you are using True Type fonts, you can declare the paths of the different ttf- and ttc-files
 * to this static class first and then create fonts in your code using one of the static getFont-method
 * without having to enter a path as parameter.
 *
 * @author  Bruno Lowagie
 */

public class FontFactory extends java.lang.Object {
    
/** This is a possible value of a base 14 type 1 font */
    public static final String COURIER = BaseFont.COURIER;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String COURIER_BOLD = BaseFont.COURIER_BOLD;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String COURIER_OBLIQUE = BaseFont.COURIER_OBLIQUE;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String COURIER_BOLDOBLIQUE = BaseFont.COURIER_BOLDOBLIQUE;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA = BaseFont.HELVETICA;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA_BOLD = BaseFont.HELVETICA_BOLD;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA_OBLIQUE = BaseFont.HELVETICA_OBLIQUE;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA_BOLDOBLIQUE = BaseFont.HELVETICA_BOLDOBLIQUE;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String SYMBOL = BaseFont.SYMBOL;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_NEW_ROMAN = "Times New Roman";
    
/** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_ROMAN = BaseFont.TIMES_ROMAN;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_BOLD = BaseFont.TIMES_BOLD;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_ITALIC = BaseFont.TIMES_ITALIC;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_BOLDITALIC = BaseFont.TIMES_BOLDITALIC;
    
/** This is a possible value of a base 14 type 1 font */
    public static final String ZAPFDINGBATS = BaseFont.ZAPFDINGBATS;
    
/** This is a map of postscriptfontnames of True Type fonts and the path of their ttf- or ttc-file. */
    private static Properties trueTypeFonts = new Properties();
    
    static {
        trueTypeFonts.setProperty(COURIER, COURIER);
        trueTypeFonts.setProperty(COURIER_BOLD, COURIER_BOLD);
        trueTypeFonts.setProperty(COURIER_OBLIQUE, COURIER_OBLIQUE);
        trueTypeFonts.setProperty(COURIER_BOLDOBLIQUE, COURIER_BOLDOBLIQUE);
        trueTypeFonts.setProperty(HELVETICA, HELVETICA);
        trueTypeFonts.setProperty(HELVETICA_BOLD, HELVETICA_BOLD);
        trueTypeFonts.setProperty(HELVETICA_OBLIQUE, HELVETICA_OBLIQUE);
        trueTypeFonts.setProperty(HELVETICA_BOLDOBLIQUE, HELVETICA_BOLDOBLIQUE);
        trueTypeFonts.setProperty(SYMBOL, SYMBOL);
        trueTypeFonts.setProperty(TIMES_ROMAN, TIMES_ROMAN);
        trueTypeFonts.setProperty(TIMES_BOLD, TIMES_BOLD);
        trueTypeFonts.setProperty(TIMES_ITALIC, TIMES_ITALIC);
        trueTypeFonts.setProperty(TIMES_BOLDITALIC, TIMES_BOLDITALIC);
        trueTypeFonts.setProperty(ZAPFDINGBATS, ZAPFDINGBATS);
    }
    
/** This is a map of fontfamilies. */
    private static Hashtable fontFamilies = new Hashtable();
    
    static {
        HashSet tmp;
        tmp = new HashSet();
        tmp.add(COURIER);
        tmp.add(COURIER_BOLD);
        tmp.add(COURIER_OBLIQUE);
        tmp.add(COURIER_BOLDOBLIQUE);
        fontFamilies.put(COURIER, tmp);
        tmp = new HashSet();
        tmp.add(HELVETICA);
        tmp.add(HELVETICA_BOLD);
        tmp.add(HELVETICA_OBLIQUE);
        tmp.add(HELVETICA_BOLDOBLIQUE);
        fontFamilies.put(HELVETICA, tmp);
        tmp = new HashSet();
        tmp.add(SYMBOL);
        fontFamilies.put(SYMBOL, tmp);
        tmp = new HashSet();
        tmp.add(TIMES_ROMAN);
        tmp.add(TIMES_BOLD);
        tmp.add(TIMES_ITALIC);
        tmp.add(TIMES_BOLDITALIC);
        fontFamilies.put(TIMES_NEW_ROMAN, tmp);
        tmp = new HashSet();
        tmp.add(ZAPFDINGBATS);
        fontFamilies.put(ZAPFDINGBATS, tmp);
    }
    
    
/** This is the default encoding to use. */
    public static String defaultEncoding = BaseFont.WINANSI;
    
/** This is the default value of the <VAR>embedded</VAR> variable. */
    public static boolean defaultEmbedding = BaseFont.NOT_EMBEDDED;
    
/** Creates new FontFactory */
    private FontFactory() {
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @param	color	    the <CODE>Color</CODE> of this font.
 */
    
    public static Font getFont(String fontname, String encoding, boolean embedded, float size, int style, Color color) {
        if (fontname == null) return new Font(Font.UNDEFINED, size, style, color);
        HashSet tmp = (HashSet) fontFamilies.get(fontname);
        if (tmp != null) {
            int s = style == Font.UNDEFINED ? Font.NORMAL : style;
            for (Iterator i = tmp.iterator(); i.hasNext(); ) {
                String f = (String) i.next();
                int fs = Font.NORMAL;
                if (f.toLowerCase().indexOf("bold") != -1) fs |= Font.BOLD;
                if (f.toLowerCase().indexOf("italic") != -1 || f.toLowerCase().indexOf("oblique") != -1) fs |= Font.ITALIC;
                if ((s & Font.BOLDITALIC) == fs) {
                    fontname = f;
                    break;
                }
            }
        }
        BaseFont basefont = null;
        try {
            try {
                // the font is a type 1 font or CJK font
                basefont = BaseFont.createFont(fontname, encoding, embedded);
            }
            catch(DocumentException de) {
                // the font is a true type font or an unknown font
                fontname = trueTypeFonts.getProperty(fontname);
                // the font is not registered as truetype font
                if (fontname == null) return new Font(Font.UNDEFINED, size, style, color);
                // the font is registered as truetype font
                basefont = BaseFont.createFont(fontname, encoding, embedded);
            }
        }
        catch(DocumentException de) {
            // this shouldn't happen
            throw new ExceptionConverter(de);
        }
        catch(IOException ioe) {
            // the font is registered as a true type font, but the path was wrong
            return new Font(Font.UNDEFINED, size, style, color);
        }
        catch(NullPointerException npe) {
            // null was entered as fontname and/or encoding
            return new Font(Font.UNDEFINED, size, style, color);
        }
        return new Font(basefont, size, style, color);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param   attributes  the attributes of a <CODE>Font</CODE> object.
 */
    
    public static Font getFont(Properties attributes) {
        
        String fontname = null;
        String encoding = defaultEncoding;
        boolean embedded = defaultEmbedding;
        float size = Font.UNDEFINED;
        int style = Font.NORMAL;
        Color color = null;
        String value = (String) attributes.remove(MarkupTags.STYLE);
        if (value != null && value.length() > 0) {
            Properties styleAttributes = MarkupParser.parseAttributes(value);
            fontname = (String)styleAttributes.remove(MarkupTags.CSS_FONTFAMILY);
            if (fontname != null) {
                String tmp;
                while (fontname.indexOf(",") != -1) {
                    tmp = fontname.substring(0, fontname.indexOf(","));
                    if (isRegistered(tmp)) {
                        fontname = tmp;
                    }
                    else {
                        fontname = fontname.substring(fontname.indexOf(",") + 1);
                    }
                }
            }
            if ((value = (String)styleAttributes.remove(MarkupTags.CSS_FONTSIZE)) != null) {
                size = MarkupParser.parseLength(value);
            }
            if ((value = (String)styleAttributes.remove(MarkupTags.CSS_FONTWEIGHT)) != null) {
                style |= Font.getStyleValue(value);
            }
            if ((value = (String)styleAttributes.remove(MarkupTags.CSS_FONTSTYLE)) != null) {
                style |= Font.getStyleValue(value);
            }
            if ((value = (String)styleAttributes.remove(MarkupTags.CSS_COLOR)) != null) {
                color = MarkupParser.decodeColor(value);
            }
            attributes.putAll(styleAttributes);
            for (Enumeration e = styleAttributes.keys(); e.hasMoreElements();) {
                Object o = e.nextElement();
                attributes.put(o, styleAttributes.get(o));
            }
        }
        if ((value = (String)attributes.remove(ElementTags.ENCODING)) != null) {
            encoding = value;
        }
        if ("false".equals((String) attributes.remove(ElementTags.EMBEDDED))) {
            embedded = false;
        }
        if ((value = (String)attributes.remove(ElementTags.FONT)) != null) {
            fontname = value;
        }
        if ((value = (String)attributes.remove(ElementTags.SIZE)) != null) {
            size = Float.valueOf(value + "f").floatValue();
        }
        if ((value = (String)attributes.remove(ElementTags.STYLE)) != null) {
            style |= Font.getStyleValue(value);
        }
        String r = (String)attributes.remove(ElementTags.RED);
        String g = (String)attributes.remove(ElementTags.GREEN);
        String b = (String)attributes.remove(ElementTags.BLUE);
        if (r != null || g != null || b != null) {
            int red = 0;
            int green = 0;
            int blue = 0;
            if (r != null) red = Integer.parseInt(r);
            if (g != null) green = Integer.parseInt(g);
            if (b != null) blue = Integer.parseInt(b);
            color = new Color(red, green, blue);
        }
        else if ((value = (String)attributes.remove(ElementTags.COLOR)) != null) {
            color = MarkupParser.decodeColor(value);
        }
        if (fontname == null) return getFont(null, encoding, embedded, size, style, color);
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
 */
    
    public static Font getFont(String fontname, String encoding, boolean embedded, float size, int style) {
        return getFont(fontname, encoding, embedded, size, style, null);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 * @param	size	    the size of this font
 */
    
    public static Font getFont(String fontname, String encoding, boolean embedded, float size) {
        return getFont(fontname, encoding, embedded, size, Font.UNDEFINED, null);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 */
    
    public static Font getFont(String fontname, String encoding, boolean embedded) {
        return getFont(fontname, encoding, embedded, Font.UNDEFINED, Font.UNDEFINED, null);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @param	color	    the <CODE>Color</CODE> of this font.
 */
    
    public static Font getFont(String fontname, String encoding, float size, int style, Color color) {
        return getFont(fontname, encoding, defaultEmbedding, size, style, color);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 */
    
    public static Font getFont(String fontname, String encoding, float size, int style) {
        return getFont(fontname, encoding, defaultEmbedding, size, style, null);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param	size	    the size of this font
 */
    
    public static Font getFont(String fontname, String encoding, float size) {
        return getFont(fontname, encoding, defaultEmbedding, size, Font.UNDEFINED, null);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 */
    
    public static Font getFont(String fontname, String encoding) {
        return getFont(fontname, encoding, defaultEmbedding, Font.UNDEFINED, Font.UNDEFINED, null);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @param	color	    the <CODE>Color</CODE> of this font.
 */
    
    public static Font getFont(String fontname, float size, int style, Color color) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, style, color);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 */
    
    public static Font getFont(String fontname, float size, int style) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, style, null);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 */
    
    public static Font getFont(String fontname, float size) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, Font.UNDEFINED, null);
    }
    
/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 */
    
    public static Font getFont(String fontname) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, Font.UNDEFINED, Font.UNDEFINED, null);
    }
    
/**
 * Register a ttf- or a ttc-file.
 *
 * @param   path    the path to a ttf- or ttc-file
 */
    
    public static void register(String path) {
        register(path, null);
    }
    
/**
 * Register a ttf- or a ttc-file and use an alias for the font contained in the ttf-file.
 *
 * @param   path    the path to a ttf- or ttc-file
 * @param   alias   the alias you want to use for the font
 */
    
    public static void register(String path, String alias) {
        try {
            if (path.toLowerCase().endsWith(".ttf")) {
                BaseFont bf = BaseFont.createFont(path, BaseFont.WINANSI, false, false, null, null);
                trueTypeFonts.setProperty(bf.getPostscriptFontName(), path);
                if (alias != null) {
                    trueTypeFonts.setProperty(alias, path);
                }
                String fullName = null;
                String familyName = null;
                String[][] names = bf.getFullFontName();
                for (int i = 0; i < names.length; i++) {
                    if ("0".equals(names[i][2])) {
                        fullName = names[i][3];
                        trueTypeFonts.setProperty(fullName, path);
                        break;
                    }
                }
                if (fullName != null) {
                    names = bf.getFamilyFontName();
                    for (int i = 0; i < names.length; i++) {
                        if ("0".equals(names[i][2])) {
                           familyName = names[i][3];
                           HashSet tmp = (HashSet) fontFamilies.get(familyName);
                           if (tmp == null) {
                               tmp = new HashSet();
                           }
                           tmp.add(fullName);
                           fontFamilies.put(familyName, tmp);
                           break;
                        }
                    }
                }
            }
            else if (path.toLowerCase().endsWith(".ttc")) {
                String[] names = BaseFont.enumerateTTCNames(path);
                for (int i = 0; i < names.length; i++) {
                    trueTypeFonts.setProperty(names[i], path + "," + (i + 1));
                }
                if (alias != null) {
                    System.err.println("class FontFactory: You can't define an alias for a true type collection.");
                }
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
    
/**
 * Gets a set of registered fontnames.
 */
    
    public static Set getRegisteredFonts() {
        return Chunk.getKeySet(trueTypeFonts);
    }
    
/**
 * Gets a set of registered fontnames.
 */
    
    public static Set getRegisteredFamilies() {
        return Chunk.getKeySet(fontFamilies);
    }
    
/**
 * Gets a set of registered fontnames.
 */
    
    public static boolean contains(String fontname) {
        return trueTypeFonts.containsKey(fontname);
    }
    
/**
 * Checks if a certain font is registered.
 *
 * @param   fontname    the name of the font that has to be checked.
 * @return  true if the font is found
 */
    
    public static boolean isRegistered(String fontname) {
        String tmp;
        for (Enumeration e = trueTypeFonts.propertyNames(); e.hasMoreElements(); ) {
            tmp = (String) e.nextElement();
            if (fontname.equalsIgnoreCase(tmp)) {
                return true;
            }
        }
        return false;
    }
}