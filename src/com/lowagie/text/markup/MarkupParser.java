/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Bruno Lowagie.
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

package com.lowagie.text.markup;

import java.awt.Color;
import java.util.Properties;
import java.util.StringTokenizer;

import com.lowagie.text.FontFactory;

/**
 * This class contains several static methods that can be used to parse markup.
 *
 * @author  blowagie
 */
public class MarkupParser {
    
/** Creates new MarkupParser */
    private MarkupParser() {
    }
    
/**
 * This method parses a String with attributes and returns a Properties object.
 *
 * @param   string   a String of this form: 'key1="value1"; key2="value2";... keyN="valueN" '
 * @return  a Properties object
 */
    
    public static Properties parseAttributes(String string) {
        Properties result = new Properties();
        if (string == null) return result;
        StringTokenizer keyValuePairs = new StringTokenizer(string, ";");
        StringTokenizer keyValuePair;
        String key;
        String value;
        while (keyValuePairs.hasMoreTokens()) {
            keyValuePair = new StringTokenizer(keyValuePairs.nextToken(), ":");
            if (keyValuePair.hasMoreTokens()) key = keyValuePair.nextToken().trim();
            else continue;
            if (keyValuePair.hasMoreTokens()) value = keyValuePair.nextToken().trim();
            else continue;
            if (value.startsWith("\"")) value = value.substring(1);
            if (value.endsWith("\"")) value = value.substring(0, value.length() - 1);
            result.setProperty(key, value);
        }
        return result;
    }
/**
 * This method parses the value of 'font' attribute and returns a Properties object.
 *
 * @param   string   a String of this form: 'style1 ... styleN size/leading font1 ... fontN'
 * @return  a Properties object
 */
    
    public static Properties parseFont(String string) {
        Properties result = new Properties();
        if (string == null) return result;
        int pos = 0;
        String value;
        string = string.trim();
        while (string.length() > 0) {
            pos = string.indexOf(" ", pos);
            if (pos == -1) {
                value = string;
                string = "";
            }
            else {
                value = string.substring(0, pos);
                string = string.substring(pos).trim();
            }
            if (value.equalsIgnoreCase("bold")) {
                result.setProperty(MarkupTags.CSS_FONTWEIGHT, MarkupTags.CSS_BOLD);
                continue;
            }
            if (value.equalsIgnoreCase("italic")) {
                result.setProperty(MarkupTags.CSS_FONTSTYLE, MarkupTags.CSS_ITALIC);
                continue;
            }
            if (value.equalsIgnoreCase("oblique")) {
                result.setProperty(MarkupTags.CSS_FONTSTYLE, MarkupTags.CSS_OBLIQUE);
                continue;
            }
            float f;
            if ((f = parseLength(value)) > 0) {
                result.setProperty(MarkupTags.CSS_FONTSIZE, String.valueOf(f) + "pt");
                int p = value.indexOf("/");
                if (p > -1 && p < value.length() - 1) {
                    result.setProperty(MarkupTags.CSS_LINEHEIGHT, String.valueOf(value.substring(p + 1)) + "pt");
                }
            }
            if (value.endsWith(",")) {
                value = value.substring(0, value.length() - 1);
                if (FontFactory.contains(value)) {
                    result.setProperty(MarkupTags.CSS_FONTFAMILY, value);
                    return result;
                }
            }
            if ("".equals(string) && FontFactory.contains(value)) {
                result.setProperty(MarkupTags.CSS_FONTFAMILY, value);
            }
        }
        return result;
    }
    
/**
 * Parses a length.
 *
 * @param   string  a length in the form of an optional + or -, followed by a number and a unit.
 * @return  a float
 */
    
    public static float parseLength(String string) {
        int pos = 0;
        int length = string.length();
        boolean ok = true;
        while (ok && pos < length) {
            switch(string.charAt(pos)) {
                case '+':
                case '-':
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '.':
                    pos++;
                    break;
                    default:
                        ok = false;
            }
        }
        if (pos == 0) return 0f;
        if (pos == length) return Float.valueOf(string + "f").floatValue();
        float f = Float.valueOf(string.substring(0, pos) + "f").floatValue();
        string = string.substring(pos);
        // inches
        if (string.startsWith("in")) {
            return f * 72f;
        }
        // centimeters
        if (string.startsWith("cm")) {
            return (f / 2.54f) * 72f;
        }
        // millimeters
        if (string.startsWith("mm")) {
            return (f / 25.4f) * 72f;
        }
        // picas
        if (string.startsWith("pc")) {
            return f * 12f;
        }
        // default: we assume the length was measured in points
        return f;
    }
    
/**
 * Converts a <CODE>Color</CODE> into a HTML representation of this <CODE>Color</CODE>.
 *
 * @param	color	the <CODE>Color</CODE> that has to be converted.
 * @return	the HTML representation of this <COLOR>Color</COLOR>
 */
    
    public static Color decodeColor(String color) {
        int red = 0;
        int green = 0;
        int blue = 0;
        try {
            red = Integer.parseInt(color.substring(1, 3), 16);
            green = Integer.parseInt(color.substring(3, 5), 16);
            blue = Integer.parseInt(color.substring(5), 16);
        }
        catch(Exception sioobe) {
            // empty on purpose
        }
        return new Color(red, green, blue);
    }
}