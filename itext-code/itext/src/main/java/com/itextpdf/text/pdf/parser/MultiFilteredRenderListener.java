/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Alexey Subach, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.parser;

import java.util.ArrayList;
import java.util.List;

public class MultiFilteredRenderListener implements RenderListener {

    private final List<RenderListener> delegates;
    private final List<RenderFilter[]> filters;

    public MultiFilteredRenderListener() {
        delegates = new ArrayList<RenderListener>();
        filters = new ArrayList<RenderFilter[]>();
    }

    /**
     * Attaches a {@link RenderListener} for the corresponding filter set.
     * @param delegate RenderListener instance to be attached.
     * @param filterSet filter set to be attached. The delegate will be invoked if all the filters pass.
     */
    public <E extends RenderListener> E attachRenderListener(E delegate, RenderFilter... filterSet) {
        delegates.add(delegate);
        filters.add(filterSet);

        return delegate;
    }

    public void beginTextBlock() {
        for (RenderListener delegate : delegates) {
            delegate.beginTextBlock();
        }
    }

    public void renderText(TextRenderInfo renderInfo) {
        for (int i = 0; i < delegates.size(); i++) {
            boolean filtersPassed = true;
            for (RenderFilter filter : filters.get(i)) {
                if (!filter.allowText(renderInfo)) {
                    filtersPassed = false;
                    break;
                }
            }
            if (filtersPassed)
                delegates.get(i).renderText(renderInfo);
        }
    }

    public void endTextBlock() {
        for (RenderListener delegate : delegates) {
            delegate.endTextBlock();
        }
    }

    public void renderImage(ImageRenderInfo renderInfo) {
        for (int i = 0; i < delegates.size(); i++) {
            boolean filtersPassed = true;
            for (RenderFilter filter : filters.get(i)) {
                if (!filter.allowImage(renderInfo)) {
                    filtersPassed = false;
                    break;
                }
            }
            if (filtersPassed)
                delegates.get(i).renderImage(renderInfo);
        }
    }
}
