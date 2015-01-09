package com.itextpdf.text.pdf;

import com.itextpdf.testutils.ITextTest;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.qrcode.EncodeHintType;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

public class BarcodeUnicodeTest extends ITextTest {

    private static final String OUT_DIR = "./target/com/itextpdf/test/pdf/BarcodeUnicodeTest/";

    @Before
    public void setUp() {
        new File(OUT_DIR).mkdirs();
    }

    @Override
    protected void makePdf(String outPdf) throws Exception {
        // step 1
        Document document = new Document(new Rectangle(340, 842));
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPdf));
        // step 3
        document.open();
        // step 4
        PdfContentByte cb = writer.getDirectContent();

        String str = "\u6D4B";

        document.add(new Paragraph("QR code unicode"));
        BarcodeQRCode q = new BarcodeQRCode(str, 100, 100, new HashMap<EncodeHintType, Object>() {{put(EncodeHintType.CHARACTER_SET, "UTF-8");}});
        document.add(q.getImage());

        // step 5
        document.close();
    }

    @Test
    public void test() throws Exception {
        runTest();
    }

    @Override
    protected String getOutPdf() {
        return OUT_DIR + "barcode.pdf";
    }
}
