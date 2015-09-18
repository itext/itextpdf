package com.itextpdf.text.pdf.fonts;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FontEmbeddingTest {

    private static String srcFolder ="./src/test/resources/com/itextpdf/text/pdf/fonts/FontEmbeddingTest/";

    @Test
    public void testNotoFont() throws DocumentException, IOException {
        Document document = new Document();
        PdfWriter.getInstance(document, new ByteArrayOutputStream());
        document.open();
        String fontPath = srcFolder + "NotoSansCJKjp-Bold.otf";
        BaseFont bf = BaseFont.createFont(fontPath, "Identity-H", true);
        Font font = new Font(bf, 14);
        String[] lines = new String[] {"Noto test", "in japanese:", "\u713C"};

        for (String line: lines) {
            document.add(new Paragraph(line, font));
        }
        document.add(Chunk.NEWLINE);
        document.close();
    }
}
