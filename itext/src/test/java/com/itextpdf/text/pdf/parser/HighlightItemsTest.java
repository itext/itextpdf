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
