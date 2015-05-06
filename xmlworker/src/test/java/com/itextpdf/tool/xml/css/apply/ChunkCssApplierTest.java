/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
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
package com.itextpdf.tool.xml.css.apply;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ChunkCssApplierTest {
    private Tag t;
    private Chunk c;
    private final ChunkCssApplier applier = new ChunkCssApplier();

    @Before
    public void setup() {
        LoggerFactory.getInstance().setLogger(new SysoLogger(3));
        t = new Tag(null);
        t.getCSS().put("color", "#000000");
        t.getCSS().put("font-family", "Helvetica");
        t.getCSS().put("font-size", "12pt");
        c = new Chunk("default text for chunk creation");
        applier.apply(c, t);
    }

    @Test
    public void resolveFontFamily() throws IOException {
        assertEquals("Helvetica", c.getFont().getFamilyname());
//		t.getCSS().put("font-family", "Verdana");
//		c = new ChunkCssApplier().apply(c, t);
//		assertEquals("Verdana", c.getFont().getFamilyname()); Where can i retrieve the familyname of the font?
    }

    @Test
    public void resolveFontSize() throws IOException {
        assertEquals(12, c.getFont().getSize(), 0);
        t.getCSS().put("font-size", "18pt");
        c = applier.apply(c, t);
        assertEquals(18, c.getFont().getSize(), 0);
    }

    @Test
    public void resolveCharacterSpacing() throws IOException {
        assertEquals(0, c.getCharacterSpacing(), 0);
        t.getCSS().put("letter-spacing", "15pt");
        c = applier.apply(c, t);
        assertEquals(15, c.getCharacterSpacing(), 0);
    }

    @Test
    public void resolveHorizontalAndVerticalScaling() throws IOException {
        t.getCSS().put("xfa-font-vertical-scale", "75pt");
        assertEquals(1, c.getHorizontalScaling(), 0);
        t.getCSS().put("xfa-font-horizontal-scale", "75%");
        c = applier.apply(c, t);
        assertEquals(0.75f, c.getHorizontalScaling(), 0);
        t.getCSS().put("xfa-font-vertical-scale", "75%");
        c = applier.apply(c, t);
        assertEquals(9, c.getFont().getSize(), 0);
        assertEquals(100 / 75f, c.getHorizontalScaling(), 0);
    }

    @Test
    public void resolveColor() throws IOException {
        assertEquals(BaseColor.BLACK, c.getFont().getColor());
        t.getCSS().put("color", "#00f");
        c = applier.apply(c, t);
        assertEquals(255, c.getFont().getColor().getBlue(), 0);
        t.getCSS().put("color", "#00ff00");
        c = applier.apply(c, t);
        assertEquals(255, c.getFont().getColor().getGreen(), 0);
        t.getCSS().put("color", "rgb(255,0,0)");
        c = applier.apply(c, t);
        assertEquals(255, c.getFont().getColor().getRed(), 0);
    }

    @Test
    public void resolveVerticalAlign() throws IOException {
        assertEquals(0, c.getTextRise(), 0);
        t.getCSS().put("vertical-align", "5pt");
        c = applier.apply(c, t);
        assertEquals(5, c.getTextRise(), 0);

        t.getCSS().put("vertical-align", "sub");
        c = applier.apply(c, t);
        assertEquals(12, c.getFont().getSize(), 0);
        assertEquals(-c.getFont().getSize() / 2, c.getTextRise(), 0);

        t.getCSS().put("vertical-align", "super");
        c = applier.apply(c, t);
        assertEquals(12, c.getFont().getSize(), 0);
        assertEquals(c.getFont().getSize() / 2 + 0.5, c.getTextRise(), 0);
    }
}
