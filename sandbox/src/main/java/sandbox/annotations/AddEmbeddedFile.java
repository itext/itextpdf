/*
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/26648462/how-to-delete-attachment-of-pdf-using-itext
 * This is part one, there's also a part two named RemoveEmbeddedFile (or RemoveEmbeddedFiles)
 */
package sandbox.annotations;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddEmbeddedFile {

    public static final String SRC = "resources/pdfs/hello.pdf";
    public static final String DEST = "results/annotations/hello_with_attachment.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new AddEmbeddedFile().manipulatePdf(SRC, DEST);
    }
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(
                stamper.getWriter(), null, "test.txt", "Some test".getBytes());
        stamper.addFileAttachment("some test file", fs);
        stamper.close();
    }
}
