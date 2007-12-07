/*
 * Copyright 2004 Paulo Soares
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

package com.lowagie.text.html.simpleparser;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.html.Markup;
import com.lowagie.text.pdf.BaseFont;

/**
 *
 * @author  psoares
 */
public class FactoryProperties {
    
    private FontFactoryImp fontImp = FontFactory.getFontImp();
    
    /** Creates a new instance of FactoryProperties */
    public FactoryProperties() {
    }
    
    public Chunk createChunk(String text, ChainedProperties props) {
        Font font = getFont(props);
        float size = font.getSize();
        size /= 2;
        Chunk ck = new Chunk(text, font);
        if (props.hasProperty("sub"))
            ck.setTextRise(-size);
        else if (props.hasProperty("sup"))
            ck.setTextRise(size);
        return ck;
    }
    
    private static void setParagraphLeading(Paragraph p, String leading) {
        if (leading == null) {
            p.setLeading(0, 1.5f);
            return;
        }
        try {
            StringTokenizer tk = new StringTokenizer(leading, " ,");
            String v = tk.nextToken();
            float v1 = Float.parseFloat(v);
            if (!tk.hasMoreTokens()) {
                p.setLeading(v1, 0);
                return;
            }
            v = tk.nextToken();
            float v2 = Float.parseFloat(v);
            p.setLeading(v1, v2);
        }
        catch (Exception e) {
            p.setLeading(0, 1.5f);
        }
    }

    public static Paragraph createParagraph(HashMap props) {
        Paragraph p = new Paragraph();
        String value = (String)props.get("align");
        if (value != null) {
            if (value.equalsIgnoreCase("center"))
                p.setAlignment(Element.ALIGN_CENTER);
            else if (value.equalsIgnoreCase("right"))
                p.setAlignment(Element.ALIGN_RIGHT);
            else if (value.equalsIgnoreCase("justify"))
                p.setAlignment(Element.ALIGN_JUSTIFIED);
        }
        setParagraphLeading(p, (String)props.get("leading"));
        return p;
    }
    
    public static void createParagraph(Paragraph p, ChainedProperties props) {
        String value = props.getProperty("align");
        if (value != null) {
            if (value.equalsIgnoreCase("center"))
                p.setAlignment(Element.ALIGN_CENTER);
            else if (value.equalsIgnoreCase("right"))
                p.setAlignment(Element.ALIGN_RIGHT);
            else if (value.equalsIgnoreCase("justify"))
                p.setAlignment(Element.ALIGN_JUSTIFIED);
        }
        setParagraphLeading(p, props.getProperty("leading"));
        value = props.getProperty("before");
        if (value != null) {
            try {
                p.setSpacingBefore(Float.parseFloat(value));
            }
            catch (Exception e) {}
        }
        value = props.getProperty("after");
        if (value != null) {
            try {
                p.setSpacingAfter(Float.parseFloat(value));
            }
            catch (Exception e) {}
        }
        value = props.getProperty("extraparaspace");
        if (value != null) {
            try {
                p.setExtraParagraphSpace(Float.parseFloat(value));
            }
            catch (Exception e) {}
        }
    }

    public static Paragraph createParagraph(ChainedProperties props) {
        Paragraph p = new Paragraph();
        createParagraph(p, props);
        return p;
    }

    public static ListItem createListItem(ChainedProperties props) {
        ListItem p = new ListItem();
        createParagraph(p, props);
        return p;
    }

    public Font getFont(ChainedProperties props) {
        String face = props.getProperty("face");
        if (face != null) {
            StringTokenizer tok = new StringTokenizer(face, ",");
            while (tok.hasMoreTokens()) {
                face = tok.nextToken().trim();
                if (face.startsWith("\""))
                    face = face.substring(1);
                if (face.endsWith("\""))
                    face = face.substring(0, face.length() - 1);
                if (fontImp.isRegistered(face))
                    break;
            }
        }
        int style = 0;
        if (props.hasProperty("i"))
            style |= Font.ITALIC;
        if (props.hasProperty("b"))
            style |= Font.BOLD;
        if (props.hasProperty("u"))
            style |= Font.UNDERLINE;
        if (props.hasProperty("s"))
            style |= Font.STRIKETHRU ;
        String value = props.getProperty("size");
        float size = 12;
        if (value != null)
            size = Float.parseFloat(value);
        Color color = Markup.decodeColor(props.getProperty("color"));
        String encoding = props.getProperty("encoding");
        if (encoding == null)
            encoding = BaseFont.WINANSI;
        return fontImp.getFont(face, encoding, true, size, style, color);
    }
    
    public static void insertStyle(HashMap h) {
        String style = (String)h.get("style");
        if (style == null)
            return;
        Properties prop = Markup.parseAttributes(style);
        for (Iterator it = prop.keySet().iterator(); it.hasNext();) {
            String key = (String)it.next();
            if (key.equals(Markup.CSS_KEY_FONTFAMILY)) {
                h.put("face", prop.getProperty(key));
            }
            else if (key.equals(Markup.CSS_KEY_FONTSIZE)) {
                h.put("size", Float.toString(Markup.parseLength(prop.getProperty(key))) + "px");
            }
            else if (key.equals(Markup.CSS_KEY_FONTSTYLE)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                if (ss.equals("italic") || ss.equals("oblique"))
                    h.put("i", null);
            }
            else if (key.equals(Markup.CSS_KEY_FONTWEIGHT)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                if (ss.equals("bold") || ss.equals("700") || ss.equals("800") || ss.equals("900"))
                    h.put("b", null);
            }
            else if (key.equals(Markup.CSS_KEY_FONTWEIGHT)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                if (ss.equals("underline"))
                    h.put("u", null);
            }
            else if (key.equals(Markup.CSS_KEY_COLOR)) {
                Color c = Markup.decodeColor(prop.getProperty(key));
                if (c != null) {
                    int hh = c.getRGB();
                    String hs = Integer.toHexString(hh);
                    hs = "000000" + hs;
                    hs = "#" + hs.substring(hs.length() - 6);
                    h.put("color", hs);
                }
            }
            else if (key.equals(Markup.CSS_KEY_LINEHEIGHT)) {
                String ss = prop.getProperty(key).trim();
                float v = Markup.parseLength(prop.getProperty(key));
                if (ss.endsWith("%")) {
                    h.put("leading", "0," + (v / 100));
                }
                else {
                    h.put("leading", v + ",0");
                }
            }
            else if (key.equals(Markup.CSS_KEY_TEXTALIGN)) {
                String ss = prop.getProperty(key).trim().toLowerCase();
                h.put("align", ss);
            }
        }
    }
    
    public FontFactoryImp getFontImp() {
        return fontImp;
    }
    
    public void setFontImp(FontFactoryImp fontImp) {
        this.fontImp = fontImp;
    }

    public static HashMap followTags = new HashMap();
    static {
        followTags.put("i", "i");
        followTags.put("b", "b");
        followTags.put("u", "u");
        followTags.put("sub", "sub");
        followTags.put("sup", "sup");
        followTags.put("em", "i");
        followTags.put("strong", "b");
        followTags.put("s", "s");
        followTags.put("strike", "s");
    }
}
