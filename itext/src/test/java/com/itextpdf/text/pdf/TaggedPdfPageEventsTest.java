package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.Assert;

public class TaggedPdfPageEventsTest extends PdfPageEventHelper {

    private static String CMP_FOLDER ="./src/test/resources/com/itextpdf/text/pdf/TaggedPdfPageEventsTest/";
    private static String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/TaggedPdfPageEventsTest/";

    @BeforeClass
    public static void init() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void test() throws IOException, DocumentException, InterruptedException {
        String file = "tagged_pdf_page_events.pdf";

        final Document document = new Document();

        final PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + file));

        pdfWriter.setTagged();
        pdfWriter.setPageEvent(new TaggedPdfPageEventsTest());

        document.open();

        document.add(new Paragraph("Hello"));
        document.newPage();
        document.add(new Paragraph("World"));

        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        final PdfPTable headerTable = new PdfPTable(1);
        headerTable.addCell(new Phrase("Header"));
        try {
            writeTable(writer.getDirectContent(), headerTable, new Rectangle(0, document.getPageSize().getHeight()-50f, document.getPageSize().getWidth(), document.getPageSize().getHeight()));
        } catch (DocumentException e) {
            e.printStackTrace(); // TODO catch exception properly
        }
    }

    @Override
    public void onEndPage(final PdfWriter writer, final Document document) {
        final PdfPTable footerTable = new PdfPTable(1);
        footerTable.addCell(new Phrase("Footer"));
        try {
            writeTable(writer.getDirectContent(), footerTable, new Rectangle(0, 0, document.getPageSize().getWidth(), 50f));
        } catch (DocumentException e) {
            e.printStackTrace(); // TODO catch exception properly
        }
    }

    private void writeTable(final PdfContentByte directContent, final PdfPTable table, final Rectangle coordinates) throws DocumentException {
        final ColumnText columnText = new ColumnText(directContent);
        columnText.setSimpleColumn(coordinates);
        columnText.addElement(table);
        columnText.go();
    }

}
