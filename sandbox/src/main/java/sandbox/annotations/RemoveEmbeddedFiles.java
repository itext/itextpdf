/*
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/26648462/how-to-delete-attachment-of-pdf-using-itext
 * (This is part two, there's also a part one named AddEmbeddedFile)
 */
package sandbox.annotations;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author iText
 */
public class RemoveEmbeddedFiles {

    public static final String SRC = "resources/pdfs/hello_with_attachment.pdf";
    public static final String DEST = "results/annotations/hello_without_attachments.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new RemoveEmbeddedFiles().manipulatePdf(SRC, DEST);
    }
    public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src);
        PdfDictionary root = reader.getCatalog();
        PdfDictionary names = root.getAsDict(PdfName.NAMES);
        names.remove(PdfName.EMBEDDEDFILES);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.close();
    }
}
