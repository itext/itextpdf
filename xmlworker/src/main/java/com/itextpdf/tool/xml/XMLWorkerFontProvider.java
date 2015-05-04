/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with this program; if not, see http://www.gnu.org/licenses or
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a
 * covered work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
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
	public static final String DONTLOOKFORFONTS = "\ufffc";
    protected HashMap<String, String> fontSubstitutionMap = new HashMap<String, String>();
    protected boolean useUnicode = true;

    public XMLWorkerFontProvider() {
        this(null, null);
    }

    public XMLWorkerFontProvider(String fontsPath) {
        this(fontsPath, null);
    }

    public XMLWorkerFontProvider(String fontsPath, HashMap<String, String> fontSubstitutionMap) {
        if (fontsPath == null || fontsPath.length() == 0) {
            super.registerDirectories();
        } else if (!fontsPath.equals(DONTLOOKFORFONTS)) {
            super.registerDirectory(fontsPath, true);
        }

        if (fontSubstitutionMap != null) {
            this.fontSubstitutionMap = fontSubstitutionMap;
        }
    }

    public void addFontSubstitute(String font, String substitute) {
    	fontSubstitutionMap.put(font, substitute);
    }
    
    public void setUseUnicode(boolean useUnicode) {
    	this.useUnicode = useUnicode;
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
            font = super.getFont(fontName, useUnicode ? BaseFont.IDENTITY_H : encoding, BaseFont.EMBEDDED, size, style, null);

            if (font != null) {
                baseFont = font.getBaseFont();
            }

            if (baseFont == null) {
                String substFontName = fontSubstitutionMap.get(fontName);
                if ((substFontName != null) && (substFontName.length() > 0)) {
                    font = super.getFont(substFontName, useUnicode ? BaseFont.IDENTITY_H : encoding, BaseFont.EMBEDDED, size, style, null);
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
