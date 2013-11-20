package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class PdfWriterTest {

    public static final String IMG =  "./src/test/resources/com/itextpdf/text/pdf/PdfWriterTest/img.png";

    @Test
    @Ignore
    public void test1() throws IOException, DocumentException {
        String output = "Test.pdf";
        Document pdfDoc = new Document(PageSize.A4);
        PdfWriter pdfWriter = PdfWriter.getInstance(pdfDoc, new FileOutputStream(output));
        pdfWriter.setPdfVersion(PdfWriter.VERSION_1_5);

        pdfDoc.open();
        PdfPTable table = new PdfPTable(1);
        Image img = Image.getInstance(IMG);
        PdfPCell cell = new PdfPCell(img, false);
        table.addCell(cell);
        pdfDoc.add(table);

        pdfWriter.setFullCompression();

        pdfDoc.close();


        PdfReader reader = new PdfReader(output);
        PdfDictionary page = reader.getPageN(1);
        PdfDictionary resources = page.getAsDict(PdfName.RESOURCES);
        PdfDictionary xObject = resources.getAsDict(PdfName.XOBJECT);
        Assert.assertTrue("Lost image", xObject.size() > 0);
    }
}
