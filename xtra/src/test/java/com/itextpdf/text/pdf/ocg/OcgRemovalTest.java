package com.itextpdf.text.pdf.ocg;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;

public class OcgRemovalTest {

    private static final String INPUT = "./src/test/resources/com/itextpdf/text/pdf/ocg/peek-a-boo2.pdf";

    @Test
    public void removeOcgLayer() throws IOException, DocumentException {
        PdfReader reader = new PdfReader(INPUT);
        OCGRemover ocgRemover = new OCGRemover();
        ocgRemover.removeLayers(reader, "Do you see me?");
        PdfDictionary catalog = reader.getCatalog();
        Assert.assertNull(catalog.get(PdfName.OCPROPERTIES));
        Assert.assertNotSame(PdfName.USEOC, catalog.get(PdfName.PAGEMODE));
        reader.close();
    }
}