package com.itextpdf.tool.xml;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactoryImp;
import com.itextpdf.text.pdf.*;

import java.nio.charset.UnsupportedCharsetException;
import java.util.HashMap;

/**
 *
 */
public class XMLWorkerFontProvider extends FontFactoryImp {
    private HashMap<String, String> fontSubstitutionMap = new HashMap<String, String>();

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

        Font unicodeFont = getUnicodeFont(fontname, encoding, size, style);
        return unicodeFont;
    }

    private Font getUnicodeFont(String fontName, String encoding, float size, int style) {
        Font font = null;
        try {
            BaseFont baseFont = null;
            font = super.getFont(fontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, size, style, null);

            if (font != null) {
                baseFont = font.getBaseFont();
            }

            if (baseFont == null) {
                String substFontName = fontSubstitutionMap.get(fontName);
                if ((substFontName != null) && (substFontName.length() > 0)) {
                    font = super.getFont(substFontName, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, size, style, null);
                }
            }
        } catch (UnsupportedCharsetException uce) {
            BaseFont baseFont = null;
            font = super.getFont(fontName, encoding, BaseFont.EMBEDDED, size, style, null);

            if (font != null) {
                baseFont = font.getBaseFont();
            }
            if (baseFont == null) {
                String substFontName = fontSubstitutionMap.get(fontName);
                if ((substFontName != null) && (substFontName.length() > 0)) {
                    font = super.getFont(substFontName, encoding, BaseFont.EMBEDDED, size, style, null);
                }
            }
        }

        return font;
    }
}
