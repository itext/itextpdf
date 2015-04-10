/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part4.helper;

import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * Stores the font name and font size of a text item.
 */
public class TextStyle {
    /** The name of the font. */
    String fontName;
    /** The size of the font. */
    float fontSize;

    /**
     * Creates a TextStyle object.
     * @param fontName the name of the font
     * @param fontSize the size of the font
     */
    public TextStyle(String fontName, float fontSize) {
        this.fontName = fontName;
        this.fontSize = fontSize;
    }

    /**
     * Creates a TextStyle object by getting the font name and font size
     * from a TextRenderInfo object.
     * @param textRenderInfo Object that contains info about a text snippet
     */
    public TextStyle(TextRenderInfo textRenderInfo) {
        String font = textRenderInfo.getFont().getFullFontName()[0][3];
        if (font.contains("+"))
            font = font.substring(font.indexOf("+") + 1, font.length());
        if (font.contains("-"))
            font = font.substring(0, font.indexOf("-"));
        this.fontName = font;
        this.fontSize = textRenderInfo.getAscentLine().getStartPoint().get(1) - textRenderInfo.getDescentLine().getStartPoint().get(1);
    }

    @Override
    public int hashCode() {
        return fontName.hashCode() ^ Math.round(fontSize * 10);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TextStyle) {
            TextStyle ts = (TextStyle) obj;
            return fontName.equals(ts.fontName) && Math.abs(fontSize - ts.fontSize) < 0.05;
        }
        return false;
    }
}
