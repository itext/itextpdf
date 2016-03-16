package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

import static junit.framework.Assert.*;

public class PdfEncryptionTest {

    public static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/PdfEncryptionTest/";
    public static final String SOURCE_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/PdfEncryptionTest/";

    public static byte[] ownerPassword = "ownerPassword".getBytes();

    @Before
    public void setUp() {
        new File(DEST_FOLDER).mkdirs();
    }

    @Test
    public void encryptAES256() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "AES256Encrypted.pdf";
        String cmpPdf = SOURCE_FOLDER + "cmp_AES256Encrypted.pdf";
        Document doc = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(outPdf));
//        byte[] userPassword = "userPassword".getBytes();
        byte[] userPassword = null;
        pdfWriter.setEncryption(userPassword, ownerPassword, -1852, PdfWriter.ENCRYPTION_AES_256);
        doc.open();
        doc.add(new Paragraph("hello encrypted world"));
        doc.close();
        pdfWriter.close();

        assertNull(new CompareTool().compareByContent(outPdf, cmpPdf, DEST_FOLDER, "diff_"));
    }

    @Test
    public void stampAES256() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "stampAES256.pdf";
        String cmpPdf = SOURCE_FOLDER + "cmp_stampAES256.pdf";
        PdfReader reader = new PdfReader(SOURCE_FOLDER + "AES256EncryptedDocument.pdf", ownerPassword);
        PdfStamper pdfStamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        pdfStamper.close();

        assertNull(new CompareTool().compareByContent(outPdf, cmpPdf, DEST_FOLDER, "diff_"));

    }

    @Test
    public void unethicalStampAES256() throws IOException, DocumentException, InterruptedException {
        String outPdf = DEST_FOLDER + "unethicalStampAES256.pdf";
        String cmpPdf = SOURCE_FOLDER + "cmp_unethicalStampAES256.pdf";
        PdfReader reader = new PdfReader(SOURCE_FOLDER + "AES256EncryptedDocument.pdf");
        PdfReader.unethicalreading = true;
        PdfStamper pdfStamper = new PdfStamper(reader, new FileOutputStream(outPdf));
        pdfStamper.close();

        assertNull(new CompareTool().compareByContent(outPdf, cmpPdf, DEST_FOLDER, "diff_"));
    }
}
