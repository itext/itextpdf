package com.itextpdf.tool.xml;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactoryImp;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class XMLWorkerFontProvider extends FontFactoryImp {
    private final Map<String, BaseFont> fonts = new ConcurrentHashMap<String, BaseFont>();
    private final Map<String, BaseFont> unicodeFonts = new ConcurrentHashMap<String, BaseFont>();
    private HashMap<String, String> fontNamesMap = new HashMap<String, String>();
    private HashMap<String, String> fontSubstitutionMap = new HashMap<String, String>();
    private ArrayList<File> filesToDelete = new ArrayList<File>();

    public XMLWorkerFontProvider() {
        this(null, null);
    }

    public XMLWorkerFontProvider(String fontsPath) {
        this(fontsPath, null);
    }

    public XMLWorkerFontProvider(String fontsPath, HashMap<String, String> fontSubstitutionMap) {
        if ((fontsPath != null) && (fontsPath.length() > 0)) {
            super.registerDirectory(fontsPath, true);
        } else {
            super.registerDirectories();
        }

        if (fontSubstitutionMap != null) {
            this.fontSubstitutionMap = fontSubstitutionMap;
        }
    }

    @Override
    public Font getFont(final String fontname, final String encoding, final boolean embedded, final float size, final int style, final BaseColor color) {
        Font font = getFont(fontname, encoding, size, style);
        font.setColor(color);
        return font;
    }

    @Override
    public Font getFont(final String fontname, String encoding, float size, final int style) {
        if (fontname == null) {
            return new Font(Font.FontFamily.UNDEFINED, size, style);
        }

        String resolvedFontName = resolveFontName(fontname);
        BaseFont unicodeBaseFont = getUnicodeBaseFont(resolvedFontName, encoding, size, style);
        String unicodeFontName = null;
        if (unicodeBaseFont != null) {
            unicodeFontName = unicodeBaseFont.getFullFontName()[0][3];
        }
        if ((unicodeFontName != null) && (unicodeFonts.get(unicodeFontName) == null)) {
            unicodeFonts.put(unicodeFontName, unicodeBaseFont);
        }
        return new Font(unicodeBaseFont, size, style);
    }

    public BaseFont getBaseFont(final String fontName, String encoding, final float size, final int style) {
        String fontNameAndStyle = fontName;
        String styleString = getStyleString(style);
        if ((styleString != null) && (styleString.length() > 0))
            fontNameAndStyle = fontNameAndStyle + " " + styleString;
        fontNameAndStyle = fontNameAndStyle.toLowerCase();
        BaseFont baseFont = fonts.get(fontNameAndStyle);
        if (baseFont == null) {
            try {
                baseFont = createBaseFont(fontName, encoding, size, style);
                if (baseFont == null) {
                    String substFontName = fontSubstitutionMap.get(fontName);
                    if ((substFontName != null) && (substFontName.length() > 0)) {
                        baseFont = createBaseFont(substFontName,encoding, size, style);
                    }
                }
                if (baseFont != null && unicodeFonts.get(fontNameAndStyle) == null) {
                    unicodeFonts.put(fontNameAndStyle, baseFont);
                }
            } catch (UnsupportedCharsetException uce) {
                return null;
            }
        }

        return baseFont;
    }

    public void dispose() {
        this.finalize();
    }

    protected void finalize() {
        for (File f : filesToDelete) {
            f.delete();
        }
        filesToDelete.clear();
    }

    private BaseFont getUnicodeBaseFont(String fontName, String encoding, float size, int style) {
        String fontNameAndStyle = fontName;
        String styleString = getStyleString(style);
        if ((styleString != null) && (styleString.length() > 0))
            fontNameAndStyle = fontNameAndStyle + " " + styleString;
        fontNameAndStyle = fontNameAndStyle.toLowerCase();
        BaseFont baseFont = unicodeFonts.get(fontNameAndStyle);
        if (baseFont == null) {
            try {
                baseFont = createBaseFont(fontName, BaseFont.IDENTITY_H, size, style);
                if (baseFont == null) {
                    String substFontName = fontSubstitutionMap.get(fontName);
                    if ((substFontName != null) && (substFontName.length() > 0)) {
                        baseFont = createBaseFont(substFontName, BaseFont.IDENTITY_H, size, style);
                    }
                }
                if (baseFont != null && unicodeFonts.get(fontNameAndStyle) == null) {
                    unicodeFonts.put(fontNameAndStyle, baseFont);
                }
            } catch (UnsupportedCharsetException uce) {
                baseFont = createBaseFont(fontName, encoding, size, style);
                if (baseFont == null) {
                    String substFontName = fontSubstitutionMap.get(fontName);
                    if ((substFontName != null) && (substFontName.length() > 0)) {
                        baseFont = createBaseFont(substFontName, encoding, size, style);
                    }
                }
            }
        }

        return baseFont;
    }

    private BaseFont createBaseFont(String fontName, String encoding, float size, int style) {
        BaseFont baseFont = null;
        Font f = super.getFont(fontName, encoding, BaseFont.EMBEDDED, size, style, null);
        if (f != null) {
            baseFont = f.getBaseFont();
        }
        return baseFont;
    }

    private String resolveFontName(String fontname) {
        String resolved = fontNamesMap.get(fontname);
        if (resolved != null && resolved.length() > 0)
            return resolved;
        else
            return fontname;
    }

    private static boolean isFontFacesEqual(final String s1, final String s2) {
        String trimmedS1 = s1.toLowerCase().trim();
        String trimmedS2 = s2.toLowerCase().trim();
        int length = trimmedS1.length();
        if (length > trimmedS2.length()) {
            length = trimmedS2.length();
        }
        if (length > 5) {
            length = 5;
        }
        return trimmedS1.substring(0,length).equals(trimmedS2.substring(0,length));
    }

    /**
     * Converts the Integer style to a String which could be a part of BaseFont family font name.
     * Also converts the Integer style to Font.Normal, to avoid having double styles being applied.
     *
     * @param style Integer. Either Font.BOLD, Font.ITALIC or Font.BOLDITALIC is converted.
     * @return styleName which could be a part of BaseFont family font name.
     */
    private String getStyleString(final Integer style) {
        String styleName = "";
        if (style != Font.UNDEFINED) {
            if ((style & Font.BOLD) != 0 && (style & Font.ITALIC) != 0) {
                styleName = Font.FontStyle.BOLD.getValue() + Font.FontStyle.ITALIC.getValue();
            } else if ((style & Font.BOLD) != 0) {
                styleName = Font.FontStyle.BOLD.getValue();
            } else if ((style & Font.ITALIC) != 0) {
                styleName = Font.FontStyle.BOLD.getValue();
            }
        }
        return styleName;
    }
}
