/**
 * Example written by Bruno Lowagie in answer to:
 * http://stackoverflow.com/questions/20233630/itextsharp-how-to-add-a-full-line-break
 * 
 * We create a Chunk and add a background color.
 */
package sandbox.objects;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

public class FullDottedLine {
    public static void main(String[] args) throws IOException,
        DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(
            "results/full_dotted_line.pdf"));
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
