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

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ParagraphCssApplierTest {
    /**
     *
     */
	static {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
	}
    private static final FontSizeTranslator fst = FontSizeTranslator.getInstance();
    private Tag parent;
    private Tag first;
    private Tag second;
    private Tag child;
    private Paragraph firstPara;
    private Paragraph secondPara;
	private final ParagraphCssApplier applier = new ParagraphCssApplier(new CssAppliersImpl());
    private HtmlPipelineContext configuration;

    @Before
    public void setup() {
        parent = new Tag("body");
        parent.getCSS().put(CSS.Property.FONT_FAMILY, "Helvetica");
        parent.getCSS().put(CSS.Property.FONT_SIZE, "12pt");
        first = new Tag(null);
        first.getCSS().put(CSS.Property.FONT_FAMILY, "Helvetica");
        first.getCSS().put(CSS.Property.FONT_SIZE, "12pt");
        second = new Tag(null);
        second.getCSS().put(CSS.Property.FONT_FAMILY, "Helvetica");
        second.getCSS().put(CSS.Property.FONT_SIZE, "12pt");
        child = new Tag(null);
        child.getCSS().put(CSS.Property.FONT_FAMILY, "Helvetica");
        child.getCSS().put(CSS.Property.FONT_SIZE, "12pt");

        parent.addChild(first);
        first.setParent(parent);
        second.setParent(parent);
        first.addChild(child);
        second.addChild(child);
        parent.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(parent) + "pt");
        first.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(first) + "pt");
        first.getCSS().put(CSS.Property.TEXT_ALIGN, CSS.Value.LEFT);
        second.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(second) + "pt");
        child.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(child) + "pt");
        firstPara = new Paragraph(new Chunk("default text for chunk creation"));
        secondPara = new Paragraph(new Chunk("default text for chunk creation"));
        configuration = new HtmlPipelineContext(null);
        applier.apply(firstPara, first, configuration);
    }

    @Test
    public void resolveAlignment() throws IOException {
        assertEquals(Element.ALIGN_LEFT, firstPara.getAlignment(), 0);

        first.getCSS().put("text-align", "right");
        applier.apply(firstPara, first, configuration);
        assertEquals(Element.ALIGN_RIGHT, firstPara.getAlignment(), 0);

        first.getCSS().put("text-align", "left");
        applier.apply(firstPara, first, configuration);
        assertEquals(Element.ALIGN_LEFT, firstPara.getAlignment(), 0);

        first.getCSS().put("text-align", "center");
        applier.apply(firstPara, first, configuration);
        assertEquals(Element.ALIGN_CENTER, firstPara.getAlignment(), 0);
    }

    @Test
    public void resolveFirstLineIndent() throws IOException {
        assertEquals(0f, firstPara.getFirstLineIndent(), 0);

        first.getCSS().put("text-indent", "16pt");
        applier.apply(firstPara, first, configuration);
        assertEquals(16, firstPara.getFirstLineIndent(), 0);
    }

    @Test
    public void resolveIndentationLeft() throws IOException {
        assertEquals(0f, firstPara.getIndentationLeft(), 0);

        first.getCSS().put("margin-left", "10pt");
        applier.apply(firstPara, first, configuration);
        assertEquals(10, firstPara.getIndentationLeft(), 0);
    }

    @Test
    public void resolveIndentationRight() throws IOException {
        assertEquals(0f, firstPara.getIndentationRight(), 0);

        first.getCSS().put("margin-right", "10pt");
        applier.apply(firstPara, first, configuration);
        assertEquals(10, firstPara.getIndentationRight(), 0);
    }

    @Test
    @Ignore//We need possibility to detect that line-height undefined;
    public void resolveLeading() throws IOException {
        assertEquals(18f, firstPara.getLeading(), 0);

        first.getCSS().put("line-height", "25pt");
        applier.apply(firstPara, first, configuration);
        assertEquals(25, firstPara.getLeading(), 0);

        child.getCSS().put("line-height", "19pt");
        applier.apply(firstPara, first, configuration);
        assertEquals(25, firstPara.getLeading(), 0);

        child.getCSS().put("line-height", "30pt");
        applier.apply(firstPara, first, configuration);
        assertEquals(30, firstPara.getLeading(), 0);
    }

    @Test
    public void resolveSpacingAfter() throws IOException {
        assertEquals(0, firstPara.getSpacingBefore(), 0);
        second.getCSS().put("margin-bottom", "25pt");

        applier.apply(secondPara, second, configuration);
        assertEquals(25, secondPara.getSpacingAfter(), 0);
    }

    @Test
    public void resolveSpacingBeforeIs10() throws IOException {
        parent.addChild(second);
        first.getCSS().put("margin-bottom", "12pt");
        second.getCSS().put("margin-top", "22pt");

        applier.apply(firstPara, first, configuration);
        applier.apply(secondPara, second, configuration);
        assertEquals(22 - 12, secondPara.getSpacingBefore(), 0);
    }

    @Test
    public void resolveSpacingBeforeIs5() throws IOException {
        parent.addChild(second);
        first.getCSS().put("margin-bottom", "25pt");
        second.getCSS().put("margin-top", "30pt");

        applier.apply(firstPara, first, configuration);
        applier.apply(secondPara, second, configuration);
        assertEquals(30 - 25, secondPara.getSpacingBefore(), 0);
    }

    @Test
    public void resolveSpacingBeforeIs0() throws IOException {
        parent.addChild(second);
        first.getCSS().put("margin-bottom", "35pt");
        second.getCSS().put("margin-top", "30pt");

        applier.apply(firstPara, first, configuration);
        applier.apply(secondPara, second, configuration);
        //30-35 is reverted to 0.
        assertEquals(0, secondPara.getSpacingBefore(), 0);
    }

    @Test
    public void resolveSpacingBeforeIs6() throws IOException {
        parent.addChild(second);
        first.getCSS().put("margin-bottom", "2em");
        second.getCSS().put("margin-top", "30pt");

        applier.apply(firstPara, first, configuration);
        applier.apply(secondPara, second, configuration);
        assertEquals(30 - (2 * 12), secondPara.getSpacingBefore(), 0);
    }

    @Test
    public void resolveSpacingBeforeIs24() throws IOException {
        parent.addChild(second);
        first.getCSS().put("margin-bottom", "2em");
        first.getCSS().put(CSS.Property.FONT_SIZE, "18");
        first.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(first) + "pt");
        second.getCSS().put("margin-top", "60pt");

        applier.apply(firstPara, first, configuration);
        applier.apply(secondPara, second, configuration);
        // 60 - 2 * (18px = 13.5pt)
        assertEquals(60 - (2 * 13.5f), secondPara.getSpacingBefore(), 0);
    }

    @Test
    public void resolveSpacingBeforeIs12() throws IOException {
        parent.addChild(second);
        first.getCSS().put("margin-bottom", "2em");
        first.getCSS().put(CSS.Property.FONT_SIZE, "2em");
        first.getCSS().put(CSS.Property.FONT_SIZE, fst.translateFontSize(first) + "pt");
        second.getCSS().put("margin-top", "60pt");

        applier.apply(firstPara, first, configuration);
        applier.apply(secondPara, second, configuration);
        assertEquals(60 - (2 * 12 * 2), secondPara.getSpacingBefore(), 0);
    }
}
