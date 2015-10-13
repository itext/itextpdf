package com.itextpdf.text.pdf.fonts;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FontEmbeddingTest {

    private static String srcFolder = "./src/test/resources/com/itextpdf/text/pdf/fonts/";
    private static String outFolder = "./target/com/itextpdf/text/pdf/fonts/FontEmbeddingTest/";

    @BeforeClass
    public static void setUp() throws Exception {
        new File(outFolder).mkdirs();
    }


    @Test
    public void testNotoFont() throws DocumentException, IOException, InterruptedException {
        Document document = new Document();
        String filename = outFolder + "testNotoFont.pdf";
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        String fontPath = srcFolder + "NotoFont/NotoSansCJKjp-Bold.otf";
        BaseFont bf = BaseFont.createFont(fontPath, "Identity-H", true);
        Font font = new Font(bf, 14);
        String[] lines = new String[] {"Noto test", "in japanese:", "\u713C"};

        for (String line: lines) {
            document.add(new Paragraph(line, font));
        }
        document.add(Chunk.NEWLINE);
        document.close();

        String cmpname = srcFolder + "FontEmbeddingTest/cmp_testNotoFont.pdf";

        new CompareTool().compareByContent(filename, cmpname, outFolder, "diff");
    }
}
