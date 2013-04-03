package com.itextpdf.text;

import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class RemoteGifImageTest {

    private final String[] GIF_LOCATION = {
            "http://itextpdf.com/img/logo.gif",
            "http://itextsupport.com/files/testresources/img/remote_gif_test.gif",
            "./src/test/resources/com/itextpdf/text/Chunk/logo.gif" // non-remote gif
    };

    @Test
    public void remoteGifTest() throws IOException, DocumentException {
        for (int i = 0; i < GIF_LOCATION.length; i++) {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("gif_remote["+i+"].pdf"));
            document.open();

            Image img = Image.getInstance(GIF_LOCATION[i]);
            document.add(img);

            document.close();
        }
    }
}
