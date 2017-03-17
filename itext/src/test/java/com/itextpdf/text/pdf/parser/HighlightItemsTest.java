/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.parser;


import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfChunk;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HighlightItemsTest {

    static final private String outputPath = "./target/com/itextpdf/text/pdf/parser/HighlightItemsTest/";
    static final private String inputPath = "./src/test/resources/com/itextpdf/text/pdf/parser/HighlightItemsTest/";

    @Before
    public void before() {
        new File(outputPath).mkdirs();
    }

    @Test
    public void highlightPage229() throws IOException, DocumentException, InterruptedException, URISyntaxException {
        String input = inputPath + "page229.pdf";
        String output = outputPath + "page229.pdf";
        String cmp = inputPath + "cmp_page229.pdf";
        parseAndHighlight(input, output, false);
        Assert.assertEquals(null, new CompareTool().compareByContent(output, cmp, outputPath, "diff"));
    }

    @Test
    public void highlightCharactersPage229() throws IOException, DocumentException, InterruptedException, URISyntaxException {
        String input = inputPath + "page229.pdf";
        String output = outputPath + "page229_characters.pdf";
        String cmp = inputPath + "cmp_page229_characters.pdf";
        parseAndHighlight(input, output, true);
        Assert.assertEquals(null, new CompareTool().compareByContent(output, cmp, outputPath, "diff"));
    }

    @Test
    public void highlightIsoTc171() throws IOException, DocumentException, InterruptedException, URISyntaxException {
        String input = inputPath + "ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda.pdf";
        String output = outputPath + "SC2_N0896_SC2WG5_Edinburgh_Agenda.pdf";
        String cmp = inputPath + "cmp_ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda.pdf";
        parseAndHighlight(input, output, false);
        Assert.assertEquals(null, new CompareTool().compareByContent(output, cmp, outputPath, "diff"));
    }

    @Test
    public void highlightCharactersIsoTc171() throws IOException, DocumentException, InterruptedException, URISyntaxException {
        String input = inputPath + "ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda.pdf";
        String output = outputPath + "ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda_characters.pdf";
        String cmp = inputPath + "cmp_ISO-TC171-SC2_N0896_SC2WG5_Edinburgh_Agenda_characters.pdf";
        parseAndHighlight(input, output, true);
        Assert.assertEquals(null, new CompareTool().compareByContent(output, cmp, outputPath, "diff"));
    }

    @Test
    public void highlightHeaderFooter() throws IOException, DocumentException, InterruptedException, URISyntaxException {
        String input = inputPath + "HeaderFooter.pdf";
        String output = outputPath +  "HeaderFooter.pdf";
        String cmp = inputPath + "cmp_HeaderFooter.pdf";
        parseAndHighlight(input, output, false);
        Assert.assertEquals(null, new CompareTool().compareByContent(output, cmp, outputPath, "diff"));
    }

    @Test
    public void highlightCharactersHeaderFooter() throws IOException, DocumentException, InterruptedException, URISyntaxException {
        String input = inputPath + "HeaderFooter.pdf";
        String output = outputPath +  "HeaderFooter_characters.pdf";
        String cmp = inputPath + "cmp_HeaderFooter_characters.pdf";
        parseAndHighlight(input, output, true);
        Assert.assertEquals(null, new CompareTool().compareByContent(output, cmp, outputPath, "diff"));
    }

    void parseAndHighlight(String input, String output, boolean singleCharacters) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(input);
        FileOutputStream fos = new FileOutputStream(output);
        PdfStamper stamper = new PdfStamper(reader, fos);

        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        MyRenderListener myRenderListener = singleCharacters ? new MyCharacterRenderListener() : new MyRenderListener();
        for (int pageNum = 1; pageNum <= reader.getNumberOfPages(); pageNum++) {
            List<Rectangle> rectangles = parser.processContent(pageNum, myRenderListener).getRectangles();
            PdfContentByte canvas = stamper.getOverContent(pageNum);
            canvas.setLineWidth(0.5f);
            canvas.setColorStroke(BaseColor.RED);
            for (Rectangle rectangle : rectangles) {
                canvas.rectangle(rectangle.getLeft(), rectangle.getBottom(), rectangle.getWidth(), rectangle.getHeight());
                canvas.stroke();
            }
        }
        stamper.close();
        fos.close();
        reader.close();
    }

    static private String getOutputPdfPath(Class<?> c, String inputPdf, String suffix) throws URISyntaxException {
        File f = new File(c.getClassLoader().getResource(TestResourceUtils.getFullyQualifiedResourceName(c, inputPdf)).toURI());
        return f.getAbsolutePath() + suffix;
    }

    static private String getOutputPdfPath(Class<?> c, String inputPdf) throws URISyntaxException {
        File f = new File(c.getClassLoader().getResource(TestResourceUtils.getFullyQualifiedResourceName(c, inputPdf)).toURI());
        return f.getAbsolutePath().replaceAll(inputPdf, "");
    }

    static class MyRenderListener implements RenderListener {

        private List<Rectangle> rectangles = new ArrayList<Rectangle>();

        public void beginTextBlock() {

        }

        public void renderText(TextRenderInfo renderInfo) {
            Vector startPoint = renderInfo.getDescentLine().getStartPoint();
            Vector endPoint = renderInfo.getAscentLine().getEndPoint();
            float x1 = Math.min(startPoint.get(0), endPoint.get(0));
            float x2 = Math.max(startPoint.get(0), endPoint.get(0));
            float y1 = Math.min(startPoint.get(1), endPoint.get(1));
            float y2 = Math.max(startPoint.get(1), endPoint.get(1));
            rectangles.add(new Rectangle(x1, y1, x2, y2));
        }

        public void endTextBlock() {

        }

        public void renderImage(ImageRenderInfo renderInfo) {

        }

        public List<Rectangle> getRectangles() {
            return rectangles;
        }
    }

    static class MyCharacterRenderListener extends MyRenderListener {
        @Override
        public void renderText(TextRenderInfo renderInfo) {
            for (TextRenderInfo tri : renderInfo.getCharacterRenderInfos())
                super.renderText(tri);
        }
    }

}
