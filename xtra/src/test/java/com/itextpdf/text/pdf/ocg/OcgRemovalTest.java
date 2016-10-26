package com.itextpdf.text.pdf.ocg;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class OcgRemovalTest {

    private static final String INPUT = "./src/test/resources/com/itextpdf/text/pdf/ocg/Example.pdf";
    private static final String OUTPUT = "./src/test/resources/com/itextpdf/text/pdf/ocg/Example-out.pdf";

    @Test
    public void removeOcgLayer() throws IOException, DocumentException {
        PdfReader reader = new PdfReader(INPUT);
        OCGRemover ocgRemover = new OCGRemover();
        ocgRemover.removeLayers(reader, "ecc.pricebutton");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(OUTPUT));
        stamper.close();
        reader.close();
    }
}