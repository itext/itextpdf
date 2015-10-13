package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

public class PdfAFontEmbeddingTest {

    protected static final String outputDir = "./target/test/fontembedding/";

    @BeforeClass
    public static void setup(){
        new File(outputDir).mkdirs();
    }


    @Test
    public void testNotoFont() throws DocumentException, IOException, InterruptedException {
        Document document = new Document();
        String filename = outputDir + "testNotoFont.pdf";
        PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(filename), PdfAConformanceLevel.PDF_A_1B);
        writer.createXmpMetadata();
        document.open();

        String fontPath = "./src/test/resources/com/itextpdf/text/pdf/NotoSansCJKjp-Bold.otf";
        BaseFont bf = BaseFont.createFont(fontPath, "Identity-H", true);
        Font font = new Font(bf, 14);
        String[] lines = new String[] {"Noto test", "in japanese:", "\u713C"};

        for (String line: lines) {
            document.add(new Paragraph(line, font));
        }

        ICC_Profile icc = ICC_Profile.getInstance(new FileInputStream("./src/test/resources/com/itextpdf/text/pdf/sRGB Color Space Profile.icm"));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);

        document.close();

        String cmpFile = "./src/test/resources/com/itextpdf/text/pdf/fontembedding/cmp_testNotoFont.pdf";

        new CompareTool().compareByContent(filename, cmpFile, outputDir, "diff");
    }
}
