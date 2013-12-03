/**
 * Example written by Bruno Lowagie in answer to:
 * http://thread.gmane.org/gmane.comp.java.lib.itext.general/65892
 * 
 * Some text displayed using a Small Caps font.
 */
package sandbox.fonts;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

public class SmallCapsExample {
    public static void main(String[] args) throws IOException,
        DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(
            "results/small_caps.pdf"));
        document.open();
        BaseFont bf = BaseFont.createFont("resources/fonts/Delicious-SmallCaps.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font f = new Font(bf, 12);
        Paragraph p = new Paragraph("This is some text displayed using a Small Caps font.", f);
        document.add(p);
        document.close();
    }
}
