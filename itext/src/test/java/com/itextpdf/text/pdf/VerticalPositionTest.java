package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.*;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VerticalPositionTest {
    private static String CMP_FOLDER ="./src/test/resources/com/itextpdf/text/pdf/VerticalPositionTest/";
    private static String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/VerticalPositionTest/";

    @BeforeClass
    public static void init() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void verticalPositionTest() throws IOException, DocumentException, InterruptedException {
        String file = "vertical_position.pdf";

        Document document = new Document();
        final PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + file));
        document.open();

        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                Rectangle pageSize = writer.getPageSize();
                float verticalPosition = writer.getVerticalPosition(false);
                PdfContentByte canvas = writer.getDirectContent();
                Rectangle rect = new Rectangle(0, verticalPosition, pageSize.getRight(), pageSize.getTop());
                rect.setBorder(Rectangle.BOX);
                rect.setBorderWidth(1);
                rect.setBorderColor(BaseColor.BLUE);
                canvas.rectangle(rect);
            }
        });

        PdfPTable table = new PdfPTable(2);
        for (int i = 0; i < 100; i++) {
            table.addCell("Hello " + i);
            table.addCell("World " + i);
        }

        document.add(table);

        document.newPage();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("some more text ");
        }
        document.add(new Paragraph(sb.toString()));

        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}
