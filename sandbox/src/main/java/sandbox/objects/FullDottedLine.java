/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/20233630/itextsharp-how-to-add-a-full-line-break
 * 
 * We create a Chunk and add a background color.
 */
package sandbox.objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import sandbox.WrapToTest;

@WrapToTest
public class FullDottedLine {
    public static final String DEST = "results/objects/full_dotted_line.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new FullDottedLine().createPdf(DEST);
    }
    
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        document.add(new Paragraph("Before dotted line"));
        DottedLineSeparator separator = new DottedLineSeparator();
        separator.setPercentage(59500f / 523f);
        Chunk linebreak = new Chunk(separator);
        document.add(linebreak);
        document.add(new Paragraph("After dotted line"));
        document.close();
    }
}
