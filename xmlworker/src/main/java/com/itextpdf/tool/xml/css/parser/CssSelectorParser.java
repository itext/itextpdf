/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Pavel Alay, Bruno Lowagie, et al.
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
package com.itextpdf.tool.xml.css.parser;

import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CssSelectorItem;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CssSelectorParser {

    private static final String selectorPatternString =
            "(\\*)|([_a-zA-Z][\\w-]*)|(\\.[_a-zA-Z][\\w-]*)|(#[_a-z][\\w-]*)|(\\[[_a-zA-Z][\\w-]*(([~^$*|])?=((\"[\\w-]+\")|([\\w-]+)))?\\])|(:[\\w()-]*)|( )|(\\+)|(>)|(~)";

    private static final Pattern selectorPattern = Pattern.compile(selectorPatternString);

    private static final int a = 1 << 16;
    private static final int b = 1 << 8;
    private static final int c = 1;

    public static List<CssSelectorItem> createCssSelector(String selector) {
        List<CssSelectorItem> cssSelectorItems = new ArrayList<CssSelectorItem>();
        Matcher itemMatcher = selectorPattern.matcher(selector);
        boolean isTagSelector = false;
        int crc = 0;
        while(itemMatcher.find()) {
            String selectorItem = itemMatcher.group(0);
            crc += selectorItem.length();
            switch (selectorItem.charAt(0)) {
                case '#':
                    cssSelectorItems.add(new CssIdSelector(selectorItem.substring(1)));
                    break;
                case '.':
                    cssSelectorItems.add(new CssClassSelector(selectorItem.substring(1)));
                    break;
                case '[':
                    cssSelectorItems.add(new CssAttributeSelector(selectorItem));
                    break;
                case ':':
                    cssSelectorItems.add(new CssPseudoSelector(selectorItem));
                    break;
                case ' ':
                case '+':
                case '>':
                case '~':
                    if (cssSelectorItems.size() == 0) return null;
                    CssSelectorItem lastItem = cssSelectorItems.get(cssSelectorItems.size() - 1);
                    CssSelectorItem currItem = new CssSeparatorSelector(selectorItem.charAt(0));
                    if (lastItem instanceof CssSeparatorSelector) {
                        if (selectorItem.charAt(0) == ' ')
                            break;
                        else if (lastItem.getSeparator() == ' ')
                            cssSelectorItems.set(cssSelectorItems.size() - 1, currItem);
                        else
                            return null;
                    } else {
                        cssSelectorItems.add(currItem);
                        isTagSelector = false;
                    }
                    break;
                default: //and case '*':
                    if (isTagSelector)
                        return null;
                    isTagSelector = true;
                    cssSelectorItems.add(new CssTagSelector(selectorItem));
                    break;
            }
        }

        if (selector.length() == 0 || selector.length() != crc)
            return null;
        return cssSelectorItems;
    }

    static class CssTagSelector implements CssSelectorItem {
        private String t;
        private boolean isUniversal;

        CssTagSelector(String t) {
            this.t = t;
            isUniversal = this.t.equals("*") ? true : false;
        }

        public boolean matches(Tag t){
            return isUniversal || this.t.equals(t.getName());
        }

        public char getSeparator() {
            return 0;
        }

        public int getSpecificity() {
            if (isUniversal) return 0;
            return CssSelectorParser.c;
        }

        @Override
        public String toString() {
            return t;
        }
    }

    static class CssClassSelector implements CssSelectorItem {
        private String className;

        CssClassSelector(String className) {
            this.className = className;
        }

        public boolean matches(Tag t){
            String classAttr = t.getAttributes().get("class");
            if (classAttr == null || classAttr.length() == 0)
                return false;
            String[] classNames = classAttr.split(" ");
            for (String currClassName: classNames)
                if (this.className.equals(currClassName.trim()))
                    return true;
            return false;
        }

        public char getSeparator() {
            return 0;
        }

        public int getSpecificity() {
            return CssSelectorParser.b;
        }

        @Override
        public String toString() {
            return "." + className;
        }
    }

    static class CssIdSelector implements CssSelectorItem {
        private String id;

        CssIdSelector(String id) {
            this.id = id;
        }

        public boolean matches(Tag t){
            String id = t.getAttributes().get("id");
            return id != null && this.id.equals(id.trim());
        }

        public char getSeparator() {
            return 0;
        }

        public int getSpecificity() {
            return CssSelectorParser.a;
        }

        @Override
        public String toString() {
            return "#" + id;
        }
    }

    static class CssAttributeSelector implements CssSelectorItem {
        private String property;
        private char matchSymbol = 0;
        private String value = null;

        CssAttributeSelector(String attrSelector) {
            int indexOfEqual = attrSelector.indexOf('=');
            if (indexOfEqual == -1) {
                property = attrSelector.substring(1, attrSelector.length() - 1);
            } else {
                if (attrSelector.charAt(indexOfEqual + 1) == '"')
                    value = attrSelector.substring(indexOfEqual + 2, attrSelector.length() - 2);
                else
                    value = attrSelector.substring(indexOfEqual + 1, attrSelector.length() - 1);
                matchSymbol = attrSelector.charAt(indexOfEqual - 1);
                if ("~^$*|".indexOf(matchSymbol) == -1) {
                    matchSymbol = 0;
                    property = attrSelector.substring(1, indexOfEqual);
                } else {
                    property = attrSelector.substring(1, indexOfEqual - 1);
                }
            }
        }

        public char getSeparator() {
            return 0;
        }

        public boolean matches(Tag t) {
            if (t == null)
                return false;
            String attrValue = t.getAttributes().get(property);
            if (attrValue == null) return false;
            if (value == null) return true;

            switch (matchSymbol) {
                case '|':
                    String pattern = String.format("^%s-?", value);
                    if (Pattern.compile(pattern).matcher(attrValue).find())
                        return true;
                    break;
                case '^':
                    if (attrValue.startsWith(value))
                        return true;
                    break;
                case '$':
                    if (attrValue.endsWith(value))
                        return true;
                    break;
                case '~':
                    pattern = String.format("(^%s\\s+)|(\\s+%s\\s+)|(\\s+%s$)", value, value, value);
                    if (Pattern.compile(pattern).matcher(attrValue).find())
                        return true;
                    break;
                case 0:
                    if (attrValue.equals(value))
                        return true;
                    break;
                case '*':
                    if (attrValue.contains(value))
                        return true;
                    break;
            }
            return false;
        }

        public int getSpecificity() {
            return CssSelectorParser.b;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append('[').append(property);
            if (matchSymbol != 0)
                buf.append(matchSymbol);
            if (value != null)
                buf.append('=').append('"').append(value).append('"');
            buf.append(']');
            return buf.toString();
        }
    }


    static class CssPseudoSelector implements CssSelectorItem {
        private String selector;

        CssPseudoSelector(String selector) {
            this.selector = selector;
        }

        public boolean matches(Tag t) {
            return false;
        }

        public char getSeparator() {
            return 0;
        }

        public int getSpecificity() {
            return 0;
        }

        @Override
        public String toString() {
            return selector;
        }
    }

    static class CssSeparatorSelector implements CssSelectorItem {
        private char separator;

        CssSeparatorSelector(char separator) {
            this.separator = separator;
        }

        public char getSeparator() {
            return separator;
        }

        public boolean matches(Tag t){
            return false;
        }

        public int getSpecificity() {
            return 0;
        }
        @Override
        public String toString() {
            return String.valueOf(separator);
        }
    }
}
