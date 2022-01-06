/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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

/**
 * A text render listener that filters text operations before passing them on to a delegate
 * @since 5.0.1
 */

public class FilteredRenderListener implements RenderListener {

    /** The delegate that will receive the text render operation if the filters all pass */
    private final RenderListener delegate;
    /** The filters to be applied */
    private final RenderFilter[] filters;

    /**
     * Construction
     * @param delegate the delegate {@link RenderListener} that will receive filtered text operations
     * @param filters the filter(s) to apply
     */
    public FilteredRenderListener(RenderListener delegate, RenderFilter... filters) {
        this.delegate = delegate;
        this.filters = filters;
    }

    /**
     * Applies filters, then delegates to the delegate if all filters pass
     * @param renderInfo contains info to render text
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(com.itextpdf.text.pdf.parser.TextRenderInfo)
     */
    public void renderText(TextRenderInfo renderInfo) {
        for (RenderFilter filter : filters) {
            if (!filter.allowText(renderInfo))
                return;
        }
        delegate.renderText(renderInfo);
    }

    /**
     * This class delegates this call
     * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
     */
    public void beginTextBlock() {
        delegate.beginTextBlock();
    }

    /**
     * This class delegates this call
     * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
     */
    public void endTextBlock() {
        delegate.endTextBlock();
    }

    /**
     * Applies filters, then delegates to the delegate if all filters pass
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo)
     * @since 5.0.1
     */
    public void renderImage(ImageRenderInfo renderInfo) {
        for (RenderFilter filter : filters) {
            if (!filter.allowImage(renderInfo))
                return;
        }
        delegate.renderImage(renderInfo);
    }

}
