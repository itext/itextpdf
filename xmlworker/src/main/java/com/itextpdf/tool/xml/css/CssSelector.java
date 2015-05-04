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

import com.itextpdf.tool.xml.Tag;

import java.util.List;
import java.util.Stack;

public class CssSelector {
    private List<CssSelectorItem> selectorItems;

    public CssSelector(List<CssSelectorItem> selector) {
        this.selectorItems = selector;
    }

    public boolean matches(Tag t) {
        return matches(t, selectorItems.size() - 1);
    }

    private boolean matches(Tag t, int index) {
        if (t == null)
            return false;
        Stack<CssSelectorItem> currentSelector = new Stack<CssSelectorItem> ();
        for (; index >= 0; index--) {
            if (selectorItems.get(index).getSeparator() != 0)
                break;
            else
                currentSelector.push(selectorItems.get(index));
        }
        while (!currentSelector.empty())
            if (!currentSelector.pop().matches(t))
                return false;
        if (index == -1)
            return true;
        else
        {
            char separator = selectorItems.get(index).getSeparator();
            if (separator == 0)
                return false;
            int precededIndex;
            index--;
            switch (separator)
            {
                case '>':
                    return matches (t.getParent(), index);
                case ' ':
                    while (t != null) {
                        if (matches (t.getParent(), index))
                            return true;
                        t = t.getParent();
                    }
                    return false;
                case '~':
                    if (!t.hasParent())
                        return false;
                    precededIndex = t.getParent().getChildren().indexOf (t) - 1;
                    while (precededIndex >= 0) {
                        if (matches (t.getParent().getChildren().get(precededIndex), index))
                            return true;
                        precededIndex--;
                    }
                    return false;
                case '+':
                    if (!t.hasParent())
                        return false;
                    precededIndex = t.getParent().getChildren().indexOf (t) - 1;
                    return precededIndex >= 0 && matches(t.getParent().getChildren().get(precededIndex), index);
                default:
                    return false;
            }
        }
    }

    public int calculateSpecifity() {
        int specifity = 0;
        for (CssSelectorItem item : this.selectorItems)
            specifity += item.getSpecificity();
        return specifity;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (CssSelectorItem item: selectorItems)
            buf.append(item.toString());
        return buf.toString();
    }
}
