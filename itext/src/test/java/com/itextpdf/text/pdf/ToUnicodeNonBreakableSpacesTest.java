package com.itextpdf.text.pdf;

import com.itextpdf.text.*;
import org.junit.Before;
import org.junit.Test;

import java.io.FileOutputStream;

/**
 *
 */
public class ToUnicodeNonBreakableSpacesTest {

    private BaseFont fontWithToUnicode;

    @Before
    public void setUp() throws Exception {
        PdfReader reader = new PdfReader(
            ToUnicodeNonBreakableSpacesTest.class.getResourceAsStream("/com/itextpdf/text/pdf/ToUnicodeNonBreakableSpacesTest/fontWithToUnicode.pdf"));
        PdfDictionary resourcesDict = reader.getPageResources(1);
        PdfDictionary fontsDict = resourcesDict.getAsDict(PdfName.FONT);
        for (PdfName key : fontsDict.getKeys()) {
            PdfObject pdfFont = fontsDict.get(key);

            if (pdfFont instanceof PRIndirectReference) {
                fontWithToUnicode = BaseFont.createFont((PRIndirectReference) pdfFont);
                break;
            }
        }
    }

    @Test
    public void writeTextWithWordSpacing() throws Exception {
        Document document = new Document();
        FileOutputStream out = new FileOutputStream("target/test-classes/com/itextpdf/text/pdf/ToUnicodeNonBreakableSpacesTest/textWithWorldSpacing.pdf");
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        document.setPageSize(PageSize.A4);
        document.newPage();
        writer.getDirectContent().setWordSpacing(10);
        ColumnText columnText = new ColumnText(writer.getDirectContent());
        columnText.setSimpleColumn(new Rectangle(30, 0, document.getPageSize().getRight(), document.getPageSize().getTop() - 30));
        columnText.setUseAscender(true);
        columnText.addText(new Chunk("H H H H H H H H H  !", new Font(fontWithToUnicode, 30)));
        columnText.go();
        document.close();
    }
}
