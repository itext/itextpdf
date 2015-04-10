package sandbox.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import sandbox.WrapToTest;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.OutputStreamCounter;
import com.itextpdf.text.pdf.PdfWriter;

@WrapToTest
public class PdfWithComments {
    public static final String DEST = "results/logging/pdf_comments.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PdfWithComments().createPdf(DEST);
    }

    public void createPdf(String filename) throws DocumentException, IOException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        // step 3
        document.open();
        OutputStreamCounter os = writer.getOs();
        os.write("% After open\n".getBytes());
        // step 4
        document.add(new Paragraph("Hello World!"));
        document.newPage();
        // step 5
        os.write("% Before close\n".getBytes());
        document.close();
    }
}
