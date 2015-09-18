package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfAFontEmbeddingTest {
    @Test
    public void testNotoFont() throws DocumentException, IOException {
        Document document = new Document();
        PdfAWriter writer = PdfAWriter.getInstance(document, new ByteArrayOutputStream(), PdfAConformanceLevel.PDF_A_1B);
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
    }
}
