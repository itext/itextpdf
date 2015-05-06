/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
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
package com.itextpdf.tool.xml.css;

import com.itextpdf.text.pdf.events.IndexEvents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CssRule implements Comparable<CssRule> {

    private CssSelector selector;
    private Map<String, String> normalDeclarations;
    private Map<String, String> importantDeclarations;
    private static final Pattern importantMatcher = Pattern.compile(".*!\\s*important$");

    public CssRule(List<CssSelectorItem> selector, Map<String, String> declarations) {
        this.selector = new CssSelector(selector);
        this.normalDeclarations = declarations;
        this.importantDeclarations = new HashMap<String, String>();

        for(Map.Entry<String,String> declaration: normalDeclarations.entrySet()) {
            int exclIndex = declaration.getValue().indexOf('!');
            if (exclIndex > 0 && importantMatcher.matcher(declaration.getValue()).matches()) {
                importantDeclarations.put(declaration.getKey(), declaration.getValue().substring(0, exclIndex).trim());
            }
        }
        //remove important declarations from normal declarations
        for (String key: importantDeclarations.keySet())
            normalDeclarations.remove(key);
    }

    public CssSelector getSelector() {
        return selector;
    }

    public Map<String, String> getNormalDeclarations() {
        return normalDeclarations;
    }

    public Map<String, String> getImportantDeclarations() {
        return importantDeclarations;
    }

    @Override
    public String toString() {
        return String.format("%s { count: %d } #spec:%d",
                selector.toString(), normalDeclarations.size() + importantDeclarations.size(), selector.calculateSpecifity());
    }

    public int compareTo(CssRule o) {
        return this.selector.calculateSpecifity() - o.selector.calculateSpecifity();
    }
}
