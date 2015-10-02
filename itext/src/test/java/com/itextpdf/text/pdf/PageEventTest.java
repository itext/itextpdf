package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PageEventTest {
    private static String CMP_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/PageEventTest/";
    private static String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/PageEventTest/";

    @BeforeClass
    public static void init() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void pageEventTest01() throws IOException, DocumentException, InterruptedException {
        String fileName = "pageEventTest01.pdf";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document(PageSize.LETTER, 144, 144, 144, 144);
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setPageEvent(new MyEventHandler());

        writer.setTagged();
        doc.open();

        Chunk c = new Chunk("This is page 1");
        doc.add(c);

        doc.close();

        FileOutputStream fos = new FileOutputStream(OUTPUT_FOLDER + fileName);
        fos.write(baos.toByteArray());
        baos.close();
        fos.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + fileName, CMP_FOLDER + fileName, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }


    private class MyEventHandler extends PdfPageEventHelper {
        private PdfPTable _header;
        private PdfPTable _footer;

        public MyEventHandler() throws DocumentException {
            _header = new PdfPTable(1);
            PdfPCell hCell = new PdfPCell(new Phrase("HEADER"));
            hCell.setBorder(Rectangle.NO_BORDER);
            _header.addCell(hCell);
            _header.setTotalWidth(new float[]{300f});

            _footer = new PdfPTable(1);
            PdfPCell fCell = new PdfPCell(new Phrase("FOOTER"));
            fCell.setBorder(Rectangle.NO_BORDER);
            _footer.addCell(fCell);
            _footer.setTotalWidth(new float[]{300f});
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            super.onStartPage(writer, document);
            writeHeader(writer);
        }

        public void onEndPage(PdfWriter writer, Document document) {
            super.onEndPage(writer, document);
            writeFooter(writer);
        }

        private void writeHeader(PdfWriter writer) {
            writer.getDirectContent().saveState();
            _header.writeSelectedRows(0, _header.getRows().size(), 72, writer.getPageSize().getHeight() - 72, writer.getDirectContent());
            writer.getDirectContent().restoreState();
        }

        private void writeFooter(PdfWriter writer) {
            writer.getDirectContent().saveState();
            _footer.writeSelectedRows(0, _footer.getRows().size(), 72, 72, writer.getDirectContent());
            writer.getDirectContent().restoreState();
        }
    }
}
