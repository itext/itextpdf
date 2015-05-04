/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpGraphicsState.LineDashPattern;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Stores parameters related to specific context which is either page or xobject
 * (in other words: which is the object having contents stream)
 */
class PdfCleanUpContext {

    private PdfDictionary resources;
    private PdfContentByte canvas;

    private Deque<PdfCleanUpGraphicsState> graphicsStateStack = new ArrayDeque<PdfCleanUpGraphicsState>();

    public PdfCleanUpContext(PdfDictionary resources, PdfContentByte canvas) {
        this.resources = resources;
        this.canvas = canvas;

        graphicsStateStack.push(new PdfCleanUpGraphicsState());
    }

    public PdfDictionary getResources() {
        return resources;
    }

    public void setResources(PdfDictionary resources) {
        this.resources = resources;
    }

    public PdfContentByte getCanvas() {
        return canvas;
    }

    public void setCanvas(PdfContentByte canvas) {
        this.canvas = canvas;
    }

    public float getFontSize() {
        return graphicsStateStack.peek().getFontSize();
    }

    public void setFontSize(float fontSize) {
        graphicsStateStack.peek().setFontSize(fontSize);
    }

    public float getHorizontalScaling() {
        return graphicsStateStack.peek().getHorizontalScaling();
    }

    public void setHorizontalScaling(float horizontalScaling) {
        graphicsStateStack.peek().setHorizontalScaling(horizontalScaling);
    }

    public float getCharacterSpacing() {
        return graphicsStateStack.peek().getCharacterSpacing();
    }

    public void setCharacterSpacing(float characterSpacing) {
        graphicsStateStack.peek().setCharacterSpacing(characterSpacing);
    }

    public float getWordSpacing() {
        return graphicsStateStack.peek().getWordSpacing();
    }

    public void setWordSpacing(float wordSpacing) {
        graphicsStateStack.peek().setWordSpacing(wordSpacing);
    }

    public float getLineWidth() {
        return graphicsStateStack.peek().getLineWidth();
    }

    public void setLineWidth(float lineWidth) {
        graphicsStateStack.peek().setLineWidth(lineWidth);
    }

    public int getLineCapStyle() {
        return graphicsStateStack.peek().getLineCapStyle();
    }

    public void setLineCapStyle(int lineCapStyle) {
        graphicsStateStack.peek().setLineCapStyle(lineCapStyle);
    }

    public int getLineJoinStyle() {
        return graphicsStateStack.peek().getLineJoinStyle();
    }

    public void setLineJoinStyle(int lineJoinStyle) {
        graphicsStateStack.peek().setLineJoinStyle(lineJoinStyle);
    }

    public float getMiterLimit() {
        return graphicsStateStack.peek().getMiterLimit();
    }

    public void setMiterLimit(float miterLimit) {
        graphicsStateStack.peek().setMiterLimit(miterLimit);
    }

    /**
     * @return {@link LineDashPattern} object, describing the dash pattern which should be applied.
     *         If no pattern should be applied (i.e. solid line), then returns <CODE>null</CODE>.
     */
    public LineDashPattern getLineDashPattern() {
        return graphicsStateStack.peek().getLineDashPattern();
    }

    public void setLineDashPattern(LineDashPattern lineDashPattern) {
        graphicsStateStack.peek().setLineDashPattern(lineDashPattern);
    }


    public void saveGraphicsState() {
        graphicsStateStack.push(new PdfCleanUpGraphicsState(graphicsStateStack.peek()));
    }

    public void restoreGraphicsState() {
        graphicsStateStack.pop();
    }
}