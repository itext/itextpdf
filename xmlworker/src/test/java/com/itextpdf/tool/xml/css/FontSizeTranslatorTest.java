/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.css;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.apply.ChunkCssApplier;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * @author Emiel Ackermann
 */
public class FontSizeTranslatorTest {

    private final FontSizeTranslator fst = FontSizeTranslator.getInstance();
    Tag p = new Tag("p");
    Tag span = new Tag("span");

    @Before
    public void before() {
        LoggerFactory.getInstance().setLogger(new SysoLogger(3));
        p.getCSS().put(CSS.Property.FONT_FAMILY, "Helvetica");
        p.getCSS().put(CSS.Property.FONT_SIZE, "12pt");
        p.addChild(span);
        span.setParent(p);
    }

    @Test
    public void resolveXXAndXSmall() throws IOException {
        p.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.XX_SMALL);
        p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p) + "pt");
        Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
        assertEquals(6.75f, c1.getFont().getSize(), 0);

        span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.X_SMALL);
        span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span) + "pt");
        Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
        assertEquals(7.5f, c2.getFont().getSize(), 0);
    }

    @Test
    public void resolveSmallAndMedium() throws IOException {
        p.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.SMALL);
        p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p) + "pt");
        Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
        assertEquals(9.75f, c1.getFont().getSize(), 0);

        span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.MEDIUM);
        span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span) + "pt");
        Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
        assertEquals(12f, c2.getFont().getSize(), 0);
    }

    @Test
    public void resolveLargeXLargeXXLarge() throws IOException {
        p.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.LARGE);
        p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p) + "pt");
        Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
        assertEquals(13.5f, c1.getFont().getSize(), 0);

        span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.X_LARGE);
        span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span) + "pt");
        Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
        assertEquals(18f, c2.getFont().getSize(), 0);

        p.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.XX_LARGE);
        p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p) + "pt");
        Chunk c3 = new ChunkCssApplier().apply(new Chunk("Text after span."), p);
        assertEquals(24f, c3.getFont().getSize(), 0);
    }

    @Test
    public void resolveDefaultToSmaller() throws IOException {
        p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p) + "pt");
        Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
        assertEquals(12, c1.getFont().getSize(), 0);

        span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.SMALLER);
        span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span) + "pt");
        Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
        assertEquals(9.75f, c2.getFont().getSize(), 0);
    }

    @Test
    public void resolveDefaultToLarger() throws IOException {
        p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p) + "pt");
        Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
        assertEquals(12, c1.getFont().getSize(), 0);

        span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.LARGER);
        span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span) + "pt");
        Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
        assertEquals(13.5f, c2.getFont().getSize(), 0);
    }

    @Test
    public void resolve15ToSmaller() throws IOException {
        p.getCSS().put(CSS.Property.FONT_SIZE, "15pt");
        p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p) + "pt");
        Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
        assertEquals(15, c1.getFont().getSize(), 0);

        span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.SMALLER);
        span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span) + "pt");
        Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
        assertEquals(15 * 0.85f, c2.getFont().getSize(), 0);
    }

    @Test
    public void resolve15ToLarger() throws IOException {
        p.getCSS().put(CSS.Property.FONT_SIZE, "15pt");
        p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p) + "pt");
        Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
        assertEquals(15, c1.getFont().getSize(), 0);

        span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.LARGER);
        span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span) + "pt");
        Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
        assertEquals(15 * 1.5f, c2.getFont().getSize(), 0);
    }

    @Test
    public void resolve34ToSmaller() throws IOException {
        p.getCSS().put(CSS.Property.FONT_SIZE, "34pt");
        p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p) + "pt");
        Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
        assertEquals(34, c1.getFont().getSize(), 0);

        span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.SMALLER);
        span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span) + "pt");
        Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
        assertEquals(34 * 2 / 3f, c2.getFont().getSize(), 0);
    }

    @Test
    public void resolve34ToLarger() throws IOException {
        p.getCSS().put(CSS.Property.FONT_SIZE, "34pt");
        p.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(p) + "pt");
        Chunk c1 = new ChunkCssApplier().apply(new Chunk("Text before span "), p);
        assertEquals(34, c1.getFont().getSize(), 0);

        span.getCSS().put(CSS.Property.FONT_SIZE, CSS.Value.LARGER);
        span.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(span) + "pt");
        Chunk c2 = new ChunkCssApplier().apply(new Chunk("text in span "), span);
        assertEquals(34 * 1.5f, c2.getFont().getSize(), 0);
    }
}
